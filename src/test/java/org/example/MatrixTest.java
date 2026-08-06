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
        Matrix m1 = new Matrix();
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
    void getEntriesTest() {
        // The getEntries function is basically the inverse of the matrix generator functions.

        // Test 1: 1x1 Matrix
        double[][] entries1 = new double[][] {
                {2}
        };
        Matrix t1 = new Matrix(entries1);
        Assertions.assertArrayEquals(Matrix.getEntries(t1), entries1);

        // Test 2: Row Vector
        double[][] entries2 = new double[][] {
                {1, 4, 5, 2, 7}
        };
        Matrix t2 = new Matrix(entries2);
        Assertions.assertArrayEquals(Matrix.getEntries(t2), entries2);

        // Test 3: Column Vector
        double[][] entries3 = new double[][] {
                {3},
                {-2},
                {5},
                {0},
                {-6}
        };
        Matrix t3 = new Matrix(entries3);
        Assertions.assertArrayEquals(Matrix.getEntries(t3), entries3);

        // Test 4: Just another matrix
        double[][] entries4 = new double[][] {
                {3, 5, -2, 5},
                {1, 2, 9, 0},
                {-3, -3, 7, 6}
        };
        Matrix t4 = new Matrix(entries4);
        Assertions.assertArrayEquals(Matrix.getEntries(t4), entries4);
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
    // BOOLEAN FUNCTION TESTS - COMPLETE

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
    void hasColTest() {
        // Test 1: Valid size, Contained
        Matrix t1 = new Matrix(new double[][] {
                {2, 3, 1},
                {2, 4, 5},
                {2, 1, 6}
        });
        Assertions.assertTrue(Matrix.hasCol(t1, new Matrix(new double[][] {
                {2},
                {2},
                {2}
        })));
        Assertions.assertTrue(Matrix.hasCol(t1, new Matrix(new double[][] {
                {3},
                {4},
                {1}
        })));
        Assertions.assertTrue(Matrix.hasCol(t1, new Matrix(new double[][] {
                {1},
                {5},
                {6}
        })));


        Matrix t2 = new Matrix(new double[][] {
                {7},
                {-4}
        });
        Assertions.assertTrue(Matrix.hasCol(t2, new Matrix(new double[][] {
                {7},
                {-4},
        })));

        Matrix t3 = new Matrix(4);
        Assertions.assertTrue(Matrix.hasCol(t3, new Matrix(new double[][] {
                {1},
                {0},
                {0},
                {0}
        })));
        Assertions.assertTrue(Matrix.hasCol(t3, new Matrix(new double[][] {
                {0},
                {1},
                {0},
                {0}
        })));
        Assertions.assertTrue(Matrix.hasCol(t3, new Matrix(new double[][] {
                {0},
                {0},
                {1},
                {0}
        })));
        Assertions.assertTrue(Matrix.hasCol(t3, new Matrix(new double[][] {
                {0},
                {0},
                {0},
                {1}
        })));

        // Test 2: Valid size, Not contained
        Assertions.assertFalse(Matrix.hasCol(t1, new Matrix(new double[][] {
                {2},
                {2},
                {3}
        })));
        Assertions.assertFalse(Matrix.hasCol(t1, new Matrix(new double[][] {
                {3},
                {4},
                {-1}
        })));
        Assertions.assertFalse(Matrix.hasCol(t2, new Matrix(new double[][] {
                {-7},
                {6}
        })));
        Assertions.assertFalse(Matrix.hasCol(t2, new Matrix(new double[][] {
                {2},
                {-4}
        })));
        Assertions.assertFalse(Matrix.hasCol(t3, new Matrix(new double[][] {
                {1},
                {0},
                {0},
                {1}
        })));
        Assertions.assertFalse(Matrix.hasCol(t2, new Matrix(new double[][] {
                {0},
                {-1},
                {0},
                {0}
        })));

        // Test 3: Invalid size
        Assertions.assertFalse(Matrix.hasCol(t1, new Matrix(new double[][] {
                {1, 3, 7}
        })));
        Assertions.assertFalse(Matrix.hasCol(t1, new Matrix(new double[][] {
                {3},
                {4},
                {1},
                {0}
        })));
        Assertions.assertFalse(Matrix.hasCol(t2, new Matrix(new double[][] {
                {7},
                {-4},
                {1}
        })));
        Assertions.assertFalse(Matrix.hasCol(t2, new Matrix(new double[][] {
                {5, 6}
        })));
        Assertions.assertFalse(Matrix.hasCol(t3, new Matrix(new double[][] {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        })));
        Assertions.assertFalse(Matrix.hasCol(t3, new Matrix(new double[][] {
                {1},
                {0}
        })));
    }

    @Test
    void hasRowTest() {
        // Test 1: Valid size, Contained
        Matrix t1 = new Matrix(new double[][] {
                {2, 3, 1},
                {2, 4, 5},
                {2, 1, 6}
        });
        Assertions.assertTrue(Matrix.hasRow(t1, new Matrix(new double[][] {
                {2, 3, 1}
        })));
        Assertions.assertTrue(Matrix.hasRow(t1, new Matrix(new double[][] {
                {2, 4, 5}
        })));
        Assertions.assertTrue(Matrix.hasRow(t1, new Matrix(new double[][] {
                {2, 1, 6}
        })));

        Matrix t2 = new Matrix(new double[][] {
                {7},
                {-4}
        });
        Assertions.assertTrue(Matrix.hasRow(t2, new Matrix(new double[][] {
                {7}
        })));
        Assertions.assertTrue(Matrix.hasRow(t2, new Matrix(new double[][] {
                {-4}
        })));

        Matrix t3 = new Matrix(4);
        Assertions.assertTrue(Matrix.hasRow(t3, new Matrix(new double[][] {
                {1, 0, 0, 0}
        })));
        Assertions.assertTrue(Matrix.hasRow(t3, new Matrix(new double[][] {
                {0, 1, 0, 0}
        })));
        Assertions.assertTrue(Matrix.hasRow(t3, new Matrix(new double[][] {
                {0, 0, 1, 0}
        })));
        Assertions.assertTrue(Matrix.hasRow(t3, new Matrix(new double[][] {
                {0, 0, 0, 1}
        })));

        // Test 2: Valid size, Not contained
        Assertions.assertFalse(Matrix.hasRow(t1, new Matrix(new double[][] {
                {2, 3, 2}
        })));
        Assertions.assertFalse(Matrix.hasRow(t1, new Matrix(new double[][] {
                {2, 4, -5}
        })));
        Assertions.assertFalse(Matrix.hasRow(t2, new Matrix(new double[][] {
                {-7}
        })));
        Assertions.assertFalse(Matrix.hasRow(t2, new Matrix(new double[][] {
                {10}
        })));
        Assertions.assertFalse(Matrix.hasRow(t3, new Matrix(new double[][] {
                {1, 0, 4, 0}
        })));
        Assertions.assertFalse(Matrix.hasRow(t3, new Matrix(new double[][] {
                {0, 6, 0, 0}
        })));

        // Test 3: Invalid size
        Assertions.assertFalse(Matrix.hasRow(t1, new Matrix(new double[][] {
                {1},
                {3},
                {7}
        })));
        Assertions.assertFalse(Matrix.hasRow(t1, new Matrix(new double[][] {
                {3, 4, 1, 0}
        })));
        Assertions.assertFalse(Matrix.hasRow(t2, new Matrix(new double[][] {
                {7, -4, 1}
        })));
        Assertions.assertFalse(Matrix.hasRow(t2, new Matrix(new double[][] {
                {5},
                {6}
        })));
        Assertions.assertFalse(Matrix.hasRow(t3, new Matrix(new double[][] {
                {1, 0, 0},
                {0, 0, 1}
        })));
        Assertions.assertFalse(Matrix.hasRow(t3, new Matrix(new double[][] {
                {1, 3}
        })));
    }

    @Test
    void sameSizeTest() {
        // Test 1 (Pair): Column vectors, same size
        Matrix t1 = new Matrix(new double[][] {
                {1},
                {0}
        });
        Matrix t2 = new Matrix(new double[][] {
                {-9},
                {6}
        });
        Assertions.assertTrue(Matrix.sameSize(t1, t2));

        // Test 2 (Pair): Column vectors, different sizes
        Matrix t3 = new Matrix(new double[][] {
                {3},
                {-6},
                {-2}
        });
        Assertions.assertFalse(Matrix.sameSize(t3, t1));

        // Test 3 (Pair): Row vectors, same size
        Matrix t4 = new Matrix(new double[][] {
                {1, 0}
        });
        Matrix t5 = new Matrix(new double[][] {
                {-9, 6}
        });
        Assertions.assertTrue(Matrix.sameSize(t4, t5));

        // Test 4 (Pair): Row vectors, different sizes
        Matrix t6 = new Matrix(new double[][] {
                {3, -6, 2}
        });
        Assertions.assertFalse(Matrix.sameSize(t6, t4));

        // Test 5 (Pair): Arbitrary matrices, some same size, some different
        Matrix t7 = new Matrix(new double[][] {
                {2, 3},
                {1, 4}
        });
        Matrix t8 = new Matrix(new double[][] {
                {5, 6},
                {7, -8}
        });
        Matrix t9 = new Matrix(new double[][] {
                {-4, 8},
                {-9, 0},
                {1, 1},
                {4, 3}
        });
        Matrix t10 = new Matrix(new double[][] {
                {-8, 1},
                {3, 2},
                {3, 3},
                {0, -4}
        });
        Matrix t11 = new Matrix(new double[][] {
                {6, -7, 1},
                {-6, 1, 3}
        });
        Assertions.assertTrue(Matrix.sameSize(t7, t8));
        Assertions.assertTrue(Matrix.sameSize(t9, t10));
        Assertions.assertFalse(Matrix.sameSize(t7, t9));
        Assertions.assertFalse(Matrix.sameSize(t11, t8));
        Assertions.assertFalse(Matrix.sameSize(t11, t10));

        // Test 6 (Array): Empty set
        Matrix[] t12 = new Matrix[0];
        Assertions.assertFalse(Matrix.sameSize(t12));

        // Test 7 (Array): Sets of column vectors, same size elements
        Matrix[] t13 = new Matrix[4];
        t13[0] = t1;
        t13[1] = t2;
        t13[2] = new Matrix(new double[][] {
                {3},
                {-1}
        });
        t13[3] = new Matrix(new double[][] {
                {14},
                {-4}
        });
        Assertions.assertTrue(Matrix.sameSize(t13));

        Matrix[] t14 = new Matrix[4];
        t14[0] = t3;
        t14[1] = new Matrix(new double[][] {
                {6},
                {1},
                {4}
        });
        t14[2] = new Matrix(new double[][] {
                {-1},
                {5},
                {3}
        });
        t14[3] = new Matrix(new double[][] {
                {8},
                {-3},
                {9}
        });
        Assertions.assertTrue(Matrix.sameSize(t14));

        // Test 8 (Array): Sets of column vectors, different size elements
        Matrix[] t15 = new Matrix[4];
        t15[0] = t13[0];
        t15[1] = t13[1];
        t15[2] = t13[2];
        t15[3] = t14[0];
        Assertions.assertFalse(Matrix.sameSize(t15));

        Matrix[] t16 = new Matrix[4];
        t16[0] = t13[2];
        t16[1] = t14[1];
        t16[2] = t14[2];
        t16[3] = t14[3];
        Assertions.assertFalse(Matrix.sameSize(t16));

        // Test 9 (Array): Sets of row vectors, same size elements
        Matrix[] t17 = new Matrix[4];
        t17[0] = t4;
        t17[1] = t5;
        t17[2] = new Matrix(new double[][] {
                {13, -42}
        });
        t17[3] = new Matrix(new double[][] {
                {4, 64}
        });
        Assertions.assertTrue(Matrix.sameSize(t17));

        Matrix[] t18 = new Matrix[4];
        t18[0] = t6;
        t18[1] = new Matrix(new double[][] {
                {5, -4, 2}
        });
        t18[2] = new Matrix(new double[][] {
                {0, 0, -6}
        });
        t18[3] = new Matrix(new double[][] {
                {7, -5, -19}
        });
        Assertions.assertTrue(Matrix.sameSize(t18));

        // Test 10 (Array): Sets of row vectors, different size elements
        Matrix[] t19 = new Matrix[4];
        t19[0] = t17[0];
        t19[1] = t18[0];
        t19[2] = t18[1];
        t19[3] = t18[2];
        Assertions.assertFalse(Matrix.sameSize(t19));

        Matrix[] t20 = new Matrix[4];
        t20[0] = t18[3];
        t20[1] = t17[1];
        t20[2] = t17[2];
        t20[3] = t17[3];
        Assertions.assertFalse(Matrix.sameSize(t20));

        // Test 11 (Array): Sets of arbitrary matrices, some same size, some different
        Matrix[] t21 = new Matrix[4];
        t21[0] = t7;
        t21[1] = t8;
        t21[2] = new Matrix(new double[][] {
                {5, -6},
                {9, 0}
        });
        t21[3] = new Matrix(new double[][] {
                {2, -5},
                {8, -7}
        });
        Assertions.assertTrue(Matrix.sameSize(t21));

        Matrix[] t22 = new Matrix[4];
        t22[0] = t9;
        t22[1] = t10;
        t22[2] = new Matrix(new double[][] {
                {-16, 5},
                {23, 56},
                {9, -4},
                {0, 33}
        });
        t22[3] = new Matrix(new double[][] {
                {3, 5},
                {3, -5},
                {-50, -2},
                {7, -6}
        });
        Assertions.assertTrue(Matrix.sameSize(t22));

        Matrix[] t23 = new Matrix[4];
        t23[0] = t21[0];
        t23[1] = t21[1];
        t23[2] = t22[0];
        t23[3] = t22[1];
        Assertions.assertFalse(Matrix.sameSize(t23));
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

    @Test
    void isUpperTriangularTest() {
        // Test 1: Upper Triangular
        Matrix t1 = new Matrix(new double[][] {
                {3, 2, 3},
                {0, -7, -1},
                {0, 0, 6}
        });
        Assertions.assertTrue(Matrix.isUpperTriangular(t1));

        // Test 2: Lower Triangular
        Matrix t2 = new Matrix(new double[][] {
                {3, 0, 0},
                {2, -7, 0},
                {3, -1, 6}
        });
        Assertions.assertFalse(Matrix.isUpperTriangular(t2));

        // Test 3: Diagonal
        Matrix t3 = Matrix.diag(new double[] { 3, 4, 5, 6 });
        Assertions.assertTrue(Matrix.isUpperTriangular(t3));

        // Test 4: Square, Non-Triangular
        Matrix t4 = new Matrix(new double[][] {
                {3, 2, 3},
                {0, -7, -1},
                {4, 0, 6}
        });
        Assertions.assertFalse(Matrix.isUpperTriangular(t4));

        // Test 5: Non-square
        Matrix t5 = new Matrix(new double[][] {
                {3, 2, 3},
                {0, -7, -1}
        });
        Assertions.assertFalse(Matrix.isUpperTriangular(t5));
    }

    @Test
    void isLowerTriangularTest() {
        // Test 1: Upper Triangular
        Matrix t1 = new Matrix(new double[][] {
                {3, 2, 3},
                {0, -7, -1},
                {0, 0, 6}
        });
        Assertions.assertFalse(Matrix.isLowerTriangular(t1));

        // Test 2: Lower Triangular
        Matrix t2 = new Matrix(new double[][] {
                {3, 0, 0},
                {2, -7, 0},
                {3, -1, 6}
        });
        Assertions.assertTrue(Matrix.isLowerTriangular(t2));

        // Test 3: Diagonal
        Matrix t3 = Matrix.diag(new double[] { 3, 4, 5, 6 });
        Assertions.assertTrue(Matrix.isLowerTriangular(t3));

        // Test 4: Square, Non-Triangular
        Matrix t4 = new Matrix(new double[][] {
                {3, 2, 3},
                {0, -7, -1},
                {4, 0, 6}
        });
        Assertions.assertFalse(Matrix.isLowerTriangular(t4));

        // Test 5: Non-square
        Matrix t5 = new Matrix(new double[][] {
                {3, 2, 3},
                {0, -7, -1}
        });
        Assertions.assertFalse(Matrix.isLowerTriangular(t5));
    }

    @Test
    void isTriangularTest() {
        // Test 1: Upper Triangular
        Matrix t1 = new Matrix(new double[][] {
                {3, 2, 3},
                {0, -7, -1},
                {0, 0, 6}
        });
        Assertions.assertTrue(Matrix.isTriangular(t1));

        // Test 2: Lower Triangular
        Matrix t2 = new Matrix(new double[][] {
                {3, 0, 0},
                {2, -7, 0},
                {3, -1, 6}
        });
        Assertions.assertTrue(Matrix.isTriangular(t2));

        // Test 3: Diagonal
        Matrix t3 = Matrix.diag(new double[] { 3, 4, 5, 6 });
        Assertions.assertTrue(Matrix.isTriangular(t3));

        // Test 4: Square, Non-Triangular
        Matrix t4 = new Matrix(new double[][] {
                {3, 2, 3},
                {0, -7, -1},
                {4, 0, 6}
        });
        Assertions.assertFalse(Matrix.isTriangular(t4));

        // Test 5: Non-square
        Matrix t5 = new Matrix(new double[][] {
                {3, 2, 3},
                {0, -7, -1}
        });
        Assertions.assertFalse(Matrix.isTriangular(t5));
    }

    @Test
    void isDiagonalTest() {
        // Test 1: Identity
        Matrix t1 = new Matrix();
        Assertions.assertTrue(Matrix.isDiagonal(t1));

        // Test 2: 1x1 Matrix
        Matrix t2 = new Matrix(new double[][] {
                {3}
        });
        Assertions.assertTrue(Matrix.isDiagonal(t2));

        // Test 3: Diagonal
        Matrix t3 = Matrix.diag(new double[] {3, 4, 5, 6, 7});
        Assertions.assertTrue(Matrix.isDiagonal(t3));

        // Test 4: Diagonal Zero Matrix
        Matrix t4 = Matrix.zeroMatrix(4, 4);
        Assertions.assertTrue(Matrix.isDiagonal(t4));

        // Test 5: Square, non-diagonal
        Matrix t5 = new Matrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        });
        Assertions.assertFalse(Matrix.isDiagonal(t5));

        // Test 6: Non-square
        Matrix t6 = Matrix.zeroMatrix(4, 5);
        Assertions.assertFalse(Matrix.isDiagonal(t6));
    }

    @Test
    void isInvertibleTest() {
        // Test 1: Invertible
        Matrix t1 = new Matrix();
        Assertions.assertTrue(Matrix.isInvertible(t1));

        Matrix t2 = new Matrix(new double[][] {
                {1, 0, -2},
                {-3, 1, 4},
                {2, -3, 4}
        });
        Assertions.assertTrue(Matrix.isInvertible(t2));

        // Test 2: Square, Non-invertible
        Matrix t3 = new Matrix(new double[][] {
                {3, 6, 2},
                {6, 12, -3},
                {2, 4, -6}
        });
        Assertions.assertFalse(Matrix.isInvertible(t3));

        // Test 3: Non-square
        Matrix t4 = new Matrix(new double[][] {
                {3, 2, -1},
                {0, 4, -6}
        });
        Assertions.assertFalse(Matrix.isInvertible(t4));
    }

    @Test
    void isSymmetricTest() {
        // Test 1: Symmetric
        Matrix t1 = new Matrix(new double[][] {
                {3, 2, 1},
                {2, 4, 5},
                {1, 5, 2}
        });
        Assertions.assertTrue(Matrix.isSymmetric(t1));

        // Test 2: Square, Non-symmetric
        Matrix t2 = new Matrix(new double[][] {
                {3, 2, 1},
                {6, 5, 4},
                {9, 8, 7}
        });
        Assertions.assertFalse(Matrix.isSymmetric(t2));

        // Test 3: Diagonal Matrix
        Matrix t3 = new Matrix();
        Assertions.assertTrue(Matrix.isSymmetric(t3));

        Matrix t4 = Matrix.diag(new double[] {3, 4, 5, 6});
        Assertions.assertTrue(Matrix.isSymmetric(t4));

        // Test 4: Zero Matrix
        Matrix t5 = Matrix.zeroMatrix(4, 4);
        Assertions.assertTrue(Matrix.isSymmetric(t5));

        Matrix t6 = Matrix.zeroMatrix(3, 4);
        Assertions.assertFalse(Matrix.isSymmetric(t6));

        // Test 5: 1x1 Matrix
        Matrix t7 = new Matrix(new double[][] {
                {-7}
        });
        Assertions.assertTrue(Matrix.isSymmetric(t7));

        // Test 6: Non-square
        Matrix t8 = new Matrix(new double[][] {
                {5, 1, -9},
                {6, 0, 2},
                {-3, -5, -1},
                {9, 0, 0}
        });
        Assertions.assertFalse(Matrix.isSymmetric(t8));
    }

    @Test
    void isUnitTest() {
        // Test 1: Unit Column Vector
        Matrix t1 = new Matrix(new double[][] {
                {1},
                {0},
                {0}
        });
        Assertions.assertTrue(Matrix.isUnit(t1));

        Matrix t2 = new Matrix(new double[][] {
                {0},
                {1 / Math.sqrt(3)},
                {1 / Math.sqrt(3)},
                {0},
                {1 / Math.sqrt(3)}
        });
        Assertions.assertTrue(Matrix.isUnit(t2));

        Matrix t3 = new Matrix(new double[][] {
                {1 / Math.sqrt(4)},
                {0},
                {-1 / Math.sqrt(4)},
                {1 / Math.sqrt(2)}
        });
        Assertions.assertTrue(Matrix.isUnit(t3));

        // Test 2: Unit Row Vector
        Matrix t4 = new Matrix(new double[][] {
                {1, 0, 0}
        });
        Assertions.assertTrue(Matrix.isUnit(t4));

        Matrix t5 = new Matrix(new double[][] {
                {0, 1 / Math.sqrt(3), 1 / Math.sqrt(3), 0, 1 / Math.sqrt(3)}
        });
        Assertions.assertTrue(Matrix.isUnit(t5));

        Matrix t6 = new Matrix(new double[][] {
                {1 / Math.sqrt(4), 0, -1 / Math.sqrt(4), 1 / Math.sqrt(2)}
        });
        Assertions.assertTrue(Matrix.isUnit(t6));

        // Test 3: Non-unit Column Vector
        Matrix t7 = new Matrix(new double[][] {
                {2}
        });
        Assertions.assertFalse(Matrix.isUnit(t7));

        Matrix t8 = new Matrix(new double[][] {
                {1},
                {0},
                {1},
                {0}
        });
        Assertions.assertFalse(Matrix.isUnit(t8));

        Matrix t9 = new Matrix(new double[][] {
                {-4},
                {-2},
                {1},
                {4}
        });
        Assertions.assertFalse(Matrix.isUnit(t9));

        // Test 4: Non-unit Row Vector
        Matrix t10 = new Matrix(new double[][] {
                {1, 0, 1, 0}
        });
        Assertions.assertFalse(Matrix.isUnit(t10));

        Matrix t11 = new Matrix(new double[][] {
                {-4, -2, 1, 4}
        });
        Assertions.assertFalse(Matrix.isUnit(t11));

        Matrix t12 = new Matrix(new double[][] {
                {0.5, 0.25, 0.25}
        });
        Assertions.assertFalse(Matrix.isUnit(t12));

        // Test 5: Non-vector
        Matrix t13 = new Matrix();
        Assertions.assertFalse(Matrix.isUnit(t13));

        Matrix t14 = Matrix.zeroMatrix(3, 3);
        Assertions.assertFalse(Matrix.isUnit(t14));

        Matrix t15 = new Matrix(new double[][] {
                {3, -8, 1},
                {1, -1, 2},
                {-5, 5, 2}
        });
        Assertions.assertFalse(Matrix.isUnit(t15));

        Matrix t16 = new Matrix(new double[][] {
                {0, -2, 1, 5},
                {9, -9, 8, -3}
        });
        Assertions.assertFalse(Matrix.isUnit(t16));
    }

    @Test
    void areOrthoTest() {
        // Test 1: Orthogonal Column Vectors
        Matrix t1 = Matrix.zeroMatrix(3, 1);
        Matrix t2 = Matrix.zeroMatrix(3, 1);
        Assertions.assertTrue(Matrix.areOrtho(t1, t2));

        Matrix t3 = new Matrix(new double[][] {
                {2},
                {1},
                {0}
        });
        Matrix t4 = new Matrix(new double[][] {
                {0},
                {0},
                {3}
        });
        Assertions.assertTrue(Matrix.areOrtho(t3, t4));

        Matrix t5 = new Matrix(new double[][] {
                {-2},
                {1}
        });
        Matrix t6 = new Matrix(new double[][] {
                {1},
                {2}
        });
        Assertions.assertTrue(Matrix.areOrtho(t5, t6));

        // Test 2: Orthogonal Row Vectors
        Matrix t7 = Matrix.zeroMatrix(1, 3);
        Matrix t8 = Matrix.zeroMatrix(1, 3);
        Assertions.assertTrue(Matrix.areOrtho(t7, t8));

        Matrix t9 = new Matrix(new double[][] {
                {2, 1, 0},
        });
        Matrix t10 = new Matrix(new double[][] {
                {0, 0, 3}
        });
        Assertions.assertTrue(Matrix.areOrtho(t9, t10));

        Matrix t11 = new Matrix(new double[][] {
                {-2, 1}
        });
        Matrix t12 = new Matrix(new double[][] {
                {1, 2}
        });
        Assertions.assertTrue(Matrix.areOrtho(t11, t12));

        // Test 3: Non-orthogonal Column Vectors
        Matrix t13 = new Matrix(new double[][] {
                {4},
                {-2},
                {1},
                {5}
        });
        Matrix t14 = new Matrix(new double[][] {
                {-4},
                {2},
                {-1},
                {-5}
        });
        Assertions.assertFalse(Matrix.areOrtho(t13, t14));

        Matrix t15 = new Matrix(new double[][] {
                {0},
                {1}
        });
        Matrix t16 = new Matrix(new double[][] {
                {0},
                {-1}
        });
        Assertions.assertFalse(Matrix.areOrtho(t15, t16));

        Matrix t17 = new Matrix(new double[][] {
                {3},
                {-1},
                {0}
        });
        Matrix t18 = new Matrix(new double[][] {
                {3},
                {1},
                {0}
        });
        Assertions.assertFalse(Matrix.areOrtho(t17, t18));

        // Test 4: Non-orthogonal Row Vectors
        Matrix t19 = new Matrix(new double[][] {
                {4, -2, 1, 5}
        });
        Matrix t20 = new Matrix(new double[][] {
                {-4, 2, -1, -5}
        });
        Assertions.assertFalse(Matrix.areOrtho(t19, t20));

        Matrix t21 = new Matrix(new double[][] {
                {0, 1}
        });
        Matrix t22 = new Matrix(new double[][] {
                {0, -1}
        });
        Assertions.assertFalse(Matrix.areOrtho(t21, t22));

        Matrix t23 = new Matrix(new double[][] {
                {3, -1, 0}
        });
        Matrix t24 = new Matrix(new double[][] {
                {3, 1, 0}
        });
        Assertions.assertFalse(Matrix.areOrtho(t23, t24));

        // Test 5: Vectors of Different Sizes
        Assertions.assertFalse(Matrix.areOrtho(t1, t5));
        Assertions.assertFalse(Matrix.areOrtho(t14, t15));
        Assertions.assertFalse(Matrix.areOrtho(t19, t21));
        Assertions.assertFalse(Matrix.areOrtho(t22, t24));
        Assertions.assertFalse(Matrix.areOrtho(t1, t23));
        Assertions.assertFalse(Matrix.areOrtho(t14, t20));

        // Test 6: Non-vectors
        Matrix t25 = Matrix.zeroMatrix(2, 2);
        Assertions.assertFalse(Matrix.areOrtho(t25, t21));
        Assertions.assertFalse(Matrix.areOrtho(t25, t15));

        Matrix t26 = Matrix.diag(new double[] {3, 4, 5, 6});
        Assertions.assertFalse(Matrix.areOrtho(t26, t13));
        Assertions.assertFalse(Matrix.areOrtho(t26, t20));

    }

    @Test
    void hasOrthoColsAndIsOrthoTest() {
        // hasOrthoCols tests
        // Test 1: Orthonormal Columns
        Matrix t1 = new Matrix();
        Assertions.assertTrue(Matrix.hasOrthoCols(t1, false));
        Assertions.assertTrue(Matrix.hasOrthoCols(t1, true));

        Matrix t2 = new Matrix(new double[][] {
                {3/Math.sqrt(11), -1/Math.sqrt(6), -1/Math.sqrt(66)},
                {1/Math.sqrt(11), 2/Math.sqrt(6), -4/Math.sqrt(66)},
                {1/Math.sqrt(11), 1/Math.sqrt(6), 7/Math.sqrt(66)}
        });
        Assertions.assertTrue(Matrix.hasOrthoCols(t2, false));
        Assertions.assertTrue(Matrix.hasOrthoCols(t2, true));

        Matrix t3 = new Matrix(new double[][] {
                {2/Math.sqrt(30), -2/Math.sqrt(6)},
                {5/Math.sqrt(30), 1/Math.sqrt(6)},
                {-1/Math.sqrt(30), 1/Math.sqrt(6)}
        });
        Assertions.assertTrue(Matrix.hasOrthoCols(t3, false));
        Assertions.assertTrue(Matrix.hasOrthoCols(t3, true));

        // Test 2: Orthogonal Columns, but Not Orthonormal
        Matrix t4 = Matrix.scale(new Matrix(), 3);
        Assertions.assertTrue(Matrix.hasOrthoCols(t4, false));
        Assertions.assertFalse(Matrix.hasOrthoCols(t4, true));

        Matrix t5 = new Matrix(new double[][] {
                {3, -1, -1},
                {1, 2, -4},
                {1, 1, 7}
        });
        Assertions.assertTrue(Matrix.hasOrthoCols(t5, false));
        Assertions.assertFalse(Matrix.hasOrthoCols(t5, true));

        Matrix t6 = new Matrix(new double[][] {
                {2, -2},
                {5, 1},
                {-1, 1}
        });
        Assertions.assertTrue(Matrix.hasOrthoCols(t6, false));
        Assertions.assertFalse(Matrix.hasOrthoCols(t6, true));

        Matrix t7 = Matrix.zeroMatrix(3, 3);
        Assertions.assertTrue(Matrix.hasOrthoCols(t7, false));
        Assertions.assertFalse(Matrix.hasOrthoCols(t7, true));

        // Test 3: Column Vectors
        Matrix t8 = new Matrix(new double[][] {
                {3},
                {-4},
                {-1}
        });
        Assertions.assertTrue(Matrix.hasOrthoCols(t8, false));
        Assertions.assertFalse(Matrix.hasOrthoCols(t8, true));

        Matrix t9 = new Matrix(new double[][] {
                {1 / Math.sqrt(2)},
                {-1 / Math.sqrt(2)},
                {0}
        });
        Assertions.assertTrue(Matrix.hasOrthoCols(t9, false));
        Assertions.assertTrue(Matrix.hasOrthoCols(t9, true));

        Matrix t10 = Matrix.zeroMatrix(3, 1);
        Assertions.assertTrue(Matrix.hasOrthoCols(t10, false));
        Assertions.assertFalse(Matrix.hasOrthoCols(t10, true));

        // Test 4: Non-orthogonal Columns
        Matrix t11 = new Matrix(new double[][] {
                {1, 2},
                {3, 4}
        });
        Assertions.assertFalse(Matrix.hasOrthoCols(t11, false));
        Assertions.assertFalse(Matrix.hasOrthoCols(t11, true));

        Matrix t12 = new Matrix(new double[][] {
                {2, 0, -3},
                {3, 0, -2},
                {0, 2, 1},
        });
        Assertions.assertFalse(Matrix.hasOrthoCols(t12, false));
        Assertions.assertFalse(Matrix.hasOrthoCols(t12, true));

        Matrix t13 = new Matrix(new double[][] {
                {1, 2, 3},
                {2, 4, 6},
                {3, 6, 9}
        });
        Assertions.assertFalse(Matrix.hasOrthoCols(t13, false));
        Assertions.assertFalse(Matrix.hasOrthoCols(t13, true));

        // isOrtho tests
        // Test 1: Invalid inputs
        Matrix[] t14 = new Matrix[0];
        Assertions.assertFalse(Matrix.isOrtho(t14, false));
        Assertions.assertFalse(Matrix.isOrtho(t14, true));

        Matrix[] t15 = new Matrix[] {
                new Matrix(new double[][] {
                        {2, 3},
                        {4, 1}
                }),
                new Matrix(new double[][] {
                        {-4, 1},
                        {7, 6}
                }),
                new Matrix(new double[][] {
                        {11, -2},
                        {-5, 4}
                })
        };
        Assertions.assertFalse(Matrix.isOrtho(t15, false));
        Assertions.assertFalse(Matrix.isOrtho(t15, true));

        // Test 2: Orthonormal Columns
        Matrix[] t16 = Matrix.getCols(t1);
        Assertions.assertTrue(Matrix.isOrtho(t16, false));
        Assertions.assertTrue(Matrix.isOrtho(t16, true));

        Matrix[] t17 = Matrix.getCols(t2);
        Assertions.assertTrue(Matrix.isOrtho(t17, false));
        Assertions.assertTrue(Matrix.isOrtho(t17, true));

        Matrix[] t18 = Matrix.getCols(t3);
        Assertions.assertTrue(Matrix.isOrtho(t18, false));
        Assertions.assertTrue(Matrix.isOrtho(t18, true));

        // Test 3: Orthogonal Columns, but Not Orthonormal
        Matrix[] t19 = Matrix.getCols(t4);
        Assertions.assertTrue(Matrix.isOrtho(t19, false));
        Assertions.assertFalse(Matrix.isOrtho(t19, true));

        Matrix[] t20 = Matrix.getCols(t5);
        Assertions.assertTrue(Matrix.isOrtho(t20, false));
        Assertions.assertFalse(Matrix.isOrtho(t20, true));

        Matrix[] t21 = Matrix.getCols(t6);
        Assertions.assertTrue(Matrix.isOrtho(t21, false));
        Assertions.assertFalse(Matrix.isOrtho(t21, true));

        Matrix[] t22 = Matrix.getCols(t7);
        Assertions.assertTrue(Matrix.isOrtho(t22, false));
        Assertions.assertFalse(Matrix.isOrtho(t22, true));

        // Test 4: Sets with One Vector
        Matrix[] t23 = Matrix.getCols(t8);
        Assertions.assertTrue(Matrix.isOrtho(t23, false));
        Assertions.assertFalse(Matrix.isOrtho(t23, true));

        Matrix[] t24 = Matrix.getCols(t9);
        Assertions.assertTrue(Matrix.isOrtho(t24, false));
        Assertions.assertTrue(Matrix.isOrtho(t24, true));

        Matrix[] t25 = Matrix.getCols(t10);
        Assertions.assertTrue(Matrix.isOrtho(t25, false));
        Assertions.assertFalse(Matrix.isOrtho(t25, true));

        // Test 5: Non-orthogonal Columns
        Matrix[] t26 = Matrix.getCols(t11);
        Assertions.assertFalse(Matrix.isOrtho(t26, false));
        Assertions.assertFalse(Matrix.isOrtho(t26, true));

        Matrix[] t27 = Matrix.getCols(t12);
        Assertions.assertFalse(Matrix.isOrtho(t27, false));
        Assertions.assertFalse(Matrix.isOrtho(t27, true));

        Matrix[] t28 = Matrix.getCols(t13);
        Assertions.assertFalse(Matrix.isOrtho(t28, false));
        Assertions.assertFalse(Matrix.isOrtho(t28, true));


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