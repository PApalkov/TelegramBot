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


    public Group getGroup(long chatId){
        Group group;

        for (int i = 0; i < participants.size(); i++){
            if (participants.get(i).contains(chatId)){
                return participants.get(i);
            }
        }
        return null;
    }

    public void add(long chatId){
        Group group = new Group(new User(chatId));
        participants.add(group);
    }


    public void addToGroup(String groupName, long chatId){
        for (int i = 0; i < participants.size(); i++) {
            //привести и то и другое к нижнему регистру
            if (participants.get(i).getGroupName().equals(groupName)){
                participants.get(i).addUser(chatId);
            }
        }
    }

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


    public void setGroupName(long chatId, String groupName) throws Exception{

        for (int i = 0; i < participants.size(); i++){
            //привести к нижнему регистру
            if (participants.get(i).getGroupName().equals(groupName)){
                throw new Exception("The name is alredy exists.");
            }
        }

        for (int i = 0; i < participants.size(); i++){
            if (participants.get(i).contains(chatId)){
                participants.get(i).setGroupName(groupName);
                break;
            }
        }
    }


    public boolean hasGroup(String group){

        for (int i = 0; i < participants.size(); i++) {
            //привести и то и другое к нижнему регистру
            if (participants.get(i).getGroupName().equals(group)){
                return true;
            }
        }
        return false;
    }

    //todo
}
