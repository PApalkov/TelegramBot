import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;

public class KeyBoards {

    public ReplyKeyboardMarkup AloneOrGroupKeyboard(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboad(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow KeyboardRow = new KeyboardRow();
        KeyboardRow.add("Один");
        KeyboardRow.add("Команда");

        keyboard.add(KeyboardRow);
        keyboardMarkup.setKeyboard(keyboard);


        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup MakeOrMadeQuest(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboad(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow KeyboardRow = new KeyboardRow();
        KeyboardRow.add("Пройти");
        KeyboardRow.add("Создать");

        keyboard.add(KeyboardRow);
        keyboardMarkup.setKeyboard(keyboard);


        return keyboardMarkup;
    }


}
