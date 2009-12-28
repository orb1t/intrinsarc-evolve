package ar.dc.uba.model.condition;

import java.util.*;

import org.apache.commons.lang.*;

import ac.ic.doc.commons.relations.*;
import ac.ic.doc.mtstools.model.*;
import ac.ic.doc.mtstools.model.MTS.*;
import ar.dc.uba.model.language.*;
import controller.game.util.*;

/**
 * Utility functions for Fluents
 * 
 * @author gsibay
 * 
 */
public class FluentUtils {

	// TODO: testear esta clase!

	private static FluentUtils instance;

	private FluentUtils() {
	}

	public static FluentUtils getInstance() {
		if (instance == null) {
			instance = new FluentUtils();
		}
		return instance;
	}

	/**
	 * Returns all the initiating actions of the fluents
	 * 
	 * @param fluents
	 * @return
	 */
	public Set<Symbol> getInitiatingSymbols(Collection<Fluent> fluents) {
		Set<Symbol> result = new HashSet<Symbol>();
		for (Fluent fluent : fluents) {
			result.addAll(fluent.getInitiatingActions());
		}

		return result;
	}

	/**
	 * Returns all the terminating actions of the fluents
	 * 
	 * @param fluents
	 * @return
	 */
	public Set<Symbol> getTerminatingSymbols(Collection<Fluent> fluents) {
		Set<Symbol> result = new HashSet<Symbol>();
		for (Fluent fluent : fluents) {
			result.addAll(fluent.getTerminatingActions());
		}

		return result;
	}

	/**
	 * Returns all the symbols affecting the valuation of the fluents; i.e all
	 * the initiating an terminating symbols of each fluent.
	 * 
	 * @param fluents
	 * @return
	 */
	public Set<Symbol> getInvolvedSymbols(Collection<Fluent> fluents) {
		HashSet<Symbol> result = new HashSet<Symbol>();
		result.addAll(this.getInitiatingSymbols(fluents));
		result.addAll(this.getTerminatingSymbols(fluents));
		return result;
	}

	/*
	 * DIPI: Parameters problem. toString shouldn't be used for this
	 */
	public <Action> boolean isActionInTerminatingSymbols(Action action,
			Fluent fluent) {
		return isActionInSymbolSet(action, fluent.getTerminatingActions());
	}

	// DIPI: Needs check. Is Action ok here?
	public <Action> boolean isActionInInitiatingSymbols(Action action,
			Fluent fluent) {
		return isActionInSymbolSet(action, fluent.getInitiatingActions());
	}

	private <Action> boolean isActionInSymbolSet(Action action,
			Set<Symbol> symbols) {
		for (Symbol symbol : symbols) {
			// DIPI: needs change! toString it's not sound.
			if (symbol.toString().equals(action.toString())) {
				return true;
			}
		}
		return false;
	}

	public <State, Action> FluentStateValuation<State> buildValuation(
			MTS<State, Action> mts, Set<Fluent> fluents) {
		FluentStateValuation<State> valuation = new FluentStateValuation<State>(
				mts.getStates());
		// initialise
		this.initialiseInitialState(fluents, mts, valuation);

		// tableau method
		Set<State> visited = new HashSet<State>();
		Queue<State> toVisit = new LinkedList<State>();
		toVisit.addAll(mts.getStates());
		while (!toVisit.isEmpty()) {
			State actual = toVisit.poll();
			visited.add(actual);
			for (Pair<Action, State> transition : mts.getTransitions(actual,
					TransitionType.REQUIRED)) {
				boolean changed = false;
				State to = transition.getSecond();
				for (Fluent fluent : fluents) {
					// DIPI: Change Needed. Use the toString method it is not
					// correct, we need to parametrise the fluent class
					Action transitionLabel = transition.getFirst();

					boolean actionInTerminatingSymbols = isActionInTerminatingSymbols(
							transitionLabel, fluent);
					boolean actionInInitiatingSymbols = isActionInInitiatingSymbols(
							transitionLabel, fluent);
					if ((valuation.isTrue(actual, fluent) && !actionInTerminatingSymbols)
							|| actionInInitiatingSymbols) {
						Validate.isTrue(!mts.getInitialState().equals(to)
								|| valuation.isTrue(to, fluent),
								"Initial State: " + to
										+ " has the top value for fluent: "
										+ fluent);
						changed = valuation.addHoldingFluent(to, fluent);
					}
				}
				if (changed) {
					toVisit.add(to);
				}
			}
		}
		for (State from : mts.getStates()) {
			for (Pair<Action, State> transition : mts.getTransitions(from,
					TransitionType.REQUIRED)) {
				State to = transition.getSecond();
				if (mts.getTransitions(to, TransitionType.REQUIRED).size() < 1) {
					continue;
				}
				for (Fluent fluent : fluents) {
					String errorMessage = "State: " + to
							+ " has the top value for fluent: " + fluent;
					boolean stateToValuation = valuation.isTrue(to, fluent);
					if (valuation.isTrue(from, fluent)) {
						if (isActionInTerminatingSymbols(transition.getFirst(),
								fluent)) {
							Validate.isTrue(!stateToValuation, errorMessage);
						}
					} else {
						if (isActionInInitiatingSymbols(transition.getFirst(),
								fluent)) {
							Validate.isTrue(stateToValuation, errorMessage);
						}
					}
				}
			}
		}

		return valuation;
	}

	private <State, Action> void initialiseInitialState(Set<Fluent> fluents,
			MTS<State, Action> mts, FluentStateValuation<State> relation) {

		for (Fluent fluent : fluents) {
			if (fluent.initialValue()) {
				relation.addHoldingFluent(mts.getInitialState(), fluent);
			}
		}
	}
}
