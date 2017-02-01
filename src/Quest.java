import java.util.ArrayList;

public class Quest {
    private String introMessage;

    private ArrayList<Task> quest = new ArrayList<Task>();

    // todo

    public String getIntroMessage() {
        return introMessage;
    }

    public void setIntroMessage(String introMessage) {
        this.introMessage = introMessage;
    }
}

