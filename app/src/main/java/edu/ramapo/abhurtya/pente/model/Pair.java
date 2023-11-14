package edu.ramapo.abhurtya.pente.model;

import java.io.Serializable;

public class Pair<K, V> implements Serializable {

    private K first;
    private V second;

    /**
     * Constructor for Pair Class.
     * @param first the first element of the pair
     * @param second the second element of the pair
     */
    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }


    /**
     * Overriding the equals method from the Object Class.
     * @param o the reference object with which to compare
     * @return boolean value, true if this object is the same as the obj argument; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (first != null ? !first.equals(pair.first) : pair.first != null) return false;
        return second != null ? second.equals(pair.second) : pair.second == null;
    }

    /**
     * Gets the first element of this pair.
     * @return the first element of the pair
     */
    public K getKey() {
        return first;
    }

    /**
     * Sets the first element of this pair.
     * @param first the first element to set
     * @return the current Pair object
     */
    public Pair<K, V> setKey(K first) {
        this.first = first;
        return this;
    }

    /**
     * Gets the second element of this pair.
     * @return the second element of the pair
     */
    public V getValue() {
        return second;
    }

    /**
     * Sets the second element of this pair.
     * @param second the second element to set
     * @return the current Pair object
     */
    public Pair<K, V> setValue(V second) {
        this.second = second;
        return this;
    }

    /**
     * A String representation of the Pair.
     * @return a string representation of the Pair
     */
    @Override
    public String toString() {
        return "Pair{" + "first=" + first + ", second=" + second + '}';
    }
}
