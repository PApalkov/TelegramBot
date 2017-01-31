import org.apache.commons.logging.Log;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.awt.*;

//306909816:AAH3l7NfCtda-OY2au8soBF4b_-ZJ5vBe5g


public class FirstBot extends TelegramLongPollingBot{

    @Override
    public void onUpdateReceived(Update update){

        if(update.hasMessage()){
            Message message = update.getMessage();



            if(message.hasText()){

                SendMessage sendMessageRequest = new SendMessage();
                sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                sendMessageRequest.setText("you said: " + message.getText());
                try {
                    sendMessage(sendMessageRequest); //at the end, so some magic and send the message ;)
                } catch (TelegramApiException e) {

                }
            }

            if (message.hasPhoto()){
                SendPhoto pic = new SendPhoto();
                pic.setChatId(message.getChatId());
                pic.setPhoto(pic.getPhoto());
            }
        }

    }

    @Override
    public String getBotUsername() {
        return "MyFirstBot";
    }

    @Override
    public void onClosing() {
        //todo
    }

    @Override
    public String getBotToken(){
        return "306909816:AAH3l7NfCtda-OY2au8soBF4b_-ZJ5vBe5g";
    }

    public static void main(String[] args) {

        ApiContextInitializer.init();

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new FirstBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println("Fuck");
        }//end catch()

    }//end main()

}
