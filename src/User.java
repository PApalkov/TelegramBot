import org.telegram.telegrambots.api.objects.Update;

public class User{

    private long chatId;
    private String name;
    private String surname;
    private int groupID;

    public User(Update update){
        this.chatId = update.getMessage().getChatId();
    }

    public User(long chatId, int groupID){
        this.chatId = chatId;
        this.groupID = groupID;
    }

    public User(long chatId, String name, String surname, int groupID) {
        this.chatId = chatId;
        this.name = name;
        this.surname = surname;
        this.groupID = groupID;
    }

    public boolean equal(User user) {
        if (this.chatId == user.chatId) {
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public long getChatId() {
        return chatId;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

}
