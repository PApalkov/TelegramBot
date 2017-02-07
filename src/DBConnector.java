import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class DBConnector {
    private static final String DB_NAME = "DataBase.s3db";

    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement stmt;
    private static ResultSet rs;

    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    private static void connectDB() throws ClassNotFoundException, SQLException
    {
        connection = null;
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
        statement = null;
        stmt = null;
        rs = null;

        System.out.println("Successful connection to DB!");
    }

    // --------Создание таблицы квестов--------
    private static void createTasksDB() throws ClassNotFoundException, SQLException
    {
        statement = connection.createStatement();
        statement.execute("CREATE TABLE if not exists 'tasks' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                "'questName' text," +
                "'taskNumber' INT," +
                "'task' text," +
                "'hint1' text," +
                "'hint2' text," +
                "'photoPath' text," +
                "'answer' text," +
                "'loc_latitude' DOUBLE ," +
                "'loc_longitude' DOUBLE );");
    }

    private static void createQuestsDB() throws ClassNotFoundException, SQLException
    {
        statement = connection.createStatement();
        statement.execute("CREATE TABLE if not exists 'quests' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                "'introMessage' text," +
                "'questName' text," +
                "'isAdminsQuest' BOOLEAN," +
                "'inventorID' INTEGER );");
    }

    /*
    // --------Создание таблицы пользователей--------
    private static void createUsersDB() throws ClassNotFoundException, SQLException
    {
        statement = connection.createStatement();
        statement.execute("CREATE TABLE if not exists 'users' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                "'groupName' text" +
                "'chatID' INTEGER);");

        System.out.println("Users table is created or already exist.\n");
    }
    */

    private static void createGroupsDB() throws ClassNotFoundException, SQLException
    {
        statement = connection.createStatement();
        statement.execute("CREATE TABLE if not exists 'groups' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                "'groupName' text," +
                "'questName' text," +
                "'taskNumber' INT);");
    }

    public static void addQuest(Quest quest) throws SQLException, ClassNotFoundException {
        connectDB();
        createQuestsDB();
        createTasksDB();

        boolean isAdminsQuest = false;
        if (quest.getInventorId() == 247579905 || quest.getInventorId() == 135354058) {
            isAdminsQuest = true;
        }
        int i = 0;

        while (i < quest.getTaskNumbers()) {
            stmt = connection.prepareStatement(
                    "INSERT INTO tasks " +
                            "(questName, taskNumber, task, hint1, hint2, photoPath, answer, loc_latitude, loc_longitude) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, quest.getQuestName());
            stmt.setInt(2, quest.getQuest().get(i).getTaskNumber());
            stmt.setString(3, quest.getQuest().get(i).getTask());
            stmt.setString(4, quest.getQuest().get(i).getHint1());
            stmt.setString(5, quest.getQuest().get(i).getHint2());
            stmt.setString(6, quest.getQuest().get(i).getPhotoPath());
            stmt.setString(7, quest.getQuest().get(i).getAnswer());
            stmt.setDouble(8, quest.getQuest().get(i).getLocation().getLatitude());
            stmt.setDouble(9, quest.getQuest().get(i).getLocation().getLongitude());
            stmt.execute();
            ++i;
        }

        stmt = connection.prepareStatement("INSERT INTO quests " +
                                                    "(introMessage, questName, isAdminsQuest, inventorID)" +
                                                    "VALUES (?, ?, ?, ?)");
            stmt.setString(1, quest.getIntroMessage());
            stmt.setString(2, quest.getQuestName());
            stmt.setBoolean(3, isAdminsQuest);
            stmt.setLong(4, quest.getInventorId());
            stmt.execute();

            closeDB();
        }

    /*
    public static void addUser(User user) throws SQLException {
        stmt = null;
        try {
            stmt = connection.prepareStatement(
                    "INSERT INTO users (chatID) VALUES (?) ");
            stmt.setLong(1, user.getChatId());
            stmt.execute();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public static ArrayList<User> getAllUsers() throws SQLException, ClassNotFoundException {
        connectDB();
        createGroupsDB();

        ArrayList<User> users = new ArrayList<>();
        stmt = connection.prepareStatement(
                "SELECT chatID FROM users");
        stmt.execute();
        rs = stmt.executeQuery();

        while (rs.next()) {
            users.add(new User(rs.getLong("chatID")));
        }
        closeDB();
        return users;
    }
    */

    public static Quest getQuest(String questName, boolean isAdminsQuest) throws SQLException, ClassNotFoundException {
        connectDB();
        createQuestsDB();
        createTasksDB();

        Quest quest;
        stmt = connection.prepareStatement("SELECT introMessage, inventorID FROM quests WHERE questName=? AND isAdminsQuest=?");
        stmt.setString(1, questName);
        stmt.setBoolean(2, isAdminsQuest);
        rs = stmt.executeQuery();

        quest = new Quest(questName, rs.getString(1), rs.getLong(2));

        stmt = connection.prepareStatement("SELECT taskNumber, task, hint1, hint2, photoPath, answer, loc_latitude, loc_longitude FROM tasks WHERE questName=? ORDER BY taskNumber");
        stmt.setString(1, questName);
        rs = stmt.executeQuery();
        ArrayList<Task> tasks = new ArrayList<>();

        while(rs.next()) {
            tasks.add(new Task(
                    rs.getInt("taskNumber"),
                    rs.getString("task"),
                    rs.getString("hint1"),
                    rs.getString("hint2"),
                    rs.getString("photoPath"),
                    rs.getString("answer"),
                    new MyLocation(rs.getDouble("loc_latitude"), rs.getDouble("loc_longitude"))
                    )
            );
        }
        quest.setQuest(tasks);
        closeDB();
        return quest;
    }

    public static void deleteQuest(String questName) throws SQLException, ClassNotFoundException {
        connectDB();
        createQuestsDB();
        createTasksDB();

        stmt = connection.prepareStatement("DELETE FROM quests WHERE questName=?");
        stmt.setString(1, questName);
        stmt.execute();

        stmt = connection.prepareStatement("DELETE FROM tasks WHERE questName=?");
        stmt.setString(1, questName);
        stmt.execute();
        closeDB();
    }

    public static boolean containsQuest(String questName) throws SQLException, ClassNotFoundException {
        connectDB();
        createQuestsDB();

        boolean contains = false;
        stmt = connection.prepareStatement("SELECT questName FROM quests");
        rs = stmt.executeQuery();

        while (rs.next()) {
            if (Objects.equals(rs.getString("questName"), questName)) {
                contains = true;
                break;
            }
        }
        closeDB();
        return contains;
    }


    // --------Закрытие--------
    private static void closeDB() throws ClassNotFoundException, SQLException {
        if (connection != null) {
            connection.close();
        }
        if (statement != null) {
            statement.close();
        }
        if (stmt != null) {
            stmt.close();
        }
        if (rs != null) {
            rs.close();
        }

        System.out.println("DB connection closed succesfully.");
    }

    public static Quest getAllQuests(String questName) throws SQLException, ClassNotFoundException {
        connectDB();
        createQuestsDB();
        createTasksDB();

        Quest quest;
        stmt = connection.prepareStatement("SELECT introMessage, inventorID FROM quests WHERE questName=?");
        stmt.setString(1, questName);
        rs = stmt.executeQuery();
            
        quest = new Quest(questName, rs.getString(1), rs.getLong(2));
            
        stmt = connection.prepareStatement("SELECT taskNumber, task, hint1, hint2, photoPath, answer, loc_latitude, loc_longitude FROM tasks WHERE questName=? ORDER BY taskNumber");
        stmt.setString(1, questName);
        rs = stmt.executeQuery();
        ArrayList<Task> tasks = new ArrayList<>();
            
        while(rs.next()) {
            tasks.add(new Task(
                            rs.getInt("taskNumber"),
                            rs.getString("task"),
                            rs.getString("hint1"),
                            rs.getString("hint2"),
                            rs.getString("photoPath"),
                            rs.getString("answer"),
                            new MyLocation(rs.getDouble("loc_latitude"), rs.getDouble("loc_longitude"))
                    )
            );
        }
        quest.setQuest(tasks);
        closeDB();
        return quest;
    }

    public static ArrayList<String> getMadeQuestsName(long chatID) throws SQLException, ClassNotFoundException {
        connectDB();
        createQuestsDB();

        ArrayList<String> names = new ArrayList<>();

        stmt = connection.prepareStatement("SELECT questName FROM quests WHERE inventorID=?");
        stmt.setLong(1, chatID);
        rs = stmt.executeQuery();

        while(rs.next()) {
            names.add(rs.getString("questName"));
        }
        closeDB();
        return names;
    }

    public static Quest getAdminsQuests(String questName) throws SQLException, ClassNotFoundException {
        connectDB();
        createQuestsDB();
        createTasksDB();

        Quest quest;
        stmt = connection.prepareStatement("SELECT introMessage, inventorID FROM quests WHERE questName=? AND isAdminsQuest=?");
        stmt.setString(1, questName);
        stmt.setBoolean(2, true);
        rs = stmt.executeQuery();

        quest = new Quest(questName, rs.getString(1), rs.getLong(2));

        stmt = connection.prepareStatement("SELECT taskNumber, task, hint1, hint2, photoPath, answer, loc_latitude, loc_longitude FROM tasks WHERE questName=? ORDER BY taskNumber");
        stmt.setString(1, questName);
        rs = stmt.executeQuery();
        ArrayList<Task> tasks = new ArrayList<>();

        while(rs.next()) {
            tasks.add(new Task(
                    rs.getInt("taskNumber"),
                    rs.getString("task"),
                    rs.getString("hint1"),
                    rs.getString("hint2"),
                    rs.getString("photoPath"),
                    rs.getString("answer"),
                    new MyLocation(rs.getDouble("loc_latitude"), rs.getDouble("loc_longitude"))
                    )
            );
        }
        quest.setQuest(tasks);
        closeDB();
        return quest;
    }

    public static ArrayList<String> getAllQuestsName() throws SQLException, ClassNotFoundException {
        connectDB();
        createQuestsDB();

        ArrayList<String> names = new ArrayList<>();
        rs = statement.executeQuery("SELECT questName FROM quests");
        while (rs.next()) {
            names.add(rs.getString(1));
        }
        closeDB();
        return names;
    }

    public static ArrayList<String> getAdminsQuestsName() throws SQLException, ClassNotFoundException {
        connectDB();
        createQuestsDB();

        ArrayList<String> names = new ArrayList<>();
        stmt = connection.prepareStatement("SELECT questName FROM quests WHERE isAdminsQuest=?");
        stmt.setBoolean(1, true);
        rs = stmt.executeQuery();
        while (rs.next()) {
            names.add(rs.getString(1));
        }
        closeDB();
        return names;
    }

    public static ArrayList<String> getNotAdminsQuestsName() throws SQLException, ClassNotFoundException {
        connectDB();
        createQuestsDB();

        ArrayList<String> names = new ArrayList<>();
        stmt = connection.prepareStatement("SELECT questName FROM quests WHERE isAdminsQuest=?");
        stmt.setBoolean(1, false);
        rs = stmt.executeQuery();
        while (rs.next()) {
            names.add(rs.getString(1));
        }
        closeDB();
        return names;
    }

    public static boolean hasMadeQuests(long chatID) throws SQLException, ClassNotFoundException {
        connectDB();
        createQuestsDB();

        boolean made = false;
        stmt = connection.prepareStatement("SELECT questName FROM quests WHERE inventorID=?");
        stmt.setLong(1, chatID);
        rs = stmt.executeQuery();
        if (rs != null) {
            made = true;
        }
        closeDB();
        return made;
    }

}
