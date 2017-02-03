import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.sql.SQLException;

//если ник существует, то не нало его вводить

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

                            //todo выбор квеста

                        } catch (Exception e) {
                            sendMessageHandle(chatId, e.getMessage());
                            BotLogger.error(LOGTAG, e);
                        }
                    }

                    break;

                }

                case JOINING_EXISTING_GROUP: {

                    System.out.println("Joining");
                    players.getGroup(chatId).setStep(joiningGroup(update.getMessage()));
                    break;
                }

                case QUEST_FIELDS_SELECTION:{

                    players.getGroup(chatId).setStep(makingQuest(update.getMessage()));

                    break;
                }

                case MAKING_QUEST:{

                    players.getGroup(chatId).getQuest().setActiveParameter(update.getMessage());
                    players.getGroup(chatId).setStep(QUEST_FIELDS_SELECTION);

                    //сделать проверку на фото/локацию
                    markUpSendMessageHandle(chatId, "Вы ввели:" + update
                            .getMessage().getText(), KeyBoards.makingQuestKeyBoard());

                    break;

                }
                case FILLING_TASKS:{

                    players.getGroup(chatId).getQuest().setActiveParameter(update.getMessage());
                    players.getGroup(chatId).setStep(TASK_FIELDS_SELECTION);

                    //сделать проверку на фото/локацию
                    markUpSendMessageHandle(chatId, "Вы ввели:" + update
                            .getMessage().getText(), KeyBoards.makingTasksKeyboayd());

                    break;
                }

                case TASK_FIELDS_SELECTION:{

                    players.getGroup(chatId).setStep(fillingTasks(update.getMessage()));
                    break;

                }
                case CHOOSING_QUEST:{

                    String questName = update.getMessage().getText();
                    try {
                        DBConnector.init();
                        if (DBConnector.containsQuest(questName)){
                            players.getGroup(chatId).setQuest( DBConnector.getQuest(questName) );
                            players.getGroup(chatId).setStep(RUNNING_QUEST);
                            sendMessageHandle(chatId, players.getGroup(chatId).getQuest().getIntroMessage());
                            sendTask(update.getMessage());
                        }
                        DBConnector.closeDB();
                    } catch (SQLException e){
                        BotLogger.error(LOGTAG, e);
                        markUpSendMessageHandle(chatId, "Такого квеста не существует", KeyBoards.makeOrMadeQuest());
                        players.getGroup(chatId).setStep(MAKE_OR_MADE_QUEST_SELECTION);
                    } catch (ClassNotFoundException b){
                        BotLogger.error(LOGTAG, b);
                    }

                    break;
                }
                case RUNNING_QUEST: {

                    if (update.hasMessage()) {
                        Message gotMessage = update.getMessage();
                        if (gotMessage.hasText()) {
                            try {
                                handleIncomingMessage(gotMessage);
                            } catch (TelegramApiException e) {
                                BotLogger.error(LOGTAG, e);
                            }
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

    private void handleIncomingMessage(Message message) throws TelegramApiException {

        long chatId = message.getChatId();
        int task_num = players.getGroup(chatId).getTaskNum();
        Task task = players.getGroup(chatId).getQuest().getTask(task_num);

        String answer = message.getText().toLowerCase();
        String correct_answer = players.getGroup(chatId).getQuest().getTask(task_num).getAnswer().toLowerCase();


        if (answer.equals(correct_answer)){
            sentToAll(players.getGroup(chatId), "Член вашей группы ввел правильный ответ!");

            //todo сделать отправку всей группе сразу

            if (task_num == players.getGroup(chatId).getQuest().getTaskNumbers() - 1){

                markUpSendMessageHandle(chatId, "Это было последнее задание!\nВы прошли квест!", KeyBoards.makeOrMadeQuest());
                players.getGroup(chatId).deleteQuest();
                players.getGroup(chatId).setStep(MAKE_OR_MADE_QUEST_SELECTION);
                players.getGroup(chatId).setGroupName(null);

            } else {
                players.getGroup(chatId).setTaskNum(task_num + 1);
                sendTask(message);
            }

        } else {
            sendMessageHandle(chatId, "Неправильно!");
        }

    }

    public void sendTask(Message message){
        long chatId = message.getChatId();

        int task_num = players.getGroup(chatId).getTaskNum();
        Task task = players.getGroup(chatId).getQuest().getTask(task_num);

        if (task.containsTask())
            sendMessageHandle(chatId, task.getTask());

        if (task.containsPhoto()){
            sendPhotoHandle(chatId, task.getPhotoPath());
        }

        //todo локацию
    }

    public void sentToAll(Group group, String text){
        long chatId;

        for (int i = 0; i < group.getSize(); i++) {
            chatId = group.getUser(i).getChatId();
            sendMessageHandle(chatId, text);
        }
    }

    private int aloneOrGroup(Message message){

        if (message.getText().equals("Один")) {

            sendMessageHandle(message.getChatId(), "Придумайте свое кодовое имя.");

            return ENTERING_GROUPNAME;

        } else if(message.getText().equals("Команда")) {

            sendMessageHandle(message.getChatId(), "Придумайте название команды");

            return ENTERING_GROUPNAME;

        } else if(message.getText().equals("Назад")){

            markUpSendMessageHandle(message.getChatId(), "Выберите:", KeyBoards.makeOrMadeQuest());

            return MAKE_OR_MADE_QUEST_SELECTION;
        } else {

            sendMessageHandle(message.getChatId(), "Неверный выбор.\nПользуйтесь кнопками");

            return SINGLE_OR_GROUP_PLAYING_SELECTION;
        }
    }

    private int makeOrMadeQuest(Message message){

        String text = message.getText();

        switch (text){
            case "Пройти":{

                markUpSendMessageHandle(message.getChatId(), "Вы будете один или в команде?", KeyBoards.aloneOrGroupKeyboard());
                return SINGLE_OR_GROUP_PLAYING_SELECTION;

            }

            case "Создать":{

                long chatId = message.getChatId();
                players.getGroup(chatId).addQuest(chatId);
                players.getGroup(chatId).getQuest().setOnCreating(true);

                markUpSendMessageHandle(chatId, "Введите название квеста.\nПо этому названию люди смогут его найти\n" +
                                "Так же введите приветственное сообщение",
                        KeyBoards.makingQuestKeyBoard());

                return QUEST_FIELDS_SELECTION;
            }

            case "Присоединиться к команде":{
                sendMessageHandle(message.getChatId(), "Введите имя команды");
                return JOINING_EXISTING_GROUP;

            }

            case "Найти квест":{
                sendMessageHandle(message.getChatId(), "Введите название квеста");
                return CHOOSING_QUEST;
            }

            default:{
                markUpSendMessageHandle(message.getChatId(), "Неверный выбор.\nПользуйтесь кнопками", KeyBoards.makeOrMadeQuest());
                return MAKE_OR_MADE_QUEST_SELECTION;
            }
        }
    }

    private int joiningGroup(Message message){
        //сделать проверку на содержание текста в сообщении

        String gotGroupName = message.getText();
        if (players.hasGroup(gotGroupName)){

            players.addToGroup(gotGroupName, message.getChatId());

            sendTask(message);

            return RUNNING_QUEST;

        } else {

            markUpSendMessageHandle(message.getChatId(), "Такой команды не существует", KeyBoards.makeOrMadeQuest());

            return MAKE_OR_MADE_QUEST_SELECTION;
        }
    }

    private int choosingQuest(Message message){

        markUpSendMessageHandle(message.getChatId(), "Выберите квест!", KeyBoards.showQuests());

        return CHOOSING_QUEST;
    }

    private int makingQuest(Message message) {

        String text = message.getText();
        long chatId = message.getChatId();

        switch (text) {

            case "Название квеста": {

                sendMessageHandle(chatId, "Введите название квеста.");
                players.getGroup(chatId).getQuest().setCurrentMakingStep(Quest.NAME);
                break;
            }

            case "Приветственное сообщение": {

                sendMessageHandle(chatId, "Введите приветственное сообщение.");
                players.getGroup(chatId).getQuest().setCurrentMakingStep(Quest.INTRO_MESSAGE);
                break;
            }
            case "Добавить задание":{

                players.getGroup(chatId).getQuest().addTask();
                markUpSendMessageHandle(chatId, "Введите задание и ответ на него\nК заданию может быть " +
                        "прикриплена фотография и геопозиция", KeyBoards.makingTasksKeyboayd());

                return TASK_FIELDS_SELECTION;
            }
            case "Готово": {
                String questName = players.getGroup(chatId).getQuest().getQuestName();
                boolean contains = false;
                try {
                    DBConnector.init();
                    contains = DBConnector.containsQuest(questName);
                    DBConnector.closeDB();
                } catch (ClassNotFoundException e){
                    BotLogger.error(LOGTAG, e);
                }catch (SQLException e){
                    BotLogger.error(LOGTAG, e);
                }

                if (contains) {
                    sendMessageHandle(chatId, "Такое название занято.\nВведите другое.");
                }else{
                    markUpSendMessageHandle(chatId, "Вы создали квест: " + players.getGroup(chatId).getQuest().getQuestName(),
                            KeyBoards.makeOrMadeQuest());

                    players.getGroup(chatId).saveQuest();

                    return MAKE_OR_MADE_QUEST_SELECTION;
                }

            }

            case "Отменить": {

                markUpSendMessageHandle(chatId, "Вы отменили создание квеста", KeyBoards.makeOrMadeQuest());
                players.getGroup(chatId).deleteQuest();

                return MAKE_OR_MADE_QUEST_SELECTION;
            }

            default: {
                markUpSendMessageHandle(message.getChatId(), "Неверный выбор.\nПользуйтесь кнопками", KeyBoards.makingTasksKeyboayd());
            }
        }

        return MAKING_QUEST;
    }

    private  int fillingTasks(Message message){

        String text = message.getText();
        long chatId = message.getChatId();
        switch (text){


            case "Задание":{
                sendMessageHandle(chatId, "Введите задание.");
                players.getGroup(chatId).getQuest().setCurrentMakingStep(Quest.TASK);
                break;
            }

            case "Ответ":{
                sendMessageHandle(chatId, "Введите ответ.");
                players.getGroup(chatId).getQuest().setCurrentMakingStep(Quest.ANSWER);
                break;
            }

            case "Подсказка 1":{
                sendMessageHandle(chatId, "Введите первую подсказку.");
                players.getGroup(chatId).getQuest().setCurrentMakingStep(Quest.HINT_1);
                break;
            }

            case "Подсказка 2":{
                sendMessageHandle(chatId, "Введите вторую подсказку.");
                players.getGroup(chatId).getQuest().setCurrentMakingStep(Quest.HINT_2);
                break;
            }

            case "Фото":{
                sendMessageHandle(chatId, "Добавьте фото к заданию");
                players.getGroup(chatId).getQuest().setCurrentMakingStep(Quest.PHOTO);
                break;
            }

            case "Геолокация":{
                sendMessageHandle(chatId, "Добавьте геолокацию к заданию");
                players.getGroup(chatId).getQuest().setCurrentMakingStep(Quest.LOCATION);
                break;
            }

            case "Готово":{

                int taskNum = players.getGroup(chatId).getTaskNum();
                String task = players.getGroup(chatId).getQuest().getTask(taskNum).getTask();
                String answer = players.getGroup(chatId).getQuest().getTask(taskNum).getAnswer();

                if ( (answer == null) || (task == null) ){
                    markUpSendMessageHandle(chatId, "Не заполнены поля \"Задание\" или \"Ответ\"", KeyBoards.makingTasksKeyboayd());
                    return TASK_FIELDS_SELECTION;
                }

                markUpSendMessageHandle(chatId, "Задание сохранено!",
                        KeyBoards.makingQuestKeyBoard());
                players.getGroup(chatId).getQuest().setCurrentMakingStep(Quest.NONE);

                return QUEST_FIELDS_SELECTION;
            }

            case "Назад":{

                markUpSendMessageHandle(chatId, "Отмена создания задания",
                        KeyBoards.makingQuestKeyBoard());

                players.getGroup(chatId).getQuest().removeLastTask();

                players.getGroup(chatId).getQuest().setCurrentMakingStep(Quest.NONE);

                return QUEST_FIELDS_SELECTION;
            }

            default:{
                markUpSendMessageHandle(message.getChatId(), "Неверный выбор.\nПользуйтесь кнопками", KeyBoards.makingTasksKeyboayd());
            }

        }

        return FILLING_TASKS;
    }

    public void sendMessageHandle(long chatId, String text){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            sendMessage(message);
        } catch (TelegramApiException e){
            BotLogger.error(LOGTAG, e);
        }
    }

    public void markUpSendMessageHandle(long chatId, String text, ReplyKeyboardMarkup replyKeyboardMarkup){
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

