/**
 * A library containing a bunch of matrix operations. Matrices are represented as 2D arrays of doubles.
 *
 * Author: romander60  
 * Overall time spent on project: ~15 hours
 *
 * Last updated: May 22, 2026
 */



package org.example;

import java.util.ArrayList;
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
     * @throws AssertionError if n isn't a positive integer.
     */
    public Matrix(int n) throws AssertionError {
        if (n <= 0) {
            throw new AssertionError("Input must be a positive integer.");
        }

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
     * @return the mxn zero matrix.
     * @throws InvalidMatrixException if m and/or n isn't a positive integer.
     */
    public static Matrix zeroMatrix(int m, int n) throws InvalidMatrixException {
        if (m <= 0 || n <= 0) {
            throw new InvalidMatrixException("");
        }

        return new Matrix(new double[m][n]);
    }


    /**
     * Returns the diagonal matrix containing the elements of diags.
     * @param diags the array containing the entries of the resulting diagonal matrix.
     * @return a diagonal matrix whose diagonal entries are those from diags.
     * @throws InvalidMatrixException if diags is empty.
     */
    public static Matrix diag(double[] diags) throws InvalidMatrixException {
        if (diags.length == 0) {
            throw new InvalidMatrixException("diags must not be empty.");
        }

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
     * @return "true" if v is a vector.
     */
    public static boolean isVec(Matrix v) {
        return v.colVec || v.rowVec;
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
     * @return "true" if A is an upper triangular matrix; that is, if all the entries below A's main diagonal are zero.
     */
    public static boolean isUpperTriangular(Matrix A) {
        if (!isSquare(A)) { return false; }

        for (int j = 1; j < A.cols + 1; j++) {
            Matrix curCol = getCol(A, j);
            for (int i = j; i < A.rows; i++) {
                if ( Math.abs(getEntry(curCol, i, 1)) >= tol ) { return false; }
            }
        }

        return true;
    }


    /**
     * @return "true" if A is a lower triangular matrix; that is, if all the entries above A's main diagonal are zero.
     */
    public static boolean isLowerTriangular(Matrix A) {
        if (!isSquare(A)) { return false; }

        // Conveniently, a matrix is lower triangular if all the entries
        // to the right of the main diagonal are zero.
        for (int i = 1; i < A.rows + 1; i++) {
            Matrix curRow = getRow(A, i);
            for (int j = i; j < A.cols; j++) {
                if ( Math.abs(getEntry(curRow, 1, j)) >= 0 ) { return false; }
            }
        }

        return true;
    }


    /**
     * @return "true" if A is a triangular matrix; that is, if all the entries either above or below
     * A's main diagonal are zero.
     */
    public static boolean isTriangular(Matrix A) {
        return isLowerTriangular(A) || isUpperTriangular(A);
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
        return isSquare(A) && rowRed(A).equals( new Matrix(A.rows) );
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
        if (!isVec(v)) {
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
            if (col.equals( getCol(A, j + 1) )) { return true; }
        }

        return false;
    }


    /**
     * @return "true" if A contains row.
     */
    public static boolean hasRow(Matrix A, Matrix row) {
        if (row.cols != A.cols) { return false; }
        for (int i = 0; i < A.rows; i++) {
            if (row.equals( getCol(A, i + 1) )) { return true; }
        }

        return false;
    }


    /**
     * @return "true" if A and B have the same dimension.
     */
    public static boolean sameSize(Matrix A, Matrix B) {
        return (A.rows == B.rows) && (A.cols == B.cols);
    }

    /**
     * @return "true" if all the matrices in mats have the same dimension
     */
    public static boolean sameSize(Matrix[] mats) {
        int rowCount = mats[0].rows;
        int colCount = mats[0].cols;
        for (int i = 1; i < mats.length; i++) {
            if (mats[i].rows != rowCount || mats[i].cols != colCount) {
                return false;
            }
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


    /**
     * Returns an array containing A's columns
     * @param A the matrix in question
     * @return an array containing A's columns in the order they appear in A from left to right
     */
    public static Matrix[] getCols(Matrix A) {
        Matrix[] cols = new Matrix[A.cols];
        for (int i = 0; i < A.cols; i++) {
            cols[i] = getCol(A, i + 1);
        }
        return cols;
    }


    /**
     * Returns an array containing A's rows
     * @param A the matrix in question
     * @return an array containing A's rows in the order they appear in A from top to bottom
     */
    public static Matrix[] getRows(Matrix A) {
        Matrix[] rows = new Matrix[A.rows];
        for (int i = 0; i < A.rows; i++) {
            rows[i] = getRow(A, i + 1);
        }
        return rows;
    }


    /**
     * Returns the submatrix of A whose upper leftmost entry is the (topLeftRow, topLeftCol)-entry of A and whose
     * bottom rightmost entry is the (botRightRow, botRightCol)-entry of A.
     * @param A the matrix in question
     * @param topLeftRow the row index of the top left entry of the resulting submatrix, with indexing beginning at 1
     * @param topLeftCol the column index of the top left entry of the resulting submatrix
     * @param botRightRow the row index of the bottom right entry of the resulting submatrix
     * @param botRightCol the column index of the bottom right entry of the resulting submatrix
     * @return a new Matrix object representing the submatrix of A whose upper leftmost entry is
     * the (topLeftRow, topLeftCol)-entry of A and whose bottom rightmost entry is
     * the (botRightRow, botRightCol)-entry of A.
     * @throws AssertionError if 1) any of the indices aren't positive numbers within the dimensions of A
     */
    public static Matrix getSubmatrix(Matrix A, int topLeftRow, int topLeftCol, int botRightRow, int botRightCol)
            throws AssertionError {
        if ( (topLeftRow <= 0 || topLeftRow > A.rows) ||
                (topLeftCol <= 0 || topLeftCol > A.cols) ||
                (botRightRow <= 0 || botRightRow > A.rows) ||
                (botRightCol <= 0 || botRightCol > A.cols) ) {
            throw new AssertionError("All indices must be within the dimensions of A.");
        }

        if (topLeftRow == 1 && topLeftCol == 1 && botRightRow == A.rows && botRightCol == A.cols) {
            return copy(A);
        }

        if (topLeftRow == botRightRow && topLeftCol == botRightCol) {
            return new Matrix(new double[][] { {getEntry(A, topLeftRow, topLeftCol)} });
        }

        if (topLeftRow > botRightRow && topLeftCol < botRightCol) {
            return getSubmatrix(A, botRightRow, topLeftCol, topLeftRow, botRightCol);
        }

        else if (topLeftRow < botRightRow && topLeftCol > botRightCol) {
            return getSubmatrix(A, topLeftRow, botRightCol, botRightRow, topLeftCol);
        }

        else if (topLeftRow > botRightRow && topLeftCol > botRightCol) {
            return getSubmatrix(A, botRightRow, botRightCol, topLeftRow, topLeftCol);
        }

        double[][] subEntries = new double[botRightRow - topLeftRow + 1][botRightCol - topLeftCol + 1];

        for (int i = 0; i < botRightRow - topLeftCol + 1; i++) {
            for (int j = 0; j < botRightCol - topLeftCol + 1; j++) {
                subEntries[i][j] = A.entries[topLeftRow - 1 + i][topLeftCol - 1 + j];
            }
        }

        return new Matrix(subEntries);

    }

//-------------------------------------------------------------------------------------------------------------
// MATRIX MANIPULATION

    /**
     * Generates the partitioned matrix [A | v], where v is assumed to be a column vector.
     * @param A the matrix that's being appended to.
     * @param v the column vector that's getting appended to A.
     * @return a new Matrix object representing A, but with v as an augmented column.
     * @throws MatrixSizeMismatchException if v isn't a column vector with as many rows as A.
     */
    private static Matrix appendCol(Matrix A, Matrix v) throws MatrixSizeMismatchException {
        if (v.rows != A.rows || !isColVec(v)) {
            throw new MatrixSizeMismatchException("v must be a column vector with as many rows as A.");
        }

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
     * @throws MatrixSizeMismatchException if v isn't a row vector with as many columns as A.
     */
    private static Matrix appendRow(Matrix A, Matrix v) throws MatrixSizeMismatchException {
        if (v.cols != A.cols || !isRowVec(v)) {
            throw new MatrixSizeMismatchException("v must be a row vector with as many columns as A.");
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
     * Transposes v. Helper for Matrix.transpose().
     * @return the transpose of v.
     */
    private static Matrix transposeVec(Matrix v) {
        assert isVec(v);

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
        Matrix firstRow = getRow(swapped, row1);
        Matrix secondRow = getRow(swapped, row2);
        swapped = replaceRow(swapped, row1, secondRow);
        swapped = replaceRow(swapped, row2, firstRow);

        return swapped;
    }


    /**
     * Removes the colInd'th column of A.
     * @param A the matrix in question
     * @param colInd the index of the column being removed, with indexing beginning at 1.
     * @return a new Matrix object representing A, but with its colInd'th column removed.
     * @throws AssertionError if colInd isn't a positive integer less than or equal to the number of columns in A.
     * @throws InvalidMatrixException if A is a column vector.
     */
    public static Matrix removeCol(Matrix A, int colInd) throws AssertionError, InvalidMatrixException {
        if (isColVec(A)) {
            throw new InvalidMatrixException("A is already a column vector; " +
                    "this operation would just delete the vector.");
        }

        if (colInd <= 0 || colInd > A.cols) {
            throw new AssertionError("colInd must be a positive integer less than or equal to " +
                    "the number of columns in A.");
        }

        if (colInd == 1) {
            Matrix removed = getCol(A, 2);
            for (int j = 3; j < A.cols + 1; j++) {
                removed = appendCol(removed, getCol(A, j));
            }
            return removed;
        }

        else {
            Matrix removed = getCol(A, 1);
            for (int j = 2; j < A.cols + 1; j++) {
                if (j != colInd) {
                    removed = appendCol(removed, getCol(A, j));
                }
            }
            return removed;
        }
    }


    /**
     * Removes the rowInd'th row of A.
     * @param A the matrix in question
     * @param rowInd the index of the row being removed, with indexing beginning at 1.
     * @return a new Matrix object representing A, but with its rowInd'th row removed.
     * @throws AssertionError if rowInd isn't a positive integer less than or equal to the number of rows in A.
     * @throws InvalidMatrixException if A is a row vector.
     */
    public static Matrix removeRow(Matrix A, int rowInd) throws AssertionError, InvalidMatrixException {
        if (isRowVec(A)) {
            throw new InvalidMatrixException("A is already a row vector; " +
                    "this operation would just delete the vector.");
        }

        if (rowInd <= 0 || rowInd > A.rows) {
            throw new AssertionError("colInd must be a positive integer less than or equal to " +
                    "the number of columns in A.");
        }

        if (rowInd == 1) {
            Matrix removed = getRow(A, 2);
            for (int i = 3; i < A.rows + 1; i++) {
                removed = appendRow(removed, getRow(A, i));
            }
            return removed;
        }

        else {
            Matrix removed = getRow(A, 1);
            for (int i = 2; i < A.cols + 1; i++) {
                if (i != rowInd) {
                    removed = appendRow(removed, getRow(A, i));
                }
            }
            return removed;
        }
    }


    /**
     * Returns A, but padded with filler until it's mxn.
     * @param A the matrix being padded
     * @param m the number of rows the padded matrix will have
     * @param n the number of columns the padded matrix will have
     * @param filler the number A is being padded with
     * @return a new Matrix object representing A, but padded with filler into an mxn matrix.
     * @throws AssertionError if 1) m isn't a positive integer greater than or equal to the number of rows in A,
     * 2) n isn't a positive integer greater than or equal to the number of columns in A
     */
    public static Matrix pad(Matrix A, int m, int n, double filler) throws AssertionError {
        if (m <= 0 || m < A.rows || n <= 0 || n < A.cols) {
            throw new AssertionError("m and n must be positive integers within the dimensions of A.");
        }

        double[][] newEntries = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i < A.rows && j < A.cols) {
                    newEntries[i][j] = A.entries[i][j];
                }
                else {
                    newEntries[i][j] = filler;
                }
            }
        }

        return new Matrix(newEntries);
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
     * @return the length of v.
     * @throws InvalidMatrixException if v isn't a column or row vector.
     */
    public static double magn(Matrix v) throws InvalidMatrixException {
        if (!isVec(v)) {
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
     * Returns the normalized version of v.
     * @param v the vector being normalized
     * @return a new Matrix object representing the unit vector pointing in the same direction as v.
     * @throws InvalidMatrixException if v isn't a nonzero vector
     */
    public static Matrix normalize(Matrix v) throws InvalidMatrixException {
        if (!isVec(v) || magn(v) <= tol) {
            throw new InvalidMatrixException("Input must be a nonzero vector.");
        }

        return scale(v, 1 / magn(v));
    }

    /**
     * Normalizes each vector in vecs.
     * @param vecs the list of vectors being normalized
     * @return an array of new Matrix objects representing the normalized version of the corresponding vector in vecs,
     * in the other that they appear in vecs; that is, vecs[i] -> normedVecs[i] for each valid i.
     * @throws InvalidMatrixException if vecs doesn't contain only nonzero vectors.
     */
    public static Matrix[] normalize(Matrix[] vecs) throws InvalidMatrixException {
        for (Matrix vec : vecs) {
            if (!isVec(vec) || magn(vec) <= tol) {
                throw new InvalidMatrixException("Each element in vecs must be a nonzero vector.");
            }
        }

        Matrix[] normedVecs = new Matrix[vecs.length];
        for (int i = 0; i < vecs.length; i++) {
            normedVecs[i] = scale(vecs[i], 1 / magn(vecs[i]));
        }
        return normedVecs;
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
     * @throws MatrixSizeMismatchException if A and B do not have the same number of rows and columns.
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
     * Returns A - B.
     * @param A the first matrix in the difference.
     * @param B the second matrix in the difference.
     * @return a new Matrix object representing A - B.
     * @throws MatrixSizeMismatchException if A and B do not have the same number of rows and columns.
     */
    public static Matrix sub(Matrix A, Matrix B) throws MatrixSizeMismatchException {
        if (rows(A) != rows(B) || cols(A) != cols(B)) {
            throw new MatrixSizeMismatchException("Both matrices must have the same size.");
        }

        return add(A, scale(B, -1));
    }


    /**
     * Returns AB.
     * @param A the left matrix in the product.
     * @param B the right matrix in the product.
     * @return a new Matrix object representing the product AB.
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
        Matrix zeCol = zeroMatrix(A.rows, 1);
        int curRowIndex = 1;

//        System.out.println("Initial: \n" + reduced);

        for (int j = 1; j < A.cols + 1; j++) {
            Matrix curCol = getCol(reduced, j);
            Matrix replacements = zeroMatrix(A.rows, A.cols);

            if ( curCol.equals(zeCol) ) {
                continue;
            }
            else if ( getEntry(curCol, curRowIndex, 1) == 0 ) {
                for (int i = A.rows; i > curRowIndex; i--) {
                    if ( getEntry(reduced, i, j) != 0 ) {
                        reduced = swapRows(reduced, curRowIndex, i);
                        curCol = getCol(reduced, j);
//                        System.out.println("Swapped: \n" + reduced);
                    }
                }
            }

//            System.out.println("Current row: " + getRow(reduced, curRowIndex));
//            System.out.println("Pivot: " + getEntry(curCol, curRowIndex, 1));
            Matrix normedRow = scale(getRow(reduced, curRowIndex), 1 / getEntry(curCol, curRowIndex, 1));
            reduced = replaceRow( reduced, curRowIndex, normedRow );
            for (int i = 1; i < A.rows + 1; i++) {
                if (i != curRowIndex) {
                    replacements = replaceRow( replacements, i, scale( normedRow, -1 * getEntry(reduced, i, j) ) );
                }
            }
            reduced = add(reduced, replacements);
//            System.out.println("Replaced: \n" + reduced);

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


    /**
     * Returns det(A).
     * @param A the matrix in question
     * @return the determinant of A.
     * @throws InvalidMatrixException if A isn't square.
     *
     * NOT COMPLETE
     */
    public static double det(Matrix A) throws InvalidMatrixException {
        if (!isSquare(A)) {
            throw new InvalidMatrixException("Input must be a square matrix.");
        }

        if (isTriangular(A)) {
            double det = 1;
            for (int i = 0; i < A.rows; i++) {
                det *= A.entries[i][i];
            }

            return det;
        }

        else {
            // Duplicating the logic for row reduction because Java doesn't let me return multiple
            // quantities of different types

            double detFactor = 1;

            Matrix reduced = copy(A);
            Matrix zeCol = zeroMatrix(A.rows, 1);
            int curRowIndex = 1;

            System.out.println("Initial: \n" + reduced);

            for (int j = 1; j < A.cols + 1; j++) {
                Matrix curCol = getCol(reduced, j);
                Matrix replacements = zeroMatrix(A.rows, A.cols);

                if ( curCol.equals(zeCol) ) {
                    continue;
                }
                else if ( getEntry(curCol, curRowIndex, 1) == 0 ) {
                    for (int i = A.rows; i > curRowIndex; i--) {
                        if ( getEntry(reduced, i, j) != 0 ) {
                            reduced = swapRows(reduced, curRowIndex, i);
                            curCol = getCol(reduced, j);
//                            System.out.println("Swapped: \n" + reduced);
                            detFactor *= -1;
                        }
                    }
                }

//                System.out.println("Current row: " + getRow(reduced, curRowIndex));
//                System.out.println("Pivot: " + getEntry(curCol, curRowIndex, 1));
                Matrix normedRow = scale(getRow(reduced, curRowIndex), 1 / getEntry(curCol, curRowIndex, 1));
                detFactor *= getEntry(curCol, curRowIndex, 1);
                reduced = replaceRow( reduced, curRowIndex, normedRow );
                for (int i = 1; i < A.rows + 1; i++) {
                    if (i != curRowIndex) {
                        replacements = replaceRow( replacements, i, scale( normedRow, -1 * getEntry(reduced, i, j) ) );
                    }
                }
                reduced = add(reduced, replacements);
//                System.out.println("Replaced: \n" + reduced);

                curRowIndex++;
            }

            System.out.println(reduced);
            return detFactor;
        }
    }

//-------------------------------------------------------------------------------------------------------------
// SOLVING SYSTEMS / SUBSPACES

    /**
     * Returns an orthonormal basis for the solution set to the equation Ax = b.
     * @param A the coefficient matrix
     * @param b the target vector
     * @return an array of Matrix objects representing an orthonormal basis for the solution set to Ax = b.
     * @throws MatrixSizeMismatchException if b isn't a column vector with as many rows as A.
     *
     * NOT COMPLETE
     */
    public static Matrix[] solve(Matrix A, Matrix b) throws MatrixSizeMismatchException{
        //TODO: implement this
        if (b.rows != A.rows || !isColVec(b)) {
            throw new MatrixSizeMismatchException("b must be a column vector with as many rows as A.");
        }
        return null;
    }


    /**
     * Returns the dimension of the subspace spanned by the vectors in vecs.
     * @param vecs the set of vectors spanning some subspace
     * @return the dimension of the spanned subspace
     * @throws MatrixSizeMismatchException if the vectors in vecs aren't column vectors with the same size.
     */
    public static int dimension(Matrix[] vecs) throws MatrixSizeMismatchException {
        if (!isColVec(vecs[0]) || !sameSize(vecs)) {
            throw new MatrixSizeMismatchException("vecs must contain columns vectors with the same size.");
        }

        if (vecs.length == 1 && vecs[0].equals(zeroMatrix(vecs[0].rows, 1))) {
            return 0;
        }

        return -1;
    }


    /**
     * Returns an orthonormal basis for Col(A).
     * @param A the matrix in question
     * @return an array of Matrix objects representing an orthonormal basis for the column space of A.
     */
    public static Matrix[] columnSpace(Matrix A) {
        return null;
    }


    /**
     * Returns the dimension of Col(A).
     * @param A the matrix in question.
     * @return the dimension of Col(A)
     */
    public static int rank(Matrix A) {
        return dimension(columnSpace(A));
    }


    /**
     * Returns an orthonormal basis for Nul(A).
     * @param A the matrix in question
     * @return an array containing Matrix objects representing an orthonormal basis for the null space of A.
     */
    public static Matrix[] nullSpace(Matrix A) {
        return null;
    }


    /**
     * Returns the dimension of Nul(A).
     * @param A the matrix in question.
     * @return the dimension of Nul(A).
     */
    public static int nullity(Matrix A) {
        return dimension(nullSpace(A));
    }

//-------------------------------------------------------------------------------------------------------------
// ORTHOGONALITY AND LEAST SQUARES

    /**
     * Returns the orthogonal projecton of v onto projVec.
     * @param v the vector being projected
     * @param projVec the vector onto which v is being projected
     * @return a new Matrix object representing the orthogonal projection of v onto projVec.
     * @throws InvalidMatrixException if 1) v and/or projVec isn't a column vector 2) projVec is the zero vector.
     * @throws MatrixSizeMismatchException if v and projVec don't have the same size.
     */
    public static Matrix proj(Matrix v, Matrix projVec) {
        if (!isColVec(v) || !isColVec(projVec)) {
            throw new InvalidMatrixException("Inputs must be column vectors of the same size.");
        }
        if (projVec.equals(zeroMatrix(projVec.rows, 1))) {
            throw new InvalidMatrixException("projVec must be nonzero.");
        }
        if (!sameSize(v, projVec)) {
            throw new MatrixSizeMismatchException("Vectors must be the same size.");
        }

        double scaleFactor = dot(v, projVec) / dot(projVec, projVec);
        return scale(projVec, 1 / scaleFactor);
    }


    /**
     * Returns the orthogonal projection of v onto the subspace spanned by the vectors in vecs
     * @param v the vector being projected
     * @param vecs the vectors spanning the subspace
     * @return the orthogonal projection of v onto the span on the vectors in vecs
     * @throws InvalidMatrixException if vecs doesn't contain only column vectors of the same size
     * @throws MatrixSizeMismatchException if v isn't a column vector with the same size as those in vecs
     */
    public static Matrix proj(Matrix v, Matrix[] vecs) throws InvalidMatrixException, MatrixSizeMismatchException {
        if (!isColVec(vecs[0]) || !sameSize(vecs)) {
            throw new MatrixSizeMismatchException("All elements of vecs must be column vectors with the same size.");
        }
        if (!sameSize(v, vecs[0])) {
            throw new MatrixSizeMismatchException("v must have the same dimension as the vectors in vecs.");
        }

        Matrix proj = zeroMatrix(vecs[0].rows, 1);
        for (int i = 0; i < vecs.length; i++) {
            double scaleFactor = dot(v, vecs[i]) / (dot(vecs[i], vecs[i]));
            proj = add(proj, scale(vecs[i], scaleFactor));
        }

        return proj;
    }


    /**
     * Returns an orthonormal basis for the subspace spanned by the vectors in vecs.
     * @param vecs the set of vectors that the GSP is being applied to
     * @return an orthonormal basis for the subspace spanned by the vectors in vecs
     * @throws InvalidMatrixException if vecs doesn't contain only column vectors of the same size
     * @throws MatrixSizeMismatchException if the vectors in vecs aren't column vectors of the same size.
     */
    public static Matrix[] gs(Matrix[] vecs) throws InvalidMatrixException, MatrixSizeMismatchException {
        if (!isColVec(vecs[0])) {
            throw new InvalidMatrixException("vecs must contain only column vectors of the same size.");
        }
        if (!sameSize(vecs)) {
            throw new MatrixSizeMismatchException("All elements of vecs must be column vectors of the same size.");
        }

        if (vecs.length == 1 && vecs[0].equals(zeroMatrix(vecs[0].rows, 1))) {
            return new Matrix[] {zeroMatrix(vecs[0].rows, 1)};
        }

        // initializing the vectors
        ArrayList<Matrix> onb = new ArrayList<>();
        onb.add(vecs[0]);

        // Computing the projections
        for (int i = 1; i < vecs.length; i++) {
            Matrix[] arr = new Matrix[onb.size()];
            arr = onb.toArray(arr);
            Matrix proj = proj(vecs[i], arr);
            if (!proj.equals(vecs[i])) {
                // only add to the onb if the projection isn't equal to the vector itself
                // (i.e., if the vector isn't already in the span of the rest)
                // if this check wasn't here, we'd be risking division by zero
                Matrix ortho = sub(vecs[i], proj);
                onb.add(ortho);
            }
        }

        Matrix[] arr = new Matrix[onb.size()];
        arr = onb.toArray(arr);
        return normalize(arr);
    }


    /**
     * Returns an orthonormal basis for the orthogonal complement of the subspace spanned by the vectors in vecs.
     * @param vecs the vectors spanning some subspace
     * @return a basis for vecs-perp
     * @throws MatrixSizeMismatchException if the vectors in vecs aren't column vectors of the same size.
     *
     * NOT COMPLETE
     */
    public static Matrix[] wPerp(Matrix[] vecs) throws MatrixSizeMismatchException {
        //TODO: implement this
        if (!sameSize(vecs)) {
            throw new MatrixSizeMismatchException("vecs must contain column vectors of the same size.");
        }

        return null;
    }


    /**
     * Returns an orthonormal basis of the set of least squares solutions to the equation Ax = b.
     * @param A the coefficient matrix
     * @param b the target vector
     * @return an array of Matrix objects representing an orthonormal basis for the set of
     * least squares solutions to Ax = b.
     * @throws MatrixSizeMismatchException if b isn't a column vector with as many rows as A.
     *
     * NOT COMPLETE
     */
    public static Matrix[] leastSquares(Matrix A, Matrix b) throws MatrixSizeMismatchException {
        //TODO: implement this
        if (b.rows != A.rows || !isColVec(b)) {
            throw new MatrixSizeMismatchException("b must be a column vector with as many rows as A.");
        }
        return null;
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

        if (this.rows != matrix.rows ||
                this.cols != matrix.cols ||
                this.rowVec != matrix.rowVec ||
                this.colVec != matrix.colVec ||
                this.number != matrix.number) { return false;}

        // leaving some room for roundoff error
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if ( Math.abs( this.entries[i][j] - matrix.entries[i][j] ) >= tol ) {return false;}
            }
        }

        return true;
    }
}
