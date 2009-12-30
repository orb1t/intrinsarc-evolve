package ac.ic.doc.mtstools.model.impl;

import java.util.*;

import ac.ic.doc.commons.relations.*;
import ac.ic.doc.mtstools.model.*;

public class MTSAdapter<State, Action> implements MTS<State, Action> {

	private LTS<State, Action> lts;

	public MTSAdapter(LTS<State, Action> lts) {
		this.lts = lts;
	}

	public boolean addAction(Action action) {
		return lts.addAction(action);
	}

	public boolean addActions(Collection<? extends Action> actions) {
		return lts.addActions(actions);
	}

	public boolean addPossible(State from, Action label, State to) {
		throw new UnsupportedOperationException();
	}

	public boolean addRequired(State from, Action label, State to) {
		return lts.addTransition(from, label, to);
	}

	public boolean addState(State state) {
		return lts.addState(state);
	}

	public boolean addStates(Collection<? extends State> states) {
		return lts.addStates(states);
	}

	public boolean addTransition(State from, Action label, State to,
			TransitionType type) {
		if (type != TransitionType.REQUIRED) {
			throw new UnsupportedOperationException();
		}
		return lts.addTransition(from, label, to);
	}

	public Set<Action> getActions() {
		return lts.getActions();
	}

	public State getInitialState() {
		return lts.getInitialState();
	}

	public Set<State> getStates() {
		return lts.getStates();
	}

	public BinaryRelation<Action, State> getTransitions(State state,
			TransitionType type) {
		if (type == TransitionType.MAYBE) {
			return new MapSetBinaryRelation<Action, State>();
		}
		return lts.getTransitions(state);
	}

	public Map<State, BinaryRelation<Action, State>> getTransitions(
			TransitionType type) {
		if (type == TransitionType.MAYBE) {
			return new HashMap<State, BinaryRelation<Action, State>>();
		}
		return lts.getTransitions();
	}

	public boolean removeTransition(State from, Action label, State to,
			ac.ic.doc.mtstools.model.MTS.TransitionType type) {
		if (type != TransitionType.REQUIRED) {
			throw new UnsupportedOperationException();
		}
		return lts.removeTransition(from, label, to);
	}

	public boolean removePossible(State from, Action label, State to) {
		throw new UnsupportedOperationException();
	}

	public boolean removeRequired(State from, Action label, State to) {
		return lts.removeTransition(from, label, to);
	}

	public boolean removeUnreachableStates() {
		return lts.removeUnreachableStates();
	}

	public void removeAction(Action action) {
		lts.removeAction(action);
	}
}