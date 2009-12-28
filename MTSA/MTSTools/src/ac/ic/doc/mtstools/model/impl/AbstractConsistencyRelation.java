package ac.ic.doc.mtstools.model.impl;

import java.util.*;

import ac.ic.doc.commons.relations.*;
import ac.ic.doc.mtstools.model.*;

public abstract class AbstractConsistencyRelation {

	public AbstractConsistencyRelation() {
		super();
	}

	protected abstract <S1, S2, A> FixedPointRelationConstructor getRelationContructor(
			MTS<S1, A> mtsA, MTS<S2, A> mtsB);

	public <S1, S2, A> boolean areConsistent(MTS<S1, A> mtsA, MTS<S2, A> mtsB) {
		return getConsistecyRelation(mtsA, mtsB).contains(
				Pair.create(mtsA.getInitialState(), mtsB.getInitialState()));
	}

	public <S1, S2, A> Set<Pair<S1, S2>> getConsistecyRelation(MTS<S1, A> mtsA,
			MTS<S2, A> mtsB) {
		return getRelationContructor(mtsA, mtsB).getLargestRelation(mtsA, mtsB);
	}

}