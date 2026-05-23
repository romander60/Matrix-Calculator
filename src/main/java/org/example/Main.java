package org.example;

public class Main {
    public static void main(String[] args) {

        double[][] test1 = new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        double[][] test2 = new double[][] {
                {1, 3, 5, 7},
                {3, 5, 7, 9},
                {5, 7, 9, 1}
        };

        Matrix m1 = new Matrix(test1);
        Matrix m2 = new Matrix(test2);
        System.out.println(m1 + "\n");
        //System.out.println(Matrix.getRow(m1, 2) + "\n");
        //System.out.println(Matrix.getCol(m1, 2) + "\n");
        System.out.println(Matrix.rowRed(m2));
    }
}