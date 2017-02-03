public class Task {

    private int taskNumber;
    private String task;
    private String hint1;
    private String hint2;
    private String photoPath;
    private String answer;
    private Location location;

    public Task(int taskNumber, String task, String hint1, String hint2, String photoPath, String answer, Location location) {
        this.taskNumber = taskNumber;
        this.task = task;
        this.hint1 = hint1;
        this.hint2 = hint2;
        this.photoPath = photoPath;
        this.answer = answer;
        this.location = location;
    }

    public Task(String task, String hint1, String hint2, String photoPath, String answer, Location location) {
        this.task = task;
        this.hint1 = hint1;
        this.hint2 = hint2;
        this.photoPath = photoPath;
        this.answer = answer;
        this.location = location;
    }

    public Task(){
        this.task = "Task";
        this.hint1 = "Hint1";
        this.hint2 = "Hint2";
        this.photoPath = "PhotoPath";
        this.answer = "Answer";
        this.location = null;
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

    public String getHint1() {
        return hint1;
    }

    public void setHint1(String hint1) {
        this.hint1 = hint1;
    }

    public String getHint2() {
        return hint2;
    }

    public void setHint2(String hint2) {
        this.hint2 = hint2;
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

    public int getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}