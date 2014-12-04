package main.java.PTCA;
/*
 * Nicholas Bergez
 * nlb49@drexel.edu
 * CS575:Software Design, Class Project
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.BoxLayout;
import javax.swing.AbstractListModel;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

import java.awt.GridLayout;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import org.springframework.web.client.RestTemplate;

public class PTCA_MainWindow extends JFrame {

	private JPanel contentPane;
	private PTCA_UserLogin loginDialog;
	private PTCA_ClientMeasurements measurementDialog;
	
	private JLabel lblImage;
	private JButton btnAddClient;
	private JButton btnMeasurements;
	private JButton btnSwitchUser;
	private JCheckBox chckbxShowInactive;
	
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	
	private User currentUser;
	private JList<Client> clientList;
	private DefaultListModel<Client> listModel;
	private ArrayList<Client> inactiveClientList;
	private static RestTemplate rt = new RestTemplate();
	
	//Updated labels
	private JLabel lblFnameval;
	private JLabel lblMnameval;
	private JLabel lblLnameval;
	private JLabel lblDob;
	private JLabel lblGenderVal;
	private JTextPane txtpnNotes;
	private JList<Condition> listCnd;
	private DefaultListModel<Condition> cndModel;

	/**
	 * Create the frame.
	 */
	public PTCA_MainWindow() {
		
		//Initialize Main window settings.
		setTitle("PT Client Assistant");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 552, 486);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		//Create and initialize components on Client list panel.
		JPanel clientListPanel = new JPanel();
		contentPane.add(clientListPanel, BorderLayout.WEST);
		clientListPanel.setLayout(new BorderLayout(0, 0));
		
		//Create top sections of the client list panel and add components
		JPanel clp_topContent = new JPanel();
		clientListPanel.add(clp_topContent, BorderLayout.NORTH);
		
		JLabel lblClientList = new JLabel("Client List");
		clp_topContent.add(lblClientList);
		
		btnAddClient = new JButton("Add Client");
		btnAddClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Create a new window for PTCA_ClientMeasurements with true and self reference.
				measurementDialog = new PTCA_ClientMeasurements(true, PTCA_MainWindow.this);
				measurementDialog.pack();
				measurementDialog.setVisible(true);
				
			}
		});
		clp_topContent.add(btnAddClient);
		
		//Create bottom section of client list panel and add components.
		JPanel clp_bottomContent = new JPanel();
		clientListPanel.add(clp_bottomContent, BorderLayout.SOUTH);
		
		btnSwitchUser = new JButton("Switch User");
		btnSwitchUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loginDialog.setVisible(true);
			}
		});
		clp_bottomContent.add(btnSwitchUser);
		
		chckbxShowInactive = new JCheckBox("Show Inactive");
		chckbxShowInactive.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(chckbxShowInactive.isSelected()){
					//Add in the inactive users.
					for(int i = 0; i < inactiveClientList.size(); i++){
						listModel.addElement(inactiveClientList.get(i));
					}
				}
				else{
					//remove the inactive users.
					for(int i = 0; i < inactiveClientList.size(); i++){
						listModel.removeElement(inactiveClientList.get(i));
					}
				}
			}
		});
		
		chckbxShowInactive.setEnabled(false);
		//Check box state changed action listener to add that adds/removes the inactives.
		clp_bottomContent.add(chckbxShowInactive);
		
		//Create middle frame and add components.
		JPanel clp_ClientListPanel = new JPanel();
		clientListPanel.add(clp_ClientListPanel, BorderLayout.CENTER);
		
		//JList for selecting users. Will need to customize rendered entries for the list display.
		listModel = new DefaultListModel<Client>();
		clientList = new JList<Client>(listModel);
		clientList.setCellRenderer(new ExampleListCellRenderer());
		clientList.addListSelectionListener(new ClientListSelectionHandler());
		
		clientList.setSelectedIndex(0);
		clp_ClientListPanel.add(clientList);
		clientList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		
		//Initialize Client info display frame and add components.
		JPanel selectedClientInfoPanel = new JPanel();
		contentPane.add(selectedClientInfoPanel, BorderLayout.CENTER);
		selectedClientInfoPanel.setLayout(new GridLayout(2, 0, 0, 0));
		
		//Top panel displays a picture of the selected client.
		JPanel picturePanel = new JPanel();
		selectedClientInfoPanel.add(picturePanel);
		
		lblImage = new JLabel();
		picturePanel.add(lblImage);
		
		
		//Bottom half of the frame displays information for the client.
		JPanel basicClientInfoPanel = new JPanel();
		selectedClientInfoPanel.add(basicClientInfoPanel);
		basicClientInfoPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("right:max(50px;pref):grow"),
				FormFactory.GROWING_BUTTON_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblFirstName = new JLabel("First Name:");
		basicClientInfoPanel.add(lblFirstName, "1, 2, right, default");
		
		lblFnameval = new JLabel("");
		basicClientInfoPanel.add(lblFnameval, "2, 2");
		
		JLabel lblMiddleName = new JLabel("Middle Name:");
		basicClientInfoPanel.add(lblMiddleName, "1, 4, right, default");
		
		lblMnameval = new JLabel("");
		basicClientInfoPanel.add(lblMnameval, "2, 4");
		
		JLabel lblLastName = new JLabel("Last Name:");
		basicClientInfoPanel.add(lblLastName, "1, 6, right, default");
		
		lblLnameval = new JLabel("");
		basicClientInfoPanel.add(lblLnameval, "2, 6");
		
		JLabel lblDateOfBirth = new JLabel("Date of Birth:");
		basicClientInfoPanel.add(lblDateOfBirth, "1, 8, right, default");
		
		lblDob = new JLabel("");
		basicClientInfoPanel.add(lblDob, "2, 8");
		
		JLabel lblGender = new JLabel("Gender:");
		basicClientInfoPanel.add(lblGender, "1, 10, right, default");
		
		lblGenderVal = new JLabel("");
		basicClientInfoPanel.add(lblGenderVal, "2, 10");
		
		JLabel lblNotes = new JLabel("Notes:");
		basicClientInfoPanel.add(lblNotes, "1, 12");
		
		txtpnNotes = new JTextPane();
		txtpnNotes.setEditable(false);
		basicClientInfoPanel.add(txtpnNotes, "2, 12, fill, fill");
		
		JLabel lblConditions = new JLabel("Conditions:");
		basicClientInfoPanel.add(lblConditions, "1, 14");
		
		listCnd = new JList<Condition>();
		cndModel = new DefaultListModel<Condition>();
		listCnd.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listCnd.setModel(cndModel);
		basicClientInfoPanel.add(listCnd, "2, 14, fill, fill");
		
		btnMeasurements = new JButton("Edit Client Info / Input Measurements");
		btnMeasurements.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Create new PTCA_ClientMeasurement window and give it false and self reference.
				measurementDialog = new PTCA_ClientMeasurements(false, PTCA_MainWindow.this);
				measurementDialog.pack();
				measurementDialog.loadClient(clientList.getSelectedValue());
				measurementDialog.setVisible(true);
				
			}
		});
		basicClientInfoPanel.add(btnMeasurements, "2, 16");
				
		//disable Buttons while users not logged in.
		btnAddClient.setEnabled(false);
		btnMeasurements.setEnabled(false);	
		
		//Initialize UserLogin screen;
		loginDialog = new PTCA_UserLogin(PTCA_MainWindow.this);
		loginDialog.pack();
		loginDialog.setVisible(true);
		
		
	}
	
	public void userLogin(User username){
		
		//Clear client list and blank out the other fields.
		clientList.clearSelection();
		
		listModel.clear();
		cndModel.clear();
		lblFnameval.setText("");
		lblMnameval.setText("");
		lblLnameval.setText("");
		lblDob.setText("");
		lblGenderVal.setText("");
		txtpnNotes.setText("");
		lblImage.setIcon(null);
		inactiveClientList = new ArrayList<Client>();
		
		//Set user
		currentUser = new User (username);		
		 
		//Load active client information from webservice into the list model.
		Client[] acl = rt.getForObject("http://localhost:8080/clients?id=" + currentUser.getUserID() +"&active=1", Client[].class);
		for (int i = 0; i < acl.length; i++){
                    Client temp = new Client(acl[i]);
			
			listModel.addElement(new Client(temp));
			//Might need to add to the modelList too.
                    
        }
		//Grab all inactive users and put them in the 'inactive users' list.
			
		Client[] icl = rt.getForObject("http://localhost:8080/clients?id=" + currentUser.getUserID() +"&active=0", Client[].class);
		//inactiveClientList.add(temp);
		for (int i = 0; i < icl.length; i++){
                    Client temp = new Client(icl[i]);
		    inactiveClientList.add(temp);
			   
                }
                btnAddClient.setEnabled(true);
		chckbxShowInactive.setEnabled(true);
	}
	
	public void AddNewClient (Client cl){
		//add given client to the listmodel.
		if(cl != null && cl.isActive()){
			listModel.addElement(cl);
		}
		if(cl != null && !cl.isActive()){
			inactiveClientList.add(cl);
		}
	}
	
	public void UpdateClient (Client cl){
		Client selectedCl = clientList.getSelectedValue();
		//Selected client is active, updated version active
		if(selectedCl.isActive() && cl.isActive()){
			//Update clientList
			clientList.clearSelection();
			listModel.removeElement(selectedCl);
			listModel.addElement(cl);
		}
		
		//Selected client is active, update version inactive
		if(selectedCl.isActive() && !cl.isActive()){
			//Remove from client list, move to inactive list.
			if(chckbxShowInactive.isSelected()){
				inactiveClientList.add(cl);
				clientList.clearSelection();
				listModel.removeElement(selectedCl);
				listModel.addElement(cl);
			}
			else{
				listModel.removeElement(selectedCl);
				inactiveClientList.add(cl);
			}
		}
		//Selected client isn't active, update is active
		if(!selectedCl.isActive() && cl.isActive()){
			//Remove from inactiveClientList, add to clientList
			inactiveClientList.remove(selectedCl);
			listModel.removeElement(selectedCl);
			listModel.addElement(cl);
		}
		//Selected client ins't active, update isn't active			
		if(!selectedCl.isActive() && !cl.isActive()){
			//Update inativeClientList
			inactiveClientList.remove(selectedCl);
			inactiveClientList.add(cl);
			listModel.removeElement(selectedCl);
			listModel.addElement(cl);
		}
	}

	
	private class ExampleListCellRenderer extends DefaultListCellRenderer
	{
	    public Component getListCellRendererComponent(
	        JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	    {
	        JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	        
	        Client client = (Client)value;
	        
	        //Scale and add a border to the image based on the last time a client was scheduled.
	        ImageIcon tmp = new ImageIcon(client.getImageData());
	        
	        Image img = tmp.getImage();
	        
	        img = img.getScaledInstance(50, 50, Image.SCALE_FAST);
	        
	        Border border = new LineBorder (Color.gray, 2);
	        
	        if(client.isActive()){
	        	switch(FindBorderColor(client.getLastScheduled())){
	        	case(0):
	        		border = new LineBorder(Color.green, 2);
	        		break;
	        	case(1):
	        		border = new LineBorder(Color.yellow, 2);
	        		break;
	        	case(2):
	        		border = new LineBorder(Color.red, 2);
	        		break;
	        	}
	        }
	        else
	        	border = new LineBorder(Color.gray, 2);
	        	        
	        tmp.setImage(img);
	        
	        
	        label.setText(client.getFirstName() + " " + client.getLastName());
	        label.setIcon(tmp);
	        label.setBorder(border);
	        
	        return label;

	    }
	}
	
	private class ClientListSelectionHandler implements ListSelectionListener{

		@Override
		public void valueChanged(ListSelectionEvent event) {
			//clientList; cndModel; listCnd;
			//Load selected client values into form.
			if(!clientList.isSelectionEmpty()){
				Client tmp = clientList.getSelectedValue();
				
				lblFnameval.setText(tmp.getFirstName());
				lblMnameval.setText(tmp.getMiddleName());
				lblLnameval.setText(tmp.getLastName());
				lblDob.setText(tmp.getDob());
				lblGenderVal.setText(String.valueOf(tmp.getGender()));
				txtpnNotes.setText(tmp.getNotes());
				
				//Initialize Conditions
				if(tmp.getConditions() == null)
					tmp.setConditions(new ArrayList<Condition>());
				if(!tmp.getConditions().isEmpty()){
					for(int i = 0; i < tmp.getConditions().size(); i++){
						cndModel.addElement(tmp.getConditions().get(i));
					}
				}
				
				//Display picture
				ImageIcon icon = new ImageIcon(tmp.getImageData());
				lblImage.setIcon(icon);
				
				//Enable buttons
				btnMeasurements.setEnabled(true);
			}
		}		
		
	}
	
	private int FindBorderColor(String lastSched){
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String curDate = format.format(Calendar.getInstance().getTime());
		int retVal = 2;
		
		Date last = null;
		Date now = null;
		try {
			last = format.parse(lastSched);
			now = format.parse(curDate);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		long diff = now.getTime() - last.getTime();
		final long msToDays = 86400000;
		double diffDays = diff/msToDays;
		
		if(diffDays < 14)
			retVal = 0;
		
		if(diffDays > 14 && diffDays < 21)
			retVal = 1;
		
		return retVal;
	}
	
	public User getCurrentUser(){
		return this.currentUser;
	}

}
