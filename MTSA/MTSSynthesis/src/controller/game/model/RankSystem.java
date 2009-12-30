package controller.game.model;

import java.util.*;

public interface RankSystem<State> {

	public abstract Rank getMaximum(int guaranteeId, Set<State> states);

	public abstract Rank getMinimum(int guaranteeId, Set<State> states);

	// I think that this function should not be available
	public abstract void increase(State state, int guarantee);

	public abstract boolean isInfinity(State state, int guarantee);

	public abstract void set(State state, int guarantee, Rank rank);

	public abstract Rank getRank(State state, int guaranteeIndex);

	public abstract RankContext getContext(int guaranteeId);

}