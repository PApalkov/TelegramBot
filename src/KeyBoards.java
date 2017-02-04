import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import javax.swing.text.JTextComponent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KeyBoards {

    public static final String ALONE = Emoji.BOY + " Один " + Emoji.BOY;
    public static final String GROUP = Emoji.FAMILY + " Команда " + Emoji.FAMILY;
    public static final String BACK = Emoji.BACK_WITH_LEFTWARDS_ARROW_ABOVE + " Назад " + Emoji.BACK_WITH_LEFTWARDS_ARROW_ABOVE;
    public static final String READY = Emoji.THUMBS_UP_SIGN + " Готово " + Emoji.THUMBS_UP_SIGN;
    public static final String CANCEL = Emoji.NO_ENTRY_SIGN + " Отменить " + Emoji.NO_ENTRY_SIGN;
    public static final String QUEST_NAME = Emoji.CHEQUERED_FLAG + " Название квеста " + Emoji.CHEQUERED_FLAG;
    public static final String INTRO_MESSAGE = Emoji.ENVELOPE + " Приветственное сообщение " + Emoji.ENVELOPE;
    public static final String ADD_QUEST_TASK = Emoji.BLACK_QUESTION_MARK_ORNAMENT + " Добавить задание " + Emoji.BLACK_QUESTION_MARK_ORNAMENT;
    public static final String TASK = Emoji.BLACK_QUESTION_MARK_ORNAMENT + " Задание " + Emoji.BLACK_QUESTION_MARK_ORNAMENT;
    public static final String ANSWER = Emoji.PARTY_POPPER + " Ответ " + Emoji.PARTY_POPPER;
    public static final String HINT1 = Emoji.SPEECH_BALLOON + " Подсказка 1 " + Emoji.SPEECH_BALLOON;
    public static final String HINT2 = Emoji.SPEECH_BALLOON + " Подсказка 2 " + Emoji.SPEECH_BALLOON;
    public static final String PHOTO = Emoji.SUNRISE + " Фото " + Emoji.SUNRISE;
    public static final String LOCATION = Emoji.ROUND_PUSHPIN + " Геолокация " + Emoji.ROUND_PUSHPIN;

    public KeyBoards(){}

    public static ReplyKeyboardMarkup aloneOrGroupKeyboard(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow firstKeyboardRow = new KeyboardRow();
        firstKeyboardRow.add(ALONE);
        firstKeyboardRow.add(GROUP);

        KeyboardRow secondKeyboardRow = new KeyboardRow();
        secondKeyboardRow.add(BACK);


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
        firstKeyboardRow.add("Пройти квест");
        firstKeyboardRow.add("Создать квест");

        KeyboardRow secondKeyboardRow = new KeyboardRow();
        secondKeyboardRow.add("Найти команду");
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
        questNameKeyBoardRow.add(QUEST_NAME);

        KeyboardRow startMessageKeyBoard = new KeyboardRow();
        startMessageKeyBoard.add(INTRO_MESSAGE);

        KeyboardRow taskKeyBoard = new KeyboardRow();
        taskKeyBoard.add(ADD_QUEST_TASK);

        KeyboardRow readyKeyBoardRow = new KeyboardRow();
        readyKeyBoardRow.add(READY);

        KeyboardRow backKeyboardRow = new KeyboardRow();
        backKeyboardRow.add(CANCEL);


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
        firstKeyboardRow.add(TASK);
        firstKeyboardRow.add(ANSWER);

        KeyboardRow secondKeyboardRow = new KeyboardRow();
        secondKeyboardRow.add(HINT1);
        secondKeyboardRow.add(HINT2);

        KeyboardRow thirdKeyboardRow = new KeyboardRow();
        thirdKeyboardRow.add(PHOTO);
        thirdKeyboardRow.add(LOCATION);

        KeyboardRow readyKeyBoardRow = new KeyboardRow();
        readyKeyBoardRow.add(READY);

        KeyboardRow backKeyboardRow = new KeyboardRow();
        backKeyboardRow.add(BACK);

        keyboard.add(firstKeyboardRow);
        keyboard.add(secondKeyboardRow);
        keyboard.add(thirdKeyboardRow);
        keyboard.add(readyKeyBoardRow);
        keyboard.add(backKeyboardRow);

        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }

}
