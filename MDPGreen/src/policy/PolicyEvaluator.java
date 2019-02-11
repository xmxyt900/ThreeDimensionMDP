package policy;

import java.util.ArrayList;

import model.*;

/**
 * This class is used to iterate different paths (policies) to maximize
 * objective fucntion.
 * 
 * @author minxianx
 *
 */
public class PolicyEvaluator {

	MarkovDecisionProcess mdp;

	ArrayList<State> transitStates;

	int size;
	// Not used yet, but can be used for further extension in utility function.
	// double gamma;

	public PolicyEvaluator(MarkovDecisionProcess mdp, int timeInterval) throws IllegalStateException {
		this.mdp = mdp;

		mdp.compileStates(timeInterval);
		size = mdp.getNumReachableStates();
		System.out.println("NumReachableStates: " + size + " at time interval " + timeInterval);

		if (size == 0) {
			throw new IllegalStateException(
					"MDP is not prepared at time interval: " + timeInterval + " No reachable state");
		}

	}

	/**
	 * Find the best paths to reach states at current interval
	 * 
	 * @param timeInveral
	 */
	public void solve(int timeInterval) {

		System.out.println("################Time Interval " + timeInterval + " Started#####################\n");
		State state = mdp.getStartState();
		Action action;
		// Go through all reachable states
		while (state != null) {

			// System.out.print("Now Trying actions for " + state.toString());

			State sPrime; // Next state after action
			double prob;
			double reward;
			double preUtility;
//			double afterUtility;

			int tempWorkloadLevel;
			int tempGreenEnergyLevel;
//			int tempBatteryLevel;
			String tempPath;

			action = mdp.getStartAction();
			// Reste the action index to 0;
			mdp.resetCurrentAction();
			// Go through all possible actions
			state.setReward(-999);
			
			while (action != null) {
				
				if (mdp.isPossibleTransit(state, action)) {
					
					transitStates = mdp.transit(state, action, timeInterval);
					
					reward = state.getReward(action);
//					System.out.println(state.toFormattedString());
//					System.out.println(action.toString());
//					System.out.println("S1-Reward:" + state.getReward(action));
//					System.out.println("S2-GetReward:" + state.getReward());
//					if(state.getBestAction() != null) {
//					System.out.println("Best Action:" + state.getBestAction().toFormattedString());
//					}
//					System.out.println("Action updated" + state.toFormattedString() + action.toFormattedString());
					if(reward > state.getReward()) {
					System.out.println("Reward:" + state.getReward(action));
					System.out.println("GetReward:" + state.getReward());
					System.out.println("Action updated" + state.toFormattedString() + action.toFormattedString());

						state.setReward(reward);
						state.setBestAction(action);
					}

					
					for (int transitStatesIndex = 0; transitStatesIndex < transitStates.size(); transitStatesIndex++) {
						sPrime = transitStates.get(transitStatesIndex);
						

						prob = sPrime.getProbability();
						preUtility = state.getUtility();
						// Utility function: U(s')= R(s)+ prob(s,a,s')*R(s')
						double afterUtility = preUtility + prob * reward;
						
//						reward = sPrime.getReward();
//						prob = sPrime.getProbability();
//						preUtility = state.getUtility();
//						// Utility function: U(s')= R(s)+ prob(s,a,s')*R(s')
//						double afterUtility = preUtility + prob * reward;

						tempWorkloadLevel = sPrime.getWorkload();
						tempGreenEnergyLevel = sPrime.getGreenEnergy();
//						tempBatteryLevel = sPrime.getBattery();
//						int batteryLevel = 0;
						int nextBatteryLevel = mdp.getNextBatteryLevel(state, action);
//						int batteryLevel = sPrime.getBattery();
						tempPath = state.getPath();
						
						if (sPrime.isVisited() == false && prob != 0.0) {

//							for (batteryLevel = 0; batteryLevel < mdp.totalBatteryLevel; batteryLevel++) {

								mdp.grid[timeInterval + 1][tempWorkloadLevel][tempGreenEnergyLevel][nextBatteryLevel]
										.setVisited(true);

								mdp.grid[timeInterval + 1][tempWorkloadLevel][tempGreenEnergyLevel][nextBatteryLevel]
										.setUtility(afterUtility);

								mdp.grid[timeInterval + 1][tempWorkloadLevel][tempGreenEnergyLevel][nextBatteryLevel]
										.setPath(tempPath + "-->" + "Time interval#" + (timeInterval + 1) + " "
												+ mdp.grid[timeInterval
														+ 1][tempWorkloadLevel][tempGreenEnergyLevel][nextBatteryLevel]
																.toString()
												+ "By action: " + action.toString());
//								 System.out.println(state.toString() + "######Path: " + tempPath + "-->" +
//								 "Time interval#" + (timeInterval+1) +" "+ sPrime.toString() + "By action: " +
//								 action.toString());
								
//								System.out.println("S1-Reward:" + state.getReward(action));
//								System.out.println("S1-GetReward:" + state.getReward());
//								if(state.getReward() == -999) {
//
//								state.setBestAction(action);
//								state.setReward(reward);
//								}
							    updateActionMatrix(timeInterval+1, state, sPrime, action.toString());
								updateRewardMatrix(timeInterval + 1, state, sPrime, prob * reward);
								updateBatteryMatrix(timeInterval + 1, state, sPrime, nextBatteryLevel);
								
//								System.out.print("###### With Action " + action.toString());
//								System.out.println("###### Path updated for " + mdp.grid[timeInterval
//										+ 1][tempWorkloadLevel][tempGreenEnergyLevel][nextBatteryLevel].toString());
							
//							}

						}
						


//						if (prob != 0.0 && sPrime.isVisited() == true && reward >= state.getReward()) {
////							System.out.println("Reward:" + state.getReward(action));
////							System.out.println("GetReward:" + state.getReward());
////							System.out.println("Action updated" + state.toFormattedString() + action.toFormattedString());
//
////							for (batteryLevel = 0; batteryLevel < mdp.totalBatteryLevel; batteryLevel++) {
////
//							mdp.grid[timeInterval + 1][tempWorkloadLevel][tempGreenEnergyLevel][nextBatteryLevel]
//									.setVisited(true);
//
//							mdp.grid[timeInterval + 1][tempWorkloadLevel][tempGreenEnergyLevel][nextBatteryLevel]
//									.setUtility(afterUtility);
//
//							mdp.grid[timeInterval + 1][tempWorkloadLevel][tempGreenEnergyLevel][nextBatteryLevel]
//									.setPath(tempPath + "-->" + "Time interval#" + (timeInterval + 1) + " "
//											+ mdp.grid[timeInterval
//													+ 1][tempWorkloadLevel][tempGreenEnergyLevel][nextBatteryLevel]
//															.toString()
//											+ "By action: " + action.toString());
////								 System.out.println(state.toString() + "######Path: " + tempPath + "-->" +
////								 "Time interval#" + (timeInterval+1) +" "+ sPrime.toString() + "By action: " +
////								 action.toString());
//
//							state.setBestAction(action);
//							state.setReward(reward);
//						    updateActionMatrix(timeInterval + 1, state, sPrime, action.toString());
//							updateRewardMatrix(timeInterval + 1, state, sPrime, prob * reward);
//							updateBatteryMatrix(timeInterval + 1, state, sPrime, nextBatteryLevel);								
////								System.out.print("###### (Visited) With Action " + action.toString());
////								System.out.println("###### Path updated for " + mdp.grid[timeInterval
////										+ 1][tempWorkloadLevel][tempGreenEnergyLevel][batteryLevel].toString());
////							}
//						}

						// Keep the path with highest utility
						if (prob != 0.0 && sPrime.isVisited() == true && afterUtility >= mdp.grid[timeInterval
								+ 1][tempWorkloadLevel][tempGreenEnergyLevel][sPrime.getBattery()].getUtility()) {

//							for (batteryLevel = 0; batteryLevel < mdp.totalBatteryLevel; batteryLevel++) {


								


								mdp.grid[timeInterval + 1][tempWorkloadLevel][tempGreenEnergyLevel][nextBatteryLevel]
										.setUtility(afterUtility);

								mdp.grid[timeInterval + 1][tempWorkloadLevel][tempGreenEnergyLevel][nextBatteryLevel]
										.setPath(tempPath + "-->" + "Time interval#" + (timeInterval + 1) + " "
												+ mdp.grid[timeInterval
														+ 1][tempWorkloadLevel][tempGreenEnergyLevel][nextBatteryLevel]
																.toString()
												+ "By action: " + action.toString());
								
//								 System.out.println(state.toString() + "######Path: " + tempPath + "-->" +
//								 "Time interval#" + (timeInterval+1) +" "+ sPrime.toString() + "By action: " +
//								 action.toString());

//								state.setBestAction(action);
//								state.setReward(reward);
//								System.out.println("Reward updated");
							    updateActionMatrix(timeInterval + 1, state, sPrime, action.toString());
								updateRewardMatrix(timeInterval + 1, state, sPrime, prob * reward);
								updateBatteryMatrix(timeInterval + 1, state, sPrime, nextBatteryLevel);					
//								System.out.print("###### (Visited) With Action " + action.toString());
//								System.out.println("###### Path updated for " + mdp.grid[timeInterval
//										+ 1][tempWorkloadLevel][tempGreenEnergyLevel][nextBatteryLevel].toString());

//							}
						}

					}
				}
				action = mdp.getNextAction();
			}

			state = mdp.getNextReachableState();
		}

	}

