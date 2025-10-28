package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest {

    @Test
    void testToString() {
        Vector v = new Vector();
        String result = v.toString();
        assertEquals("[1.0   1.0   1.0]", result);
    }

}