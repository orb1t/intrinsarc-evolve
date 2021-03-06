package ac.ic.doc.mtstools.model.operations;

import java.util.*;

import ac.ic.doc.mtstools.model.impl.*;

public class MTSTrace<Action, State> {
	private List<MTSTransition<Action, State>> trace = new ArrayList<MTSTransition<Action, State>>();

	public void addTransition(MTSTransition transition) {
		this.trace.add(transition);
	}

	public ListIterator<MTSTransition<Action, State>> listIterator() {
		return trace.listIterator();
	}

	public void clear() {
		trace.clear();
	}

	public void add(MTSTransition<Action, State> newTransition) {
		trace.add(newTransition);
	}

	public int size() {
		return trace.size();
	}

	public void remove(int i) {
		trace.remove(i);
	}

	public void removeLastTransition() {
		this.remove(this.size() - 1);
	}

	public ListIterator<MTSTransition<Action, State>> listIterator(int index) {
		return trace.listIterator(index);
	}

	public MTSTransition<Action, State> get(int i) {
		return trace.get(i);
	}

	public String toString() {
		return trace.toString();
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof MTSTrace) {
			MTSTrace aTrace = (MTSTrace) obj;
			return trace.equals(aTrace.trace);
		}
		return false;
	}

	public int hashCode() {
		return trace.hashCode();
	}
}
