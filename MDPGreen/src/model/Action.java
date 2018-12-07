package model;


/**
 * This class defines the Action class in Markov Decision Process
 * @author minxianx
 *
 */
public class Action {
	
	int c_workload;
	int c_greenEnergy;
	int c_battery;
	public Action(int c_wokrload, int c_greenEnergy, int c_battery) {
		this.c_workload = c_wokrload;
		this.c_greenEnergy = c_greenEnergy;
		this.c_battery = c_battery;
		
	}
	
	public int getChangedWorkload() {
		return c_workload;
		
	}
	
	public int getChangedGreenEnergy() {
		return c_greenEnergy;
	}
	
	public int getChangedBattery() {
		return c_battery;
	}
	
	public String toString() {
		return "workload change:" + c_workload +
				"green energy change:" + c_greenEnergy +
				"battery change:" + c_battery + "\n";
	}

}
