package Progetto_Gruppo_33.parser.ast;

import Progetto_Gruppo_33.visitors.Visitor;

public class IntLiteral extends AtomicLiteral<Integer> {

	public IntLiteral(int n) {
		super(n);
	}
	
	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitIntLiteral(value);
	}
}
