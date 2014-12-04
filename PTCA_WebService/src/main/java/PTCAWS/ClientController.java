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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author nbergez
 */
@RestController
public class ClientController{

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private static ArrayList<Client> clientList;
    private Client client;
	
	//GET for clients.
    @RequestMapping(value="/clients")
    public ArrayList<Client> clients(@RequestParam(value="id" ,defaultValue="1")int id,@RequestParam(value="active" ,defaultValue="1")int active) {
        clientList = new ArrayList<Client>();
		String query = "";
        //Access DB to populate the user dropdown list and local list of users.
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection(Application.DB_Connection_String);
            statement = connection.createStatement();
			
			query = "SELECT * FROM PTCA_CLIENTS " +
					"WHERE PTCA_CLIENTS.USERID = '" + id + "' " +
					"AND ISACTIVE = " + active;
			
			resultSet = statement.executeQuery(query);
			
			while( resultSet.next()){
				
				Client temp = new Client();
								
				temp.setClientID(resultSet.getInt("CLIENT_ID"));
				temp.setUserID(resultSet.getInt("USERID"));
				temp.setFirstName(resultSet.getString("FIRSTNAME"));
				temp.setLastName(resultSet.getString("LASTNAME"));
				temp.setMiddleName(resultSet.getString("MIDDLENAME"));
				temp.setDob(resultSet.getString("DOB"));
				temp.setLastScheduled(resultSet.getString("LASTSCHED"));
				temp.setGender(resultSet.getString("GENDER").charAt(0));
				temp.setActive(resultSet.getBoolean("ISACTIVE"));
				temp.setNotes(resultSet.getString("NOTES"));
				temp.setConditions(null);
				
				String filepath = resultSet.getString("IMAGE_PATH");
				File imgFile = new File(filepath);
				try{
					byte[] tmpImageData = new byte[(int)imgFile.length()];
					
					FileInputStream fis = new FileInputStream(imgFile);
					fis.read(tmpImageData, 0, (int)imgFile.length());
					
					temp.setImageData(tmpImageData);
					
				}
				catch(FileNotFoundException e){
					//JOptionPane.showMessageDialog(null, e.getMessage());
				} catch (IOException e) {
					//JOptionPane.showMessageDialog(null, e.getMessage());
				}
				
				clientList.add(temp);
			}
			
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
        
        return clientList;
        
    }    


//POST for new clients.
    @RequestMapping(value="/clients/add")
    public Client addClient(@RequestParam(value="UserId" ,defaultValue="1")int UserId, @RequestParam(value="fName", defaultValue="Tom")String fName,
            @RequestParam(value="lName" ,defaultValue="Bombadil")String lName,@RequestParam(value="mName" ,defaultValue="S")String mName,
            @RequestParam(value="dob" ,defaultValue="01-05-2014")String dob,@RequestParam(value="lSched" ,defaultValue="2005-01-05")String lSched,
            @RequestParam(value="active" ,defaultValue="1")String active,@RequestParam(value="notes" ,defaultValue="None!")String notes,
            @RequestParam(value="gender" ,defaultValue="F")char gender,@RequestParam(value="image" ,defaultValue="C:\\img")String image) {
        Client cl = new Client();
							
        cl.setUserID(UserId);
        cl.setFirstName(fName);
        cl.setLastName(lName);
        cl.setMiddleName(mName);
        cl.setDob(dob);
        cl.setLastScheduled(lSched);
        if(active == "1")
            cl.setActive(true);
        else
            cl.setActive(false);
        cl.setNotes(notes);
        cl.setConditions(new ArrayList<Condition>());
        cl.setGender(gender);
        String imagePath = image;

        
        //Add new information to the database

        try{
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(Application.DB_Connection_String);
                statement = connection.createStatement();
                String bit;

                if(cl.isActive())
                        bit = "1";
                else
                        bit = "0";

                String query = "INSERT INTO PTCA_CLIENTS "
                                + "VALUES (" + UserId + ", '" + fName + "', '" + lName + "', '"
                                + mName + "', '" + String.valueOf(gender) + "', '" + dob + "', '2005-01-05', " + bit + ", '"
                                + notes + "', '" + image + "')";

                                
                statement.executeUpdate(query);
                
                //Get the Client ID from the DB for the new client.
                query = "SELECT MAX(CLIENT_ID) AS THEID FROM PTCA_CLIENTS";
                ResultSet set = statement.executeQuery(query);
                set.next();

                int id = set.getInt("THEID");

                cl.setClientID(id);
                
                                
                //Create image data
                File imgF = new File(imagePath);

                byte[] tmpImageData = new byte[(int)imgF.length()];

                FileInputStream fis = new FileInputStream(imgF);
                fis.read(tmpImageData, 0, (int)imgF.length());

                cl.setImageData(tmpImageData);

                                
                set.close();
                statement.close();
                connection.close();

        }
        catch(SQLException ex)
        {
                //JOptionPane.showMessageDialog(null, "SQLException" + ex.getMessage());
        }
        catch (ClassNotFoundException ex) {

                //JOptionPane.showMessageDialog(null, ex.getMessage());
        } catch (FileNotFoundException e1) {
                //JOptionPane.showMessageDialog(null, e1.getMessage());
        } catch (IOException e1) {

                //JOptionPane.showMessageDialog(null, e1.getMessage());
        }
													
	return cl;
    }    

//PUT for updating clients.
    @RequestMapping(value="/clients/update")
    public Client updateClient(@RequestParam(value="id" ,defaultValue="1")int id,@RequestParam(value="fName", defaultValue="Tom")String fName,
            @RequestParam(value="lName" ,defaultValue="Bombadil")String lName,@RequestParam(value="mName" ,defaultValue="S")String mName,
            @RequestParam(value="dob" ,defaultValue="01-05-2014")String dob,
            @RequestParam(value="active" ,defaultValue="1")String active,@RequestParam(value="notes" ,defaultValue="None!")String notes,
            @RequestParam(value="gender" ,defaultValue="F")char gender,@RequestParam(value="image" ,defaultValue="C:\\img")String image) {
                
        Client cl = new Client();
        cl.setFirstName(fName);
        cl.setLastName(lName);
        cl.setMiddleName(mName);
        cl.setDob(dob);
        if(active == "1")
            cl.setActive(true);
        else
            cl.setActive(false);
        cl.setNotes(notes);
        cl.setGender(gender);
        
//        String bit = "0";
//
//        if(cl.isActive())
//                bit = "1";
//        else
//                bit = "0";

        //Generate query string
        String query = "UPDATE PTCA_CLIENTS "
                        + "SET FIRSTNAME='" + cl.getFirstName() + "', "
                        + "LASTNAME='" + cl.getLastName() + "', "
                        + "MIDDLENAME='" + cl.getMiddleName() + "', "
                        + "GENDER='" + String.valueOf(cl.getGender()) + "', "
                        + "DOB='" + cl.getDob() + "', "
                        + "ISACTIVE=" + active + ", "
                        + "NOTES='" + cl.getNotes() + "', ";

        String imagePath = image;
        String ending = "";

        try{
                if(!imagePath.isEmpty()){
                        //Append , 'imagepath') to end of query string
                        File imgF = new File(imagePath);

                        byte[] tmpImageData = new byte[(int)imgF.length()];

                        FileInputStream fis = new FileInputStream(imgF);
                        fis.read(tmpImageData, 0, (int)imgF.length());

                        cl.setImageData(tmpImageData);

                        ending = "IMAGE_PATH='" + imagePath + "' "
                                        + "WHERE CLIENT_ID = " + id;
                }
                else{
                        ending = "WHERE CLIENT_ID = " + id;
                }

                query = query + ending;


                //update database
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(Application.DB_Connection_String);
                statement = connection.createStatement();
                System.out.printf(query);
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
        } catch (FileNotFoundException e1) {
                //JOptionPane.showMessageDialog(null, e1.getMessage());
        } catch (IOException e1) {

                //JOptionPane.showMessageDialog(null, e1.getMessage());
        }
        
        
        return cl;
    }  
}

