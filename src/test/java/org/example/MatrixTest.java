package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MatrixTest {

    //-------------------------------------------------------------------------------------------------------
    // GENERATOR TESTS - COMPLETE

    double tol = 0.0001;

    @Test
    void MatrixGenTest() {
        // Test 1: No Args
        Matrix m1 = new Matrix();;
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        }), m1);

        // Test 2: Invalid Args
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
    // GETTER TESTS - COMPLETE

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

    @Test
    void getEntryTest() {
        // Test 1: Square Matrix
        Matrix m1 = new Matrix(new double[][] {
                {1, 2, 3, 4, 5},
                {-3, 8, 39, 39, -15},
                {40, -4, 76, -9, 0},
                {6, 10, -6, 1, -1},
                {-57, 32, -45, 43, -7}
        } );
        Assertions.assertEquals(-9, Matrix.getEntry(m1, 3, 4));
        Assertions.assertEquals(-7, Matrix.getEntry(m1, 5, 5));
        Assertions.assertEquals(1, Matrix.getEntry(m1, 1, 1));
        Assertions.assertEquals(10, Matrix.getEntry(m1, 4, 2));
        Assertions.assertThrows(AssertionError.class, () -> {Matrix.getEntry(m1, -3, 5);} );
        Assertions.assertThrows(AssertionError.class, () -> {Matrix.getEntry(m1, 6, 4);} );

        // Test 2: Non-square Matrix
        Matrix m2 = new Matrix(new double[][] {
                {0, -2, 4},
                {-4, 5, 7},
                {3, -3, 30},
                {-41, 17, -59}
        } );

        Assertions.assertEquals(4, Matrix.getEntry(m2, 1, 3));
        Assertions.assertEquals(30, Matrix.getEntry(m2, 3, 3));
        Assertions.assertEquals(7, Matrix.getEntry(m2, 2, 3));
        Assertions.assertEquals(-4, Matrix.getEntry(m2, 2, 1));
        Assertions.assertThrows(AssertionError.class, () -> {Matrix.getEntry(m2, 2, -1);} );
        Assertions.assertThrows(AssertionError.class, () -> {Matrix.getEntry(m2, 3, 7);} );
    }

    @Test
    void getColTest() {
        // Test 1: Column Vector
        Matrix m1 = new Matrix(new double[][] {
                {1},
                {-3},
                {0},
                {-4}
        });
        Assertions.assertEquals(m1, Matrix.getCol(m1, 1));
        Assertions.assertThrows(AssertionError.class, () -> {Matrix.getCol(m1, 2);});
        Assertions.assertThrows(AssertionError.class, () -> {Matrix.getCol(m1, -3);});
        Assertions.assertThrows(AssertionError.class, () -> {Matrix.getCol(m1, 0);});

        // Test 2: Row Vector
        Matrix m2 = new Matrix(new double[][] {
                {4, -5, 2, -6}
        } );
        Assertions.assertEquals(new Matrix(new double[][] {
                {4}
        }), Matrix.getCol(m2, 1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-5}
        }), Matrix.getCol(m2, 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {2}
        }), Matrix.getCol(m2, 3));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-6}
        }), Matrix.getCol(m2, 4));
        Assertions.assertThrows(AssertionError.class, () -> {Matrix.getCol(m2, 5);});
        Assertions.assertThrows(AssertionError.class, () -> {Matrix.getCol(m2, -1);});

        // Tests 3: Non-vector
        Matrix m3 = new Matrix(new double[][] {
                {0, -2, 4},
                {-4, 5, 7},
                {3, -3, 30},
                {-41, 17, -59},
                {9, -1, 22}
        } );
        Assertions.assertEquals(new Matrix(new double[][] {
                {0},
                {-4},
                {3},
                {-41},
                {9}
        }), Matrix.getCol(m3, 1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-2},
                {5},
                {-3},
                {17},
                {-1}
        }), Matrix.getCol(m3, 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {4},
                {7},
                {30},
                {-59},
                {22}
        }), Matrix.getCol(m3, 3));
        Assertions.assertThrows(AssertionError.class, () -> {Matrix.getCol(m3, 4);} );
    }

    @Test
    void getRowTest() {
        // Test 1: Column Vector
        Matrix m1 = new Matrix(new double[][] {
                {1},
                {-3},
                {0},
                {-4}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {1}
        }), Matrix.getRow(m1, 1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-3}
        }), Matrix.getRow(m1, 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {0}
        }), Matrix.getRow(m1, 3));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-4}
        }), Matrix.getRow(m1, 4));
        Assertions.assertThrows(AssertionError.class, () -> {Matrix.getCol(m1, 5);});
        Assertions.assertThrows(AssertionError.class, () -> {Matrix.getCol(m1, -1);});
        Assertions.assertThrows(AssertionError.class, () -> {Matrix.getCol(m1, 0);});

        // Test 2: Row Vector
        Matrix m2 = new Matrix(new double[][] {
                {4, -5, 2, -6}
        } );
        Assertions.assertEquals(m2, Matrix.getRow(m2, 1));
        Assertions.assertThrows(AssertionError.class, () -> {Matrix.getRow(m2, 2);});
        Assertions.assertThrows(AssertionError.class, () -> {Matrix.getRow(m2, -1);});

        // Tests 3: Non-vector
        Matrix m3 = new Matrix(new double[][] {
                {0, -2, 4},
                {-4, 5, 7},
                {3, -3, 30},
                {-41, 17, -59},
                {9, -1, 22}
        } );
        Assertions.assertEquals(new Matrix(new double[][] {
                {0, -2, 4},
        }), Matrix.getRow(m3, 1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-4, 5, 7}
        }), Matrix.getRow(m3, 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {3, -3, 30}
        }), Matrix.getRow(m3, 3));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-41, 17, -59}
        }), Matrix.getRow(m3, 4));
        Assertions.assertEquals(new Matrix(new double[][] {
                {9, -1, 22}
        }), Matrix.getRow(m3, 5));
        Assertions.assertThrows(AssertionError.class, () -> {Matrix.getRow(m3, 6);} );
        Assertions.assertThrows(AssertionError.class, () -> {Matrix.getRow(m3, -2);} );
    }

    @Test
    void getColsTest() {
        // Test 1: Column Vector
        Matrix m1 = new Matrix(new double[][] {
                {1},
                {-3},
                {0},
                {-4}
        });
        Assertions.assertArrayEquals(new Matrix[] {new Matrix(new double[][] {
                {1},
                {-3},
                {0},
                {-4}
        }) }, Matrix.getCols(m1));


        // Test 2: Row Vector
        Matrix m2 = new Matrix(new double[][] {
                {4, -5, 2, -6}
        });
        Assertions.assertArrayEquals(new Matrix[] {new Matrix(new double[][] {
                {4}
        }), new Matrix(new double[][] {
                {-5}
        }), new Matrix(new double[][] {
                {2}
        }), new Matrix(new double[][] {
                {-6}
        }) } , Matrix.getCols(m2));

        // Tests 3: Non-vector
        Matrix m3 = new Matrix(new double[][] {
                {0, -2, 4},
                {-4, 5, 7},
                {3, -3, 30},
                {-41, 17, -59},
                {9, -1, 22}
        });
        Assertions.assertArrayEquals(new Matrix[] {new Matrix(new double[][] {
                {0}, {-4}, {3}, {-41}, {9}
        }),
        new Matrix(new double[][] {
                {-2}, {5}, {-3}, {17}, {-1}
        }),
        new Matrix(new double[][] {
                {4}, {7}, {30}, {-59}, {22}
        })}, Matrix.getCols(m3));
    }

    @Test
    void getRowsTest() {
        // Test 1: Column Vector
        Matrix m1 = new Matrix(new double[][]{
                {1},
                {-3},
                {0},
                {-4}
        });
        Assertions.assertArrayEquals(new Matrix[]{new Matrix(new double[][]{
                {1},
        }), new Matrix(new double[][]{
                {-3}
        }), new Matrix(new double[][]{
                {0}
        }), new Matrix(new double[][]{
                {-4}
        })}, Matrix.getRows(m1));


        // Test 2: Row Vector
        Matrix m2 = new Matrix(new double[][]{
                {4, -5, 2, -6}
        });
        Assertions.assertArrayEquals(new Matrix[]{
                new Matrix(new double[][]{{4, -5, 2, -6}})
        }, Matrix.getRows(m2));

        // Tests 3: Non-vector
        Matrix m3 = new Matrix(new double[][]{
                {0, -2, 4},
                {-4, 5, 7},
                {3, -3, 30},
                {-41, 17, -59},
                {9, -1, 22}
        });
        Assertions.assertArrayEquals(new Matrix[] {
                new Matrix(new double[][] {
                        {0, -2, 4}
                }), new Matrix(new double[][] {
                {-4, 5, 7}
        }), new Matrix(new double[][] {
                {3, -3, 30}
        }), new Matrix(new double[][] {
                {-41, 17, -59}
        }), new Matrix(new double[][] {
                {9, -1, 22}
        })
        }  , Matrix.getRows(m3));
    }

    @Test
    void getSubmatrixTest() {
        // Test 1: Column Vector
        Matrix m1 = new Matrix(new double[][] {
                {1},
                {-3},
                {0},
                {-4}
        });
        Assertions.assertEquals(m1, Matrix.getSubmatrix(m1, 1, 1, 4, 1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-3},
                {0},
                {-4}
        }), Matrix.getSubmatrix(m1, 2, 1, 4, 1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {1},
                {-3}
        }), Matrix.getSubmatrix(m1, 1, 1, 2, 1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {1}
        }), Matrix.getSubmatrix(m1, 1, 1, 1, 1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-4}
        }), Matrix.getSubmatrix(m1, 4, 1, 4, 1));

        Assertions.assertThrows(AssertionError.class, () ->
            {Matrix.getSubmatrix(m1, 1, -1, 1, 1);} );
        Assertions.assertThrows(AssertionError.class, () ->
            {Matrix.getSubmatrix(m1, 4, 1, 2, 1);});
        Assertions.assertThrows(AssertionError.class, () ->
            {Matrix.getSubmatrix(m1, 2, 1, 1, 1);});

        // Test 2: Row Vector
        Matrix m2 = new Matrix(new double[][] {
                {4, -5, 2, -6}
        });
        Assertions.assertEquals(m2, Matrix.getSubmatrix(m2, 1, 1, 1, 4));
        Assertions.assertEquals(new Matrix(new double[][] {
                {4, -5, 2}
        }), Matrix.getSubmatrix(m2, 1, 1, 1, 3));
        Assertions.assertEquals(new Matrix(new double[][] {
                {2, -6}
        }), Matrix.getSubmatrix(m2, 1, 3, 1, 4));

        Assertions.assertThrows(AssertionError.class, () ->
            {Matrix.getSubmatrix(m2, 1, 1, 3, 1);} );
        Assertions.assertThrows(AssertionError.class, () ->
            {Matrix.getSubmatrix(m2, 0, 1, 1, 4);} );
        Assertions.assertThrows(AssertionError.class, () ->
            {Matrix.getSubmatrix(m2, 1, 4, 1, 1);} );


        // Tests 3: Non-vector
        Matrix m3 = new Matrix(new double[][] {
                {0, -2, 4},
                {-4, 5, 7},
                {3, -3, 30},
                {-41, 17, -59},
                {9, -1, 22}
        });
        Assertions.assertEquals(m3, Matrix.getSubmatrix(m3, 1, 1, 5, 3));
        Assertions.assertEquals(new Matrix(new double[][] {
                {5, 7},
                {-3, 30},
                {17, -59}
        }), Matrix.getSubmatrix(m3, 2, 2, 4, 3));
        Assertions.assertEquals(new Matrix(new double[][] {
                {0, -2},
                {-4, 5},
                {3, -3}
        }), Matrix.getSubmatrix(m3, 1, 1, 3, 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-41, 17},
                {9, -1},
        }), Matrix.getSubmatrix(m3, 4, 1, 5, 2));
        Assertions.assertEquals(Matrix.getCol(m3, 1),
                Matrix.getSubmatrix(m3, 1, 1, 5, 1));
        Assertions.assertEquals(Matrix.getRow(m3, 3),
                Matrix.getSubmatrix(m3, 3, 1, 3, 3));

        Assertions.assertThrows(AssertionError.class, () ->
            {Matrix.getSubmatrix(m3, 4, 3, 2, 2);});
        Assertions.assertThrows(AssertionError.class, () ->
            {Matrix.getSubmatrix(m3, 6, 2, 5, 3);});
        Assertions.assertThrows(AssertionError.class, () ->
            {Matrix.getSubmatrix(m3, 2, 5, 5, 3);});
        Assertions.assertThrows(AssertionError.class, () ->
            {Matrix.getSubmatrix(m3, 2, 2, -2, 3);});
        Assertions.assertThrows(AssertionError.class, () ->
            {Matrix.getSubmatrix(m3, 2, 2, 5, -1);});
        Assertions.assertThrows(AssertionError.class, () ->
            {Matrix.getSubmatrix(m3, 2, 2, 5, 4);});
    }

    //-------------------------------------------------------------------------------------------------------
    // BOOLEAN FUNCTION TESTS

    @Test
    void isColVecTest() {
        // Test 1: Column Vector
        Matrix m1 = new Matrix(new double[][] {
                {-2},
                {4},
                {-3}
        });
        Assertions.assertTrue(Matrix.isColVec(m1));

        // Test 2: Row Vector
        Matrix m2 = new Matrix(new double[][] {
                {-2, 4, -3}
        });
        Assertions.assertFalse(Matrix.isColVec(m2));

        // Test 3: Non-vector
        Matrix m3 = new Matrix(new double[][] {
                {4, -2, 3},
                {1, 0, 0},
                {-9, 6, 7}
        });
        Assertions.assertFalse(Matrix.isColVec(m3));
        Assertions.assertTrue( Matrix.isColVec(Matrix.getCol(m3, 1)) );
        Assertions.assertTrue( Matrix.isColVec(Matrix.getCol(m3, 2)) );
        Assertions.assertTrue( Matrix.isColVec(Matrix.getCol(m3, 3)) );
    }

    @Test
    void isRowVecTest() {
        // Test 1: Column Vector
        Matrix m1 = new Matrix(new double[][] {
                {-2},
                {4},
                {-3}
        });
        Assertions.assertFalse(Matrix.isRowVec(m1));

        // Test 2: Row Vector
        Matrix m2 = new Matrix(new double[][] {
                {-2, 4, -3}
        });
        Assertions.assertTrue(Matrix.isRowVec(m2));

        // Test 3: Non-vector
        Matrix m3 = new Matrix(new double[][] {
                {4, -2, 3},
                {1, 0, 0},
                {-9, 6, 7}
        });
        Assertions.assertFalse(Matrix.isRowVec(m3));
        Assertions.assertTrue( Matrix.isRowVec(Matrix.getRow(m3, 1)) );
        Assertions.assertTrue( Matrix.isRowVec(Matrix.getRow(m3, 2)) );
        Assertions.assertTrue( Matrix.isRowVec(Matrix.getRow(m3, 3)) );
    }

    @Test
    void isVecTest() {
        // Test 1: Column Vector
        Matrix m1 = new Matrix(new double[][] {
                {-2},
                {4},
                {-3}
        });
        Assertions.assertTrue(Matrix.isVec(m1));

        // Test 2: Row Vector
        Matrix m2 = new Matrix(new double[][] {
                {-2, 4, -3}
        });
        Assertions.assertTrue(Matrix.isVec(m2));

        // Test 3: Non-vector
        Matrix m3 = new Matrix(new double[][] {
                {4, -2, 3},
                {1, 0, 0},
                {-9, 6, 7}
        });
        Assertions.assertFalse(Matrix.isVec(m3));
    }

    @Test
    void sameTypeTest() {
        // Test 1: Two Columns Vectors
        Matrix v1 = new Matrix(new double[][] {
                {1},
                {-3},
                {-4}
        });
        Matrix v2 = new Matrix(new double[][] {
                {4},
                {-5},
                {6},
                {0},
                {9}
        });
        Matrix v3 = new Matrix(new double[][] {
                {7}
        });
        Assertions.assertTrue(Matrix.sameType(v1, v2));
        Assertions.assertTrue(Matrix.sameType(v2, v3));
        Assertions.assertTrue(Matrix.sameType(v1, v3));

        // Test 2: Two Row Vectors
        Matrix v4 = new Matrix(new double[][] {
                {1, -3, -4}
        });
        Matrix v5 = new Matrix(new double[][] {
                {4, -5, 6, 0, 9}
        });
        Assertions.assertTrue(Matrix.sameType(v4, v5));
        Assertions.assertTrue(Matrix.sameType(v4, v3));
        Assertions.assertTrue(Matrix.sameType(v5, v3));

        // Test 3: Two Vectors, Different Types
        Assertions.assertFalse(Matrix.sameType(v1, v4));
        Assertions.assertFalse(Matrix.sameType(v2, v4));
        Assertions.assertFalse(Matrix.sameType(v1, v5));
        Assertions.assertFalse(Matrix.sameType(v2, v5));

        // Test 4: Non-vectors
        Matrix v6 = new Matrix();
        Matrix v7 = Matrix.zeroMatrix(4, 2);
        Assertions.assertFalse(Matrix.sameType(v6, v1));
        Assertions.assertFalse(Matrix.sameType(v6, v2));
        Assertions.assertFalse(Matrix.sameType(v6, v3));
        Assertions.assertFalse(Matrix.sameType(v6, v4));
        Assertions.assertFalse(Matrix.sameType(v6, v5));
        Assertions.assertFalse(Matrix.sameType(v7, v1));
        Assertions.assertFalse(Matrix.sameType(v7, v2));
        Assertions.assertFalse(Matrix.sameType(v7, v3));
        Assertions.assertFalse(Matrix.sameType(v7, v4));
        Assertions.assertFalse(Matrix.sameType(v7, v5));



    }

    @Test
    void isSquareTest() {
        // Test 1: Squares
        Matrix m1 = new Matrix();
        Matrix m2 = new Matrix(5);
        Matrix m3 = new Matrix(new double[][] {
                {-2, 3},
                {7, -5}
        });
        Matrix m4 = Matrix.zeroMatrix(1, 1);
        Matrix m5 = Matrix.diag(new double[] {4, 5, 2, 7, 9});
        Assertions.assertTrue(Matrix.isSquare(m1));
        Assertions.assertTrue(Matrix.isSquare(m2));
        Assertions.assertTrue(Matrix.isSquare(m3));
        Assertions.assertTrue(Matrix.isSquare(m4));
        Assertions.assertTrue(Matrix.isSquare(m5));

        // Test 2: Non-squares
        Matrix m6 = new Matrix(new double[][] {
                {1, 2},
                {5, 7},
                {-2, 0},
                {9, -8}
        });
        Matrix m7 = new Matrix(new double[][] {
                {3, 3, -5},
                {0, -7, 8}
        });
        Assertions.assertFalse(Matrix.isSquare(m6));
        Assertions.assertFalse(Matrix.isSquare(m7));
        Assertions.assertFalse( Matrix.isSquare(Matrix.getCol(m6, 1)) );
        Assertions.assertFalse( Matrix.isSquare(Matrix.getRow(m7, 2)) );
    }

    @Test // INCOMPLETE
    void isUpperTriangularTest() {
        // Test 1: Upper Triangular
        // Test 2: Lower Triangular
        // Test 3: Diagonal
        // Test 4: Square, Non-Triangular
        // Test 5: Non-square
    }

    @Test // INCOMPLETE
    void isLowerTriangularTest() {
        // Test 1: Upper Triangular
        // Test 2: Lower Triangular
        // Test 3: Diagonal
        // Test 4: Square, Non-Triangular
        // Test 5: Non-square
    }

    @Test // INCOMPLETE
    void isTriangularTest() {
        // Test 1: Upper Triangular
        // Test 2: Lower Triangular
        // Test 3: Diagonal
        // Test 4: Square, Non-Triangular
        // Test 5: Non-square
    }

    @Test // INCOMPLETE
    void isDiagonalTest() {
        // Test 1: Identity
    }

    @Test // INCOMPLETE
    void isInvertibleTest() {
        // Test 1: Invertible
        // Test 2: Square, Non-invertible
        // Test 3: Non-square
    }

    @Test // INCOMPLETE
    void isSymmetricTest() {
        // Test 1: Symmetric
        // Test 2: Square, Non-symmetric
        // Test 3: Non-square
    }

    @Test // INCOMPLETE
    void isUnitTest() {
        // Test 1: Unit Column Vector
        // Test 2: Unit Row Vector
        // Test 3: Non-unit Column Vector
        // Test 4: Non-unit Row Vector
        // Test 5: Non-vector
    }

    @Test // INCOMPLETE
    void areOrthoTest() {
        // Test 1: Orthogonal Column Vectors
        // Test 2: Orthogonal Row Vectors
        // Test 3: Non-orthogonal Column Vectors
        // Test 4: Non-orthogonal Row Vectors
        // Test 5: Non-vectors
    }

    @Test // INCOMPLETE
    void isOrthoTest() {
        // Test 1 (matrix version): Orthogonal
        // Test 2 (matrix version): Square, Non-orthogonal
        // Test 3 (matrix version): Non-square
        // Test 4: Just one vector
    }

    @Test // INCOMPLETE
    void hasColTest() {
        // Test 1: Valid size, Contained
        // Test 2: Valid size, Not contained
        // Test 3: Invalid size
    }

    @Test // INCOMPLETE
    void hasRowTest() {
        // Test 1: Valid size, Contained
        // Test 2: Valid size, Not contained
        // Test 3: Invalid size
    }

    @Test // INCOMPLETE
    void sameSizeTest() {
        // Test 1: Column vectors, same size
        // Test 2: Column vectors, different sizes
        // Test 3: Row vectors, same size
        // Test 4: Row vectors, different sizes
        // Test 5: Arbitrary matrices, some same size, some different
        // Test 6: Set of column vectors, all same size
        // Test 7: Set of column vectors, some different size
        // Test 8: Set of row vectors, all same size
        // Test 9: Set of row vectors, all different size
        // Test 10: Set of arbitrary matrices some same size, some different
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
    void orthoCompTest() {

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