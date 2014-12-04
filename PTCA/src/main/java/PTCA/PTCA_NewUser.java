package main.java.PTCA;

/*
 * Nicholas Bergez
 * nlb49@drexel.edu
 * CS575:Software Design, Class Project
 */

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPasswordField;

import java.awt.Dialog.ModalityType;
import java.awt.Window.Type;
import java.util.Arrays;


public class PTCA_NewUser extends JDialog {

	private final JPanel createUserPanel = new JPanel();
	private JTextField txtUsername;
	private JPasswordField pwfPassword;
	private JPasswordField pwfPasswordConfirm;
	private boolean nusr;
	private int usrid;
	
	/**
	 * Create the dialog.
	 */
	public PTCA_NewUser(boolean newUser, PTCA_UserLogin parent) {
		setModal(true);
		nusr = newUser;
		usrid = parent.getSelectedUser().getUserID();
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		if(newUser)
			setTitle("Create New User");
		else
			setTitle("Update User");
		setBounds(100, 100, 254, 156);
		getContentPane().setLayout(new BorderLayout());
		createUserPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(createUserPanel, BorderLayout.CENTER);
		createUserPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JLabel lblUsername = new JLabel("Username");
			createUserPanel.add(lblUsername, "2, 2, right, default");
		}
		{
			txtUsername = new JTextField();
			createUserPanel.add(txtUsername, "4, 2, fill, default");
			txtUsername.setColumns(10);
		}
		{
			JLabel lblPassword = new JLabel("Password");
			createUserPanel.add(lblPassword, "2, 4, right, default");
		}
		{
			pwfPassword = new JPasswordField();
			createUserPanel.add(pwfPassword, "4, 4, fill, default");
			pwfPassword.setColumns(10);
		}
		{
			JLabel lblConfirmPassword = new JLabel("Confirm Password");
			createUserPanel.add(lblConfirmPassword, "2, 6, right, default");
		}
		{
			pwfPasswordConfirm = new JPasswordField();
			createUserPanel.add(pwfPasswordConfirm, "4, 6, fill, default");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnCancel = new JButton("Cancel");
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//Ask if they really want to discard changes.
						if(nusr){
							int response = JOptionPane.showConfirmDialog(PTCA_NewUser.this, "Are you sure you want discard new user?", 
								"Cancel creation?", JOptionPane.YES_NO_OPTION);
							
							if (response == 0){
								PTCA_NewUser.this.setVisible(false);
							}
						}
						else{
							int response = JOptionPane.showConfirmDialog(PTCA_NewUser.this, "Are you sure you want discard changes?", 
									"Cancel changes?", JOptionPane.YES_NO_OPTION);
								
							if (response == 0){
								PTCA_NewUser.this.setVisible(false);
							}
							
						}
						
					}
				});
				btnCancel.setActionCommand("Cancel");
				buttonPane.add(btnCancel);
			}
			{
				//Create/Update User Button init and action listener.
				JButton btnCreateUser = new JButton("Create User");
				if(!newUser)
					btnCreateUser.setText("Update User");
				btnCreateUser.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						//check for just regular password matches and do create/update.
						if(Arrays.equals(pwfPassword.getPassword(),pwfPasswordConfirm.getPassword())){
							
							if(nusr){
								//Make new user and send it to PTCA_UserLogin to update model and DB.
								User temp = new User(0, txtUsername.getText(), pwfPassword.getText());
								PTCA_UserLogin.updateUserList(true, temp);
								PTCA_NewUser.this.setVisible(false);
							}
							else{
								//Update user and send it to PTCA_UserLogin to update model and DB.
								User temp = new User(usrid, txtUsername.getText(), pwfPassword.getText());
								PTCA_UserLogin.updateUserList(false, temp);
								PTCA_NewUser.this.setVisible(false);
							}
						}
						else{
							//let user know their passwords don't match.
							if(!Arrays.equals(pwfPassword.getPassword(),pwfPasswordConfirm.getPassword()))
								JOptionPane.showMessageDialog(PTCA_NewUser.this, "Passwords don't match!");
						}
					}
				});
				btnCreateUser.setActionCommand("OK");
				buttonPane.add(btnCreateUser);
				getRootPane().setDefaultButton(btnCreateUser);
			}
		}
		
		//Initialize fields if the request is an update
		if(!nusr){
			User tmp = new User(parent.getSelectedUser());
			txtUsername.setText(tmp.getUsername());
			pwfPassword.setText(tmp.getPassword());
			pwfPasswordConfirm.setText(tmp.getPassword());
		}
	}

}
