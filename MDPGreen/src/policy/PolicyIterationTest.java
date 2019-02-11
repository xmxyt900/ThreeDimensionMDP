package policy;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
	
	 static double workloadLevelProb[][] = new double[timeIntervals][totalWorkloadLevel];
	
	static double greenEnergyLevelProb[][] = new double[timeIntervals][totalGreenEnergyLevel];
	
	static double rewardMatrix[][][] = new double[timeIntervals][totalWorkloadLevel*totalGreenEnergyLevel][totalWorkloadLevel*totalGreenEnergyLevel];
	static int batteryLevelMatrix[][][] = new int[timeIntervals][totalWorkloadLevel*totalGreenEnergyLevel][totalWorkloadLevel*totalGreenEnergyLevel];
    static String actionMatrix[][][] = new String[timeIntervals][totalWorkloadLevel*totalGreenEnergyLevel][totalWorkloadLevel*totalGreenEnergyLevel];
	
	static MarkovDecisionProcess mdp;

	public static void main(String[] args) throws IOException {

//	prob = generateProb();	
		
	
		
	
	prob = generateProbByFile();
		
	mdp = new MarkovDecisionProcess(totalWorkloadLevel, totalGreenEnergyLevel, totalBatteryLevel,
														 prob, timeIntervals);
	mdp.ListAllStates();
	
	mdp.compileActions(totalWorkloadLevel, totalBatteryLevel);
	
	System.out.println(mdp.getNumPossibleActions());
   
    pe = new PolicyEvaluator(mdp, -1);
    pe.solve(-1);
	
    for(int t = 0; t < timeIntervals -1 ; t++) {
    	pe = new PolicyEvaluator(mdp, t);
    	pe.solve(t);
    	
    }
    pe = new PolicyEvaluator(mdp, timeIntervals -1);
    pe.solveFinalSate(timeIntervals -1);
    
	rewardMatrix = mdp.getRewardMatrix();
	batteryLevelMatrix = mdp.getBatteryMatrix();
	actionMatrix  = mdp.getActionMatrix();
    
//    outputRewardMatrix();
    
    outputActionMatrix();
    
//    findPathForState(0, mdp.grid[0][1][0][0]);
    
    MapFile mapFile = new MapFile();
    
    HashMap<State, Action> state_action_map = new HashMap<State, Action>();
    Action tempAction;
    for(int time = 0; time <= timeIntervals-1; time++) {
    	for(int i = 0 ; i < totalWorkloadLevel; i++) {
    		for(int j = 0; j < totalGreenEnergyLevel; j++) {
    			for(int k = 0; k < totalBatteryLevel; k++) {
//    				tempAction = findActionForState(time, mdp.grid[time][i][j][k]);
    				tempAction = mdp.grid[time][i][j][k].getBestAction();
    				state_action_map.put(mdp.grid[time][i][j][k], tempAction);
    				if(tempAction == null) {
    				mapFile.writeMap("Time Interval#" + time + mdp.grid[time][i][j][k].toFormattedString(), "Impossible State");
    				}else {
        				mapFile.writeMap("Time Interval#" + time + mdp.grid[time][i][j][k].toFormattedString(), tempAction.toFormattedString());

    				}
    			}
    		}
    	}
    }
    
    mapFile.closeFile();
//    
//    mapFile.readMap("Time Interval#7" + mdp.grid[7][2][0][0].toFormattedString());
//    findActionForState(0, mdp.grid[0][1][0][0]);
    
     
    
		
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
	 * Generate the probability of states transitions
	 * The data are loaded from files.
	 * @return
	 */
	public static  double[][][][] generateProbByFile() {
		//update workloadLevelProb[][]
		generateWorkloadProbByFile();
		//update greenEnergyLevelProb[][]
		generateGreenEnergyLevelProbByFile();
		
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
	 * Read the workload level probability from files
	 * @return
	 */
	public static double[][] generateWorkloadProbByFile(){
		File file = new File("src/normalworkloadProb.csv");
		ReadProbFromFile rpf = new ReadProbFromFile(file, timeIntervals, totalWorkloadLevel);
		workloadLevelProb = rpf.getProbVector();
		return workloadLevelProb;
	}
	
	/**
	 * Read the green energy level probability from files
	 * @return
	 */
	public static double[][] generateGreenEnergyLevelProbByFile(){
		File file = new File("src/normalgreenEnergyLevelProb.csv");
		ReadProbFromFile rpf = new ReadProbFromFile(file, timeIntervals, totalGreenEnergyLevel);
		greenEnergyLevelProb = rpf.getProbVector();
		return greenEnergyLevelProb;
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

	/**
	 * Generate green energy level probability by random
	 * @return
	 */
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
				System.out.println(greenEnergyLevelProb[t][i]);

			}
		}				
		return greenEnergyLevelProb;
	}
	
	/**
	 * Find the best action for a state to reach another state.
	 * @param time
	 * @param state
	 * @return
	 */
	public static Action findActionForState(int time, State state) {
		if(state.getProbability() == 0) {
			System.out.println("This state is unreachable: + " + state.toString());
			return null;
		}else {
			int workloadLevel = state.getWorkload();
			int greenEnergyLevel = state.getGreenEnergy();
			int batteryLevel = state.getBattery();
			
			double reward = state.getUtility();
		
			int usedBattery = 0;
			int changedworkloadLevel = 0;
			int nexBatteryLevel = 0;
			
			Action action; 
			
			int matrixDimensionSize = totalWorkloadLevel * totalGreenEnergyLevel;
			int seqNo; 
		
			seqNo = workloadLevel * totalGreenEnergyLevel + greenEnergyLevel;
			
			int nextSeqNo = 0;
			
			int i = time + 1;
				
				for(int index = 0; index < matrixDimensionSize; index ++) {
						double tempReward = Math.pow(-2, 31);
						
						if(rewardMatrix[i][seqNo][index] != 0 && rewardMatrix[i][seqNo][index] > tempReward) {
							tempReward = rewardMatrix[i][seqNo][index];
							nextSeqNo = index;
						}
									
				}
				
				usedBattery = batteryLevelMatrix[i][seqNo][nextSeqNo];
				changedworkloadLevel = nextSeqNo / totalGreenEnergyLevel - seqNo / totalGreenEnergyLevel;
				
				action = new Action(changedworkloadLevel, usedBattery);
				System.out.println(action.toString());
				return action;			
		}
		
	}
	
	/**
	 * Find the best path (actions) to reach a specific state
	 * @param time
	 * @param state
	 */
	public static void findPathForState(int time, State state) {
		if(state.getProbability() == 0) {
			System.out.println("This state is unreachable: + " + state.toString());
		}else {
		int workloadLevel = state.getWorkload();
		int greenEnergyLevel = state.getGreenEnergy();
		int batteryLevel = state.getBattery();
		
		String path = state.getPath();
		double reward = state.getUtility();
		
		int usedBattery = 0;
		int changedworkloadLevel = 0;
		int nexBatteryLevel = 0;
		
		
		Action action; 
		
		int matrixDimensionSize = totalWorkloadLevel * totalGreenEnergyLevel;
		int seqNo; 
	
		seqNo = workloadLevel * totalGreenEnergyLevel + greenEnergyLevel;
		
		int nextSeqNo = 0;
		
		for(int i = time + 1; i < timeIntervals; i++) {
			
			for(int index = 0; index < matrixDimensionSize; index ++) {
					double tempReward = Math.pow(-2, 31);
					
					if(rewardMatrix[i][seqNo][index] != 0 && rewardMatrix[i][seqNo][index] > tempReward) {
						tempReward = rewardMatrix[i][seqNo][index];
						nextSeqNo = index;
					}
								
			}
//			System.out.println("Next Seq No: " + nextSeqNo);
			
			usedBattery = batteryLevelMatrix[i][seqNo][nextSeqNo];
			changedworkloadLevel = nextSeqNo / totalGreenEnergyLevel - seqNo / totalGreenEnergyLevel;
			
			action = new Action(changedworkloadLevel, usedBattery);
			
//			nexBatteryLevel = mdp.getNextBatteryLevel(mdp.grid[i][seqNo / 3][seqNo % 3][batteryLevel], action);
			nexBatteryLevel =usedBattery;
			
			path = path + "-->" + "Time interval$" + i + " "
			+ mdp.grid[i][nextSeqNo / totalGreenEnergyLevel][nextSeqNo % totalGreenEnergyLevel][nexBatteryLevel]
							.toString()
			+ "By action: " + action.toString();
			
			reward += rewardMatrix[i][seqNo][nextSeqNo];
			
			batteryLevel = nexBatteryLevel; 
			seqNo = nextSeqNo;
		}
		System.out.println("Best Path: " + path);

		System.out.println("Final Utility: " + reward);
		}
	}
	
	
    public static void outputRewardMatrix() {    	
		for(int i = 0; i < timeIntervals; i++) {
			for(int j = 0; j < totalWorkloadLevel*totalGreenEnergyLevel; j++) {
				for(int k = 0; k < totalWorkloadLevel*totalGreenEnergyLevel; k++) {
					if(rewardMatrix[i][j][k] != 0.0) {
					System.out.println("matrix[" + i + ", " + j + ", " + k +"]: " + rewardMatrix[i][j][k] + ", " + batteryLevelMatrix[i][j][k]);
					}
				}
			}
		}
	
    }
    
    
    public static void outputActionMatrix() {    	
//		for(int i = 0; i < timeIntervals; i++) {
//			for(int j = 0; j < totalWorkloadLevel*totalGreenEnergyLevel; j++) {
//				for(int k = 0; k < totalWorkloadLevel*totalGreenEnergyLevel; k++) {
////					if(actionMatrix[i][j][k] != null) {
//					System.out.println("matrix[" + i + ", " + j + ", " + k +"]: " + actionMatrix[i][j][k]);
////					}
//				}
//			}
//		}
    	for (int t = 0; t < timeIntervals; t++) {
    		System.out.println("========Time Interval " + timeIntervals + "============");
			for (int i = 0; i < totalWorkloadLevel; i++) {
				for (int j = 0; j < totalGreenEnergyLevel; j++) {
					for (int k = 0; k < totalBatteryLevel; k++) {
						if(mdp.grid[t][i][j][k].getBestAction() != null) {
							System.out.print(mdp.grid[t][i][j][k].toFormattedString() +  mdp.grid[t][i][j][k].getBestAction());
						}
					}
				}
			}
		}
	
    }
	
}