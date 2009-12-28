package dispatcher;

import java.util.*;

import lts.*;
import lts.Minimiser;
import lts.controller.*;
import lts.controller.util.*;
import lts.util.*;

import org.apache.commons.collections15.*;
import org.apache.commons.lang.*;

import ac.ic.doc.mtstools.facade.*;
import ac.ic.doc.mtstools.model.*;
import ac.ic.doc.mtstools.model.impl.*;
import ac.ic.doc.mtstools.model.operations.*;
import ac.ic.doc.mtstools.model.operations.impl.*;
import ac.ic.doc.mtstools.util.fsp.*;

/**
 * This class consists exclusively of static methods that operate on or return
 * CompactState, CompositeState and MTS.
 * 
 * In the MTSA architecture this class is the communication layer between the
 * graphical interface, LTSA Core and MTSACore. None of then communicates each
 * other directly, they do it through this class.
 * 
 */
public class TransitionSystemDispatcher {

	/**
	 * Given a CompactState model returns the optimistic version of it.
	 * 
	 * @param compactState
	 * @param output
	 * @return the optimistic representation of the compactState parameter
	 */
	public static CompactState getOptimisticModel(CompactState compactState,
			LTSOutput output) {
		MTS<Long, String> mts = AutomataToMTSConverter.getInstance().convert(
				compactState);
		long initialTime = System.currentTimeMillis();
		mts = MTSAFacade.getOptimisticModel(mts);
		output.outln("MTS Representation: Optimistic operator applied to "
				+ compactState.getName() + " generated in: "
				+ (System.currentTimeMillis() - initialTime) + "ms.");

		return MTSToAutomataConverter.getInstance().convert(mts,
				compactState.name);
	}

	/**
	 * Given a CompactState model returns the pessimistic version of it.
	 * 
	 * @param compactState
	 * @return the pessimistic representation of the compactState parameter
	 */
	public static CompactState getPessimistModel(CompactState composition) {
		MTS<Long, String> mts = AutomataToMTSConverter.getInstance().convert(
				composition);
		mts = MTSAFacade.getPesimisticModel(mts);
		return MTSToAutomataConverter.getInstance().convert(mts,
				composition.name);
	}

	/**
	 * Given a CompositeState model this method builds the optimistic version of
	 * it. If the composition field of compositeState parameter it's not null
	 * then it's replaced for its optimistic version. Else every compactState
	 * inside compositeState parameter it's replaced for its optimistic version.
	 * 
	 * @param compositeState
	 */
	public static void makeOptimisticModel(CompositeState compositeState,
			LTSOutput ltsOutput) {
		if (MTSUtils.isMTSRepresentation(compositeState)) {
			if (compositeState.getComposition() == null) {
				if (MTSUtils.isMTSRepresentation(compositeState)) {
					long initialTime = System.currentTimeMillis();
					compositeState.setMachines(TransitionSystemDispatcher
							.getOptimistModels(compositeState.getMachines(),
									new EmptyLTSOuput()));
					ltsOutput
							.outln("MTS Representation: Optimistic model generated for all automatas in: "
									+ (System.currentTimeMillis() - initialTime)
									+ "ms.");
				}
			} else {
				compositeState.setMachines(TransitionSystemDispatcher
						.getOptimistModels(compositeState.getMachines(),
								new EmptyLTSOuput()));
				compositeState.setComposition(TransitionSystemDispatcher
						.getOptimisticModel(compositeState.getComposition(),
								ltsOutput));
			}
		}
	}

	/**
	 * Given a CompositeState model this method builds the optimistic version of
	 * it. If the composition field of compositeState parameter it's not null
	 * then it's replaced for its optimistic version. Else every compactState
	 * inside compositeState parameter it's replaced for its optimistic version.
	 * 
	 * @param compositeState
	 */
	public static void makePessimisticModel(CompositeState compositeState,
			LTSOutput ltsOutput) {
		if (MTSUtils.isMTSRepresentation(compositeState)) {
			if (compositeState.getComposition() == null) {
				if (MTSUtils.isMTSRepresentation(compositeState)) {
					long initialTime = System.currentTimeMillis();
					compositeState
							.setMachines(getPessimisticModels(compositeState
									.getMachines()));
					ltsOutput
							.outln("MTS Representation: Pessimistic model generated for all automatas in: "
									+ (System.currentTimeMillis() - initialTime)
									+ "ms.");
				}
				applyComposition(compositeState, ltsOutput);
			} else {
				long initialTime = System.currentTimeMillis();
				compositeState.setComposition(TransitionSystemDispatcher
						.getPessimistModel(compositeState.getComposition()));
				ltsOutput
						.outln("MTS Representation: Pessimistic model generated for composition in: "
								+ (System.currentTimeMillis() - initialTime)
								+ "ms.");
			}
		}
	}

	/**
	 * Safety check over compositeState parameter.
	 * 
	 * 
	 * @param compositeState
	 *            model to be checked
	 * @param ltsOutput
	 *            used to print output in MTSA
	 */
	public static void checkSafety(CompositeState compositeState,
			LTSOutput ltsOutput) {
		checkSafetyOrDeadlock(false, compositeState, ltsOutput);
	}

	// public static <State, Action> boolean checkFLTL(MTS<State, Action> mts,
	// String propertyName) {
	//		
	// }
	//	
	private static void checkSafetyOrDeadlock(boolean checkDeadlocks,
			CompositeState compositeState, LTSOutput ltsOutput) {
		if (MTSUtils.isMTSRepresentation(compositeState)) {
			CompactState composition = compositeState.getComposition();

			if (hasCompositionDeadlockFreeImplementations(compositeState,
					ltsOutput)) {
				checkSafety(compositeState, ltsOutput, checkDeadlocks);
			} else {
				ltsOutput
						.outln("*****************************************************************************************");
				ltsOutput
						.outln("Model must have at least one deadlock free implementation for a Safety or Deadlock check.");
				ltsOutput
						.outln("*****************************************************************************************");
			}

		} else {
			compositeState.analyse(checkDeadlocks, ltsOutput);
		}
	}

