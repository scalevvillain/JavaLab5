package ru.university.textbot;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.university.textbot.config.BotConfig;
import ru.university.textbot.processor.TextProcessor;

public class TextProcessorBot implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;

    public TextProcessorBot() {
        telegramClient = new OkHttpTelegramClient(BotConfig.BOT_TOKEN);
        System.out.println("🤖 Бот инициализирован. Имя: @" + BotConfig.BOT_USERNAME);
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String userMessage = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getFirstName();

            System.out.println("📩 Получено сообщение от " + userName + ": " + userMessage);

            String answerText;

            if (userMessage.equals("/start")) {
                answerText = "Привет, " + userName + "! ✨\n\n" +
                        "Я бот-генератор аббревиатур (Вариант 4).\n" +
                        "Отправь мне текст, и я создам аббревиатуру из первых букв каждого слова.\n\n" +
                        "Пример: 'Объектно Ориентированное Программирование' → 'ООП'";
            } else if (userMessage.equals("/help")) {
                answerText = "📖 **Справка**\n\n" +
                        "Просто отправь мне любое предложение.\n" +
                        "Я выделю первые буквы каждого слова и составлю из них аббревиатуру.\n\n" +
                        "Пример:\n" +
                        "`Java разработка программного обеспечения` → `ЯРПО`";
            } else if (userMessage.equals("/about")) {
                answerText = "🤖 **О боте**\n\n" +
                        "Версия: 1.0\n" +
                        "Вариант: 4 (Генератор аббревиатур)\n" +
                        "Технология: Java + Telegram Bot API";
            } else {

                String abbreviation = TextProcessor.processText(userMessage);
                answerText = "📝 **Исходный текст:**\n" + userMessage + "\n\n" +
                        "🔤 **Аббревиатура:**\n" + abbreviation;
            }

            SendMessage reply = SendMessage.builder()
                    .chatId(chatId)
                    .text(answerText)
                    .build();

            try {
                telegramClient.execute(reply);
                System.out.println("✅ Ответ отправлен пользователю " + userName);
            } catch (TelegramApiException e) {
                System.err.println("❌ Ошибка при отправке сообщения: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}