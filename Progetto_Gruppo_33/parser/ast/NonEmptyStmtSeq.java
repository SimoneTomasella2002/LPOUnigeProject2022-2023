package Progetto_Gruppo_33.parser.ast;

import Progetto_Gruppo_33.visitors.Visitor;

public class NonEmptyStmtSeq extends NonEmptySeq<Stmt, StmtSeq> implements StmtSeq {

	public NonEmptyStmtSeq(Stmt first, StmtSeq rest) {
		super(first, rest);
	}
	
	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitNonEmptyStmtSeq(first, rest);
	}
}