	/**
	 * This method applies composition over the parameter model. If the
	 * <code>toCompose</code> model it's a LTS, then it can only be composed
	 * using parallel composition, it means that the compositionType field of
	 * <code>toCompose</code> should be set to Symbol.OR. If
	 * <code>toCompose</code> is an MTS then there are diferent kinds of model
	 * composition might be applied depending on the value of the
	 * compositionType field.
	 * 
	 * 
	 * @param toCompose
	 *            composite model to be composed
	 * @param ltsOutput
	 *            used for process output
	 */
	public static void applyComposition(CompositeState toCompose,
			LTSOutput ltsOutput) {
		int compositionType = toCompose.getCompositionType();
		switch (compositionType) {
		case Symbol.MERGE:
			merge(toCompose, ltsOutput);
			break;
		case Symbol.PLUS_CR:
			applyPlusCROperator(toCompose, ltsOutput);
			break;
		case Symbol.PLUS_CA:
			applyPlusCAOperator(toCompose, ltsOutput);
			break;
		case Symbol.OR:
		default:
			paralellComposition(toCompose, ltsOutput);
		}
		toCompose.applyOperations(ltsOutput);
	}

	private static void merge(CompositeState composition, LTSOutput ltsOutput) {
		if (composition.getComposition() == null) {
			ArrayList<MTS<Long, String>> toCompose = new ArrayList<MTS<Long, String>>();

			for (Iterator it = composition.getMachines().iterator(); it
					.hasNext();) {
				CompactState compactState = (CompactState) it.next();
				toCompose.add(AutomataToMTSConverter.getInstance().convert(
						compactState));
			}

			ltsOutput.outln("Applying Merge Operator to MTSs...("
					+ composition.name + ")");
			long initialTime = System.currentTimeMillis();

			if (toCompose.size() > 2) {
				ltsOutput
						.outln("Warning: Merge is being applied to more than two models. Pair-wise merging will be performed. See [FM06] for details on associativity of merge");
			}
			int i = 0;
			Set<String> tau = Collections.singleton(MTSConstants.TAU);
			MTS<Long, String> merge = toCompose.get(i++);
			while (i < toCompose.size()) {

				MTS<Long, String> mtsB = toCompose.get(i++);
				MTS<?, String> cr;

				try {
					cr = new WeakAlphabetMergeBuilder(tau).merge(merge, mtsB);
				} catch (Exception e) {
					if (CollectionUtils.isEqualCollection(mtsB.getActions(),
							merge.getActions())) {
						// Same alphabets
						// Strong or Weak
						ltsOutput
								.outln("***************************************************************");
						ltsOutput
								.outln("There is no weak consistency relation for these models.");
						ltsOutput
								.outln("This means they are inconsistent and  cannot be merged [TOSEM].");
						ltsOutput
								.outln("****************************************************************");
					} else {
						// Weak Alphabet
						ltsOutput
								.outln("********************************************************************************");
						ltsOutput
								.outln("There is no weak alphabet consistency relation for these models.");
						ltsOutput
								.outln("This does NOT mean they are inconsistent, however they cannot be merged [TOSEM].");
						ltsOutput
								.outln("Try merging them on their common alphabet. If they are still inconsistent,");
						ltsOutput
								.outln("then the models currently being merged are inconsistent [TOSEM].");
						ltsOutput
								.outln("*********************************************************************************");
					}
					return;
				}
				MTS<Long, String> mts = new GenericMTSToLongStringMTSConverter()
						.convert(cr);

				ltsOutput.outln("Merge operator applied in "
						+ (System.currentTimeMillis() - initialTime) + "ms.");
				composition.setComposition(MTSToAutomataConverter.getInstance()
						.convert(mts, composition.name));
				// ((CompositeState) composition).applyOperations(ltsOutput);

				Set<String> alphabetAminusB = new HashSet<String>(
						CollectionUtils.subtract(merge.getActions(), mtsB
								.getActions()));
				Set<String> alphabetBminusA = new HashSet<String>(
						CollectionUtils.subtract(mtsB.getActions(), merge
								.getActions()));

				alphabetAminusB.addAll(tau);
				alphabetBminusA.addAll(tau);

				RefinementByRelation refA = new WeakSemantics(alphabetBminusA);
				RefinementByRelation refB = new WeakSemantics(alphabetAminusB);

				ltsOutput
						.outln("Internal sanity check: Validating merge is a common refinement...");
				isRefinement(
						mts,
						composition.name,
						merge,
						((CompactState) composition.getMachines().get(i - 2)).name,
						refA, ltsOutput);
				isRefinement(
						mts,
						composition.name,
						mtsB,
						((CompactState) composition.getMachines().get(i - 1)).name,
						refB, ltsOutput);
				merge = mts;
				ltsOutput.outln(""); // leave an empty line
			}
		}
	}

	/***************************************************************************
	 * Applies parallel composition over all model instances in machines field
	 * of <code>compositeState</code> and it's result is setted to composition
	 * field of <code>compositeState</code>.
	 * 
	 * If <code>compositeState</code> is an LTS then LTS parallel composition is
	 * applied. If <code>compositeState</code> is an MTS then MTS parallel
	 * composition is applied.
	 * 
	 * 
	 * @param compositeState
	 * @param ltsOutput
	 */
	public static void paralellComposition(CompositeState compositeState,
			LTSOutput ltsOutput) {
		if (compositeState.getComposition() == null) {
			if (MTSUtils.isMTSRepresentation(compositeState)) {
				Vector machines = compositeState.getMachines();
				List<MTS<Long, String>> toCompose = new ArrayList<MTS<Long, String>>();

				long initialTime = System.currentTimeMillis();
				ltsOutput.outln("Converting MTSs from " + compositeState.name);

				for (Iterator<CompactState> it = machines.iterator(); it
						.hasNext();) {
					CompactState compactState = it.next();
					toCompose.add(AutomataToMTSConverter.getInstance().convert(
							compactState));
				}

				ltsOutput.outln("Composing MTSs from " + compositeState.name);

				MTS<Long, String> mts = compose(toCompose);

				ltsOutput.outln("MTSs composed in "
						+ (System.currentTimeMillis() - initialTime) + "ms.\n");

				CompactState convert = MTSToAutomataConverter.getInstance()
						.convert(mts, compositeState.name);
				compositeState.setComposition(convert);

			} else {
				compositeState.compose(ltsOutput);
			}
		}
	}

