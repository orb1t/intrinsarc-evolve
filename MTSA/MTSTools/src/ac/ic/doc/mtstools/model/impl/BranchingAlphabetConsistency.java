package ac.ic.doc.mtstools.model.impl;

import static ac.ic.doc.mtstools.model.MTS.TransitionType.*;

import java.util.*;

import org.apache.commons.collections15.*;

import ac.ic.doc.mtstools.model.*;
import ac.ic.doc.mtstools.model.operations.*;

public class BranchingAlphabetConsistency extends AbstractConsistencyRelation
		implements Consistency {

	private Set<?> silentActions;

	public BranchingAlphabetConsistency(Set<?> silentActions) {
		this.setSilentActions(silentActions);
	}

	public Set<?> getSilentActions() {
		return silentActions;
	}

	public void setSilentActions(Set<?> silentActions) {
		this.silentActions = silentActions;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <S1, S2, A> FixedPointRelationConstructor getRelationContructor(
			MTS<S1, A> mtsA, MTS<S2, A> mtsB) {

		Set extendedSiletActions = new HashSet(this.getSilentActions());
		extendedSiletActions.addAll(CollectionUtils.subtract(mtsA.getActions(),
				mtsB.getActions()));
		extendedSiletActions.addAll(CollectionUtils.subtract(mtsB.getActions(),
				mtsA.getActions()));

		return new FixedPointRelationConstructor(new SimulationChain().add(
				new BranchingForwardSimulation(extendedSiletActions, REQUIRED,
						POSSIBLE)).add(
				new BranchingBackwardSimulation(extendedSiletActions, REQUIRED,
						POSSIBLE)));
	}

}
