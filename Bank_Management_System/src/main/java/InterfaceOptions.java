import java.sql.SQLException;
import java.util.Scanner;

public class InterfaceOptions {
	private static Scanner scanner = new Scanner(System.in);
	private static long account_number;
	public static void firstOption(int userID) throws SQLException {
		if(userID > 0){
            System.out.println("User Logged In!");
            if(!Accounts.isAccountExists(userID)){
            	System.out.println("╔════════════════════════════════════╗");
            	System.out.println("║     What would you like to do?     ║");
            	System.out.println("║ ---------------------------------- ║");
            	System.out.println("║   1. Create new bank account       ║");
            	System.out.println("║   2. Exit                          ║");
            	System.out.println("╚════════════════════════════════════╝");
            	System.out.print("Enter your choice: ");

                int c = scanner.nextInt();
                if(c == 1) {
                    account_number = Accounts.createNewAccount(userID);
                    System.out.println("Account Created Successfully");
                    System.out.println("Your Account Number is: " + account_number);
                }else{
                    return;
                }

            }
            secondOption(userID);
        }
        else{
            System.out.println("Incorrect Email or Password!");
        }
	}
	
	public static void secondOption(int userID) throws SQLException {
		account_number = Accounts.getAccountNumber(userID);
        int choice2 = 0;
        while (choice2 != 5) {
        	System.out.println();
        	System.out.println("Account no: " + account_number);
        	System.out.println("╔════════════════════════════════════╗");
        	System.out.println("║     What would you like to do?     ║");
        	System.out.println("║ ---------------------------------- ║");
        	System.out.println("║   1. Credit Money                  ║");
        	System.out.println("║   2. Debit Money                   ║");
        	System.out.println("║   3. Check Balance                 ║");
        	System.out.println("║   4. Transfer Money                ║");
        	System.out.println("║   5. Print Transactions            ║");
        	System.out.println("║   6. Update Profile                ║");
        	System.out.println("║   7. Log Out                       ║");
        	System.out.println("╚════════════════════════════════════╝");
        	System.out.print("Enter your choice: ");

            choice2 = scanner.nextInt();
            switch (choice2) {
                case 1:
                	AccountTransactions.creditMoney(account_number);
                    break;
                case 2:
                	AccountTransactions.debitMoney(account_number);
                    break;
                case 3:
                    double balance = AccountTransactions.getBalance(account_number);
                    System.out.println("Balance : ₹"+balance);
                    break;
                case 4:
                	AccountTransactions.transferMoney(account_number);
                	break;
                case 5:
                	AccountTransactions.printTransactionHistory(account_number);
                    break;
                case 6:
                	updateOptions(userID);
                	break;
                case 7: 
                	System.out.println("Log out!");
                	System.exit(0);
                	break;
                default:
                    System.out.println("Enter Valid Choice!");
                    break;
            }
        }
	}
	
	public static void updateOptions(int userID) throws SQLException {
		System.out.println();
		System.out.println("╔════════════════════════════════════╗");
		System.out.println("║   What would you like to update?   ║");
		System.out.println("║ ---------------------------------- ║");
		System.out.println("║   1. Name                          ║");
		System.out.println("║   2. Phone                         ║");
		System.out.println("║   3. Email                         ║");
		System.out.println("║   4. Password                      ║");
		System.out.println("║   5. Address                       ║");
		System.out.println("║   6. Exit                          ║");
		System.out.println("╚════════════════════════════════════╝");
		System.out.print("Enter your choice: ");

    	int choice3 = scanner.nextInt();
    	switch (choice3) {
			case 1:
				User.updateProfile("full_name", userID);
				break;
			case 2:
				User.updateProfile("phone", userID);
				break;
			case 3:
				User.updateProfile("email", userID);
				break;
			case 4:
				User.updateProfile("password", userID);
				break;
			case 5:
				User.updateProfile("address", userID);
				break;
			case 6:
				break;
			default:
				break;
		}
	}
}