	private static MTS<Long, String> compose(List<MTS<Long, String>> toCompose) {
		// MTSComposer composer = new MTSComposer();
		// return composer.compose(toCompose);
		CompositionRuleApplier compositionRuleApplier = new CompositionRuleApplier();
		return new MTSMultipleComposer(compositionRuleApplier)
				.compose(toCompose);
	}

	/**
	 * Applies plus CR merge operator over the models in <code>machines</code>
	 * field of <code>compositeState</code>. The result of +CR is setted to the
	 * <code>composition</code> field of <code>compositeState</code> .
	 * 
	 * 
	 * @param compositeState
	 * @param ltsOutput
	 */
	public static void applyPlusCROperator(CompositeState compositeState,
			LTSOutput ltsOutput) {
		if (compositeState.getComposition() == null) {
			ArrayList<MTS<Long, String>> toCompose = new ArrayList<MTS<Long, String>>();

			long initialTime = System.currentTimeMillis();
			ltsOutput.outln("Converting CompactState to MTSs...");

			for (Iterator it = compositeState.getMachines().iterator(); it
					.hasNext();) {
				CompactState compactState = (CompactState) it.next();
				toCompose.add(AutomataToMTSConverter.getInstance().convert(
						compactState));
			}
			// ltsOutput.outln("MTSs converted in "
			// + (System.currentTimeMillis() - initialTime) + "ms.");

			ltsOutput.outln("Applying +CR Operator to MTSs...");
			initialTime = System.currentTimeMillis();

			Set<String> silentActions = Collections.singleton(MTSConstants.TAU);
			assert (toCompose.size() >= 2);
			Iterator<MTS<Long, String>> iterator = toCompose.iterator();

			MTS<Long, String> merge = iterator.next();
			while (iterator.hasNext()) {
				MTS<Long, String> mts = iterator.next();
				MTS<?, String> cr = new WeakAlphabetPlusCROperator(
						silentActions).compose(merge, mts);
				merge = new GenericMTSToLongStringMTSConverter().convert(cr);

			}

			ltsOutput.outln("+CR operator applied in "
					+ (System.currentTimeMillis() - initialTime) + "ms.");
			compositeState.setComposition(MTSToAutomataConverter.getInstance()
					.convert(merge, compositeState.name));
			ltsOutput.outln(""); // leave an empty line
			// ((CompositeState) composition).applyOperations(ltsOutput);
		}
	}

	/**
	 * Applies plus CR merge operator over the models in <code>machines</code>
	 * field of <code>compositeState</code>. The result of +CR is setted to the
	 * <code>composition</code> field of <code>compositeState</code> .
	 * 
	 * 
	 * @param compositeState
	 * @param ltsOutput
	 */
	public static void applyPlusCAOperator(CompositeState compositeState,
			LTSOutput ltsOutput) {
		if (compositeState.getComposition() == null) {
			PlusCARulesApplier plusCARulesApplier = new PlusCARulesApplier();
			String plusSymbol = "+CA";

			ArrayList<MTS<Long, String>> toCompose = new ArrayList<MTS<Long, String>>();

			long initialTime = System.currentTimeMillis();
			ltsOutput.outln("Converting CompactState to MTSs...");

			for (Iterator it = compositeState.getMachines().iterator(); it
					.hasNext();) {
				CompactState compactState = (CompactState) it.next();
				toCompose.add(AutomataToMTSConverter.getInstance().convert(
						compactState));
			}
			ltsOutput.outln("MTSs converted in "
					+ (System.currentTimeMillis() - initialTime) + "ms.");

			ltsOutput.outln("Applying " + plusSymbol + " operator to MTSs...");
			initialTime = System.currentTimeMillis();
			MTS<Long, String> merge = new MTSMultipleComposer(
					plusCARulesApplier).compose(toCompose);

			ltsOutput.outln(plusSymbol + " operator applied in "
					+ (System.currentTimeMillis() - initialTime) + "ms.");

			compositeState.setComposition(MTSToAutomataConverter.getInstance()
					.convert(merge, compositeState.name));
			ltsOutput.outln(""); // leave an empty line
			// ((CompositeState) composition).applyOperations(ltsOutput);
		}
	}

	/**
	 * Applies determinisation to <code>composition</code> parameter. The
	 * determinisation semantics depends on the model type. If
	 * <code>compositeState</code> it's an MTS then MTS semantics is applied,
	 * otherwise LTS is applied. If <code>compositeState</code> it's an MTS then
	 * before applying determinisation, composition is applied.
	 * 
	 * The result of determinisation is setted to <code>composition</code> field
	 * of <code>compositeState</code>/
	 * 
	 * @param compositeState
	 * @param ltsOutput
	 */
	public static void determinise(CompositeState compositeState,
			LTSOutput ltsOutput) {
		if (MTSUtils.isMTSRepresentation(compositeState)) {
			TransitionSystemDispatcher.applyComposition(compositeState,
					ltsOutput);
			CompactState determinise = determinise(compositeState
					.getComposition(), ltsOutput);
			compositeState.setComposition(determinise);
		} else {
			compositeState.determinise(ltsOutput);
		}
	}

