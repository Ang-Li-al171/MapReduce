package keyvaluepair;

import java.io.Serializable;


public class KeyValuePair<K, V> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public KeyValuePair (K k, V v) {
        this.k = new Key<K>(k);
        this.v = new Value<V>(v);
    }

    public Key<K> k;
    public Value<V> v;

    public K getKey () {
        return k.getKey();
    }

    public V getValue () {
        return v.getValue();
    }
}
