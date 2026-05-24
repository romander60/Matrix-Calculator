package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MatrixTest {

    //-------------------------------------------------------------------------------------------------------
    // GENERATOR TESTS - COMPLETE

    @Test
    void MatrixGenTest() {
        // Test 1: Invalid Args
        Assertions.assertThrows(AssertionError.class, () -> {new Matrix(0);} );
        Assertions.assertThrows(AssertionError.class, () -> {new Matrix(-5);} );
        Assertions.assertThrows(InvalidMatrixException.class, () -> { new Matrix(new double[][] {
                {}
        } ); }  );
        Assertions.assertThrows(InvalidMatrixException.class, () -> { new Matrix(new double[][] {
                {2, 3, 4},
                {3, 5},
                {2},
                {4, 5, 2, 5, 6},
                {}
        } ); }  );
    }

    @Test
    void zeroMatrixTest() {
        // Test 1: Zero row vector
        Matrix test1 = Matrix.zeroMatrix(1, 4);
        Assertions.assertEquals(new Matrix(new double[][] {
                {0, 0, 0, 0}
        }), test1);

        // Test 2: Zero column vector
        Matrix test2 = Matrix.zeroMatrix(4, 1);
        Assertions.assertEquals(new Matrix(new double[][] {
                {0},
                {0},
                {0},
                {0}
        }), test2);

        // Test 3: Random Matrix
        int m = (int)(Math.random() * (7 - 1 + 1)) + 1;
        int n = (int)(Math.random() * (7 - 1 + 1)) + 1;
        Matrix test3 = Matrix.zeroMatrix(m, n);
        Assertions.assertEquals(new Matrix(new double[m][n]), test3);


        // Test 4: Invalid Matrices
        Assertions.assertThrows(InvalidMatrixException.class, () -> {Matrix.zeroMatrix(m, 0);});
        Assertions.assertThrows(InvalidMatrixException.class, () -> {Matrix.zeroMatrix(0, n);});
        Assertions.assertThrows(InvalidMatrixException.class, () -> {Matrix.zeroMatrix(0, 0);});
        Assertions.assertThrows(InvalidMatrixException.class, () -> {Matrix.zeroMatrix(m, -3);});
        Assertions.assertThrows(InvalidMatrixException.class, () -> {Matrix.zeroMatrix(-6, n);});

    }

    @Test
    void diagTest() {
        // Tests 1-3: Array Args
        Matrix t1 = Matrix.diag(new double[] {2, 3, 4, 5});
        Matrix t2 = Matrix.diag(new double[] {-3, 4, -72, 91, 13, -42});
        Matrix t3 = Matrix.diag(new double[] {0, 0, 0});
        Assertions.assertEquals(new Matrix(new double[][] {
                {2, 0, 0, 0},
                {0, 3, 0, 0},
                {0, 0, 4, 0},
                {0, 0, 0, 5}
        }), t1);
        Assertions.assertEquals(new Matrix(new double[][] {
                {-3, 0, 0, 0, 0, 0},
                {0, 4, 0, 0, 0, 0},
                {0, 0, -72, 0, 0, 0},
                {0, 0, 0, 91, 0, 0},
                {0, 0, 0, 0, 13, 0},
                {0, 0, 0, 0, 0, -42}
        }), t2);
        Assertions.assertEquals(Matrix.zeroMatrix(3, 3), t3);


        // Test 4: Random Identity
        int n = (int)(Math.random() * (7 - 1 + 1)) + 1;
        double[] diags = new double[n];
        for (int i = 0; i < diags.length; i++) {
            diags[i] += 1;
        }
        Matrix t4 = Matrix.diag(diags);
        Assertions.assertEquals(new Matrix(n), t4);


        // Test 5: Invalid Input
        Assertions.assertThrows(InvalidMatrixException.class, () -> {Matrix.diag(new double[] {});} );
    }

    //-------------------------------------------------------------------------------------------------------
    // GETTER TESTS

    @Test
    void colsTest() {
        int m = (int)(Math.random() * (7 - 1 + 1)) + 1;
        int n = (int)(Math.random() * (7 - 1 + 1)) + 1;
        // Test 1: Column Vector
        Matrix t1 = Matrix.zeroMatrix(m, 1);
        Assertions.assertEquals(1, Matrix.cols(t1));

        // Test 2: Row Vector
        Matrix t2 = Matrix.zeroMatrix(1, n);
        Assertions.assertEquals(n, Matrix.cols(t2));

        // Test 3: Random Matrix
        Matrix t3 = Matrix.zeroMatrix(m, n);
        Assertions.assertEquals(n, Matrix.cols(t3));
    }

    @Test
    void rowsTest() {
        int m = (int)(Math.random() * (7 - 1 + 1)) + 1;
        int n = (int)(Math.random() * (7 - 1 + 1)) + 1;
        // Test 1: Column Vector
        Matrix t1 = Matrix.zeroMatrix(m, 1);
        Assertions.assertEquals(m, Matrix.rows(t1));

        // Test 2: Row Vector
        Matrix t2 = Matrix.zeroMatrix(1, n);
        Assertions.assertEquals(1, Matrix.rows(t2));

        // Test 3: Random Matrix
        Matrix t3 = Matrix.zeroMatrix(m, n);
        Assertions.assertEquals(m, Matrix.rows(t3));

    }

    @Test // INCOMPLETE
    void getEntryTest() {
    }

    @Test // INCOMPLETE
    void getColTest() {
        // Test 1: Column Vector
        // Test 2: Row Vector
        // Tests 3-5: Non-vector
    }

    @Test // INCOMPLETE
    void getRowTest() {
    }

    @Test // INCOMPLETE
    void getSubmatrixTest() {

    }

    //-------------------------------------------------------------------------------------------------------
    // BOOLEAN FUNCTION TESTS

    @Test
    void isRowVecTest() {
    }

    @Test
    void isColVecTest() {
    }

    @Test
    void isVecTest() {
    }

    @Test
    void sameTypeTest() {
        // Test 1: Two Columns Vectors
        // Test 2: Two Row Vectors
        // Test 3: Two Vectors, Different Types
        // Test 4:
    }

    @Test
    void isNumberTest() {
        // Test 1: Number
        // Test 2: Non-Number
        // Test 3: Diagonal
    }

    @Test
    void isSquareTest() {
        // Test 1: Square
        // Test 2: Non-square
    }

    @Test
    void isTriangularTest() {
        // Test 1: Upper Triangular
        // Test 2: Lower Triangular
        // Test 3: Diagonal
        // Test 4: Square, Non-Triangular
        // Test 5: Non-square
    }

    @Test
    void isDiagonalTest() {
        // Test 1: Identity
    }

    @Test
    void isInvertibleTest() {
        // Test 1: Invertible
        // Test 2: Square, Non-invertible
        // Test 3: Non-square
    }

    @Test
    void isSymmetricTest() {
        // Test 1: Symmetric
        // Test 2: Square, Non-symmetric
        // Test 3: Non-square
    }

    @Test
    void isUnitTest() {
        // Test 1: Unit Column Vector
        // Test 2: Unit Row Vector
        // Test 3: Non-unit Column Vector
        // Test 4: Non-unit Row Vector
        // Test 5: Non-vector
    }

    @Test
    void areOrthoTest() {
        // Test 1: Orthogonal Column Vectors
        // Test 2: Orthogonal Row Vectors
        // Test 3: Non-orthogonal Column Vectors
        // Test 4: Non-orthogonal Row Vectors
        // Test 5: Non-vectors
    }

    @Test
    void isOrthoTest() {
        // Test 1: Orthogonal
        // Test 2: Square, Non-orthogonal
        // Test 3: Non-square
    }

    @Test
    void hasColTest() {
        // Test 1: Valid size, Contained
        // Test 2: Valid size, Not contained
        // Test 3: Invalid size
    }

    @Test
    void hasRowTest() {
        // Test 1: Valid size, Contained
        // Test 2: Valid size, Not contained
        // Test 3: Invalid size
    }

    //-------------------------------------------------------------------------------------------------------
    // MATRIX MANIPULATION TESTS

    @Test
    void appendTest() {
        // Test 1: Valid size (vectors, matrices)
        // Test 2: Invalid size
    }

    @Test
    void transposeTest() {
        // Test 1: Arbitrary Matrix
        // Test 2: Column Vector
        // Test 3: Row Vector
        // Test 4: Symmetric Matrix
    }

    @Test
    void replaceColTest() {
        // Test 1: Valid index, Valid column
        // Test 2: Valid index, Invalid column
        // Test 3: Invalid index
    }

    @Test
    void replaceRowTest() {
        // Test 1: Valid index, Valid row
        // Test 2: Valid index, Invalid row
        // Test 3: Invalid index
    }

    @Test
    void swapColsTest() {
        // Test 1: Valid indices
        // Test 2: Invalid indices
    }

    @Test
    void swapRowsTest() {
        // Test 1: Valid indices
        // Test 2: Invalid indices
    }

    @Test
    void removeRowTest() {

    }

    @Test
    void removeColTest() {

    }

    //-------------------------------------------------------------------------------------------------------
    // OPERATION TESTS

    @Test
    void copyTest() {
        // Test 1: Just copying a matrix, not much else to do here
    }

    @Test
    void magnTest() {
        // Test 1: Column vector
        // Test 2: Row vector
        // Test 3: Non-vector
    }

    @Test
    void dotTest() {
        // Test 1: Column vectors
        // Test 2: Row vectors
        // Test 3: Non-vectors

    }

    @Test
    void traceTest() {
        // Test 1: Square Matrices
        // Test 2: Non-square Matrices
    }

    @Test
    void scaleTest() {
        // Test 1: Matrices
    }

    @Test
    void addTest() {
        // Test 1: Valid sizes
        // Test 2: Invalid sizes
    }

    @Test
    void subTest() {

    }

    @Test
    void multTest() {
        // Test 1: Valid sizes
        // Test 2: Invalid sizes
    }

    @Test
    void powerTest() {
        // Test 1: Square matrix, positive power
        // Test 2: Square matrix, zeroth power
        // Test 3: Square matrix, invertible, negative power
        // Test 4: Square matrix, non-invertible, negative power
        // Test 5: Non-square
    }

    @Test
    void rowRedTest() {
        // Test 1: Nice matrix
        // Test 2: Midway interchanges
        // Test 3: Invertible matrix

    }

    @Test
    void inverseTest() {
        // Test 1: Square, Triangular
        // Test 2: Square, Non-triangular
        // Test 3: Non-square
    }

    @Test
    void detTest() {
        // Test 1: Square, Triangular
        // Test 2: Square, Non-Triangular
        // Test 3: Non-square
    }


    //-------------------------------------------------------------------------------------------------------
    // ORTHOGONALITY / LEAST SQUARES TESTS
    @Test
    void projTest() {

    }

    @Test
    void gsTest() {

    }

    @Test
    void wPerpTest() {

    }

    @Test
    void leastSquaresTest() {

    }

    //-------------------------------------------------------------------------------------------------------
    // MISC TESTS

    // not really sure how to test these tbh, they behave as i want them to so i don't think it's necessary
    @Test
    void toStringTest() {
        // Test 1: Random Matrix

    }

    @Test
    void equalsTest() {
        // Test 1: Random Matrix
        // Test 2: Diagonal Matrix
        // Test 3: Summed Matrix
        // Test 4: Product Matrix
        // Test 5: Power Matrix
        // Test 6: Row-reduced Matrix
        // Test 7: Row-swapped Matrix
        // Test 8: Column-swapped Matrix
    }

}