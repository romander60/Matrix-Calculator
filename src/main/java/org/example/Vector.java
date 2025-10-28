package org.example;

import java.util.Arrays;

public class Vector {

    private final double[] elements;

    // Constructor
    public Vector() {
        this.elements = new double[]{1.0, 1.0, 1.0};
    }

    public Vector(double[] elements) {
        // Defensive copy to maintain immutability
        this.elements = Arrays.copyOf(elements, elements.length);
    }

    // Get size
    public int size() {
        return elements.length;
    }

    // Access element (read-only)
    public double get(int index) {
        return elements[index];
    }

    /**
     *
     * add(vec) returns the vector sum of this Vector object and vec.
     */
    public Vector add(Vector other) {
        if (this.size() != other.size()) {
            throw new IllegalArgumentException("Vectors must be the same size");
        }
        double[] result = new double[this.size()];
        for (int i = 0; i < this.size(); i++) {
            result[i] = this.elements[i] + other.elements[i];
        }
        return new Vector(result);
    }

    /**
     *
     * scale(c) multiplies every entry in this Vector object by c.
     */
    public Vector scale(double scalar) {
        double[] result = new double[this.size()];
        for (int i = 0; i < this.size(); i++) {
            result[i] = this.elements[i] * scalar;
        }
        return new Vector(result);
    }

    /**
     *
     * dot(vec) returns the dot product of this Vector object and vec.
     */
    public double dot(Vector other) {
        if (this.size() != other.size()) {
            throw new IllegalArgumentException("Vectors must be the same size");
        }
        double sum = 0;
        for (int i = 0; i < this.size(); i++) {
            sum += this.elements[i] * other.elements[i];
        }
        return sum;
    }

    /**
     * norm returns the magnitude of this vector object.
     */
    public double norm() {
        double sum = 0;
        for (double e : elements) {
            sum += e * e;
        }
        return Math.sqrt(sum);
    }

    /**
     *
     * ortho(vec) returns true if vec dotted with this Vector object is 0.
     */
    public boolean ortho(Vector other) {
        return dot(other) == 0;
    }

    /**
     *
     * Returns the string representation of this Vector object.
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("[");
        for (int i = 0; i < this.elements.length; i++) {
            string.append(this.elements[i]);

            if (i != this.elements.length - 1) {
                string.append("   ");
            }
        }
        string.append("]");
        return string.toString();
    }
}
