import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class HelpCommand extends BotCommand {
    private static final String LOGTAG = "HELPCOMMAND";

    public HelpCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    public HelpCommand() {
        super("help", "Get help info");
    }

    @Override
    public void execute(AbsSender absSender, org.telegram.telegrambots.api.objects.User user, Chat chat, String[] strings) {

        String helpMessage = getHelpInfo();

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.setText(helpMessage);

        try {
            absSender.sendMessage(answer);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }


    private String getHelpInfo() {

        String helpMessage = "";
        String fileName = "helpMessage.txt";
        List<String> fileContent;
        try {
            fileContent = Files.readAllLines(Paths.get(fileName));

            for (int i = 0; i < fileContent.size(); i++) {
                helpMessage = helpMessage + fileContent.get(i) + "\n";
            }

        } catch (IOException e){
            BotLogger.error(LOGTAG, e);
            helpMessage = "Intro Text";
        }

        return helpMessage;
    }
}
