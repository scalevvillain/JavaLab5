import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.university.quizbot.QuizBot;
import ru.university.quizbot.config.BotConfig;

public class Main {
    public static void main(String[] args) {
        try {
            // 1. Сначала удаляем старый вебхук, если он был
            DefaultBotSession session = new DefaultBotSession();
            session.clearWebhook(BotConfig.BOT_TOKEN);
            System.out.println("✅ Вебхук очищен.");

            // 2. Запускаем самого бота
            TelegramBotsLongPollingApplication app = new TelegramBotsLongPollingApplication();
            app.registerBot(BotConfig.BOT_TOKEN, new QuizBot());
            
            System.out.println("✅ Бот успешно запущен!");
            System.out.println("🤖 Имя бота: @" + BotConfig.BOT_USERNAME);
            System.out.println("📊 Ожидание сообщений...");
            
            Thread.currentThread().join();
            
        } catch (TelegramApiException e) {
            System.err.println("❌ Ошибка Telegram API при очистке вебхука: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Неожиданная ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}