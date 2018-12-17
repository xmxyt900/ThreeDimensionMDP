package policy;

import java.util.Random;

import model.*;

/**
 * This class is used to test MDP, settings realted to model can be defined here
 * @author minxianx
 *
 */
public class PolicyIterationTest {

	final static int totalWorkloadLevel = 5;
	final static int totalGreenEnergyLevel = 3;
	final static int totalBatteryLevel = 3;

	// 24 hours
	final static int timeIntervals = 24;
	
	 static PolicyEvaluator pe;

	 static double prob[][][][] = new double[timeIntervals][totalWorkloadLevel][totalGreenEnergyLevel][totalBatteryLevel];
	
	 static double workloadLevelProb[][] = new double[timeIntervals][totalWorkloadLevel];
	
	static double greenEnergyLevelProb[][] = new double[timeIntervals][totalGreenEnergyLevel];

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
	public static  double[][][][] generateProb() {
		//update workloadLevelProb[][]
		generateWorkloadProb();
		//update greenEnergyLevelProb[][]
		generateGreenEnergyLevelProb();
		
		for (int t = 0; t < timeIntervals; t++) {
			for (int i = 0; i < totalWorkloadLevel; i++) {
				for (int j = 0; j < totalGreenEnergyLevel; j++) {
					for (int k = 0; k < totalBatteryLevel; k++) {
						//The probability is not realted to battery level, which is decided by actions.
						prob[t][i][j][k] = workloadLevelProb[t][i] * greenEnergyLevelProb[t][j];
					}
				}
			}
		}
		return prob;
	}
	
	
	/**
	 * Generate the probabilities for different levels, can also be the data from workload traces.
	 * @return
	 */
	public static  double[][] generateWorkloadProb(){
		int seed = 1;
		Random random = new Random(seed);
		for(int t = 0; t < timeIntervals; t++) {
			for(int i =0; i < totalWorkloadLevel; i++) {
				workloadLevelProb[t][i] = (double) random.nextInt(10) / 100;
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
			}
		}				
		return workloadLevelProb;
	}

	
	public static double[][] generateGreenEnergyLevelProb(){
		int seed = 1;
		Random random = new Random(seed);
		for(int t = 0; t < timeIntervals; t++) {
			for(int i =0; i < totalGreenEnergyLevel; i++) {
				greenEnergyLevelProb[t][i] = (double) random.nextInt(10) / 100;
			}
		}
		
		for(int t = 0; t < timeIntervals; t++) {
			double tempGreenEnergy = 0; 
			double ratio = 1.0;
			for(int i =0; i < totalGreenEnergyLevel; i++) {
				tempGreenEnergy += greenEnergyLevelProb[t][i];
			}
			ratio = ((int) (1.0 / tempGreenEnergy * 100)) / 100.0 ; 
			for(int i =0; i < totalGreenEnergyLevel; i++) {
				greenEnergyLevelProb[t][i] = greenEnergyLevelProb[t][i] * ratio;
			}
		}				
		return greenEnergyLevelProb;
	}
}