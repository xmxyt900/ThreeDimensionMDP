package policy;

import model.*;
import Jama.*;

/**
 * Jama is the leaner package to solve solution matrix
 * @author minxianx
 *
 */
public class PolicyEvaluator {

	MarkovDecisionProcess mdp;
	
	int size;
	
	double gamma;
	
	double[][] A;
	
	double[][] b;
	
	public PolicyEvaluator(MarkovDecisionProcess mdp) 
			throws IllegalStateException {
		this.mdp = mdp;
		size = mdp.getNumReachableStates();
		
		if (size == 0) {
			throw new IllegalStateException("MDP is not prepared.");
		}
		
		A = new double[size][size];
		
		b = new double[size][1];
		
	}
	
	public void solve() {
		for(int i = 0; i < size; ++i) {
			b[i][0] = 0.;
			for(int j = 0; j < size; ++j) {
				A[i][j] = 0;
			}
		}
		
		State state = mdp.getStartState();
		while (state!= null) {
			int sIndex = state.getIndex();
			A[sIndex][sIndex] = 1.0;
			b[sIndex][0] = state.getReward();
			
			Action action = mdp.getAction(state);
			
			//Simplified here, only one probability to transit from one state to another
			State sPrime = mdp.transit(state, action);
			//double prob = mdp.transGrid[][sPrime.getWorkload()][sPrime.getGreenEnergy()][sPrime.getBattery()];
			double prob = sPrime.getProbability();
			if(sPrime.isTerminated()) {
				b[sIndex][0] += prob*sPrime.getReward();
			}else {
				A[sIndex][sPrime.getIndex()] -= prob;
			}
			
			state = mdp.getNextReachableState();
		}
		Matrix mA = new Matrix(A);
		if(mA.cond() > 1e3) 
			throw (new ArithmeticException("Singular solution matrix."));
		
		Matrix mb = new Matrix(b);
		Matrix x = mA.solve(mb);
		
		for (int i = 0; i < size; ++i) {
			((State) mdp.getReachableStates().get(i)).setReward(x.get(i,0));
		}
			
		
	}
}
