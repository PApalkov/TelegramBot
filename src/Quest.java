import java.util.ArrayList;

public class Quest {
    private String introMessage;
    private int questID;
    private String questName;
    private ArrayList<Task> quest = new ArrayList<Task>();

    public Quest(String introMessage, int questID, String questName, ArrayList<Task> quest) {
        this.introMessage = introMessage;
        this.questID = questID;
        this.questName = questName;
        this.quest = quest;
    }

    public Quest(String questName) {
        this.questName = questName;
    }

    // todo

    public int tasksNumbers() {
        return quest.size();
    }

    public Task get(int i) {
        return quest.get(i);
    }

    public int getQuestID() {
        return questID;
    }

    public String getIntroMessage() {
        return introMessage;
    }

    public void setIntroMessage(String introMessage) {
        this.introMessage = introMessage;
    }
}

