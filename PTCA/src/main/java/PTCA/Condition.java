package main.java.PTCA;
/*
 * Nicholas Bergez
 * nlb49@drexel.edu
 * CS575:Software Design, Class Project
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Condition {
	
	//Private members
	private int ctID;
	private int clientID;
	private String type;
	private String enteredDate;
	
	//Constructors
	public Condition(String str, int clID, String date){
		ctID = 0;
		clientID = clID;
		type = str;
		enteredDate = date;
	}
	
	//Get and Set Methods
	public int getCtID() {
		return ctID;
	}
	public void setCtID(int ctID) {
		this.ctID = ctID;
	}
	public int getClientID() {
		return clientID;
	}
	public void setClientID(int clientID) {
		this.clientID = clientID;
	}
	public String getEnteredDate() {
		return enteredDate;
	}
	public void setEnteredDate(String enteredDate) {
		this.enteredDate = enteredDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
