import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

//в общем написанные ниже методы работают и работают пиздато (наверно)
//не успел дописать остальные потомучто мозги начали чуток плавиться
//если я забыл заккоментировать какие то другие методы этого класса то сорян (не пользуйся никакими кроме тех что ниже)
//а то короче я такой писал цикл и забыл i++ написать и у меня бд стала весить пару гигов, ебать я охуел :3

//public static void init() - инициализации базы данных (открывает её и всё такое)
//public static void closeDB() - зкрывает БД (обязательно в конце каждой программы)
//public static void addQuest(ArrayList<Task> quest) - добавляет в БД quest, состоящий из заданий собранных в ArrayList
//public static void addUser(User current) - добавление User в БД
//public static Collection<Task> getAllQuests() - выходной ArrayList содержит все квесты, находящиеся в БД
//public Collection<User> getUsersFromGroup(int groupID) - выходная коллекция содержит всех User'ов у которых groupID совпадает
//public Collection<String> getAllQuestsName() - выходная коллекция содержит имена всех квестов
//public Collection<String> getAllQuestsIntroMessage() - выходная коллекция содержит приветственное сообщение во всех квестах


// ====================ГОДНЫЕ========================
//public Quest getQuest(String questName)

//завтра я перепишу код в более нормальный вид, сорян если что(

public class DBConnector {
    private static final String DB_NAME = "DataBase.s3db";

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    public static void init() throws SQLException, ClassNotFoundException {
        connectDB();
        createQuestsDB();
        createUsersDB();
        createQuestsIntroMessagesDB();
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
    private static void createQuestsDB() throws ClassNotFoundException, SQLException
    {
        statement = connection.createStatement();
        statement.execute("CREATE TABLE if not exists 'quests' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                "'task' text," +
                "'hint' text," +
                "'photo' text," +
                "'answer' text," +
                "'loc_latitude' DOUBLE ," +
                "'loc_longitude' DOUBLE ," +
                "'questName' text);");

        System.out.println("Quest table is created or already exist.");
    }

    private static void createQuestsIntroMessagesDB() throws ClassNotFoundException, SQLException
    {
        statement = connection.createStatement();
        statement.execute("CREATE TABLE if not exists 'quests_intro' (" +
                "'questName' text," +
                "'introMessage' text);");

        System.out.println("Quests-names table is created or already exist.");
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
/*
    public static void addQuest(Quest quest) throws SQLException {
        PreparedStatement stmt = null;
        try {
            int i=0;
            while(i < quest.getTaskNumber()) {
                stmt = connection.prepareStatement(
                        "INSERT INTO quests " +
                                "(task, hint, photo, answer, loc_latitude, loc_longitude, questName) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?)");
                stmt.setString(1, quest.get(i).getTask());
                stmt.setString(2, quest.get(i).getHint());
                stmt.setString(3, quest.get(i).getPhoto());
                stmt.setString(4, quest.get(i).getAnswer());
                stmt.setDouble(5, quest.get(i).getLocation().getLatitude());
                stmt.setDouble(6, quest.get(i).getLocation().getLongitude());
                stmt.setString(7, quest.getQuestName());
                stmt.execute();
                ++i;
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
*/
    public ArrayList<String> getAllQuestsName() throws SQLException {
        ResultSet rs = null;
        ArrayList<String> names = new ArrayList<String>();
        Statement stmt = statement;
        try {
                rs = stmt.executeQuery("SELECT questName FROM quests");
                while (rs.next()) {
                    names.add(rs.getString(0));
                }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return names;
    }

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
    public static Collection<Task> getAllQuests() throws ClassNotFoundException, SQLException
    {
        resultSet = statement.executeQuery("SELECT * FROM quests");
        Collection<Task> quests =  new ArrayList<>();

        while(resultSet.next())
        {
            int id = resultSet.getInt("id");
            String task = resultSet.getString("task");
            String hint = resultSet.getString("hint");
            String photo = resultSet.getString("photo");
            String answer = resultSet.getString("answer");
            Double latitude = resultSet.getDouble("loc_latitude");
            Double longitude = resultSet.getDouble("loc_longitude");

            quests.add(new Task(task, hint, photo, answer, new Location(latitude, longitude)));

            /*
            System.out.println("task: " + task + "\n" +
                                "hint: " + hint + "\n" +
                                "photo: " + photo + "\n" +
                                "answer: " + answer + "\n" +
                                "location is (" + latitude + ", " + longitude + ")");


        }

        //System.out.println("End of quests table\n");
        return quests;
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

    public Quest getQuest(String questName) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Quest quest;
        try {
            stmt = connection.prepareStatement(
                    "SELECT task, hint, photo, answer, loc_latitude, loc_longitude" +
                            "FROM quests " +
                            "WHERE questName=?");
            stmt.setString(1, questName);
            rs = stmt.executeQuery();
            ArrayList<Task> tasks = new ArrayList<>();
            while (rs.next()) {
                tasks.add(new Task(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), new Location(rs.getDouble(5), rs.getDouble(6))));
            }

            stmt = connection.prepareStatement("SELECT introMessage FROM quests_intro WHERE questName=?");
            stmt.setString(1, questName);
            rs = stmt.executeQuery();
            quest = new Quest(rs.getString(1), questName, tasks);
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
        resultSet.close();

        System.out.println("DB connection closed succesfully.");
    }

    /*
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DBConnector.connectDB();
        DBConnector.createQuestDB();
        DBConnector.createUsersDB();
        ArrayList<Task> list = new ArrayList<>();
        Task task = new Task("My task", "My hint", "My photo URL", "My answer",
                                        new Location(55.55, 37.08));
        Task task1 = new Task("My task1", "My hint1", "My photo URL1", "My answer1",
                new Location(55.551, 37.081));
        list.add(task);
        list.add(task1);
        DBConnector.addQuest(list);
        DBConnector.addUser(new User(1488228, 15));
        DBConnector.addUser(new User(2736134, 81));
        ArrayList<Task> tasks = DBConnector.getAllQuests();
        for(int i=0; i<tasks.size(); ++i) {
            System.out.println(tasks.get(i).getTask() + " " + tasks.get(i).getAnswer());
        }
        //DBConnector.getUsersFromDB();
        DBConnector.closeDB();
    }
    */

}
