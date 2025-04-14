package Progetto_Gruppo_33.visitors.execution;

public class IntValue extends AtomicValue<Integer> {

	public IntValue(Integer value) {
		super(value);
	}

	// Factory Methods

	// Calculates the scalar product of two vectors and returns the result as an integer value.
	public static IntValue scalarProd(VectValue LV, VectValue RV){	
		// Checks if vector dimensions are the same
		LV.checkDimOf(RV);

		// Value to be returned (it will be the result of the scalar product)
		int val = 0;

		// Auxiliary value
		int RVPos = 0;
		
		// Calculates the scalar product by iterating through the elements of both vectors and saving results in val
		for(int leftValue : LV){
			int rightValue = RV.getValue(RVPos++);
			val += leftValue * rightValue;
		}
		
		// Returns a new IntValue object initiated with val
		return new IntValue(val);
	}

	@Override
	public int toInt() {
		return value;
	}

}