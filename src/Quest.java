import java.util.ArrayList;

public class Quest {

    public static final int NAME = 0;
    public static final int TASK = 1;
    public static final int HINT_1 = 2;
    public static final int PHOTO = 4;
    public static final int HINT_2 = 3;
    public static final int LOCATION = 5;

    private String questName;
    private String introMessage;
    private ArrayList<Task> quest = new ArrayList<Task>();

    private boolean onCreating;
    private long inventorId;
    private int currentMakingStep;

    public Quest(String introMessage, String questName, ArrayList quest) {
        this.introMessage = introMessage;
        this.quest = quest;
        this.questName = questName;
    }

    public Quest(){
        this.introMessage = "Quest Intro Message";
        this.questName = "QuestName";
        this.onCreating = false;
        this.inventorId = 0;
        this.currentMakingStep = NAME;
    }

    public Quest(long inventorId){
        this.introMessage = "Quest Intro Message";
        this.questName = "QuestName";
        this.onCreating = false;
        this.inventorId = inventorId;
        this.currentMakingStep = NAME;
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

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    public ArrayList<Task> getQuest() {
        return quest;
    }

    public void setQuest(ArrayList<Task> quest) {
        this.quest = quest;
    }

    public boolean isOnCreating() {
        return onCreating;
    }

    public void setOnCreating(boolean onCreating) {
        this.onCreating = onCreating;
    }

    public long getInventorId() {
        return inventorId;
    }

    public void setInventorId(long inventorId) {
        this.inventorId = inventorId;
    }

}

