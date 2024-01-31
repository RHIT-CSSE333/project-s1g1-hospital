package main;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Base64;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JOptionPane;

public class UserLogin {

	private static final Random RANDOM = new SecureRandom();
	private static final Base64.Encoder enc = Base64.getEncoder();
	private static final Base64.Decoder dec = Base64.getDecoder();
	ConnectionService con = null;
	
	public UserLogin(ConnectionService connect) {
		this.con = connect;
	}
	
	public boolean login(String username, String password) {
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        String query = "SELECT passwordSalt, passwordHash FROM User WHERE username = ?";
	        pstmt = con.getConnection().prepareStatement(query);
	        pstmt.setString(1, username);

	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            String storedSalt = rs.getString("PasswordSalt");
	            String storedHash = rs.getString("passwordHash");

	            String hashedPassword = hashPassword(storedSalt.getBytes(), password);

	            if (storedHash.equals(hashedPassword)) {
	                return true; 
	            }
	        }
	        JOptionPane.showMessageDialog(null, "Login Failed.");
	        return false; // Login failed
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(null, "Login Failed.");
	        e.printStackTrace();
	        return false;
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

	public boolean register(String firstName, String lastName, String dob, String username, String password) {
		String existsProc = "{? = call isUserExists(?, ?, ?)}";
		CallableStatement estmt = null;
		try {
			estmt = con.getConnection().prepareCall(existsProc);
			estmt.setString(2, firstName);
			estmt.setString(3, lastName);
			
            Date date = Date.valueOf(dob);
			estmt.setDate(4, date);

			estmt.registerOutParameter(1, Types.INTEGER);
			estmt.executeUpdate();
			
            int returnCode = estmt.getInt(1);
            if (returnCode != 0) {
				JOptionPane.showMessageDialog(null, "Registration failed.");
            	return false;
            }
		}
		catch(SQLException e) {
			JOptionPane.showMessageDialog(null, "Registration failed.");
			return false;
		}
		
		String storedProc = "{? = call Register(?, ?, ?)}";
		CallableStatement cstmt = null;
		try {
			byte[] salt = getNewSalt();
			cstmt = con.getConnection().prepareCall(storedProc);
			cstmt.setString(2, username);
			cstmt.setString(3, salt.toString());
			cstmt.setString(4, hashPassword(salt, password));
			cstmt.registerOutParameter(1, Types.INTEGER);

			cstmt.execute();
			
			int returnCode = cstmt.getInt(1);
			if(returnCode == 1 || returnCode == 2 || returnCode == 3 || returnCode == 4) {
				JOptionPane.showMessageDialog(null, "Registration failed.");
				return false;
			}
			else {
				JOptionPane.showMessageDialog(null, "Success.");
				return true;
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (cstmt != null) cstmt.close();
			}
			catch (SQLException e) {
				e.printStackTrace(); 
			} 
		}
		return false;
	}
	
	public byte[] getNewSalt() {
		byte[] salt = new byte[16];
		RANDOM.nextBytes(salt);
		return salt;
	}
	
	public String getStringFromBytes(byte[] data) {
		return enc.encodeToString(data);
	}

	public String hashPassword(byte[] salt, String password) {

		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		SecretKeyFactory f;
		byte[] hash = null;
		try {
			f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			hash = f.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException e) {
			JOptionPane.showMessageDialog(null, "An error occurred");
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			JOptionPane.showMessageDialog(null, "An error occurred");
			e.printStackTrace();
		}
		return getStringFromBytes(hash);
	}
	
}