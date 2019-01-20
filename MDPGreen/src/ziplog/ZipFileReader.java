package ziplog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/***
 * This class is used to read zipfiles on solar hourly data
 * File the specific row and column in the txt files in the zips
 * Monash University, Clayton Campus location: (-37.907803, 145.133957)
 * The data in the txt files starts from location (-43.975, 112.025) with distance 0.05
 * row: (-37.907803 - (-43.975)) / 0.05 =  121
 * column:  (145.133957 - 112.025) /0.05 = 662
 * @author minxianx
 *
 */
public class ZipFileReader {
	
	 private final static Long MILLS_IN_DAY = 86400000L;
	 
	 final static int ROW = 121; //121
	 final static int COLUMN = 662;

	    public static void main(String[] args) throws IOException {
	    	String fileName;

	        for(int year = 1990; year < 2018; year++) {
	    	
	        fileName = "C://GHI/GHI-" + year + ".zip";
	        
	        ZipFile zipFile = new ZipFile(fileName);
	        
//	        PrintWriter out = new PrintWriter("src/solardata.txt");
	        
	        BufferedWriter writer = new BufferedWriter( new FileWriter("src/solardatafortest.txt", true));
	        
	        Enumeration<? extends ZipEntry> entries = zipFile.entries();
	        
	        
	        try {
	        
	        while(entries.hasMoreElements()){
	            ZipEntry entry = entries.nextElement();
	            InputStream stream = zipFile.getInputStream(entry);
	            
	            System.out.println(entry.getName());
//	            writer.write(entry.getName() + "\n");
	            
	            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
	            
	            String line;
	            //The first 6 lines are no data
	            for(int i = 0 ; i < ROW + 6; i++) {
//	            while((line = br.readLine()) != null) {
//	            System.out.println(line);
	            	line = br.readLine();
//	            }
	            }
	            line = br.readLine();
	            String[] lines = line.split(" ");
	            
	            System.out.println(lines[COLUMN]);
	            writer.write(lines[COLUMN] + " ");

	            
	            br.close();
	            
	            
	        }
	        writer.write("\n");
	        }catch (IOException e) {
				e.printStackTrace();
			}	
	        zipFile.close();
	        writer.close();

	        
	        
	    }
	    }
}
