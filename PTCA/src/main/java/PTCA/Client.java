package main.java.PTCA;
/*
 * Nicholas Bergez
 * nlb49@drexel.edu
 * CS575:Software Design, Class Project
 */

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Client {
	
	//Private Members
	private int clientID;
	private int userID;
	private String firstName;
	private String lastName;
	private String middleName;
	private String lastScheduled;
	private char gender;
	private String dob;
	private boolean isActive;
	private byte[] imageData;
	private ArrayList<Condition> conditions;
	private String notes;
	
	//Constructors
	public Client(Client cl){
		
		this(cl.getClientID(), cl.getUserID(), cl.getFirstName(), cl.getLastName(), cl.getMiddleName(), cl.getLastScheduled(), cl.getGender(), cl.getDob(),
				cl.isActive(), cl.getImageData(), cl.getConditions(), cl.getNotes());
		
	}
	
	public Client (int id, int uID, String fName, String lName, String mName, String lSched, char gend, String dob, boolean isActive, byte[] img, List<Condition> conds, String note ){
		
		this.clientID = id;
		this.userID = uID;
		this.firstName = fName;
		this.lastName = lName;
		this.middleName = mName;
		this.lastScheduled = lSched;
		this.gender = gend;
		this.dob = dob;
		this.isActive = isActive;
		this.imageData = img;
		if (!(conds == null)){
			conditions = new ArrayList<Condition>(conds);
		}
		else{
			conditions = new ArrayList<Condition>();
		}
		this.notes = note;
		
	}
	
	public Client(){
		
	}
	
	//Get and Set Methods
	public int getClientID() {
		return clientID;
	}
	public void setClientID(int clientID) {
		this.clientID = clientID;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public char getGender() {
		return gender;
	}
	public void setGender(char gender) {
		this.gender = gender;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public List<Condition> getConditions() {
		return conditions;
	}
	public void addCondition(Condition cond) {
		this.conditions.add(cond);
	}
	public void setConditions(ArrayList<Condition> list){
		this.conditions = list;
	}
	public String getLastScheduled() {
		return lastScheduled;
	}
	public void setLastScheduled(String lastScheduled) {
		this.lastScheduled = lastScheduled;
	}
	public byte[] getImageData() {
		return imageData;
	}
	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	

}
