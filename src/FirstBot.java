
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;


public class FirstBot extends TelegramLongPollingCommandBot {

    private static final String LOGTAG = "FIRSTBOT";

    private Participants players;

    public FirstBot() {

        players = new Participants();

        register(new StartCommand());
        register(new StopCommand());
        register(new HelpCommand());

        registerDefaultAction((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(message.getChatId());
            commandUnknownMessage.setText("The command '" + message.getText() + "' is not known by this bot. Here comes some help ");
            try {
                absSender.sendMessage(commandUnknownMessage);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
        });
    }


    @Override
    public void processNonCommandUpdate(Update update) {
        if (players.contains(update)) {
            try {
                if (update.hasMessage()) {
                    Message message = update.getMessage();
                    if (message.hasText() || message.hasLocation()) {
                        handleIncomingMessage(message);
                    }
                }
            } catch (Exception e) {
                BotLogger.error(LOGTAG, e);
            }
        } else {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(update.getMessage().getChatId());
            commandUnknownMessage.setText("The command '" + update.getMessage().getText() + "' is not known by this bot. Here comes some help ");
            try {
                sendMessage(commandUnknownMessage);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
        }
    }


    private void handleIncomingMessage(Message message) throws TelegramApiException {

        //todo
        /*
        if (!message.isUserMessage() && message.hasText()) {
            if (isCommandForOther(message.getText())) {
                return;
            } else if (message.getText().startsWith("/stop")) {
                sendHideKeyboard(message.getFrom().getId(), message.getChatId(), message.getMessageId());
                return;
            }
        }
        */

    }

    @Override
    public String getBotUsername() {
        return "MyFirstBot";
    }

    @Override
    public void onClosing() {}

    @Override
    public String getBotToken() {
        return "TOKEN";
    }

}

