package keyvaluepair;

import java.io.Serializable;


public class Value<E> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    // I looked up java documents there's no good way to get generic type info except for passing in
    // the Type.class
    String myType = "blah blah...";
    E myValue;

    public Value (E v) {
        myValue = v;
    }

    public String getKeyType () {
        return myType;
    }

    public E getValue () {
        return myValue;
    }
}
