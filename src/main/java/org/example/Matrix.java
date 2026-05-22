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

//-------------------------------------------------------------------------------------------------------------
// CONSTRUCTORS

    /**
     * Matrix() generates a new Matrix object representing the 3x3 identity matrix.
     */
    public Matrix() {
        this.entries = new double[][]{
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };

        this.rows = 3;
        this.cols = 3;
        this.rowVec = false;
        this.colVec = false;
        this.number = false;
    }


    /**
     * Matrix(n) generates the nxn identity matrix.
     */
    public Matrix(int n) {
        assert n > 0;

        double[][] entries = new double[n][n]; // all entries are initially as 0.0
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) { entries[i][j] = 1;} // only modifying the diagonal entries
            }
        }

        this.entries = Arrays.copyOf(entries, entries.length);
        this.rows = n;
        this.cols = n;
        if (n == 1) {
            this.rowVec = true;
            this.colVec = true;
            this.number = true;
        }
        else {
            this.rowVec = false;
            this.colVec = false;
            this.number = false;
        }
    }


    /**
     * Matrix(entries) generates a new Matrix object whose entries are
     * determined by the "entries" argument. Each array in "entries" is
     * a row of the resulting matrix. Throws InvalidMatrixException if
     * any of the rows are empty or if any given pair of rows do
     * not have the same length.
     */
    public Matrix(double[][] entries) throws InvalidMatrixException {
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

        this.entries = Arrays.copyOf(entries, entries.length);
        this.rowVec = false;
        this.colVec = false;
        this.number = false;
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
     * zeroColVec(n) generates the n-dimensional column zero vector.
     */
    public static Matrix zeroColVec(int n) {
        return new Matrix(new double[n][1]);
    }


    /**
     * zeroRowVec(n) generates the n-dimensional row zero vector.
     */
    public static Matrix zeroRowVec(int n) {
        return new Matrix(new double[1][n]);
    }

//-------------------------------------------------------------------------------------------------------------
// BOOLEAN OPERATIONS

    /**
     * Matrix.isRowVec(v) returns "true" if v is a row vector.
     */
    public static boolean isRowVec(Matrix v) {
        return v.rowVec;
    }


    /**
     * Matrix.isColVec(v) returns "true" if v is a column vector.
     */
    public static boolean isColVec(Matrix v) {
        return v.colVec;
    }


    /**
     * Matrix.sameType(v1, v2) returns "true" if v1 and v2 are either both column vectors or both row vectors.
     */
    public static boolean sameType(Matrix v1, Matrix v2) {
        return (isRowVec(v1) && isRowVec(v2)) || (isColVec(v1) && isColVec(v2));
    }


    /**
     * Matrix.isNumber(n) returns "true" if n is a 1x1 matrix (a number).
     */
    public static boolean isNumber(Matrix n) {
        return n.number;
    }


    /**
     * Matrix.isSquare(A) returns "true" if A has the same number of rows as it does columns.
     */
    public static boolean isSquare(Matrix A) {
        return A.rows == A.cols;
    }


    /**
     * Matrix.isDiagonal(A) returns "true" if A is a diagonal matrix; that is, if
     * all A's off-diagonal entries are zero.
     */
    public static boolean isDiagonal(Matrix A) {
        if (!isSquare(A)) { return false;}

        for (int i = 0; i < rows(A); i++) {
            for (int j = 0; i < cols(A); j++) {
                if (i != j && A.entries[i][j] != 0) { return false;}
            }
        }
        return true;
    }


    /**
     * Matrix.isInvertible(A) returns "true" if A^-1 exists.
     *
     * NOT COMPLETE
     */
    public static boolean isInvertible(Matrix A) {
        //TODO: implement this
        return true;
    }


    /**
     * Matrix.equals(A, B) returns "true" is A and B's corresponding entries are equal.
     */
    public static boolean equals(Matrix A, Matrix B) {
        if (A.rows != B.rows || A.cols != B.cols) { return false;}

        // leaving some room for roundoff error
        for (int i = 0; i < rows(A); i++) {
            for (int j = 0; j < cols(A); j++) {
                if ( Math.abs( A.entries[i][j] - B.entries[i][j] ) >= 0.005 ) {return false;}
            }
        }

        return true;
    }


    /**
     * Matrix.isSymmetric(A) returns "true" if A is equal to its transpose.
     */
    public static boolean isSymmetric(Matrix A) {
        return equals(A, transpose(A));
    }


    /**
     * Matrix.isUnit(v) returns "true" if v is a unit vector; that is, if it's magnitude is 1.
     * Throws InvalidMatrixException if v isn't a column or row vector.
     */
    public static boolean isUnit(Matrix v) throws InvalidMatrixException {
        if (!isColVec(v) && !isRowVec(v)) {
            throw new InvalidMatrixException("Input must be a column or row vector.");
        }

        // a bit of tolerance for computational instability
        return Math.abs(1 - magn(v)) < 0.005;
    }


    /**
     * Matrix.areOrtho(v1, v2) returns "true" if v1 and v2 are orthogonal; that is,
     * if their dot product is zero.
     * Throws InvalidMatrixException if either v1 or v2 isn't a column vector.
     * Throws MatrixSizeMismatchException if v1 and v2 aren't column vectors with the same size.
     */
    public static boolean areOrtho(Matrix v1, Matrix v2) throws InvalidMatrixException, MatrixSizeMismatchException {
        // a bit of tolerance for computational stability
        return Math.abs(dot(v1, v2)) < 0.005;
    }


    /**
     * Matrix.isOrtho(A) returns "true" if A has orthonormal columns; that is, if A^T * A = I.
     */
    public static boolean isOrtho(Matrix A) {
        return equals( mult(transpose(A), A), new Matrix(cols(A)) );
    }

//-------------------------------------------------------------------------------------------------------------
// GETTERS

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
     * Matrix.getRow(A, i) returns a new Matrix object representing
     * the ith row of "A". Row indexing begins at 1; that is,
     * to get the first (top) row of A, you would call Matrix.getRow(A, 1).
     */
    public static Matrix getRow(Matrix A, int i) {
        assert (i > 0) && (i <= Matrix.rows(A));

        double[][] row = new double[1][Matrix.cols(A)];

        for (int j = 0; j < Matrix.cols(A); j++) {
            row[0][j] = A.entries[i - 1][j];
        }
        return new Matrix(row);
    }


    /**
     * Matrix.getCol(A, j) returns a new Matrix object representing
     * the jth column of "A". Column indexing begins at 1; that is,
     * to get the first (leftmost) column of A, you would call Matrix.getCol(A, 1).
     */
    public static Matrix getCol(Matrix A, int j) {
        assert (j > 0) && (j <= Matrix.rows(A));

        double[][] col = new double[Matrix.rows(A)][1];

        for (int i = 0; i < Matrix.rows(A); i++) {
            col[i][0] = A.entries[i][j - 1];
        }
        return new Matrix(col);
    }


//-------------------------------------------------------------------------------------------------------------
// MATRIX OPERATIONS

    /**
     * Matrix.copy(A) creates a new matrix with the same entries as A.
     */
    public static Matrix copy(Matrix A) {
        return new Matrix(A.entries);
    }


    /**
     * transposeVec(v) just transposes v. Helper for Matrix.transpose().
     */
    private static Matrix transposeVec(Matrix v) {
        assert isRowVec(v) || isColVec(v);

        if (isRowVec(v)) {
            double[][] newEntries = new double[cols(v)][1];
            for (int i = 0; i < cols(v); i++) {
                newEntries[i][0] = v.entries[i][0];
            }

            return new Matrix(newEntries);
        }

        else {
            double[][] newEntries = new double[1][rows(v)];
            for (int i = 0; i < cols(v); i++) {
                newEntries[0][i] = v.entries[0][i];
            }
            return new Matrix(newEntries);
        }

    }

    /**
     * appendCol(v, A) creates the partitioned matrix [A | v], where v is assumed to be a column vector.
     * Throws a MatrixSizeMismatchException if v doesn't have as many rows as A.
     */
    private static Matrix appendCol(Matrix v, Matrix A) throws MatrixSizeMismatchException {
        if (rows(v) != rows(A)) { throw new MatrixSizeMismatchException("Inputs must have an equal number of rows."); }

        double[][] entries = new double[A.rows + 1][A.cols + 1];
        for (int i = 0; i < A.rows + 1; i++) {
            for (int j = 0; j < A.cols + 1; j++) {
                if (j != A.cols) {
                    entries[i][j] = A.entries[i][j];
                }
                else {
                    entries[i][j] = v.entries[i][0];
                }
            }
        }

        return new Matrix(entries);
    }


    /**
     * Matrix.append(B, A) generates the partitioned matrix [A | B].
     * Throws MatrixSizeMismatchException if A and B don't have an equal number of rows.
     */
    public static Matrix append(Matrix B, Matrix A) throws MatrixSizeMismatchException {
        if (rows(A) != rows(B)) {throw new MatrixSizeMismatchException("Matrices must have an equal number of rows.");}

        Matrix appended = copy(A);
        for (int j = 0; j < B.cols; j++) {
            appended = appendCol(getCol(B, j + 1), appended);
        }
        return appended;
    }


    /**
     * Matrix.transpose(A) returns a new Matrix object representing
     * the transpose of "A".
     */
    public static Matrix transpose(Matrix A) {
        //TODO: implement this

        Matrix transposed = transposeVec(getRow(A, 1));
        for (int i = 1; i < rows(A); i++) {
            transposed = appendCol( transposeVec(getRow(A, i + 1)) , transposed );
        }
        return transposed;
    }


    /**
     * Matrix.magn(v) returns the length of v.
     * Throws InvalidMatrixException if v isn't a column or row vector.
     */
    public static double magn(Matrix v) throws InvalidMatrixException {
        if (!isColVec(v) && !isRowVec(v)) {
            throw new InvalidMatrixException("Input must be a column or row vector.");
        }

        if (isColVec(v)) {
            double magSquared = 0;
            for (int i = 0; i < rows(v); i++) {
                magSquared += v.entries[i][0] * v.entries[i][0];
            }
            return Math.sqrt(magSquared);
        }

        else {
            double magSquared = 0;
            for (int i = 0; i < cols(v); i++) {
                magSquared += v.entries[0][i] * v.entries[0][i];
            }
            return Math.sqrt(magSquared);
        }
    }


    /**
     * Matrix.dot(v1, v2) returns the dot product of v1 and v2, assuming that
     * both v1 and v2 are columns vectors of equal size.
     * Throws InvalidMatrixException if either of v1 or v2 isn't a column or row vector, or if v1 and v2
     * aren't both column vectors or row vectors.
     * Throws MatrixSizeMismatchException if v1 and v2 aren't vectors with the same size.
     */
    public static double dot(Matrix v1, Matrix v2) throws InvalidMatrixException, MatrixSizeMismatchException {
        if ( !isColVec(v1) || !isColVec(v2) || !isRowVec(v1) || !isRowVec(v2) ) {
            throw new InvalidMatrixException("Both inputs must be vectors.");
        }
        if ( !sameType(v1, v2) ) {
            throw new InvalidMatrixException("Both inputs must be columns vectors or row vectors.");
        }

        double sum = 0;

        if (isColVec(v1)) {
            if (rows(v1) != rows(v2)) {
                throw new MatrixSizeMismatchException("Inputs must both be vectors of the same size.");
            }

            for (int i = 0; i < rows(v1); i++) {
                sum += v1.entries[i][0] * v2.entries[i][0];
            }
        }

        else {
            if (cols(v1) != cols(v2)) {
                throw new MatrixSizeMismatchException("Inputs must both be vectors of the same size.");
            }

            for (int j = 0; j < cols(v1); j++) {
                sum += v1.entries[0][j] * v2.entries[0][j];
            }
        }

        return sum;
    }


    /**
     * Matrix.add(A, B) returns a new Matrix object representing A + B.
     * Throws MatrixSizeMismatchException is "A" and "B" do not have the same
     * number of rows and columns.
     */
    public static Matrix add(Matrix A, Matrix B) throws MatrixSizeMismatchException {
        if (rows(A) != rows(B) || cols(A) != cols(B)) {
            throw new MatrixSizeMismatchException("Both matrices must have the same size.");
        }

        double[][] sum = new double[Matrix.rows(A)][Matrix.cols(A)];
        for (int i = 0; i < Matrix.rows(A); i++) {
            for (int j = 0; j < Matrix.cols(A); j++) {
                sum[i][j] = A.entries[i][j] + B.entries[i][j];
            }
        }

        return new Matrix(sum);
    }

    /**
     * Matrix.mult(A, B) returns a new Matrix object representing AB.
     * To obtain BA, call Matrix.mult(B, A).
     * Throws MatrixSizeMismatchException if the number of columns in "A" isn't equal to the number of rows in "B".
     */
    public static Matrix mult(Matrix A, Matrix B) throws MatrixSizeMismatchException {
        if (cols(A) != rows(B)) {
            throw new MatrixSizeMismatchException("The number of columns in the first argument " +
                    "must equal the number of rows in the second.");
        }

        double[][] prod = new double[rows(A)][cols(B)];
        for (int i = 0; i < rows(A); i++) {
            for (int j = 0; j < cols(B); j++) {
                for (int k = 0; k < cols(A); k++) {
                    prod[i][j] += A.entries[i][k] * B.entries[k][j];
                }
            }
        }

        return new Matrix(prod);
    }

    /**
     * Matrix.power(A, n) returns a new Matrix object representing A^n.
     * Throws InvalidMatrixException if:
     * 1) A isn't square
     * 2) A isn't invertible, if n < 0.
     */
    public static Matrix power(Matrix A, int n) throws InvalidMatrixException {
        if (!isSquare(A)) { throw new InvalidMatrixException("Input must be a square matrix."); }

        if (n == 0) {return new Matrix();}

        else if (n > 0) {
            Matrix product = copy(A);
            for (int i = 0; i < n - 1; i++) {
                product = mult(product, A);
            }
            return product;
        }

        else {
            if (!isInvertible(A)) { throw new InvalidMatrixException("Input must be invertible."); }
            Matrix product = copy(inverse(A));
            for (int i = 0; i < n - 1; i++) {
                product = mult(product, A);
            }
            return product;
        }
    }

    /**
     * Matrix.inverse(A) returns a new Matrix object representing A^-1.
     * Throws InvalidMatrixException if A isn't invertible.
     *
     * NOT COMPLETE
     */
    public static Matrix inverse(Matrix A) {
        //TODO: implement this
        if (!isInvertible(A)) {throw new InvalidMatrixException("Input must be invertible");}

        return new Matrix();
    }
//-------------------------------------------------------------------------------------------------------------
// MISC OPERATIONS

    /**
     * m.toString() returns the string representation of this Matrix object.
     */
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
