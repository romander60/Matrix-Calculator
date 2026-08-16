/**
 * The test suite for the Matrix Calculator
 * Author: romander60
 * Last updated: August 15, 2026
 */


package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MatrixTest {

    double tol = 0.000001;

    //-------------------------------------------------------------------------------------------------------
    // GENERATOR TESTS - COMPLETE

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
        Assertions.assertThrows(AssertionError.class, () -> new Matrix(0) );
        Assertions.assertThrows(AssertionError.class, () -> new Matrix(-5) );
        Assertions.assertThrows(InvalidMatrixException.class, () -> new Matrix(new double[][] {
                {}
        })  );
        Assertions.assertThrows(InvalidMatrixException.class, () -> new Matrix(new double[][] {}));
        Assertions.assertThrows(InvalidMatrixException.class, () -> new Matrix(new double[][] {
                {2, 3, 4},
                {3, 5},
                {2},
                {4, 5, 2, 5, 6},
                {}
        })  );
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
        Assertions.assertThrows(InvalidMatrixException.class, () -> Matrix.zeroMatrix(m, 0));
        Assertions.assertThrows(InvalidMatrixException.class, () -> Matrix.zeroMatrix(0, n));
        Assertions.assertThrows(InvalidMatrixException.class, () -> Matrix.zeroMatrix(0, 0));
        Assertions.assertThrows(InvalidMatrixException.class, () -> Matrix.zeroMatrix(m, -3));
        Assertions.assertThrows(InvalidMatrixException.class, () -> Matrix.zeroMatrix(-6, n));

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
        Assertions.assertThrows(InvalidMatrixException.class, () -> Matrix.diag(new double[] {}) );
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
        Assertions.assertThrows(AssertionError.class, () -> Matrix.getCol(m1, 2));
        Assertions.assertThrows(AssertionError.class, () -> Matrix.getCol(m1, -3));
        Assertions.assertThrows(AssertionError.class, () -> Matrix.getCol(m1, 0));

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
        Assertions.assertThrows(AssertionError.class, () -> Matrix.getCol(m2, 5));
        Assertions.assertThrows(AssertionError.class, () -> Matrix.getCol(m2, -1));

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
        Assertions.assertThrows(AssertionError.class, () -> Matrix.getCol(m3, 4) );
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
        Assertions.assertThrows(AssertionError.class, () -> Matrix.getCol(m1, 5));
        Assertions.assertThrows(AssertionError.class, () -> Matrix.getCol(m1, -1));
        Assertions.assertThrows(AssertionError.class, () -> Matrix.getCol(m1, 0));

        // Test 2: Row Vector
        Matrix m2 = new Matrix(new double[][] {
                {4, -5, 2, -6}
        } );
        Assertions.assertEquals(m2, Matrix.getRow(m2, 1));
        Assertions.assertThrows(AssertionError.class, () -> Matrix.getRow(m2, 2));
        Assertions.assertThrows(AssertionError.class, () -> Matrix.getRow(m2, -1));

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
        Assertions.assertThrows(AssertionError.class, () -> Matrix.getRow(m3, 6) );
        Assertions.assertThrows(AssertionError.class, () -> Matrix.getRow(m3, -2) );
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
        Assertions.assertThrows(AssertionError.class, () -> Matrix.getEntry(m1, -3, 5) );
        Assertions.assertThrows(AssertionError.class, () -> Matrix.getEntry(m1, 6, 4) );

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
        Assertions.assertThrows(AssertionError.class, () -> Matrix.getEntry(m2, 2, -1) );
        Assertions.assertThrows(AssertionError.class, () -> Matrix.getEntry(m2, 3, 7) );
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
            Matrix.getSubmatrix(m1, 1, -1, 1, 1) );
        Assertions.assertThrows(AssertionError.class, () ->
            Matrix.getSubmatrix(m1, 4, 1, 2, 1));
        Assertions.assertThrows(AssertionError.class, () ->
            Matrix.getSubmatrix(m1, 2, 1, 1, 1));

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
            Matrix.getSubmatrix(m2, 1, 1, 3, 1));
        Assertions.assertThrows(AssertionError.class, () ->
            Matrix.getSubmatrix(m2, 0, 1, 1, 4));
        Assertions.assertThrows(AssertionError.class, () ->
            Matrix.getSubmatrix(m2, 1, 4, 1, 1));


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
            Matrix.getSubmatrix(m3, 4, 3, 2, 2));
        Assertions.assertThrows(AssertionError.class, () ->
            Matrix.getSubmatrix(m3, 6, 2, 5, 3));
        Assertions.assertThrows(AssertionError.class, () ->
            Matrix.getSubmatrix(m3, 2, 5, 5, 3));
        Assertions.assertThrows(AssertionError.class, () ->
            Matrix.getSubmatrix(m3, 2, 2, -2, 3));
        Assertions.assertThrows(AssertionError.class, () ->
            Matrix.getSubmatrix(m3, 2, 2, 5, -1));
        Assertions.assertThrows(AssertionError.class, () ->
            Matrix.getSubmatrix(m3, 2, 2, 5, 4));
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
    // MATRIX MANIPULATION TESTS - COMPLETE

    @Test
    void appendTest() {
        // Test 1 (Right-Append): Valid Size
        Matrix t1 = new Matrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        });
        Matrix t2 = new Matrix(new double[][] {
                {10, 11, 12},
                {13, 14, 15},
                {16, 17, 18}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 2, 3, 10, 11, 12},
                {4, 5, 6, 13, 14, 15},
                {7, 8, 9, 16, 17, 18}
        }), Matrix.append(t1, t2, true));
        Assertions.assertEquals(new Matrix(new double[][] {
                {10, 11, 12, 1, 2, 3},
                {13, 14, 15, 4, 5, 6},
                {16, 17, 18, 7, 8, 9}
        }), Matrix.append(t2, t1, true));

        Matrix t3 = new Matrix(new double[][] {
                {3},
                {4}
        });
        Matrix t4 = new Matrix(new double[][] {
                {4, 1, -7},
                {1, -2, 9}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {3, 4, 1, -7},
                {4, 1, -2, 9}
        }), Matrix.append(t3, t4, true));
        Assertions.assertEquals(new Matrix(new double[][] {
                {4, 1, -7, 3},
                {1, -2, 9, 4}
        }), Matrix.append(t4, t3, true));

        // Test 2 (Right-Append): Invalid Size
        Assertions.assertThrows(MatrixSizeMismatchException.class, () -> Matrix.append(t1, t4, true));
        Assertions.assertThrows(MatrixSizeMismatchException.class, () -> Matrix.append(t4, t1, true));
        Assertions.assertThrows(MatrixSizeMismatchException.class, () -> Matrix.append(t2, t3, true));
        Assertions.assertThrows(MatrixSizeMismatchException.class, () -> Matrix.append(t3, t2, true));


        // Test 3 (Bottom-Append): Valid Size
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
                {10, 11, 12},
                {13, 14, 15},
                {16, 17, 18}
        }), Matrix.append(t1, t2, false));
        Assertions.assertEquals(new Matrix(new double[][] {
                {10, 11, 12},
                {13, 14, 15},
                {16, 17, 18},
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        }), Matrix.append(t2, t1, false));
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
                {4, 1, -7},
                {1, -2, 9}
        }), Matrix.append(t1, t4, false));
        Assertions.assertEquals(new Matrix(new double[][] {
                {4, 1, -7},
                {1, -2, 9},
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        }), Matrix.append(t4, t1, false));


        // Test 4 (Bottom-Append): Invalid Size
        Assertions.assertThrows(MatrixSizeMismatchException.class, () -> Matrix.append(t1, t3, false));
        Assertions.assertThrows(MatrixSizeMismatchException.class, () -> Matrix.append(t3, t1, false));
        Assertions.assertThrows(MatrixSizeMismatchException.class, () -> Matrix.append(t3, t4, false));
        Assertions.assertThrows(MatrixSizeMismatchException.class, () -> Matrix.append(t4, t3, false));

    }

    @Test
    void formMatrixTest() {
        // Test 1 (Column Vectors): Valid Input Array
        Matrix[] t1 = new Matrix[2];
        t1[0] = new Matrix(new double[][] {
                {1},
                {3}

        });
        t1[1] = new Matrix(new double[][] {
                {2},
                {4}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 2},
                {3, 4}
        }), Matrix.formMatrix(t1));

        Matrix[] t2 = new Matrix[3];
        t2[0] = new Matrix(new double[][] {
                {1},
                {2},
                {3},
                {4}
        });
        t2[1] = new Matrix(new double[][] {
                {5},
                {6},
                {7},
                {8}
        });
        t2[2] = new Matrix(new double[][] {
                {9},
                {10},
                {11},
                {12}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 5, 9},
                {2, 6, 10},
                {3, 7, 11},
                {4, 8, 12}
        }), Matrix.formMatrix(t2));

        // Test 2 (Column Vectors): Invalid Input Array
        Matrix[] t3 = new Matrix[0];
        Assertions.assertThrows(AssertionError.class, () -> Matrix.formMatrix(t3));

        Matrix[] t4 = new Matrix[3];
        t4[0] = new Matrix(new double[][] {
                {1, 2},
                {3, 4}
        });
        t4[1] = new Matrix(new double[][] {
                {5, 6},
                {7, 8}
        });
        t4[2] = new Matrix(new double[][] {
                {9},
                {10}
        });
        Assertions.assertThrows(InvalidMatrixException.class, () -> Matrix.formMatrix(t4));

        Matrix[] t5 = new Matrix[3];
        t5[0] = new Matrix(new double[][] {
                {1},
                {2},
                {3}
        });
        t5[1] = new Matrix(new double[][] {
                {4},
                {5}
        });
        t5[2] = new Matrix(new double[][] {
                {6},
                {7},
                {8}
        });
        Assertions.assertThrows(InvalidMatrixException.class, () -> Matrix.formMatrix(t5));

        // Test 3 (Row Vectors): Valid Input Array
        Matrix[] t6 = new Matrix[2];
        t6[0] = Matrix.transpose(t1[0]);
        t6[1] = Matrix.transpose(t1[1]);
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 3},
                {2, 4}
        }), Matrix.formMatrix(t6));

         Matrix[] t7 = new Matrix[3];
         t7[0] = Matrix.transpose(t2[0]);
         t7[1] = Matrix.transpose(t2[1]);
         t7[2] = Matrix.transpose(t2[2]);
         Assertions.assertEquals(new Matrix(new double[][] {
                 {1, 2, 3, 4},
                 {5, 6, 7, 8},
                 {9, 10, 11, 12}
         }),  Matrix.formMatrix(t7));

        // Test 4 (Row Vectors): Invalid Input Array
        Matrix[] t8 = new Matrix[3];
        t8[0] = new Matrix(new double[][] {
                {4, 7, 2},
                {3, 4, 5},
                {6, 5, 3}
        });
        t8[1] = new Matrix(new double[][] {
                {5, 6, 1},
                {7, 8, -1}
        });
        t8[2] = new Matrix(new double[][] {
                {9, -10, 2}
        });
        Assertions.assertThrows(InvalidMatrixException.class, () -> Matrix.formMatrix(t8));

        Matrix[] t9 = new Matrix[4];
        t9[0] = new Matrix(new double[][] {
                {1, 2, 3, 4}
        });
        t9[1] = new Matrix(new double[][] {
                {5, 6, 7, 8}
        });
        t9[2] = new Matrix(new double[][] {
                {9, 10, 11}
        });
        t9[3] = new Matrix(new double[][] {
                {12}
        });
        Assertions.assertThrows(InvalidMatrixException.class, () -> Matrix.formMatrix(t9));

        // Test 5: Matrix Creation -> getCols (or getRows) -> formMatrix = Identity operation
        Matrix t10 = new Matrix(new double[][] {
                {1}
        });
        Assertions.assertEquals(t10, Matrix.formMatrix(Matrix.getCols(t10)));
        Assertions.assertEquals(t10, Matrix.formMatrix(Matrix.getRows(t10)));
        Matrix t11 = new Matrix(new double[][] {
                {3, 4, 9},
                {3, -1, 0},
                {-2, 10, -2}
        });
        Assertions.assertEquals(t11, Matrix.formMatrix(Matrix.getCols(t11)));
        Assertions.assertEquals(t11, Matrix.formMatrix(Matrix.getRows(t11)));
        Matrix t12 = new Matrix(new double[][] {
                {1, -3},
                {0, 0},
                {9, -9},
                {5, 12},
                {6, 8}
        });
        Assertions.assertEquals(t12, Matrix.formMatrix(Matrix.getCols(t12)));
        Assertions.assertEquals(t12, Matrix.formMatrix(Matrix.getRows(t12)));

    }

    @Test
    void replaceColTest() {
        // Test 1: Valid index, Valid column
        Matrix t1 = new Matrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        });
        Matrix c1 = new Matrix(new double[][] {
                {-1},
                {-2},
                {-3}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {-1, 2, 3},
                {-2, 5, 6},
                {-3, 8, 9}
        }), Matrix.replaceCol(t1, 1, c1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, -1, 3},
                {4, -2, 6},
                {7, -3, 9}
        }), Matrix.replaceCol(t1, 2, c1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 2, -1},
                {4, 5, -2},
                {7, 8, -3}
        }), Matrix.replaceCol(t1, 3, c1));

        Matrix t2 = new Matrix(new double[][] {
                {-1, -6},
                {3, 3},
                {6, 7},
                {-9, 2}
        });
        Matrix c2 = new Matrix(new double[][] {
                {1},
                {2},
                {3},
                {4}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, -6},
                {2, 3},
                {3, 7},
                {4, 2}
        }), Matrix.replaceCol(t2, 1, c2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-1, 1},
                {3, 2},
                {6, 3},
                {-9, 4}
        }), Matrix.replaceCol(t2, 2, c2));

        Matrix t3 = new Matrix(new double[][] {
                {4, 6, 1, 5},
                {0, 0, 2, 5}
        });
        Matrix c3 = new Matrix(new double[][] {
                {-10},
                {-10}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {-10, 6, 1, 5},
                {-10, 0, 2, 5}
        }), Matrix.replaceCol(t3, 1, c3));
        Assertions.assertEquals(new Matrix(new double[][] {
                {4, -10, 1, 5},
                {0, -10, 2, 5}
        }), Matrix.replaceCol(t3, 2, c3));
        Assertions.assertEquals(new Matrix(new double[][] {
                {4, 6, 1, -10},
                {0, 0, 2, -10}
        }), Matrix.replaceCol(t3, 4, c3));


        // Test 2: Valid index, Invalid column
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceCol(t1, 1, c2));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceCol(t1, 2, c2));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceCol(t1, 1, c3));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceCol(t1, 3, c3));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceCol(t2, 1, c1));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceCol(t2, 2, c1));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceCol(t2, 1, c3));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceCol(t2, 2, c3));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceCol(t3, 1, c1));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceCol(t3, 2, c1));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceCol(t3, 3, c2));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceCol(t3, 4, c2));

        // Test 3: Invalid index
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceCol(t1, 0, c1));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceCol(t1, -1, c1));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceCol(t1, 4, c1));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceCol(t1, 7, c1));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceCol(t2, 0, c2));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceCol(t2, -1, c2));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceCol(t2, 3, c2));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceCol(t2, 6, c2));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceCol(t3, 0, c3));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceCol(t3, -1, c3));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceCol(t3, 5, c3));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceCol(t3, 14, c3));
    }

    @Test
    void replaceRowTest() {
        // Test 1: Valid index, Valid row
        Matrix t1 = new Matrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        });
        Matrix r1 = new Matrix(new double[][] {
                {-1, -2, -3}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {-1, -2, -3},
                {4, 5, 6},
                {7, 8, 9}
        }), Matrix.replaceRow(t1, 1, r1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 2, 3},
                {-1, -2, -3},
                {7, 8, 9}
        }), Matrix.replaceRow(t1, 2, r1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {-1, -2, -3}
        }), Matrix.replaceRow(t1, 3, r1));

        Matrix t2 = new Matrix(new double[][] {
                {-1, -6},
                {3, 3},
                {6, 7},
                {-9, 2}
        });
        Matrix r2 = new Matrix(new double[][] {
                {5, 5}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {5, 5},
                {3, 3},
                {6, 7},
                {-9, 2}
        }), Matrix.replaceRow(t2, 1, r2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-1, -6},
                {5, 5},
                {6, 7},
                {-9, 2}
        }), Matrix.replaceRow(t2, 2, r2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-1, -6},
                {3, 3},
                {5, 5},
                {-9, 2}
        }), Matrix.replaceRow(t2, 3, r2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-1, -6},
                {3, 3},
                {6, 7},
                {5, 5}
        }), Matrix.replaceRow(t2, 4, r2));

        Matrix t3 = new Matrix(new double[][] {
                {4, 6, 1, 5},
                {0, 0, 2, 5}
        });
        Matrix r3 = new Matrix(new double[][] {
                {-10, -10, -10, -10}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {-10, -10, -10, -10},
                {0, 0, 2, 5}
        }), Matrix.replaceRow(t3, 1, r3));
        Assertions.assertEquals(new Matrix(new double[][] {
                {4, 6, 1, 5},
                {-10, -10, -10, -10}
        }), Matrix.replaceRow(t3, 2, r3));


        // Test 2: Valid index, Invalid row
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceRow(t1, 1, r2));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceRow(t1, 2, r2));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceRow(t1, 1, r3));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceRow(t1, 3, r3));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceRow(t2, 1, r1));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceRow(t2, 2, r1));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceRow(t2, 3, r3));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceRow(t2, 4, r3));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceRow(t3, 1, r1));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceRow(t3, 2, r1));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceRow(t3, 1, r2));
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.replaceRow(t3, 2, r2));

        // Test 3: Invalid index
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceRow(t1, 0, r1));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceRow(t1, -1, r1));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceRow(t1, 4, r1));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceRow(t1, 7, r1));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceRow(t2, 0, r2));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceRow(t2, -1, r2));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceRow(t2, 5, r2));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceRow(t2, 14, r2));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceRow(t3, 0, r3));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceRow(t3, -1, r3));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceRow(t3, 3, r3));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.replaceRow(t3, 10, r3));
    }

    @Test
    void swapColsTest() {
        // Test 1: Valid indices
        Matrix t1 = new Matrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {2, 1, 3},
                {5, 4, 6},
                {8, 7, 9}
        }), Matrix.swapCols(t1, 1, 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {2, 1, 3},
                {5, 4, 6},
                {8, 7, 9}
        }), Matrix.swapCols(t1, 2, 1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 3, 2},
                {4, 6, 5},
                {7, 9, 8}
        }), Matrix.swapCols(t1, 2, 3));
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 3, 2},
                {4, 6, 5},
                {7, 9, 8}
        }), Matrix.swapCols(t1, 3, 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {3, 2, 1},
                {6, 5, 4},
                {9, 8, 7}
        }), Matrix.swapCols(t1, 1, 3));
        Assertions.assertEquals(new Matrix(new double[][] {
                {3, 2, 1},
                {6, 5, 4},
                {9, 8, 7}
        }), Matrix.swapCols(t1, 3, 1));
        Assertions.assertEquals(t1, Matrix.swapCols(t1, 1, 1));
        Assertions.assertEquals(t1, Matrix.swapCols(t1, 2, 2));
        Assertions.assertEquals(t1, Matrix.swapCols(t1, 3, 3));

        Matrix t2 = new Matrix(new double[][] {
                {-1, -6},
                {3, 3},
                {6, 7},
                {-9, 2}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {-6, -1},
                {3, 3},
                {7, 6},
                {2, -9}
        }), Matrix.swapCols(t2, 1, 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-6, -1},
                {3, 3},
                {7, 6},
                {2, -9}
        }), Matrix.swapCols(t2, 2, 1));
        Assertions.assertEquals(t2, Matrix.swapCols(t2, 1, 1));
        Assertions.assertEquals(t2, Matrix.swapCols(t2, 2, 2));

        Matrix t3 = new Matrix(new double[][] {
                {4, 6, 1, 5},
                {0, 0, 2, 5}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {6, 4, 1, 5},
                {0, 0, 2, 5}
        }), Matrix.swapCols(t3, 1, 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 6, 4, 5},
                {2, 0, 0, 5}
        }), Matrix.swapCols(t3, 1, 3));
        Assertions.assertEquals(new Matrix(new double[][] {
                {5, 6, 1, 4},
                {5, 0, 2, 0}
        }), Matrix.swapCols(t3, 1, 4));
        Assertions.assertEquals(new Matrix(new double[][] {
                {4, 1, 6, 5},
                {0, 2, 0, 5}
        }), Matrix.swapCols(t3, 2, 3));
        Assertions.assertEquals(t3, Matrix.swapCols(t3, 1, 1));
        Assertions.assertEquals(t3, Matrix.swapCols(t3, 2, 2));
        Assertions.assertEquals(t3, Matrix.swapCols(t3, 3, 3));
        Assertions.assertEquals(t3, Matrix.swapCols(t3, 4, 4));

        // Test 2: Invalid indices
        Assertions.assertThrows( AssertionError.class, () -> Matrix.swapCols(t1, 0, 1) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.swapCols(t1, -1, 1) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.swapCols(t1, 4, 1) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.swapCols(t2, 1, 0) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.swapCols(t2, 1, -1) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.swapCols(t2, 1, 3) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.swapCols(t3, 0, 0) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.swapCols(t3, -1, -1) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.swapCols(t3, 5, 6) );

    }

    @Test
    void swapRowsTest() {
        // Test 1: Valid indices
        Matrix t1 = new Matrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {4, 5, 6},
                {1, 2, 3},
                {7, 8, 9}
        }), Matrix.swapRows(t1, 1, 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {4, 5, 6},
                {1, 2, 3},
                {7, 8, 9}
        }), Matrix.swapRows(t1, 2, 1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 2, 3},
                {7, 8, 9},
                {4, 5, 6}
        }), Matrix.swapRows(t1, 2, 3));
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 2, 3},
                {7, 8, 9},
                {4, 5, 6}
        }), Matrix.swapRows(t1, 3, 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {7, 8, 9},
                {4, 5, 6},
                {1, 2, 3}
        }), Matrix.swapRows(t1, 1, 3));
        Assertions.assertEquals(new Matrix(new double[][] {
                {7, 8, 9},
                {4, 5, 6},
                {1, 2, 3}
        }), Matrix.swapRows(t1, 3, 1));
        Assertions.assertEquals(t1, Matrix.swapRows(t1, 1, 1));
        Assertions.assertEquals(t1, Matrix.swapRows(t1, 2, 2));
        Assertions.assertEquals(t1, Matrix.swapRows(t1, 3, 3));

        Matrix t2 = new Matrix(new double[][] {
                {-1, -6},
                {3, 3},
                {6, 7},
                {-9, 2}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {3, 3},
                {-1, -6},
                {6, 7},
                {-9, 2}
        }), Matrix.swapRows(t2, 1, 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {6, 7},
                {3, 3},
                {-1, -6},
                {-9, 2}
        }), Matrix.swapRows(t2, 1, 3));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-9, 2},
                {3, 3},
                {6, 7},
                {-1, -6}
        }), Matrix.swapRows(t2, 1, 4));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-1, -6},
                {6, 7},
                {3, 3},
                {-9, 2}
        }), Matrix.swapRows(t2, 2, 3));
        Assertions.assertEquals(t2, Matrix.swapRows(t2, 1, 1));
        Assertions.assertEquals(t2, Matrix.swapRows(t2, 2, 2));
        Assertions.assertEquals(t2, Matrix.swapRows(t2, 3, 3));
        Assertions.assertEquals(t2, Matrix.swapRows(t2, 4, 4));

        Matrix t3 = new Matrix(new double[][] {
                {4, 6, 1, 5},
                {0, 0, 2, 5}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {0, 0, 2, 5},
                {4, 6, 1, 5}
        }), Matrix.swapRows(t3, 1, 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {0, 0, 2, 5},
                {4, 6, 1, 5}
        }), Matrix.swapRows(t3, 2, 1));
        Assertions.assertEquals(t3, Matrix.swapRows(t3, 1, 1));
        Assertions.assertEquals(t3, Matrix.swapRows(t3, 2, 2));

        // Test 2: Invalid indices
        Assertions.assertThrows( AssertionError.class, () -> Matrix.swapRows(t1, 0, 1) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.swapRows(t1, -1, 1) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.swapRows(t1, 4, 1) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.swapRows(t2, 1, 0) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.swapRows(t2, 1, -1) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.swapRows(t2, 1, 5) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.swapRows(t3, 0, 0) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.swapRows(t3, -1, -1) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.swapRows(t3, 3, 6) );
    }

    @Test
    void removeColTest() {
        // Test 1: Valid index
        Matrix t1 = new Matrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {2, 3},
                {5, 6},
                {8, 9}
        }), Matrix.removeCol(t1, 1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 3},
                {4, 6},
                {7, 9}
        }), Matrix.removeCol(t1, 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 2},
                {4, 5},
                {7, 8}
        }), Matrix.removeCol(t1, 3));

        Matrix t2 = new Matrix(new double[][] {
                {-1, -6},
                {3, 3},
                {6, 7},
                {-9, 2}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {-6},
                {3},
                {7},
                {2}
        }), Matrix.removeCol(t2, 1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-1},
                {3},
                {6},
                {-9}
        }), Matrix.removeCol(t2, 2));

        Matrix t3 = new Matrix(new double[][] {
                {4, 6, 1, 5},
                {0, 0, 2, 5}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {6, 1, 5},
                {0, 2, 5}
        }), Matrix.removeCol(t3, 1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {4, 1, 5},
                {0, 2, 5}
        }), Matrix.removeCol(t3, 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {4, 6, 5},
                {0, 0, 5}
        }), Matrix.removeCol(t3, 3));
        Assertions.assertEquals(new Matrix(new double[][] {
                {4, 6, 1},
                {0, 0, 2}
        }), Matrix.removeCol(t3, 4));

        // Test 2: Sequential removals
        Assertions.assertEquals(new Matrix(new double[][] {
                {2},
                {5},
                {8}
        }), Matrix.removeCol(Matrix.removeCol(t1, 1), 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {0},
                {0},
                {0},
                {0}
        }), Matrix.removeCol(Matrix.removeCol(Matrix.removeCol(t2, 1), 1), 1));
        // Third removal should effectively do nothing; the second removal will result in a zero vector.

        // Test 3: Invalid index
        Assertions.assertThrows( AssertionError.class, () -> Matrix.removeCol(t1, 0) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.removeCol(t1, -1) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.removeCol(t1, 4) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.removeCol(t2, 3) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.removeCol(t3, 5) );
    }

    @Test
    void removeRowTest() {
        // Test 1: Valid index
        Matrix t1 = new Matrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {4, 5, 6},
                {7, 8, 9}
        }), Matrix.removeRow(t1, 1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 2, 3},
                {7, 8, 9}
        }), Matrix.removeRow(t1, 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6}
        }), Matrix.removeRow(t1, 3));

        Matrix t2 = new Matrix(new double[][] {
                {-1, -6},
                {3, 3},
                {6, 7},
                {-9, 2}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {3, 3},
                {6, 7},
                {-9, 2}
        }), Matrix.removeRow(t2, 1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-1, -6},
                {6, 7},
                {-9, 2}
        }), Matrix.removeRow(t2, 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-1, -6},
                {3, 3},
                {-9, 2}
        }), Matrix.removeRow(t2, 3));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-1, -6},
                {3, 3},
                {6, 7}
        }), Matrix.removeRow(t2, 4));

        Matrix t3 = new Matrix(new double[][] {
                {4, 6, 1, 5},
                {0, 0, 2, 5}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {0, 0, 2, 5}
        }), Matrix.removeRow(t3, 1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {4, 6, 1, 5}
        }), Matrix.removeRow(t3, 2));

        // Test 2: Sequential removals
        Assertions.assertEquals(new Matrix(new double[][] {
                {4, 5, 6}
        }), Matrix.removeRow(Matrix.removeRow(t1, 1), 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {0, 0, 0, 0}
        }), Matrix.removeRow(Matrix.removeRow(Matrix.removeRow(t3, 1), 1), 1));
        // Third removal should effectively do nothing; the second removal will result in a zero vector.

        // Test 3: Invalid index
        Assertions.assertThrows( AssertionError.class, () -> Matrix.removeRow(t1, 0) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.removeRow(t1, -1) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.removeRow(t1, 4) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.removeRow(t2, 5) );
        Assertions.assertThrows( AssertionError.class, () -> Matrix.removeRow(t3, 3) );
    }

    @Test
    void padTest() {
        // Test 1: Padding Rows
        Matrix t1 = new Matrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
                {-4, -4, -4}
        }), Matrix.pad(t1, 4, 3, -4));
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        }), Matrix.pad(t1, 6, 3, 0));

        // Test 2: Padding Columns
        Matrix t2 = new Matrix(new double[][] {
                {-1, -6},
                {3, 3},
                {6, 7},
                {-9, 2}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {-1, -6, 2, 2},
                {3, 3, 2, 2},
                {6, 7, 2, 2},
                {-9, 2, 2, 2}
        }), Matrix.pad(t2, 4, 4, 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-1, -6, 14, 14, 14, 14},
                {3, 3, 14, 14, 14, 14},
                {6, 7, 14, 14, 14, 14},
                {-9, 2, 14, 14, 14, 14}
        }), Matrix.pad(t2, 4, 6, 14));

        // Test 3: Padding Rows and Columns
        Matrix t3 = new Matrix(new double[][] {
                {4, 6, 1, 5},
                {0, 0, 2, 5}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {4, 6, 1, 5, 0},
                {0, 0, 2, 5, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        }), Matrix.pad(t3, 5, 5, 0));
        Assertions.assertEquals(new Matrix(new double[][] {
                {4, 6, 1, 5, 1},
                {0, 0, 2, 5, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1}
        }), Matrix.pad(t3, 7, 5, 1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {4, 6, 1, 5, 3, 3},
                {0, 0, 2, 5, 3, 3},
                {3, 3, 3, 3, 3, 3}
        }), Matrix.pad(t3, 3, 6, 3));

        // Test 4: Invalid Inputs

        // quick edge cases
        Assertions.assertEquals(t1, Matrix.pad(t1, Matrix.rows(t1), Matrix.cols(t1), 0));
        Assertions.assertEquals(t2, Matrix.pad(t2, Matrix.rows(t2), Matrix.cols(t2), 0));
        Assertions.assertEquals(t3, Matrix.pad(t3, Matrix.rows(t3), Matrix.cols(t3), 0));

        // actual invalid inputs
        Assertions.assertThrows( AssertionError.class, () -> Matrix.pad(t1, 0, 3, 0));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.pad(t1, -1, 3, 0));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.pad(t1, 2, 3, 0));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.pad(t2, 4, 0, 0));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.pad(t2, 4, -1, 0));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.pad(t2, 4, 1, 0));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.pad(t3, 0, 0, 0));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.pad(t3, -1, -1, 0));
        Assertions.assertThrows( AssertionError.class, () -> Matrix.pad(t3, 1, 3, 0));
    }

    //-------------------------------------------------------------------------------------------------------
    // OPERATION TESTS

    @Test
    void copyTest() {
        // Test 1: Just copying a matrix, not much else to do here
        Matrix t1 = new Matrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        });
        Matrix t2 = Matrix.zeroMatrix(4, 6);
        Matrix t3 = Matrix.diag(new double[] {-3, 6, -5});

        Assertions.assertEquals(t1, Matrix.copy(t1));
        Assertions.assertEquals(t2, Matrix.copy(t2));
        Assertions.assertEquals(t3, Matrix.copy(t3));
    }

    @Test
    void transposeTest() {
        // Test 1: Arbitrary Matrices
        Matrix t1 = new Matrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 4, 7},
                {2, 5, 8},
                {3, 6, 9}
        }), Matrix.transpose(t1));

        Matrix t2 = new Matrix(new double[][] {
                {-1, -6},
                {3, 3},
                {6, 7},
                {-9, 2}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {-1, 3, 6, -9},
                {-6, 3, 7, 2}
        }), Matrix.transpose(t2));

        Matrix t3 = new Matrix(new double[][] {
                {4, 6, 1, 5},
                {0, 0, 2, 5}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {4, 0},
                {6, 0},
                {1, 2},
                {5, 5}
        }), Matrix.transpose(t3));

        // Test 2: Column Vectors
        Matrix t4 = Matrix.getCol(t1, 1);
        Matrix t5 = Matrix.getCol(t2, 2);
        Matrix t6 = Matrix.getCol(t3, 3);
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 4, 7}
        }), Matrix.transpose(t4));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-6, 3, 7, 2}
        }), Matrix.transpose(t5));

        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 2}
        }), Matrix.transpose(t6));

        // Test 3: Row Vectors
        Matrix t7 = Matrix.getRow(t1, 1);
        Matrix t8 = Matrix.getRow(t2, 2);
        Matrix t9 = Matrix.getRow(t3, 1);
        Assertions.assertEquals(new Matrix(new double[][] {
                {1},
                {2},
                {3}
        }), Matrix.transpose(t7));
        Assertions.assertEquals(new Matrix(new double[][] {
                {3},
                {3}
        }), Matrix.transpose(t8));
        Assertions.assertEquals(new Matrix(new double[][] {
                {4},
                {6},
                {1},
                {5}
        }), Matrix.transpose(t9));

        // Test 4: Symmetric Matrices
        Matrix t10 = new Matrix();
        Matrix t11 = Matrix.zeroMatrix(4, 4);
        Matrix t12 = Matrix.diag(new double[] {6, -2, 5});
        Matrix t13 = new Matrix(new double[][] {
                {4, 7, 10, -1},
                {7, -3, 3, -8},
                {10, 3, 6, 9},
                {-1, -8, 9, 2}
        });
        Assertions.assertEquals(t10, Matrix.transpose(t10));
        Assertions.assertEquals(t11, Matrix.transpose(t11));
        Assertions.assertEquals(t12, Matrix.transpose(t12));
        Assertions.assertEquals(t13, Matrix.transpose(t13));

        // Test 5: Two Transposes = Identity
        Assertions.assertEquals(t1, Matrix.transpose(Matrix.transpose(t1)));
        Assertions.assertEquals(t2, Matrix.transpose(Matrix.transpose(t2)));
        Assertions.assertEquals(t3, Matrix.transpose(Matrix.transpose(t3)));
        Assertions.assertEquals(t4, Matrix.transpose(Matrix.transpose(t4)));
        Assertions.assertEquals(t5, Matrix.transpose(Matrix.transpose(t5)));
        Assertions.assertEquals(t6, Matrix.transpose(Matrix.transpose(t6)));
        Assertions.assertEquals(t7, Matrix.transpose(Matrix.transpose(t7)));
        Assertions.assertEquals(t8, Matrix.transpose(Matrix.transpose(t8)));
        Assertions.assertEquals(t9, Matrix.transpose(Matrix.transpose(t9)));
        Assertions.assertEquals(t10, Matrix.transpose(Matrix.transpose(t10)));
        Assertions.assertEquals(t11, Matrix.transpose(Matrix.transpose(t11)));
        Assertions.assertEquals(t12, Matrix.transpose(Matrix.transpose(t12)));
        Assertions.assertEquals(t13, Matrix.transpose(Matrix.transpose(t13)));
    }

    @Test
    void dotTest() {
        // Test 1: Column vectors
        Matrix t1 = new Matrix(new double[][] {
                {3},
                {6},
                {0}
        });
        Matrix t2 = new Matrix(new double[][] {
                {0},
                {0},
                {2}
        });
        Assertions.assertEquals(0, Matrix.dot(t1, t2));
        Assertions.assertEquals(0, Matrix.dot(t2, t1));

        Matrix t3 = new Matrix(new double[][] {
                {1},
                {-4}
        });
        Matrix t4 = new Matrix(new double[][] {
                {2},
                {-8}
        });
        Assertions.assertEquals(34,  Matrix.dot(t3, t4));
        Assertions.assertEquals(34, Matrix.dot(t4, t3));

        Matrix t5 = new Matrix(new double[][] {
                {4},
                {-8},
                {-9},
                {3}
        });
        Matrix t6 = new Matrix(new double[][] {
                {2},
                {4},
                {4},
                {7}
        });
        Assertions.assertEquals(-39,  Matrix.dot(t5, t6));
        Assertions.assertEquals(-39, Matrix.dot(t6, t5));

        // Test 2: Row vectors
        Matrix t7 = Matrix.transpose(t1);
        Matrix t8 = Matrix.transpose(t2);
        Assertions.assertEquals(0, Matrix.dot(t7, t8));
        Assertions.assertEquals(0, Matrix.dot(t8, t7));

        Matrix t9 = Matrix.transpose(t3);
        Matrix t10 = Matrix.transpose(t4);
        Assertions.assertEquals(34, Matrix.dot(t9, t10));
        Assertions.assertEquals(34, Matrix.dot(t10, t9));

        Matrix t11 = Matrix.transpose(t5);
        Matrix t12 = Matrix.transpose(t6);
        Assertions.assertEquals(-39, Matrix.dot(t11, t12));
        Assertions.assertEquals(-39, Matrix.dot(t12, t11));

        Matrix t13 = new Matrix(new double[][] {
                {3, 13, -2},
        });
        Matrix t14 = new Matrix(new double[][] {
                {-9, 2, -3}
        });
        Assertions.assertEquals(5, Matrix.dot(t13, t14));
        Assertions.assertEquals(5, Matrix.dot(t14, t13));

        Matrix t15 = new Matrix(new double[][] {
                {5}
        });
        Matrix t16 = new Matrix(new double[][] {
                {-3}
        });
        Assertions.assertEquals(-15,  Matrix.dot(t15, t16));
        Assertions.assertEquals(-15, Matrix.dot(t16, t15));

        // Test 3: Non-vectors
        Matrix t17 = new Matrix(new double[][] {
                {3, -4},
                {2, -3},
                {5, -5}
        });
        Matrix t18 = new Matrix(new double[][] {
                {4, 5, 6},
                {-2, 5, 9}
        });
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.dot(t17, t1) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.dot(t17, t3) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.dot(t17, t5) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.dot(t17, t15) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.dot(t18, t1) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.dot(t18, t3) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.dot(t18, t5) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.dot(t18, t15) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.dot(t17, t18) );

        // Test 4: Vector size mismatches
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.dot(t1, t3) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.dot(t1, t5) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.dot(t1, t15) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.dot(t3, t5) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.dot(t3, t15) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.dot(t5, t15) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.dot(t7, t9) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.dot(t7, t16) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.dot(t9, t11) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.dot(t9, t12) );


    }

    @Test
    void magnTest() {
        // Test 1: Column vector
        Matrix t1 = new Matrix(new double[][] {
                {3},
                {6},
                {0}
        });
        Matrix t2 = new Matrix(new double[][] {
                {1},
                {-4}
        });
        Matrix t3 = new Matrix(new double[][] {
                {4},
                {-8},
                {-9},
                {3}
        });
        Assertions.assertEquals(Math.sqrt(45), Matrix.magn(t1));
        Assertions.assertEquals(Math.sqrt(17), Matrix.magn(t2));
        Assertions.assertEquals(Math.sqrt(170), Matrix.magn(t3));

        // Test 2: Row vector
        Matrix t4 = Matrix.transpose(t1);
        Matrix t5 = Matrix.transpose(t2);
        Matrix t6 = Matrix.transpose(t3);
        Assertions.assertEquals(Math.sqrt(45), Matrix.magn(t4));
        Assertions.assertEquals(Math.sqrt(17), Matrix.magn(t5));
        Assertions.assertEquals(Math.sqrt(170), Matrix.magn(t6));

        Matrix t7 = new Matrix(new double[][] {
                {3, -2, 6}
        });
        Matrix t8 = new Matrix(new double[][] {
                {-11, 4, 8}
        });
        Assertions.assertEquals(7, Matrix.magn(t7));
        Assertions.assertEquals(Math.sqrt(201), Matrix.magn(t8));

        // Test 3: Non-vector
        Matrix t9 = new Matrix();
        Matrix t10 = Matrix.zeroMatrix(2, 3);
        Matrix t11 = Matrix.diag(new double[] {5, 4, 2, 9});
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.magn(t9) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.magn(t10) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.magn(t11) );


    }

    @Test
    void angleTest() {
        // Test 1: Parallel Vectors
        Matrix t1 = new Matrix(new double[][] {
                {1},
                {2}
        });
        Matrix t2 = new Matrix(new double[][] {
                {2},
                {4}
        });
        Assertions.assertTrue( Math.abs( Matrix.angle(t1, t2) ) <= tol );
        Assertions.assertTrue( Math.abs( Matrix.angle(t2, t1) ) <= tol );

        // Test 2: Antiparallel Vectors
        Matrix t3 = Matrix.transpose(t1);
        Matrix t4 = new Matrix(new double[][] {
                {-1, -2}
        });
        Assertions.assertTrue( Math.abs( Math.PI - Matrix.angle(t3, t4) ) <= tol );
        Assertions.assertTrue( Math.abs( Math.PI - Matrix.angle(t4, t3) ) <= tol );

        // Test 3: Orthogonal Vectors
        Matrix t5 =  new Matrix(new double[][] {
                {-2},
                {1}
        });
        Assertions.assertTrue( Math.abs( (Math.PI / 2) - Matrix.angle(t1, t5) ) <= tol );
        Assertions.assertTrue( Math.abs( (Math.PI / 2) - Matrix.angle(t5, t1) ) <= tol );

        // Test 4: Vectors with Unequal Sizes
        Matrix t6 = new Matrix(new double[][] {
                {1},
                {2},
                {3}
        });
        Matrix t7 = new Matrix(new double[][] {
                {4, 5, 6}
        });
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.angle(t6, t7) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.angle(t1, t6) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.angle(t3, t7) );

        // Test 5: Non-vectors
        Matrix t8 = new Matrix(2);
        Matrix t9 = new Matrix(new double[][] {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12}
        });
        Matrix t10 = new Matrix(new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
                {10, 11, 12}
        });
        Matrix t11 = new Matrix();
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.angle(t8, t9) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.angle(t10, t11) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.angle(t1, t8) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.angle(t8, t9) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.angle(t7, t10) );

    }

    @Test
    void distanceTest() {
        // Test 1: Two equal vectors
        Matrix t1 = new Matrix(new double[][] {
                {1},
                {2}
        });
        // Two equal vectors have a distance of zero separating them.
        Assertions.assertTrue( Math.abs(Matrix.distance(t1, t1)) <= tol );

        // Test 2: Two antiparallel vectors
        Matrix t2 = new Matrix(new double[][] {
                {-1},
                {-2}
        });
        // Two antiparallel are separated by a distance equal to the sum of their lengths.
        Assertions.assertTrue( Math.abs(Matrix.magn(t1) + Matrix.magn(t2) - Matrix.distance(t1, t2)) <= tol );
        Assertions.assertTrue( Math.abs(Matrix.magn(t1) + Matrix.magn(t2) - Matrix.distance(t2, t1)) <= tol );

        // Test 3: Two orthogonal vectors
        Matrix t3 = Matrix.transpose(t1);
        Matrix t4 = new Matrix(new double[][] {
                {-2, 1}
        });
        // Two orthogonal vectors are separated by a distance equal to the hypotenuse of the right triangle
        // that they form the legs of.
        Assertions.assertTrue( Math.abs( Math.sqrt(Matrix.dot(t3, t3) + Matrix.dot(t4, t4))
                - Matrix.distance(t3, t4) ) <= tol );
        Assertions.assertTrue( Math.abs( Math.sqrt(Matrix.dot(t3, t3) + Matrix.dot(t4, t4))
                - Matrix.distance(t4, t3) ) <= tol );

        // Test 4: Vectors with unequal sizes
        Matrix t5 = new Matrix(new double[][] {
                {1},
                {2},
                {3}
        });
        Matrix t6 = new Matrix(new double[][] {
                {4, 5, 6}
        });

        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.distance(t1, t3) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.distance(t2, t5) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.distance(t4, t6) );

        // Test 5: Non-vectors
        Matrix t7 = new Matrix(2);
        Matrix t8 = new Matrix(new double[][] {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12}
        });
        Matrix t9 = new Matrix(new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
                {10, 11, 12}
        });
        Matrix t10 = new Matrix();
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.distance(t7, t8) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.distance(t9, t10) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.distance(t1, t7) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.distance(t7, t8) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.distance(t6, t9) );
    }

    @Test
    void normalizeTest() {
        // Single Matrix Tests

        // Test 1: Arbitrary Vectors
        Matrix t1 = new Matrix(new double[][] {
                {2},
                {-6},
                {7}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {2 / Math.sqrt(89)},
                {-6 / Math.sqrt(89)},
                {7 / Math.sqrt(89)}
        }), Matrix.normalize(t1, true));

        Matrix t2 = new Matrix(new double[][] {
                {-5, 5, 0, -1}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {-5 / Math.sqrt(51), 5 / Math.sqrt(51), 0, -1 / Math.sqrt(51)}
        }), Matrix.normalize(t2, true));

        // Test 2: Unit Vectors
        Matrix t3 = new Matrix(new double[][] {
                {2 / Math.sqrt(89)},
                {-6 / Math.sqrt(89)},
                {7 / Math.sqrt(89)}
        });
        Matrix t4 = new Matrix(new double[][] {
                {-5 / Math.sqrt(51), 5 / Math.sqrt(51), 0, -1 / Math.sqrt(51)}
        });
        Assertions.assertEquals(t3, Matrix.normalize(t3, true));
        Assertions.assertEquals(t4, Matrix.normalize(t4, true));

        // Test 3: Zero Vectors / Zero Matrices
        Matrix t5 = Matrix.zeroMatrix(3, 1);
        Matrix t6 = Matrix.zeroMatrix(1, 4);
        Matrix t7 = Matrix.zeroMatrix(5, 2);
        Matrix t8 = Matrix.zeroMatrix(3, 3);
        Assertions.assertEquals(t5, Matrix.normalize(t5, true));
        Assertions.assertEquals(t6, Matrix.normalize(t6, true));
        Assertions.assertEquals(t7, Matrix.normalize(t7, true));
        Assertions.assertEquals(t7, Matrix.normalize(t7, false));
        Assertions.assertEquals(t8, Matrix.normalize(t8, true));
        Assertions.assertEquals(t8, Matrix.normalize(t8, false));

        // Test 4: Arbitrary Matrices
        Matrix t9 = new Matrix();
        Matrix t10 = new Matrix(new double[][] {
                {3, 2, -6, -7},
                {1, 4, 5, -2}
        });
        Matrix t11 = new Matrix(new double[][] {
                {4}
        });
        Matrix t12 = new Matrix(new double[][] {
                {2, 5},
                {-5, 1},
                {1, 4},
                {4, 9},
                {9, -3}
        });
        Assertions.assertEquals(t9, Matrix.normalize(t9, true));
        Assertions.assertEquals(t9, Matrix.normalize(t9, false));
        Assertions.assertEquals(new Matrix(new double[][] {
                {3 / Math.sqrt(10), 2 / Math.sqrt(20), -6 / Math.sqrt(61), -7 / Math.sqrt(53)},
                {1 / Math.sqrt(10), 4 / Math.sqrt(20), 5 / Math.sqrt(61), -2 / Math.sqrt(53)}
        }), Matrix.normalize(t10, true));
        Assertions.assertEquals(new Matrix(new double[][] {
                {3 / Math.sqrt(98), 2 / Math.sqrt(98), -6 / Math.sqrt(98), -7 / Math.sqrt(98)},
                {1 / Math.sqrt(46), 4 / Math.sqrt(46), 5 / Math.sqrt(46), -2 / Math.sqrt(46)}
        }), Matrix.normalize(t10, false));
        Assertions.assertEquals(new Matrix(new double[][] {
                {1}
        }), Matrix.normalize(t11, true));
        Assertions.assertEquals(new Matrix(new double[][] {
                {1}
        }), Matrix.normalize(t11, false));
        Assertions.assertEquals(new Matrix(new double[][] {
                {2 / Math.sqrt(127), 5 / Math.sqrt(132)},
                {-5 / Math.sqrt(127), 1 / Math.sqrt(132)},
                {1 / Math.sqrt(127), 4 / Math.sqrt(132)},
                {4 / Math.sqrt(127), 9 / Math.sqrt(132)},
                {9 / Math.sqrt(127), -3 / Math.sqrt(132)}
        }), Matrix.normalize(t12, true));
        Assertions.assertEquals(new Matrix(new double[][] {
                {2 / Math.sqrt(29), 5 / Math.sqrt(29)},
                {-5 / Math.sqrt(26), 1 / Math.sqrt(26)},
                {1 / Math.sqrt(17), 4 / Math.sqrt(17)},
                {4 / Math.sqrt(97), 9 / Math.sqrt(97)},
                {9 / Math.sqrt(90), -3 / Math.sqrt(90)}
        }), Matrix.normalize(t12, false));


        // Matrix Array Tests

        // Test 5: Empty input array
        Matrix[] t13 = new Matrix[] {};
        Assertions.assertNull(Matrix.normalize(t13, true));

        // Test 6: Array with arbitrary vectors and matrices
        Matrix[] t14 = new Matrix[] {t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12};
        Assertions.assertArrayEquals(new Matrix[] {t3, t4, t3, t4, t5, t6, t7, t8, t9,
                new Matrix(new double[][] {
                        {3 / Math.sqrt(10), 2 / Math.sqrt(20), -6 / Math.sqrt(61), -7 / Math.sqrt(53)},
                        {1 / Math.sqrt(10), 4 / Math.sqrt(20), 5 / Math.sqrt(61), -2 / Math.sqrt(53)}
                }),
                new Matrix(new double[][] {
                        {1}
                }),
                new Matrix(new double[][] {
                        {2 / Math.sqrt(127), 5 / Math.sqrt(132)},
                        {-5 / Math.sqrt(127), 1 / Math.sqrt(132)},
                        {1 / Math.sqrt(127), 4 / Math.sqrt(132)},
                        {4 / Math.sqrt(127), 9 / Math.sqrt(132)},
                        {9 / Math.sqrt(127), -3 / Math.sqrt(132)}
                })}, Matrix.normalize(t14, true));
        Assertions.assertArrayEquals(new Matrix[] {t3, t4, t3, t4, t5, t6, t7, t8, t9,
                new Matrix(new double[][] {
                        {3 / Math.sqrt(98), 2 / Math.sqrt(98), -6 / Math.sqrt(98), -7 / Math.sqrt(98)},
                        {1 / Math.sqrt(46), 4 / Math.sqrt(46), 5 / Math.sqrt(46), -2 / Math.sqrt(46)}
                }),
                new Matrix(new double[][] {
                        {1}
                }),
                new Matrix(new double[][] {
                        {2 / Math.sqrt(29), 5 / Math.sqrt(29)},
                        {-5 / Math.sqrt(26), 1 / Math.sqrt(26)},
                        {1 / Math.sqrt(17), 4 / Math.sqrt(17)},
                        {4 / Math.sqrt(97), 9 / Math.sqrt(97)},
                        {9 / Math.sqrt(90), -3 / Math.sqrt(90)}
                })}, Matrix.normalize(t14, false));

    }

    @Test
    void traceTest() {
        // Test 1: Square Matrices
        Matrix t1 = new Matrix();
        Matrix t2 = Matrix.diag(new double[] {4, -2, 5});
        Matrix t3 = Matrix.zeroMatrix(2, 2);
        Matrix t4 = new Matrix(new double[][] {
                {-6}
        });
        Matrix t5 = new Matrix(new double[][] {
                {3, -4, 1},
                {1, -9, 5},
                {6, 5, 7}
        });
        Assertions.assertEquals(3, Matrix.trace(t1));
        Assertions.assertEquals(7, Matrix.trace(t2));
        Assertions.assertEquals(0, Matrix.trace(t3));
        Assertions.assertEquals(-6, Matrix.trace(t4));
        Assertions.assertEquals(1, Matrix.trace(t5));

        // Test 2: Non-square Matrices
        Matrix t6 = Matrix.zeroMatrix(3, 4);
        Matrix t7 = Matrix.zeroMatrix(5, 2);
        Matrix t8 = new Matrix(new double[][] {
                {2},
                {4},
                {6}
        });
        Matrix t9 = new Matrix(new double[][] {
                {4, 5, 2},
                {-5, 1, 2}
        });
        Matrix t10 = new Matrix(new double[][] {
                {4, 2, -7},
                {-2, -3, 5},
                {1, -3, 6},
                {7, 0, 9}
        });
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.trace(t6) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.trace(t7) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.trace(t8) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.trace(t9) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.trace(t10) );
    }

    @Test
    void addTest() {
        // Test 1: Valid sizes
        Matrix t1 = new Matrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        });
        Matrix t2 = new Matrix(new double[][] {
                {5, -3, 9},
                {1, 6, 2},
                {1, 9, 0}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {6, -1, 12},
                {5, 11, 8},
                {8, 17, 9}
        }), Matrix.add(t1, t2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {6, -1, 12},
                {5, 11, 8},
                {8, 17, 9}
        }), Matrix.add(t2, t1));

        Matrix t3 = new Matrix(new double[][] {
                {5},
                {-4}
        });
        Matrix t4 = new Matrix(new double[][] {
                {0},
                {-9}
        });
        Assertions.assertEquals(new Matrix(new double[][]{
                {5},
                {-13}
        }), Matrix.add(t3, t4));
        Assertions.assertEquals(new Matrix(new double[][]{
                {5},
                {-13}
        }), Matrix.add(t4, t3));

        Matrix t5 = new Matrix(new double[][] {
                {1, 4, -2, 6}
        });
        Matrix t6 = new Matrix(new double[][] {
                {6, -1, 4, -8}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {7, 3, 2, -2}
        }), Matrix.add(t5, t6));
        Assertions.assertEquals(new Matrix(new double[][] {
                {7, 3, 2, -2}
        }), Matrix.add(t6, t5));

        Matrix t7 = new Matrix(new double[][] {
                {2, -3, -1, -2, 7},
                {8, 9, -3, 5, 6}
        });
        Matrix t8 = new Matrix(new double[][] {
                {5, -1, 4, 5, 9},
                {1, 2, 3, 4, 5}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {7, -4, 3, 3, 16},
                {9, 11, 0, 9, 11}
        }), Matrix.add(t7, t8));
        Assertions.assertEquals(new Matrix(new double[][] {
                {7, -4, 3, 3, 16},
                {9, 11, 0, 9, 11}
        }), Matrix.add(t8, t7));

        Matrix t9 = new Matrix(new double[][] {
                {4, 2, 5},
                {-7, 1, 2},
                {0, 0, 8},
                {-9, -4, -5}
        });
        Matrix t10 = new Matrix(new double[][] {
                {-2, 7, 8},
                {1, 0, -2},
                {7, 3, 4},
                {-7, 9, -1}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {2, 9, 13},
                {-6, 1, 0},
                {7, 3, 12},
                {-16, 5, -6}
        }), Matrix.add(t9, t10));
        Assertions.assertEquals(new Matrix(new double[][] {
                {2, 9, 13},
                {-6, 1, 0},
                {7, 3, 12},
                {-16, 5, -6}
        }), Matrix.add(t10, t9));

        // Test 2: Mismatched sizes
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.add(t1, t3) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.add(t1, t5) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.add(t1, t7) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.add(t1, t9) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.add(t3, t5) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.add(t3, t7) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.add(t3, t9) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.add(t5, t7) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.add(t5, t9) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.add(t7, t9) );
    }

    @Test
    void scaleTest() {
        // Test 1: Matrices (not much else to use here)
        Matrix t1 = new Matrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        });
        Assertions.assertEquals(Matrix.zeroMatrix(3, 3), Matrix.scale(t1, 0));
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        }), Matrix.scale(t1, 1));

        Matrix t2 = new Matrix(new double[][] {
                {5},
                {-4}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {-12.5},
                {10}
        }), Matrix.scale(t2, -2.5));
        Assertions.assertEquals(new Matrix(new double[][] {
                {10},
                {-8}
        }), Matrix.scale(t2, 2));

        Matrix t3 = new Matrix(new double[][] {
                {1, 4, -2, 6}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {3, 12, -6, 18}
        }), Matrix.scale(t3, 3));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-3.3, -13.2, 6.6, -19.8}
        }), Matrix.scale(t3, -3.3));

        Matrix t4 = new Matrix(new double[][] {
                {2, -3, -1, -2, 7},
                {8, 9, -3, 5, 6}
        });
        Assertions.assertEquals(Matrix.zeroMatrix(2, 5), Matrix.scale(t4, 0));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-2, 3, 1, 2, -7},
                {-8, -9, 3, -5, -6}
        }), Matrix.scale(t4, -1));

        Matrix t5 = new Matrix(new double[][] {
                {4, 2, 5},
                {-7, 1, 2},
                {0, 0, 8},
                {-9, -4, -5}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {28, 14, 35},
                {-49, 7, 14},
                {0, 0, 56},
                {-63, -28, -35}
        }), Matrix.scale(t5, 7));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-20, -10, -25},
                {35, -5, -10},
                {0, 0, -40},
                {45, 20, 25}
        }), Matrix.scale(t5, -5));

    }

    @Test
    void subTest() {
        // Test 1: Valid sizes
        Matrix t1 = new Matrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        });
        Matrix t2 = new Matrix(new double[][] {
                {5, -3, 9},
                {1, 6, 2},
                {1, 9, 0}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {-4, 5, -6},
                {3, -1, 4},
                {6, -1, 9}
        }), Matrix.sub(t1, t2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {4, -5, 6},
                {-3, 1, -4},
                {-6, 1, -9}
        }), Matrix.sub(t2, t1));

        Matrix t3 = new Matrix(new double[][] {
                {5},
                {-4}
        });
        Matrix t4 = new Matrix(new double[][] {
                {0},
                {-9}
        });
        Assertions.assertEquals(new Matrix(new double[][]{
                {5},
                {5}
        }), Matrix.sub(t3, t4));
        Assertions.assertEquals(new Matrix(new double[][]{
                {-5},
                {-5}
        }), Matrix.sub(t4, t3));

        Matrix t5 = new Matrix(new double[][] {
                {1, 4, -2, 6}
        });
        Matrix t6 = new Matrix(new double[][] {
                {6, -1, 4, -8}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {-5, 5, -6, 14}
        }), Matrix.sub(t5, t6));
        Assertions.assertEquals(new Matrix(new double[][] {
                {5, -5, 6, -14}
        }), Matrix.sub(t6, t5));

        Matrix t7 = new Matrix(new double[][] {
                {2, -3, -1, -2, 7},
                {8, 9, -3, 5, 6}
        });
        Matrix t8 = new Matrix(new double[][] {
                {5, -1, 4, 5, 9},
                {1, 2, 3, 4, 5}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {-3, -2, -5, -7, -2},
                {7, 7, -6, 1, 1}
        }), Matrix.sub(t7, t8));
        Assertions.assertEquals(new Matrix(new double[][] {
                {3, 2, 5, 7, 2},
                {-7, -7, 6, -1, -1}
        }), Matrix.sub(t8, t7));

        Matrix t9 = new Matrix(new double[][] {
                {4, 2, 5},
                {-7, 1, 2},
                {0, 0, 8},
                {-9, -4, -5}
        });
        Matrix t10 = new Matrix(new double[][] {
                {-2, 7, 8},
                {1, 0, -2},
                {7, 3, 4},
                {-7, 9, -1}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {6, -5, -3},
                {-8, 1, 4},
                {-7, -3, 4},
                {-2, -13, -4}
        }), Matrix.sub(t9, t10));
        Assertions.assertEquals(new Matrix(new double[][] {
                {-6, 5, 3},
                {8, -1, -4},
                {7, 3, -4},
                {2, 13, 4}
        }), Matrix.sub(t10, t9));

        // Test 2: Mismatched sizes
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.sub(t1, t3) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.sub(t1, t5) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.sub(t1, t7) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.sub(t1, t9) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.sub(t3, t5) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.sub(t3, t7) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.sub(t3, t9) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.sub(t5, t7) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.sub(t5, t9) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.sub(t7, t9) );
    }

    @Test
    void multTest() {
        // Test 1: Valid sizes
        Matrix t1 = new Matrix();
        Matrix t2 = new Matrix(new double[][] {
                {3, 5, 1},
                {-3, 6, 4},
                {-7, -8, 0}
        });
        Matrix t3 = new Matrix(new double[][] {
                {9},
                {8},
                {7}
        });
        Assertions.assertEquals(t2, Matrix.mult(t1, t2));
        Assertions.assertEquals(t3, Matrix.mult(t1, t3));
        Assertions.assertEquals(new Matrix(new double[][] {
                {74},
                {49},
                {-127}
        }), Matrix.mult(t2, t3));

        Matrix t4 = new Matrix(new double[][] {
                {2, 3},
                {1, -5}
        });
        Matrix t5 = new Matrix(new double[][] {
                {4, 3, 6},
                {1, -2, 3}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {11, 0, 21},
                {-1, 13, -9}
        }), Matrix.mult(t4, t5));

        Matrix t6 = new Matrix(new double[][] {
                {2, -5, 0},
                {-1, 3, -4},
                {6, -8, -7},
                {-3, 0, 9}
        });
        Matrix t7 = new Matrix(new double[][] {
                {4, -6},
                {7, 1},
                {3, 2}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {-27, -17},
                {5, 1},
                {-53, -58},
                {15, 36}
        }), Matrix.mult(t6, t7));

        // Test 2: Mismatched sizes
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.mult(t3, t2) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.mult(t5, t4) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.mult(t7, t6) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.mult(t1, t6) );
        Assertions.assertThrows( MatrixSizeMismatchException.class, () -> Matrix.mult(t7, t1) );

    }

    @Test
    void powerTest() {
        // Test 1: Square matrix, positive power
        Matrix t1 = new Matrix(new double[][] {
                {5, -3, 1},
                {0, 7, -4},
                {0, 0, -2}
        });
        Assertions.assertEquals(t1, Matrix.power(t1, 1));
        Assertions.assertEquals(new Matrix(new double[][] {
                {25, -36, 15},
                {0, 49, -20},
                {0, 0, 4}
        }), Matrix.power(t1, 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {3125, -20523, 8971},
                {0, 16807, -7484},
                {0, 0, -32}
        }), Matrix.power(t1, 5));

        Matrix t2 = new Matrix(new double[][] {
                {0, 0, 0, 0},
                {1, 3, -2, 1},
                {2, 6, 1, 5},
                {0, 1, 4, 2}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {0, 0, 0, 0},
                {-1, -2, -4, -5},
                {8, 29, 9, 21},
                {9, 29, 10, 25}
        }), Matrix.power(t2, 2));
        Assertions.assertEquals(new Matrix(new double[][] {
                {0, 0, 0, 0},
                {-10847, -37869, -7938, -26495},
                {27986, 97798, 19473, 67717},
                {34768, 121473, 24580, 84370}
        }), Matrix.power(t2, 7));

        // Test 2: Square matrix, invertible, negative power
        Assertions.assertEquals(new Matrix(new double[][] {
                {1.0/25.0, 36.0/1225.0, -3.0/980.0},
                {0, 1.0/49.0, 5.0/49.0},
                {0, 0, 1.0/4.0}
        }), Matrix.power(t1, -2));

        Matrix t3 = new Matrix(new double[][] {
                {1, 0},
                {0, 1},
                {2, 3},
                {-1, 4}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {1113.0/219488.0, -109.0/219488.0},
                {-109.0/219488.0, 23.0/219488.0}
        }), Matrix.power(Matrix.mult(Matrix.transpose(t3), t3), -3));

        // Test 3: Square matrix, non-invertible, negative power
        Matrix t4 = Matrix.diag(new double[] {2, 0, -7});
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.power(t4, -2) );

        Matrix t5 = new Matrix(new double[][] {
                {4, 6, -7, 1},
                {0, 0, -4, 9},
                {0, 0, 2, 5},
                {0, 0, 0, 2}
        });
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.power(t5, -1) );

        Matrix t6 = new Matrix(new double[][] {
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 10},
                {11, 12, 13, 14, 15},
                {16, 17, 18, 19, 20},
                {21, 22, 23, 24, 25}
        });
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.power(t6, -4) );

        // Test 4: Non-square
        Matrix t7 = new Matrix(new double[][] {
                {1, 2, 1},
                {0, 1, 3},
                {2, 5, 5},
                {1, 1, -2}
        });
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.power(t7, -1) );

        Matrix t8 = new Matrix(new double[][] {
                {1, 0},
                {0, 1},
                {2, 3},
                {-1, 4}
        });
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.power(t8, -3) );

        Matrix t9 = new Matrix(new double[][] {
                {1, 3, 5, 7},
                {3, 5, 7, 9},
                {5, 7, 9, 1}
        });
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.power(t9, -2) );
    }

    @Test
    void rowRedTest() {
        // Test 1: Full Rank Matrices
        Matrix t1 = new Matrix();
        Assertions.assertEquals(t1, Matrix.rowRed(t1, false)[0]);
        Assertions.assertEquals(t1, Matrix.rowRed(t1, true)[0]);

        Matrix t2 = new Matrix(new double[][] {
                {2, 3},
                {4, 1}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {2, 3},
                {0, -5}
        }), Matrix.rowRed(t2, false)[0]);
        Assertions.assertEquals(new Matrix(2), Matrix.rowRed(t2, true)[0]);

        Matrix t3 = new Matrix(new double[][] {
                {1, -1, 2, 4},
                {2, 1, -1, 1},
                {-1, 2, 3, 7}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, -1, 2, 4},
                {0, 3, -5, -7},
                {0, 0, 20.0/3.0, 40.0/3.0}
        }), Matrix.rowRed(t3, false)[0]);
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 0, 0, 1},
                {0, 1, 0, 1},
                {0, 0, 1, 2}
        }), Matrix.rowRed(t3, true)[0]);

        Matrix t4 = new Matrix(new double[][] {
                {1, 0},
                {0, 1},
                {2, 3},
                {-1, 4}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 0},
                {0, 1},
                {0, 0},
                {0, 0}
        }), Matrix.rowRed(t4, false)[0]);
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 0},
                {0, 1},
                {0, 0},
                {0, 0}
        }), Matrix.rowRed(t4, true)[0]);

        // Test 2: Zero rows / Zero columns / Zero Matrices
        Matrix t5 = Matrix.zeroMatrix(3, 3);
        Matrix t6 = Matrix.zeroMatrix(4, 2);
        Matrix t7 = Matrix.zeroMatrix(3, 5);
        Assertions.assertEquals(t5, Matrix.rowRed(t5, false)[0]);
        Assertions.assertEquals(t5, Matrix.rowRed(t5, true)[0]);
        Assertions.assertEquals(t6, Matrix.rowRed(t6, false)[0]);
        Assertions.assertEquals(t6, Matrix.rowRed(t6, true)[0]);
        Assertions.assertEquals(t7, Matrix.rowRed(t7, false)[0]);
        Assertions.assertEquals(t7, Matrix.rowRed(t7, true)[0]);

        Matrix t8 = new Matrix(new double[][] {
                {0, 0, 0, 0},
                {1, 3, -2, 1},
                {2, 6, 1, 5},
                {0, 1, 4, 2}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 3, -2, 1},
                {0, 1, 4, 2},
                {0, 0, 5, 3},
                {0, 0, 0, 0}
        }), Matrix.rowRed(t8, false)[0]);
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 0, 0, 3.4},
                {0, 1, 0, -0.4},
                {0, 0, 1, 0.6},
                {0, 0, 0, 0}
        }), Matrix.rowRed(t8, true)[0]);

        Matrix t9 = new Matrix(new double[][] {
                {2, 0, 0, 4, 2},
                {0, 1, 0, -1, 1},
                {4, -1, 0, 9, 3},
                {1, 2, 0, 0, 3},
                {3, 0, 0, 6, 3}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {2, 0, 0, 4, 2},
                {0, 1, 0, -1, 1},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        }), Matrix.rowRed(t9, false)[0]);
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 0, 0, 2, 1},
                {0, 1, 0, -1, 1},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        }), Matrix.rowRed(t9, true)[0]);

        Matrix t10 = new Matrix(new double[][] {
                {0, 2, 4, -2},
                {0, 1, 2, -1},
                {0, -3, -6, 3},
                {0, 0, 0, 0}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {0, 2, 4, -2},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        }), Matrix.rowRed(t10, false)[0]);
        Assertions.assertEquals(new Matrix(new double[][] {
                {0, 1, 2, -1},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        }), Matrix.rowRed(t10, true)[0]);

        // Test 3: Rank-deficient Matrices
        Matrix t11 = new Matrix(new double[][] {
                {1, -2, 3},
                {-2, 4, -6}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, -2, 3},
                {0, 0, 0}
        }), Matrix.rowRed(t11, false)[0]);
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, -2, 3},
                {0, 0, 0}
        }), Matrix.rowRed(t11, true)[0]);

        Matrix t12 = new Matrix(new double[][] {
                {1, 2, 3},
                {2, 5, 7},
                {3, 7, 10}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 2, 3},
                {0, 1, 1},
                {0, 0, 0}
        }), Matrix.rowRed(t12, false)[0]);
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 0, 1},
                {0, 1, 1},
                {0, 0, 0}
        }), Matrix.rowRed(t12, true)[0]);

        Matrix t13 = new Matrix(new double[][] {
                {3, 6, 9},
                {1, 2, 3},
                {-2, -4, -6}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {3, 6, 9},
                {0, 0, 0},
                {0, 0, 0}
        }), Matrix.rowRed(t13, false)[0]);
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 2, 3},
                {0, 0, 0},
                {0, 0, 0}
        }), Matrix.rowRed(t13, true)[0]);

        Matrix t14 = new Matrix(new double[][] {
                {1, 2, 1},
                {0, 1, 3},
                {2, 5, 5},
                {1, 1, -2}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 2, 1},
                {0, 1, 3},
                {0, 0, 0},
                {0, 0, 0}
        }), Matrix.rowRed(t14, false)[0]);
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 0, -5},
                {0, 1, 3},
                {0, 0, 0},
                {0, 0, 0}
        }), Matrix.rowRed(t14, true)[0]);

        Matrix t15 = new Matrix(new double[][] {
                {1, 3, 5, 7},
                {3, 5, 7, 9},
                {5, 7, 9, 1}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 3, 5, 7},
                {0, -4, -8, -12},
                {0, 0, 0, -10}
        }), Matrix.rowRed(t15, false)[0]);
        Assertions.assertEquals(new Matrix(new double[][] {
                {1, 0, -1, 0},
                {0, 1, 2, 0},
                {0, 0, 0, 1}
        }), Matrix.rowRed(t15, true)[0]);
    }

    @Test
    void inverseTest() {
        // Test 1: Square, Invertible
        Matrix t1 = new Matrix(new double[][] {
                {5, -3, 1},
                {0, 7, -4},
                {0, 0, -2}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {1.0/5.0, 3.0/35.0, -1.0/14.0},
                {0, 1.0/7.0, -2.0/7.0},
                {0, 0, -1.0/2.0}
        }), Matrix.inverse(t1));

        Matrix t2 = Matrix.diag(new double[] {4, 1, -6});
        Assertions.assertEquals(Matrix.diag(new double[] {1.0/4.0, 1, -1.0/6.0}), Matrix.inverse(t2));

        Matrix t3 = new Matrix(new double[][] {
                {0, 1, 2},
                {1, 0, 3},
                {4, -3, 8}
        });
        Assertions.assertEquals(new Matrix(new double[][] {
                {-9.0/2.0, 7, -3.0/2.0},
                {-2, 4, -1},
                {3.0/2.0, -2, 1.0/2.0}
        }), Matrix.inverse(t3));

        // Test 2: Square, Non-invertible
        Matrix t4 = Matrix.diag(new double[] {2, 0, -7});
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.inverse(t4) );

        Matrix t5 = new Matrix(new double[][] {
                {4, 6, -7, 1},
                {0, 0, -4, 9},
                {0, 0, 2, 5},
                {0, 0, 0, 2}
        });
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.inverse(t5) );

        Matrix t6 = new Matrix(new double[][] {
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 10},
                {11, 12, 13, 14, 15},
                {16, 17, 18, 19, 20},
                {21, 22, 23, 24, 25}
        });
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.inverse(t6) );

        // Test 3: Non-square
        Matrix t7 = new Matrix(new double[][] {
                {1, 2, 1},
                {0, 1, 3},
                {2, 5, 5},
                {1, 1, -2}
        });
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.inverse(t7) );

        Matrix t8 = new Matrix(new double[][] {
                {1, 0},
                {0, 1},
                {2, 3},
                {-1, 4}
        });
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.inverse(t8) );

        Matrix t9 = new Matrix(new double[][] {
                {1, 3, 5, 7},
                {3, 5, 7, 9},
                {5, 7, 9, 1}
        });
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.inverse(t9) );
    }

    @Test
    void detTest() {
        // Test 1: Square, Triangular
        Matrix t1 = new Matrix();
        Assertions.assertEquals(1, Matrix.det(t1));

        Matrix t2 = Matrix.zeroMatrix(4, 4);
        Assertions.assertEquals(0, Math.abs(Matrix.det(t2)));

        Matrix t3 = Matrix.diag(new double[] {5, -2, 0});
        Assertions.assertEquals(0, Math.abs(Matrix.det(t3)));

        Matrix t4 = new Matrix(new double[][] {
                {5, -3, 1},
                {0, 7, -4},
                {0, 0, -2}
        });
        Assertions.assertEquals(-70, Matrix.det(t4));

        Matrix t5 = new Matrix(new double[][] {
                {6, 0, 0},
                {-3, 3, 0},
                {7, 8, 9}
        });
        Assertions.assertEquals(162, Matrix.det(t5));

        // Test 2: Square, Non-Triangular
        Matrix t6 = new Matrix(new double[][] {
                {2, 3},
                {4, 1}
        });
        Assertions.assertEquals(-10, Matrix.det(t6));

        Matrix t7 = new Matrix(new double[][] {
                {1, 2, 3},
                {2, 5, 7},
                {3, 7, 10}
        });
        Assertions.assertEquals(0, Math.abs(Matrix.det(t7)));

        Matrix t8 = new Matrix(new double[][] {
                {0, 0, 0, 0},
                {1, 3, -2, 1},
                {2, 6, 1, 5},
                {0, 1, 4, 2}
        });
        Assertions.assertEquals(0, Math.abs(Matrix.det(t8)));

        Matrix t9 = new Matrix(new double[][] {
                {0, 1},
                {2, 3}
        });
        Assertions.assertEquals(-2,  Matrix.det(t9));

        Matrix t10 = new Matrix(new double[][] {
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 10},
                {11, 12, 13, 14, 15},
                {16, 17, 18, 19, 20},
                {21, 22, 23, 24, 25}
        });
        Assertions.assertEquals(0, Math.abs(Matrix.det(t10)));

        // Test 3: Non-square
        Matrix t11 = new Matrix(new double[][] {
                {1, 2, 1},
                {0, 1, 3},
                {2, 5, 5},
                {1, 1, -2}
        });
        Matrix t12 = new Matrix(new double[][] {
                {1, 3, 5, 7},
                {3, 5, 7, 9},
                {5, 7, 9, 1}
        });
        Matrix t13 = new Matrix(new double[][] {
                {1, 0},
                {0, 1},
                {2, 3},
                {-1, 4}
        });
        Matrix t14 = new Matrix(new double[][] {
                {4},
                {-9},
                {0}
        });
        Matrix t15 = new Matrix(new double[][] {
                {1, -5, 2}
        });
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.det(t11) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.det(t12) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.det(t13) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.det(t14) );
        Assertions.assertThrows( InvalidMatrixException.class, () -> Matrix.det(t15) );
    }


    //-------------------------------------------------------------------------------------------------------
    // SUBSPACE TESTS

    @Test
    void dimensionTest() {}

    @Test
    void columnSpaceTest() {}

    @Test
    void rankTest() {}

    @Test
    void nullSpaceTest() {}

    @Test
    void nullityTest() {}

    //-------------------------------------------------------------------------------------------------------
    // SYSTEM SOLVER TESTS

    @Test
    void solveTest() {
        // Test 1: Consistent Systems (various ranks)
        // Test 2: Inconsistent Systems
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
    // EIGENSTUFF TEST

    @Test
    void eigenvaluesTest() {}

    @Test
    void eigenvectorsTest() {}

    @Test
    void singularValuesTest() {}


    //-------------------------------------------------------------------------------------------------------
    // MATRIX FACTORIZATION TESTS

    @Test
    void QRTest() {}

    @Test
    void diagonalizeTest() {}

    @Test
    void SVDTest() {}


    //-------------------------------------------------------------------------------------------------------
    // MISC TESTS

    // not really sure how to test these tbh, they behave as I want them to, so I don't think it's necessary
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