	/**
	 * Applies determinisation over <code>lts</code> depending on the model type
	 * it can be used LTS or MTS semantic.
	 * 
	 * @param lts
	 * @param ltsOutput
	 *            output support
	 * @return deterministic version of <code>lts</code>
	 */
	public static CompactState determinise(CompactState lts, LTSOutput ltsOutput) {
		CompactState compactState = (CompactState) lts;
		if (MTSUtils.isMTSRepresentation(compactState)) {
			long initialTime = System.currentTimeMillis();
			ltsOutput.outln("Converting CompactState to MTS...");
			MTS<Long, String> mts = AutomataToMTSConverter.getInstance()
					.convert(compactState);
			// ltsOutput.outln("MTS converted in "
			// + (System.currentTimeMillis() - initialTime) + "ms.");

			// initialTime = System.currentTimeMillis();
			ltsOutput.outln("Determinising ...");
			MTSDeterminiser determiniser = new MTSDeterminiser(mts);
			mts = determiniser.determinize();
			ltsOutput.outln("MTS determinised in "
					+ (System.currentTimeMillis() - initialTime) + "ms.");
			return MTSToAutomataConverter.getInstance().convert(mts,
					compactState.name);
		}
		return lts;
	}

	/**
	 * 
	 * Checks if <code>refines</code> model it's a refinement of refined
	 * <code>semantic</code> as the semantic for the refinement check.
	 * 
	 * 
	 * @param refines
	 * @param refined
	 * @param semantic
	 * @param ltsOutput
	 * @return
	 */
	public static boolean isRefinement(CompactState refines,
			CompactState refined, SemanticType semantic, LTSOutput ltsOutput) {

		Refinement refinement = semantic.getRefinement();
		MTS<Long, String> refinedMTS = AutomataToMTSConverter.getInstance()
				.convert((CompactState) refined);
		MTS<Long, String> refinesMTS = AutomataToMTSConverter.getInstance()
				.convert((CompactState) refines);
		return isRefinement(refinesMTS, refines.name, refinedMTS, refined.name,
				refinement, ltsOutput);
	}

	/**
	 * 
	 * Checks if <code>refines</code> model it's a refinement of
	 * <code>refined</code> using <code>refinement</code> parameter as the
	 * refinement notion
	 * 
	 * @param refines
	 * @param refinesName
	 * @param refined
	 * @param refinedName
	 * @param refinement
	 * @param ltsOutput
	 * @param <S>
	 *            Models states type
	 * @param <A>
	 *            Models action type
	 * 
	 */
	public static <S, A> boolean isRefinement(MTS<S, A> refines,
			String refinesName, MTS<S, A> refined, String refinedName,
			Refinement refinement, LTSOutput ltsOutput) {

		String refinesOutput = "model [" + refinesName + "] ";
		String refinedOuput = "model [" + refinedName + "] ";
		ltsOutput.outln("Does " + refinesOutput + "refine " + refinedOuput
				+ "? Verifying...");
		long initialTime = System.currentTimeMillis();
		boolean isRefinement = refinement.isARefinement(refined, refines);
		String refinesString = (isRefinement) ? "Yes" : "No";
		// ltsOutput.outln("Verified that " + refinesOutput + refinesString
		ltsOutput.outln(refinesString + ". ("
				+ (System.currentTimeMillis() - initialTime) + "ms.)");
		return isRefinement;
	}

	/**
	 * Applies minimisation to <code>composition</code> parameter. The
	 * minimisation semantics depends on the model type. If
	 * <code>compositeState</code> it's an MTS then MTS semantics is applied,
	 * otherwise LTS is applied. If <code>compositeState</code> it's an MTS then
	 * before applying minimisation, composition is applied.
	 * 
	 * The result of minimisation is setted to <code>composition</code> field of
	 * <code>compositeState</code>/
	 * 
	 * @param compositeState
	 * @param ltsOutput
	 */
	public static void minimise(CompositeState compositeState,
			LTSOutput ltsOutput) {

		if (MTSUtils.isMTSRepresentation(compositeState)) {
			Validate.isTrue(compositeState.getComposition() != null,
					"MTS ON-THE-FLY minimisation it is not implemented yet.");

			CompactState compactState = (CompactState) compositeState
					.getComposition();

			// compactState may be null, for instance after trying
			// to merge two inconsistent MTSs.
			if (compactState != null) {
				MTS<Long, String> mts = mtsMinimise(compactState, ltsOutput);
				compositeState.setComposition(MTSToAutomataConverter
						.getInstance().convert(mts,
								compositeState.getComposition().name));
			}

		} else {
			compositeState.minimise(ltsOutput);
		}
	}

	private static MTS<Long, String> mtsMinimise(CompactState compactState,
			LTSOutput ltsOutput) {
		long initialTime = System.currentTimeMillis();
		ltsOutput.outln("Converting CompactState " + compactState.name
				+ " to MTS...");
		MTS<Long, String> mts = AutomataToMTSConverter.getInstance().convert(
				compactState);
		// ltsOutput.outln("MTS converted in "
		// + (System.currentTimeMillis() - initialTime) + "ms.");

		// initialTime = System.currentTimeMillis();
		ltsOutput.outln("Minimising with respect to refinement equivalence...");
		MTSMinimiser<String> minimiser = new MTSMinimiser<String>();

		// get the minimised MTS
		MTS<Long, String> minimisedMTS = minimiser.minimise(mts);
		ltsOutput.outln(compactState.name + " minimised in "
				+ (System.currentTimeMillis() - initialTime) + "ms.");

		// minimisation sanity check
		ltsOutput
				.outln("Internal sanity check: Validating minimised and original are equivalent by simulation...");
		WeakSemantics weakSemantics = new WeakSemantics(Collections
				.singleton(MTSConstants.TAU));
		isRefinement(mts, " original " + compactState.name, minimisedMTS,
				" minimised " + compactState.name, weakSemantics, ltsOutput);
		isRefinement(minimisedMTS, " minimised " + compactState.name, mts,
				" original " + compactState.name, weakSemantics, ltsOutput);
		ltsOutput.outln(""); // leave an empty line
		return minimisedMTS;
	}

