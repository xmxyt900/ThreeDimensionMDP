package model;

import java.util.Vector;

import model.*;
public class MarkovDecisionProcess {
	
	Vector transitions;
	State state;
	//The states that can be reached, which means the transition probability is not 0 for these states. 
	Vector reachableStates;
	
	Vector possibleActions;
	
	int totalWorkloadLevel;
	int totalGreenEnergyLevel;
	int totalBatteryLevel;
	//All the states
	State[][][] grid;
	
	Action[][][] actions;

	int numReachableState;

	int numActions;
	
	int currentStateIndex;
	
	int currentAction;
	
	//24 hours, 24 time intervals
	final int MAX_TIME_INTERVAL = 23;
	
	//initilize the MDP space
	public MarkovDecisionProcess(int totalWorkloadLevel, int totalGreenEnergyLevel, int totalBatteryLevel) {
		this.totalWorkloadLevel = totalWorkloadLevel;
		this.totalGreenEnergyLevel = totalGreenEnergyLevel;
		this.totalBatteryLevel = totalBatteryLevel;
		numActions = (2*totalWorkloadLevel-1) * (2*totalGreenEnergyLevel-1) * (2*totalBatteryLevel-1);
		for(int i = 0; i < totalWorkloadLevel; i++) {
			for(int j = 0; j < totalGreenEnergyLevel; j++) {
				for(int k = 0; k < totalBatteryLevel; k++) {
					grid[i][j][k] = new State(i ,j, k, 0.0, 0.0, 0);
				}
			}
		}
	
		
		reachableStates = new Vector(totalWorkloadLevel*totalGreenEnergyLevel*totalBatteryLevel);
			
	}
	
	/**
	 * Identify the possible actions at a time interval 
	 * @param totalWorkloadLevel
	 * @param totalGreenEnergyLevel
	 * @param totalBatteryLevel
	 */
	public void compileActions(int totalWorkloadLevel, int totalGreenEnergyLevel, int totalBatteryLevel) {
		//possible actions
		actions = new Action[2*totalWorkloadLevel-1][2*totalGreenEnergyLevel-1][2*totalBatteryLevel-1];
		for(int i = 1-totalWorkloadLevel; i < totalWorkloadLevel; i++ ) {
			for(int j = 1- totalGreenEnergyLevel; j < totalGreenEnergyLevel; j++) {
				for (int k = 1 - totalBatteryLevel; k < totalBatteryLevel; k++) {
					actions[i][j][k] = new Action(i ,j , k);
					possibleActions.add(actions[i][j][k] );
				}
			}
		}
		
	}
	
	/**
	 * Find the reachable states. The states with 0 probability are not considered. 
	 * The following 4 methods are related to states
	 */
	public void compileStates() {
		State s;
		int index = 0;
		reachableStates.clear();
		for(int i = 0; i < totalWorkloadLevel; i++) {
			for(int j = 0; j < totalGreenEnergyLevel; j++) {
				for(int k = 0; k < totalBatteryLevel; k++) {
					s = grid[i][j][k];
					if(s.getProbability() != 0) {
						//If probability of state is not 0, put this state into the reachable list.  
						reachableStates.add(s);
						index++;
					}
				}
			}
		}
		this.numReachableState = index;
	}
	
	public State getStartState() {
		currentStateIndex = 0;
		return (State)reachableStates.get(0);
	}
	
	public int getNumReachableStates() {
		return numReachableState;
	}
	
	public State getNextReachableState() {
		currentStateIndex ++;
		if(currentStateIndex == numReachableState)
			return null;
		else return (State)reachableStates.get(currentStateIndex);
			
	}
	/**
	 * The following methods are related to actions.
	 * @param s
	 * @param a
	 */
	public void setAction(State s, Action a) {
		if(s.isTerminated())
			return ;
		
		s.action = a;
	}
	
	public Action getAction(State s) {
		return s.action;
	}
	
	public Action getStartAction() {
		currentAction = 0;
		return actions[0][0][0];
	}

	
	public Action getNextAction() {
		currentAction ++;
		if(currentAction == numActions)
			return null;
		else
			return (Action) possibleActions.get(currentAction);
	}
	
	/**
	 * Get the reward value, reward function is defined in State class
	 * @param s
	 * @return
	 */
	public double getReward(State s) {
		return s.getReward();
	}
	
	/**
	 * Transit from one state to another with probability Pr(s, a, s')
	 * @param s
	 * @param a
	 * @return
	 */
	public State transit(State s, Action a) {
		if(isPossibleTransit(s, a)) {
			int newWorkloadLevel = s.getWorkload() + a.getChangedWorkload();
			int newGreenEnergyLevel = s.getGreenEnergy() + a.getChangedGreenEnergy();
			int newBatteryLevel = s.getBattery() + a.getChangedBattery();
			return grid[newWorkloadLevel][newGreenEnergyLevel][newBatteryLevel];
		}
		return s;
	}
	
	/**
	 * To validate the transit is possible, e.g. using some actions can reach out of boundary.
	 * The next state should belong to [0, maxLevel)
	 * @param s
	 * @param a
	 * @return
	 */
	public boolean isPossibleTransit(State s, Action a) {
		int newWorkloadLevel = s.getWorkload() + a.getChangedWorkload();
		int newGreenEnergyLevel = s.getGreenEnergy() + a.getChangedGreenEnergy();
		int newBatteryLevel = s.getBattery() + a.getChangedBattery();
	    if((newWorkloadLevel >= 0) &&  (newWorkloadLevel < totalWorkloadLevel)
	    		&& (newGreenEnergyLevel >= 0) && (newGreenEnergyLevel <  totalGreenEnergyLevel)
	    		&& (newBatteryLevel >= 0) &&  (newBatteryLevel <  totalBatteryLevel))
		return true;
	    else 
	    	return false;
	}
	
}
