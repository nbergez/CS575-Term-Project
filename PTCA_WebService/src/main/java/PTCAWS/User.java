package main.java.PTCAWS;
/*
 * Nicholas Bergez
 * nlb49@drexel.edu
 * CS575:Software Design, Class Project
 */

public class User {
	
	//Private Members
	private int userID;
	private String username;
	private String password;
	
	//Constructors
	public User(){
		this(1, "Bob", "password");
	}
	
	public User(int id, String username, String password){
		this.userID = id;
		this.username = username;
		this.password = password;
	}
	
	
	public User(User user){
		this(user.getUserID(), user.getUsername(), user.getPassword());
	}
	
	//Get and Set Methods

	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
