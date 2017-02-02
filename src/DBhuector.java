import java.sql.SQLException;
import java.util.ArrayList;

public class DBhuector{

    public static ArrayList<String> getAllQuestsName() throws SQLException {
        ArrayList<String> names = new ArrayList<String>();
        names.add("FirstName");
        names.add("SecondName");

        //если пустой
        if (false) {
            throw new SQLException("DICK");
        }

        return names;
    }

}
