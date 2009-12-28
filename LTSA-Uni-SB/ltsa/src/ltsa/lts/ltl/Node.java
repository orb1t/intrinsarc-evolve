package ltsa.lts.ltl;

import java.util.*;

import ltsa.lts.*;

public class Node implements Comparable<Node> {

	int id = 0;
	int equivId = -1;
	SortedSet<Node> incoming; // list of incoming nodes
	SortedSet<Formula> oldf; // list of formula
	SortedSet<Formula> newf; // list of formula
	SortedSet<Formula> next; // list of formula
	BitSet accepting; // accepting set
	BitSet rightOfU; // right of untils set of formula
	static FormulaFactory fac;
	static GeneralizedBuchiAutomata aut;

	static void setAut(GeneralizedBuchiAutomata a) {
		aut = a;
	} // a bit kludgy

	static void setFactory(FormulaFactory f) {
		fac = f;
	}

	public Node() {
		this(null, null, null, null, null, null);
	}

	public Node(SortedSet<Node> i, SortedSet<Formula> o, SortedSet<Formula> n,
			SortedSet<Formula> x, BitSet a, BitSet r) {
		this.id = aut.newId();
		incoming = (i != null) ? new TreeSet<Node>(i) : new TreeSet<Node>();
		oldf = (o != null) ? new TreeSet<Formula>(o) : new TreeSet<Formula>();
		newf = (n != null) ? new TreeSet<Formula>(n) : new TreeSet<Formula>();
		next = (x != null) ? new TreeSet<Formula>(x) : new TreeSet<Formula>();
		accepting = new BitSet();
		if (a != null)
			accepting.or(a);
		rightOfU = new BitSet();
		if (r != null)
			rightOfU.or(r);
	}

	public Node(Formula f) {
		this();
		collapsed = false;
		if (!(f instanceof True))
			decomposeAndforNext(f);
	}

	public int compareTo(Node obj) {
		return id - (obj).id;
	}

	public void decomposeAndforNext(Formula f) {
		if (f instanceof And) {
			decomposeAndforNext(((And) f).getLeft());
			decomposeAndforNext(((And) f).getRight());
		} else if (!isRedundant(next, null, f))
			next.add(f);
	}

	private boolean isRedundant(SortedSet<Formula> one, SortedSet<Formula> two,
			Formula f) {
		return fac.specialCaseV(f, one)
				|| fac.syntaxImplied(f, one, two)
				&& (!(f instanceof Until) || fac.syntaxImplied(f.getSub2(),
						one, two));
	}

	private Node split(Formula f) {
		Node node = new Node(incoming, oldf, newf, next, accepting, rightOfU);
		Formula nf = f.getSub2();
		if (!oldf.contains(nf))
			node.newf.add(nf);
		if (f instanceof Release) {
			nf = f.getSub1();
			if (!oldf.contains(nf))
				node.newf.add(nf);
		}
		nf = f.getSub1();
		if (!oldf.contains(nf))
			newf.add(nf);
		nf = ((f instanceof Until) || (f instanceof Release)) ? f : null;
		if (nf != null)
			decomposeAndforNext(nf);
		if (f instanceof Until) {
			accepting.set(f.getUI());
			node.accepting.set(f.getUI());
		}
		if (f.isRightOfUntil()) {
			rightOfU.or(f.getRofWU());
			node.rightOfU.or(f.getRofWU());
		}
		if (f.isLiteral()) {
			oldf.add(f);
			node.oldf.add(f);
		}
		return node;
	}

