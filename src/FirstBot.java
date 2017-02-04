import org.telegram.telegrambots.api.methods.send.SendLocation;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.api.objects.Location;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.sql.SQLException;

public class FirstBot extends TelegramLongPollingCommandBot {

    public static final int MAKE_OR_MADE_QUEST_SELECTION = 0;
    public static final int SINGLE_OR_GROUP_PLAYING_SELECTION = 1;
    public static final int ENTERING_GROUPNAME = 2;
    public static final int JOINING_EXISTING_GROUP = 3;
    public static final int MAKING_QUEST = 4;
    public static final int FILLING_TASKS = 5;
    public static final int RUNNING_QUEST = 6;
    public static final int CHOOSING_QUEST = 7;
    public static final int QUEST_FIELDS_SELECTION = 8;
    public static final int TASK_FIELDS_SELECTION = 9;

    private static final String LOGTAG = "FIRSTBOT";

    private Participants players = new Participants();

    public FirstBot() {

        register(new StartCommand(players));
        register(new StopCommand(players));
        register(new FirstHintCommand(players));
        register(new SecondHintCommand(players));
        register(new HelpCommand());

        registerDefaultAction((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(message.getChatId());
            commandUnknownMessage.setText("The command '" + message.getText() + "' is not known by this bot. Here comes some help ");

            try {
                absSender.sendMessage(commandUnknownMessage);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
        });
    }

    @Override
    public void processNonCommandUpdate(Update update) {

        if (update.hasMessage()) {

            long chatId = update.getMessage().getChatId();

            if (players.contains(chatId)) {

                int step = players.getGroup(chatId).getStep();

                switch (step) {

                    case MAKE_OR_MADE_QUEST_SELECTION: {

                        players.getGroup(chatId).setStep(makeOrMadeQuest(update.getMessage()));
                        break;

                    }

                    case SINGLE_OR_GROUP_PLAYING_SELECTION: {

                        players.getGroup(chatId).setStep(aloneOrGroup(update.getMessage()));
                        break;

                    }

                    case ENTERING_GROUPNAME: {

                        Message gotMessage = update.getMessage();

                        if (gotMessage.hasText()) {
                            try {

                                players.setGroupName(chatId, gotMessage.getText());
                                players.getGroup(chatId).setStep(choosingQuest(gotMessage));

                            } catch (Exception e) {
                                sendMessageHandle(chatId, e.getMessage());
                                BotLogger.error(LOGTAG, e);
                            }
                        } else {
                            markUpSendMessageHandle(gotMessage.getChatId(), "Неверный формат!", KeyBoards.aloneOrGroupKeyboard());
                            players.getGroup(gotMessage.getChatId()).setStep(SINGLE_OR_GROUP_PLAYING_SELECTION);
                        }

                        break;
                    }

                    case JOINING_EXISTING_GROUP: {
                        players.getGroup(chatId).setStep(joiningGroup(update.getMessage()));
                        break;
                    }

                    case QUEST_FIELDS_SELECTION: {

                        players.getGroup(chatId).setStep(makingQuest(update.getMessage()));

                        break;
                    }

                    case MAKING_QUEST: {

                        players.getGroup(chatId).getQuest().setActiveParameter(update.getMessage());
                        players.getGroup(chatId).setStep(QUEST_FIELDS_SELECTION);

                        if (update.getMessage().hasText()) {
                            markUpSendMessageHandle(chatId, "Вы ввели: " + update
                                    .getMessage().getText(), KeyBoards.makingQuestKeyBoard());
                        }

                        if (update.getMessage().hasLocation()){
                            markUpSendMessageHandle(chatId, "Вы отправили геолокацию!", KeyBoards.makingQuestKeyBoard());
                        }

                        if (update.getMessage().hasPhoto()){
                            markUpSendMessageHandle(chatId, "Вы отправили фотографию!", KeyBoards.makingQuestKeyBoard());
                        }

                        break;

                    }
                    case FILLING_TASKS: {

                        players.getGroup(chatId).getQuest().setActiveParameter(update.getMessage());
                        players.getGroup(chatId).setStep(TASK_FIELDS_SELECTION);

                        if (update.getMessage().hasText()) {
                            markUpSendMessageHandle(chatId, "Вы ввели:" + update
                                    .getMessage().getText(), KeyBoards.makingTasksKeyboayd());
                        }

                        if (update.getMessage().hasLocation()){
                            markUpSendMessageHandle(chatId, "Вы отправили геолокацию!", KeyBoards.makingTasksKeyboayd());
                        }

                        if (update.getMessage().hasPhoto()){
                            markUpSendMessageHandle(chatId, "Вы отправили фотографию!", KeyBoards.makingTasksKeyboayd());
                        }

                        break;
                    }

                    case TASK_FIELDS_SELECTION: {

                        players.getGroup(chatId).setStep(fillingTasks(update.getMessage()));
                        break;

                    }
                    case CHOOSING_QUEST: {

                        if (update.getMessage().hasText()) {
                            String questName = update.getMessage().getText();
                            try {
                                DBConnector.init();
                                if (DBConnector.containsQuest(questName)) {

                                    players.getGroup(chatId).setQuest(DBConnector.getQuest(questName));
                                    players.getGroup(chatId).setStep(RUNNING_QUEST);

                                    sendMessageHandle(chatId, players.getGroup(chatId).getQuest().getIntroMessage());
                                    sendTask(update.getMessage());
                                }
                                DBConnector.closeDB();
                            } catch (SQLException e) {
                                BotLogger.error(LOGTAG, e);
                                markUpSendMessageHandle(chatId, "Такого квеста не существует", KeyBoards.makeOrMadeQuest());
                                players.getGroup(chatId).setStep(MAKE_OR_MADE_QUEST_SELECTION);
                            } catch (ClassNotFoundException b) {
                                BotLogger.error(LOGTAG, b);
                            }

                        } else  {
                            markUpSendMessageHandle(chatId, "Введите название квеста!", KeyBoards.makeOrMadeQuest());
                        }

                        break;
                    }
                    case RUNNING_QUEST: {

                        Message gotMessage = update.getMessage();
                        if (gotMessage.hasText()) {
                            try {
                                handleIncomingMessage(gotMessage);
                            } catch (TelegramApiException e) {
                                BotLogger.error(LOGTAG, e);
                            }
                        }

                        break;
                    }

                    default: {
                        sendMessageHandle(chatId, "FATAL ERROR");
                    }
                }

            } else {
                sendMessageHandle(chatId, "Нажмите /start");
            }
        }
    }

    private void handleIncomingMessage(Message message) throws TelegramApiException {

        long chatId = message.getChatId();
        int task_num = players.getGroup(chatId).getTaskNum();
        Task task = players.getGroup(chatId).getQuest().getTask(task_num);

        String answer = message.getText().toLowerCase();
        String correct_answer = players.getGroup(chatId).getQuest().getTask(task_num).getAnswer().toLowerCase();


        if (answer.equals(correct_answer)){
            sendToAll(players.getGroup(chatId), "Член вашей группы ввел правильный ответ!");

            if (task_num == players.getGroup(chatId).getQuest().getTaskNumbers() - 1){


                sendToAll(players.getGroup(chatId), "Это было последнее задание!\n" +
                        "Вы прошли квест!");

                markUpSentToAll(players.getGroup(chatId),"Для продолжения нажмите /start" ,KeyBoards.makeOrMadeQuest());

                players.getGroup(chatId).deleteQuest();
                players.removeGroup(chatId);

            } else {
                players.getGroup(chatId).setTaskNum(task_num + 1);
                sendTasktoAll(message);
            }

        } else {
            sendMessageHandle(chatId, "Неправильно!");
        }

    }

    public void sendTasktoAll(Message message){
        long chatId = message.getChatId();

        int task_num = players.getGroup(chatId).getTaskNum();
        Task task = players.getGroup(chatId).getQuest().getTask(task_num);

        if (task.containsTask())
            sendToAll(players.getGroup(chatId), task.getTask());

        if (task.containsPhoto()){
            sentPhotoToAll(players.getGroup(chatId), task.getPhotoPath());
        }

        if (task.containsLocation()){
            sendLocationToAll(players.getGroup(chatId), task.getLocation());
        }
    }

    public void sendTask(Message message){
        long chatId = message.getChatId();

        int task_num = players.getGroup(chatId).getTaskNum();
        Task task = players.getGroup(chatId).getQuest().getTask(task_num);

        if (task.containsPhoto()){
            sendPhotoHandle(chatId, task.getPhotoPath());
        }

        if (task.containsTask()) {
            sendMessageHandle(chatId, task.getTask());
        }

        if (task.containsLocation()){
            mySendLocation(chatId, task.getLocation());
        }

    }

    public void sendToAll(Group group, String text){
        long chatId;

        for (int i = 0; i < group.getSize(); i++) {
            chatId = group.getUser(i).getChatId();
            sendMessageHandle(chatId, text);
        }
    }

    public void sentPhotoToAll(Group group, String photoPath){

        long chatId;

        for (int i = 0; i < group.getSize(); i++) {
            chatId = group.getUser(i).getChatId();
            sendPhotoHandle(chatId, photoPath);
        }
    }

    public void sendLocationToAll(Group group, MyLocation location){

        long chatId;

        for (int i = 0; i < group.getSize(); i++) {
            chatId = group.getUser(i).getChatId();
            mySendLocation(chatId, location);
        }
    }

    public void mySendLocation(long chatId, MyLocation location){
        SendLocation sendLocation = new SendLocation();

        sendLocation.setLatitude(location.getFloatLatitude());
        sendLocation.setLongitude(location.getFloatLongitude());
        sendLocation.setChatId(chatId);

        try {
            sendLocation(sendLocation);
        } catch (TelegramApiException e){
            BotLogger.error(LOGTAG, e);
        }

    }

    public void markUpSentToAll(Group group, String text, ReplyKeyboardMarkup keyboardMarkup){
        long chatId;

        for (int i = 0; i < group.getSize(); i++) {
            chatId = group.getUser(i).getChatId();
            markUpSendMessageHandle(chatId, text, keyboardMarkup);
        }
    }

    private int aloneOrGroup(Message message){

        if (message.hasText()) {

            String text = message.getText();
            if (message.getText().equals(KeyBoards.ALONE)) {

                sendMessageHandle(message.getChatId(), "Придумайте свое кодовое имя.");
                return ENTERING_GROUPNAME;

            } else if (message.getText().equals(KeyBoards.GROUP)) {

                sendMessageHandle(message.getChatId(), "Придумайте название команды");
                return ENTERING_GROUPNAME;

            } else if (message.getText().equals(KeyBoards.BACK)) {

                markUpSendMessageHandle(message.getChatId(), "Выберите:", KeyBoards.makeOrMadeQuest());
                return MAKE_OR_MADE_QUEST_SELECTION;

            } else {

                markUpSendMessageHandle(message.getChatId(), "Неверный выбор.\nПользуйтесь кнопками",
                        KeyBoards.aloneOrGroupKeyboard());
                return SINGLE_OR_GROUP_PLAYING_SELECTION;

            }
        } else {

            markUpSendMessageHandle(message.getChatId(), "Вы будете один или в команде?", KeyBoards.aloneOrGroupKeyboard());
            return  SINGLE_OR_GROUP_PLAYING_SELECTION;
        }
    }

    private int makeOrMadeQuest(Message message){

        if (message.hasText()) {
            String text = message.getText();

            switch (text) {
                case "Пройти квест": {

                    markUpSendMessageHandle(message.getChatId(), "Вы будете один или в команде?", KeyBoards.aloneOrGroupKeyboard());
                    return SINGLE_OR_GROUP_PLAYING_SELECTION;

                }

                case "Создать квест": {

                    long chatId = message.getChatId();
                    players.getGroup(chatId).addQuest(chatId);
                    players.getGroup(chatId).getQuest().setOnCreating(true);

                    markUpSendMessageHandle(chatId, "Введите название квеста.\nПо этому названию люди смогут его найти\n" +
                                    "Так же введите приветственное сообщение",
                            KeyBoards.makingQuestKeyBoard());

                    return QUEST_FIELDS_SELECTION;
                }

                case "Найти команду": {

                    sendMessageHandle(message.getChatId(), "Введите имя команды");
                    return JOINING_EXISTING_GROUP;

                }

                case "Найти квест": {
                    sendMessageHandle(message.getChatId(), "Введите название квеста");
                    return CHOOSING_QUEST;
                }

                default: {
                    markUpSendMessageHandle(message.getChatId(), "Неверный выбор.\nПользуйтесь кнопками", KeyBoards.makeOrMadeQuest());
                    return MAKE_OR_MADE_QUEST_SELECTION;
                }
            }
        } else {
            markUpSendMessageHandle(message.getChatId(), "Неверный выбор.\nПользуйтесь кнопками", KeyBoards.makeOrMadeQuest());
            return MAKE_OR_MADE_QUEST_SELECTION;
        }
    }

    private int joiningGroup(Message message){

        if (message.hasText()) {
            String gotGroupName = message.getText();
            long chatId = message.getChatId();
            String userName = message.getChat().getFirstName() + message.getChat().getLastName();
            if (players.hasGroup(gotGroupName)) {

                players.remove(message.getChatId());
                players.addToGroup(gotGroupName, message.getChatId());

                sendToAll(players.getGroup(chatId), userName + " присоединился к команде!");

                sendTask(message);


                return RUNNING_QUEST;

            } else {

                markUpSendMessageHandle(message.getChatId(), "Такой команды не существует", KeyBoards.makeOrMadeQuest());

                return MAKE_OR_MADE_QUEST_SELECTION;
            }
        } else {

            sendMessageHandle(message.getChatId(), "Введите корректное название команды!");
            return JOINING_EXISTING_GROUP;

        }
    }

    private int choosingQuest(Message message){

        markUpSendMessageHandle(message.getChatId(), "Выберите квест!", KeyBoards.showQuests());

        return CHOOSING_QUEST;
    }

    private int makingQuest(Message message) {

        if (message.hasText()) {
            String text = message.getText();
            long chatId = message.getChatId();

            if (text.equals(KeyBoards.QUEST_NAME)){

                sendMessageHandle(chatId, "Введите название квеста.");
                players.getGroup(chatId).getQuest().setCurrentMakingStep(Quest.NAME);

            } else if (text.equals(KeyBoards.INTRO_MESSAGE)){

                sendMessageHandle(chatId, "Введите приветственное сообщение.");
                players.getGroup(chatId).getQuest().setCurrentMakingStep(Quest.INTRO_MESSAGE);

            } else if (text.equals(KeyBoards.ADD_QUEST_TASK)) {

                players.getGroup(chatId).getQuest().addTask();
                markUpSendMessageHandle(chatId, "Введите задание и ответ на него\nК заданию может быть " +
                        "прикриплена фотография и геопозиция", KeyBoards.makingTasksKeyboayd());
                return TASK_FIELDS_SELECTION;

            } else if (text.equals(KeyBoards.READY)){
                if (players.getGroup(chatId).getQuest().getTaskNumbers() == 0){
                    markUpSendMessageHandle(chatId, "Необходимо добавить хотя бы одно задание",
                            KeyBoards.makingQuestKeyBoard());
                    return QUEST_FIELDS_SELECTION;
                } else {

                    String questName = players.getGroup(chatId).getQuest().getQuestName();
                    boolean contains = false;
                    try {
                        DBConnector.init();
                        contains = DBConnector.containsQuest(questName);
                        DBConnector.closeDB();
                    } catch (ClassNotFoundException e) {
                        BotLogger.error(LOGTAG, e);
                    } catch (SQLException e) {
                        BotLogger.error(LOGTAG, e);
                    }

                    if (contains) {
                        markUpSendMessageHandle(chatId, "Такое название занято.\nВведите другое.", KeyBoards.makingQuestKeyBoard());
                        return QUEST_FIELDS_SELECTION;
                    } else {
                        markUpSendMessageHandle(chatId, "Вы создали квест: " + players.getGroup(chatId).getQuest().getQuestName(),
                                KeyBoards.makeOrMadeQuest());

                        players.getGroup(chatId).saveQuest();

                        return MAKE_OR_MADE_QUEST_SELECTION;
                    }
                }
            } else if (text.equals(KeyBoards.CANCEL)){

                markUpSendMessageHandle(chatId, "Вы отменили создание квеста", KeyBoards.makeOrMadeQuest());
                players.getGroup(chatId).deleteQuest();
                return MAKE_OR_MADE_QUEST_SELECTION;

            } else {
                markUpSendMessageHandle(message.getChatId(), "Неверный выбор.\nПользуйтесь кнопками", KeyBoards.makingQuestKeyBoard());
            }

        } else {
            markUpSendMessageHandle(message.getChatId(), "Неверный выбор.\nПользуйтесь кнопками", KeyBoards.makingQuestKeyBoard());
        }

        return MAKING_QUEST;
    }

    private  int fillingTasks(Message message){

        if (message.hasText()) {
            String text = message.getText();
            long chatId = message.getChatId();

            if (text.equals(KeyBoards.TASK)) {

                sendMessageHandle(chatId, "Введите задание.");
                players.getGroup(chatId).getQuest().setCurrentMakingStep(Quest.TASK);

            } else if (text.equals(KeyBoards.ANSWER)) {

                sendMessageHandle(chatId, "Введите ответ.");
                players.getGroup(chatId).getQuest().setCurrentMakingStep(Quest.ANSWER);

            } else if (text.equals(KeyBoards.HINT1)) {

                sendMessageHandle(chatId, "Введите первую подсказку.");
                players.getGroup(chatId).getQuest().setCurrentMakingStep(Quest.HINT_1);

            } else if (text.equals(KeyBoards.HINT2)) {

                sendMessageHandle(chatId, "Введите вторую подсказку.");
                players.getGroup(chatId).getQuest().setCurrentMakingStep(Quest.HINT_2);

            } else if (text.equals(KeyBoards.PHOTO)) {

                sendMessageHandle(chatId, "Добавьте фото к заданию");
                players.getGroup(chatId).getQuest().setCurrentMakingStep(Quest.PHOTO);

            } else if (text.equals(KeyBoards.LOCATION)) {

                sendMessageHandle(chatId, "Добавьте геолокацию к заданию");
                players.getGroup(chatId).getQuest().setCurrentMakingStep(Quest.LOCATION);

            } else if (text.equals(KeyBoards.READY)) {

                int taskNum = players.getGroup(chatId).getQuest().getTaskNumbers();
                String task = players.getGroup(chatId).getQuest().getTask(taskNum - 1).getTask();
                String answer = players.getGroup(chatId).getQuest().getTask(taskNum - 1).getAnswer();

                System.out.println(answer + "\n" + task);
                if ((answer == null) || (task == null)) {
                    markUpSendMessageHandle(chatId, "Не заполнены поля \"Задание\" или \"Ответ\"", KeyBoards.makingTasksKeyboayd());
                    return TASK_FIELDS_SELECTION;
                }

                markUpSendMessageHandle(chatId, "Задание сохранено!",
                        KeyBoards.makingQuestKeyBoard());
                players.getGroup(chatId).getQuest().setCurrentMakingStep(Quest.NONE);

                return QUEST_FIELDS_SELECTION;

            } else if (text.equals(KeyBoards.BACK)) {

                markUpSendMessageHandle(chatId, "Отмена создания задания",
                        KeyBoards.makingQuestKeyBoard());

                players.getGroup(chatId).getQuest().removeLastTask();

                players.getGroup(chatId).getQuest().setCurrentMakingStep(Quest.NONE);

                return QUEST_FIELDS_SELECTION;

            } else {
                markUpSendMessageHandle(message.getChatId(), "Неверный выбор.\nПользуйтесь кнопками", KeyBoards.makingTasksKeyboayd());
            }

        } else {
            markUpSendMessageHandle(message.getChatId(), "Неверный выбор.\nПользуйтесь кнопками", KeyBoards.makingTasksKeyboayd());
        }
        return FILLING_TASKS;
    }

    public  void sendMessageHandle(long chatId, String text){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            sendMessage(message);
        } catch (TelegramApiException e){
            BotLogger.error(LOGTAG, e);
        }
    }

    public  void markUpSendMessageHandle(long chatId, String text, ReplyKeyboardMarkup replyKeyboardMarkup){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(replyKeyboardMarkup);

        try {
            sendMessage(message);
        } catch (TelegramApiException e){
            BotLogger.error(LOGTAG, e);
        }
    }

    public void sendPhotoHandle(long chatId, String photoPath){
        SendPhoto sendPhoto1 = new SendPhoto();
        sendPhoto1.setChatId(chatId);
        sendPhoto1.setPhoto(photoPath);
        try {
            sendPhoto(sendPhoto1);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "QuestBot";
    }

    @Override
    public void onClosing() {}

    @Override
    public String getBotToken() {
        return "TOKEN";
    }

}

