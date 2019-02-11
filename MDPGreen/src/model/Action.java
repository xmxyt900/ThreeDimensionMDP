package model;


/**
 * This class defines the Action class in Markov Decision Process
 * @author minxianx
 *
 */
public class Action {
	
	int c_workload; // The workloads that will be changed
	int batteryUsed; //Can only be 0, 1
	public Action(int c_wokrload, int batteryUsed) {
		this.c_workload = c_wokrload;
		this.batteryUsed = batteryUsed;
		
	}
	
	public int getChangedWorkload() {
		return c_workload;
		
	}
	
	public int getBatteryUsed() {
		return batteryUsed;
	}
	
	public String toString() {
		return "[" + c_workload + ", " + batteryUsed + "]" + " workload change:" + c_workload +
				", battery used:" + batteryUsed + "\n";
	}
	
	public String toFormattedString() {
		return "[" + c_workload + ", " + batteryUsed + "]";
	}

}
