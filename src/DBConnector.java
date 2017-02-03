import java.sql.*;
import java.util.ArrayList;

public class DBConnector {
    private static final String DB_NAME = "DataBase.s3db";

    private static Connection connection;
    private static Statement statement;
    //private static ResultSet resultSet;

    public static void init() throws SQLException, ClassNotFoundException {
        connectDB();
        createTasksDB();
        createQuestsDB();
        createUsersDB();
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
                "'inventorID' INTEGER );");

        System.out.println("Quests table is created or already exist.");
    }

    // --------Создание таблицы пользователей--------
    private static void createUsersDB() throws ClassNotFoundException, SQLException
    {
        statement = connection.createStatement();
        statement.execute("CREATE TABLE if not exists 'users' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                "'chatID' INTEGER," +
                "'name' text," +
                "'surname' text," +
                "'groupID' INT);");

        System.out.println("Users table is created or already exist.\n");
    }

    public static void addQuest(Quest quest) throws SQLException {
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        Connection conn = connection;
        try {
            int i=0;
            while(i<quest.getTaskNumbers()) {
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

            stmt1 = conn.prepareStatement("INSERT INTO quests " +
                                                    "(introMessage, questName, inventorID)" +
                                                    "VALUES (?, ?, ?)");
            stmt1.setString(1, quest.getIntroMessage());
            stmt1.setString(2, quest.getQuestName());
            stmt1.setLong(3, quest.getInventorId());
            stmt1.execute();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (stmt1 != null) {
                stmt1.close();
            }
        }
    }

    public static ArrayList<String> getAllQuestsName() throws SQLException {
        ResultSet rs = null;
        ArrayList<String> names = new ArrayList<>();
        Statement stmt = statement;
        try {
            rs = stmt.executeQuery("SELECT questName FROM 'quests'");
            while (rs.next()) {
                names.add(rs.getString(1));
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return names;
    }

    /*
    public Collection<String> getAllQuestsIntroMessage() throws SQLException {
        ResultSet rs = null;
        Collection<String> introMessages = new ArrayList<>();
        Statement stmt = statement;
        try {
            rs = stmt.executeQuery("SELECT introMessage FROM quests");
            while (rs.next()) {
                introMessages.add(rs.getString(0));
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return introMessages;
    }
    */

    /*
    public static void addUser(User current) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(
                    "INSERT INTO users " +
                            "(chatID, name, surname, groupID) " +
                            "VALUES (?, ?, ?, ?)");
            stmt.setLong(1, current.getChatId());
            stmt.setString(2, current.getName());
            stmt.setString(3, current.getSurname());
            stmt.setInt(4, current.getGroupID());
            stmt.execute();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    */

    /*
    public static Collection<Task> getAllQuests() throws ClassNotFoundException, SQLException {
        resultSet = statement.executeQuery("SELECT * FROM quests");
        Collection<Task> quests = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String task = resultSet.getString("task");
            String hint = resultSet.getString("hint");
            String photo = resultSet.getString("photo");
            String answer = resultSet.getString("answer");
            Double latitude = resultSet.getDouble("loc_latitude");
            Double longitude = resultSet.getDouble("loc_longitude");

            quests.add(new Task(task, hint, photo, answer, new Location(latitude, longitude)));


            System.out.println("task: " + task + "\n" +
                                "hint: " + hint + "\n" +
                                "photo: " + photo + "\n" +
                                "answer: " + answer + "\n" +
                                "location is (" + latitude + ", " + longitude + ")");

            //System.out.println("End of quests table\n");
            return quests;
        }
    }
    */



    //todo: доделать этот метод, так как непонятно ничего с User потомучто овощ
    /*
    public ArrayList<User> getAllUsers() throws ClassNotFoundException, SQLException
    {
        resultSet = statement.executeQuery("SELECT * FROM users");
        ArrayList<User> users = new ArrayList<>();

        while(resultSet.next())
        {
            int id = resultSet.getInt("id");
            long chatID = resultSet.getLong("chatID");
            String name = resultSet.getString("name");
            String surname = resultSet.getString("surname");
            int groupID = resultSet.getInt("groupID");
            users.add(new User(chatID, groupID));



            System.out.println("chatID: " + chatID + "\n" +
                    "name: " + name + "\n" +
                    "surname: " + surname);

        }
        return users;
        //System.out.println("End of users table\n");
    }
    */

    /*
    public Collection<User> getUsersFromGroup(int groupID) throws SQLException {
        Collection<User> users = new ArrayList<>();

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement(
                    "SELECT chatID, name, surname, tasknumber" +
                            "FROM users " +
                            "WHERE groupID=?");
            stmt.setInt(1, groupID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                User st = new User(rs.getLong(0), rs.getString(1), rs.getString(2), groupID);
                users.add(st);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return users;
    }
    */

    public static Quest getQuest(String questName) throws SQLException {
        Quest quest;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement("SELECT introMessage, inventorID FROM quests WHERE questName=?");
            stmt.setString(1, questName);
            rs = stmt.executeQuery();

            quest = new Quest(questName, rs.getString(1), rs.getLong(2));
            quest = new Quest();

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
                        new Location(rs.getDouble("loc_latitude"), rs.getDouble("loc_longitude"))
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


    // --------Закрытие--------
    public static void closeDB() throws ClassNotFoundException, SQLException
    {
        connection.close();
        statement.close();
        //resultSet.close();

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
        Quest quest1 = new Quest("this is quest 2 intro", "Quest name 2", list);
        DBConnector.addQuest(quest1);
        //DBConnector.addUser(new User(1488228, 15));
        //DBConnector.addUser(new User(2736134, 81));
        //ArrayList<String> tasks = DBConnector.getAllQuestsName();
        //for(int i=0; i<tasks.size(); ++i) {
        //    System.out.println(tasks.get(i));
        //}
        //DBConnector.getUsersFromDB();

        Quest quest2 = DBConnector.getQuest("Quest name 2");

        System.out.println(quest2.getIntroMessage());

        DBConnector.closeDB();

    }
    */



}
