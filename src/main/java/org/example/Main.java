package org.example;

import java.util.Arrays;

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
                {1, -2, -9, 5},
                {0, 1, 2, -6},
        };

        double[][] test4 = new double[][] {
                {3, 1},
                {6, 2},
                {0, 2}
        };

        Matrix m = new Matrix(test4);
//        Matrix m1 = Matrix.getSubmatrix(m, 1, 1, Matrix.rows(m), Matrix.cols(m) - 1);
//        Matrix m2 = Matrix.getCol(m, Matrix.cols(m));
//        Matrix[] solved = Matrix.solve(m1, m2, true);
//        System.out.println("Particular solution: \n" + solved[0]);
//        System.out.println("Null space: \n" + solved[1]);
        System.out.println(Matrix.nullity(m));
        System.out.println(Matrix.rank(m));
    }
}