	/**
	 * Minimise <code>compactState</code> model using MTS or LTS semantic
	 * depending on the model type.
	 * 
	 * @param compactState
	 * @param ltsOutput
	 * @return
	 */
	public static CompactState minimise(CompactState compactState,
			LTSOutput ltsOutput) {
		if (MTSUtils.isMTSRepresentation(compactState)) {
			MTS<Long, String> mts = mtsMinimise(compactState, ltsOutput);
			return MTSToAutomataConverter.getInstance().convert(mts,
					compactState.name);

		} else {
			Minimiser me = new Minimiser(compactState, ltsOutput);
			return me.minimise();
		}
	}

	/**
	 * Returns a vector with the optimistic representation of every model in the
	 * <code>originalMachines</code> parameter.
	 * 
	 * @param originalMachines
	 * @param output
	 * @return optimistic version of the original models.
	 */
	private static Vector getOptimistModels(Vector originalMachines,
			LTSOutput output) {
		Vector retValue = new Vector();
		for (Iterator ir = originalMachines.iterator(); ir.hasNext();) {
			CompactState compactState = (CompactState) ir.next();
			retValue.add(getOptimisticModel(compactState, output));
		}
		return retValue;
	}

	/**
	 * Returns a vector with the pessimistic representation of every model in
	 * the <code>originalMachines</code> parameter.
	 * 
	 * @param originalMachines
	 * @return pessimistic version of the original models.
	 */
	private static Vector getPessimisticModels(Vector originalMachines) {
		Vector retValue = new Vector();
		for (Iterator ir = originalMachines.iterator(); ir.hasNext();) {
			CompactState compactState = (CompactState) ir.next();
			retValue.add(getPessimistModel(compactState));
		}
		return retValue;
	}

	private static void checkSafety(CompositeState compositeState,
			LTSOutput ltsOutput, boolean checkDeadlocks) {
		long initialCurrentTimeMillis = System.currentTimeMillis();
		printLine(" ", ltsOutput);
		printLine(" ", ltsOutput);
		printLine("Starting safety check on " + compositeState.name, ltsOutput);

		long currentTimeMillis = 0;
		CompactState compactState = compositeState.getComposition();
		Vector machines = compositeState.getMachines();
		String reference = "[Missing Reference]";

		printLine(" ", ltsOutput);
		printLine("Phase I: Does " + compositeState.name + "+ have errors?",
				ltsOutput);

		CompactState optimisticModel = getOptimisticModel(compactState,
				ltsOutput);
		Vector toCheck = new Vector();
		toCheck.add(optimisticModel);
		compositeState.setMachines(toCheck);

		compositeState.analyse(checkDeadlocks, ltsOutput);

		if (compositeState.getErrorTrace() == null
				|| compositeState.getErrorTrace().isEmpty()) {
			// M+ |= FI
			printLine(compositeState.name
					+ "+ does not have errors. Which means that...", ltsOutput);
			printLine(
					"*******************************************************************************************",
					ltsOutput);
			printLine("NO ERRORS FOUND: All implementations of "
					+ compositeState.name + " do not have errors." + reference,
					ltsOutput);
			printLine(
					"********************************************************************************************",
					ltsOutput);

		} else {
			printLine(compositeState.name
					+ "+ does have errors. Which means that...", ltsOutput);
			printLine("This means that some implementations of "
					+ compositeState.name + " have errors.", ltsOutput);
			printLine("", ltsOutput);

			CompactState pessimisticModel = getPessimistModel(compactState);
			compositeState.setErrorTrace(ListUtils.EMPTY_LIST);

			if (!MTSUtils.isEmptyMTS(pessimisticModel)) {
				printLine("Phase II: Does " + compositeState.name
						+ "- have errors?", ltsOutput);

				toCheck = new Vector();
				toCheck.add(pessimisticModel);
				compositeState.setMachines(toCheck);
				compositeState.composition = null;

				compositeState.analyse(checkDeadlocks, ltsOutput);

				if (compositeState.getErrorTrace() == null
						|| compositeState.getErrorTrace().isEmpty()) {
					// M- |= FI
					printLine(compositeState.name
							+ "- does not have errors. Which means that...",
							ltsOutput);
					printLine(
							"*****************************************************************",
							ltsOutput);
					printLine("Model " + compositeState.name
							+ " has some implementations with errors."
							+ reference, ltsOutput);
					printLine(
							"*****************************************************************",
							ltsOutput);
				} else {
					// M- !|= FI
					printLine(compositeState.name
							+ "- does have errors. Which means that...",
							ltsOutput);
					printLine(
							"*****************************************************************",
							ltsOutput);
					printLine("All implmentations of  " + compositeState.name
							+ " have errors." + reference, ltsOutput);
					printLine(
							"*****************************************************************",
							ltsOutput);
				}
			} else {

				// complementar la propiedad
				// armar el buchi de la propiedad.
				printLine("Phase II: " + compositeState.name
						+ "- turned out to be empty. Does the complement of "
						+ compositeState.name + "+ have errors?", ltsOutput);

				CompactState originalProperty = null;
				int propertyIndex = 0;
				for (Iterator it = machines.iterator(); it.hasNext();) {
					CompactState aModel = (CompactState) it.next();
					if (MTSUtils.isPropertyModel(aModel)) {
						originalProperty = aModel;
						break;
					}
					propertyIndex++;
				}
				if (originalProperty == null) {
					Diagnostics.fatal("There must be a property to check.");
				}
				compositeState.composition = compactState;
				compositeState.machines = machines;

				CompactState property = TransitionSystemDispatcher
						.buildBuchiFromProperty(compositeState, ltsOutput);

				// chequear M+ junto con la propiedad complementada y dar
				// resultado.
				compositeState.composition = getOptimisticModel(compactState,
						ltsOutput);
				Vector<CompactState> propVector = new Vector<CompactState>();
				propVector.add(property);
				CompositeState propertyComp = new CompositeState(propVector);
				propertyComp.compose(ltsOutput);
				checkFLTL(compositeState, propertyComp, null, false, ltsOutput);
				// System.out.println("si llega aca es de milagro!");

				if (compositeState.getErrorTrace() == null
						|| compositeState.getErrorTrace().isEmpty()) {
					// M+ !|= NOT FI

					printLine(
							compositeState.name
									+ "+ does not satisfy the complement of the property. This means that... ",
							ltsOutput);
					printLine(
							"*****************************************************************",
							ltsOutput);
					printLine("All implmentations of  " + compositeState.name
							+ " have errors." + reference, ltsOutput);
					printLine(
							"*****************************************************************",
							ltsOutput);

				} else {
					// M+ |= NOT FI
					printLine(
							compositeState.name
									+ "+ satisfies the complement of the property. This means that... ",
							ltsOutput);
					printLine(
							"*****************************************************************",
							ltsOutput);
					printLine("Model " + compositeState.name
							+ " has implementations with errors." + reference,
							ltsOutput);
					printLine(
							"*****************************************************************",
							ltsOutput);
				}
			}
		}

		printLine(" ", ltsOutput);
		printLine("*******************************************", ltsOutput);
		printLine("Total safety analysis time: "
				+ (System.currentTimeMillis() - initialCurrentTimeMillis),
				ltsOutput);
		printLine("*******************************************", ltsOutput);

		// leave the original compositeState intact
		compositeState.setComposition(compactState);
		compositeState.setMachines(machines);
	}

