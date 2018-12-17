package model;

/**
 * This class defines the State in Markov Decision Process
 * @author minxianx
 *
 */
public class State {
	
	//Three dimension to represent state, including workload level, green energy level (renewable energy, e.g. solar
	//power) and battery.
	int workload, greenEnergy, battery;
	//Transition probability of each state
	double probability;
	//Reward of each state
	double reward;
	//Action to change one state to another
	//To be defined later, should also be a three dimension action
	Action action;
	//It is used to trace a proper policy, the visited state can be stored, thus no need to calculate again.
	boolean visited;
	//Time interval as we consider the finite markov decision process. 
	//We consider 24 hours as observation time, which consist of 24 intervals {0,1,...,23}
	int time;
	//State is terminated, e.g. at the last time interval
	boolean terminate;
	
	//Index of this state in the reachable state vector
	int index;
	
	//Utility based on rewards in all states
	double utility;
	
	//Path to reach current state
	String path; 
	
	//Used to balance performance and renewable energy usage in getRewward() function
	
	
	//Weight in reward function, if lambda = 1.0, it cares more about brown energy usage; if lambda = 0.0, it cares
	//more about number of services running
	final static double lambda = 0.5;
	
	public State(int workload, int greenEnergy, int battery, double probability, double reward, int time) {
		this.workload = workload;
		this.greenEnergy = greenEnergy;
		this.battery = battery;
		this.reward = reward;
		this.action = null;
		this.index = -1;
		this.visited = false;
		this.time= time;
		this.terminate = false;
		this.probability = probability;
		this.utility = 0;
		this.path = "";
	}
	
	public String toString() {
		return ("State[" + workload + ", " + greenEnergy + ", " + battery +"] " + "reward:" + getReward() + " prob:" + getProbability() + "\n");
	}
	
	public void setTerminate() {
		this.terminate = true;
	}
	
	public boolean isTerminated() {
		return this.terminate;
	}
	
	public boolean isVisited() {
		return visited;
	}
	
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	public int getWorkload() {
		return workload;
	}
	
	public int getGreenEnergy() {
		return greenEnergy;
	}
	
	public int getBattery() {
		return battery;
	}
	
	//Computed based on probabilities of workload, greenenergy and battery probability of at different levels
	//W: workload, G: greenEnergy, B: battery
	//E.g. Pr(W=10) = 0.8, Pr(G=5) = 0.8, Pr(B=0) = 0.5, then Pr(S=(10, 5, 0)) = 0.8*0.8*0.5
	public double getProbability() {
		return probability;
		//return Math.random();
	}
	
	//Reward value
	//W: workload, G: greenEnergy, B: battery
	//Reward of State 1: W = 10, G = 5, B = 0£¬ max(10-5-0, 0) = 5, reward = 10 - 5 = 5
	//Reward of State 2: W = 10, G = 15, B = 0, max(10 -15 - 0, 0) =0, reward = 10 - 0 = 10
	//Reward of State 2 is better, which means few brown energy is used while supporting same amount of microservices. 
	
   //New reward function:
	//(lambda)* -(max(W - G -B), 0) + (1 - lambda)*W
	public double getReward() {
//		return workload - Math.max(workload - greenEnergy - battery, 0);
		return lambda * -1 * Math.max(workload - greenEnergy - battery, 0) + (1 - lambda) * workload;
		
	}
	
	public void setReward(double reward) {
		this.reward = reward;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getTimeInterval() {
		return time;
	}
	
	public void setUtility(double utility) {
		this.utility = utility;
	}
	
	public double getUtility() {
		return utility;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * Get the best path to reach this sate
	 * @return
	 */
	public String getPath() {
		return path; 
	}

}
