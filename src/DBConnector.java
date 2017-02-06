import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class DBConnector {
    private static final String DB_NAME = "DataBase.s3db";

    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement stmt;
    private static ResultSet rs;

    public static void init() throws SQLException, ClassNotFoundException {
        connectDB();
        createTasksDB();
        createQuestsDB();
        //createUsersDB();
        createGroupsDB();
    }

    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    private static void connectDB() throws ClassNotFoundException, SQLException
    {
        connection = null;
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);

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

        System.out.println("Tasks table is created or already exist.");
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

        System.out.println("Quests table is created or already exist.");
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

        System.out.println("Tasks table is created or already exist.");
    }

    public static void addQuest(Quest quest) throws SQLException {
        stmt = null;
        boolean isAdminsQuest = false;
        if (quest.getInventorId() == 247579905 || quest.getInventorId() == 135354058) {
            isAdminsQuest = true;
        }
        try {
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
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        try {
            stmt = connection.prepareStatement("INSERT INTO quests " +
                                                    "(introMessage, questName, isAdminsQuest, inventorID)" +
                                                    "VALUES (?, ?, ?, ?)");
            stmt.setString(1, quest.getIntroMessage());
            stmt.setString(2, quest.getQuestName());
            stmt.setBoolean(3, isAdminsQuest);
            stmt.setLong(4, quest.getInventorId());
            stmt.execute();

        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
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
    */

    public static ArrayList<User> getAllUsers() throws SQLException {
        stmt = null;
        rs = null;
        ArrayList<User> users = new ArrayList<>();
        try {
            stmt = connection.prepareStatement(
                    "SELECT chatID FROM users");
            stmt.execute();
            rs = stmt.executeQuery();

            while (rs.next()) {
                users.add(new User(rs.getLong("chatID")));
            }

        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return users;
    }

    public static Quest getQuest(String questName, boolean isAdminsQuest) throws SQLException {
        Quest quest;
        stmt = null;
        rs = null;
        try {
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
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return quest;
    }

    public static void deleteQuest(String questName) throws SQLException {
        stmt = null;
        try {
            stmt = connection.prepareStatement("DELETE FROM quests WHERE questName=?");
            stmt.setString(1, questName);
            stmt.execute();

            stmt = connection.prepareStatement("DELETE FROM tasks WHERE questName=?");
            stmt.setString(1, questName);
            stmt.execute();

        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }

    }

    public static boolean containsQuest(String questName) throws SQLException {
        stmt = null;
        rs = null;
        boolean contains = false;
        try {
            stmt = connection.prepareStatement("SELECT questName FROM quests");
            rs = stmt.executeQuery();

            while (rs.next()) {
                if (Objects.equals(rs.getString("questName"), questName)) {
                    contains = true;
                    break;
                }
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return contains;
    }


    // --------Закрытие--------
    public static void closeDB() throws ClassNotFoundException, SQLException {
        connection.close();
        statement.close();
        stmt.close();
        rs.close();

        System.out.println("DB connection closed succesfully.");
    }
/*
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DBConnector.init();

        ArrayList<Task> list = new ArrayList<>();
        Task first_task = new Task(10, "first task", "My hint1", "My hint2", "My photo URL", "My answer",
                new Location(55.55, 37.08));
        Task second_task = new Task(15 , "second task","My hint1", "My hint2", "My photo URL2", "My answer2",
                                         new Location(23.09, 26.19));
        list.add(first_task);
        list.add(second_task);
        Quest quest = new Quest("this is intro", "Quest name 1", list);
        //System.out.println("quest name is " + quest.getQuestName());
        DBConnector.addQuest(quest);
        list.clear();
        Task myTask1 = new Task(20, "Task3", "My hint1", "My hint2", "My photo URL", "My answer",
                new Location(55.55, 37.08));
        Task task1 = new Task(25 , "Task2","My hint1", "My hint2", "My photo URL2", "My answer2",
                new Location(23.09, 26.19));
        list.add(myTask1);
        list.add(task1);
        Quest quest1 = new Quest();
        quest1.setQuest(list);
        quest1.setQuestName("quest 2");
        DBConnector.addQuest(quest1);
        //DBConnector.addUser(new User(1488228, 15));
        //DBConnector.addUser(new User(2736134, 81));
        ArrayList<String> tasks = DBConnector.getAllQuestsName();
        for(int i=0; i<tasks.size(); ++i) {
            System.out.println(tasks.get(i));
        }
        //DBConnector.getUsersFromDB();


        System.out.println(DBConnector.containsQuest("Quest name 1"));
        System.out.println(DBConnector.containsQuest("Quest name 5"));


        DBConnector.closeDB();

    }
    */

    public static Quest getAllQuests(String questName) throws SQLException {
        Quest quest;
        stmt = null;
        rs = null;

        try {
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
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        
        return quest;
    }

    public static ArrayList<String> getMadeQuestsName(long chatID) throws SQLException {
        stmt = null;
        rs = null;
        ArrayList<String> names = new ArrayList<>();

        try {
            stmt = connection.prepareStatement("SELECT questName FROM quests WHERE inventorID=?");
            stmt.setLong(1, chatID);
            rs = stmt.executeQuery();

            while(rs.next()) {
                names.add(rs.getString("questName"));
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return names;
    }

    public static Quest getAdminsQuests(String questName) throws SQLException {
        Quest quest;
        stmt = null;
        rs = null;

        try {
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
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return quest;
    }

    public static ArrayList<String> getAllQuestsName() throws SQLException {
        rs = null;
        ArrayList<String> names = new ArrayList<>();
        try {
            rs = statement.executeQuery("SELECT questName FROM 'quests'");
            while (rs.next()) {
                names.add(rs.getString(1));
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return names;
    }

    public static ArrayList<String> getAdminsQuestsName() throws SQLException {
        rs = null;
        ArrayList<String> names = new ArrayList<>();
        stmt = null;
        try {
            stmt = connection.prepareStatement("SELECT questName FROM quests WHERE isAdminsQuest=?");
            stmt.setBoolean(1, true);
            rs = stmt.executeQuery();
            while (rs.next()) {
                names.add(rs.getString(1));
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return names;
    }

    public static ArrayList<String> getNotAdminsQuestsName() throws SQLException {
        rs = null;
        ArrayList<String> names = new ArrayList<>();
        stmt = null;
        try {
            stmt = connection.prepareStatement("SELECT questName FROM 'quests' WHERE isAdminsQuest=?");
            stmt.setBoolean(1, false);
            rs = stmt.executeQuery();
            while (rs.next()) {
                names.add(rs.getString(1));
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return names;
    }

    public static boolean hasMadeQuests(long chatID) throws SQLException {
        rs = null;
        boolean made = false;
        stmt = null;
        try {
            stmt = connection.prepareStatement("SELECT questName FROM quests WHERE inventorID=?");
            stmt.setLong(1, chatID);
            rs = stmt.executeQuery();
            if (rs != null) {
                made = true;
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return made;
    }

}
