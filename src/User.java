import org.telegram.telegrambots.api.objects.Update;

public class User{

    private long chatId;

    public User(Update update){
        this.chatId = update.getMessage().getChatId();
    }

    public User(long chatId){
        this.chatId = chatId;
    }

    public boolean equal(User user) {
        if (this.chatId == user.chatId) {
            return true;
        }
        return false;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

}
