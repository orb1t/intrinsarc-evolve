package ac.ic.doc.mtstools.model.operations;

import java.util.*;

import org.apache.commons.collections.*;

import ac.ic.doc.commons.relations.*;
import ac.ic.doc.mtstools.model.*;
import ac.ic.doc.mtstools.model.MTS.*;
import ac.ic.doc.mtstools.model.impl.*;

public class MTSAbstractBuilder {
	public MTS<Long, String> getAbstractModel(MTS<Long, String> mts) {
		MTS<Long, String> result = new MTSImpl<Long, String>(mts
				.getInitialState());
		result.addStates(mts.getStates());
		result.addActions(mts.getActions());
		// Must have new state and because of that I need trap state
		long newState = Collections.max(mts.getStates()) + 1;
		result.addState(newState);
		addCiclesOverAllActions(newState, result);
		for (Long state : mts.getStates()) {
			// para cada transicion que no haga
			Set<String> outgoingActions = new HashSet<String>();
			for (Pair<String, Long> transition : mts.getTransitions(state,
					TransitionType.REQUIRED)) {
				outgoingActions.add(transition.getFirst());
				result.addRequired(state, transition.getFirst(), transition
						.getSecond());
			}
			for (Pair<String, Long> transition : mts.getTransitions(state,
					TransitionType.MAYBE)) {
				outgoingActions.add(transition.getFirst());
				result.addPossible(state, transition.getFirst(), transition
						.getSecond());
			}
			Collection actionsToAdd = CollectionUtils.subtract(
					mts.getActions(), outgoingActions);
			actionsToAdd.remove(MTSConstants.TAU);
			for (Object object : actionsToAdd) {
				String action = (String) object;
				result.addPossible(state, action, newState);
			}
		}
		result.removeUnreachableStates();
		return result;
	}

	private void addCiclesOverAllActions(long newState, MTS<Long, String> mts) {
		Set<String> actions = mts.getActions();
		for (String action : actions) {
			if (!action.equals(MTSConstants.TAU)) {
				mts.addPossible(newState, action, newState);
			}
		}
	}
}
