import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

/**
 * Created by pavel on 01.02.17.
 */
public class HelpCommand extends BotCommand{
    private static final String LOGTAG = "HELPCOMMAND";

    public HelpCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    public HelpCommand() {
        super("help", "Get help info");
    }

    @Override
    public void execute(AbsSender absSender, org.telegram.telegrambots.api.objects.User user, Chat chat, String[] strings) {

        String introMessage = getHelpInfo();

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.setText(introMessage);

        try {
            absSender.sendMessage(answer);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }


    private String getHelpInfo() {
        String introMessage;
        String fileName = "helpInfo.txt";

        //todo

        return "Help Info Message";
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
