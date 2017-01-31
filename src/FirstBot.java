
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.File;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.FileReader;


//306909816:AAH3l7NfCtda-OY2au8soBF4b_-ZJ5vBe5g


public class FirstBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasText()) {


                String gotMessage = message.getText();

                switch (message.getText()) {
                    case "/start": {
                        onStart(update);
                        break;
                    }
                    //todo
                    //доделать остальное
                }
            }


            SendMessage sendMessageRequest = new SendMessage();
            sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
            sendMessageRequest.setText("you said: " + message.getText());
            try {
                sendMessage(sendMessageRequest); //at the end, so some magic and send the message ;)
            } catch (TelegramApiException e) {

            }
        }
    }


    @Override
    public String getBotUsername() {
        return "MyFirstBot";
    }

    @Override
    public void onClosing() {

    }

    @Override
    public String getBotToken() {
        return "TOKEN";
    }


    //вывод приветственного сообщения и сообщения
    public void onStart(Update update) {
        // todo
        //завершить активный квест у данного пользователя
        //если он состоит в группе, выкинуть только из группы, а не не всех членов группы
        String introMessage = getInroMessage();

        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(update.getMessage().getChatId());
        sendMessageRequest.setText(introMessage);

        try {
            sendMessage(sendMessageRequest);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }

    }


    private String getInroMessage() {
        String introMessage;
        String fileName = "startMessage.txt";

        //todo
        return "First Message";
    }
/*
        try(FileReader reader = new FileReader("C:\\SomeDir\\notes3.txt"))
        {
            // читаем посимвольно
            int c;
            while((c=reader.read())!=-1){

                System.out.print((char)c);
            }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

*/

}

