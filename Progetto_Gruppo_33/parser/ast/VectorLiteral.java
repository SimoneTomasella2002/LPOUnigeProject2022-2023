package Progetto_Gruppo_33.parser.ast;

import Progetto_Gruppo_33.visitors.Visitor;

// Implements VectorLiteral [Exp;Exp]

public class VectorLiteral extends BinaryOp {

    public VectorLiteral(Exp left, Exp right){
        super(left, right);
    }

    @Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitVectorLiteral(left, right);
	}
}
