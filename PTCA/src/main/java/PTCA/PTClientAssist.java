package main.java.PTCA;
/*
 * Nicholas Bergez
 * nlb49@drexel.edu
 * CS575:Software Design, Class Project
 */


public class PTClientAssist {

	static final String DB_Connection_String = "jdbc:sqlserver://TARKIR\\DEV;databaseName=PTCA;integratedSecurity=false;user=ptca_service;password=password";
	static final String Default_Image = "C:\\Users\\nbergez\\Pictures\\PTCA_Images\\nophoto.jpg";
	static final String PTCA_SA = "ptca_service";
	static final String PTCA_PW = "password";
	
	public static void main(String[] args) {
		PTCA_MainWindow main = new PTCA_MainWindow();
		main.pack();
		main.setVisible(true);
		
	}

}
