package main.java.PTCA;
/*
 * Nicholas Bergez
 * nlb49@drexel.edu
 * CS575:Software Design, Class Project
 */

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import java.awt.Dialog.ModalityType;
import java.awt.Window.Type;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Types;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import org.springframework.web.client.RestTemplate;

public class PTCA_UserLogin extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JPasswordField txtPassword;
	private JLabel lblUsername;
	private JComboBox cmbUsers;
	private static DefaultComboBoxModel model;
	private JButton btnNewUser;
	private JLabel lblPassword;
	private JButton btnCancel;
	private JButton btnLogin;
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
        
    private static RestTemplate rt = new RestTemplate();
	
	private static ArrayList<User> userList;
	private JButton btnUpdateUser;
	private PTCA_MainWindow parent;
	
	/**
	 * Create the dialog.
	 */
	public PTCA_UserLogin(PTCA_MainWindow par) {
		setModal(true);
		setResizable(false);
		setTitle("Select User");
		setBounds(100, 100, 360, 121);
		
		//Initialize Data structures
		userList = new ArrayList<User>();
		model = new DefaultComboBoxModel();
		parent = par;
                
		
		//Configure and add content panes and layouts.
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("40dlu"),
				ColumnSpec.decode("50dlu:grow"),
				ColumnSpec.decode("70dlu"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		{
			lblUsername = new JLabel("Username: ");
			contentPanel.add(lblUsername, "1, 2, right, default");
		}
		{
			cmbUsers = new JComboBox(model);
			contentPanel.add(cmbUsers, "2, 2, fill, default");
		}
		{
			btnNewUser = new JButton("New User");
			btnNewUser.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					PTCA_NewUser nuWindow = new PTCA_NewUser(true, PTCA_UserLogin.this);
					nuWindow.pack();
					nuWindow.setVisible(true);
				}
			});
			contentPanel.add(btnNewUser, "3, 2");
		}
		{
			lblPassword = new JLabel("Password: ");
			contentPanel.add(lblPassword, "1, 4, right, default");
		}
		{
			txtPassword = new JPasswordField();
			txtPassword.setEchoChar('*');
			contentPanel.add(txtPassword, "2, 4, fill, default");
			txtPassword.setColumns(10);
		}
		{
			btnUpdateUser = new JButton("Update User");
			btnUpdateUser.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					PTCA_NewUser nuWindow = new PTCA_NewUser(false, PTCA_UserLogin.this);
					nuWindow.pack();
					nuWindow.setVisible(true);
				}
			});
			contentPanel.add(btnUpdateUser, "3, 4");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnCancel = new JButton("Cancel");
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						PTCA_UserLogin.this.setVisible(false);
					}
				});
				btnCancel.setActionCommand("Cancel");
				buttonPane.add(btnCancel);
			}
			{
				btnLogin = new JButton("Login");
				btnLogin.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						if(getSelectedUser().getPassword().equals(txtPassword.getText())){
							parent.userLogin(userList.get(model.getIndexOf(model.getSelectedItem())));
							PTCA_UserLogin.this.setVisible(false);
						}
						else{
							JOptionPane.showMessageDialog(null, "Password incorrect!");
						}
						
					}
				});
				btnLogin.setActionCommand("OK");
				buttonPane.add(btnLogin);
				getRootPane().setDefaultButton(btnLogin);
			}
		}
		
		//Access web service to populate the user dropdown list and local list of users.
                
                User[] ul = rt.getForObject("http://localhost:8080/users", User[].class);
                  
                
                for (int i = 0; i < ul.length; i++){
                    
                    //System.out.printf("%d: username: %s, password: %s./n", ul[i].getUserID(), ul[i].getUsername(), ul[i].getPassword());
                    User temp = new User();
				
			temp.setUserID(ul[i].getUserID());
			temp.setUsername(ul[i].getUsername());
			temp.setPassword(ul[i].getPassword());
				
			userList.add(temp);
				
                        model.addElement(temp.getUsername());
                    
                }
		
		
	}
	
	public User getSelectedUser(){
		return userList.get(cmbUsers.getSelectedIndex());
	}
	
	
	public static void updateUserList(boolean newUser, User theUser){
       
		try{
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection connection = DriverManager.getConnection(PTClientAssist.DB_Connection_String);
			Statement statement = connection.createStatement();
			String query = "";
			
			
			if(newUser){
				//Create a new user to add to the list and update the DB with a new user
                            User user = rt.getForObject("http://localhost:8080/users/add?name=" + theUser.getUsername() + "&pword=" + theUser.getPassword(),User.class);
				
//				query = "INSERT INTO PTCA_USERS VALUES( '" + theUser.getUsername() + "', '" + theUser.getPassword().toString() + "')";
//				
//				statement.executeUpdate(query);
//				
//				query = "SELECT MAX(USERID) AS THEID FROM PTCA_USERS";
//				ResultSet set = statement.executeQuery(query);
//				set.next();
//				
//				int id = set.getInt("THEID");
//				
				userList.add(user);
//				
				model.addElement(user.getUsername());
				//refresh the combobox.
			}
			else{
				//Update the currently selected user.
				User user = rt.getForObject("http://localhost:8080/users/update?id=" + theUser.getUserID()+ "&name=" + theUser.getUsername() + "&pword=" + theUser.getPassword(),User.class);
				//query = "UPDATE PTCA_USERS " +
				//		"SET USERNAME = '" + theUser.getUsername() + "', ENC_PASSWORD = '" + theUser.getPassword().toString() + "'" +
				//		"WHERE USERID = " + theUser.getUserID();
				
				//statement.executeUpdate(query);
				
				int i = model.getIndexOf(model.getSelectedItem());				
				model.removeElementAt(i);
				model.insertElementAt(user.getUsername(), i);
			}
		}
		catch (SQLException ex){
			//print message in a messagepopup
			JOptionPane.showMessageDialog(null, ex.getMessage());
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
	}

}
