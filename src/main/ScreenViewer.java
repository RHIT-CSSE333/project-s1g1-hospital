package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;


public class ScreenViewer extends JFrame {

	private JPanel panel;
	private JPanel textPanel;
	private JPanel resultPanel;
//	private JPanel bottomPanel;
	private JButton loginAsProvider;
	private JButton loginAsAdmin;
	private JButton cancelButton;

	private JButton loginAsPatient;
	private JButton confirmLogInButtonPro;
	private JButton confirmLogInButtonPat;

	private JButton confirmLogInButtonAdmin;
	private JButton registerAsProviderButton;
	private JButton registerAsPatientButton;

	private JButton confirmRegisterAsProvider;

	private JButton confirmAddPatientButton;
	private JButton addPatientButton;
	private JButton addProviderButton;
	private JButton confirmAddProviderButton;
	private JButton confirmRegisterAsPatient;

	private JTextField field1;
	private JTextField field2;
	private JTextField field3;
	private JTextField field4;
	private JTextField field5;
	private JTextField field6;
	
	private JPasswordField pass1;

	private String field1text = "fail";
	private String field2text = "fail";

	private String field3text = "fail";
	private String field4text = "fail";
	private String field5text = "fail";
	private String field6text = "fail";

	private JFrame frame;

	private JLabel success;

	private JTable resultTable;

	static final int frameWidth = 1600;
	static final int frameHeight = 800;

	static final int frameLocX = 50;
	static final int frameLocY = 50;
	protected ConnectionService connectionService;

	protected User user;

	enum UserType {
		PATIENT, PROVIDER, ADMIN;
	}

	public ScreenViewer(JFrame frame) {
//		this.panel = new JPanel();
		this.textPanel = new JPanel();
		this.resultPanel = new JPanel();
//		this.bottomPanel = new JPanel();
		this.frame = frame;

		this.frame.setTitle("Hospital");
//		this.frame.setLocation(frameLocX, frameLocY);
//		this.frame.setLayout(new BorderLayout());

		initializeHospitalLogin();
//        Color c = new Color(47, 47, 47);
//        frame.getContentPane().setBackground( c );
//        textPanel.setBackground(c);
        
//		this.frame.pack();
		this.frame.setVisible(true);

//        after initalizing login buttons, pack the frame
//        this.frame.setSize(frameWidth, frameHeight);

	}

