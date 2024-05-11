import java.sql.*;
import java.util.Scanner;

public class AccountTransactions {
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

	public static void creditMoney(long accountNumber) throws SQLException {
		createConnection();
		double amount = getValidAmount();
		String pin = getValidPin();

		if (checkSecurityPin(accountNumber, pin)) {
			String query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
			pst = conn.prepareStatement(query);
			pst.setDouble(1, amount);
			pst.setLong(2, accountNumber);
			int i = pst.executeUpdate();
			if (i > 0) {
				System.out.println("Rs." + amount + " credited Successfully");
				addTransaction(accountNumber, amount, "credit");
				return;
			} else {
				System.out.println("Transaction Failed!");
			}
		} else {
			System.out.println("Invalid Security Pin!");
		}

	}

	public static void debitMoney(long accountNumber) throws SQLException {
		createConnection();
		double amount = getValidAmount();

		String security_pin = getValidPin();

		if (checkSecurityPin(accountNumber, security_pin)) {
			double current_balance = rs.getDouble("balance");
			if (amount <= current_balance) {
				String query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
				pst = conn.prepareStatement(query);
				pst.setDouble(1, amount);
				pst.setLong(2, accountNumber);
				int i = pst.executeUpdate();
				if (i > 0) {
					System.out.println("Rs." + amount + " debited successfully");
					addTransaction(accountNumber, amount, "debit");
					return;
				} else {
					System.out.println("Transaction Failed!");
				}
			} else {
				System.out.println("Insufficient Balance!");
			}
		} else {
			System.out.println("Invalid Pin!");
		}

	}

	public static double getBalance(long account_number) throws SQLException {
		createConnection();
		pst = conn.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ?");
		pst.setLong(1, account_number);
		rs = pst.executeQuery();
		if (rs.next()) {
			double balance = rs.getDouble("balance");
			return balance;
		} else {
			System.out.println("Invalid Pin!");
		}
		return -1;
	}

	private static void addTransaction(long accountNumber, double amount, String transaction_type) throws SQLException {
		createConnection();

		String query = "INSERT INTO transactions (account_number, amount, balance, transaction_type) VALUES (?, ?, ?, ?)";

		double balance = getBalance(accountNumber);
		pst = conn.prepareStatement(query);

		pst.setLong(1, accountNumber);
		pst.setDouble(2, amount);
		pst.setDouble(3, balance);
		pst.setString(4, transaction_type);

		int i = pst.executeUpdate();

		if (i <= 0) {
			System.err.println("Something went wrong");
		}

	}

	public static void printTransactionHistory(long account_number) throws SQLException {
		createConnection();
		String query = "SELECT * FROM transactions where account_number = " + account_number
				+ " order by transaction_date";

		stmt = conn.createStatement();
		rs = stmt.executeQuery(query);

		System.out.println();
		System.out.printf("╔═════════════════════════════════════════════╗%n");
		System.out.printf("║ %-10s | %-8s | %-8s | %-8s ║%n", "Date", "Credit", "Debit", "Balance");
		System.out.printf("║═════════════════════════════════════════════║%n");
		while (rs.next()) {

			String transactionType = rs.getString("transaction_type");
			String amount = String.valueOf(rs.getDouble("amount"));
			String balance = String.valueOf(rs.getDouble("balance"));
			String date = rs.getTimestamp("transaction_date").toString().split(" ")[0];

			// Print the transaction details
			if (transactionType.equals("credit")) {
				System.out.printf("║ %-10s | %-8s | %-8s | %-8s ║%n", date, amount, "-", balance);
			} else {
				System.out.printf("║ %-10s | %-8s | %-8s | %-8s ║%n", date, "-", amount, balance);
			}

		}
		System.out.printf("╚═════════════════════════════════════════════╝%n");
	}

	public static void transferMoney(long senderAccountNumber) throws SQLException {
		createConnection();
		System.out.println();
		System.out.print("Enter account no. of receiver : ");
		long receiverAccountNumber = scanner.nextLong();

		String query = "SELECT * from Accounts WHERE account_number = ?";

		pst = conn.prepareStatement(query);
		pst.setLong(1, receiverAccountNumber);
		rs = pst.executeQuery();

		if (rs.next()) {
			double amount = getValidAmount();

			double balance = getBalance(senderAccountNumber);

			if (!(amount > balance)) {

				String securityPin = getValidPin();

				if (checkSecurityPin(senderAccountNumber, securityPin)) {

					String queryForSender = "update accounts set balance = balance - ? where account_number = ?";
					String queryForReceiver = "update accounts set balance = balance + ? where account_number = ?";

					pst = conn.prepareStatement(queryForSender);
					pst.setDouble(1, amount);
					pst.setLong(2, senderAccountNumber);
					int i = pst.executeUpdate();

					if (i > 0) {
						addTransaction(senderAccountNumber, amount, "debit");
					}

					pst = conn.prepareStatement(queryForReceiver);
					pst.setDouble(1, amount);
					pst.setLong(2, receiverAccountNumber);
					int j = pst.executeUpdate();

					if (j > 0) {
						addTransaction(receiverAccountNumber, amount, "credit");
					}

					if (i > 0 && j > 0) {
						System.out.println("Transfer successful");
					} else {
						System.err.println("Transfer unsuccessful");
					}
				}

			} else {
				System.err.println("Insuffiecient balance");
			}
		} else {
			System.err.println("Account does not exists.");
		}
	}

	private static boolean checkSecurityPin(long accountNumber, String securityPin) throws SQLException {
		pst = conn.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? and security_pin = ? ");
		pst.setLong(1, accountNumber);
		pst.setString(2, securityPin);
		rs = pst.executeQuery();
		return rs.next();
	}

	private static double getValidAmount() {
		double amount;
		do {
			System.out.print("Enter amount : ");
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
