public class Task {

    private int taskNumber;
    private String task;
    private String hint1;
    private String hint2;
    private String photoPath;
    private String answer;
    private MyLocation location;

    public Task(int taskNumber, String task, String hint1, String hint2, String photoPath, String answer, MyLocation location) {
        this.taskNumber = taskNumber;
        this.task = task;
        this.hint1 = hint1;
        this.hint2 = hint2;
        this.photoPath = photoPath;
        this.answer = answer;
        this.location = location;
    }

    public Task(String task, String hint1, String hint2, String photoPath, String answer, MyLocation location) {
        this.task = task;
        this.hint1 = hint1;
        this.hint2 = hint2;
        this.photoPath = photoPath;
        this.answer = answer;
        this.location = location;
    }

    public Task(){
        this.task = null;
        this.hint1 = null;
        this.hint2 = null;
        this.photoPath = null;
        this.answer = null;
        this.location = new MyLocation();
    }

    public boolean checkAnswer(String answer) {
        return this.answer.equals(answer);
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

    public MyLocation getLocation() {
        return location;
    }

    public void setLocation(MyLocation location) {
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

    public boolean containsTask(){
        if (task == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean containsHint1(){
        if (hint1 == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean containsHint2(){
        if (hint2 == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean containsPhoto(){
        if (photoPath == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean containsLocation(){
        if ( (location.getLatitude() == 0) && (location.getLongitude() == 0 ) ) {
            return false;
        } else {
            return true;
        }
    }


}