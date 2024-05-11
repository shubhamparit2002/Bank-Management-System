import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private static Connection conn = null;
    private static Scanner scanner;
    private static ResultSet rs = null;
    private static Statement stmt = null;
    private static PreparedStatement pst = null;

    public static void createConnection() throws SQLException {
        if (conn == null)
            conn = DatabaseConnection.getConnection();
        if (scanner == null)
            scanner = new Scanner(System.in);
    }

    public static long createNewAccount(int userID) throws SQLException {
        createConnection();
        //checking if account exists for given user_id
        if (!isAccountExists(userID)) {
            String query = "INSERT INTO Accounts(account_number, user_id, balance, security_pin) VALUES(?, ?, ?, ?)";

            double balance = getValidAmount();

            String securityPin = getValidPin();

            long account_number = generateAccountNumber();

            pst = conn.prepareStatement(query);
            pst.setLong(1, account_number);
            pst.setInt(2, userID);
            pst.setDouble(3, balance);
            pst.setString(4, securityPin);
            int i = pst.executeUpdate();
            if (i > 0) {
                return account_number;
            } else {
                throw new RuntimeException("Account Creation failed!!");
            }
        }
        throw new RuntimeException("Account Already Exist");
    }

    public static long getAccountNumber(int userID) throws SQLException {
        createConnection();
        
       
        String query = "SELECT account_number from Accounts WHERE user_id = ?";

        pst = conn.prepareStatement(query);
        pst.setInt(1, userID);
        rs = pst.executeQuery();
        if (rs.next()) {
            return rs.getLong("account_number");
        }
        throw new RuntimeException("Account Number Doesn't Exist!");
    }

    private static long generateAccountNumber() throws SQLException {
    	 //if the user is first one to create account then he will get account no. 1000000000 else last account number + 1
        stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT account_number from Accounts ORDER BY account_number DESC LIMIT 1");
        if (rs.next()) {
            long lastAccountNo = rs.getLong("account_number");
            return lastAccountNo + 1;
        }
        return 1000000000;
    }

    public static boolean isAccountExists(int user_id) throws SQLException {
        createConnection();
        String query = "SELECT * from Accounts WHERE user_id = ?";
        pst = conn.prepareStatement(query);
        pst.setInt(1, user_id);
        rs = pst.executeQuery();

        return rs.next();
    }

    private static double getValidAmount() {
        double amount;
        do {
            System.out.print("Enter initial amount : ");
            amount = scanner.nextDouble();
            if (amount <= 0) {
                System.err.println("Invalid amount");
            }
        } while (amount <= 0);
        return amount;
    }

    private static String getValidPin() {
        String pin;
        do {
            System.out.print("Enter pin : ");
            pin = scanner.next();
            if (pin.length() != 4) {
                System.err.println("Invalid pin.");
            }
        } while (pin.length() != 4);
        return pin;
    }
}
