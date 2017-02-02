public class Task {

    private String task;
    private String hint;
    private String photoPath;
    private String answer;
    private Location location;

    public Task(String task, String hint, String photoPath, String answer, Location location) {
        this.task = task;
        this.hint = hint;
        this.photoPath = photoPath;
        this.answer = answer;
        this.location = location;
    }

    public boolean checkAnswer(String answer) {
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

    public String getPhoto() {
        return photoPath;
    }

    public void setPhoto(String photo) {
        this.photoPath = photo;
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