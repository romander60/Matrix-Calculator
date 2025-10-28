package org.example;

import java.util.Arrays;

public class Matrix {

    // The values in the matrix
    private final double[][] entries;
    // The number of rows in the matrix
    private final int rows;
    // The number of columns in the matrix
    private final int cols;
    // True if this matrix is a 1 x n matrix, false otherwise
    private boolean rowVec;
    // True if this matrix is an m x 1 matrix, false otherwise
    private boolean colVec;
    // True if this matrix is a 1 x 1 matrix, false otherwise
    private boolean number;

    /**
     * Matrix() generates a new Matrix object representing the 3x3 identity matrix.
     */
    public Matrix() throws InvalidMatrixException {
        this.entries = new double[][]{
                {1, 0, 0}, {0, 1, 0}, {0, 0, 1}
        };

        this.rows = 3;
        this.cols = 3;
        this.rowVec = false;
        this.colVec = false;
        this.number = false;
    }


    /**
     * Matrix(entries) generates a new Matrix object whose entries are
     * determines by the "entries" argument. Each array in "entries" is
     * a row of the resulting matrix. Throws InvalidMatrixException if
     * any of the rows are empty or if any given pair of rows do
     * not have the same length.
     */
    public Matrix(double[][] entries) throws InvalidMatrixException {
        this.entries = Arrays.copyOf(entries, entries.length);
        this.rowVec = false;
        this.colVec = false;
        this.number = false;
        int rowLength = entries[0].length;
        if (rowLength == 0) {
            throw new InvalidMatrixException("Matrix rows can't be empty.");
        }

        for (double[] row : entries) {
            if (row.length != rowLength) {
                throw new InvalidMatrixException("All rows in a matrix must " +
                        "have the same number of entries.");
            }
        }

        this.rows = entries.length;
        this.cols = rowLength;
        if (this.rows == 1) {
            this.rowVec = true;
        }
        if (this.cols == 1) {
            this.colVec = true;
        }
        if (this.rowVec && this.colVec) {
            this.number = true;
        }
    }


    /**
     * Matrix.rows(A) returns the number of rows in "A".
     */
    public static int rows(Matrix A) {
        return A.rows;
    }

    /**
     * Matrix.cols(A) returns the number of columns in "A".
     */
    public static int cols(Matrix A) {
        return A.cols;
    }

    /**
     * Matrix.transpose(A) returns a new Matrix object representing
     * the transpose of "A".
     */
    public static Matrix transpose(Matrix A) {
        return new Matrix();
    }

    /**
     * Matrix.getRow(A, i) returns a new Matrix object representing
     * the "i"th row of "A". Row indexing begins at 1.
     */
    public static Matrix getRow(Matrix A, int i) {
        assert (i > 0) && (i <= Matrix.rows(A));

        return new Matrix(new double[][]{ A.entries[i - 1] });
    }

    /**
     * Matrix.getCol(A, j) returns a new Matrix object representing
     * the "j"th column of "A". Column indexing begins at 1.
     */
    public static Matrix getCol(Matrix A, int j) {
        assert (j > 0) && (j <= Matrix.rows(A));

        double[][] col = new double[Matrix.rows(A)][1];

        for (int i = 0; i < Matrix.rows(A); i++) {
            col[i][0] = A.entries[i][j - 1];
        }

        return new Matrix(col);
    }


    /**
     * Matrix.add(A, B) returns a new Matrix object representing A + B.
     * Throws MatrixSizeMismatchException is "A" and "B" do not have the same
     * number of rows and columns.
     */
    public static Matrix add(Matrix A, Matrix B) throws MatrixSizeMismatchException {
        if (Matrix.rows(A) != Matrix.rows(B) || Matrix.cols(A) != Matrix.cols(B)) {
            throw new MatrixSizeMismatchException("Both matrices must have the same dimensions.");
        }

        double[][] sum = new double[Matrix.rows(A)][Matrix.cols(A)];
        for (int i = 0; i < Matrix.rows(A); i++) {
            for (int j = 0; j < Matrix.cols(A); j++) {
                sum[i][j] = A.entries[i][j] + B.entries[i][j];
            }
        }

        return new Matrix(sum);
    }

    public static double dot(Matrix v1, Matrix v2) {
        return 0;
    }

    /**
     * Matrix.mult(A, B) returns a new Matrix object representing AB.
     * To obtain BA, call Matrix.mult(B, A). Throws MatrixSizeMismatchException
     * if the number of columns in "A" isn't equal to the number of rows in "B".
     */
    public static Matrix mult(Matrix A, Matrix B) throws MatrixSizeMismatchException {
        if (Matrix.cols(A) != Matrix.rows(B)) {
            throw new MatrixSizeMismatchException("The number of columns in the first argument " +
                    "must equal the number of rows in the second.");
        }

        double[][] prod = new double[Matrix.rows(A)][Matrix.cols(B)];

        return new Matrix(prod);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        if (this.number) {
            string.append(this.entries[0][0]);
        }
        else {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (j == 0){
                        string.append("[");
                    }
                    string.append(this.entries[i][j]);
                    if (j != cols - 1) {
                        string.append("   ");
                    }
                    else {
                        string.append("]");
                    }
                }
                if (i != rows - 1) {
                    string.append("\n");
                }
            }
        }

        return string.toString();
    }
}
