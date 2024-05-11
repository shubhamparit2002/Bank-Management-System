
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	private static String url = "jdbc:mysql://localhost:3306/banking_application";
	private static String user = "root";
	private static String pass = "root";

	private static Connection conn = null;

	public static Connection getConnection() throws SQLException {
		if (conn == null) {
			conn = DriverManager.getConnection(url, user, pass);
		}
		return conn;
	}

}
