package ru.university.textbot;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.university.textbot.config.BotConfig;
import ru.university.textbot.processor.TextProcessor;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TextProcessorBot implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private boolean isRunning = true;

    public TextProcessorBot() {
        telegramClient = new OkHttpTelegramClient(BotConfig.BOT_TOKEN);
        logToFile("🤖 Бот инициализирован. Имя: @" + BotConfig.BOT_USERNAME);
        System.out.println("🤖 Бот инициализирован. Имя: @" + BotConfig.BOT_USERNAME);
    }

    private void logToFile(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BotConfig.LOG_FILE_PATH, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.println(timestamp + " - " + message);
        } catch (Exception e) {
            System.err.println("❌ Ошибка записи лога: " + e.getMessage());
        }
    }

    @Override
    public void consume(Update update) {
        if (!isRunning) return;

        if (update.hasMessage() && update.getMessage().hasText()) {
            String userMessage = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getFirstName();
            String userLogin = update.getMessage().getFrom().getUserName();

            // Логируем входящее сообщение
            String logMsg = "📩 Получено сообщение от @" + userLogin + " (" + userName + "): " + userMessage;
            System.out.println(logMsg);
            logToFile(logMsg);

            String answerText;

            if (userMessage.equals("/start")) {
                answerText = "Привет, " + userName + "! ✨\n\n" +
                        "Я бот-генератор аббревиатур (Вариант 4).\n" +
                        "Отправь мне текст, и я создам аббревиатуру из первых букв каждого слова.\n\n" +
                        "Пример: 'Объектно Ориентированное Программирование' → 'ООП'\n\n" +
                        "Команды:\n" +
                        "/help - справка\n" +
                        "/stop - остановить бота (только для администратора)";

                logToFile("🟢 Отправлен /start пользователю @" + userLogin);

            } else if (userMessage.equals("/help")) {
                answerText = "📖 **Справка**\n\n" +
                        "Просто отправь мне любое предложение.\n" +
                        "Я выделю первые буквы каждого слова и составлю из них аббревиатуру.\n\n" +
                        "Пример:\n" +
                        "`Java разработка программного обеспечения` → `ЯРПО`\n\n" +
                        "Команды:\n" +
                        "/start - приветствие\n" +
                        "/help - эта справка\n" +
                        "/stop - остановить бота (админ)";

                logToFile("📖 Отправлен /help пользователю @" + userLogin);

            } else if (userMessage.equals("/stop")) {
                // Команда остановки (можно ограничить по ID администратора)
                answerText = "🛑 Бот останавливается... До свидания!";
                isRunning = false;

                logToFile("🛑 Бот остановлен пользователем @" + userLogin);
                System.out.println("🛑 Бот остановлен пользователем @" + userLogin);

            } else {
                String abbreviation = TextProcessor.processText(userMessage);
                answerText = "📝 **Исходный текст:**\n" + userMessage + "\n\n" +
                        "🔤 **Аббревиатура:**\n" + abbreviation;

                logToFile("🔧 Обработан текст: \"" + userMessage + "\" → \"" + abbreviation + "\"");
            }

            SendMessage reply = SendMessage.builder()
                    .chatId(chatId)
                    .text(answerText)
                    .build();

            try {
                telegramClient.execute(reply);
                System.out.println("✅ Ответ отправлен пользователю " + userName);
                logToFile("✅ Ответ отправлен пользователю @" + userLogin);
            } catch (TelegramApiException e) {
                String errorMsg = "❌ Ошибка при отправке сообщения: " + e.getMessage();
                System.err.println(errorMsg);
                logToFile(errorMsg);
                e.printStackTrace();
            }
        }
    }

    public boolean isRunning() {
        return isRunning;
    }
}