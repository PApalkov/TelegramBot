import java.sql.SQLException;
import java.util.ArrayList;

public class Group {

    public static boolean ADMIN_QUEST = true;
    public static boolean MADE_QUEST = false;

    DBConnector db = new DBConnector();

    private String groupName;
    private ArrayList<User> group = new ArrayList<User>();
    private String questName;
    private int taskNum;
    private int step;
    private Quest groupQuest;

    private boolean isAdminQuest = false;

    public Group() {
        this.groupName = "None";
        this.questName = "QuestName";
        this.taskNum = 0;
        this.step = 0;
        this.groupQuest = null;
    }

    public Group(User user) {
        group.add(user);
        this.groupName = "None";
        this.questName = "QuestName";
        this.taskNum = 0;
        this.step = 0;
        this.groupQuest = null;
    }

    public Group(String groupName, ArrayList<User> group, String questName, int taskNum, int step) {
        this.groupName = groupName;
        this.group = group;
        this.questName = "QuestName";
        this.taskNum = taskNum;
        this.step = step;
        this.groupQuest = null;
    }

    public boolean contains(long chatId){
        for (int i = 0; i < group.size(); i++) {
            if (group.get(i).getChatId() == chatId) {
                return true;
            }
        }
        return false;
    }

    public void addQuest(){
        groupQuest = new Quest();
    }

    public void addQuest(long chatId){
        groupQuest = new Quest(chatId);
    }

    public Quest getQuest(){
        return groupQuest;
    }

    public void setQuest(Quest quest){
        this.groupQuest = quest;
        this.questName = quest.getQuestName();
    }

    public void saveQuest() {

        groupQuest.setCurrentMakingStep(Quest.NONE);
        groupQuest.setOnCreating(false);
        try {
            DBConnector.init();
            db.addQuest(groupQuest);
            DBConnector.closeDB();
        } catch (SQLException e) {
            System.out.println("SAVING_QUEST_ERROR");
        } catch (ClassNotFoundException b){
            System.out.println(b.getMessage());
        }

        groupQuest = null;
    }

    public void deleteQuest(){

        groupQuest.setCurrentMakingStep(Quest.NONE);
        groupQuest.setOnCreating(false);
        groupQuest = null;
        questName = "QuestName";

    }

    public void removeLastTask(){
        groupQuest.removeLastTask();
    }

    public void addUser(User user){
        group.add(0, user);
    }

    public void addUser(long userId){
        group.add(0, new User(userId));
    }

    public void addUser(int index, User user){
        group.add(0, user);
    }

    public void addUser(int index, long chatId){
        group.add(0, new User(chatId));
    }

    public void delUser(int chatId){
        for (int i = 0; i < group.size(); i++){
            if (group.get(i).getChatId() == chatId){
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

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public ArrayList<User> getGroup() {
        return group;
    }

    public void setGroup(ArrayList<User> group) {
        this.group = group;
    }

    public Quest getGroupQuest() {
        return groupQuest;
    }

    public void setGroupQuest(Quest groupQuest) {
        this.groupQuest = groupQuest;
    }

    public int getSize(){
        return group.size();
    }

    public User getUser(int index){
        return group.get(index);
    }

    public void remove(long chatId){
        for (int i = 0; i < group.size(); i++) {
            if (group.get(i).getChatId() == chatId){
                group.remove(i);
            }
        }
    }

    public boolean isEmpty(){
        if (group.size() == 0) {
            return true;
        }

        return false;
    }

    public boolean isAdminQuest() {
        return isAdminQuest;
    }

    public void setAdminQuest(boolean adminQuest) {
        isAdminQuest = adminQuest;
    }
}

