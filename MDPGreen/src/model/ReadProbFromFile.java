package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReadProbFromFile {
	
	double[][] probVector;	
	int row;
	int column;
	
	public ReadProbFromFile(File file, int row, int column) {
		BufferedReader reader;
		this.row = row;
		this.column = column;
		probVector = new double[row][column];
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;;
			int index = 0;

			for(int i = 0; i < row; i++ ) {
				for(int j = 0; j < column; j++) {
					line = reader.readLine();
					System.out.println(index++ + ": " + line);
					probVector[i][j]  =  Double.parseDouble(line);
				}
			}		
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		
	}
	
	public double[][] getProbVector(){		
		return probVector;
	}
	
	/**
	 * Validating whether the total probability at a single time interval is 1.0
	 * @param probVector
	 * @return
	 */
	public boolean validateProbVector(double[][] probVector) {
		boolean isValid = true;
		int i;
		int j;
		for(i = 0; i < row; i++) {
			double totalProb = 0;
			for(j = 0; j < column; j++) {
				totalProb += probVector[i][j];
			}
//			System.out.println("TotalProb at Interval:" + i +" :"+ totalProb);
			//To eliminate some errors
			if(totalProb < 0.99 || totalProb > 1.01) {				
				isValid = false;
				System.out.println("Validating probabity: interval " + i  + " is not equvalent to 1.0");
			}
		}
		if(isValid == true) {
			System.out.println("Good, the probability vector is valid!");
		}
		return isValid;
	}
	
	public static void main(String[] args) {
		File file = new File("src/workloadProb.csv");
		ReadProbFromFile rpf = new ReadProbFromFile(file, 24, 5);
		rpf.validateProbVector(rpf.getProbVector());
		
//		File file = new File("src/greenEnergyLevelProb.csv");
//		ReadProbFromFile rpf = new ReadProbFromFile(file, 24, 3);
//		rpf.validateProbVector(rpf.getProbVector());
	}
	
	

}
