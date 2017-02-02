import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KeyBoards {


    DBConnector db = new DBConnector();
    private ArrayList<String> questNames;

    public KeyBoards(){

        try {
            this.questNames = db.getAllQuestsName();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ReplyKeyboardMarkup aloneOrGroupKeyboard(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboad(true);

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

    public ReplyKeyboardMarkup makeOrMadeQuest(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboad(true);

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


    public ReplyKeyboardMarkup showQuests(){

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        for (int i = 0; i < questNames.size(); i++) {

            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add(questNames.get(i));
            keyboard.add(keyboardRow);
        }

        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }

}