	private static CompactState buildBuchiFromProperty(
			CompositeState compositeState, LTSOutput ltsOutput) {

		// mover a mtsUtils
		CompactState compactState = getProperty(compositeState);
		MTS<Long, String> property = AutomataToMTSConverter.getInstance()
				.convert(compactState);

		Long trapState = Collections.max(property.getStates()) + 1;

		MTSPropertyToBuchiConverter.convert(property, trapState, "@"
				+ compactState.name);
		return MTSToAutomataConverter.getInstance().convert(property,
				compactState.name);
	}

	@SuppressWarnings("unchecked")
	private static CompactState getProperty(CompositeState compositeState) {
		for (Iterator it = compositeState.machines.iterator(); it.hasNext();) {
			CompactState compactState = (CompactState) it.next();
			if (MTSUtils.isPropertyModel(compactState)) {
				return compactState;
			}
		}
		Diagnostics.fatal("There must be exactly one property to check");
		return null;
	}

	/**
	 * Returns true if the <code>compositeState</code> parameter has any
	 * deadlock free implementation.
	 * 
	 * @param compositeState
	 * @param ltsOutput
	 */
	public static boolean hasCompositionDeadlockFreeImplementations(
			CompositeState compositeState, LTSOutput ltsOutput) {
		applyComposition(compositeState, ltsOutput);
		if (MTSUtils.isMTSRepresentation(compositeState)) {
			MTS<Long, String> mts = AutomataToMTSConverter.getInstance()
					.convert((CompactState) compositeState.getComposition());
			MTSTrace<String, Long> trace = new MTSTrace<String, Long>();

			String reference = "[Mising Reference]";
			int deadlockStatus = MTSAFacade.getTraceToDeadlock(mts, trace);
			if (deadlockStatus == 1) {
				String output = "All implementations of " + compositeState.name
						+ " have a deadlock state." + reference;
				ltsOutput.outln(output);

				return false;
			} else if (deadlockStatus == 2) {
				String output = "All implementations of " + compositeState.name
						+ " are deadlock free." + reference;
				ltsOutput.outln(output);
			} else {
				String output = "Some implementations of "
						+ compositeState.name
						+ " are deadlock free while others have a deadlock state."
						+ reference;
				ltsOutput.outln(output);
			}
		} else {
			compositeState.analyse(true, ltsOutput);
		}
		return true;
	}

	private static void printLine(String toPrint, LTSOutput ltsOutput) {
		ltsOutput.outln(toPrint);
	}

