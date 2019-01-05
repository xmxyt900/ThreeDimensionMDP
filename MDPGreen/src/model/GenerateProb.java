package model;

import java.util.Random;

public class GenerateProb {
	
	
	public static void main(String[] args) {
	
//		generateWithProb();
		generateSparceProb();
	}
	
	
	public static void generateSparceProb() {
		int timeIntervals = 24;
		int totalWorkloadLevel = 3;
		int i = 0;
		double [][] workloadLevelProb = new double[timeIntervals][totalWorkloadLevel];
		for(int t = 0; t < timeIntervals; t++) {
			for(i =0; i < totalWorkloadLevel-1; i++) {
				workloadLevelProb[t][i] = 0;
				System.out.println(workloadLevelProb[t][i]);

				}
			    workloadLevelProb[t][i] = 1;
				System.out.println(workloadLevelProb[t][i]);

			}
		
	}
	
	public static void generateWithProb() {
		int timeIntervals = 24;
		int totalWorkloadLevel = 5;
		double [][] workloadLevelProb = new double[timeIntervals][totalWorkloadLevel];
		
		int seed = 1;
		Random random = new Random(seed);
		for(int t = 0; t < timeIntervals; t++) {
			for(int i =0; i < totalWorkloadLevel; i++) {
				workloadLevelProb[t][i] = (double) random.nextInt(10) / 100;
				if(workloadLevelProb[t][i] < 0.07) {
					workloadLevelProb[t][i] = 0;
				}
			}
		}
		
		for(int t = 0; t < timeIntervals; t++) {
			double tempWorkload = 0; 
			double ratio = 1.0;
			for(int i =0; i < totalWorkloadLevel; i++) {
				tempWorkload += workloadLevelProb[t][i];
			}
			ratio = ((int) (1.0 / tempWorkload * 100)) / 100.0 ; 
			for(int i =0; i < totalWorkloadLevel; i++) {
				workloadLevelProb[t][i] = workloadLevelProb[t][i] * ratio;
				System.out.println(workloadLevelProb[t][i]);
			}
		}		
	}
	

}
