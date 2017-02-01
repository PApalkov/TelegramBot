import org.telegram.telegrambots.api.objects.Update;
import java.util.ArrayList;

public class Participants {
    private ArrayList<Group> participants = new ArrayList<Group>();

    public boolean contains(Update update){
        long chatId = update.getMessage().getChatId();
        for (int i = 0; i < participants.size(); i++) {
            if (participants.get(i).contains(chatId)){
                return true;
            }
        }
        return false;
    }

    //todo
}
