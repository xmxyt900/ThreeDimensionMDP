package model;

import java.util.ArrayList;
import java.util.Vector;
public class MarkovDecisionProcess {
	

	Vector transitions;
	State state;
	//The states that can be reached, which means the transition probability is not 0 for these states. 
	Vector reachableStates;
	
	ArrayList<State> transitStates;
	
	
    Vector possibleActions;
	
    public int totalWorkloadLevel;
    public int totalGreenEnergyLevel;
	public int totalBatteryLevel;
	

	//All the states
	public State[][][][] grid;
	
	Action[][][] actions;

	int numReachableState;

	int numActions;
	
	int currentStateIndex;
	
	int currentAction;
	
	double prob[][][][];
	
	//24 hours, 24 time intervals
	int maxTimeInterval;
	
	State initialState;
		
	//initilize the MDP space
	public MarkovDecisionProcess(int totalWorkloadLevel, int totalGreenEnergyLevel, int totalBatteryLevel, double prob[][][][], int maxTimeInterval) {
		this.totalWorkloadLevel = totalWorkloadLevel;
		this.totalGreenEnergyLevel = totalGreenEnergyLevel;
		this.totalBatteryLevel = totalBatteryLevel;
		this.prob = prob;
		this.maxTimeInterval = maxTimeInterval;
		
		//Initial State, we set it at [0,0,0]
		initialState = new State(0, 0, 0, 1, 0, -1);
		initialState.setPath(initialState.toString());
		
		grid = new State[maxTimeInterval][totalWorkloadLevel][totalGreenEnergyLevel][totalBatteryLevel]; 
		//Initialize actions space
		numActions = (2*totalWorkloadLevel-1) * (2*totalGreenEnergyLevel-1) * (2*totalBatteryLevel-1);
		for(int t =0; t < maxTimeInterval; t++) {
		for(int i = 0; i < totalWorkloadLevel; i++) {
			for(int j = 0; j < totalGreenEnergyLevel; j++) {
				for(int k = 0; k < totalBatteryLevel; k++) {
					grid[t][i][j][k] = new State(i ,j, k, prob[t][i][j][k], 0.0, t);
					if( t == maxTimeInterval - 1) {
						grid[t][i][j][k].setTerminate();
					}
				}
			}
		}
		}	
		reachableStates = new Vector(totalWorkloadLevel*totalGreenEnergyLevel*totalBatteryLevel);
			
	}
	
	public void ListAllStates() {
		for(int t = 0; t < maxTimeInterval; t ++) {
			System.out.println("Time Interval: " + t);
		for(int i = 0; i < totalWorkloadLevel; i++) {
			for(int j = 0; j < totalGreenEnergyLevel; j++) {
				for(int k = 0; k < totalBatteryLevel; k++) {
					System.out.print(grid[t][i][j][k].toString());
				}
			}
		}
		}
	}
	
	/**
	 * Identify the possible actions at a time interval 
	 * @param totalWorkloadLevel
	 * @param totalGreenEnergyLevel
	 * @param totalBatteryLevel
	 */
	public void compileActions(int totalWorkloadLevel, int totalGreenEnergyLevel, int totalBatteryLevel) {
		//possible actions
		//Two possible actions for battery usage
		possibleActions = new Vector((2*totalWorkloadLevel-1) * 2);
		for(int i = 1-totalWorkloadLevel; i < totalWorkloadLevel; i++ ) {
			for(int j = 0; j < 2; j++) {
					possibleActions.add(new Action(i ,j));
			}
		}
		
	}
	
	/**
	 * Find the reachable states. The states with 0 probability are not considered. 
	 * The following 4 methods are related to states
	 */
	public void compileStates(int timeinterval) {
		if(timeinterval == -1) {
			//Add initial State
			reachableStates.add(initialState);
			this.numReachableState = 1;
			
		}else{				
		State s;
		int index = 0;
		reachableStates.clear();
		for(int i = 0; i < totalWorkloadLevel; i++) {
			for(int j = 0; j < totalGreenEnergyLevel; j++) {
				for(int k = 0; k < totalBatteryLevel; k++) {
					s = grid[timeinterval][i][j][k];
					if(s.getProbability() != 0) {
						//If probability of state is not 0, put this state into the reachable list.  
						s.index = index;
						index++;
						reachableStates.add(s);
					}
				}
			}
		}
		this.numReachableState = index;
		}
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
		if(currentStateIndex == numReachableState) {
			System.out.println("All reachable states have been evaluated......\n");
			return null;
		}
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
		return (Action) possibleActions.get(0);
	}

	
	public Action getNextAction() {
		currentAction ++;
		if(currentAction == possibleActions.size()) {
			System.out.println("All possible actions have been tried......\n");
			return null;
		}
		else
			return (Action) possibleActions.get(currentAction);
	}
	
	public void resetCurrentAction() {
		currentAction = 0;
	}
	
	public int getNumPossibleActions() {
		return possibleActions.size();
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
	public ArrayList<State> transit(State s, Action a, int timeInterval) {
		    transitStates = new ArrayList<State>();
			int newWorkloadLevel = s.getWorkload() + a.getChangedWorkload();
			int newBatteryLevel = getNextBatteryLevel(s, a);
			for(int newGreenEnergyLevel = 0; newGreenEnergyLevel < totalGreenEnergyLevel; newGreenEnergyLevel++) {
				transitStates.add(grid[timeInterval+1][newWorkloadLevel][newGreenEnergyLevel][newBatteryLevel]);
			}
			return transitStates;
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

	    if((newWorkloadLevel >= 0) &&  (newWorkloadLevel < totalWorkloadLevel))
	    {
		return true;
	    }
	    else { 
	    	return false;
	    }
	}
	
	public Vector getReachableStates() {
		return reachableStates;
	}
	
	public int getNextBatteryLevel(State s, Action a) {
		if(a.getBatteryUsed() == 1) {
		int workloadLevel = s.getWorkload() ;
		int greenEnergyLevel = s.getGreenEnergy() ;
        int batteryLevel = s.getBattery();
        //Example: State[4, 5, 10], Action[-2,1], 
        int nextBatteryLevel = Math.min(Math.max(greenEnergyLevel + batteryLevel - workloadLevel - a.getChangedWorkload(), 0), totalBatteryLevel-1);
        return nextBatteryLevel;
        //if battery is not not used for next time interval, battery level will be 0
		}else {
			return 0;
		}
	}
	
	
}