	/**
	 * Checks if the model <code>compositeState</code> satisfies the property
	 * <code>ltlProperty</code>. If <code>compositeState</code> is an LTS, then
	 * the traditional LTS model checking algorithm is applied. Otherwise MTS
	 * model checking algorithm is applied.
	 * 
	 * 
	 * @param compositeState
	 * @param ltlProperty
	 * @param not_ltl_property
	 * @param fairCheck
	 * @param ltsOutput
	 */
	public static void checkFLTL(CompositeState compositeState,
			CompositeState ltlProperty, CompositeState not_ltl_property,
			boolean fairCheck, LTSOutput ltsOutput) {

		if (MTSUtils.isMTSRepresentation(compositeState)) {

			if (fairCheck) {
				throw new UnsupportedOperationException(
						"FLTL model checking of MTS with Fair Choice is not yet defined.");
			}
			// ISSUE We can't do FLTL check on-the-fly.
			applyComposition(compositeState, ltsOutput);

			if (saved != null) {
				compositeState.getMachines().remove(saved);
				saved = null;
			}

			compositeState.setErrorTrace(ListUtils.EMPTY_LIST);

			if (hasCompositionDeadlockFreeImplementations(compositeState,
					ltsOutput)) {

				long initialCurrentTimeMillis = System.currentTimeMillis();

				String reference = "[TOSEM]";
				printLine(" ", ltsOutput);
				printLine(" ", ltsOutput);
				printLine("Starting model check of " + compositeState.name
						+ " against property " + ltlProperty.name, ltsOutput);

				Vector machines = compositeState.getMachines();
				CompactState composition = compositeState.getComposition();

				CompactState optimistModel = getOptimisticModel(composition,
						ltsOutput);
				Vector toCheck = new Vector();
				toCheck.add(optimistModel);
				compositeState.setMachines(toCheck);

				printLine(" ", ltsOutput);
				printLine("Phase I: Does " + compositeState.name + "+ satisfy "
						+ ltlProperty.name + "?", ltsOutput);

				// phase I checking
				compositeState.checkLTL(ltsOutput, ltlProperty);

				if (compositeState.getErrorTrace() == null
						|| compositeState.getErrorTrace().isEmpty()) {
					// M+ |= FI
					printLine(" ", ltsOutput);
					printLine("Yes. " + compositeState.name + "+ satisfies. "
							+ ltlProperty.name, ltsOutput);
					printLine("This means that...", ltsOutput);
					printLine(
							"*****************************************************************",
							ltsOutput);
					printLine("All implementations of " + compositeState.name
							+ " satisfy " + ltlProperty.name + " " + reference,
							ltsOutput);
					printLine(
							"*****************************************************************",
							ltsOutput);

				} else {
					printLine(" ", ltsOutput);
					printLine("No. " + compositeState.name
							+ "+ does not satisfy " + ltlProperty.name,
							ltsOutput);
					printLine("This means that some implementations of "
							+ compositeState.name + " do not satisfy "
							+ ltlProperty.name, ltsOutput);

					CompactState pessimisticModel = getPessimistModel(composition);
					compositeState.setErrorTrace(ListUtils.EMPTY_LIST);

					if (!MTSUtils.isEmptyMTS(pessimisticModel)) {

						toCheck = new Vector();
						toCheck.add(pessimisticModel);
						compositeState.setMachines(toCheck);

						printLine(" ", ltsOutput);
						printLine("Phase II: Does " + compositeState.name
								+ "- satisfy " + ltlProperty.name + "?",
								ltsOutput);

						// phase II checking
						compositeState.checkLTL(ltsOutput, ltlProperty);

						if (compositeState.getErrorTrace() == null
								|| compositeState.getErrorTrace().isEmpty()) {
							// M- |= FI
							printLine("Yes. " + compositeState.name
									+ "- does satisfy " + ltlProperty.name
									+ ", which means that...", ltsOutput);
							printLine(
									"*****************************************************************",
									ltsOutput);
							printLine("Some but not all implementations of "
									+ compositeState.name + " satisfy "
									+ ltlProperty.name + " " + reference,
									ltsOutput);
							printLine(
									"*****************************************************************",
									ltsOutput);
						} else {
							// M- !|= FI
							printLine("No. " + compositeState.name
									+ "- does satisfy " + ltlProperty.name
									+ ", which means that...", ltsOutput);
							printLine(
									"*****************************************************************",
									ltsOutput);
							printLine("No implementations of "
									+ compositeState.name + " satisfy "
									+ ltlProperty.name + " " + reference,
									ltsOutput);
							printLine(
									"*****************************************************************",
									ltsOutput);
						}

					} else {
						printLine(" ", ltsOutput);
						printLine("Phase II: As " + compositeState.name
								+ "- is empty it cannot be checked against "
								+ ltlProperty.name + ". " + reference,
								ltsOutput);
						printLine("Will check " + compositeState.name
								+ "+ against the negation of "
								+ ltlProperty.name + " " + reference, ltsOutput);
						printLine("Does " + compositeState.name + "+ satisfy "
								+ not_ltl_property.name + "?", ltsOutput);

						// phase II checking
						compositeState.checkLTL(ltsOutput, not_ltl_property);

						if (compositeState.getErrorTrace() == null
								|| compositeState.getErrorTrace().isEmpty()) {
							// M+ |= !FI
							printLine("Yes. " + compositeState.name
									+ "+ does satisfy " + not_ltl_property.name
									+ ", which means that...", ltsOutput);

							printLine(
									"*****************************************************************",
									ltsOutput);
							printLine("No implementations of "
									+ compositeState.name + " satisfy "
									+ ltlProperty.name + " " + reference,
									ltsOutput);
							printLine(
									"*****************************************************************",
									ltsOutput);
						} else {
							// M+ !|= !FI
							printLine("No. " + compositeState.name
									+ "+ does not satisfy "
									+ not_ltl_property.name
									+ ", which means that...", ltsOutput);

							printLine(
									"*****************************************************************",
									ltsOutput);
							printLine("Some but not all implementations of "
									+ compositeState.name + " satisfy "
									+ ltlProperty.name + " " + reference,
									ltsOutput);
							printLine(
									"*****************************************************************",
									ltsOutput);
						}
					}
				}

				printLine(
						compositeState.name
								+ " model checked in "
								+ (System.currentTimeMillis() - initialCurrentTimeMillis)
								+ "ms", ltsOutput);

				// restore the orginal compositeState
				machines.add(saved = ltlProperty.getComposition());
				compositeState.setMachines(machines);
				compositeState.setComposition(composition);
			} else {
				ltsOutput
						.outln("The model must have deadlock free implementations to be checked.");
			}
		} else {
			if (compositeState.makeController) {
				checkControllerFLTL(compositeState, ltlProperty, ltsOutput);
			} else {
				compositeState.checkLTL(ltsOutput, ltlProperty);
			}
		}
	}

	private static void checkControllerFLTL(CompositeState compositeState,
			CompositeState ltlProperty, LTSOutput ltsOutput) {
		// Controllers does not support on-the-fly FLTL checking.
		synthesiseController(compositeState, ltsOutput);
		Vector machines = new Vector();
		machines.add(compositeState.getComposition());
		CompositeState cs = new CompositeState(compositeState.name, machines);
		cs.checkLTL(ltsOutput, ltlProperty);
	}

	private static CompactState saved = null;

