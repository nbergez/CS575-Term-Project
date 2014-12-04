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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
//import javax.swing.JOptionPane;

/**
 *
 * @author nbergez
 */
@RestController
public class ConditionController{

	private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private static ArrayList<Condition> condList;
    private Condition cond;
	
	//GET for Condition.
//    @RequestMapping(value="/conditions")
//    public ArrayList<Condition> conditions() {
//        
//        return new ArrayList<Condition>();
//    }    


//POST for new Condition.
    @RequestMapping(value="/conditions/add")
    public Condition addCondition(@RequestParam(value="id" ,defaultValue="1")int id,@RequestParam(value="name" ,defaultValue="Tom")String name) {
        
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
							
	String query = "INSERT INTO PTCA_CONDITION "
            + "VALUES ( " + id + ", '"
            + name + "', '"
            + date + "')"; 
	
        Condition cd = new Condition(name, id ,date);
        
	try{
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		connection = DriverManager.getConnection(Application.DB_Connection_String);
		statement = connection.createStatement();
								
		statement.executeUpdate(query);
								
		statement.close();
		connection.close();
	}
	catch(SQLException ex)
	{
		//JOptionPane.showMessageDialog(null, "SQLException" + ex.getMessage());
	}
	catch (ClassNotFoundException ex) {
								
		//JOptionPane.showMessageDialog(null, ex.getMessage());
	}
        
        return cd;
    }    
}