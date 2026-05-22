package org.example;

public class Main {
    public static void main(String[] args) {

        double[][] test = new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        Matrix m1 = new Matrix(test);
        Matrix m2 = new Matrix();
        System.out.println(m1 + "\n");
        System.out.println(Matrix.getRow(m1, 2) + "\n");
        System.out.println(Matrix.getCol(m1, 2) + "\n");
        System.out.println(Matrix.add(m1, m2));
    }
}