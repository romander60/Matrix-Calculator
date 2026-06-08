/**
 * A library containing a bunch of matrix operations. Matrices are represented as 2D arrays of doubles.
 *
 * Author: romander60  
 * Overall time spent on project: ~35 hours
 *
 * Last updated: May 31, 2026
 */



package org.example;

import java.lang.reflect.Array;
import java.util.*;

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


    // The value within which roundoff errors are tolerated
    private static final double tol = 0.0001;
    // The number of decimal places quantities should be rounded to
    private static final int decPlaces = 5;

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
        }
        else {
            this.rowVec = false;
            this.colVec = false;
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
        this.rows = entries.length;
        this.cols = rowLength;
        if (this.rows == 1) {
            this.rowVec = true;
        }
        if (this.cols == 1) {
            this.colVec = true;
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


    /**
     * Creates an m-dimensional column vector with all zeroes except for a one in the index'th spot.
     * Indexing begins at 1.
     */
    private static Matrix singleOneCol(int m, int index) throws AssertionError {
        if (index <= 0 || index > m) {
            throw new AssertionError("m and index need to be positive, and index needs to " +
                    "be <= m.");
        }

        double[][] entries = new double[m][1];

        for (int i = 0; i < m; i++) {
            if (i == index - 1) {
                entries[i][0] = 1;
            }
            else {
                entries[i][0] = 0;
            }
        }

        return new Matrix(entries);
    }


    /**
     * Creates an n-dimensional row vector with all zeroes except for a one in the index'th spot.
     * Indexing begins at 1.
     */
    private static Matrix singleOneRow(int n, int index) throws AssertionError {
        if (index <= 0 || index > n) {
            throw new AssertionError("n and index need to be positive, and index needs to " +
                    "be <= n.");
        }

        double[][] entries = new double[1][n];

        for (int j = 0; j < n; j++) {
            if (j == index - 1) {
                entries[0][j] = 1;
            }
            else {
                entries[0][j] = 0;
            }
        }

        return new Matrix(entries);
    }
//-------------------------------------------------------------------------------------------------------------
// BOOLEAN OPERATIONS


    /**
     * @return "true" if v is a column vector.
     */
    public static boolean isColVec(Matrix v) {
        return v.colVec;
    }

    /**
     * @return "true" if v is a row vector.
     */
    public static boolean isRowVec(Matrix v) {
        return v.rowVec;
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
        return (isColVec(v1) && isColVec(v2)) || (isRowVec(v1) && isRowVec(v2));
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

//        System.out.println("In Upper");
        for (int j = 1; j < A.cols; j++) {
            Matrix curCol = getCol(A, j);
            for (int i = j + 1; i < A.rows; i++) {
//                System.out.println("Entry: " + getEntry(curCol, i, j));
                if ( Math.abs(getEntry(curCol, i, j)) >= tol ) { return false; }
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
//        System.out.println("In Lower");
        for (int i = 1; i < A.rows; i++) {
            Matrix curRow = getRow(A, i);
            for (int j = i + 1; j < A.cols; j++) {
//                System.out.println("Entry: " + getEntry(curRow, i, j));
                if ( Math.abs(getEntry(curRow, i, j)) >= 0 ) { return false; }
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
     * @return "true" if the vectors in vecs form an orthogonal set, if normal is false | "true" if the vectors in vecs
     * form an orthonormal set, if normal is true
     * @throws AssertionError if vecs is empty
     * @throws InvalidMatrixException if the vectors in vecs aren't column vectors with the same size.
     */
    public static boolean isOrtho(Matrix[] vecs, boolean normal) throws InvalidMatrixException {
        if (vecs.length == 0) {
            throw new AssertionError("vecs must not be empty.");
        }

        if (!isColVec(vecs[0]) || !sameSize(vecs)) {
            throw new InvalidMatrixException("The vectors in vecs must be column vectors with the same size.");
        }

        Matrix zeCol = zeroMatrix(vecs[0].rows, 1);
        if (vecs.length == 1) {
            if ( (normal && !isUnit(vecs[0])) || vecs[0].equals(zeCol) ) {
                return false;
            }
            else if ( !normal || isUnit(vecs[0]) ) {
                return true;
            }
        }

        for (int i = 0; i < vecs.length; i++) {
            for (int j = 0; j < vecs.length; j++) {
                if ( (i != j && Math.abs(dot(vecs[i], vecs[j])) >= tol) ||
                        (normal && Math.abs(1 - dot(vecs[i], vecs[i])) >= tol ) ) {
                    return false;
                }
            }
        }

        return true;

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
     * @throws AssertionError if mats is empty
     */
    public static boolean sameSize(Matrix[] mats) throws AssertionError {
        if (mats.length == 0) {
            throw new AssertionError("Input array must not be empty.");
        }

        if (mats.length == 1) {return true;}

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
     * @throws AssertionError if j isn't a positive integer less than or equal to the number of columns A has.
     */
    public static Matrix getCol(Matrix A, int j) throws AssertionError {
        if (j <= 0 || j > Matrix.cols(A)) {
            throw new AssertionError("j must be a positive integer less than or " +
                    "equal to the number of rows A has.");
        }

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
     * * @throws AssertionError if i isn't a positive integer less than or equal to the number of rows A has.
     */
    public static Matrix getRow(Matrix A, int i) throws AssertionError {
        if (i <= 0 || i > Matrix.rows(A)) {
            throw new AssertionError("i must be a positive integer less than or " +
                    "equal to the number of columns A has.");
        }

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
     * Takes the vectors in vecs and concatenates them into a matrix. If the vectors are column vectors, they will
     * be arranged from left to right in the order they appear in vecs; that is, vecs[i] will be column i
     * in the resulting matrix, assuming indexing begins at one. Similarly, if the vectors are row vectors,
     * they will be arranged from top to bottom in the order they appear in vecs; that is, vecs[i] will be row i
     * in the resulting matrix.
     * @param vecs the vectors being concatenated
     * @return a new Matrix object representing the matrix formed by the vectors in vecs.
     * @throws AssertionError if vecs is empty
     * @throws InvalidMatrixException if the elements of vecs aren't vectors of the same size.
     */
    public static Matrix formMatrix(Matrix[] vecs) throws AssertionError, InvalidMatrixException {
        if (vecs.length == 0) {
            throw new AssertionError("Input array must not be empty.");
        }

        if (!isVec(vecs[0]) || !sameSize(vecs)) {
            throw new InvalidMatrixException("All vectors in vecs must be vectors with the same size");
        }

        if (isColVec(vecs[0])) {
            Matrix concatenated = vecs[0];
            for (int i = 1; i < vecs.length; i++) {
                concatenated = appendCol(concatenated, vecs[i]);
            }
            return concatenated;
        }

        else {
            Matrix concatenated = vecs[0];
            for (int i = 1; i < vecs.length; i++) {
                concatenated = appendRow(concatenated, vecs[i]);
            }
            return concatenated;
        }
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
     * 2) topLeftRow > botRightRow and/or topLeftCol > botRightCol
     */
    public static Matrix getSubmatrix(Matrix A, int topLeftRow, int topLeftCol, int botRightRow, int botRightCol)
            throws AssertionError {
        if ( (topLeftRow <= 0 || topLeftRow > A.rows) ||
                (topLeftCol <= 0 || topLeftCol > A.cols) ||
                (botRightRow <= 0 || botRightRow > A.rows) ||
                (botRightCol <= 0 || botRightCol > A.cols) ) {
            throw new AssertionError("All indices must be within the dimensions of A.");
        }
        if ( topLeftRow > botRightRow || topLeftCol > botRightCol ) {
            throw new AssertionError("Make sure that your first two indices are each less than or " +
                    "equal to the corresponding last two indices.");
        }

        if (topLeftRow == 1 && topLeftCol == 1 && botRightRow == A.rows && botRightCol == A.cols) {
            return copy(A);
        }

        if (topLeftRow == botRightRow && topLeftCol == botRightCol) {
            return new Matrix(new double[][] { {getEntry(A, topLeftRow, topLeftCol)} });
        }

        double[][] subEntries = new double[botRightRow - topLeftRow + 1][botRightCol - topLeftCol + 1];

        for (int i = 0; i < botRightRow - topLeftRow + 1; i++) {
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
                newEntries[i][0] = v.entries[0][i];
            }

            return new Matrix(newEntries);
        }

        else {
            double[][] newEntries = new double[1][rows(v)];
            for (int i = 0; i < cols(v); i++) {
                newEntries[0][i] = v.entries[i][0];
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
     * Removes the colInd'th column of A. If A is a column vector, this function will return a zero vector.
     * @param A the matrix in question
     * @param colInd the index of the column being removed, with indexing beginning at 1.
     * @return <ul>
     *    <li>if A is not a column vector, a new Matrix object representing A, but with its colInd'th row removed.</li>
     *    <li>if A is a column vector, the zero vector with the same dimensions as A.</li>
     * </ul>
     * @throws AssertionError if colInd isn't a positive integer less than or equal to the number of columns in A.
     */
    public static Matrix removeCol(Matrix A, int colInd) throws AssertionError {
        if (isColVec(A)) {
            return zeroMatrix(A.rows, 1);
        }

        if (colInd <= 0 || colInd > A.cols) {
            throw new AssertionError("colInd must be a positive integer less than or equal to " +
                    "the number of columns in A.");
        }

        Matrix removed;
        if (colInd == 1) {
            removed = getCol(A, 2);
            for (int j = 3; j < A.cols + 1; j++) {
                removed = appendCol(removed, getCol(A, j));
            }
        } else {
            removed = getCol(A, 1);
            for (int j = 2; j < A.cols + 1; j++) {
                if (j != colInd) {
                    removed = appendCol(removed, getCol(A, j));
                }
            }
        }
        return removed;
    }


    /**
     * Removes the rowInd'th row of A. If A is a row vector, this function will return a zero vector.
     * @param A the matrix in question
     * @param rowInd the index of the row being removed, with indexing beginning at 1.
     * @return <ul>
     *     <li>if A is not a row vctor, a new Matrix object representing A, but with its rowInd'th row removed.</li>
     *     <li>if A is a row vector, the zero vector with the same dimensions as A.</li>
     * </ul>
     * @throws AssertionError if rowInd isn't a positive integer less than or equal to the number of rows in A.
     */
    public static Matrix removeRow(Matrix A, int rowInd) throws AssertionError {
        if (isRowVec(A)) {
            return zeroMatrix(1, A.cols);
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
     * Returns A, but padded with filler until it's mxn. If m and n are the number of rows and columns A has,
     * respectively, this function returns a copy of A.
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
        if (m == A.rows && n == A.cols) {
            return copy(A);
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
     * Returns || v1 - v2 ||.
     * @param v1 the first vector
     * @param v2 the second vector
     * @return the distance between v1 and v2
     * @throws MatrixSizeMismatchException if v1 and v2 aren't both column or row vectors with the same size.
     */
    public static double distance(Matrix v1, Matrix v2) throws MatrixSizeMismatchException {
        if (!isVec(v1) || !sameType(v1, v2)) {
            throw new MatrixSizeMismatchException("Inputs must both be column or row vectors with the same size.");
        }

        return magn( sub(v1, v2) );
    }


    /**
     * Returns the angle between v1 and v2.
     * @param v1 the first vector
     * @param v2 the second vector
     * @return the angle, in radians, between v1 and v2. This angle will be between 0 and pi.
     * @throws MatrixSizeMismatchException if v1 and v2 aren't both column or row vectors with the same size.
     */
    public static double angle(Matrix v1, Matrix v2) throws MatrixSizeMismatchException {
        if (!isVec(v1) || !sameType(v1, v2)) {
            throw new MatrixSizeMismatchException("Inputs must both be column or row vectors with the same size.");
        }
        return Math.acos( dot(v1, v2) / (magn(v1) * magn(v2)) );
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

        // moving any all-zero rows to the bottom
        Matrix zeRow = zeroMatrix(1, A.cols);
        for (int i = 1; i < A.rows; i++) {
            // only looping until A.rows since we don't care about the bottom row
            // if it's an all-zero row it's supposed to be there
            Matrix curRow = getRow(reduced, i);
            if (curRow.equals(zeRow)) {
                for (int j = A.rows; j > i; j--) {
                    if ( !getRow(reduced, j).equals(zeRow) ) {
                        reduced = swapRows(reduced, i, j);
                        break;
                    }
                }
            }
        }


        Matrix zeCol = zeroMatrix(A.rows, 1);
        int curRowIndex = 1;
//        System.out.println("Initial: \n" + reduced);
        for (int j = 1; j < A.cols + 1; j++) {
            if (curRowIndex > A.rows) {
                // breakout condition if the matrix has more columns than rows
                // could also just change the loop variable to be the smaller of the rows and columns
                break;
            }

            Matrix curCol = getCol(reduced, j);
            Matrix replacements = zeroMatrix(A.rows, A.cols);

            if ( curCol.equals(zeCol) ) {
                continue;
            }
            else if ( Math.abs(getEntry(curCol, curRowIndex, 1)) <= tol ) {
                for (int i = A.rows; i > curRowIndex; i--) {
                    if ( Math.abs(getEntry(reduced, i, j)) >= tol ) {
                        reduced = swapRows(reduced, curRowIndex, i);
                        curCol = getCol(reduced, j);
//                        System.out.println("Swapped: \n" + reduced);
                    }
                }
                if ( Math.abs(getEntry(curCol, curRowIndex, 1)) <= tol ) { // if all entries below are still zero somehow
                    continue;
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
        // n = A.rows (or A.cols) here
        return getSubmatrix(reduced, 1, A.rows, A.rows, 2 * A.rows);
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

//            System.out.println("Initial: \n" + reduced);

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
                    if ( getEntry(curCol, curRowIndex, 1) == 0 ) { // if all entries below are still zero somehow
                        continue;
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

//            System.out.println(reduced);
            return detFactor;
        }
    }

//-------------------------------------------------------------------------------------------------------------
// THE SYSTEM SOLVER

    /**
     * Returns the indices of the pivot columns and non-pivot columns of A. The leftmost column of A
     * is column 1. Helper for solve()
     * @param A the matrix in question
     * @return an array containing two arrays of integers. The first array contains the indices of the pivot columns,
     * and the second array contains the indices of the non-pivot columns.
     */
    private static int[][] pivotAndNonPivots(Matrix A) {
        ArrayList<Integer> basicCols = new ArrayList<>();
        ArrayList<Integer> freeCols = new ArrayList<>();
        Matrix reduced = rowRed(A);
        int pivotsFound = 0;
        // the number of pivots a matrix can have is bounded from above
        // by the smaller of the number of rows and the number of columns
        int min = Math.min(reduced.rows, reduced.cols);
        for (int j = 0; j < reduced.cols; j++) {
            if (pivotsFound < min) {
//                System.out.println("blah: " + reduced.rows + " " + (pivotsFound + 1));
                Matrix pivCol = singleOneCol(reduced.rows, pivotsFound + 1);
//            System.out.println("pivCol: \n" + pivCol);
                if (getCol(A, j + 1).equals(pivCol)) {
                    // if the current column is a pivot column
                    basicCols.add(j + 1);
                    pivotsFound++;
                } else {
                    freeCols.add(j + 1);
                }
            }
            else {
                freeCols.add(j + 1);
            }
        }
        // Manually copying elements since toArray doesn't work for primitives
        int[] basics = new int[basicCols.size()];
        for (int i = 0; i < basics.length; i++) {
            basics[i] = basicCols.get(i);
        }
        int[] frees = new int[freeCols.size()];
        for (int i = 0; i < frees.length; i++) {
            frees[i] = freeCols.get(i);
        }
        return new int[][] {basics, frees};

    }


    /**
     * Helper for solve()
     */
    private static boolean arrayContains(int[] arr, int val) {
        for (int i = 0; i < arr.length; i++) {
            if (val == arr[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the orthogonal representation of the solution set of the equation Ax = b.<br>
     * NOTE: If Ax = b is consistent and p is a solution to the equation, then
     * the solution set of Ax = b is the set of all vectors of the form x = p + v, where v is any solution
     * to Ax = 0. As such, the first entry in the returned array will be this vector p, and the second entry
     * will be a matrix whose columns form an orthogonal basis for Nul(A).
     * If the argument for normal is true, this basis will be orthonormal.
     * @param A the coefficient matrix
     * @param b the target vector
     * @param normal determines if the resulting basis is orthonormal
     * @return an array of two Matrix objects representing an orthogonal basis for the solution set to Ax = 0,
     * translated by a vector p that satisfies Ap = b.<br>
     * If p = 0, it will still be an element of the array.<br>
     * If the second element of the returned array is the zero vector, the null space of A is trivial.<br>
     * If the equation is inconsistent, both elements of the returned array will be zero vectors with as many
     * entries as b.
     * @throws MatrixSizeMismatchException if b isn't a column vector with as many rows as A.
     *
     * NOT COMPLETE
     */
    public static Matrix[] solve(Matrix A, Matrix b, boolean normal)
            throws MatrixSizeMismatchException {
        if (b.rows != A.rows || !isColVec(b)) {
            throw new MatrixSizeMismatchException("b must be a column vector with as many rows as A.");
        }
        Matrix equation = append(A, b);
//        System.out.println("Equation: \n" + equation);

        try {
            int m = A.rows;
            int n = A.cols;
            Matrix reduced = rowRed(append(A, b));
//            System.out.println("Reduced: \n" + reduced + "\n");
            Matrix zeRow = zeroMatrix(1, n);
            // checking if the system is inconsistent
            for (int i = m; i > 0; i--) {
                // starting from the bottom since any all-zero rows should be there
                Matrix curRow = getRow(reduced, i);
                Matrix lhs = getSubmatrix(curRow, 1, 1, 1, n);
                double rhs = getEntry(curRow, 1, n + 1);
                if ( lhs.equals(zeRow) && Math.abs(rhs) >= tol ) {
                    throw new InconsistentSystemException("Equation was inconsistent.");
                }
            }

            Matrix[] solutions = new Matrix[2];

            // Getting the particular solution
            if (b.equals(zeroMatrix(m, 1))) {
                solutions[0] = zeroMatrix(m, 1);
            }

            else {
                double[][] particularEntries = new double[n][1];
                double[][] rhsEntries = getCol(reduced, n + 1).entries;
                for (int i = 0; i < n; i++) {
                    if (i < m) {
                        particularEntries[i][0] = rhsEntries[i][0];
                    } else {
                        particularEntries[i][0] = 0;
                    }
                }
                solutions[0] = (new Matrix(particularEntries));
            }

            // Getting the null space

            // Identify free columns
            Matrix coeffMat = getSubmatrix(reduced, 1, 1, m, n);
//            System.out.println("Coeffs: \n" + coeffMat + "\n");
            int[][] basicsAndFrees = pivotAndNonPivots(coeffMat);
            int[] frees = basicsAndFrees[1];
            if (frees.length == 0) {
                solutions[1] = zeroMatrix(n, 1);
                return solutions;
            }
            // taking care of the first pivot column and setting up the accumulator simultaneously
            for (int i = 0; i < frees.length; i++) {
//                System.out.print("Frees: " + frees[i] + " ");
            }
//            System.out.println();

            //TODO: edit this to work for nonsquare matrices
//            double[][] basisVecs = new double[n][n];
//            for (int i = 0; i < m; i++) {
//                for (int j = 0; j < n; j++) {
//                    if (arrayContains(frees, j + 1) && i == j) {
//                        // in a free column, located at where the pivot in the column should've been
//                        basisVecs[i][j] = 1;
//                    }
//                    else if (!arrayContains(frees, j + 1) && i == j) {
//                        // in a pivot column, at the pivot
//                        basisVecs[i][j] = 0;
//                    }
//                    else {
//                        basisVecs[i][j] = -getEntry(coeffMat, i + 1, j + 1);
//                    }
//                }
//            }
            double[][] basisVecs = new double[n][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if ( !arrayContains(frees, j + 1) ) {
                        // skipping the basic columns since they won't make vectors in the null space
                        continue;
                    }
                    else if ( arrayContains(frees, j + 1) && i != j ) {
                        basisVecs[i][j] = -getEntry(coeffMat, i + 1, j + 1);
                    }
                }
            }
            for (int i = 0; i < n; i++) {
                if ( arrayContains(frees, i + 1) ) {
                    basisVecs[i][i] = 1;
                }
            }


            solutions[1] = formMatrix(gs(getCols(new Matrix(basisVecs)), normal));
//            System.out.println("nullspace: \n" + solutions[1]);
            return solutions;

        }

        catch (InconsistentSystemException ise) {
            System.out.println("The equation was inconsistent. Both elements of the returned matrix are zero vectors.");
            return new Matrix[] {zeroMatrix(A.rows, 1), zeroMatrix(A.rows, 1)};
        }
    }

//-------------------------------------------------------------------------------------------------------------
// SUBSPACES

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

        Matrix zero = zeroMatrix(vecs[0].rows, 1);
        Matrix[] basis = gs(vecs, false);
        int dimension = 0;
        for (int i = 0; i < basis.length; i++) {
            if ( !(basis[i].equals(zero)) ) {
                dimension++;
            }
        }
        return dimension;
    }


    /**
     * Returns an orthogonal basis for Col(A). If normal is true, this basis will be orthonormal.
     * @param A the matrix in question
     * @param normal determines if the resulting basis is orthonormal
     * @return a new Matrix object whose columns form an orthogonal (or orthonormal) basis for Col(A).
     */
    public static Matrix columnSpace(Matrix A, boolean normal) {
        Matrix reduced = rowRed(A);
        if (reduced.equals(zeroMatrix(A.rows, A.cols))) {
            return zeroMatrix(A.rows, 1);
        }

        Matrix zero = zeroMatrix(A.rows, 1);
        ArrayList<Integer> nonzeros = new ArrayList<>();
        for (int j = 0; j < reduced.cols; j++) {
            if ( !(getCol(reduced, j + 1).equals(zero))  ) {
                nonzeros.add(j + 1);
            }
        }
        Matrix[] basis = new Matrix[nonzeros.size()];
        for (int i = 0; i < basis.length; i++) {
            basis[i] = getCol(A, nonzeros.get(i));
        }
        return formMatrix(gs(basis, normal));
    }


    /**
     * Returns the dimension of Col(A).
     * @param A the matrix in question.
     * @return the dimension of Col(A)
     */
    public static int rank(Matrix A) {
        return dimension(getCols(columnSpace(A, false)));
    }


    /**
     * Returns an orthogonal basis for Nul(A). If normal is true, this basis will be orthonormal.
     * @param A the matrix in question
     * @param normal determines if the resulting basis is orthonormal
     * @return a new Matrix object whose columns form an orthogonal (or orthonormal) basis for Nul(A).
     */
    public static Matrix nullSpace(Matrix A, boolean normal) {
        return solve(A, zeroMatrix(A.rows, 1), normal)[1];
    }


    /**
     * Returns the dimension of Nul(A).
     * @param A the matrix in question.
     * @return the dimension of Nul(A).
     */
    public static int nullity(Matrix A) {
        return dimension(getCols(nullSpace(A, false)));
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
     * Returns the orthogonal projection of v onto the subspace spanned by the vectors in vecs.
     * @param v the vector being projected
     * @param vecs the vectors spanning the subspace
     * @return the orthogonal projection of v onto the span on the vectors in vecs
     * @throws InvalidMatrixException if 1) vecs doesn't contain only column vectors of the same size
     * 2) if vecs isn't an orthogonal set
     * @throws MatrixSizeMismatchException if v isn't a column vector with the same size as those in vecs
     */
    public static Matrix proj(Matrix v, Matrix[] vecs) throws InvalidMatrixException, MatrixSizeMismatchException {
        if (!isColVec(vecs[0]) || !sameSize(vecs)) {
            throw new MatrixSizeMismatchException("All elements of vecs must be column vectors with the same size.");
        }
        if (!sameSize(v, vecs[0])) {
            throw new MatrixSizeMismatchException("v must have the same dimension as the vectors in vecs.");
        }
        if (!isOrtho(vecs, false)) {
            throw new InvalidMatrixException("vecs must be an orthogonal set.");
        }

        Matrix zeCol = zeroMatrix(vecs[0].rows, 1);
        ArrayList<Matrix> nonzeros = new ArrayList<>();
        for (int i = 0; i < vecs.length; i++) {
            if (!vecs[i].equals(zeCol)) {
                nonzeros.add(vecs[i]);
            }
        }

        Matrix proj = zeroMatrix(vecs[0].rows, 1); // the accumulator
        for (int i = 0; i < nonzeros.size(); i++) {
            Matrix curVec = nonzeros.get(i);
            double scaleFactor = dot(v, curVec) / dot(curVec, curVec);
            proj = add(proj, scale(vecs[i], scaleFactor));
        }

        return proj;
    }


    /**
     * Returns an orthogonal basis for the subspace spanned by the vectors in vecs.
     * If normal is true, this basis will be orthonormal.
     * @param vecs the set of vectors that the GSP is being applied to
     * @param normal determines whether the returned basis is orthonormal or not
     * @return an orthogonal basis for the subspace spanned by the vectors in vecs. If vecs only contains zero vectors,
     * this functions returns an array containing only a zero vector.
     * @throws InvalidMatrixException if vecs doesn't contain only column vectors of the same size
     * @throws MatrixSizeMismatchException if the vectors in vecs aren't column vectors of the same size.
     */
    public static Matrix[] gs(Matrix[] vecs, boolean normal) throws InvalidMatrixException, MatrixSizeMismatchException {
        if (!isColVec(vecs[0])) {
            throw new InvalidMatrixException("vecs must contain only column vectors of the same size.");
        }
        if (!sameSize(vecs)) {
            throw new MatrixSizeMismatchException("All elements of vecs must be column vectors of the same size.");
        }

        if (vecs.length == 1) {
            Matrix zeCol = zeroMatrix(vecs[0].rows, 1);
            if (vecs[0].equals(zeCol)) {
                return new Matrix[] {zeCol};
            }
            else {
                if (normal) {
                    return new Matrix[] {normalize(vecs[0])};
                }
                else {
                    return new Matrix[] {vecs[0]};
                }
            }
        }


//        System.out.println("vecs: \n" + formMatrix(vecs));
        boolean allZeroes = true;
        Matrix zeCol = zeroMatrix(vecs[0].rows, 1);
        int firstNonzero = 0;
        for (int i = 0; i < vecs.length; i++) {
            if (!vecs[i].equals(zeCol)) {
                allZeroes = false;
                firstNonzero = i;
                break;
            }
        }
        if ( allZeroes || (vecs.length == 1 && vecs[0].equals(zeroMatrix(vecs[0].rows, 1)) )) {
            return new Matrix[] {zeroMatrix(vecs[0].rows, 1)};
        }

        // initializing the vectors
        ArrayList<Matrix> onb = new ArrayList<>();

        // Computing the projections
        for (int i = firstNonzero; i < vecs.length; i++) {
            if (i == firstNonzero) {
                onb.add(vecs[i]);
            }
            else {
//                System.out.println("in else, i = " + i);
                Matrix[] arr = new Matrix[onb.size()];
                arr = onb.toArray(arr);
//                System.out.println("arr: \n" + formMatrix(arr));
                Matrix proj = proj(vecs[i], arr);
//                System.out.println("Starting vector: \n" + vecs[i] + "\n");
//                System.out.println("Projection: \n" + proj + "\n");
                if (!proj.equals(vecs[i])) {
                    // only add to the onb if the projection isn't equal to the vector itself
                    // (i.e., if the vector isn't already in the span of the rest)
                    // if this check wasn't here, we'd be risking division by zero
//                    System.out.println("added");
                    Matrix ortho = sub(vecs[i], proj);
                    onb.add(ortho);
                }
            }
        }

        Matrix[] arr = new Matrix[onb.size()];
        arr = onb.toArray(arr);
        if (normal) {
            return normalize(arr);
        }
        return arr;
    }


    /**
     * Returns an orthogonal basis for the orthogonal complement of the subspace spanned by the vectors in vecs.
     * If normal is true, this basis will be orthonormal.
     * @param vecs the vectors spanning some subspace
     * @param normal determines whether the returned basis is orthonormal or not
     * @return a new Matrix object whose columns form an orthogonal (or orthonormal) basis for the orthogonal
     * complement of the subspace spanned by the vectors in vecs.
     * @throws InvalidMatrixException if the vectors in vecs aren't column vectors of the same size.
     */
    public static Matrix orthoComp(Matrix[] vecs, boolean normal) throws InvalidMatrixException {
        if (!isColVec(vecs[0]) || !sameSize(vecs)) {
            throw new InvalidMatrixException("vecs must contain column vectors of the same size.");
        }
        Matrix subspace = formMatrix(vecs);
//        System.out.println("subspace: \n" + subspace);
        int m = subspace.rows;
        int n = subspace.cols;

        return nullSpace(transpose(subspace), normal);
    }


    /**
     * Returns an orthogonal basis of the set of least-squares solutions to the equation Ax = b.
     * If normal is true, this basis will be orthonormal.
     * @param A the coefficient matrix
     * @param b the target vector
     * @param normal determines if the returned basis is orthonormal.
     * @return an array of two Matrix objects; the first one is a vector p that satisfies (A^T * A)p = (A^T)b,
     * and the second one is the matrix whose columns form an orthogonal (or orthonormal) basis for Nul(A^T * A).
     * @throws MatrixSizeMismatchException if b isn't a column vector with as many rows as A.
     */
    public static Matrix[] leastSquares(Matrix A, Matrix b, boolean normal) throws MatrixSizeMismatchException {
        if (b.rows != A.rows || !isColVec(b)) {
            throw new MatrixSizeMismatchException("b must be a column vector with as many rows as A.");
        }

        return solve( mult(transpose(A), A), mult(transpose(A), b), normal);
    }


//-------------------------------------------------------------------------------------------------------------
// EIGENSTUFF

    /**
     * Returns an approximation of A's eigenvalues.
     * @param A the matrix in question
     * @return an array containing the estimation of A's eigenvalues, arranged in decreasing order.
     * @throws InvalidMatrixException if <ul>
     *     <li>A isn't square.</li>
     *     <li>A has complex eigenvalues (I'm not going to attempt handling complex numbers anytime soon, if ever.)</li>
     * </ul>
     */
    public static double[] eigenvalues(Matrix A) throws InvalidMatrixException {
        if (!isSquare(A)) {
            throw new InvalidMatrixException("Input matrix must be square.");
        }

        Matrix acc = copy(A);
        for (int i = 0; i < 500; i++) {
            Matrix[] curQR = QR(acc);
            acc = mult( curQR[1], curQR[0] );
        }

//        System.out.println("acc: \n" + acc);
        if (!isTriangular(acc)) {
            throw new InvalidMatrixException("Input matrix likely has complex eigenvalues.");
        }


        double[] eigens = new double[A.cols];
        for (int i = 0; i < eigens.length; i++) {
            eigens[i] = acc.entries[i][i];
        }

        Arrays.sort(eigens);
        double[] copy = Arrays.copyOf(eigens, eigens.length);
        for (int i = 0; i < eigens.length; i++) {
            eigens[i] = copy[eigens.length - i - 1];
        }

        return eigens;

        //TODO: need some way to handle complex eigenvalues
    }


    /**
     * Returns an approximation of A's eigenvalues, with each element of the returned array being unique. As such,
     * the algebraic multiplicity of each eigenvalue cannot be implied from the return value of this function.
     * @param A the matrix in question
     * @return an array containing the estimation of A's unique eigenvalues, arranged in decreasing order.
     * @throws InvalidMatrixException if <ul>
     *     <li>A isn't square.</li>
     *     <li>A has complex eigenvalues (I'm not going to attempt handling complex numbers anytime soon, if ever.)</li>
     * </ul>
     */
    public static double[] uniqueEigenvalues(Matrix A) throws InvalidMatrixException {
        double[] eigens = eigenvalues(A);
        ArrayList<Double> uniqueEigens = new ArrayList<>();
        for (int i = 0; i < eigens.length; i++) {
            if (i == 0) {
                uniqueEigens.add(eigens[i]);
                continue;
            }

            for (int j = 0; j < uniqueEigens.size(); j++) {
                double diff = Math.abs(uniqueEigens.get(j) - eigens[i]);
                if (diff <= tol) {
//                    System.out.println("dupe found");
                    break;
                }

                if (j == uniqueEigens.size() - 1) {
//                    System.out.println("added");
                    uniqueEigens.add(eigens[i]);
                }

            }
        }
        // toArray apparently doesn't work on primitive types so I gotta do this manually
        double[] arr = new double[uniqueEigens.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = uniqueEigens.get(i);
        }

        return arr;
    }


    /**
     * Returns an orthogonal basis for each of A's eigenspaces. The bases will be arranged in decreasing order of
     * the corresponding eigenvalues; that is, the first basis will correspond to the largest eigenvalue, the second
     * will correspond to the second largest, and so on. If normal is true, each of these bases will be orthonormal.
     * @param A the matrix in question.
     * @return an array of Matrix objects, with each element of the array containing the matrix whose columns form
     * an orthogonal basis for the corresponding eigenvalue.
     * @throws InvalidMatrixException if <ul>
     *      <li>A isn't square.</li>
     *      <li>A has complex eigenvalues (I'm not going to attempt handling complex numbers anytime soon, if ever.)</li>
     * </ul>
     */
    public static Matrix[] eigenvectors(Matrix A, boolean normal) throws InvalidMatrixException {
        double[] eigens = uniqueEigenvalues(A);

        Matrix[] eigenspaces = new Matrix[eigens.length];
        Matrix I = new Matrix(A.cols);
        for (int i = 0; i < eigenspaces.length; i++) {
//            System.out.println("Eigenvalue: " + eigens[i]);
            eigenspaces[i] = nullSpace( sub(A, scale(I, eigens[i])), normal );
//            System.out.println("Eigenspace for " + eigens[i] + ": \n" + eigenspaces[i] + "\n");
        }
        return eigenspaces;
    }


    /**
     * Returns an approximation of A's singular values.
     * @param A the matrix in question
     * @return an array containing the estimation of A's singular values, arranged in decreasing order.
     */
    public static double[] singularValues(Matrix A) {
        double[] eigens = eigenvalues( mult(transpose(A), A) );
        // could always just go straight to using A^TA but the process below is generally more optimal
//        int m = A.rows;
//        int n = A.cols;
//        double[] eigens = new double[n];
//
//        // AA^T and A^TA have the same nonzero eigenvalues with the same multiplicities
//        // so, if m < n, we can find the nonzero eigenvalues using the smaller matrix AA^T. Furthermore,
//        // A^TA is guaranteed to have nonnegative eigenvalues, so we can infer the multiplicity of the leftover
//        // zero eigenvalue.
//        if (m < n) {
//            double[] nonzeroEigens = eigenvalues( mult(A, transpose(A)) );
//            for (int i = 0; i < eigens.length; i++) {
//                if (i < m) {
//                    eigens[i] = nonzeroEigens[i];
//                }
//                else {
//                    eigens[i] = 0;
//                }
//            }
//        }
//        else {
//            eigens = eigenvalues( mult(transpose(A), A) );
//        }

        double[] sings = new double[eigens.length];
        for (int i = 0; i < eigens.length; i++) {
            sings[i] = Math.sqrt(eigens[i]);
        }
        return sings;
    }


//-------------------------------------------------------------------------------------------------------------
// MATRIX FACTORIZATIONS

    /**
     * Returns a QR factorization of A
     * @param A the matrix being factored.
     * @return an array with two elements. The first element is the Q matrix, and the second is the R matrix.
     */
    public static Matrix[] QR(Matrix A) {
        // ONB for Col(A)
        Matrix colA = columnSpace(A, true);
//        System.out.println("Col A: \n" + colA);

        // ONB for Col(A)-perp = Nul(A^T), if necessary
        Matrix Q = colA;

        if (Q.cols < A.cols) {
            Matrix colAPerp = orthoComp(getCols(colA), true); // Col(A)-perp = Nul(A^T)
//            System.out.println("Nul A^T: \n" + colAPerp);
            Q = append(Q, colAPerp);
        }
        Matrix R = mult( transpose(Q), A );

        return new Matrix[] {Q, R};
    }


    /**
     * Returns matrices P and D such that D is diagonal and A = PDP^-1, if it exists. P will be constructed so that
     * the basis of each eigenspace will be orthonormal, and the bases will be arranged from left to right in order
     * of decreasing eigenvalues. The D matrix will be the diagonal matrix of A's eigenvalues arranged down the
     * diagonal in decreasing order.
     * @param A the matrix being diagonalized
     * @return an array containing two Matrix objects. The first element is the P matrix, and the second element is
     * the D matrix.
     * @throws InvalidMatrixException if <ul>
     *      <li>A isn't square.</li>
     *      <li>A has complex eigenvalues (I'm not going to attempt handling complex numbers anytime soon, if ever.)</li>
     *      <li>A cannot be diagonalized (not enough linearly independent eigenvectors).</li>
     * </ul>
     */
    public static Matrix[] diagonalize(Matrix A) throws InvalidMatrixException {
        double[] eigenvalues = eigenvalues(A);
        Matrix[] eigenvectors = eigenvectors(A, true);
        Matrix P = eigenvectors[0];
        for (int i = 1; i < eigenvectors.length; i++) {
            P = append(P, eigenvectors[i]);
        }
        if (P.cols != A.cols) {
            throw new InvalidMatrixException("Input matrix does not have " + A.cols + " linearly " +
                    "independent eigenvectors.");
        }

        Matrix D = diag(eigenvalues);
        return new Matrix[] {P, D};

    }


    /**
     * Returns matrices U, S, and V such that A = USV^T. Let m be the number of rows A has, and let n be the
     * number of columns.
     * U is the orthogonal matrix whose columns form an orthonormal basis for R^m using eigenvectors of AA^T.
     * S is the mxn matrix containing the nonzero singular values of A.
     * V is the matrix whose  columns form an orthonormal basis for R^n using eigenvectors of A^TA.
     * @param A the matrix being factored
     * @return an array of three matrices that together form an SVD of A. The first matrix is U, the second is S,
     * and the third is V.
     */
    public static Matrix[] SVD(Matrix A) {
        // Forming S
        double[] sings = singularValues(A);
        ArrayList<Double> intermediate = new ArrayList<>();
        for (int i = 0; i < sings.length; i++) {
            if ( Math.abs(sings[i]) >= tol ) {
                intermediate.add(sings[i]);
            }
        }
        double[] nonzeroSings = new double[intermediate.size()];
        for (int i = 0; i < intermediate.size(); i++) {
            nonzeroSings[i] = intermediate.get(i);
        }

        System.out.print("nonzero Sings: ");
        for (int i = 0; i < nonzeroSings.length; i++) {
            System.out.print(nonzeroSings[i] + " ");
        }
        System.out.println();

        Matrix S = pad( diag(nonzeroSings), A.rows, A.cols, 0 );

        // Forming V
        Matrix V = diagonalize( mult(transpose(A), A) )[0];

        // Forming U
        // Starting with an orthonormal basis for Col(A) using the columns of V
        Matrix U = zeroMatrix(A.rows, A.rows);
        for (int j = 0; j < nonzeroSings.length; j++) {
            Matrix uCol = scale(mult( A, getCol(V, j + 1) ), 1 / nonzeroSings[j]);
            U = replaceCol(U, j + 1, uCol);
        }
        if (A.rows > A.cols) {
            // need an orthonormal basis for Nul(A^T) to complete U
            Matrix nulAT = orthoComp(getCols(columnSpace(A, true)), true);
            Matrix[] nulCols = getCols(nulAT);
            for (int j = nonzeroSings.length; j < A.rows; j++) {
                U = replaceCol(U, j + 1, nulCols[j - nonzeroSings.length]);
            }
        }

        return new Matrix[] {U, S, V};
    }


//-------------------------------------------------------------------------------------------------------------
// MISC OPERATIONS

    /**
     * A.toString() returns the string representation of A.
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
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

        return string.toString();
    }


    /**
     * A.equals(B) returns "true" if A and B have the same size and if their corresponding entries are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Matrix matrix = (Matrix) o;

        if (this.rows != matrix.rows ||
                this.cols != matrix.cols ||
                this.rowVec != matrix.rowVec ||
                this.colVec != matrix.colVec) { return false;}

        // leaving some room for roundoff error
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if ( Math.abs( this.entries[i][j] - matrix.entries[i][j] ) >= tol ) {return false;}
            }
        }

        return true;
    }


    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(entries), rows, cols, rowVec, colVec);
    }


    private static double round(double num, int places) {
        return Math.round( num * Math.pow(10, places) ) / Math.pow(10, places);
    }
}
