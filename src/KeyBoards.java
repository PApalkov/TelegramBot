import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;

public class KeyBoards {

    public KeyBoards(){};

    public ReplyKeyboardMarkup AloneOrGroupKeyboard(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboad(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow firstKeyboardRow = new KeyboardRow();
        firstKeyboardRow.add("Один");
        firstKeyboardRow.add("Команда");

        KeyboardRow secondKeyboardRow = new KeyboardRow();
        secondKeyboardRow.add("Назад");


        keyboard.add(firstKeyboardRow);
        keyboard.add(secondKeyboardRow);

        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }


    public ReplyKeyboardMarkup MakeOrMadeQuest(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboad(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow firstKeyboardRow = new KeyboardRow();
        firstKeyboardRow.add("Пройти");
        firstKeyboardRow.add("Создать");

        KeyboardRow secondKeyboardRow = new KeyboardRow();
        secondKeyboardRow.add("Присоединиться к команде");


        keyboard.add(firstKeyboardRow);
        keyboard.add(secondKeyboardRow);

        keyboardMarkup.setKeyboard(keyboard);



        return keyboardMarkup;
    }


}
