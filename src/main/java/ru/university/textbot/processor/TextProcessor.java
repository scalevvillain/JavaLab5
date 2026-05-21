package ru.university.textbot.processor;

public class TextProcessor {


    public static String processText(String inputText) {
        if (inputText == null || inputText.trim().isEmpty()) {
            return "Вы отправили пустое сообщение. Напишите что-нибудь!";
        }


        String[] words = inputText.split("\\s+");
        StringBuilder abbreviation = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {

                char firstChar = word.charAt(0);
                abbreviation.append(Character.toUpperCase(firstChar));
            }
        }


        if (abbreviation.length() == 0) {
            return "Не удалось сгенерировать аббревиатуру. Используйте слова, разделённые пробелами.";
        }

        return abbreviation.toString();
    }
}