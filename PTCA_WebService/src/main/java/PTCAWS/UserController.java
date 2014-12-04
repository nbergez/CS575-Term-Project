package main.java.PTCAWS;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author nbergez
 */
@RestController
public class UserController {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private static ArrayList<User> userList;
    private User user;
    
    //GET for users.
    @RequestMapping(value="/users")
    public ArrayList<User> user() {
        
        userList = new ArrayList<User>();
        //Access DB to populate the user dropdown list and local list of users.
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection(Application.DB_Connection_String);
            statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM PTCA_USERS");
			
			while( resultSet.next()){
				
				User temp = new User();
                                //test = new User();
                            
				temp.setUserID(resultSet.getInt("USERID"));
				temp.setUsername(resultSet.getString("USERNAME"));
				temp.setPassword(resultSet.getString("ENC_PASSWORD"));
				
				userList.add(temp);
			}
			//System.out.print("After loop");
		}
		catch (SQLException ex){
			//print message in a messagepopup
			//JOptionPane.showMessageDialog(null, ex.getMessage());
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		finally{
			try{
				resultSet.close();
				statement.close();
				connection.close();
			}
			catch (Exception ex){
				//print message in a popup
				//JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		}
        
        return userList;
    }    


//POST for new users.
    @RequestMapping(value="/users/add")
    public User addUser(@RequestParam(value="name" ,defaultValue="Tom")String name, @RequestParam(value="pword", defaultValue="password")String pword) {
        
        //Access DB to populate the user dropdown list and local list of users.
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection connection = DriverManager.getConnection(Application.DB_Connection_String);
			Statement statement = connection.createStatement();
			String query = "";
                        
                        query = "INSERT INTO PTCA_USERS VALUES( '" + name + "', '" + pword + "')";
				
			statement.executeUpdate(query);
				
			query = "SELECT MAX(USERID) AS THEID FROM PTCA_USERS";
			ResultSet set = statement.executeQuery(query);
			set.next();
				
			int id = set.getInt("THEID");
			
			user = new User(id, name, pword);
			
		}
		catch (SQLException ex){
			//print message in a messagepopup
			//JOptionPane.showMessageDialog(null, ex.getMessage());
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		finally{
			try{
				resultSet.close();
				statement.close();
				connection.close();
			}
			catch (Exception ex){
				//print message in a popup
				//JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		}
        
        return user;
    }    

//PUT for updating users.
    @RequestMapping(value="/users/update")
    public User updateUser(@RequestParam(value="id" ,defaultValue="1")int id,@RequestParam(value="name" ,defaultValue="Tom")String name, @RequestParam(value="pword", defaultValue="password")String pword) {
        
        String query = "";
        //Access DB to populate the user dropdown list and local list of users.
		try{
			//Update the currently selected user.
				query = "UPDATE PTCA_USERS " +
						"SET USERNAME = '" + name + "', ENC_PASSWORD = '" + pword + "' " +
						"WHERE USERID = " + id;
				
				statement.executeUpdate(query);
				
			}
		
		catch (SQLException ex){
			//print message in a messagepopup
			//JOptionPane.showMessageDialog(null, ex.getMessage());
		}
        
        return new User(id, name, pword);
    }  
}