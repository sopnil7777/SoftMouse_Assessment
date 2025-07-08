package Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class readconfig {
Properties pro;

public readconfig() {
	File src = new File ("Configuration/config.properties");
	try {
		FileInputStream fis = new FileInputStream(src);
		pro = new Properties();
		pro.load(fis);}
	catch (FileNotFoundException e) {
		System.out.println("Exception is "+ e.getMessage());
		e.printStackTrace();
	}
	catch (IOException e) {
		System.out.println("Exception for file not found");
		e.printStackTrace();

	}
}
public String getApplicationURL() {
	String url = pro.getProperty("baseurl");
			return url;
}

public String getEmail() {
	String email = pro.getProperty("Email");
			return email;
}


public String getpassword() {
	String pswd = pro.getProperty("password");
			return pswd;
}
 public String getChromePath() {

	 String cp = pro.getProperty("ChromePath");
	 return cp;
 }




}