	public void initializeHospitalLogin() {
		System.out.println("hospital init");
		loginAsProvider = new JButton("Provider Login");
		loginAsAdmin = new JButton("Admin Login");
		cancelButton = new JButton("Cancel");

		loginAsPatient = new JButton("Patient Login");
		confirmLogInButtonAdmin = new JButton("Log In");
		confirmLogInButtonPro = new JButton("Log In");
		confirmLogInButtonPat = new JButton("Log In");

		registerAsProviderButton = new JButton("Provider Registration");
		registerAsPatientButton = new JButton("Patient Registration");

		confirmRegisterAsProvider = new JButton("Register");
		confirmRegisterAsPatient = new JButton("Register");
		
//		Color colour = new Color(204, 129, 186);		
//		
//		loginAsProvider.setBackground(colour);
//		loginAsAdmin.setBackground(colour);
//		cancelButton.setBackground(colour);
//		confirmLogInButtonAdmin.setBackground(colour);
//		confirmLogInButton.setBackground(colour);
//		registerAsProviderButton.setBackground(colour);
//		registerAsPatientButton.setBackground(colour);
//		confirmRegisterAsProvider.setBackground(colour);
//		confirmRegisterAsPatient.setBackground(colour);
//		loginAsPatient.setBackground(colour);


//		panel = new JPanel();
//        panel.setLayout(new BorderLayout());

		field1 = new JTextField();
		field2 = new JTextField();
		field3 = new JTextField();
		field4 = new JTextField();

		field5 = new JTextField();
		field6 = new JTextField();
		
		pass1 = new JPasswordField();


//		TODO: add login as patient for later functionality
//		JButton loginAsPatient = new JButton();

		registerAsProviderButton.addActionListener(e -> {
			registerActionListener(UserType.PROVIDER);
		});
		
		registerAsPatientButton.addActionListener(e -> {
			registerActionListener(UserType.PATIENT);
		});

		loginAsProvider.addActionListener(e -> {
			loginActionListener(UserType.PROVIDER);

		});

		loginAsAdmin.addActionListener(e -> {
			loginActionListener(UserType.ADMIN);
		});
		
		// NEW from CHRIS
		loginAsPatient.addActionListener(e -> {
			loginActionListener(UserType.PATIENT);
		});

		cancelButton.addActionListener(e -> {
			System.out.println("Cancelled");

			field1.setVisible(false);
			field2.setVisible(false);
			field3.setVisible(false);
			field4.setVisible(false);
			field5.setVisible(false);
			field6.setVisible(false);
			pass1.setVisible(false);


			updateFrame();
			frame.setTitle("Hospital");
//            textPanel.removeAll();
			if (this.success != null) {
				success.setVisible(false);
			}
			resultPanel.removeAll();
			if (resultPanel != null) {
				frame.remove(resultPanel);
			}
			loginAsProvider.setVisible(true);
			loginAsAdmin.setVisible(true);
			loginAsPatient.setVisible(true);
			confirmLogInButtonAdmin.setVisible(false);
			confirmLogInButtonPro.setVisible(false);
			confirmLogInButtonPat.setVisible(false);
			confirmRegisterAsPatient.setVisible(false);

			confirmRegisterAsProvider.setVisible(false);
			registerAsProviderButton.setVisible(true);
			registerAsPatientButton.setVisible(true);


			cancelButton.setVisible(false);
//            addPatientButton.setVisible(false);
//            addProviderButton.setVisible(false);
//      

			frame.repaint();
		});

		confirmLogInButtonAdmin.addActionListener(e -> {
			field1text = field1.getText();
//			System.out.println(field1text);
			
			char[] pass = pass1.getPassword();
			field2text = String.valueOf(pass);

//			field2text = field2.getText();
			System.out.println(field2text);
			

			connectionService = new ConnectionService(field1text, field2text);
			if (connectionService.connect()) {

				field1.setVisible(false);
				field2.setVisible(false);
				field3.setVisible(false);

				pass1.setVisible(false);
				loginAsProvider.setVisible(false);
				loginAsAdmin.setVisible(false);
				confirmLogInButtonAdmin.setVisible(false);
				confirmRegisterAsProvider.setVisible(false);
				loginAsPatient.setVisible(false);
				confirmRegisterAsProvider.setVisible(false);
				cancelButton.setVisible(false);
				frame.repaint();
				frame.dispose();
				this.user = new Admin(connectionService, frame);
			}

			return;

		});

		// TODO: Refactor?		
		confirmLogInButtonPro.addActionListener(e -> {
			field1text = field1.getText();
//			System.out.println(field1text);
			
			char[] pass = pass1.getPassword();
			field2text = pass.toString();
			
//			field2text = field2.getText();
//			System.out.println(field2text);
			
			field3text = field3.getText();

			field1.setVisible(false);
			field2.setVisible(false);
			field3.setVisible(false);
			pass1.setVisible(false);

			loginAsProvider.setVisible(false);
			loginAsAdmin.setVisible(false);
			loginAsPatient.setVisible(false);
			confirmLogInButtonPro.setVisible(false);
			confirmLogInButtonPat.setVisible(false);

			cancelButton.setVisible(false);
			frame.repaint();

			Encryption en = new Encryption();
			connectionService = new ConnectionService(en.getEncryptionUsername(), en.getEncryptionPassword());
			connectionService.connect();
			
			int proID = 0;
			int hosID = 0;

			UserLogin userLog = new UserLogin(connectionService);
			try {
				proID = userLog.loginPro(field1text, pass);
			} catch (Exception e1) {
		        JOptionPane.showMessageDialog(null, "Login Failed.");
				try {
					frame.dispose();
					Main.main(null);
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
			
//			try {
//				hosID = userLog.getHospitalID(field3text);
//				if(hosID == 0) {
//			        JOptionPane.showMessageDialog(null, "Incorrect Hospital.");
//
//				}
//			}
//			catch(Exception e2) {
//		        JOptionPane.showMessageDialog(null, "Incorrect Hospital.");
//				try {
//					frame.dispose();
//					Main.main(null);
//				} catch (IOException ex) {
//					throw new RuntimeException(ex);
//				}
//			}
			
			if(proID != 0) {
				JOptionPane.showMessageDialog(null, "Login success.");
				this.user = new Provider(connectionService, frame, proID, hosID);
			}else {
				cancelButton.setVisible(true);
			}
			return;

		});
		
		// TODO: Refactor?		
		confirmLogInButtonPat.addActionListener(e -> {
			field1text = field1.getText();
//			System.out.println(field1text);

//			field2text = field2.getText();
//			System.out.println(field2text);
			
			char[] pass = pass1.getPassword();
			field2text = pass.toString();

			field3text = field3.getText();

			field1.setVisible(false);
			field2.setVisible(false);
			field3.setVisible(false);
			pass1.setVisible(false);

			loginAsProvider.setVisible(false);
			loginAsAdmin.setVisible(false);
			loginAsPatient.setVisible(false);
			confirmLogInButtonPro.setVisible(false);
			confirmLogInButtonPat.setVisible(false);

			cancelButton.setVisible(false);
			frame.repaint();

			Encryption en = new Encryption();
			connectionService = new ConnectionService(en.getEncryptionUsername(), en.getEncryptionPassword());
			connectionService.connect();
			
			int patID = 0;
			int hosID = 0;

			UserLogin userLog = new UserLogin(connectionService);
			try {
				patID = userLog.loginPat(field1text, pass);
//				patID = userLog.loginPat("suesmith", "Password123");
			} catch (Exception e1) {
		        JOptionPane.showMessageDialog(null, "Login Failed.");
				try {
					frame.dispose();
					Main.main(null);
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
			
			if(patID != 0) {
				JOptionPane.showMessageDialog(null, "Login success.");
				System.out.println("it's not zero" + " " + patID);
				this.user = new Patient(connectionService, frame, patID, hosID);

			}else {
				cancelButton.setVisible(true);
			}


			return;

		});

		// TODO: Refactor?? - PROVIDER LOGIN CONFIRMATION
		confirmRegisterAsProvider.addActionListener(e -> {
			field1text = field1.getText();
//			System.out.println(field1text);

			field2text = field2.getText();
//			System.out.println(field2text);

			field3text = field3.getText();
//			System.out.println(field3text);

			field4text = field4.getText();
//			System.out.println(field4text);
			
			char[] pass = pass1.getPassword();
			field5text = pass.toString();
//			field5text = pass1.getPassword();

//			field5text = field5.getText();
//			System.out.println(field5text);
			
			field6text = field6.getText();
			
			int field6int = 0;
			try {
				field6int = Integer.parseInt(field6text);
			}
			catch(Exception e3) {
				JOptionPane.showMessageDialog(null, "Incorrect ID Format");
				return;
			}
			
			Date date = null;
			try {
	            date = Date.valueOf(field3text);

			}
			catch(Exception e3) {
				JOptionPane.showMessageDialog(null, "Incorrect Date Format");
				return;
			}
			

			field1.setVisible(false);
			field2.setVisible(false);
			field3.setVisible(false);
			field4.setVisible(false);
			field5.setVisible(false);
			field6.setVisible(false);
			pass1.setVisible(false);
			

			loginAsProvider.setVisible(false);
			loginAsAdmin.setVisible(false);
			loginAsPatient.setVisible(false);
			confirmRegisterAsProvider.setVisible(false);
			cancelButton.setVisible(false);
			frame.repaint();

			Encryption en = new Encryption();
			connectionService = new ConnectionService(en.getEncryptionUsername(), en.getEncryptionPassword());
			connectionService.connect();
			UserLogin userLog = new UserLogin(connectionService);
			
			int reg = 99;
			
			if (userLog.con != null) {
            	reg = userLog.register(field1text, field2text, date, field4text,pass, "true",field6int);
//					Boolean reg = userLog.register("Tim", "Walker", "1985-05-11", "timwalker", "Password123", "true");
	            switch(reg){
		        	case 1:
						JOptionPane.showMessageDialog(null, "Username cannot be null or empty");
						break;
		        	case 2:
						JOptionPane.showMessageDialog(null, "Password cannot be null or empty");
						break;
		        	case 3:
						JOptionPane.showMessageDialog(null, "Password cannot be null or empty");
						break;

		        	case 4:
						JOptionPane.showMessageDialog(null, "Username is already registered");
						break;

		        	case 5:
						JOptionPane.showMessageDialog(null, "No such ID exists");
						break;

		        	case 6:
						JOptionPane.showMessageDialog(null, "ID is already registered");
						
		        	case 0:
						JOptionPane.showMessageDialog(null, "Success.");
						break;

		        	case 11:
						JOptionPane.showMessageDialog(null, "First Name cannot be null or empty");
						break;

		        	case 12:
						JOptionPane.showMessageDialog(null, "LastName cannot be null or empty");
						break;

		        	case 13:
						JOptionPane.showMessageDialog(null, "DOB cannot be null or empty");
						break;

		        	case 14:
						JOptionPane.showMessageDialog(null, "No such provider exists");
						break;

		        	case 15:
						JOptionPane.showMessageDialog(null, "No such patient exists");
						break;

		        	case 16:
						JOptionPane.showMessageDialog(null, "No such ID exists");
						break;

		        	case 17:
						JOptionPane.showMessageDialog(null, "No such ID exists");
						break;

					default:
						JOptionPane.showMessageDialog(null, "Unknown error.");
						System.out.println(reg);
						break;
					}
			}

			cancelButton.setVisible(true);
		});
		
		// TODO: Refactor?? - PATIENT LOGIN CONFIRMATION
		confirmRegisterAsPatient.addActionListener(e -> {
			field1text = field1.getText();
//			System.out.println(field1text);

			field2text = field2.getText();
//			System.out.println(field2text);

			field3text = field3.getText();
//			System.out.println(field3text);

			field4text = field4.getText();
//			System.out.println(field4text);
			
			char[] pass = pass1.getPassword();
			field5text = pass.toString();

			field5text = field5.getText();
//			System.out.println(field5text);
			
			field6text = field6.getText();
			System.out.println(field6text);
			int field6int = 0;
			try {
				field6int = Integer.parseInt(field6text);
			}
			catch(Exception e3) {
				JOptionPane.showMessageDialog(null, "Incorrect ID Format");
				return;
			}
			
			Date date = null;
			try {
	            date = Date.valueOf(field3text);

			}
			catch(Exception e3) {
				JOptionPane.showMessageDialog(null, "Incorrect Date Format");
				return;
			}

			field1.setVisible(false);
			field2.setVisible(false);
			field3.setVisible(false);
			field4.setVisible(false);
			field5.setVisible(false);
			field6.setVisible(false);
			pass1.setVisible(false);

			loginAsProvider.setVisible(false);
			loginAsAdmin.setVisible(false);
			loginAsPatient.setVisible(false);
			confirmRegisterAsPatient.setVisible(false);
			
			cancelButton.setVisible(false);
			frame.repaint();
			
			Encryption en = new Encryption();
			connectionService = new ConnectionService(en.getEncryptionUsername(), en.getEncryptionPassword());
			connectionService.connect();
			UserLogin userLog = new UserLogin(connectionService);
			
			int regPat = 100;

			if (userLog.con != null) {

				regPat = userLog.register(field1text, field2text, date, field4text,pass, "false", field6int);
//					Boolean reg = userLog.register("Sue", "Smith", "1993-05-14", "suesmith", "Password123", "false");
				System.out.println(regPat);
	            switch(regPat){
		        	case 1:
						JOptionPane.showMessageDialog(null, "Username cannot be null or empty");
						break;

		        	case 2:
						JOptionPane.showMessageDialog(null, "Password cannot be null or empty");
						break;

		        	case 3:
						JOptionPane.showMessageDialog(null, "Password cannot be null or empty");
						break;

		        	case 4:
						JOptionPane.showMessageDialog(null, "Username is already registered");
						break;

		        	case 5:
						JOptionPane.showMessageDialog(null, "No such ID exists");
						break;

		        	case 6:
						JOptionPane.showMessageDialog(null, "ID is already registered");
						break;

		        	case 0:
						JOptionPane.showMessageDialog(null, "Success.");
						break;

		        	case 11:
						JOptionPane.showMessageDialog(null, "First Name cannot be null or empty");
						break;

		        	case 12:
						JOptionPane.showMessageDialog(null, "LastName cannot be null or empty");
						break;

		        	case 13:
						JOptionPane.showMessageDialog(null, "DOB cannot be null or empty");
						break;

		        	case 14:
						JOptionPane.showMessageDialog(null, "No such provider exists");
						break;

		        	case 15:
						JOptionPane.showMessageDialog(null, "No such patient exists");
						break;

		        	case 16:
						JOptionPane.showMessageDialog(null, "No such ID exists");
						break;

		        	case 17:
						JOptionPane.showMessageDialog(null, "No such ID exists");
						break;

					default:
						JOptionPane.showMessageDialog(null, "Unknown error.");
						System.out.println(regPat);
						break;
					}
			}

			cancelButton.setVisible(true);
		});
		


		textPanel.add(loginAsProvider);
		textPanel.add(loginAsAdmin);

		textPanel.add(loginAsPatient);
		textPanel.add(registerAsProviderButton);
		textPanel.add(registerAsPatientButton);


		// panel.add(loginAsPatient);

//        frame.add(panel, BorderLayout.NORTH);
//        frame.add(bottomPanel, BorderLayout.SOUTH);
		System.out.println("Hello");
		frame.add(textPanel);
		frame.repaint();

		System.out.println("init complete");

	}

	private void updateFrame() {
//		frame.pack();
//	        frame.setLocation(frameLocX, frameLocY);
		frame.setSize(frameWidth, frameHeight);
		frame.setLayout(new BorderLayout());
		frame.repaint();

	}

	private void setButtons(UserType typeOfUser) {
		field1.setVisible(true);
		pass1.setVisible(true);
//		field2.setVisible(true);

		loginAsProvider.setVisible(false);
		loginAsPatient.setVisible(false);
		loginAsAdmin.setVisible(false);
		cancelButton.setVisible(true);
		confirmRegisterAsProvider.setVisible(false);
		registerAsProviderButton.setVisible(false);
		registerAsPatientButton.setVisible(false);

	}

	private void loginActionListener(UserType typeOfUser) {
		field1.setText(typeOfUser + " Username");

		textPanel.add(field1);
		
		pass1.setText("Changepasswordnow");
		textPanel.add(pass1);

//		field2.setText(typeOfUser + " Password");
//		textPanel.add(field2);
		
		// NEW BY CHRIS

		if (typeOfUser == UserType.ADMIN) {
			textPanel.add(confirmLogInButtonAdmin);
			confirmLogInButtonAdmin.setVisible(true);
		}

		else if (typeOfUser == UserType.PATIENT) {
			textPanel.add(confirmLogInButtonPat);
			confirmLogInButtonPat.setVisible(true);

		}

		else {
			textPanel.add(confirmLogInButtonPro);
			confirmLogInButtonPro.setVisible(true);

		}

		textPanel.add(cancelButton);

		updateFrame();
		frame.setTitle(typeOfUser + " Login");
		setButtons(typeOfUser);
		frame.repaint();
	}

	private void registerActionListener(UserType u) {
		field1.setText(u + " First Name");
		textPanel.add(field1);

		field2.setText(u + " Last Name");
		textPanel.add(field2);

		field3.setText(u + " DOB");
		textPanel.add(field3);

		field4.setText(u + " Username");
		textPanel.add(field4);
		pass1.setText("Thispasswordmustbechanged");
		textPanel.add(pass1);

//		field5.setText(u + " Password");
//		textPanel.add(field5);
		
		field6.setText(u + " ID Number");
		textPanel.add(field6);

		if (u == UserType.PROVIDER) {
			textPanel.add(confirmRegisterAsProvider);
		}

        else if(u == UserType.PATIENT) {
       	 textPanel.add(confirmRegisterAsPatient);
        }
//        
//        else {
//       	 textPanel.add(confirmLogInButtonProvider);
//        }

		textPanel.add(cancelButton);

		updateFrame();
		frame.setTitle(u + " Register");
		setRegisterButtons(u);
		frame.repaint();
	}

	private void setRegisterButtons(UserType u) {
		field1.setVisible(true);
		field2.setVisible(true);
		field3.setVisible(true);
		field4.setVisible(true);
//		field5.setVisible(true);
		pass1.setVisible(true);
		field6.setVisible(true);


		loginAsProvider.setVisible(false);
		loginAsPatient.setVisible(false);
		loginAsAdmin.setVisible(false);
		if(u == UserType.PROVIDER) {
			confirmRegisterAsProvider.setVisible(true);
			confirmRegisterAsPatient.setVisible(false);
		}
		else if (u == UserType.PATIENT) {
			confirmRegisterAsProvider.setVisible(false);
			confirmRegisterAsPatient.setVisible(true);

		}
		registerAsProviderButton.setVisible(false);

		registerAsPatientButton.setVisible(false);

		cancelButton.setVisible(true);
	}

}
