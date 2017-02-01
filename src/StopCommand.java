import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public class StopCommand extends BotCommand{
    private static final String LOGTAG = "STOPCOMMAND";
    private Participants participants;

    public StopCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    public StopCommand() {
        super("stop", "Quite a quest");
    }

    public StopCommand(Participants participants) {
        super("stop", "Quite a quest");
        this.participants = participants;
    }

    @Override
    public void execute(AbsSender absSender, org.telegram.telegrambots.api.objects.User user, Chat chat, String[] strings) {

        participants.remove(chat.getId());

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.setText("Надеемся, Вам понравилось!");

        try {
            absSender.sendMessage(answer);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}