	/**
	 * Builds the <b>abstract</b> version of the original model. The result it's
	 * builded following the following procedure: <br>
	 * Firstly a state (called trap state) with loop transitions on every label
	 * in the alphabet is added to the abstract model. Then from every state in
	 * <code>compactState</code> and for every label in
	 * <code>compactState</code>s alphabet for which there are no outgoing
	 * transition from it, it is added one transition to trap state.
	 * 
	 * @param compactState
	 * @param output
	 * @return
	 */
	public static CompactState getAbstractModel(CompactState compactState,
			LTSOutput output) {
		long initialTime = System.currentTimeMillis();
		MTSAbstractBuilder abstractBuilder = new MTSAbstractBuilder();
		MTS<Long, String> mts = AutomataToMTSConverter.getInstance().convert(
				compactState);
		Set<String> toDelete = new HashSet<String>();
		toDelete.add(MTSConstants.ASTERIX);
		toDelete.add(MTSConstants.AT);
		MTSUtils.removeActionsFromAlphabet(mts, toDelete);

		MTS<Long, String> abstractModel = abstractBuilder.getAbstractModel(mts);
		output.outln("Abstract model generated for " + compactState.name
				+ " in: " + (System.currentTimeMillis() - initialTime) + "ms.");

		return MTSToAutomataConverter.getInstance().convert(abstractModel,
				compactState.name);
	}

	/**
	 * Sets the result of <code>getAbstractModel</code> method applied to
	 * composition field of <code>compositeState</code> to the composition field
	 * again.
	 * 
	 * @param compositeState
	 * @param output
	 */
	public static void makeAbstractModel(CompositeState compositeState,
			LTSOutput output) {
		if (compositeState.getComposition() != null) {
			compositeState.setComposition(getAbstractModel(compositeState
					.getComposition(), output));
		}
	}

	/**
	 * For every state in wich are choices trasnform those transitions to maybe
	 * transitions.
	 * 
	 * @param compactState
	 * @param output
	 * @return
	 */
	public static CompactState getTauClosureModel(CompactState compactState,
			LTSOutput output) {
		MTS<Long, String> mts = AutomataToMTSConverter.getInstance().convert(
				compactState);
		Set<String> silentActions = new HashSet<String>();
		silentActions.add(MTSConstants.TAU);
		MTSAFacade.applyClosure(mts, silentActions);
		return MTSToAutomataConverter.getInstance().convert(mts,
				compactState.name);
	}

	/**
	 * Sets the result of <code>getClousureModel</code> method applied to
	 * composition field of <code>compositeState</code> to the composition field
	 * again.
	 * 
	 * @param compositeState
	 * @param output
	 */
	public static void makeClosureModel(CompositeState compositeState,
			LTSOutput output) {
		long initialTime = System.currentTimeMillis();
		if (compositeState.getComposition() != null) {
			compositeState.setComposition(TransitionSystemDispatcher
					.getTauClosureModel((CompactState) compositeState
							.getComposition(), output));
			output.outln("Clousure model generated for " + compositeState.name
					+ " in: " + (System.currentTimeMillis() - initialTime)
					+ "ms.");
		}
	}

	/**
	 * For every state in wich are choices trasnform those transitions to maybe
	 * transitions.
	 * 
	 * @param compactState
	 * @param output
	 * @return
	 */
	public static CompactState makeMTSConstraintModel(
			CompactState compactState, LTSOutput output) {
		MTS<Long, String> constrained = AutomataToMTSConverter.getInstance()
				.convert(compactState);

		long initialTime = System.currentTimeMillis();

		Set<String> toDelete = new HashSet<String>();
		toDelete.add(MTSConstants.ASTERIX);
		toDelete.add(MTSConstants.AT);
		MTSUtils.removeActionsFromAlphabet(constrained, toDelete);

		MTSConstraintBuilder constraintBuilder = new MTSConstraintBuilder();
		constraintBuilder.makeConstrainedModel(constrained);

		output.outln("Constrained model generated for " + compactState.name
				+ " in: " + (System.currentTimeMillis() - initialTime) + "ms.");
		return MTSToAutomataConverter.getInstance().convert(constrained,
				compactState.name);
	}

	public static void checkProgress(CompositeState compositeState,
			LTSOutput output) {

		if (MTSUtils.isMTSRepresentation(compositeState)) {
			Diagnostics.fatal("MTS Progress check has not been defined yet.");
		} else {
			if (compositeState.makeController) {
				Vector machines = new Vector();
				machines.add(compositeState.getComposition());
				CompositeState cs = new CompositeState(compositeState.name,
						machines);
				cs.checkProgress(output);
			} else {
				compositeState.checkProgress(output);
			}
		}
	}

	public static void synthesiseController(CompositeState compositeState,
			LTSOutput output) {
		if (compositeState.getComposition() == null) {
			TransitionSystemDispatcher.applyComposition(compositeState, output);
			// } else if
			// (!compositeState.getComposition().getName().startsWith(CONTROLLER_PREFIX))
			// {
		} else if (compositeState.getComposition().getGoal() == null) {

			compositeState.composition.setGoal(compositeState.goal);
			CompactState synthesiseController = synthesiseController(
					compositeState.composition, output);
			if (synthesiseController != null) {
				compositeState.setComposition(synthesiseController);
			}
		}
	}

	public static CompactState synthesiseController(CompactState c,
			LTSOutput output) {
		long initialTime = System.currentTimeMillis();

		boolean representation = MTSUtils.isMTSRepresentation(c);
		if (!representation) {
			if (c.getGoal() != null) {

				MTS<Long, String> mts = AutomataToMTSConverter.getInstance()
						.convert(c);
				mts = ControllerUtils.removeTopStates(mts, c.getGoal()
						.getFluents());

				MTS<Long, String> synthesisePlainStateController = MTSControllerSynthesisFacade
						.getInstance().synthesisePlainStateController(mts,
								c.getGoal());
				if (synthesisePlainStateController == null) {
					output.outln(" ");
					output.outln("There is no controller for model " + c.name
							+ " with the given setting.");
					output.outln(" ");
					return null;
				} else {
					output.outln("Controller Generated in: "
							+ (System.currentTimeMillis() - initialTime)
							+ "ms.");
					mts = synthesisePlainStateController;
					return MTSToAutomataConverter.getInstance().convert(mts,
							c.getName());
				}
			} else {
				Diagnostics.fatal("The Controller must have a goal.");
				return null;
			}
		} else {
			Diagnostics.fatal("The plant to be controlled must be an LTS.");
			return null;
		}
	}
}