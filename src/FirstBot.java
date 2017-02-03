import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

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


                    System.out.println("Got here");
                    markUpSendMessageHandle(chatId, "Вы выбрали квест\nПока отправляем на начало", KeyBoards.makeOrMadeQuest());
                    players.getGroup(chatId).setStep(MAKE_OR_MADE_QUEST_SELECTION);


                    break;
                }
                case RUNNING_QUEST: {

                    //todo delete
                    players.getGroup(chatId).setStep(MAKE_OR_MADE_QUEST_SELECTION);
                    markUpSendMessageHandle(chatId, "Вы дошли до квеста\nПока отправляем на начало", KeyBoards.makeOrMadeQuest());


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

        if (message.getText() == "Один"){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("пидор");
            sendMessage(sendMessage);
        }
        //todo добавить обработку ответов
        /*
        if (!message.isUserMessage() && message.hasText()) {
            if (isCommandForOther(message.getText())) {
                return;
            } else if (message.getText().startsWith("/stop")) {
                sendHideKeyboard(message.getFrom().getId(), message.getChatId(), message.getMessageId());
                return;
            }
        }
        */
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

        //Будет реализовано через switch/case
        if (message.getText().equals("Пройти")) {

            markUpSendMessageHandle(message.getChatId(), "Вы будете один или в команде?", KeyBoards.aloneOrGroupKeyboard());

            return SINGLE_OR_GROUP_PLAYING_SELECTION;

        } else if(message.getText().equals("Создать")) {

            long chatId = message.getChatId();
            players.getGroup(chatId).addQuest(chatId);
            players.getGroup(chatId).getQuest().setOnCreating(true);

            markUpSendMessageHandle(chatId, "Введите название квеста.\nПо этому названию люди смогут его найти\n" +
                    "Так же введите приветственное сообщение",
                    KeyBoards.makingQuestKeyBoard());

            return QUEST_FIELDS_SELECTION;

        } else if(message.getText().equals("Присоединиться к команде")){

            sendMessageHandle(message.getChatId(), "Введите имя команды");

            return JOINING_EXISTING_GROUP;

        } else {

            markUpSendMessageHandle(message.getChatId(), "Неверный выбор.\nПользуйтесь кнопками", KeyBoards.makeOrMadeQuest());

            return MAKE_OR_MADE_QUEST_SELECTION;
        }

    }

    private int joiningGroup(Message message){
        //сделать проверку на содержание текста в сообщении
        String gotGroupName = message.getText();
        if (players.hasGroup(gotGroupName)){

            players.addToGroup(gotGroupName, message.getChatId());

            //todo
            sendMessageHandle(message.getChatId(), "тут будет текущее задание квеста. пока введите любоц символ");

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

                markUpSendMessageHandle(chatId, "Введите задание и ответ на него\nК заданию может быть " +
                        "прикриплена фотография и геопозиция", KeyBoards.makingTasksKeyboayd());
                return TASK_FIELDS_SELECTION;
            }
            case "Готово": {
                //Записать в бд и выйти в главное меню
                markUpSendMessageHandle(chatId, "Вы создали квест: " + players.getGroup(chatId).getQuest().getQuestName(),
                        KeyBoards.makeOrMadeQuest());

                players.getGroup(chatId).getQuest().saveQuest();
                return MAKE_OR_MADE_QUEST_SELECTION;
            }

            case "Отменить": {

                markUpSendMessageHandle(chatId, "Вы отменили создание квеста", KeyBoards.makeOrMadeQuest());
                players.getGroup(chatId).getQuest().cancelMAkingQuest();

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

