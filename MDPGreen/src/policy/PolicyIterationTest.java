package policy;

import java.util.Random;

import model.*;

/**
 * This class is used to test MDP, settings realted to model can be defined here
 * @author minxianx
 *
 */
public class PolicyIterationTest {

	final static int totalWorkloadLevel = 10;
	final static int totalGreenEnergyLevel = 10;
	final static int totalBatteryLevel = 10;

	// 24 hours
	final static int timeIntervals = 24;
	
	 static PolicyEvaluator pe;

	static double prob[][][][] = new double[timeIntervals][totalWorkloadLevel][totalGreenEnergyLevel][totalBatteryLevel];

	public static void main(String[] args) {

	prob = generateProb();	
		
	MarkovDecisionProcess mdp = new MarkovDecisionProcess(totalWorkloadLevel, totalGreenEnergyLevel, totalBatteryLevel,
														 prob, timeIntervals);
	mdp.ListAllStates();
	
	mdp.compileActions(totalWorkloadLevel, totalGreenEnergyLevel, totalBatteryLevel);
	
	System.out.println(mdp.getNumPossibleActions());
   
    pe = new PolicyEvaluator(mdp, -1);
    pe.solve(-1);
	
    for(int t = 0; t < timeIntervals -1 ; t++) {
    	pe = new PolicyEvaluator(mdp, t);
    	pe.solve(t);
    	
    }
    pe = new PolicyEvaluator(mdp, timeIntervals -1);
    pe.solveFinalSate(timeIntervals -1);
		
	}
	
	
	// PolicyIteration pi = new PolicyIteration(mdp);

	/**
	 * Generate the probability of states transitions
	 * Can also defined by external data, e.g. probability of renewable energy.
	 * @return
	 */
	public static double[][][][] generateProb() {
		int seed = 2;
		Random random = new Random(seed);
		for (int t = 0; t < timeIntervals; t++) {
			for (int i = 0; i < totalWorkloadLevel; i++) {
				for (int j = 0; j < totalGreenEnergyLevel; j++) {
					for (int k = 0; k < totalBatteryLevel; k++) {
						prob[t][i][j][k] = (double) random.nextInt(10) / 100;
					}
				}
			}
		}
		return prob;
	}

}