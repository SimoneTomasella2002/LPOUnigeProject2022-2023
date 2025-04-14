package Progetto_Gruppo_33.visitors.execution;

import java.util.Iterator;
import java.util.NoSuchElementException;
import static java.util.Objects.requireNonNull;

public class VectIterator implements Iterator<Integer>{
    
    // Fields //

    // Current position of the iterator in Vect
    int pos = 0;
    // Array to store vector elements
    int[] Vect;

    
    // Constructors //

    // Creates an iterator for the given VectValue
    public VectIterator(VectValue VV){
        
        // VV must be not null
        requireNonNull(VV);
        
        // Creates a new int array (called 'Vect') and copies elements from VV to Vect        
        Vect = new int[VV.getLength()];
        for(int i = 0; i < Vect.length; ++i){
            Vect[i] = VV.getValue(i);
        }
    }
   

    // Main Methods //
    
    // Checks if the current iterator can iterate, it returns true if pos is less than the length of Vect, otherwise it returns false
    @Override
    public boolean hasNext() {        
        return pos < Vect.length ? true : false;
    }

    // Get the next element from the vectors
    @Override
    public Integer next(){
        // If there are no more elements to iterate, throw a NoSuchElementException
        if(!hasNext()) throw new NoSuchElementException();
        
        // Get current element, and increments pos for next calls
        return Vect[pos++];
    }

}