package org.example;

public class Main {
    public static void main(String[] args) {

        double[][] test1 = new double[][] {
                {2, -8, 6, 8},
                {3, -9, 5, 10},
                {-3, 0, 1, -2},
                {1, -4, 0, 6}
        };
        double[][] test2 = new double[][] {
                {1, 3, 5, 7},
                {3, 5, 7, 9},
                {5, 7, 9, 1}
        };
        double[][] test3 = new double[][] {
                {1, 2, 0, 3},
                {2, 4, 1, 4},
        };

        double[][] test4 = new double[][] {
                {3, 1},
                {6, 2},
                {0, 2}
        };
        double[][] test5 = new double[][] {
                {1, 0, 0},
                {1, 1, 0},
                {1, 1, 1},
                {1, 1, 1}
        };
        double[][] test6 = new double[][] {
                {3, 6},
                {4, 8}
        };
        double[][] test7 = new double[][] {
                {4, 0, 2},
                {0, 2, 0},
                {1, 1, 11}
        };
        double[][] test8 = new double[][] {
                {5, 0},
                {2, 1}
        };
        double[][] test9 = new double[][] {
                {1, 3, 3},
                {-3, -5, -3},
                {3, 3, 1}
        };
        double[][] test10 = new double[][] {
                {1, -1},
                {-2, 2},
                {2, -2}
        };

        Matrix m = new Matrix(test3);
        Matrix[] blah = Matrix.rowRed(m, true);
        System.out.println("Final: \n" + blah[0]);
        System.out.println("Det Factors: \n" + blah[1]);
        System.out.println("Pivot Cols: \n" + blah[2]);
//        Matrix m1 = Matrix.getSubmatrix(m, 1, 1, Matrix.rows(m), Matrix.cols(m) - 1);
//        Matrix m2 = Matrix.getCol(m, Matrix.cols(m));
//        Matrix[] ls = Matrix.leastSquares(m1, m2, true);
//        System.out.println("Particular solution: \n" + ls[0]);
//        System.out.println("Null space: \n" + ls[1]);

//        Matrix[] diag = Matrix.diagonalize(m);
//        System.out.println("P: \n" + diag[0]);
//        System.out.println("D: \n" + diag[1]);
//        System.out.println("AP: \n" + Matrix.mult(m, diag[0]));
//        System.out.println("PD: \n" + Matrix.mult(diag[0], diag[1]));
//        System.out.println(Matrix.mult(m, diag[0]).equals(Matrix.mult(diag[0], diag[1])));

//        Matrix[] svd = Matrix.SVD(m);
//        System.out.println("U: \n" + svd[0]);
//        System.out.println("S: \n" + svd[1]);
//        System.out.println("V^T: \n" + Matrix.transpose(svd[2]));
//        Matrix US = Matrix.mult(svd[0], svd[1]);
//        Matrix USVT = Matrix.mult(US, Matrix.transpose(svd[2]));
//        System.out.println("Product: \n" + USVT);
//        System.out.println(USVT.equals(m));
    }
}