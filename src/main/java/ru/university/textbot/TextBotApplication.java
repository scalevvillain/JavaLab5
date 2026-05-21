package ru.university.textbot;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import ru.university.textbot.config.BotConfig;

public class TextBotApplication {
    private static TelegramBotsLongPollingApplication botsApplication;
    private static TextProcessorBot bot;

    public static void main(String[] args) {
        try {
            botsApplication = new TelegramBotsLongPollingApplication();
            bot = new TextProcessorBot();

            botsApplication.registerBot(BotConfig.BOT_TOKEN, bot);

            System.out.println("✅ Бот успешно запущен!");
            System.out.println("🤖 Имя бота: @" + BotConfig.BOT_USERNAME);
            System.out.println("📊 Ожидание сообщений...");
            System.out.println("📝 Логи пишутся в файл: " + BotConfig.LOG_FILE_PATH);
            System.out.println("Для остановки нажмите Ctrl+C или отправьте боту команду /stop\n");

            // Ждём, пока бот работает
            while (bot.isRunning()) {
                Thread.sleep(1000);
            }

            // Если бот остановлен командой /stop
            System.out.println("👋 Бот остановлен командой /stop");
            botsApplication.close();
            System.exit(0);

        } catch (Exception e) {
            System.err.println("❌ Ошибка при запуске бота: " + e.getMessage());
            e.printStackTrace();
        }
    }
}