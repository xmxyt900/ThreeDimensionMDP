package policy;

import java.util.ArrayList;

import model.*;


/**
 * This class is used to iterate different paths (policies) to maximize objective fucntion.
 * 
 * @author minxianx
 *
 */
public class PolicyEvaluator {

	MarkovDecisionProcess mdp;
	
   ArrayList<State> transitStates;

	int size;
	//Not used yet, but can be used for further extension in utility function.
//	double gamma;

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
	 * @param timeInveral
	 */
	public void solve(int timeInterval) {

		System.out.println("################Time Interval " +timeInterval + " Started#####################\n");
		State state = mdp.getStartState();
		Action action;
		//Go through all reachable states
		while (state != null) {		
			
			System.out.print("Now Trying actions for " + state.toString());

			State sPrime; //Next state after action
			double prob;
			double reward;
			double preUtility;
			double afterUtility;

			int tempWorkloadLevel;
			int tempGreenEnergyLevel;
			int tempBatteryLevel;
			String tempPath;
			
			action = mdp.getStartAction();
			//Reste the action index to 0;
			mdp.resetCurrentAction();
			//Go through all possible actions
			while (action != null) {
				if (mdp.isPossibleTransit(state, action)) {
					transitStates = mdp.transit(state, action, timeInterval);
					for(int transitStatesIndex = 0; transitStatesIndex < transitStates.size(); transitStatesIndex++) {
										
					sPrime = transitStates.get(transitStatesIndex);
					reward = sPrime.getReward();
					prob = sPrime.getProbability();
					preUtility = state.getUtility();
					//Utility function: U(s')= R(s)+ prob(s,a,s')*R(s')
					afterUtility = preUtility + prob * reward;

					tempWorkloadLevel = sPrime.getWorkload();
					tempGreenEnergyLevel = sPrime.getGreenEnergy();
					tempBatteryLevel = sPrime.getBattery();
					tempPath = state.getPath();
					
					if (sPrime.isVisited() == false) {

											mdp.grid[timeInterval + 1][tempWorkloadLevel][tempGreenEnergyLevel][tempBatteryLevel].setVisited(true);

						
											mdp.grid[timeInterval + 1][tempWorkloadLevel][tempGreenEnergyLevel][tempBatteryLevel]
													.setUtility(afterUtility);
							
											mdp.grid[timeInterval + 1][tempWorkloadLevel][tempGreenEnergyLevel][tempBatteryLevel]
													.setPath(tempPath + "-->" + "Time interval#" + (timeInterval+1) +" "+ sPrime.toString() + "By action: " + action.toString());
											System.out.print("###### With Action " + action.toString());
											System.out.println("###### Path updated for " + sPrime.toString());

										}

					//Keep the path with highest utility
					if (sPrime.isVisited() == true && afterUtility > mdp.grid[timeInterval
							+ 1][tempWorkloadLevel][tempGreenEnergyLevel][tempBatteryLevel].getUtility()) {

						mdp.grid[timeInterval + 1][tempWorkloadLevel][tempGreenEnergyLevel][tempBatteryLevel]
								.setUtility(afterUtility);
						mdp.grid[timeInterval + 1][tempWorkloadLevel][tempGreenEnergyLevel][tempBatteryLevel]
								.setPath(tempPath + "-->" + "Time interval#" + (timeInterval+1) +" "+ sPrime.toString() + "By action: " + action.toString());
						System.out.print("###### With Action " + action.toString());
						System.out.println("###### Path updated for " + sPrime.toString());
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
	 * @param timeInterval
	 */
	public void solveFinalSate(int timeInterval) {
		double bestUtility = - 2^30;
		int finalWorkloadLevel = -1;
		int finalGreenEnergyLevel = -1;
		int finalBatteryLevel = -1;
		//Find the final state as well as its path that can maximize optimization objective
		for (int i = 0; i < mdp.totalWorkloadLevel; i++) {
			for (int j = 0; j < mdp.totalGreenEnergyLevel; j++) {
				for (int k = 0; k < mdp.totalBatteryLevel; k++) {
					if( mdp.grid[timeInterval][i][j][k].getUtility() > bestUtility) {
						bestUtility = mdp.grid[timeInterval][i][j][k].getUtility();
						finalWorkloadLevel = i;
						finalGreenEnergyLevel = j;
						finalBatteryLevel = k;
					}
				}
			}
		}
		System.out.println("Best Path: " + mdp.grid[timeInterval][finalWorkloadLevel][finalGreenEnergyLevel][finalBatteryLevel].getPath());
	
		System.out.println("Final Utility: " + mdp.grid[timeInterval][finalWorkloadLevel][finalGreenEnergyLevel][finalBatteryLevel].getUtility());

	}
}
