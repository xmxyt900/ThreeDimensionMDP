package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class MapFile {
	
	Properties prop;
	OutputStream output;
	InputStream input;
	
	public MapFile() {
		
		prop = new Properties();
		output = null;
		input = null;
		
	}
	
	public void writeMap(String stringOne, String stringTwo) {
		try {
			output = new FileOutputStream("src/map.properties");			
			prop.setProperty(stringOne, stringTwo);

			// save properties to project root folder
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
	
	public void readMap(String stringOne) {
		try {

			input = new FileInputStream("src/map.properties");

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			System.out.println(prop.getProperty(stringOne));

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		
		MapFile mapFile = new MapFile();
		mapFile.readMap("Time Interval#0" + "State[2, 3, 0]");
	}

}
