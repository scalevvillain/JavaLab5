
            package ru.university.textbot;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.university.textbot.config.BotConfig;

public class Main {
    public static void main(String[] args) {
        try {
            // Очищаем вебхук перед запуском
            DefaultBotSession session = new DefaultBotSession();
            session.clearWebhook(BotConfig.BOT_TOKEN);
            System.out.println("✅ Вебхук очищен.");

            // Запускаем бота
            TelegramBotsLongPollingApplication app = new TelegramBotsLongPollingApplication();
            app.registerBot(BotConfig.BOT_TOKEN, new TextProcessorBot());
            
            System.out.println("✅ Бот успешно запущен!");
            System.out.println("🤖 Имя бота: @" + BotConfig.BOT_USERNAME);
            System.out.println("📊 Ожидание сообщений...");
            
            Thread.currentThread().join();
            
        } catch (TelegramApiException e) {
            System.err.println("❌ Ошибка очистки вебхука: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}