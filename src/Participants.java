import org.telegram.telegrambots.api.objects.Update;
import java.util.ArrayList;


public class Participants {
    private ArrayList<Group> participants = new ArrayList<Group>();

    public boolean contains(long chatId){
        for (int i = 0; i < participants.size(); i++) {
            if (participants.get(i).contains(chatId)){
                return true;
            }
        }
        return false;
    }


    public Group getGroup(long chatId) throws Exception{
        Group group;

        for (int i = 0; i < participants.size(); i++){
            if (participants.get(i).contains(chatId)){
                return participants.get(i);
            }
        }
        throw new Exception("No such user");
    }

    public void add(long chatId){
        Group group = new Group(new User(chatId));
        participants.add(group);
    }


    //addToGroup

    public void remove(long chatId) {

        for (int i = 0; i < participants.size(); i++){
            if (participants.get(i).contains(chatId)){

                participants.get(i).remove(chatId);

                if (participants.get(i).isEmpty()){
                    participants.remove(i);
                }

                break;
            }
        }
    }

    //todo
}
