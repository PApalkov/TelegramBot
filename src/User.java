
public class User {
    private int userId;

    public User(int userId) {
        this.userId = userId;
    }

    public User(){
        userId = 0;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean equal(User user){
        if (this.userId == user.userId){
            return true;
        }
        return false;
    }


}