	/**
	 * Find the best path until current time interval
	 * 
	 * @param timeInterval
	 */
	public void solveFinalSate(int timeInterval) {
		double bestUtility = Math.pow(-2, 31);
		int finalWorkloadLevel = -1;
		int finalGreenEnergyLevel = -1;
		int finalBatteryLevel = -1;
		// Find the final state as well as its path that can maximize optimization
		// objective
		for (int i = 0; i < mdp.totalWorkloadLevel; i++) {
			for (int j = 0; j < mdp.totalGreenEnergyLevel; j++) {
				for (int k = 0; k < mdp.totalBatteryLevel; k++) {

					if ((mdp.grid[timeInterval][i][j][k].getUtility() != 0)
							&& (mdp.grid[timeInterval][i][j][k].getUtility() > bestUtility)) {
						bestUtility = mdp.grid[timeInterval][i][j][k].getUtility();
						finalWorkloadLevel = i;
						finalGreenEnergyLevel = j;
						finalBatteryLevel = k;
					}
				}
			}
		}
		System.out.println("Best Path: "
				+ mdp.grid[timeInterval][finalWorkloadLevel][finalGreenEnergyLevel][finalBatteryLevel].getPath());

		System.out.println("Final Utility: "
				+ mdp.grid[timeInterval][finalWorkloadLevel][finalGreenEnergyLevel][finalBatteryLevel].getUtility());

	}

