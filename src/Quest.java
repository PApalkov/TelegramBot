import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.bots.AbsSender;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Quest {

    public static final int NONE = 0;
    public static final int NAME = 1;
    public static final int INTRO_MESSAGE = 2;
    public static final int TASK = 3;
    public static final int HINT_1 = 4;
    public static final int PHOTO = 5;
    public static final int HINT_2 = 6;
    public static final int LOCATION = 7;
    public static final int ANSWER = 8;

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
        this.currentMakingStep = NONE;
    }

    public Quest(){
        this.introMessage = "Quest Intro Message";
        this.questName = "QuestName";
        this.onCreating = false;
        this.inventorId = 0;
        this.currentMakingStep = NONE;
    }

    public Quest(long inventorId){
        this.introMessage = "Quest Intro Message";
        this.questName = "QuestName";
        this.onCreating = false;
        this.inventorId = inventorId;
        this.currentMakingStep = NONE;
    }

    public void setActiveParameter(Message message){

        switch (currentMakingStep){

            case NONE:{
                System.out.println("NONE PARAMETER FILLING");
                break;
            }

            case NAME:{
                this.questName = message.getText();
                break;
            }

            case INTRO_MESSAGE:{
                this.questName = message.getText();
                break;
            }

            case TASK:{
                int index = quest.size() - 1;
                quest.get(index).setTask( message.getText() );
                break;

            }

            case ANSWER:{
                int index = quest.size() - 1;
                quest.get(index).setAnswer( message.getText() );
                break;
            }

            case HINT_1:{
                int index = quest.size() - 1;
                quest.get(index).setHint1( message.getText() );
                break;
            }

            case HINT_2:{
                int index = quest.size() - 1;
                quest.get(index).setHint2( message.getText() );
                break;
            }

            case PHOTO:{
/*
                        List<PhotoSize> photos = message.getPhoto();
PhotoSize maxSizePic = photos.stream().sorted(Comparator.comparing(PhotoSize::getFileSize).reversed()).findFirst().orElse(null);


                    Objects.requireNonNull(maxSizePic);

                    if (maxSizePic.hasFilePath()) { // If the file_path is already present, we are done!
                        return photo.getFilePath();
                    } else { // If not, let find it
                        // We create a GetFile method and set the file_id from the photo
                        GetFile getFileMethod = new GetFile();
                        getFileMethod.setFileId(photo.getFileId());
                        try {
                            // We execute the method using AbsSender::getFile method.
                            File file = getFile(getFileMethod);
                            // We now have the file_path
                            return file.getFilePath();
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }

                    return null; // Just in case
                }


*/
                break;
            }

            case LOCATION:{
                //todo
                break;
            }

            default:{
                System.out.println("FATAL ERROR");
            }

        }
    }

    public void saveQuest(){

        this.currentMakingStep = NONE;
        this.onCreating = false;
        //todo
    }

    public void cancelMAkingQuest(){
        //todo
    }

    public void removeLastTask(){
        //todo
    }


    public int getCurrentMakingStep() {
        return currentMakingStep;
    }

    public void setCurrentMakingStep(int currentMakingStep) {
        this.currentMakingStep = currentMakingStep;
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


    /*private java.io.File downloadPhotoByFilePath(String filePath) {
        try {
            // Download the file calling AbsSender::downloadFile method
            return AbsSender::getFile(filePath);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }*/


    public Task getTask(int index){
        return quest.get(index);
    }


}

