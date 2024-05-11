
import java.util.Scanner;

import java.sql.SQLException;

public class MainApp {

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
        	//displaying options to user
            System.out.println("╔════════════════════════════════════╗");
            System.out.println("║     Welcome to banking system      ║");
            System.out.println("║ ---------------------------------- ║");
            System.out.println("║   1. Register                      ║");
            System.out.println("║   2. Login                         ║");
            System.out.println("║   3. Exit                          ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.print("Enter your choice: ");
            int choice1 = scanner.nextInt();
            switch (choice1) {
                case 1:
                	//register new user
                    User.register();
                    break;
                case 2:
                	//login existing user
                    int userID = User.login();
                    InterfaceOptions.firstOption(userID);
                case 3:
                	//exit
                    System.out.println();
                    System.out.println("Thank You! for using our banking system");
                    scanner.close();
                    return;
                default:
                    System.err.println("Enter Valid Choice");
                    break;
            }
        }

    }
}
