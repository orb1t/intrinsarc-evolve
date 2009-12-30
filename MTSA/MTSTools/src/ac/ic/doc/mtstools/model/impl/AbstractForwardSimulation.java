package ac.ic.doc.mtstools.model.impl;

import static ac.ic.doc.mtstools.model.MTS.TransitionType.*;

import java.util.*;

import ac.ic.doc.commons.relations.*;
import ac.ic.doc.mtstools.model.*;

/**
 * @author fdario
 * 
 */
public abstract class AbstractForwardSimulation<A> implements Simulation<A> {

	private MTS.TransitionType actualTransitionType;
	private MTS.TransitionType simulatorTransitionType;

	public AbstractForwardSimulation() {
		this(REQUIRED, REQUIRED);
	}

	public AbstractForwardSimulation(MTS.TransitionType actualTransitionType,
			MTS.TransitionType simulatorTransitionType) {
		this.setActualTransitionType(actualTransitionType);
		this.setSimulatorTransitionType(simulatorTransitionType);
	}

	public <S1, S2> boolean simulate(MTS<S1, A> mts1, S1 s1, MTS<S2, A> mts2,
			S2 s2, Set<Pair<S1, S2>> relation) {

		for (Pair<A, S1> transition : mts1.getTransitions(s1, this
				.getActualTransitionType())) {

			Iterator<List<Pair<A, S2>>> pathsIt = this.getPaths(mts2, s2,
					transition.getFirst());
			boolean simulate = false;
			while (!simulate && pathsIt.hasNext()) {
				simulate = this.simulate(s1, transition, s2, pathsIt.next(),
						relation);
			}
			if (!simulate) {
				return false;
			}
		}
		return true;
	}

	protected abstract <S> Iterator<List<Pair<A, S>>> getPaths(MTS<S, A> mts,
			S state, A label);

	protected abstract <S1, S2> boolean simulate(S1 s1, Pair<A, S1> transition,
			S2 s2, List<Pair<A, S2>> path, Set<Pair<S1, S2>> relation);

	public MTS.TransitionType getActualTransitionType() {
		return actualTransitionType;
	}

	public void setActualTransitionType(MTS.TransitionType behaviourToSimulate) {
		this.actualTransitionType = behaviourToSimulate;
	}

	public MTS.TransitionType getSimulatorTransitionType() {
		return simulatorTransitionType;
	}

	public void setSimulatorTransitionType(
			MTS.TransitionType simulateBehaviourWith) {
		this.simulatorTransitionType = simulateBehaviourWith;
	}
}