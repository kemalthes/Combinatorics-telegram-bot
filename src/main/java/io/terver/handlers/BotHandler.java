package io.terver.handlers;

import io.terver.entity.Mode;
import io.terver.repository.ModeRepository;
import io.terver.service.CalculatorService;
import io.terver.enums.State;
import io.terver.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


@Component
public class BotHandler extends TelegramLongPollingBot {

    private final CalculatorService service;
    private final ModeRepository modeRepository;
    private final UserService userService;
    @Value("${bot.userId}") String userId;

    public BotHandler(@Value("${bot.token}") String botToken,
                      CalculatorService service,
                      ModeRepository modeRepository,
                      UserService userService) {
        super(botToken);
        this.service = service;
        this.modeRepository = modeRepository;
        this.userService = userService;
    }

    @Override
    public String getBotUsername() {
        return userId;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handleQuery(update);
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update);
        }
    }

    private void handleQuery(Update update) {
        String callback = update.getCallbackQuery().getData();
        String chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        Mode mode = modeRepository.findByCallback(callback);
        userService.updateUser(chatId, mode);
        sendMessage(chatId, mode.getPrompt());
        endCallbackQuery(update.getCallbackQuery().getId());
    }

    private void handleMessage(Update update) {
        String message = update.getMessage().getText();
        String chatId = String.valueOf(update.getMessage().getChatId());
        State currentState = userService.checkUser(chatId).getMode().getState();
        try {
            switch (currentState) {
                case PERMUTATION:
                    sendMessage(chatId, String.valueOf(service.permutations(message)));
                    break;
                case DISTRIBUTION:
                    sendMessage(chatId, String.valueOf(service.distribution(message)));
                    break;
                case COMBINATION:
                    sendMessage(chatId, String.valueOf(service.combination(message)));
                    break;
                case PERMUTATION_REPEATED:
                    sendMessage(chatId, String.valueOf(service.permutationsRepeat(message)));
                    break;
                case DISTRIBUTION_REPEATED:
                    sendMessage(chatId, String.valueOf(service.distributionRepeat(message)));
                    break;
                case COMBINATION_REPEATED:
                    sendMessage(chatId, String.valueOf(service.combinationRepeat(message)));
                    break;
                case URN_MODEL_1:
                    sendMessage(chatId, String.valueOf(service.urnModelOne(message)));
                    break;
                case URN_MODEL_2:
                    sendMessage(chatId, String.valueOf(service.urnModelTwo(message)));
                    break;
                default:
                    if (message.equals("/start")) {
                        sendStartMessage(chatId, update.getMessage().getFrom().getFirstName());
                    } else {
                        sendMessage(chatId, "Неверная команда");
                        sendStartMessage(chatId, update.getMessage().getFrom().getFirstName());
                    }
                    break;
            }
        } catch (IllegalArgumentException e) {
            sendMessage(chatId, "Неверно введенные данные");
            sendStartMessage(chatId, update.getMessage().getFrom().getFirstName());
        }
        userService.setStateNone(chatId);
    }

    private void sendMessage(String chatId, String message) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Error");
        }
    }

    private void endCallbackQuery(String callbackQueryId) {
        try {
            execute(new AnswerCallbackQuery(callbackQueryId));
        } catch (TelegramApiException e) {
            System.out.println("Error");
        }
    }

    private void sendStartMessage(String chatId, String name) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text("Привет, " + name + ", выбери, что хочешь сделать")
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(
                                InlineKeyboardButton.builder()
                                        .text("Перестановка")
                                        .callbackData("permutation")
                                        .build(),
                                InlineKeyboardButton.builder()
                                        .text("Распределение")
                                        .callbackData("distribution")
                                        .build(),
                                InlineKeyboardButton.builder()
                                        .text("Сочетание")
                                        .callbackData("combination")
                                        .build()))
                        .keyboardRow(List.of(
                                InlineKeyboardButton.builder()
                                        .text("Перестановка(повт)")
                                        .callbackData("permutation+")
                                        .build(),
                                InlineKeyboardButton.builder()
                                        .text("Распределение(повт)")
                                        .callbackData("distribution+")
                                        .build(),
                                InlineKeyboardButton.builder()
                                        .text("Сочетание(повт)")
                                        .callbackData("combination+")
                                        .build()))
                        .keyboardRow(List.of(
                                InlineKeyboardButton.builder()
                                        .text("Урновая модель 1")
                                        .callbackData("urn_model1")
                                        .build(),
                                InlineKeyboardButton.builder()
                                        .text("Урновая модель 2")
                                        .callbackData("urn_model2")
                                        .build()))
                        .build())
                .build();
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Error");
        }
    }
}