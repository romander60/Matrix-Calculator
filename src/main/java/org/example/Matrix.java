/**
 * A library containing a bunch of matrix operations. Matrices are represented as 2D arrays of doubles.
 *
 * Author: romander60  
 * Last updated: May 22, 2026
 */



package org.example;

import java.util.Arrays;

public class Matrix {

    // The values in the matrix
    private final double[][] entries;
    // The number of rows in the matrix
    private final int rows;
    // The number of columns in the matrix
    private final int cols;
    // True if this matrix is 1 x n
    private boolean rowVec;
    // True if this matrix is m x 1
    private boolean colVec;
    // True if this matrix is 1 x 1
    private boolean number;
    // The value within which roundoff errors are tolerated
    private static final double tol = 0.005;

//-------------------------------------------------------------------------------------------------------------
// MATRIX GENERATORS

    /**
     * Generates a new Matrix object representing the 3x3 identity matrix.
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
     * Generates the nxn identity matrix.
     */
    public Matrix(int n) {
        assert n > 0;

        double[][] entries = new double[n][n]; // all entries are initially as 0.0
        for (int i = 0; i < n; i++) {
            entries[i][i] = 1;
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
     * Generates a new Matrix object whose elements are determined by entries. Each array in entries is
     * a row of the resulting matrix.
     * @throws InvalidMatrixException if any of the rows are empty or if any given pair of rows do
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
     * @return the n-dimensional column zero vector.
     */
    public static Matrix zeroColVec(int n) {
        return new Matrix(new double[n][1]);
    }


    /**
     * @return the n-dimensional row zero vector.
     */
    public static Matrix zeroRowVec(int n) {
        return new Matrix(new double[1][n]);
    }


    /**
     * @return the mxn zero matrix.
     */
    public static Matrix zeroMatrix(int m, int n) {
        return new Matrix(new double[m][n]);
    }


    /**
     * Returns the diagonal matrix containing the elements of diags.
     * @param diags the array containing the entries of the resulting diagonal matrix.
     * @return a diagonal matrix whose diagonal entries are those from diags.
     */
    public static Matrix diag(double[] diags) {
        double[][] entries = new double[diags.length][diags.length];

        for (int i = 0; i < diags.length; i++) {
            entries[i][i] = diags[i];
        }

        return new Matrix(entries);
    }

//-------------------------------------------------------------------------------------------------------------
// BOOLEAN OPERATIONS

    /**
     * @return "true" if v is a row vector.
     */
    public static boolean isRowVec(Matrix v) {
        return v.rowVec;
    }


    /**
     * @return "true" if v is a column vector.
     */
    public static boolean isColVec(Matrix v) {
        return v.colVec;
    }


    /**
     * @return "true" if v1 and v2 are either both column vectors or both row vectors.
     */
    public static boolean sameType(Matrix v1, Matrix v2) {
        return (isRowVec(v1) && isRowVec(v2)) || (isColVec(v1) && isColVec(v2));
    }


    /**
     * @return "true" if n is a 1x1 matrix (a number).
     */
    public static boolean isNumber(Matrix n) {
        return n.number;
    }


    /**
     * @return "true" if A has the same number of rows as it does columns.
     */
    public static boolean isSquare(Matrix A) {
        return A.rows == A.cols;
    }


    /**
     * @return "true" if A is a diagonal matrix; that is, if all A's off-diagonal entries are zero.
     */
    public static boolean isDiagonal(Matrix A) {
        if (!isSquare(A)) { return false;}

        // leaving room for roundoff error
        for (int i = 0; i < rows(A); i++) {
            for (int j = 0; i < cols(A); j++) {
                if (i != j && Math.abs(A.entries[i][j]) >= tol) { return false;}
            }
        }
        return true;
    }


    /**
     * @return "true" if A^-1 exists.
     */
    public static boolean isInvertible(Matrix A) {
        return isSquare(A) && rowRed(A).equals( new Matrix(rows(A)) );
    }

    /**
     * @return "true" if A is equal to its transpose.
     */
    public static boolean isSymmetric(Matrix A) {
        return A.equals( transpose(A) );
    }


    /**
     * @return "true" if v is a unit vector; that is, if it's magnitude is 1.
     * @throws InvalidMatrixException if v isn't a column or row vector.
     */
    public static boolean isUnit(Matrix v) throws InvalidMatrixException {
        if (!isColVec(v) && !isRowVec(v)) {
            throw new InvalidMatrixException("Input must be a column or row vector.");
        }

        // a bit of tolerance for computational instability
        return Math.abs(1 - magn(v)) < tol;
    }


    /**
     * @return "true" if v1 and v2 are orthogonal; that is, if their dot product is zero.
     * @throws InvalidMatrixException if v1 and v2 aren't vectors of the same type.
     * @throws MatrixSizeMismatchException if v1 and v2 aren't column vectors with the same size.
     */
    public static boolean areOrtho(Matrix v1, Matrix v2) throws InvalidMatrixException, MatrixSizeMismatchException {
        // a bit of tolerance for computational stability
        return sameType(v1, v2) && Math.abs(dot(v1, v2)) < tol;
    }


    /**
     * @return "true" if A has orthonormal columns; that is, if A^T * A = I.
     */
    public static boolean isOrtho(Matrix A) {
        return mult(transpose(A), A).equals( new Matrix(cols(A)) );
    }


    /**
     * @return "true" if A contains col.
     */
    public static boolean hasCol(Matrix A, Matrix col) {
        if (col.rows != A.rows) { return false; }
        for (int j = 0; j < A.cols; j++) {
            if (!col.equals( getCol(A, j + 1) )) { return false; }
        }

        return true;
    }


    /**
     * @return "true" if A contains row.
     */
    public static boolean hasRow(Matrix A, Matrix row) {
        if (row.cols != A.cols) { return false; }
        for (int i = 0; i < A.rows; i++) {
            if (!row.equals( getCol(A, i + 1) )) { return false; }
        }

        return true;
    }

//-------------------------------------------------------------------------------------------------------------
// GETTERS

    /**
     * @return the number of rows in "A".
     */
    public static int rows(Matrix A) {
        return A.rows;
    }


    /**
     * @return the number of columns in "A".
     */
    public static int cols(Matrix A) {
        return A.cols;
    }


    /**
     * Returns a new Matrix object representing the ith row of "A". Row indexing begins at 1; that is,
     * to get the first (top) row of A, you would call Matrix.getRow(A, 1).
     * @param A the matrix whose row is being obtained.
     * @param i the index of the row being obtained, with indexing beginning at 1.
     * @return the matrix representing the ith row of A.
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
     * Returns a new Matrix object representing the jth column of "A". Column indexing begins at 1; that is,
     * to get the first (leftmost) column of A, you would call Matrix.getCol(A, 1).
     * @param A the matrix whose column is being obtained.
     * @param j the index of the column being obtained, with indexing beginning at 1.
     * @return the matrix representing the jth column of A.
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
     * Returns a_ij.
     * @param A the matrix in question.
     * @param i the row that the desired entry is in, with indexing beginning at 1.
     * @param j the column that the desired entry is in.
     * @return the ij-entry of A.
     * @throws AssertionError if i isn't a positive integer less than or equal to A's row count, or
     * if j isn't a positive integer less than or equal to A's column count.
     */
    public static double getEntry(Matrix A, int i, int j) throws AssertionError {
        if ( (i <= 0 || i > A.rows) || (j <= 0 || j > A.cols) ) {
            throw new AssertionError("Second and third inputs must be valid indices.");
        }

        return A.entries[i - 1][j - 1];
    }


//-------------------------------------------------------------------------------------------------------------
// MATRIX OPERATIONS

    /**
     * Creates a new matrix with the same entries as A.
     * @return a distinct copy of A.
     */
    public static Matrix copy(Matrix A) {
        return new Matrix(A.entries);
    }


    /**
     * Transposes v. Helper for Matrix.transpose().
     * @return the transpose of v.
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
     * Generates the partitioned matrix [A | v], where v is assumed to be a column vector.
     * @param A the matrix that's being appended to.
     * @param v the column vector that's getting appended to A.
     * @return a new Matrix object representing A, but with v as an augmented column.
     * @throws MatrixSizeMismatchException if v doesn't have as many rows as A.
     */
    private static Matrix appendCol(Matrix A, Matrix v) throws MatrixSizeMismatchException {
        if (rows(v) != rows(A)) { throw new MatrixSizeMismatchException("Inputs must have an equal number of rows."); }

        double[][] entries = new double[A.rows][A.cols + 1];
        for (int i = 0; i < A.rows; i++) {
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
     * Generates a new Matrix object representing A with v added on as a row at the bottom,
     * where v is assumed to be a row vector.
     * @param A the matrix that's being appended to.
     * @param v the row vector that's getting appended to A.
     * @return a new Matrix object representing A, but with v as an augmented row.
     * @throws MatrixSizeMismatchException if v doesn't have as many columns as A.
     */
    private static Matrix appendRow(Matrix A, Matrix v) throws MatrixSizeMismatchException {
        if (cols(v) != cols(A)) {
            throw new MatrixSizeMismatchException("Inputs must have an equal number of columns.");
        }

        double[][] entries = new double[A.rows + 1][A.cols];
        for (int i = 0; i < A.rows + 1; i++) {
            for (int j = 0; j < A.cols; j++) {
                if (i != A.rows) {
                    entries[i][j] = A.entries[i][j];
                }
                else {
                    entries[i][j] = v.entries[0][i];
                }
            }
        }

        return new Matrix(entries);
    }


    /**
     * Returns the partitioned matrix [A | B].
     * @param A the matrix that's being appended to.
     * @param B the matrix that's getting appended to A.
     * @return a new Matrix object representing A, but with B as an augmented matrix.
     * @throws MatrixSizeMismatchException if A and B don't have an equal number of rows.
     */
    public static Matrix append(Matrix A, Matrix B) throws MatrixSizeMismatchException {
        if (rows(A) != rows(B)) {throw new MatrixSizeMismatchException("Matrices must have an equal number of rows.");}

        Matrix appended = copy(A);
        for (int j = 0; j < B.cols; j++) {
            appended = appendCol(appended, getCol(B, j + 1));
        }
        return appended;
    }

    /**
     * Returns A^T.
     * @return a new Matrix object representing the transpose of A.
     */
    public static Matrix transpose(Matrix A) {
        Matrix transposed = transposeVec(getRow(A, 1));
        for (int i = 1; i < rows(A); i++) {
            transposed = appendCol( transposed, transposeVec(getRow(A, i + 1)) );
        }
        return transposed;
    }


    /**
     * Returns A with its colNum'th column replaced by newCol.
     * @param A the matrix whose column is being replaced.
     * @param colNum the index of the column being replaced, with indexing beginning at 1.
     * @param newCol the column that will replace the colNum'th row of A.
     * @return A new matrix with the colNum'th column of A replaced with newCol.
     * @throws AssertionError if colNum isn't a positive integer less than or equal to the number of columns in A.
     * @throws MatrixSizeMismatchException if colNum doesn't have as many entries as A does rows.
     */
    public static Matrix replaceCol(Matrix A, int colNum, Matrix newCol) throws AssertionError, MatrixSizeMismatchException {
        if (colNum <= 0 || colNum > A.cols) {
            throw new AssertionError("2nd input must be a positive number less than or equal to " +
                    "the number of columns in the 1st input.");
        }

        if (newCol.rows != A.rows) {
            throw new MatrixSizeMismatchException("3rd input must have the same number of rows as the 1st input.");
        }

        if (newCol.equals(getRow(A, colNum))) {return copy(A);}

        double[][] replacedEntries = Arrays.copyOf(A.entries, A.entries.length);

        for (int j = 0; j < A.cols; j++) {
            if (j == colNum - 1) {
                for (int i = 0; i < A.rows; i++) {
                    replacedEntries[i][j] = newCol.entries[i][0];
                }
            }
        }

        return new Matrix(replacedEntries);
    }


    /**
     * Returns A with its rowNum'th row replaced by newRow.
     * @param A the matrix whose row is being replaced.
     * @param rowNum the index of the row being replaced, with indexing beginning at 1.
     * @param newRow the row that will replace the rowNum'th row of A.
     * @return A new matrix with the rowNum'th row of A replaced with newRow.
     * @throws AssertionError if rowNum isn't a positive integer less than or equal to the number of rows in A.
     * @throws MatrixSizeMismatchException if rowNum doesn't have as many entries as A does columns.
     */
    public static Matrix replaceRow(Matrix A, int rowNum, Matrix newRow) throws AssertionError, MatrixSizeMismatchException {
        if (rowNum <= 0 || rowNum > A.rows) {
            throw new AssertionError("2nd input must be a positive number less than or equal to " +
                    "the number of rows in the 1st input.");
        }

        if (newRow.cols != A.cols) {
            throw new MatrixSizeMismatchException("3rd input must have the same number of columns as the 1st input.");
        }

        if (newRow.equals(getRow(A, rowNum))) {return copy(A);}

        double[][] replacedEntries = Arrays.copyOf(A.entries, A.entries.length);

        for (int i = 0; i < A.rows; i++) {
            if (i == rowNum - 1) {
                replacedEntries[i] = Arrays.copyOf(newRow.entries[0], newRow.entries[0].length);
            }
        }

        return new Matrix(replacedEntries);
    }


    /**
     * Returns A with its col1'th and col2'th columns swapped.
     * @param A the matrix whose columns are being swapped
     * @param col1 the index of the first column being swapped, with indexing starting at 1
     * @param col2 the index of the second column being swapped
     * @return A new matrix with the col1'th and col2'th columns swapped.
     * @throws AssertionError if either col1 or col2 isn't a positive number less than or
     * equal to the number of columns in A.
     */
    public static Matrix swapCols(Matrix A, int col1, int col2) throws AssertionError {
        if ( (col1 <= 0 || col1 > A.cols) || (col2 <= 0 || col2 > A.cols) ) {
            throw new AssertionError("Both index inputs must be " +
                "positive numbers less than or equal to the number of columns in the first input.");
        }

        if (col1 == col2) {return copy(A);}

        Matrix swapped = copy(A);
        Matrix firstCol = getCol(swapped, col1);
        Matrix secondCol = getCol(swapped, col2);
        swapped = replaceCol(swapped, col1, secondCol);
        swapped = replaceCol(swapped, col2, firstCol);

        return swapped;
    }

    /**
     * Returns A with its row1'th and row2'th rows swapped.
     * @param A the matrix whose rows are being swapped
     * @param row1 the index of the first row being swapped, with indexing starting at 1
     * @param row2 the index of the second row being swapped
     * @return A new matrix with the row1'th and row2'th rows swapped.
     * @throws AssertionError if either row1 or row2 isn't a positive number less than or
     * equal to the number of rows in A.
     */
    public static Matrix swapRows(Matrix A, int row1, int row2) throws AssertionError {
        if ( (row1 <= 0 || row1 > A.rows) || (row2 <= 0 || row2 > A.rows) ) {
            throw new AssertionError("Both index inputs must be " +
                    "positive numbers less than or equal to the number of rows in the first input.");
        }

        if (row1 == row2) {return copy(A);}

        Matrix swapped = copy(A);
        Matrix firstRow = getCol(swapped, row1);
        Matrix secondRow = getCol(swapped, row2);
        swapped = replaceCol(swapped, row1, secondRow);
        swapped = replaceCol(swapped, row2, firstRow);

        return swapped;
    }


    /**
     * @return the length of v.
     * @throws InvalidMatrixException if v isn't a column or row vector.
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
     * Returns v1 (dot) v2.
     * @param v1 the first vector in the dot product.
     * @param v2 the second vector in the dot product.
     * @return v1 (dot) v2.
     * @throws InvalidMatrixException if v1 and v2 aren't both column vectors or row vectors.
     * @throws MatrixSizeMismatchException if v1 and v2 aren't vectors with the same size.
     */
    public static double dot(Matrix v1, Matrix v2) throws InvalidMatrixException, MatrixSizeMismatchException {
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
     * Returns the trace of A.
     * @param A the matrix in question.
     * @return the trace of A.
     * @throws InvalidMatrixException if A isn't square.
     */
    public static double trace(Matrix A) throws InvalidMatrixException {
        if (!isSquare(A)) {
            throw new InvalidMatrixException("Input must be a square matrix.");
        }

        double trace = 0;
        for (int i = 0; i < A.rows; i++) {
            trace += A.entries[i][i];
        }

        return trace;
    }

    /**
     * Returns A + B.
     * @param A the first matrix in the sum.
     * @param B the second matrix in the sum.
     * @return a new Matrix object representing A + B.
     * @throws MatrixSizeMismatchException if "A" and "B" do not have the same number of rows and columns.
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
     * Returns cA.
     * @param A the matrix being scaled.
     * @param c the scale factor.
     * @return a new Matrix object representing cA.
     */
    public static Matrix scale(Matrix A, double c) {
        double[][] scaled = new double[rows(A)][cols(A)];
        for (int i = 0; i < rows(A); i++) {
            for (int j = 0; j < cols(A); j++) {
                scaled[i][j] = c * A.entries[i][j];
            }
        }

        return new Matrix(scaled);
    }

    /**
     * Returns AB. To obtain BA, call Matrix.mult(B, A).
     * @param A the left matrix in the product.
     * @param B the right matrix in the product.
     * @return a new Matrix object representing AB.
     * @throws MatrixSizeMismatchException if the number of columns in "A" isn't equal to the number of rows in "B".
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
     * Returns A^n.
     * @param A the matrix being exponentiated.
     * @param n the exponent.
     * @return a new Matrix object representing A raised to the power of n.
     * @throws InvalidMatrixException if
     * 1) A isn't square, in general
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
     * Returns rref(A).
     * @param A the matrix being row-reduced.
     * @return a new Matrix object representing the reduced row echelon form of A.
     */
    public static Matrix rowRed(Matrix A) {
        if ( A.equals(zeroMatrix(A.rows, A.cols)) || A.equals(new Matrix(A.cols)) ) {return copy(A);}

        Matrix reduced = copy(A);
        Matrix zeRow = zeroRowVec(A.cols);
        Matrix zeCol = zeroColVec(A.rows);
        int curRowIndex = 1;

        System.out.println(reduced);

        for (int j = 1; j < A.cols + 1; j++) {
            Matrix curCol = getCol(reduced, j);
            Matrix replacements = zeroMatrix(A.rows, A.cols);

            if ( curCol.equals(zeCol) ) {
                continue;
            }
            else if ( getEntry(curCol, curRowIndex, 1) == 0 ) {
                for (int i = A.rows; i > curRowIndex; i--) {
                    if ( getEntry(reduced, i, j) != 0 ) {
                        swapRows(reduced, curRowIndex, i);
                    }
                }
                if ( getEntry(curCol, curRowIndex, 1) == 0 ) { // if all entries below are still zero somehow
                    System.out.println("Current pivot is a zero; moving over one place.\n");
                    continue;
                }
            }

            System.out.println("Current row: " + getRow(reduced, curRowIndex));
            System.out.println("Pivot: " + getEntry(curCol, curRowIndex, 1));
            Matrix normedRow = scale(getRow(reduced, curRowIndex), 1 / getEntry(curCol, curRowIndex, 1));
            reduced = replaceRow( reduced, curRowIndex, normedRow );
            for (int i = 1; i < A.rows + 1; i++) {
                if (i != curRowIndex) {
                    replacements = replaceRow( replacements, i, scale( normedRow, -1 * getEntry(reduced, i, j) ) );
                }
            }
            reduced = add(reduced, replacements);
            System.out.println("Iteration " + curRowIndex + ": \n" + reduced + "\n");

            curRowIndex++;
        }


        return reduced;
    }

    /**
     * Returns A^-1.
     * @param A the matrix being inverted.
     * @return a new Matrix object representing the inverse of A.
     * @throws InvalidMatrixException if A is singular.
     */
    public static Matrix inverse(Matrix A) throws InvalidMatrixException {
        if (!isInvertible(A)) {throw new InvalidMatrixException("Input must be invertible.");}
        if (isDiagonal(A)) {
            Matrix inv = copy(A);
            for (int i = 0; i < A.rows; i++) {
                inv = replaceRow(inv, i, scale(getRow(inv, i), 1 / A.entries[i][i]) );
            }
            return inv;
        }

        // Augment A with the identity matrix, then row reduce it.
        Matrix reduced = rowRed( append(new Matrix(cols(A)), A) );

        // Take the n + 1 to 2n columns of the augmented matrix; this matrix is A^-1.
        Matrix inv = getCol(reduced, cols(A) + 1);
        for (int j = cols(A) + 2; j < 2 * cols(A); j++) {
            inv = append(inv, getCol(inv, j));
        }

        return inv;
    }

//-------------------------------------------------------------------------------------------------------------
// MISC OPERATIONS

    /**
     * A.toString() returns the string representation of A.
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


    /**
     * A.equals(B) returns "true" is A and B's corresponding entries are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Matrix matrix = (Matrix) o;

        if (this.rows != matrix.rows || this.cols != matrix.cols) { return false;}

        // leaving some room for roundoff error
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if ( Math.abs( this.entries[i][j] - matrix.entries[i][j] ) >= tol ) {return false;}
            }
        }

        return true;
    }
}
