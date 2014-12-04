package main.java.PTCA;

/*
 * Nicholas Bergez
 * nlb49@drexel.edu
 * CS575:Software Design, Class Project
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import java.awt.GridLayout;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.jnlp.FileContents;
import javax.jnlp.FileOpenService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

import org.springframework.web.client.RestTemplate;

public class PTCA_ClientMeasurements extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtFirstname;
	private JTextField txtLastname;
	private JTextField txtMiddlename;
	private JTextField txtDob;
	private JTextField txtHeight;
	private JTextField txtWeight;
	private JTextField txtChest;
	private JTextField txtWaist;
	private JTextField txtArms;
	private JTextField txtHips;
	private JTextField txtThigh;
	private JTextField txtBodyFat;
	
	private PTCA_MainWindow parent;
	private Client currentClient;
	private String date;
	private boolean isNew;
	private String currentImagePath = PTClientAssist.Default_Image;
	
	private static RestTemplate rt = new RestTemplate();
	
	private JButton btnAddMeasurements;
	private JComboBox cmbGender;
	private JCheckBox checkBox;
	
	private JLabel lblImage;
	private JTextArea txtaNotes;
	private JButton btnUpdateClient;
	private JButton btnRemoveCondition;
	private JButton btnAddCondition;
	private JTextArea txtaMNotes;
	private JButton btnSaveMeasurements;
	private JButton btnCancel;
	
	private JComboBox<MeasurementSet> comboBox;
	private DefaultComboBoxModel<MeasurementSet> comboModel;
	
	private DefaultListModel<String> condModel;
	private JList<String> jlConditions;
	
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private JButton btnBrowse;

	/**
	 * Create the dialog.
	 */
	public PTCA_ClientMeasurements(boolean isNew, PTCA_MainWindow par) {
		parent = par;
		condModel = new DefaultListModel<String>();
		comboModel = new DefaultComboBoxModel<MeasurementSet>();
		this.isNew = isNew;
		
		setTitle("Client Information/Measurements");
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 766, 543);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 2, 0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			panel.setLayout(new FormLayout(new ColumnSpec[] {
					ColumnSpec.decode("right:max(50dlu;min)"),
					ColumnSpec.decode("left:max(60dlu;min):grow"),
					ColumnSpec.decode("max(67dlu;min)"),},
				new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					RowSpec.decode("133dlu"),
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
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					RowSpec.decode("top:30dlu:grow"),
					FormFactory.RELATED_GAP_ROWSPEC,
					RowSpec.decode("top:15dlu:grow"),
					RowSpec.decode("max(15dlu;min)"),
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,}));
			{
				lblImage = new JLabel("");
				lblImage.setIcon(new ImageIcon("C:\\Users\\nbergez\\Pictures\\PTCA_Images\\nophoto.jpg"));
				panel.add(lblImage, "1, 2, 2, 1");
			}
			{
				btnBrowse = new JButton("Browse...");
				btnBrowse.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						//File chooser for an image file
						try {
							
							JFileChooser chooser = new JFileChooser();
							FileNameExtensionFilter filter = new FileNameExtensionFilter(
							        "JPG, GIF, & PNG Images", "jpg", "gif", "jpeg", "png");
							chooser.setFileFilter(filter);
							int retVal = chooser.showOpenDialog(null);
							
							if (retVal == JFileChooser.APPROVE_OPTION){
								//Update displayed image and current filepath.
								
								File imgF = chooser.getSelectedFile();
								
								byte[] tmpImageData = new byte[(int)imgF.length()];
								
								FileInputStream fis = new FileInputStream(imgF);
								fis.read(tmpImageData, 0, (int)imgF.length());
								
								ImageIcon imgI = new ImageIcon(tmpImageData);
								lblImage.setIcon(imgI);
								
								currentImagePath = imgF.getAbsolutePath();
								
								//JOptionPane.showMessageDialog(null, currentImagePath);
							}
							
							
						} catch (Exception ex){
							JOptionPane.showMessageDialog(null, ex.getMessage());
						}
						
						
						
						
					}
				});
				panel.add(btnBrowse, "3, 2");
			}
			{
				JLabel lblFirstName = new JLabel("First Name: ");
				panel.add(lblFirstName, "1, 4, right, default");
			}
			{
				txtFirstname = new JTextField();
				txtFirstname.setText("");
				panel.add(txtFirstname, "2, 4, fill, default");
				txtFirstname.setColumns(10);
			}
			{
				JLabel lblLastName = new JLabel("Last Name: ");
				panel.add(lblLastName, "1, 6, right, default");
			}
			{
				txtLastname = new JTextField();
				panel.add(txtLastname, "2, 6, fill, default");
				txtLastname.setColumns(10);
			}
			{
				JLabel lblMiddleName = new JLabel("Middle Name: ");
				panel.add(lblMiddleName, "1, 8, right, default");
			}
			{
				txtMiddlename = new JTextField();
				txtMiddlename.setText("");
				panel.add(txtMiddlename, "2, 8, fill, default");
				txtMiddlename.setColumns(10);
			}
			{
				JLabel lblGender = new JLabel("Gender: ");
				panel.add(lblGender, "1, 10, right, default");
			}
			{
				cmbGender = new JComboBox();
				cmbGender.setEnabled(true);
				cmbGender.setEditable(true);
				cmbGender.setModel(new DefaultComboBoxModel(new String[] {"Male", "Female"}));
				panel.add(cmbGender, "2, 10, fill, default");
			}
			{
				JLabel lblDateOfBirth = new JLabel("Date of Birth: ");
				panel.add(lblDateOfBirth, "1, 12, right, default");
			}
			{
				txtDob = new JTextField();
				panel.add(txtDob, "2, 12, fill, default");
				txtDob.setColumns(10);
			}
			{
				JLabel lblActive = new JLabel("Active: ");
				panel.add(lblActive, "1, 14");
			}
			{
				checkBox = new JCheckBox("");
				panel.add(checkBox, "2, 14");
			}
			{
				JLabel lblNotes = new JLabel("Notes:");
				panel.add(lblNotes, "1, 16");
			}
			{
				txtaNotes = new JTextArea();
				panel.add(txtaNotes, "2, 16, 2, 1, fill, top");
			}
			{
				JLabel lblConditions = new JLabel("Conditions: ");
				panel.add(lblConditions, "1, 18");
			}
			{
				jlConditions = new JList();
				jlConditions.setModel(condModel);
				jlConditions.addListSelectionListener(new ConditionListSelectionHandler());
				panel.add(jlConditions, "2, 18, 1, 2, fill, fill");
			}
			{
				btnAddCondition = new JButton("Add Condition");
				btnAddCondition.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						String response = JOptionPane.showInputDialog("Enter the name of the condition:");
						if(!(response == null) && response.length() > 0){
							//Add a conition to DB using web-service
                                                    
							Condition c = rt.getForObject("http://localhost:8080/conditions/add?id=" + currentClient.getClientID()+ "&name=" + response, Condition.class);
							condModel.addElement(response);
						}
					}
				});
				panel.add(btnAddCondition, "3, 18, fill, default");
			}
			{
				btnRemoveCondition = new JButton("Remove Condition");
				btnRemoveCondition.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int index = jlConditions.getSelectedIndex();
						jlConditions.clearSelection();
						condModel.remove(index);
					}
				});
				panel.add(btnRemoveCondition, "3, 19, left, default");
			}
			{
				btnUpdateClient = new JButton("Update Client");
				btnUpdateClient.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						//Will have two paths depending on the newness of the client.
						if(PTCA_ClientMeasurements.this.isNew)
						{
                                                    //Create a new Client based on input and update DB with service
                                                    String bit = "0";
                                                    if(checkBox.isSelected())
                                                        bit = "1";
                                                    char gender;
                                                    if(cmbGender.getSelectedItem().toString() == "Male")
								gender='M';
							else
								gender='F';
                                                    
                                                    String imagePath = currentImagePath;							
                                                    if(imagePath.isEmpty())
                                                        imagePath = PTClientAssist.Default_Image;
                                                        
                                                    String endpoint = "http://localhost:8080/clients/add?UserId=" + parent.getCurrentUser().getUserID() 
                                                            + "&fName=" + txtFirstname.getText() + "&lName=" + txtLastname.getText() + "&mName=" + txtMiddlename.getText()
                                                            + "&dob=" + txtDob.getText()+ "&lSched=2005-01-05&active=" + bit+ "&notes=" + txtaNotes.getText()
                                                            + "&gender=" + gender + "&image="+imagePath;
                                                    Client cl = rt.getForObject(endpoint, Client.class);
							
                                                    //Send a new Client to be added to main window client list. --Keep
                                                    parent.AddNewClient(cl);
                                                    PTCA_ClientMeasurements.this.isNew = false;
							
                                                    //Enable/disable buttons
                                                    btnAddCondition.setEnabled(true);
                                                    btnAddMeasurements.setEnabled(true);
						}
						else{
							//Update the existing client in the DB through the webservice.
							Client cl = new Client(currentClient);
							cl.setFirstName(txtFirstname.getText());
							cl.setLastName(txtLastname.getText());
							cl.setMiddleName(txtMiddlename.getText());
							cl.setDob(txtDob.getText());
							cl.setActive(checkBox.isSelected());
							cl.setNotes(txtaNotes.getText());
							
							if(cmbGender.getSelectedItem().toString() == "Male")
								cl.setGender('M');
							else
								cl.setGender('F');
							
							String bit = "0";
							if(checkBox.isSelected())
								bit = "1";
                                                        
							String imagePath = currentImagePath;
                                                        if(imagePath.isEmpty())
                                                            imagePath = PTClientAssist.Default_Image;
                                                        
							//Call the service
                                                        String endpoint = "http://localhost:8080/clients/update?id=" + cl.getClientID()
                                                            + "&fName=" + cl.getFirstName() + "&lName=" + cl.getLastName() + "&mName=" + cl.getMiddleName()
                                                            + "&dob=" + cl.getDob() + "&active=" + bit + "&notes=" + cl.getNotes()
                                                            + "&gender=" + cl.getGender() + "&image="+imagePath;
                                                        Client temp = rt.getForObject(endpoint, Client.class);
							
                                                        //Call UpdateClient
							
							parent.UpdateClient(temp);
							
						}
					}
				});
				panel.add(btnUpdateClient, "3, 21");
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			panel.setLayout(new FormLayout(new ColumnSpec[] {
					ColumnSpec.decode("right:max(65dlu;min)"),
					ColumnSpec.decode("max(68dlu;min):grow"),
					ColumnSpec.decode("max(67dlu;min)"),},
				new RowSpec[] {
					RowSpec.decode("70dlu"),
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
					FormFactory.DEFAULT_ROWSPEC,}));
			{
				JLabel lblClientMeasurements = new JLabel("Client Measurements");
				lblClientMeasurements.setFont(new Font("Trebuchet MS", Font.PLAIN, 24));
				panel.add(lblClientMeasurements, "1, 2, 3, 1");
			}
			{
				JLabel lblMeasurementSet = new JLabel("Measurement Set: ");
				panel.add(lblMeasurementSet, "1, 4, right, default");
			}
			{
				comboBox = new JComboBox<MeasurementSet>();
				comboBox.setModel(comboModel);
				comboBox.setRenderer(new ComboListCellRenderer());
				comboBox.addItemListener(new comboboxChangeListener());
				panel.add(comboBox, "2, 4, fill, default");
			}
			{
				btnAddMeasurements = new JButton("New Measurements");
				btnAddMeasurements.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						//Add a new item to the combobox and set the selection to it. 
						MeasurementSet ms = new MeasurementSet();
						
						date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
						ms.setDate(date);
						
						comboModel.insertElementAt(ms, 0);
						comboBox.setSelectedIndex(0);
						
						txtHeight.setText("");
						txtWeight.setText("");
						txtChest.setText("");
						txtWaist.setText("");
						txtArms.setText("");
						txtHips.setText("");
						txtThigh.setText("");
						txtBodyFat.setText("");
						txtaMNotes.setText("");
						
						//enable buttons and text fields; disable new measurements button
						comboBox.setEnabled(false);
						btnAddMeasurements.setEnabled(false);
						btnSaveMeasurements.setEnabled(true);
						btnUpdateClient.setEnabled(false);
						btnAddCondition.setEnabled(false);
						btnRemoveCondition.setEnabled(false);
						btnCancel.setEnabled(true);
						txtHeight.setEnabled(true);
				        txtWeight.setEnabled(true);
				        txtChest.setEnabled(true);
				        txtWaist.setEnabled(true);
				        txtArms.setEnabled(true);
				        txtHips.setEnabled(true);
				        txtThigh.setEnabled(true);
				        txtBodyFat.setEnabled(true);
				        txtaMNotes.setEnabled(true);
						
					}
				});
				panel.add(btnAddMeasurements, "3, 4");
			}
			{
				JLabel lblHeightin = new JLabel("Height (in.): ");
				panel.add(lblHeightin, "1, 6, right, default");
			}
			{
				txtHeight = new JTextField();
				panel.add(txtHeight, "2, 6, fill, default");
				txtHeight.setColumns(10);
			}
			{
				JLabel lblWeightlbs = new JLabel("Weight(lbs.): ");
				panel.add(lblWeightlbs, "1, 8, right, default");
			}
			{
				txtWeight = new JTextField();
				txtWeight.setText("");
				panel.add(txtWeight, "2, 8, fill, top");
				txtWeight.setColumns(10);
			}
			{
				JLabel lblChest = new JLabel("Chest: ");
				panel.add(lblChest, "1, 10, right, default");
			}
			{
				txtChest = new JTextField();
				txtChest.setText("");
				panel.add(txtChest, "2, 10, fill, top");
				txtChest.setColumns(10);
			}
			{
				JLabel lblWaist = new JLabel("Waist: ");
				panel.add(lblWaist, "1, 12, right, default");
			}
			{
				txtWaist = new JTextField();
				txtWaist.setText("");
				panel.add(txtWaist, "2, 12, fill, default");
				txtWaist.setColumns(10);
			}
			{
				JLabel lblArm = new JLabel("Arm: ");
				panel.add(lblArm, "1, 14, right, default");
			}
			{
				txtArms = new JTextField();
				txtArms.setText("");
				panel.add(txtArms, "2, 14, fill, default");
				txtArms.setColumns(10);
			}
			{
				JLabel lblHips = new JLabel("Hips: ");
				panel.add(lblHips, "1, 16, right, default");
			}
			{
				txtHips = new JTextField();
				txtHips.setText("");
				panel.add(txtHips, "2, 16, fill, default");
				txtHips.setColumns(10);
			}
			{
				JLabel lblThigh = new JLabel("Thigh: ");
				panel.add(lblThigh, "1, 18, right, default");
			}
			{
				txtThigh = new JTextField();
				txtThigh.setText("");
				panel.add(txtThigh, "2, 18, fill, default");
				txtThigh.setColumns(10);
			}
			{
				JLabel lblBodyFat = new JLabel("Body Fat %: ");
				panel.add(lblBodyFat, "1, 20, right, default");
			}
			{
				txtBodyFat = new JTextField();
				txtBodyFat.setText("");
				panel.add(txtBodyFat, "2, 20, fill, default");
				txtBodyFat.setColumns(10);
			}
			{
				JLabel lblNotes_1 = new JLabel("Notes: ");
				panel.add(lblNotes_1, "1, 22");
			}
			{
				txtaMNotes = new JTextArea();
				txtaMNotes.setText("");
				panel.add(txtaMNotes, "2, 22, 2, 1, fill, fill");
			}
			{
				btnCancel = new JButton("Discard Measurements");
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//remove Measurement Set at 0 and change selection to 1
						comboBox.setSelectedIndex(1);
						comboModel.removeElementAt(0);
						
						//enable/disable fields/buttons
						comboBox.setEnabled(true);
						btnAddMeasurements.setEnabled(true);
						btnSaveMeasurements.setEnabled(false);
						btnUpdateClient.setEnabled(true);
						btnAddCondition.setEnabled(true);
						btnRemoveCondition.setEnabled(true);
						btnCancel.setEnabled(false);
						txtHeight.setEnabled(false);
				        txtWeight.setEnabled(false);
				        txtChest.setEnabled(false);
				        txtWaist.setEnabled(false);
				        txtArms.setEnabled(false);
				        txtHips.setEnabled(false);
				        txtThigh.setEnabled(false);
				        txtBodyFat.setEnabled(false);
				        txtaMNotes.setEnabled(false);
						
					}
				});
				panel.add(btnCancel, "2, 24");
			}
			{
				btnSaveMeasurements = new JButton("Save Measurements");
				btnSaveMeasurements.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//Add Measurement set to the database.
                                            
                                                String endpoint = "http://localhost:8080/ms/add?id=" + currentClient.getClientID() + "&date=" + date
                                                        + "&height=" + txtHeight.getText()+ "&weight=" + txtWeight.getText()
                                                        + "&chest=" + txtChest.getText() + "&waist=" + txtWaist.getText()
                                                        + "&arms=" + txtArms.getText() + "&hips=" + txtHips.getText()
                                                        + "&thigh=" + txtThigh.getText() + "&bFat=" + txtBodyFat.getText()
                                                        + "&notes=" + txtaMNotes.getText();
                                                MeasurementSet ms2 = rt.getForObject(endpoint, MeasurementSet.class);							
						
						//Update model with new MeasurementSet
                                                comboModel.insertElementAt(ms2, 0);
                                                comboBox.setSelectedIndex(0);
                                                comboModel.removeElementAt(1);				
						//enable/disable fields/buttons
						comboBox.setEnabled(true);
						btnAddMeasurements.setEnabled(true);
						btnSaveMeasurements.setEnabled(false);
						btnUpdateClient.setEnabled(true);
						btnAddCondition.setEnabled(true);
						btnRemoveCondition.setEnabled(true);
						btnCancel.setEnabled(false);
						txtHeight.setEnabled(false);
				        txtWeight.setEnabled(false);
				        txtChest.setEnabled(false);
				        txtWaist.setEnabled(false);
				        txtArms.setEnabled(false);
				        txtHips.setEnabled(false);
				        txtThigh.setEnabled(false);
				        txtBodyFat.setEnabled(false);
				        txtaMNotes.setEnabled(false);
				        
				        
						
					}
				});
				panel.add(btnSaveMeasurements, "3, 24");
			}
		}
		
		//Change button labels to reflect new client information.
		btnRemoveCondition.setEnabled(false);
		if(isNew){
			
			btnAddCondition.setEnabled(false);
			btnUpdateClient.setText("Save Client");
			btnSaveMeasurements.setEnabled(false);
			btnAddMeasurements.setEnabled(false);
			btnCancel.setEnabled(false);
			
			txtHeight.setEnabled(false);
	        txtWeight.setEnabled(false);
	        txtChest.setEnabled(false);
	        txtWaist.setEnabled(false);
	        txtArms.setEnabled(false);
	        txtHips.setEnabled(false);
	        txtThigh.setEnabled(false);
	        txtBodyFat.setEnabled(false);
	        txtaMNotes.setEnabled(false);
		}
		
	}
	
	public void loadClient(Client cl){
		currentClient = new Client(cl);
		
		ImageIcon img = new ImageIcon(cl.getImageData());
		lblImage.setIcon(img);
		
		currentImagePath = "";
		
		//load the client into the form. 
		txtFirstname.setText(cl.getFirstName());
		txtLastname.setText(cl.getLastName());
		txtMiddlename.setText(cl.getMiddleName());
		txtDob.setText(cl.getDob());
		checkBox.setSelected(cl.isActive());
		txtaNotes.setText(cl.getNotes());
		
		if(cl.getGender() == 'M')
			cmbGender.setSelectedIndex(0);
		else
			cmbGender.setSelectedIndex(1);
		
		for(int i = 0; i < cl.getConditions().size(); i++){
			condModel.addElement(cl.getConditions().get(i).getType());
		}
		
		
		//Initialize the measurement sets and set initial one to the most recent one taken.
                
                MeasurementSet[] msl = rt.getForObject("http://localhost:8080/ms?id=", MeasurementSet[].class);
                  
                
                for (int i = 0; i < msl.length; i++){
                    
                    //System.out.printf("%d: username: %s, password: %s./n", ul[i].getUserID(), ul[i].getUsername(), ul[i].getPassword());
                    MeasurementSet temp = new MeasurementSet();
				
			temp.setMeasureID(msl[i].getMeasureID());
                        temp.setClientID(msl[i].getClientID());
                        temp.setDate(msl[i].getDate());
                        temp.setHeightIn(msl[i].getHeightIn());
                        temp.setWeight(msl[i].getWeight());
                        temp.setChest(msl[i].getChest());
                        temp.setWaist(msl[i].getWaist());
                        temp.setArm(msl[i].getArm());
                        temp.setHips(msl[i].getHips());
                        temp.setThigh(msl[i].getThigh());
                        temp.setBodyFat(msl[i].getBodyFat());
                        temp.setNotes(msl[i].getNotes());
				
			comboModel.addElement(temp);
                    
                }
		
                btnSaveMeasurements.setEnabled(false);
		btnCancel.setEnabled(false);
			
		//Disable fields
	          
	        txtHeight.setEnabled(false);
	        txtWeight.setEnabled(false);
	        txtChest.setEnabled(false);
	        txtWaist.setEnabled(false);
	        txtArms.setEnabled(false);
	        txtHips.setEnabled(false);
	        txtThigh.setEnabled(false);
	        txtBodyFat.setEnabled(false);
	        txtaMNotes.setEnabled(false);
	        
			
		
	}

	class comboboxChangeListener implements ItemListener{
	    @Override
	    public void itemStateChanged(ItemEvent event) {
	       if (event.getStateChange() == ItemEvent.SELECTED) {
	    	   
	          MeasurementSet item = (MeasurementSet)event.getItem();
	          
	          //Load selected items into form.
	          txtHeight.setText(Integer.toString(item.getHeightIn()));
	          txtWeight.setText(Integer.toString(item.getWeight()));
	          txtChest.setText(Integer.toString(item.getChest()));
	          txtWaist.setText(Integer.toString(item.getWaist()));
	          txtArms.setText(Integer.toString(item.getArm()));
	          txtHips.setText(Integer.toString(item.getHips()));
	          txtThigh.setText(Integer.toString(item.getThigh()));
	          txtBodyFat.setText(Integer.toString(item.getBodyFat()));
	          txtaMNotes.setText(item.getNotes());
	          
	          
	       }
	    }       
	}
	
	private class ComboListCellRenderer extends DefaultListCellRenderer
	{
	    public Component getListCellRendererComponent(
	        JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	    {
	        JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	        
	        if(value != null){
	        	MeasurementSet ms = (MeasurementSet)value;
	        
	        	label.setText(ms.getDate());
	        }
	        return label;

	    }
	}
	
	private class ConditionListSelectionHandler implements ListSelectionListener{

		@Override
		public void valueChanged(ListSelectionEvent event) {
			
			if(!btnRemoveCondition.isEnabled()){
				btnRemoveCondition.setEnabled(true);;
			}
			
				
		}		
		
	}
	
}
