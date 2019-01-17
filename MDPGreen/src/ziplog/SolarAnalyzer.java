package ziplog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class SolarAnalyzer {
	
	final static int MAX_SOLAR_VALUE = 1108;
	final static int LEVELS_RANGE = 111; //1108 / 10, assume we have 10 levels
	final static int datasetSize = 28; //28 data in the same hour from 28 years
	
	public static void main(String[] args) {
		getDistributionOfSolarLevels(0);
		
	}
	
	/**
	 * Get the probability distribution of different levels
	 * @param time
	 */
	static void getDistributionOfSolarLevels(int time){ //Time should be between 0-8759
		BufferedReader reader;
		HashMap<String, Integer> hashmap = new HashMap<String, Integer>();
		int usefuldata = 0;
		
		try {
			reader = new BufferedReader(new FileReader(
					"src/solardata.txt"));
			
			//Skip first two lines
			reader.readLine();
			reader.readLine();
			
			String line = reader.readLine();
			
			
			
			int solarValue; 
			int solarlevel;
			while (line != null) {
				String[] strings = line.split(" ");
				
				solarValue = Integer.parseInt(strings[time]);
				if(solarValue != -999) {
				usefuldata++;
				solarlevel = solarValue / LEVELS_RANGE; 
				System.out.println(solarlevel);
				if(hashmap.get("Level#"+solarlevel) == null || hashmap.get("Level#"+solarlevel) == 0) {
					hashmap.put("Level#"+solarlevel, 1);
				}else {
					hashmap.put("Level#"+solarlevel, hashmap.get("Level#"+solarlevel) + 1);
				}
			}
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(hashmap.toString());
		
		for(int solarLevel = 0; solarLevel < 10; solarLevel++) {
			if(hashmap.get("Level#"+solarLevel) != null) {
				System.out.println("Solar Level " + solarLevel + " Probabability: " + (double) hashmap.get("Level#"+solarLevel) / usefuldata );
			}else {
				System.out.println("Solar Level " + solarLevel + " Probabability: 0.0");

			}
		}
	}
	
	  /**
	   * Get the maximum solar value from all data
	   * @return
	   */
      static int getMaximumSolarValue() {
    	BufferedReader reader;
    	int maxSolarValue = -999;
		try {
			reader = new BufferedReader(new FileReader(
					"src/solardata.txt"));
			
			//Skip first two lines
			reader.readLine();
			reader.readLine();
			
			String line = reader.readLine();
			
			
			while (line != null) {
				String[] strings = line.split(" ");
				
				for(int i = 0; i < strings.length; i++) {
					if(Integer.parseInt(strings[i]) >  maxSolarValue) {
						maxSolarValue = Integer.parseInt(strings[i]);
					}
				}
				
				System.out.println(strings[0]);
				
				
				// read next line
				line = reader.readLine();
			}
			System.out.println("max Solar Value:" );
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return maxSolarValue;
    }

}
