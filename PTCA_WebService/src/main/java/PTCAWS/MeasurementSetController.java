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
public class MeasurementSetController{

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private static ArrayList<MeasurementSet> msList;
    private MeasurementSet ms;
	
	//GET for MeasurementSet.
    @RequestMapping(value="/ms")
    public ArrayList<MeasurementSet> measurementSets(@RequestParam(value="id" ,defaultValue="1")int id) {
        
        msList = new ArrayList<MeasurementSet>();
        
        try{
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(Application.DB_Connection_String);
                statement = connection.createStatement();

                String query = "SELECT * FROM PTCA_MEASUREMENT_SET " +
                                                "WHERE CLIENT_ID = " + id + " " + 
                                                "ORDER BY DATE_TAKEN DESC";

                resultSet = statement.executeQuery(query);

                while(resultSet.next()){
                        //Populate dropdown list
                        MeasurementSet temp = new MeasurementSet();

                        temp.setMeasureID(resultSet.getInt("MS_ID"));
                        temp.setClientID(resultSet.getInt("CLIENT_ID"));
                        temp.setDate(resultSet.getString("DATE_TAKEN"));
                        temp.setHeightIn(resultSet.getInt("HEIGHTIN"));
                        temp.setWeight(resultSet.getInt("CL_WEIGHT"));
                        temp.setChest(resultSet.getInt("CHEST"));
                        temp.setWaist(resultSet.getInt("WAIST"));
                        temp.setArm(resultSet.getInt("ARM"));
                        temp.setHips(resultSet.getInt("HIPS"));
                        temp.setThigh(resultSet.getInt("THIGH"));
                        temp.setBodyFat(resultSet.getInt("BODYFAT"));
                        temp.setNotes(resultSet.getString("NOTES"));

                        msList.add(temp);
                }


        }
        catch(SQLException ex)
        {
                //JOptionPane.showMessageDialog(null, "SQLException" + ex.getMessage());
        }
        catch (ClassNotFoundException e) {

                //JOptionPane.showMessageDialog(null, e.getMessage());
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
        return msList;
    }    


//POST for new MeasurementSets.
    @RequestMapping(value="/ms/add")
    public MeasurementSet addMS(@RequestParam(value="id" ,defaultValue="1")int id, @RequestParam(value="date", defaultValue="2014-12-03")String date,
            @RequestParam(value="height", defaultValue="72")String height,@RequestParam(value="weight", defaultValue="175")String weight,
            @RequestParam(value="chest", defaultValue="30")String chest,@RequestParam(value="waist", defaultValue="32")String waist,
            @RequestParam(value="arms", defaultValue="12")String arms,@RequestParam(value="hips", defaultValue="38")String hips,
            @RequestParam(value="thigh", defaultValue="24")String thigh,@RequestParam(value="bFat", defaultValue="15")String bFat,
            @RequestParam(value="notes", defaultValue="password")String notes) {
        
        try{
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(Application.DB_Connection_String);
                statement = connection.createStatement();

                MeasurementSet ms = new MeasurementSet();

                ms.setClientID(id);
                ms.setDate(date);
                ms.setHeightIn(Integer.parseInt(height));
                ms.setWeight(Integer.parseInt(weight));
                ms.setChest(Integer.parseInt(chest));
                ms.setWaist(Integer.parseInt(waist));
                ms.setArm(Integer.parseInt(arms));
                ms.setHips(Integer.parseInt(hips));
                ms.setThigh(Integer.parseInt(thigh));
                ms.setBodyFat(Integer.parseInt(bFat));
                ms.setNotes(notes);

                String query = "INSERT INTO PTCA_MEASUREMENT_SET "
                                                + "VALUES ( " + id + ", '" + date + "'," + height
                                                + "," + weight + "," + chest + "," + waist + "," + arms 
                                                + "," + hips + "," + thigh+ "," + bFat + ", '" + notes + "')";

                statement.executeUpdate(query);

                query = "UPDATE PTCA_CLIENTS "
                                + "SET LASTSCHED = '" + date + "' "
                                + "WHERE CLIENT_ID = " + id;

                statement.executeUpdate(query);

        }

        catch(SQLException ex)
        {
               // JOptionPane.showMessageDialog(null, "SQLException" + ex.getMessage());
        }
        catch (ClassNotFoundException ex) {

                //JOptionPane.showMessageDialog(null, ex.getMessage());
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
        
        return ms;
    }    
}