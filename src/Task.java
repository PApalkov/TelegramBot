import org.telegram.telegrambots.api.objects.Location;
import org.telegram.telegrambots.api.objects.PhotoSize;

public class Task {

    private String task;
    private String hint;
    private PhotoSize photo;
    private String answer;
    private Location location;

    public boolean checkAnswer(String answer){
        return false;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public PhotoSize getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoSize photo) {
        this.photo = photo;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
