

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    private static Connection conn = null;
    private static Scanner scanner = null;
    private static PreparedStatement pst = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;
    

    public static void createConnection() throws SQLException{
    	//initializing connection and scanner object only if they are null
    	if(conn == null) conn = DatabaseConnection.getConnection();
    	if(scanner == null) scanner = new Scanner(System.in);
    }

    public static void register() throws SQLException{
    	
    	createConnection();
        System.out.print("Full Name: ");
        String full_name = scanner.nextLine();
       
        String phone = "";
        do{
        	System.out.print("Phone : ");
            phone = scanner.next();
            if(phone.length() != 10) {
            	System.err.println("Invalid Phone Number!");
            }
        }while(phone.length() != 10);
        scanner.nextLine();
        System.out.print("Address : ");
        String address = scanner.nextLine();
        
        String email = "";
        do{
        	System.out.print("Email: ");
            email = scanner.next();
            
            if(!isValidEmail(email)) {
            	System.err.println("Invalid email!");
            }
        }while(!isValidEmail(email));
        
        scanner.nextLine();
        
        String password = "";
        do {
        	System.out.print("Password: ");
            password = scanner.nextLine();
            if(password.length() < 8) {
            	System.err.println("Password length should be greater than or equal to 8 characters");
            }
        }while(password.length() < 8);
        
        //checking if the user already exists
        if(isUserExists(email)) {
            System.out.println("User Already Exists!");
            return;
        }
        
        //query to insert user data to database
        String query = "INSERT INTO users (full_name, phone, address, email, password) VALUES (?,?,?,?,?);";
     
        pst = conn.prepareStatement(query);
        pst.setString(1, full_name);
        pst.setString(2, phone);
        pst.setString(3, address);
        pst.setString(4, email);
        pst.setString(5, password);
        
        int affectedRows = pst.executeUpdate();
        if (affectedRows > 0) {
            System.out.println("Registration Successfull!\n");
        } else {
            System.out.println("Registration Failed!\ns");
        }
       
    }

    public static int login() throws SQLException{
    	createConnection();
    	String email = "";
    	
    	//validation user email using regular expressions
        do{
        	System.out.print("Email: ");
            email = scanner.nextLine().trim();
            
            if(!isValidEmail(email)) {
            	System.err.println("Invalid email!");
            }
        }while(!isValidEmail(email));
        
        //checking if the password contains 8 characters
        String password = "";
        do {
        	System.out.print("Password: ");
            password = scanner.nextLine();
            if(password.length() < 8) {
            	System.err.println("Password length should be greater than or equal to 8 characters");
            }
        }while(password.length() < 8);
        
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
     
        pst = conn.prepareStatement(query);
        pst.setString(1, email);
        pst.setString(2, password);
        rs = pst.executeQuery();
        
        //if user exist then returning user_id of the user
        if(rs.next()){
            return rs.getInt("user_id");
        }
        return -1;
    }

    public static boolean isUserExists(String email) throws SQLException{
    	//checking if the user is already in the database
        String query = "SELECT * FROM users WHERE email = ?";
        pst = conn.prepareStatement(query);
        pst.setString(1, email);
        ResultSet resultSet = pst.executeQuery();
        if(resultSet.next()){
            return true;
        }
        return false;
    }


	public static void updateProfile(String column, int userID) throws SQLException {
		//updating user's data according to column in database
		createConnection();
		String password = "";
        do {
        	System.out.print("Enter password: ");
            password = scanner.nextLine();
            if(password.length() < 8) {
            	System.err.println("Password length should be greater than or equal to 8 characters");
            }
        }while(password.length() < 8);
        
        String query = "select * from users where user_id="+userID;
        stmt = conn.createStatement();
        rs = stmt.executeQuery(query);
        
        String updateQuery = "update users set "+column+" = ? where user_id = "+userID;
        pst = conn.prepareStatement(updateQuery);
        int i;
        if(rs.next()) {
        if(password.equals(rs.getString("password"))) {
        	switch (column) {
			case "full_name":
				System.out.print("Enter name : ");
				String name = scanner.nextLine();
				pst.setString(1, name);
				i = pst.executeUpdate();
				if(i > 0) {
					System.out.println("Name updated! " + rs.getString("full_name") + " -> "+ name);
				}
				break;
			case "email":
				System.out.print("Enter email : ");
				String email = scanner.next();
				pst.setString(1, email);
				i = pst.executeUpdate();
				if(i > 0) {
					System.out.println("Email updated! "+ rs.getString("email") + " -> "+ email);
				}
				break;
			case "password":
				String pass = "";
		        do {
		        	System.out.print("Password: ");
		            pass = scanner.nextLine();
		            if(pass.length() < 8) {
		            	System.err.println("Password length should be greater than or equal to 8 characters");
		            }
		        }while(password.length() < 8);
				pst.setString(1, pass);
				i = pst.executeUpdate();
				if(i > 0) {
					System.out.println("Password updated! "+ rs.getString("password") + " -> "+ password);
				}
				break;
			case "phone":
				 String phone = "";
			        do{
			        	System.out.print("Phone : ");
			            phone = scanner.next();
			            if(phone.length() != 10) {
			            	System.err.println("Invalid Phone Number!");
			            }
			        }while(phone.length() != 10);
				pst.setString(1, phone);
				i = pst.executeUpdate();
				if(i > 0) {
					System.out.println("Phone updated! "+ rs.getString("phone") + " -> "+ phone);
				}
				break;
			case "address":
				System.out.print("Enter address : ");
				String address = scanner.nextLine();
				pst.setString(1, address);
				i = pst.executeUpdate();
				if(i > 0) {
					System.out.println("Address updated! "+ rs.getString("address") + " -> "+ address);
				}
				break;
			default:
				break;
			}
        }else {
        	System.err.println("Wrong password!");
        }}
        
		
	}
	
	 public static boolean isValidEmail(String email) {
		//validating email id  using regular expression
	 	final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
	 }

	
}
