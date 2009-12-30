package ac.ic.doc.mtstools.model;

import ac.ic.doc.commons.relations.*;

public interface RefinementByRelation extends Refinement {
	public <S1, S2, A> BinaryRelation<S1, S2> getLargestRelation(MTS<S1, A> m,
			MTS<S2, A> n);

	public <S1, S2, A> boolean isAValidRelation(MTS<S1, A> m, MTS<S2, A> n,
			BinaryRelation<S1, S2> relation);
}