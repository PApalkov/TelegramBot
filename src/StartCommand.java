import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;


public class StartCommand extends BotCommand {

    private static final String LOGTAG = "STARTCOMMAND";
    private Participants participants;


    public StartCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    public StartCommand() {
        super("start", "Start a new quest");
    }

    public StartCommand(Participants participants) {
        super("start", "Start a new quest");
        this.participants = participants;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        try{
            participants.getGroup(chat.getId()).setStep(0);
        } catch (Exception e){
            participants.add(chat.getId());
            BotLogger.error(LOGTAG, e);
        }

        String introMessage = getInroMessage();

        SendMessage message = new SendMessage();
        message.setReplyMarkup(new KeyBoards().MakeOrMadeQuest());
        message.setChatId(chat.getId().toString());
        message.setText(introMessage);
        try {
            absSender.sendMessage(message);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
        //todo
        //занести в базу

    }


    private String getInroMessage() {
        String introMessage;
        String fileName = "startMessage.txt";

        //todo

        return "First Message";
    }
/*  //это код с интернета
        try(FileReader reader = new FileReader("C:\\SomeDir\\notes3.txt"))
        {
            // читаем посимвольно
            int c;
            while((c=reader.read())!=-1){

                System.out.print((char)c);
            }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
 */

}



