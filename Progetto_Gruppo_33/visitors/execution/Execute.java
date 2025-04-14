package Progetto_Gruppo_33.visitors.execution;

import java.io.PrintWriter;

import Progetto_Gruppo_33.environments.EnvironmentException;
import Progetto_Gruppo_33.environments.GenEnvironment;
import Progetto_Gruppo_33.parser.ast.Block;
import Progetto_Gruppo_33.parser.ast.Exp;
import Progetto_Gruppo_33.parser.ast.Stmt;
import Progetto_Gruppo_33.parser.ast.StmtSeq;
import Progetto_Gruppo_33.parser.ast.Variable;
import Progetto_Gruppo_33.visitors.Visitor;

import static java.util.Objects.requireNonNull;

public class Execute implements Visitor<Value> {

	private final GenEnvironment<Value> env = new GenEnvironment<>();
	private final PrintWriter printWriter; // output stream used to print values

	public Execute() {
		printWriter = new PrintWriter(System.out, true);
	}

	public Execute(PrintWriter printWriter) {
		this.printWriter = requireNonNull(printWriter);
	}

	// dynamic semantics for programs; no value returned by the visitor

	@Override
	public Value visitMyLangProg(StmtSeq stmtSeq) {
		try {
			stmtSeq.accept(this);
			// possible runtime errors
			// EnvironmentException: undefined variable
		} catch (EnvironmentException e) {
			throw new InterpreterException(e);
		}
		return null;
	}

	// dynamic semantics for statements; no value returned by the visitor

	@Override
	public Value visitAssignStmt(Variable var, Exp exp) {
		env.update(var, exp.accept(this));
		return null;
	}

	@Override
	public Value visitPrintStmt(Exp exp) {
		printWriter.println(exp.accept(this));
		return null;
	}

	@Override
	public Value visitVarStmt(Variable var, Exp exp) {
		env.dec(var, exp.accept(this));
		return null;
	}

	@Override
	public Value visitIfStmt(Exp exp, Block thenBlock, Block elseBlock) {
		if (exp.accept(this).toBool())
			thenBlock.accept(this);
		else if (elseBlock != null)
			elseBlock.accept(this);
		return null;
	}

	@Override
	public Value visitBlock(StmtSeq stmtSeq) {
		env.enterScope();
		stmtSeq.accept(this);
		env.exitScope();
		return null;
	}

	// dynamic semantics for for-each loop statement
	@Override
	public Value visitForEachStmt(Variable var, Exp exp, Block block) {
        // Get the value of the expression
		Value v = exp.accept(this);

        // Check if the value is a VectValue, otherwise throw an exception
		if (!(v instanceof VectValue VV)){
			throw InterpreterException.ExpectedDynamicType("Vect");
		}

        // Enter a new scope for the loop variable
		env.enterScope();
        
		// Initialize the loop variable to 0
		env.dec(var, new IntValue(0));

		// Loop through each element in the VectValue
		for (int it : VV) {
            
			// Update the loop variable with the current element value
			env.update(var, new IntValue(it));
			
            // Execute the statements in the block for each element in the vector			
			block.accept(this);
		}

		// Exit the loop variable scope
		env.exitScope();

		return null;
	}

	// dynamic semantics for sequences of statements
	// no value returned by the visitor

	@Override
	public Value visitEmptyStmtSeq() {
		return null;
	}

	@Override
	public Value visitNonEmptyStmtSeq(Stmt first, StmtSeq rest) {
		first.accept(this);
		rest.accept(this);
		return null;
	}

	// dynamic semantics of expressions; a value is returned by the visitor

	// Updated visitorAdd to support Vector
	@Override
	public Value visitAdd(Exp left, Exp right) {

        // Evaluate the left and right expressions
		Value leftValue = left.accept(this);
		Value rightValue = right.accept(this);

        // Check if both values are integers, perform integer addition if true		
		if (leftValue instanceof IntValue && rightValue instanceof IntValue){
			return new IntValue(leftValue.toInt() + rightValue.toInt());
		}
		// Check if both values are vectors, perform vector addition if true
		else if (leftValue instanceof VectValue LV && rightValue instanceof VectValue RV){
			return VectValue.sumVector(LV, RV);
		}

        // If left or right are not int or vect, throw an exception
		throw InterpreterException.ExpectedDynamicType("Vect or Int");
	}

	@Override
	public IntValue visitIntLiteral(int value) {
		return new IntValue(value);
	}

	// Updated visitorMul to support Vector
	@Override
	public Value visitMul(Exp left, Exp right) {

        // Evaluate the left and right expressions
		Value leftValue = left.accept(this);
		Value rightValue = right.accept(this);

		// Check if both values are integers, perform integer multiplication if true
		if (leftValue instanceof IntValue && rightValue instanceof IntValue){
			return new IntValue(leftValue.toInt() * rightValue.toInt());
		}
		// Check if both values are vectors, perform vector scalar product if true
		else if (leftValue instanceof VectValue LV && rightValue instanceof VectValue RV){
			return IntValue.scalarProd(LV, RV);
		}
		// Check if one value is an integer and the other is a vector, perform mixed product if true
		else if (leftValue instanceof IntValue IV && rightValue instanceof VectValue VV){
			return VectValue.mixProdVector(IV, VV);
		}
		else if (leftValue instanceof VectValue VV && rightValue instanceof IntValue IV){
			return VectValue.mixProdVector(IV, VV);
		}
		
        // If left or right are not int or vect, throw an exception
		throw InterpreterException.ExpectedDynamicType("Vect or Int");
	}

	@Override
	public IntValue visitSign(Exp exp) {
		return new IntValue(-exp.accept(this).toInt());
	}

	@Override
	public Value visitVariable(Variable var) {
		return env.lookup(var);
	}

	@Override
	public BoolValue visitNot(Exp exp) {
		return new BoolValue(!exp.accept(this).toBool());
	}

	@Override
	public BoolValue visitAnd(Exp left, Exp right) {
		return new BoolValue(left.accept(this).toBool() && right.accept(this).toBool());
	}

	@Override
	public BoolValue visitBoolLiteral(boolean value) {
		return new BoolValue(value);
	}

	@Override
	public BoolValue visitEq(Exp left, Exp right) {
		return new BoolValue(left.accept(this).equals(right.accept(this)));
	}

	@Override
	public PairValue visitPairLit(Exp left, Exp right) {
		return new PairValue(left.accept(this), right.accept(this));
	}

	@Override
	public Value visitFst(Exp exp) {
		return exp.accept(this).toPair().getFstVal();
	}

	@Override
	public Value visitSnd(Exp exp) {
		return exp.accept(this).toPair().getSndVal();
	}

	// Create a vector literal [firstExp, secondExp]
	@Override
	public VectValue visitVectorLiteral(Exp firstExp, Exp secondExp) {
		return new VectValue(firstExp.accept(this), secondExp.accept(this));
	}

}