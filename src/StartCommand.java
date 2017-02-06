import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;


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

            if (participants.contains(chat.getId())) {
                participants.getGroup(chat.getId()).setStep(0);
                participants.getGroup(chat.getId()).setGroupName(null);
            } else {
                participants.add(chat.getId());
            }

        } catch (Exception e){

            BotLogger.error(LOGTAG, e);

        }

        String introMessage = getIntroMessage();

        SendMessage message = new SendMessage();
        message.setReplyMarkup(new KeyBoards().makeOrMadeQuest());
        message.setChatId(chat.getId().toString());
        message.setText(introMessage);
        try {
            absSender.sendMessage(message);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    private String getIntroMessage() {

        String introMessage = "";
        String fileName = "startMessage.txt";
        List<String> fileContent;
        try {
            fileContent = Files.readAllLines(Paths.get(fileName));

            for (int i = 0; i < fileContent.size(); i++) {
                introMessage = introMessage + fileContent.get(i) + "\n";
            }

        } catch (IOException e){
            BotLogger.error(LOGTAG, e);
            introMessage = "Intro Text";
        }

        return introMessage;
    }

}



