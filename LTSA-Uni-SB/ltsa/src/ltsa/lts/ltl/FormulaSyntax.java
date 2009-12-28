package ltsa.lts.ltl;

import java.util.*;

import ltsa.lts.*;

/*
 * abstract syntax tree for unexpanded (i.e. includes forall ) LTL formlae.
 */

public class FormulaSyntax {
	private final FormulaSyntax left, right;
	private final Symbol operator;
	private final Symbol proposition;
	private final ActionLabels range;
	private final ActionLabels action;
	private final Vector<Symbol> parameters;
	private final Vector<Stack<Symbol>> whenParameters; // overloaded with Stack

	// for "when expr"

	private FormulaSyntax(FormulaSyntax lt, Symbol op, FormulaSyntax rt,
			Symbol prop, ActionLabels r, ActionLabels a, Vector<Symbol> v,
			Vector<Stack<Symbol>> wv) {
		left = lt;
		right = rt;
		operator = op;
		proposition = prop;
		range = r;
		action = a;
		parameters = v;
		whenParameters = wv;
	}

	public static FormulaSyntax make(FormulaSyntax lt, Symbol op,
			FormulaSyntax rt) {
		return new FormulaSyntax(lt, op, rt, null, null, null, null, null);
	}

	public static FormulaSyntax make(Symbol prop) {
		return new FormulaSyntax(null, null, null, prop, null, null, null, null);
	}

	public static FormulaSyntax make(Symbol prop, ActionLabels r) {
		return new FormulaSyntax(null, null, null, prop, r, null, null, null);
	}

	public static FormulaSyntax make(Symbol prop, Vector<Symbol> v) {
		return new FormulaSyntax(null, null, null, prop, null, null, v, null);
	}

	public static FormulaSyntax makeW(Symbol prop, Vector<Stack<Symbol>> v) {
		return new FormulaSyntax(null, null, null, prop, null, null, null, v);
	}

	public static FormulaSyntax makeE(Symbol op, Stack<Symbol> v) {
		return new FormulaSyntax(null, op, null, null, null, null, v, null);
	}

	public static FormulaSyntax make(ActionLabels a) {
		return new FormulaSyntax(null, null, null, null, null, a, null, null);
	}

	public static FormulaSyntax make(Symbol op, ActionLabels r, FormulaSyntax rt) {
		return new FormulaSyntax(null, op, rt, null, r, null, null, null);
	}

	public Formula expand(FormulaFactory fac, Hashtable<String, Value> locals,
			Hashtable<String, Value> globals) {
		if (proposition != null) {
			if (range == null) {
				if (PredicateDefinition.definitions != null
						&& PredicateDefinition.definitions
								.containsKey(proposition.toString()))
					return fac.make(proposition);
				else {
					AssertDefinition p = AssertDefinition.definitions
							.get(proposition.toString());
					if (p == null)
						Diagnostics.fatal(
								"LTL fluent or assertion not defined: "
										+ proposition, proposition);
					if (parameters == null)
						return p.ltl_formula.expand(fac, locals, p.init_params);
					else {
						if (parameters.size() != p.params.size())
							Diagnostics.fatal(
									"Actual parameters do not match formals: "
											+ proposition, proposition);

						Hashtable<String, Value> actual_params = new Hashtable<String, Value>();
						Vector<Value> values = paramValues(whenParameters,
								locals, globals);
						for (int i = 0; i < parameters.size(); ++i)
							actual_params.put(p.params.elementAt(i), values
									.elementAt(i));
						return p.ltl_formula.expand(fac, locals, actual_params);
					}
				}
			} else {
				return fac.make(proposition, range, locals, globals);
			}
		} else if (action != null) {
			return fac.make(action, locals, globals);
		} else if (operator.kind == Symbol.RIGID) {
			return fac.make((Stack<Symbol>) parameters, locals, globals);
		} else if (operator != null && range == null) {
			if (left == null) {
				return fac.make(null, operator, right.expand(fac, locals,
						globals));
			} else {
				return fac.make(left.expand(fac, locals, globals), operator,
						right.expand(fac, locals, globals));
			}
		} else if (range != null && right != null) {
			range.initContext(locals, globals);
			Formula f = null;
			while (range.hasMoreNames()) {
				range.nextName();
				if (f == null)
					f = right.expand(fac, locals, globals);
				else {
					if (operator.kind == Symbol.AND)
						f = fac.makeAnd(f, right.expand(fac, locals, globals));
					else
						f = fac.makeOr(f, right.expand(fac, locals, globals));
				}
			}
			range.clearContext();
			return f;
		}
		return null;
	}

	private Vector<Value> paramValues(Vector<Stack<Symbol>> paramExprs,
			Hashtable<String, Value> locals, Hashtable<String, Value> globals) {
		if (paramExprs == null)
			return null;
		Enumeration<Stack<Symbol>> e = paramExprs.elements();
		Vector<Value> v = new Vector<Value>();
		while (e.hasMoreElements()) {
			Stack<Symbol> stk = e.nextElement();
			v.addElement(Expression.getValue(stk, locals, globals));
		}
		return v;
	}

}
