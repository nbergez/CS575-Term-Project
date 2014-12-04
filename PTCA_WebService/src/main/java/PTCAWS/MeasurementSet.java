package main.java.PTCAWS;
/*
 * Nicholas Bergez
 * nlb49@drexel.edu
 * CS575:Software Design, Class Project
 */
public class MeasurementSet {
	
	//Private Members
	private int measureID;
	private int clientID;
	private String date;
	private int heightIn;
	private int weight;
	private int chest;
	private int waist;
	private int arm;
	private int hips;
	private int thigh;
	private int bodyFat;
	private String Notes;
	
	//Constructors
	
	//Get and Set methods
	public int getMeasureID() {
		return measureID;
	}
	public void setMeasureID(int measureID) {
		this.measureID = measureID;
	}
	public int getClientID() {
		return clientID;
	}
	public void setClientID(int clientID) {
		this.clientID = clientID;
	}
	public int getHeightIn() {
		return heightIn;
	}
	public void setHeightIn(int heightIn) {
		this.heightIn = heightIn;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getChest() {
		return chest;
	}
	public void setChest(int chest) {
		this.chest = chest;
	}
	public int getWaist() {
		return waist;
	}
	public void setWaist(int waist) {
		this.waist = waist;
	}
	public int getArm() {
		return arm;
	}
	public void setArm(int arm) {
		this.arm = arm;
	}
	public int getHips() {
		return hips;
	}
	public void setHips(int hips) {
		this.hips = hips;
	}
	public int getThigh() {
		return thigh;
	}
	public void setThigh(int thigh) {
		this.thigh = thigh;
	}
	public int getBodyFat() {
		return bodyFat;
	}
	public void setBodyFat(int bodyFat) {
		this.bodyFat = bodyFat;
	}
	public String getNotes() {
		return Notes;
	}
	public void setNotes(String notes) {
		Notes = notes;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	

}
