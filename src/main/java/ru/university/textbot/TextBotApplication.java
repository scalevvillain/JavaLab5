package ru.university.textbot;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import ru.university.textbot.config.BotConfig;

public class TextBotApplication {
    public static void main(String[] args) {
        try {
            // Создаём экземпляр Long Polling приложения
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();

            // Создаём нашего бота
            TextProcessorBot bot = new TextProcessorBot();

            // Регистрируем бота с его токеном
            botsApplication.registerBot(BotConfig.BOT_TOKEN, bot);

            System.out.println("✅ Бот успешно запущен!");
            System.out.println("🤖 Имя бота: @" + BotConfig.BOT_USERNAME);
            System.out.println("📊 Ожидание сообщений...");
            System.out.println("Для остановки нажмите Ctrl+C\n");

            // Блокируем главный поток, чтобы бот работал постоянно
            Thread.currentThread().join();

        } catch (Exception e) {
            System.err.println("❌ Ошибка при запуске бота: " + e.getMessage());
            e.printStackTrace();
        }
    }
}