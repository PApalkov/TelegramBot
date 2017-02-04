import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public class SecondHintCommand extends BotCommand{

    private static final String LOGTAG = "STARTCOMMAND";
    private Participants participants;


    public SecondHintCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    public SecondHintCommand() {
        super("hint2", "Get first hint!");
    }

    public SecondHintCommand(Participants participants) {
        super("hint2", "Get first hint!");
        this.participants = participants;
    }

    @Override
    public void execute(AbsSender absSender, org.telegram.telegrambots.api.objects.User user, Chat chat, String[] strings) {

        if (participants.contains(chat.getId())){
            SendMessage message = new SendMessage();
            message.setChatId(chat.getId());

            if (participants.getGroup(chat.getId()).getQuest() == null){
                message.setText("Сначала выберите квест!");
            } else {
                int taskNum = participants.getGroup(chat.getId()).getTaskNum();
                String hint2 = participants.getGroup(chat.getId()).getQuest().getTask(taskNum).getHint2();

                if (hint2 == null){
                    message.setText("Подсказка отсутствует.");
                } else {
                    message.setText(hint2);
                }
            }

            try {
                absSender.sendMessage(message);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }

        } else {
            SendMessage message = new SendMessage();
            message.setChatId(chat.getId());
            message.setText("Нажмите /start");
        }
    }

}
