package ac.ic.doc.mtstools.model.impl;

import static ac.ic.doc.mtstools.model.SemanticType.STRONG;
import static ac.ic.doc.mtstools.model.SemanticType.WEAK;
import static ac.ic.doc.mtstools.model.SemanticType.WEAK_ALPHABET;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import ac.ic.doc.commons.relations.Pair;
import ac.ic.doc.mtstools.model.MTS;
import ac.ic.doc.mtstools.model.MTSConstants;
import ac.ic.doc.mtstools.model.operations.Consistency;
import ac.ic.doc.mtstools.test.util.MTSTestBase;

public class ConsistencyTests extends MTSTestBase {
	
		
	public void testSonConsistentes() throws Exception {
		String sourceString = "A = (a->a->A).\r\n";
		MTS<Long, String> mtsA = testUtils.buildMTSFrom(sourceString, ltsOutput);
		sourceString = "B = (a->B).\r\n";
		MTS<Long, String> mtsB = testUtils.buildMTSFrom(sourceString, ltsOutput);
		
		
		assertTrue(STRONG.getConsistency().areConsistent(mtsA, mtsB));
		
		Set<Pair<Long, Long>> consistecyRelation = STRONG.getConsistency().getConsistecyRelation(mtsA, mtsB);
		Set<Pair<Long, Long>> expectedConsistencyRelation = new HashSet<Pair<Long, Long>>();
		expectedConsistencyRelation.add(Pair.create(0L, 0L));
		expectedConsistencyRelation.add(Pair.create(1L, 0L));
		assertEquals(expectedConsistencyRelation, consistecyRelation);
		assertTrue(consistecyRelation.contains(Pair.create(0L, 0L)));
	}

	public void testSonWeakConsistentes() throws Exception {
		String sourceString = "A = (a->STOP).\r\n";
		MTS<Long, String> mtsA = testUtils.buildMTSFrom(sourceString, ltsOutput);
		sourceString = "B = (a->b->STOP)\\{b}.\r\n";
		MTS<Long, String> mtsB = testUtils.buildMTSFrom(sourceString, ltsOutput);
		
		assertTrue(WEAK.getConsistency().areConsistent(mtsA, mtsB));
		
		Set<Pair<Long, Long>> consistecyRelation = WEAK.getConsistency().getConsistecyRelation(mtsA, mtsB);
		Set<Pair<Long, Long>> expectedConsistencyRelation = new HashSet<Pair<Long, Long>>();
		expectedConsistencyRelation.add(Pair.create(0L, 0L));
		expectedConsistencyRelation.add(Pair.create(1L, 1L));
		expectedConsistencyRelation.add(Pair.create(1L, 2L));
		assertEquals(expectedConsistencyRelation, consistecyRelation);
		assertTrue(consistecyRelation.contains(Pair.create(0L, 0L)));
	}
	
	
	public void testConsistentes() throws Exception {
		String sourceString = "M = (a->b->M).\r\n";
		MTS<Long, String> mtsA = testUtils.buildMTSFrom(sourceString, ltsOutput);
		sourceString = "P = (a->b?->P).\r\n";
		MTS<Long, String> mtsB = testUtils.buildMTSFrom(sourceString, ltsOutput);

		assertTrue(STRONG.getConsistency().areConsistent(mtsA, mtsB));
	}
	
	
	
	public void testConsistentesAlfabetosDistintos() throws Exception {
		String sourceStringA = "A = (a?->B), B = (b->B | x->CM), CM = (c?->CM).\r\n";
		MTS<Long, String> mtsA = testUtils.buildMTSFrom(sourceStringA, ltsOutput);
		String sourceStringB = "A = (a->CM), CM = (b?->CM).\r\n";
		MTS<Long, String> mtsB = testUtils.buildMTSFrom(sourceStringB, ltsOutput);
		
		
		assertTrue(WEAK_ALPHABET.getConsistency().areConsistent(mtsA, mtsB));
		assertFalse(WEAK.getConsistency().areConsistent(mtsA, mtsB));
		
		
		sourceStringA = "A = (a?->B), B = (b->B | x->CM), CM = (c?->CM | a->STOP).\r\n";
		mtsA = testUtils.buildMTSFrom(sourceStringA, ltsOutput);
		
		assertFalse(WEAK_ALPHABET.getConsistency().areConsistent(mtsA, mtsB));
		assertFalse(WEAK.getConsistency().areConsistent(mtsA, mtsB));		
	}
	
