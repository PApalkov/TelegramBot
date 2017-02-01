
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;

import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;


public class FirstBot extends TelegramLongPollingCommandBot {

    private static final String LOGTAG = "FIRSTBOT";
    private Participants players = new Participants();

    public FirstBot() {

        register(new StartCommand(players));
        register(new StopCommand());
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
            players.add(chatId);
        } else {
            System.out.println("ALREADY REGISTERED");
        }

        SendMessage message = new SendMessage();
        SendMessage keyBoardMessage = new SendMessage();

        try {
            int step = players.getGroup(chatId).getStep();

            if (step == 0){
                //step = makeOrMadeQuest(update.getMessage());
                //создать квест/ пройти квест
            }
            else if (step == 1) {

                step = aloneOrGroup(update.getMessage());

            } else if (step == 2) {

                message.setReplyMarkup(new KeyBoards().AloneOrGroupKeyboard());
                sendMessage(message);
                //ввести ник свой/ группы
                //если выбрано создать группу, ввод ее названия
                //если название уже занято, сообщить
            } else if (step == 3){

                // создать/присоединиться к группе
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

    @Override
    public String getBotUsername() {
        return "MyFirstBot";
    }

    @Override
    public void onClosing() {}

    @Override
    public String getBotToken() {
        return "TOKEN";
    }

    private int aloneOrGroup(Message message){

        if (message.getText() == "Один") {
            return 2;
        } else if(message.getText() == "Команда"){
            return 3;
        } else {
            return 1;
        }
    }

    private int makeOrMadeQuest(Message message){
        if (message.getText() == "Пройти") {
            return 2;
        } else if(message.getText() == "Создать"){
            return 3;
        } else {
            return 1;
        }
        //возвращаемые числа будут другими
    }
}

