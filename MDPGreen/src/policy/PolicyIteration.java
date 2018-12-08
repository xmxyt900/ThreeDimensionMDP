package policy;

import model.*;
public class PolicyIteration {

	MarkovDecisionProcess mdp;
	PolicyEvaluator pe;
	
	public PolicyIteration(MarkovDecisionProcess mdp) {
		this.mdp = mdp;
		pe = new PolicyEvaluator(mdp);
	}
	
	

}