	public void testWeakAlphabetConsistencyEmptySilentSet() throws Exception {
		String sourceStringA = "A = (a->A).\r\n";
		MTS<Long, String> mtsA = testUtils.buildMTSFrom(sourceStringA, ltsOutput);
		String sourceStringB = "B = (b->B).\r\n";
		MTS<Long, String> mtsB = testUtils.buildMTSFrom(sourceStringB, ltsOutput);
		
		Consistency consistency = new WeakAlphabetConsistency(Collections.emptySet());
				
		assertTrue(consistency.areConsistent(mtsA, mtsB));

		sourceStringA = "A = (a->A)+{b}.\r\n";
		mtsA = testUtils.buildMTSFrom(sourceStringA, ltsOutput);
		assertFalse(WEAK_ALPHABET.getConsistency().areConsistent(mtsA, mtsB));
		
		assertFalse(consistency.areConsistent(mtsA, mtsB));
		
	}
	
	public void testConsistentesEjemploSebas() throws Exception {
		String sourceStringA = "A = (a?->B), B = (b->B | x->CM), CM = (c?->CM).\r\n";
		MTS<Long, String> mtsA = testUtils.buildMTSFrom(sourceStringA, ltsOutput);
		String sourceStringB = "A = (a->CM), CM = (c?->CM).\r\n";
		MTS<Long, String> mtsB = testUtils.buildMTSFrom(sourceStringB, ltsOutput);
		
		assertTrue(WEAK_ALPHABET.getConsistency().areConsistent(mtsA, mtsB));
	}
	
	public void testCounterExampleWeakAlphabetCompleteness() throws Exception {
		// The following models are weak alphabet consistent but there is no
		// consistency relation between them.
		String sourceStringA = "A = (_tau? -> l -> STOP | l -> STOP | m -> STOP)\\{_tau}.\r\n";
		MTS<Long, String> mtsA = testUtils.buildMTSFrom(sourceStringA, ltsOutput);
		String sourceStringB = "B = ( a? -> l -> STOP | m -> STOP).\r\n";
		MTS<Long, String> mtsB = testUtils.buildMTSFrom(sourceStringB, ltsOutput);
				
		assertFalse(WEAK_ALPHABET.getConsistency().areConsistent(mtsA, mtsB));
	}

	public void testCounterExampleWeakAlphabetConsistency() throws Exception {
		String sourceStringA = "M = ( m -> STOP | l -> STOP | _tau? -> l -> STOP)\\{_tau}.\r\n";
		MTS<Long, String> mtsA = testUtils.buildMTSFrom(sourceStringA, ltsOutput);
		String sourceStringB = "N = ( m -> STOP | a? -> l -> STOP).\r\n";
		MTS<Long, String> mtsB = testUtils.buildMTSFrom(sourceStringB, ltsOutput);
		
		WeakAlphabetPlusCROperator crOperator = new WeakAlphabetPlusCROperator(Collections.singleton(MTSConstants.TAU));
		MTS<Pair<Long,Long>, String> cr = crOperator.compose(mtsA, mtsB);
		assertTrue(WEAK_ALPHABET.getRefinement().isARefinement(mtsA, cr));
		assertTrue(WEAK_ALPHABET.getRefinement().isARefinement(mtsB, cr));
		assertTrue(WEAK_ALPHABET.getConsistency().areConsistent(mtsA, mtsB));
	}
}
