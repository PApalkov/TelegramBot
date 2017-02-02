import java.util.ArrayList;

public class Quest {
    private String questName;
    private String introMessage;
    private ArrayList<Task> quest = new ArrayList<Task>();

    public Quest(String introMessage, String questName, ArrayList quest) {
        this.introMessage = introMessage;
        this.quest = quest;
        this.questName = questName;
    }

    public Quest(){
        this.introMessage = "Quest Intro Message";
        this.questName = "QuestName";

    }

    public int getTaskNumbers() {
        return quest.size();
    }

    public String getIntroMessage() {
        return introMessage;
    }

    public void setIntroMessage(String introMessage) {
        this.introMessage = introMessage;
    }
}

