package keyvaluepair;

import java.io.Serializable;


public class Value<E> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    E myValue;

    public Value (E v) {
        myValue = v;
    }

    public E getValue () {
        return myValue;
    }
}
