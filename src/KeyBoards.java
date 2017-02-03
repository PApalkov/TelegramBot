import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import javax.swing.text.JTextComponent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KeyBoards {

    private static ArrayList<String> questNames;

    public KeyBoards(){

        try {
            DBConnector.init();
            this.questNames = DBConnector.getAllQuestsName();
            DBConnector.closeDB();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException b){
            System.out.println(b.getMessage());
        }

    }

    public static ReplyKeyboardMarkup aloneOrGroupKeyboard(){
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

    public static ReplyKeyboardMarkup makeOrMadeQuest(){
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
        secondKeyboardRow.add("Найти квест");

        keyboard.add(firstKeyboardRow);
        keyboard.add(secondKeyboardRow);

        keyboardMarkup.setKeyboard(keyboard);



        return keyboardMarkup;
    }

    public static ReplyKeyboardMarkup showQuests() {

        ArrayList<String> questNames = new ArrayList<String>();

        try {
            DBConnector.init();
            questNames = DBConnector.getAllQuestsName();
            DBConnector.closeDB();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException b) {
            System.out.println(b.getMessage());
        }


        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        if (questNames.size() == 0) {
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add("Назад");
        } else {
            for (int i = 0; i < questNames.size(); i++) {

                KeyboardRow keyboardRow = new KeyboardRow();
                keyboardRow.add(questNames.get(i));
                keyboard.add(keyboardRow);
            }
        }
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }

    public static ReplyKeyboardMarkup makingQuestKeyBoard(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow questNameKeyBoardRow = new KeyboardRow();
        questNameKeyBoardRow.add("Название квеста");

        KeyboardRow startMessageKeyBoard = new KeyboardRow();
        startMessageKeyBoard.add("Приветственное сообщение");

        KeyboardRow taskKeyBoard = new KeyboardRow();
        taskKeyBoard.add("Добавить задание");

        KeyboardRow readyKeyBoardRow = new KeyboardRow();
        readyKeyBoardRow.add("Готово");

        KeyboardRow backKeyboardRow = new KeyboardRow();
        backKeyboardRow.add("Отменить");


        keyboard.add(questNameKeyBoardRow);
        keyboard.add(startMessageKeyBoard);
        keyboard.add(taskKeyBoard);
        keyboard.add(readyKeyBoardRow);
        keyboard.add(backKeyboardRow);

        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }

    public static ReplyKeyboardMarkup makingTasksKeyboayd(){

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow firstKeyboardRow = new KeyboardRow();
        firstKeyboardRow.add("Задание");
        firstKeyboardRow.add("Ответ");

        KeyboardRow secondKeyboardRow = new KeyboardRow();
        secondKeyboardRow.add("Подсказка 1");
        secondKeyboardRow.add("Подсказка 2");

        KeyboardRow thirdKeyboardRow = new KeyboardRow();
        thirdKeyboardRow.add("Фото");
        thirdKeyboardRow.add("Геолокация");

        KeyboardRow readyKeyBoardRow = new KeyboardRow();
        readyKeyBoardRow.add("Готово");

        KeyboardRow backKeyboardRow = new KeyboardRow();
        backKeyboardRow.add("Назад");

        keyboard.add(firstKeyboardRow);
        keyboard.add(secondKeyboardRow);
        keyboard.add(thirdKeyboardRow);
        keyboard.add(readyKeyBoardRow);
        keyboard.add(backKeyboardRow);

        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }

}