	public void updateRewardMatrix(int time, State s1, State s2, double value) {
		int stateOneNo = s1.getWorkload() * mdp.getTotalGreenEnergyLevel() + s1.getGreenEnergy();
		int stateTwoNo = s2.getWorkload() * mdp.getTotalGreenEnergyLevel() + s2.getGreenEnergy();

		mdp.updateRewardMatrix(time, stateOneNo, stateTwoNo, value);
	}
	
	public void updateBatteryMatrix(int time, State s1, State s2, int value) {
		int stateOneNo = s1.getWorkload() * mdp.getTotalGreenEnergyLevel() + s1.getGreenEnergy();
		int stateTwoNo = s2.getWorkload() * mdp.getTotalGreenEnergyLevel() + s2.getGreenEnergy();

		mdp.updateBatteryMatrix(time, stateOneNo, stateTwoNo, value);
	}
			
	public void updateActionMatrix(int time, State s1, State s2, String string) {
		int stateOneNo = s1.getWorkload() * mdp.getTotalGreenEnergyLevel() + s1.getGreenEnergy();
		int stateTwoNo = s2.getWorkload() * mdp.getTotalGreenEnergyLevel() + s2.getGreenEnergy();
		
		mdp.updateActionMatrix(time, stateOneNo, stateTwoNo, string);
	}
	
}
