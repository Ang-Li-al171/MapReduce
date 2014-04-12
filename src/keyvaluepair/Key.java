package keyvaluepair;

import java.io.Serializable;


public class Key<E> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    E myKey;

    public Key (E k) {
        myKey = k;
    }

    public E getKey () {
        return myKey;
    }

}
