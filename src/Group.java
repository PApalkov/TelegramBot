import java.util.ArrayList;

public class Group {

    private ArrayList<User> group = new ArrayList<User>();
    private int questNum;
    private int taskNum;

    public void addUser(User user){
        group.add(0, user);
    }

    public void addUser(int userId){
        group.add(0, new User(userId));
    }

    public void addUser(int index, User user){
        group.add(0, user);
    }

    public void addUser(int index, int userId){
        group.add(0, new User(userId));
    }

    public void delUser(int userId){
        for (int i = 0; i < group.size(); i++){
            if (group.get(i).getUserId() == userId){
                group.remove(i);
            }
        }
    }

    public void delUser(User user){
        for (int i = 0; i < group.size(); i++) {
            if (group.get(i).equal(user)){
                group.remove(i);
            }
        }
    }

    public int getQuestNum() {
        return questNum;
    }

    public void setQuestNum(int questNum) {
        this.questNum = questNum;
    }

    public int getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(int taskNum) {
        this.taskNum = taskNum;
    }

    public User at(int index){
        return group.get(index);
    }

    protected void finalize(){
        for (int i = group.size(); i > 0; i++) {
            group.remove(i);
        }
    }

}
