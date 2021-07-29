package ru.kot.communications;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

public final class Bot extends TelegramLongPollingBot {
    private final String BOT_NAME;
    private final String BOT_TOKEN;

    public Bot(String botName, String botToken) {
        super();
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();

        Long chatId = msg.getChatId();

        if (!msg.getNewChatMembers().isEmpty()) {
            for (User u : msg.getNewChatMembers()) {
                sendMessage(chatId, String.format("@%s %s, водку пьешь?", u.getUserName(), getFullUserName(u)));
            }
        }
    }

    /**
     * Получение полного имени пользователя
     * @param user сообщение
     */
    private String getFullUserName(User user) {
        String firstName = Optional.ofNullable(user.getFirstName()).orElse("");
        String lastName = Optional.ofNullable(user.getLastName()).orElse("");
        if (!firstName.isEmpty() & !lastName.isEmpty()) {
            firstName += " ";
        }

        return firstName + lastName;
    }

    /**
     * Отправка сообщения
     * @param chatId id чата
     * @param text текст ответа
     */
    private void sendMessage(Long chatId, String text) {
        SendMessage msg = new SendMessage();
        msg.setText(text);
        msg.setChatId(chatId.toString());
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            // TODO: log errors
        }
    }
}