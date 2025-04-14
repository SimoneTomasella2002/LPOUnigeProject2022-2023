package Progetto_Gruppo_33.visitors.typechecking;

import static Progetto_Gruppo_33.visitors.typechecking.AtomicType.*;

import Progetto_Gruppo_33.environments.EnvironmentException;
import Progetto_Gruppo_33.environments.GenEnvironment;
import Progetto_Gruppo_33.parser.ast.Block;
import Progetto_Gruppo_33.parser.ast.Exp;
import Progetto_Gruppo_33.parser.ast.Stmt;
import Progetto_Gruppo_33.parser.ast.StmtSeq;
import Progetto_Gruppo_33.parser.ast.Variable;
import Progetto_Gruppo_33.visitors.Visitor;

public class Typecheck implements Visitor<Type> {

	private final GenEnvironment<Type> env = new GenEnvironment<>();

    // useful to typecheck binary operations where operands must have the same type 
	private void checkBinOp(Exp left, Exp right, Type type) {
		type.checkEqual(left.accept(this));
		type.checkEqual(right.accept(this));
	}

	// Checks if the given type is either INT or VECT.
	// If the type is valid, it returns the corresponding AtomicType.
	private AtomicType checkIntOrVect(Type elem){
		if(INT.equals(elem)) return INT;
		else if(VECT.equals(elem)) return VECT;

		// Throw an exception for unsupported types
		throw new TypecheckerException(elem.toString(), INT.toString() + " or " + VECT.toString());
	}

	// static semantics for programs; no value returned by the visitor

	@Override
	public Type visitMyLangProg(StmtSeq stmtSeq) {
		try {
			stmtSeq.accept(this);
		} catch (EnvironmentException e) { // undeclared variable
			throw new TypecheckerException(e);
		}
		return null;
	}

	// static semantics for statements; no value returned by the visitor

	@Override
	public Type visitAssignStmt(Variable var, Exp exp) {
		var found = env.lookup(var);
		found.checkEqual(exp.accept(this));
		return null;
	}

	@Override
	public Type visitPrintStmt(Exp exp) {
		exp.accept(this);
		return null;
	}

	@Override
	public Type visitVarStmt(Variable var, Exp exp) {
		env.dec(var, exp.accept(this));
		return null;
	}

	@Override
	public Type visitIfStmt(Exp exp, Block thenBlock, Block elseBlock) {
		BOOL.checkEqual(exp.accept(this));
		thenBlock.accept(this);
		if (elseBlock != null)
			elseBlock.accept(this);
		return null;
	}

	@Override
	public Type visitBlock(StmtSeq stmtSeq) {
		env.enterScope();
		stmtSeq.accept(this);
		env.exitScope();
		return null;
	}

	// Added new Foreach Visitor, typecheks a for-each loop statement
	@Override
	public Type visitForEachStmt(Variable var, Exp exp, Block block) {
		// exp must be a vector type
		VECT.checkEqual(exp.accept(this));
		
		// Creates a new scope to start the loop
		env.enterScope();
		
		// Declares the loop variable with type INT
		env.dec(var,INT);
		
		// Enters in block of statements inside the loop
		block.accept(this);
		
		// Exits the loop
		env.exitScope();

		// No value returnet by the visitor
		return null;
	}

	// static semantics for sequences of statements
	// no value returned by the visitor

	@Override
	public Type visitEmptyStmtSeq() {
		return null;
	}

	@Override
	public Type visitNonEmptyStmtSeq(Stmt first, StmtSeq rest) {
		first.accept(this);
		rest.accept(this);
		return null;
	}

	// static semantics of expressions; a type is returned by the visitor


	// Updated Add visitor to support vector
	@Override
	public AtomicType visitAdd(Exp left, Exp right){
		
		// Determines the type of the left expression
		AtomicType lType = checkIntOrVect(left.accept(this));
		
		// Ensures that the right expression has a compatible type (it must be equal to left)
		lType.checkEqual(checkIntOrVect(right.accept(this)));
		
		// Returns the resulting type of lType (INT = integer addition - VECT vector addition)
		return lType;
	}


	@Override
	public AtomicType visitIntLiteral(int value) {
		return INT;
	}


	// Updated Mul visitor to support vector
	@Override
	public Type visitMul(Exp left, Exp right) {
	
		// Checks and get the types of the left and right expressions
		AtomicType lType = checkIntOrVect(left.accept(this));
		AtomicType rType = checkIntOrVect(right.accept(this));

		// Determining what type should be returned
    	// If both operands are INT or VECT, the result is INT; otherwise, it's VECT
		return lType.equals(rType) ? INT : VECT;
	
	}

	@Override
	public AtomicType visitSign(Exp exp) {
		INT.checkEqual(exp.accept(this));
		return INT;
	}

	@Override
	public Type visitVariable(Variable var) {
		return env.lookup(var);
	}

	@Override
	public AtomicType visitNot(Exp exp) {
		BOOL.checkEqual(exp.accept(this));
		return BOOL;
	}

	@Override
	public AtomicType visitAnd(Exp left, Exp right) {
		checkBinOp(left, right, BOOL);
		return BOOL;
	}

	@Override
	public AtomicType visitBoolLiteral(boolean value) {
		return BOOL;
	}

	@Override
	public AtomicType visitEq(Exp left, Exp right) {
		left.accept(this).checkEqual(right.accept(this));
		return BOOL;
	}

	@Override
	public PairType visitPairLit(Exp left, Exp right) {
		return new PairType(left.accept(this), right.accept(this));
	}

	@Override
	public Type visitFst(Exp exp) {
		return exp.accept(this).getFstPairType();
	}

	@Override
	public Type visitSnd(Exp exp) {
		return exp.accept(this).getSndPairType();
	}
	
	// Added new vector Literal
	@Override
	public AtomicType visitVectorLiteral(Exp firstExp, Exp secondExp) {
		// Checks if firstExp and secondExp are INT
		checkBinOp(firstExp, secondExp, INT);
		
		// The vector literal expression has type VECT
		return VECT;
	}
}
