package Progetto_Gruppo_33.parser.ast;

import static java.util.Objects.requireNonNull;

import Progetto_Gruppo_33.visitors.Visitor;

// Implements 'foreach' IDENT 'in' Exp Block

public class ForEachStmt implements Stmt{
    private final Exp exp;      // Non null
    private final Variable var; // Non null
    private final Block block;  // Non null

    public ForEachStmt(Variable var, Exp exp, Block block){
        this.exp = requireNonNull(exp);
        this.var = requireNonNull(var);
        this.block = requireNonNull(block);
    }
    
    @Override
	public String toString() {  
		return getClass().getSimpleName() + '(' + var + exp + block +')';
	}

    @Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitForEachStmt(var, exp, block);
	}
}