import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.sql.SQLException;

public class DeleteQuest extends BotCommand {


    private static final String LOGTAG = "DELETEQUESTCOMMAND";
    private Participants participants;

    public DeleteQuest(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    public DeleteQuest() {
        super("delete_quest", "Delete handmade quest");
    }

    public DeleteQuest(Participants participants) {
        super("delete_quest", "Delete handmade quest");
        this.participants = participants;
    }

    @Override
    public void execute(AbsSender absSender, org.telegram.telegrambots.api.objects.User user, Chat chat, String[] strings) {


        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());

        try {

            DBConnector.init();

            if (DBConnector.hasMadeQuests(chat.getId())) {
                message.setText("Выберите квест");
                message.setReplyMarkup(KeyBoards.showHandMadeQuest(chat.getId()));
            } else {
                message.setText("Вы не создали ни одного квеста");
            }


            DBConnector.closeDB();
        } catch (SQLException | ClassNotFoundException e) {
            BotLogger.error(LOGTAG, e);
        }



        try {
            absSender.sendMessage(message);
        } catch (TelegramApiException e){
            BotLogger.error(LOGTAG, e);
        }
    }


}
