package Progetto_Gruppo_33.visitors.execution;

import static java.util.Objects.requireNonNull;
import java.util.Arrays;

import java.util.Iterator;

public class VectValue implements Value, Iterable<Integer> {

    // Fields //

    // Where values are gonna be stored, used a built-in java type (int array)
    private int[] Vector;


    // Constructors //

    // Uses index and dimension values to create a vector with 1 set at the specified index
    public VectValue(Value ind, Value dim) {

        // Checks if arguments are valid (dim >= 0, ind >= 0 && ind < dim)
        checkArguments(ind.toInt(), dim.toInt());

        // Creates new int array of dimension dim (converted to int), assigns it to Vector...
        Vector = new int[dim.toInt()];

        // ... and sets 1 in position ind
        Vector[ind.toInt()] = 1;
    }

    // Creates an empty vector, used for Factory methods
    private VectValue(int dim) {

        // Checks if argument is valid (dim >= 0, ind will be 0 by default to not trigger any error)
        checkArguments(0, dim);

        // Creates new int array of dimension dim
        Vector = new int[dim];
    }


    // Checker methods //

    // Checks if arguments used in constructors are correct
    private void checkArguments(int ind, int dim) {
        try {
            // Checks if arguments are not null... 
            
            int intInd = requireNonNull(ind);
            int intDim = requireNonNull(dim);

            // ... and within valid range both for index and dimensions

            if (intDim < 0)
                throw new NegativeArraySizeException();
            if (intInd < 0 || intInd >= intDim)
                throw new ArrayIndexOutOfBoundsException();

        } catch (NegativeArraySizeException e) {
            // Handle the exception for negative array size
            
            String msg = "java.lang.NegativeArraySizeException: " + dim;
            throw new InterpreterException(msg, e);

        } catch (ArrayIndexOutOfBoundsException e) {
            // Handle the exception for array index out of bounds
            
            String msg = "java.lang.ArrayIndexOutOfBoundsException: Index " + ind
                    + " out of bounds for length " + dim;
            throw new InterpreterException(msg, e);
        }
    }

    // Checks if vector dimensions are the same for the given vector
    public void checkDimOf(Value V) {
        try {

            // Cheks if V is not null and an instance of VectValue

            requireNonNull(V);

            if (!(V instanceof VectValue VectV)) {
                throw new IllegalArgumentException();
            }
            

            // Checks if vector dimensions (this Vector and object V) are equal

            if (Vector.length != VectV.getLength()) {  
                throw new IllegalStateException();
            }

        } catch (IllegalArgumentException e) {
            // Handle the exception for expected dynamic type Vect
            
            throw InterpreterException.ExpectedDynamicType("Vect");
        
        } catch (IllegalStateException e) {
            // Handle the exception for vectors with different dimensions
            
            String msg = "Vectors must have the same dimension";
            throw new InterpreterException(msg);
        }
    }

    // Checks if index is coherent with current Vector dimension (index >= 0 && index < Vector.length)
    private void checkIndex(int index) {
        checkArguments(index, this.getLength());
    }


    // Factory methods //

    // Factory method to create a new vector by summing two vectors
    public static VectValue sumVector(VectValue LV, VectValue RV) {

        // Checks if LV and RV are not null       
        requireNonNull(LV);
        requireNonNull(RV);

        // Checks if LV and RV have same dimensions
        LV.checkDimOf(RV);

        // Creates a new empty vector of LV length
        VectValue vSum = new VectValue(LV.getLength());

        // Aux variable used to interact with values in vSum vector
        int vSumPos = 0;

        // Loop to set va        int pos = 0;lues in the new Vector and returns vSum
        for (int leftValue : LV) {
            int rightValue = RV.getValue(vSumPos);
            vSum.setValue(vSumPos++, leftValue + rightValue);
        }

        return vSum;
    }

    // Factory method to create a new vector by multiplying a vector with an integer value
    public static VectValue mixProdVector(IntValue IV, VectValue VV) {

        // Checks if LV and RV are not null
        requireNonNull(IV);
        requireNonNull(VV);

        // Creates a new vector by multiplying RV with the value of IV
        VectValue vMixProd = new VectValue(VV.getLength());

        // Aux variable used to interact with values in vMixProd vector
        int vMixProdPos = 0;

        // Loop to set values in the new Vector and returns vMixProd
        for (int value : VV) {
            vMixProd.setValue(vMixProdPos++, value * IV.toInt());
        }

        return vMixProd;
    }




    public static VectValue reverseVect(Value ValueVV){

        if (!(ValueVV instanceof VectValue VV)){
			throw InterpreterException.ExpectedDynamicType("Vect");
		}

        requireNonNull(VV);

        VectValue reverseVect = new VectValue(VV.getLength());

        int reverseVectPos = VV.getLength() - 1;

        for(int value : VV){
            reverseVect.setValue(reverseVectPos--, value);
        }

        return reverseVect;
    }

    // Getter methods //

    // Getter method to get the length of the vector
    public int getLength() {
        return Vector.length;
    }

    // Getter method to get the value at the specified index
    public int getValue(int ind) {
        // ... Code to check if the index is valid ...
        checkIndex(ind);

        // ... Code to return the value at the specified index ...
        return Vector[ind];
    }


    // Setter methods //

    // Setter method to set the value at the specified index
    public void setValue(int ind, int newVal) {
        // Check if the new value is not null and index is valid
        requireNonNull(newVal);

        checkIndex(ind);

        // Sets value at specified index
        Vector[ind] = newVal;
    }


    // Aux methods //

    // Returns vector field
    @Override
    public int[] toVect() {
        return Vector;
    }

    // Returns a string representation of the vector
    @Override
    public String toString() {
        // Constructs and returns the string representation of the vector
        
        String ret = "[";

        for (var i = 0; i < Vector.length; i++) {
            ret += Vector[i];
            if (i < Vector.length - 1)
                ret += ";";
        }

        ret += "]";

        return ret; // Returns [0;0;0;1;0] for example
    }

    // Calculates the hash code for the vector
    @Override
    public int hashCode() {
        return Arrays.hashCode(Vector);
    }

    // Checks if this vector is equal to another object
    @Override
    public final boolean equals(Object obj) {
        // Checking if this object and obj are the same
        if (this == obj)
            return true;

        // Checking if they have same dynamic type
        if (obj instanceof VectValue vv) {
            
            // Checking dimensions, if they're not equal, it returns false
            if (vv.getLength() != Vector.length)
                return false;
            // Checking elements in Vectors, if there are different elements, it returns false
            for (var i = 0; i < Vector.length; i++) {
                if (vv.getValue(i) != this.Vector[i])
                    return false;
            }
            return true;
        }

        // If this and obj have not same dynamic type, then it returns false
        return false;
    }

    
    // Iterator implementation to iterate over the elements of the vector
    @Override
    public Iterator<Integer> iterator() {
        return new VectIterator(this);
    }


}