	public List<Node> expand(List<Node> aut) {
		if (newf.isEmpty()) {
			if (id != 0)
				accepting.andNot(rightOfU);
			Node node = alreadyThere(aut);
			if (node != null) {
				node.modify(this);
				return aut;
			} else {
				Node n = new Node();
				n.incoming.add(this);
				n.newf.addAll(next);
				aut.add(this);
				return n.expand(aut);
			}
		}
		Formula nf = newf.first();
		newf.remove(nf);
		if (contradiction(nf))
			return aut;
		SortedSet<Formula> oldUnew = new TreeSet<Formula>();
		oldUnew.addAll(oldf);
		oldUnew.addAll(newf);
		if (isRedundant(oldUnew, next, nf))
			return expand(aut);
		if (!nf.isLiteral()) {
			if ((nf instanceof Or) || (nf instanceof Until)
					|| (nf instanceof Release)) {
				Node n1 = split(nf);
				return n1.expand(expand(aut));
			}
			if (nf instanceof And) {
				Formula f = nf.getSub1();
				if (!oldf.contains(f))
					newf.add(f);
				f = nf.getSub2();
				if (!oldf.contains(f))
					newf.add(f);
				if (nf.isRightOfUntil())
					rightOfU.or(nf.getRofWU());
				return expand(aut);
			}
			if (nf instanceof Next) {
				decomposeAndforNext(nf.getSub1());
				if (nf.isRightOfUntil())
					rightOfU.or(nf.getRofWU());
				return expand(aut);
			}
		}
		if (!(nf instanceof True))
			oldf.add(nf);
		if (nf.isRightOfUntil())
			rightOfU.or(nf.getRofWU());
		return expand(aut);
	}

	private boolean contradiction(Formula f) {
		return fac.syntaxImplied(fac.makeNot(f), oldf, next);
	}

	private Node alreadyThere(List<Node> aut) {
		Iterator<Node> i = aut.iterator();
		while (i.hasNext()) {
			Node n = i.next();
			if (next.equals(n.next) && compareAccepting(n)) {
				return n;
			}
		}
		return null;
	}

	private boolean compareAccepting(Node n) {
		if (id == 0 && !collapsed)
			return true;
		else
			return accepting.equals(n.accepting);
	}

	static void printFormulaSet(LTSOutput out, String name,
			SortedSet<Formula> fs) {
		out.out(name + ":- ");
		Iterator<Formula> i = fs.iterator();
		while (i.hasNext()) {
			Formula f = i.next();
			out.out(f.toString() + ", ");
		}
	}

	static void printIdSet(LTSOutput out, String name, SortedSet<Node> fs) {
		out.out(name + ":- ");
		Iterator<Node> i = fs.iterator();
		while (i.hasNext()) {
			Node n = i.next();
			out.out(n.id + ", ");
		}
		out.outln(".");
	}

	void printNode(LTSOutput out) {
		out.outln("\nNODE " + id + " equivId " + equivId);
		printIdSet(out, "INCOMING", incoming);
		printFormulaSet(out, "NEW", newf);
		out.outln(".");
		printFormulaSet(out, "OLD", oldf);
		out.outln(".");
		printFormulaSet(out, "NEXT", next);
		out.outln(".");
		out.outln("ACCEPTING:- " + accepting);
		out.outln("RIGHTOFU:- " + rightOfU);
		if (otherSource != null) {
			out.outln("OTHERSOURCE " + otherSource.id + " ************** ");
			Node n = otherSource;
			while (n != null) {
				n.printNode(out);
				n = n.otherSource;
				if (n == this)
					break;
			}
		}
	}

	private Node otherSource = null;
	private static boolean collapsed = false;

	private void modify(Node n) {
		boolean found = false;
		Node n1 = this;
		Node n2 = this;
		if (id == 0 && !collapsed) {
			accepting = n.accepting;
			collapsed = true;
		}
		for (; n2 != null; n2 = n2.otherSource) {
			if (n2.oldf.equals(n.oldf)) {
				n2.incoming.addAll(n.incoming);
				found = true;
			}
			n1 = n2;
		}
		if (!found)
			n1.otherSource = n;
	}

	private boolean isSafetyAcc() {
		if (next.isEmpty())
			return true;
		Iterator<Formula> i = next.iterator();
		while (i.hasNext()) {
			Formula f = i.next();
			if (!(f instanceof Release))
				return false;
		}
		return true;
	}

	public void makeTransitions(State astate[]) {
		if (astate[id] == null)
			astate[id] = new State(equivId);
		else
			astate[id].setId(equivId);
		boolean safAcc = isSafetyAcc();
		for (Node node = this; node != null; node = node.otherSource) {
			int i;
			Iterator<Node> ii = node.incoming.iterator();
			while (ii.hasNext()) {
				Node node1 = ii.next();
				i = node1.id;
				if (astate[i] == null)
					astate[i] = new State();
				astate[i].add(new Transition(node.oldf, equivId, accepting,
						safAcc));
			}
		}
	}

}
