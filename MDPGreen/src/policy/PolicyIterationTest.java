package policy;

import java.util.Random;

import model.*;

public class PolicyIterationTest {

	final static int totalWorkloadLevel = 4;
	final static int totalGreenEnergyLevel = 3;
	final static int totalBatteryLevel = 3;
	
	final static int timeIntervals = 24;
	
	static double prob[][][][] = new double [timeIntervals][totalWorkloadLevel][totalGreenEnergyLevel][totalBatteryLevel];
	public static void main(String[] args) {

	prob = generateProb();	
		
	MarkovDecisionProcess mdp = new MarkovDecisionProcess(totalWorkloadLevel, totalGreenEnergyLevel, totalBatteryLevel,
														 prob, timeIntervals);
	mdp.ListAllStates();
	
	}

	// PolicyIteration pi = new PolicyIteration(mdp);

	public static double[][][][] generateProb() {
		Random random = new Random();
	for(int t=0; t < timeIntervals; t++) {
		for(int i = 0; i < totalWorkloadLevel; i++) {
			for(int j = 0; j < totalGreenEnergyLevel; j++) {
				for(int k = 0; k < totalBatteryLevel; k++) {
					prob[t][i][j][k] = (double) random.nextInt(100)/100;
				}
			}
		}
	}
	return prob;
	}
					
}