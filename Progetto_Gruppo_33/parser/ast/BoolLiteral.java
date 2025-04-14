package Progetto_Gruppo_33.parser.ast;

import Progetto_Gruppo_33.visitors.Visitor;

public class BoolLiteral extends AtomicLiteral<Boolean> {

	public BoolLiteral(boolean b) {
		super(b);
	}
	
	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitBoolLiteral(value);
	}
}
