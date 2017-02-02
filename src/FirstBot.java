import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;

import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;


public class FirstBot extends TelegramLongPollingCommandBot {

    public static final int MAKE_OR_MADE_QUEST_SELECTION = 0;
    public static final int SINGLE_OR_GROUP_PLAYING_SELECTION = 1;
    public static final int ENTERING_GROUPNAME = 2;
    public static final int JOINING_EXISTING_GROUP = 3;
    public static final int MAKING_QUEST = 4;
    public static final int RUNNING_QUEST = 5;

    private static final String LOGTAG = "FIRSTBOT";

    private Participants players = new Participants();
    private static final KeyBoards keyBoards = new KeyBoards();

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

        if (!players.contains(chatId)){
            //todo
            //выйти из функции!!
        } else {
            System.out.println("ALREADY REGISTERED");
        }

        System.out.println(update.getMessage().getText());
        SendMessage message = new SendMessage();

        try {
            int step = players.getGroup(chatId).getStep();

            //Позже будет реализовано через switch-case и внутренности условий будут раскиданы по функциям
            if (step == MAKE_OR_MADE_QUEST_SELECTION) {

                players.getGroup(chatId).setStep(makeOrMadeQuest(update.getMessage()));

            }
            else if (step == SINGLE_OR_GROUP_PLAYING_SELECTION) {
                System.out.println(step);
                players.getGroup(chatId).setStep(aloneOrGroup(update.getMessage()));

            } else if (step == ENTERING_GROUPNAME) {

                Message gotMessage = update.getMessage();

                if (gotMessage.hasText()){
                    try {
                        players.setGroupName(chatId, gotMessage.getText());

                        //Удалить
                        SendMessage errorMessage = new SendMessage();
                        errorMessage.setChatId(chatId);
                        errorMessage.setText("Квесты в разработке");
                        errorMessage.setReplyMarkup(keyBoards.MakeOrMadeQuest());
                        sendMessage(errorMessage);

                        players.getGroup(chatId).setStep(MAKE_OR_MADE_QUEST_SELECTION);
                        //todo выбор квеста

                    } catch (Exception e){
                        SendMessage errorMessage = new SendMessage();
                        errorMessage.setChatId(chatId);
                        errorMessage.setText(e.getMessage());
                        sendMessage(errorMessage);

                        BotLogger.error(LOGTAG, e);
                    }

                }
            } else if (step == MAKING_QUEST) {

                System.out.println(message.getText() + step);
                // создать/присоединиться к группе

            } else if (step == JOINING_EXISTING_GROUP){

                players.getGroup(chatId).setStep(joiningGroup(update.getMessage(), chatId));

            } else {
                if (update.hasMessage()) {
                    Message gotMessage = update.getMessage();
                    if (gotMessage.hasText()) {
                        handleIncomingMessage(gotMessage);
                        //обработка ответов
                    }
                }
            }

        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
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

            SendMessage enterNickMessage = new SendMessage();
            enterNickMessage.setChatId(message.getChatId());
            enterNickMessage.setText("Придумайте свое кодовое имя.");
            try {
                sendMessage(enterNickMessage);
            } catch (TelegramApiException e){
                BotLogger.error(LOGTAG, e);
            }

            return ENTERING_GROUPNAME;

        } else if(message.getText().equals("Команда")) {

            SendMessage enterGroupNameMessage = new SendMessage();
            enterGroupNameMessage.setChatId(message.getChatId());
            enterGroupNameMessage.setText("Придумайте название команды.");
            enterGroupNameMessage.getReplyMarkup();
            try {
                sendMessage(enterGroupNameMessage);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }

            return ENTERING_GROUPNAME;

        } else if(message.getText().equals("Назад")){

            SendMessage backMessage = new SendMessage();
            backMessage.setChatId(message.getChatId());
            backMessage.setText("Назад");
            backMessage.setReplyMarkup(keyBoards.MakeOrMadeQuest());

            try {
                sendMessage(backMessage);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }

            return MAKE_OR_MADE_QUEST_SELECTION;
        } else {

            SendMessage errorMessage = new SendMessage();
            errorMessage.setText("Неверный выбор.\nПользуйтесь кнопками");
            errorMessage.setChatId(message.getChatId());

            return SINGLE_OR_GROUP_PLAYING_SELECTION;
        }
    }

    private int makeOrMadeQuest(Message message){

        //Будет реализовано через switch/case
        if (message.getText().equals("Пройти")) {

            SendMessage keyBoardMessage = new SendMessage();
            keyBoardMessage.setReplyMarkup(keyBoards.AloneOrGroupKeyboard());
            keyBoardMessage.setText("Вы будете один или в команде?");
            keyBoardMessage.setChatId(message.getChatId());

            try {
                sendMessage(keyBoardMessage);
            } catch (TelegramApiException e){
                BotLogger.error(LOGTAG, e);
            }

            return SINGLE_OR_GROUP_PLAYING_SELECTION;

        } else if(message.getText().equals("Создать")) {

            SendMessage errorMessage = new SendMessage();
            errorMessage.setText("Не реализовано.");
            errorMessage.setChatId(message.getChatId());

            try {
                sendMessage(errorMessage);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
            //todo


            return MAKE_OR_MADE_QUEST_SELECTION;
            //return MAKING_QUEST;

        } else if(message.getText().equals("Присоединиться к команде")){

            SendMessage enterGroupNameMessage = new SendMessage();
            enterGroupNameMessage.setText("Введите имя команды");
            enterGroupNameMessage.setChatId(message.getChatId());

            try {
                sendMessage(enterGroupNameMessage);
            } catch (TelegramApiException e){
                BotLogger.error(LOGTAG, e);
            }

            return JOINING_EXISTING_GROUP;

        } else {

            SendMessage errorMessage = new SendMessage();
            errorMessage.setText("Неверный выбор.\nПользуйтесь кнопками");
            errorMessage.setChatId(message.getChatId());
            errorMessage.setReplyMarkup(keyBoards.MakeOrMadeQuest());

            try {
                sendMessage(errorMessage);
            } catch (TelegramApiException e){
                BotLogger.error(LOGTAG, e);
            }

            return MAKE_OR_MADE_QUEST_SELECTION;
        }

    }

    public int joiningGroup(Message message, long chatId){
        //сделать проверку на содержание текста в сообщении
        String gotGroupName = message.getText();
        if (players.hasGroup(gotGroupName)){

            players.addToGroup(gotGroupName,chatId);

            //УДАЛИТЬ
            //todo
            SendMessage errorMessage = new SendMessage();
            errorMessage.setText("Потом это сообщение не будет выводиться и вы будеие перенаправлены в квест");
            errorMessage.setChatId(message.getChatId());
            errorMessage.setReplyMarkup(keyBoards.MakeOrMadeQuest());

            try {
                sendMessage(errorMessage);
            } catch (TelegramApiException e){
                BotLogger.error(LOGTAG, e);
            }

            return MAKE_OR_MADE_QUEST_SELECTION;
            //return RUNNING_QUEST;

        } else {

            SendMessage errorMessage = new SendMessage();
            errorMessage.setText("Такой команды не существует");
            errorMessage.setChatId(message.getChatId());
            errorMessage.setReplyMarkup(keyBoards.MakeOrMadeQuest());

            try {
                sendMessage(errorMessage);
            } catch (TelegramApiException e){
                BotLogger.error(LOGTAG, e);
            }

            return MAKE_OR_MADE_QUEST_SELECTION;
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

