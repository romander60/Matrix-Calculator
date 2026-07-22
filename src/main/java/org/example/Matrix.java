/**
 * A library containing a bunch of matrix operations. Matrices are represented as 2D arrays of doubles.
 * Author: romander60  
 * Overall time spent on project: ~50 hours
 * Last updated: July 22, 2026
 */



package org.example;

import java.util.*;

public class Matrix {

    // The values in the matrix
    private final double[][] entries;
    // The number of rows in the matrix
    private final int rows;
    // The number of columns in the matrix
    private final int cols;
    // True if this matrix is 1 x n
    private final boolean rowVec;
    // True if this matrix is m x 1
    private final boolean colVec;


    // The value within which roundoff errors are tolerated
    private static final double tol = 0.0001;
    // The number of decimal places quantities should be rounded to
    private static final int decPlaces = 5;
    // THERE'S CURRENTLY NO ROUNDING FUNCTIONALITY; THAT'LL BE ADDED WHEN I TEST ALL THESE FUNCTIONS AS THEY ARE

//-------------------------------------------------------------------------------------------------------------
// MATRIX GENERATORS (COMMENTED)

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
            // setting the diagonal entries to 1
            entries[i][i] = 1;
        }

        // originally Arrays.copyOf(entries, entries.length)
        this.entries = entries;
        this.rows = n;
        this.cols = n;
        if (n == 1) {
            // 1x1 matrix; effectively just a number
            this.rowVec = true;
            this.colVec = true;
        }
        else {
            // nxn matrix for n > 1
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

        for (int i = 1; i < entries.length; i++) {
            // checking each row's length to ensure that each one has the same number of elements
            if (entries[i].length != rowLength) {
                throw new InvalidMatrixException("All rows in a matrix must " +
                        "have the same number of entries.");
            }
        }

        // using Arrays.copyOf to prevent the user from changing this matrix's value by modifying the entries array
        this.entries = Arrays.copyOf(entries, entries.length);
        this.rows = entries.length;
        this.cols = rowLength;
        this.rowVec = this.rows == 1;
        this.colVec = this.cols == 1;
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
            // only setting the diagonal entries
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

        // initializing the column vector
        double[][] entries = new double[m][1];

        for (int i = 0; i < m; i++) {
            if (i == index - 1) {
                // using index - 1 since the specification explains that indexing begins at one. So, for example, if
                // index = 1, then we'd want to set the top entry to 1, which is entries[0][0].
                entries[i][0] = 1;
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
                // using index - 1 since the specification explains that indexing begins at one. So, for example, if
                // index = 1, then we'd want to set the top entry to 1, which is entries[0][0].
                entries[0][j] = 1;
            }
        }

        return new Matrix(entries);
    }
//-------------------------------------------------------------------------------------------------------------
// BOOLEAN OPERATIONS (COMMENTED)


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
        // Triangular matrices are necessarily square
        if (!isSquare(A)) { return false; }

        // Iterate through each column
        for (int j = 1; j < A.cols + 1; j++) {
            // Starting at j = 1 as per getCol's specification
            Matrix curCol = getCol(A, j);
            for (int i = j + 1; i < A.rows + 1; i++) {
                // Starting at i = j + 1 because we only care about the entries below the main diagonal.
                if ( Math.abs(getEntry(curCol, i, 1)) >= tol ) {
                    // Values within the tolerance range are treated as being "close enough" to zero.
                    // Anything outside that range is considered nonzero.
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * @return "true" if A is a lower triangular matrix; that is, if all the entries above A's main diagonal are zero.
     */
    public static boolean isLowerTriangular(Matrix A) {
        // Triangular matrices are necessarily square
        if (!isSquare(A)) { return false; }

        // Conveniently, a matrix is lower triangular if all the entries
        // to the right of the main diagonal are zero.
        // So, I can just use the same structure as in isUpperTriangular.

        for (int i = 1; i < A.rows + 1; i++) {
            // Iterating through the rows
            Matrix curRow = getRow(A, i);
            for (int j = i + 1; j < A.cols + 1; j++) {
                // Starting at j = i + 1 because we only care about the entries to the right of the main diagonal.
                if ( Math.abs(getEntry(curRow, 1, j)) >= 0 ) {
                    // Values within the tolerance range are treated as being "close enough" to zero.
                    // Anything outside that range is considered nonzero.
                    return false;
                }
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
        // A diagonal matrix is one with all zero non-diagonal entries. Equivalently, a diagonal matrix
        // is one that's both lower and upper triangular
        return isLowerTriangular(A) && isUpperTriangular(A);
    }


    /**
     * @return "true" if A^-1 exists.
     */
    public static boolean isInvertible(Matrix A) {
        // Invertible matrices are necessarily square
        // By the Invertible Matrix Theorem, a matrix is invertible iff it's row equivalent to the identity matrix
        return isSquare(A) && rowRed(A, true)[0].equals( new Matrix(A.rows) );
    }

    /**
     * @return "true" if A is equal to its transpose.
     */
    public static boolean isSymmetric(Matrix A) {
        return A.equals( transpose(A) );
    }


    /**
     * @return "true" if v is a unit vector; that is, if it's magnitude is 1.
     */
    public static boolean isUnit(Matrix v) {
        try {
            // Anything within the tolerance range is considered to be zero.
            // So, if 1 - magn(v) is within the tolerance range, then magn(v) is close enough to 1.
            return Math.abs(1 - magn(v)) < tol;
        }

        catch (InvalidMatrixException ime) {
            // Should only end up here if v isn't a column or row vector; magn will throw an exception
            // if this happens.
            System.out.println("Input was not a column or row vector. Returning false.");
            return false;
        }
    }


    /**
     * @return "true" if v1 and v2 are orthogonal; that is, if their dot product is zero. Note that v1 and v2 are
     * implied to be vectors of the same size.
     */
    public static boolean areOrtho(Matrix v1, Matrix v2) {
        try {
            // Anything within the tolerance range is considered to be zero.
            return sameType(v1, v2) && Math.abs(dot(v1, v2)) < tol;
        }

        catch (InvalidMatrixException ime) {
            System.out.println("Both inputs must either be both column vectors or both row vectors.");
            return false;
        }

        catch (MatrixSizeMismatchException msme) {
            System.out.println("Both inputs must be vectors of the same size.");
            return false;
        }
    }


    /**
     * @return "true" if A has orthonormal columns; that is, if A^T * A = I.
     */
    public static boolean isOrtho(Matrix A) {
        return mult(transpose(A), A).equals( new Matrix(cols(A)) );
    }

    /**
     * @return <ul>
     *     <li>"true" if the vectors in vecs form an orthogonal set, if normal is false</li>
     *     <li>"true" if the vectors in vecs form an orthonormal set, if normal is true</li>
     * </ul>
     */
    public static boolean isOrtho(Matrix[] vecs, boolean normal) {
        try {
            if (vecs.length == 0) {
                // Can't operate on an empty array
                throw new AssertionError();
            }

            if (!isColVec(vecs[0]) || !sameSize(vecs)) {
                // The first conditional checks if the first element is a column vector. If it isn't, an exception
                // gets thrown because we're only operating on column vectors. The second conditional checks if all
                // the vectors in vecs have the same size; i.e., the same size as vecs[0]. If they don't, an
                // exception is thrown because we can only operate on vectors of the same size to check their
                // orthogonality.
                // (All things considered, I could just return false if the second conditional fails, but it feels more
                // natural to put these two conditionals together like this.)
                throw new InvalidMatrixException("");
            }

            // Making an all-zero column with the same size as the vectors in vecs
            // Just an edge case for when vecs only has 1 vector in it. In this case, the set is orthogonal
            // iff it isn't the zero vector.
            Matrix zeCol = zeroMatrix(vecs[0].rows, 1);
            if (vecs.length == 1) {
                // If we've reached this point and normal is false, then the set is orthogonal because we've
                // already determined vecs[0] is nonzero. If normal is true, then we need to check if it
                if ((normal && !isUnit(vecs[0])) || vecs[0].equals(zeCol)) {
                    // If normal is true and vecs[0] isn't a unit vector, then vecs isn't an orthonormal set.
                    // Additionally, if vecs[0] is the zero vector, it's definitely not an orthogonal set.
                    return false;
                }

                // If we've reached this point and normal is false, then the set is orthogonal because we've
                // already determined vecs[0] is nonzero. If normal is true, then we need to check if it's
                // a unit vector. If it is, then the set is orthonormal.
                else return !normal || isUnit(vecs[0]);
            }

            // Iterating through each vector in vecs
            for (int i = 0; i < vecs.length; i++) {
                // Comparing this vector to the others in vecs
                for (int j = i; j < vecs.length; j++) {
                    // Starting at j = i since dot products are commutative; as such, dotting the jth vector
                    // with the ith vector for j < i is a redundant operation.
                    if ((i != j && Math.abs(dot(vecs[i], vecs[j])) >= tol) ||
                            (normal && 1 - magn(vecs[i]) >= tol)) {
                        // If i != j, the vectors are distinct; as such, we want to check if their dot product is zero.
                        // If i == j, we're looking at exactly one vector in vecs. In this case, if normal is true,
                        // we want to check whether it's a unit vector. If it's false, just move to the next iteration.
                        // We're only looking for an orthogonal set in this case, so we don't care about the individual
                        // vectors; we only care about their relationships with the others.

                        return false;
                    }
                }
            }

            return true;
        }

        catch (AssertionError ae) {
            System.out.println("vecs must not be empty. Returning false.");
            return false;
        }

        catch (InvalidMatrixException ime) {
            System.out.println("The elements in vecs must be column vectors with the same size. Returning false.");
            return false;
        }

    }


    /**
     * @return "true" if col is equal to one of A's columns.
     */
    public static boolean hasCol(Matrix A, Matrix col) {
        // If col doesn't have the same number of entries as the columns in A, then it can't be in A; by the invariant
        // defining how matrices are built, all columns in a matrix must have the same number of entries.
        if (col.rows != A.rows) { return false; }

        for (int j = 0; j < A.cols; j++) {
            // There's no need to verify if col is a column vector; if it isn't, then the .equals method will declare
            // them unequal due to their mismatching sizes.
            if (col.equals( getCol(A, j + 1) )) {
                // Using j + 1 as the second argument in getCol to align its specification and the loop variable.
                // I kind of flip-flop between starting at j = 0 and using j + 1 in getCol and starting at j = 1
                // and using j in getCol; they both accomplish the same goal.
                return true;
            }
        }

        return false;
    }


    /**
     * @return "true" if row is equal to one of A's rows.
     */
    public static boolean hasRow(Matrix A, Matrix row) {
        // If row doesn't have the same number of entries as the rows in A, then it can't be in A; by the invariant
        // defining how matrices are built, all row in a matrix must have the same number of entries.
        if (row.cols != A.cols) { return false; }
        for (int i = 0; i < A.rows; i++) {
            // There's no need to verify if row is a row vector; if it isn't, then the .equals method will declare
            // them unequal due to their mismatching sizes.
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
        try {
            if (mats.length == 0) {
                // Can't operate on an empty array
                throw new AssertionError();
            }

            if (mats.length == 1) {
                // If there's only one element in mats, then it definitely has the same dimension as itself.
                return true;
            }

            // Setting the row and column counts that each element of mats will be compared to
            int rowCount = mats[0].rows;
            int colCount = mats[0].cols;

            // Starting at i = 1 since mats[0] was used to set the baseline values, so there's no point in checking it.
            // In essence, we're checking that each element in mats is the same size as mats[0]
            for (int i = 1; i < mats.length; i++) {
                if (mats[i].rows != rowCount || mats[i].cols != colCount) {
                    return false;
                }
            }

            return true;
        }

        catch (AssertionError ae) {
            System.out.println("Input array must not be empty. Returning false.");
            return false;
        }
    }

//-------------------------------------------------------------------------------------------------------------
// GETTERS  (COMMENTED)

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
        if (j <= 0 || j > A.cols) {
            throw new AssertionError("j must be a positive integer less than or " +
                    "equal to the number of rows A has.");
        }

        // Initializing the column vector that'll hold the entries of A's jth column
        double[][] col = new double[A.rows][1];

        for (int i = 0; i < A.rows; i++) {
            // Using j - 1 to align the index input with how arrays are indexed in Java.
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
        if (i <= 0 || i > A.rows) {
            throw new AssertionError("i must be a positive integer less than or " +
                    "equal to the number of columns A has.");
        }

        // Initializing the row vector that'll hold the entries of A's ith row
        double[][] row = new double[1][A.cols];

        for (int j = 0; j < A.cols; j++) {
            // Using i - 1 to align the input's indexing and Java's array indexing
            row[0][j] = A.entries[i - 1][j];
        }
        return new Matrix(row);
    }


    /**
     * Returns a_ij.
     * @param A the matrix in question.
     * @param i the row that the desired entry is in, with indexing beginning at 1.
     * @param j the column that the desired entry is in, with indexing beginning at 1.
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
     * @return a 2D array of doubles representing A's entries.
     */
    public static double[][] getEntries(Matrix A) {
        // Making a copy of A.entries to prevent users from manipulating A's entries directly
        // (I know I made the entries field final, but I'm doing this just in case)

        double[][] copy = new double[A.rows][A.cols];
        for (int i = 0; i < A.rows; i++) {
            System.out.println("bah");
            for (int j = 0; j < A.cols; j++) {
                System.out.println("gah");
                copy[i][j] = A.entries[i][j];
            }
        }

        return copy;
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
     * bottom rightmost entry is the (botRightRow, botRightCol)-entry of A. All indexing begins at 1 in this method.
     * @param A the matrix in question
     * @param topLeftRow the row index of the top left entry of the resulting submatrix
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
            // This is just when the input indices correspond to the top leftmost entry and bottom rightmost entry of A.
            return copy(A);
        }

        if (topLeftRow == botRightRow && topLeftCol == botRightCol) {
            // This is when both index pairs are equal; i.e., we're looking at a single entry in A.
            return new Matrix(new double[][] { {getEntry(A, topLeftRow, topLeftCol)} });
        }

        // Initializing the dimensions of the submatrix
        double[][] subEntries = new double[botRightRow - topLeftRow + 1][botRightCol - topLeftCol + 1];

        for (int i = 0; i < botRightRow - topLeftRow + 1; i++) {
            for (int j = 0; j < botRightCol - topLeftCol + 1; j++) {
                // Taking the appropriate entries in A and putting them in subEntries
                subEntries[i][j] = A.entries[topLeftRow - 1 + i][topLeftCol - 1 + j];
            }
        }

        return new Matrix(subEntries);

    }

//-------------------------------------------------------------------------------------------------------------
// MATRIX MANIPULATION  (COMMENTED)

    /**
     * Generates the partitioned matrix [A | v], where v is assumed to be a column vector.
     * @param A the matrix that's being appended to.
     * @param v the column vector that's getting appended to A.
     * @return a new Matrix object representing A, but with v as an augmented column.
     * @throws MatrixSizeMismatchException if v isn't a column vector with as many rows as A.
     */
    private static Matrix appendCol(Matrix A, Matrix v) throws MatrixSizeMismatchException {
        if (v.rows != A.rows || !isColVec(v)) {
            // This check is here to avoid violating the invariant stating that all matrices' columns must have
            // the same number of entries
            throw new MatrixSizeMismatchException("v must be a column vector with as many rows as A.");
        }

        // Initializing the augmented matrix
        double[][] entries = new double[A.rows][A.cols + 1];

        // Manually copying over A's entries into entries
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
            // This check is here to avoid violating the invariant stating that all matrices' rows must have
            // the same number of entries
            throw new MatrixSizeMismatchException("v must be a row vector with as many columns as A.");
        }

        // Initialized the augmented matrix
        double[][] entries = new double[A.rows + 1][A.cols];

        // Copying the entries over
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
        if (A.rows != B.rows) {throw new MatrixSizeMismatchException("Matrices must have an equal number of rows.");}

        Matrix appended = copy(A);
        for (int j = 0; j < B.cols; j++) {
            appended = appendCol(appended, getCol(B, j + 1));
        }
        return appended;
    }


    /**
     * Takes the vectors in vecs and concatenates them into a matrix. If the vectors are column vectors, they will
     * be arranged from left to right in the order they appear in vecs; that is, vecs[i] will be column i
     * in the resulting matrix, assuming indexing begins at zero. Similarly, if the vectors are row vectors,
     * they will be arranged from top to bottom in the order they appear in vecs; that is, vecs[i] will be row i
     * in the resulting matrix.
     * @param vecs the vectors being concatenated
     * @return a new Matrix object representing the matrix formed by the vectors in vecs.
     * @throws AssertionError if vecs is empty
     * @throws InvalidMatrixException if the elements of vecs aren't vectors of the same size.
     */
    public static Matrix formMatrix(Matrix[] vecs) throws AssertionError, InvalidMatrixException {
        if (vecs.length == 0) {
            // Can't operate on an empty array
            throw new AssertionError("Input array must not be empty.");
        }

        if (!isVec(vecs[0]) || !sameSize(vecs)) {
            throw new InvalidMatrixException("All vectors in vecs must be vectors with the same size");
        }

        if (isColVec(vecs[0])) {
            // In the case where we're working with column vectors

            // Initializing the matrix as the first vector in vecs
            Matrix concatenated = vecs[0];
            for (int i = 1; i < vecs.length; i++) {
                // Sticking on the remaining vectors in vecs
                concatenated = appendCol(concatenated, vecs[i]);
            }
            return concatenated;
        }

        else {
            // In the case where we're working with row vectors

            // Initializing the matrix as the first vector in vecs
            Matrix concatenated = vecs[0];
            for (int i = 1; i < vecs.length; i++) {
                // Sticking on the remaining vectors in vecs
                concatenated = appendRow(concatenated, vecs[i]);
            }
            return concatenated;
        }
    }


    /**
     * Transposes v. Helper for Matrix.transpose().
     * @return the transpose of v.
     * @throws InvalidMatrixException if v isn't a column or row vector.
     */
    private static Matrix transposeVec(Matrix v) throws InvalidMatrixException {
        if (!isVec(v)) {
            throw new InvalidMatrixException("Input must be a column vector or a row vector.");
        }

        if (isRowVec(v)) {
            double[][] newEntries = new double[v.cols][1];
            for (int i = 0; i < v.cols; i++) {
                newEntries[i][0] = v.entries[0][i];
            }

            return new Matrix(newEntries);
        }

        else {
            double[][] newEntries = new double[1][v.rows];
            for (int i = 0; i < v.rows; i++) {
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
        // Initializing the transposed matrix by transposing A's first row
        Matrix transposed = transposeVec(getRow(A, 1));

        for (int i = 1; i < A.rows; i++) {
            // Transposing each remaining row of A, then appending it to the accumulator matrix
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
     * @throws MatrixSizeMismatchException if newCol doesn't have as many entries as A does rows.
     */
    public static Matrix replaceCol(Matrix A, int colNum, Matrix newCol)
            throws AssertionError, MatrixSizeMismatchException {
        if (colNum <= 0 || colNum > A.cols) {
            throw new AssertionError("2nd input must be a positive number less than or equal to " +
                    "the number of columns in the 1st input.");
        }

        if (newCol.rows != A.rows) {
            throw new MatrixSizeMismatchException("3rd input must have the same number of rows as the 1st input.");
        }

        // Initializing the new matrix
        double[][] replacedEntries = new double[A.rows][A.cols];

        for (int i = 0; i < A.rows; i++) {
            for (int j = 0; j < A.cols; j++) {
                if (j == colNum - 1) {
                    // Only using the newCol entries when we've reached the column we're trying to replace
                    replacedEntries[i][j] = newCol.entries[i][0];
                }
                else {
                    replacedEntries[i][j] = A.entries[i][j];
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
     * @throws MatrixSizeMismatchException if newRow doesn't have as many entries as A does columns.
     */
    public static Matrix replaceRow(Matrix A, int rowNum, Matrix newRow)
            throws AssertionError, MatrixSizeMismatchException {
        if (rowNum <= 0 || rowNum > A.rows) {
            throw new AssertionError("2nd input must be a positive number less than or equal to " +
                    "the number of rows in the 1st input.");
        }

        if (newRow.cols != A.cols) {
            throw new MatrixSizeMismatchException("3rd input must have the same number of columns as the 1st input.");
        }

        // Initializing the new matrix
        double[][] replacedEntries = new double[A.rows][A.cols];

        for (int i = 0; i < A.rows; i++) {
            for (int j = 0; j < A.cols; j++) {
                if (i == rowNum - 1) {
                    // Only using the newRow entries when we've reached the row we're trying to replace
                    replacedEntries[i][j] = newRow.entries[0][j];
                }
                else {
                    replacedEntries[i][j] = A.entries[i][j];
                }
            }
        }

        return new Matrix(replacedEntries);
    }


    /**
     * Returns A with its col1'th and col2'th columns swapped. All indexing starts at 1.
     * @param A the matrix whose columns are being swapped
     * @param col1 the index of the first column being swapped
     * @param col2 the index of the second column being swapped
     * @return A new matrix with the col1'th and col2'th columns swapped.
     * @throws AssertionError if either col1 or col2 isn't a positive number less than or
     * equal to the number of columns in A.
     */
    public static Matrix swapCols(Matrix A, int col1, int col2) throws AssertionError {
        if ( (col1 <= 0 || col1 > A.cols) || (col2 <= 0 || col2 > A.cols) ) {
            throw new AssertionError("2nd and 3rd inputs must be " +
                    "positive numbers less than or equal to the number of columns in the 1st input.");
        }

        if (col1 == col2) {
            // Swapping a column with itself amounts to doing nothing with the matrix
            return copy(A);
        }

        // Copying A
        Matrix swapped = copy(A);

        // Getting the columns that are being swapped
        Matrix firstCol = getCol(swapped, col1);
        Matrix secondCol = getCol(swapped, col2);

        // Replacing the col1'th column of A with secondCol. Now two of A's columns are secondCol;
        // one at index col1 (the result of the replacement) and the other at index col2 (the original position of col2)
        swapped = replaceCol(swapped, col1, secondCol);

        // Replacing the col2'th column of A with firstCol. Now secondCol is at col1, and firstCol is at col2.
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

        // Same idea as in swapCols, just with rows this time

        if (row1 == row2) {
            return copy(A);
        }

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
     *    <li>if A is not a column vector, a new Matrix object representing A, but with its colInd'th column removed.</li>
     *    <li>if A is a column vector, the zero vector with the same dimensions as A.</li>
     * </ul>
     * @throws AssertionError if colInd isn't a positive integer less than or equal to the number of columns in A.
     */
    public static Matrix removeCol(Matrix A, int colInd) throws AssertionError {
        if (colInd <= 0 || colInd > A.cols) {
            throw new AssertionError("colInd must be a positive integer less than or equal to " +
                    "the number of columns in A.");
        }

        if (isColVec(A)) {
            // If we reach this point, colInd must equal 1
            return zeroMatrix(A.rows, 1);
        }

        // Initializing the new matrix
        Matrix removed;
        if (colInd == 1) {
            // If we're removing the first column, set removed to be the second column of A, then append the following
            // columns to it. In essence, we're ignoring the first column rather than deleting it.
            removed = getCol(A, 2);
            for (int j = 3; j < A.cols + 1; j++) {
                removed = appendCol(removed, getCol(A, j));
            }
        } else {
            // If we're removing a column that isn't the first one, set removed to be the first column of A, then
            // append all the following columns to it except for the colInd'th one.
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
        if (rowInd <= 0 || rowInd > A.rows) {
            throw new AssertionError("colInd must be a positive integer less than or equal to " +
                    "the number of columns in A.");
        }

        // Same idea as in removeCol, just with rows

        if (isRowVec(A)) {
            return zeroMatrix(1, A.cols);
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
     * @throws AssertionError if:
     * <ul>
     *     <li>1) m isn't a positive integer greater than or equal to the number of rows in A, or</li>
     *     <li>2) n isn't a positive integer greater than or equal to the number of columns in A</li>
     * </ul>
     */
    public static Matrix pad(Matrix A, int m, int n, double filler) throws AssertionError {
        if (m < A.rows || n < A.cols) {
            throw new AssertionError("m and n must be positive integers within the dimensions of A.");
        }
        if (m == A.rows && n == A.cols) {
            return copy(A);
        }

        // Initializing the padded matrix
        double[][] newEntries = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i < A.rows && j < A.cols) {
                    // If we're within the bounds of A's dimensions, use A's entries
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
// MATRIX OPERATIONS (COMMENTED)

    /**
     * Creates a new matrix with the same entries as A.
     * @return a distinct copy of A.
     */
    public static Matrix copy(Matrix A) {
        return new Matrix(A.entries);
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

        // By definition, the dot product of two vectors is the sum of the products of the corresponding
        // entries in each vector.

        double dot = 0;

        if (isColVec(v1)) {
            if (v1.rows != v2.rows) {
                throw new MatrixSizeMismatchException("Inputs must both be vectors of the same size.");
            }

            for (int i = 0; i < v1.rows; i++) {
                dot += v1.entries[i][0] * v2.entries[i][0];
            }
        }

        else {
            if (v1.cols != v2.cols) {
                throw new MatrixSizeMismatchException("Inputs must both be vectors of the same size.");
            }

            for (int j = 0; j < v1.cols; j++) {
                dot += v1.entries[0][j] * v2.entries[0][j];
            }
        }

        return dot;
    }


    /**
     * @return the length of v.
     * @throws InvalidMatrixException if v isn't a column or row vector.
     */
    public static double magn(Matrix v) throws InvalidMatrixException {
        if (!isVec(v)) {
            throw new InvalidMatrixException("Input must be a column vector or row vector.");
        }

        // By definition, the magnitude of a vector is just the square root of its dot product with itself.
        return Math.sqrt(dot(v, v));
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

        // The dot product can be equivalently defined as v1 (dot) v2 = ||v1|| * ||v2|| * cos(x), where x
        // is the angle between v1 and v2 in radians.

        return Math.acos( dot(v1, v2) / (magn(v1) * magn(v2)) );
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
     * Returns the normalized version of v.
     * @param v the vector being normalized
     * @return a new Matrix object representing the unit vector pointing in the same direction as v.
     * @throws InvalidMatrixException if v isn't a nonzero vector
     */
    public static Matrix normalize(Matrix v) throws InvalidMatrixException {
        if (!isVec(v) || magn(v) < tol) {
            throw new InvalidMatrixException("Input must be a nonzero vector.");
        }

        // By definition, the unit vector pointing in the same direction as v is u = v / ||v||.
        return scale(v, 1 / magn(v));
    }


    /**
     * Normalizes each vector in vecs.
     * @param vecs the list of vectors being normalized
     * @return an array of new Matrix objects representing the normalized versions of each of
     * the corresponding vector in vecs, in the other that they appear in vecs;
     * that is, vecs[i] -> normedVecs[i] for each valid i.
     * @throws InvalidMatrixException if vecs doesn't contain only nonzero vectors.
     */
    public static Matrix[] normalize(Matrix[] vecs) throws InvalidMatrixException {
        Matrix[] normedVecs = new Matrix[vecs.length];
        for (int i = 0; i < vecs.length; i++) {
            normedVecs[i] = normalize(vecs[i]);
        }
        return normedVecs;
    }


    /**
     * Returns the trace of A.
     * @param A the matrix in question.
     * @return the trace of A.
     * @throws InvalidMatrixException if A isn't square.
     */
    public static double trace(Matrix A) throws InvalidMatrixException {
        // Only square matrices have traces.
        if (!isSquare(A)) {
            throw new InvalidMatrixException("Input must be a square matrix.");
        }


        // A matrix's trace is defined to be the sum of its diagonal entries.
        // (It's also the sum of the matrix's eigenvalues, but that would obviously be much less efficient to use here.)
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
        if (!sameSize(A, B)) {
            throw new MatrixSizeMismatchException("Both matrices must have the same size.");
        }

        // Initializing the sum matrix
        double[][] sum = new double[A.rows][A.cols];
        for (int i = 0; i < A.rows; i++) {
            for (int j = 0; j < A.cols; j++) {
                // Adding corresponding entries
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
        double[][] scaled = new double[A.rows][A.cols];
        for (int i = 0; i < A.rows; i++) {
            for (int j = 0; j < A.cols; j++) {
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
        if (A.cols != B.rows) {
            throw new MatrixSizeMismatchException("The number of columns in the first argument " +
                    "must equal the number of rows in the second.");
        }

        // In subscript-summation notation, a matrix product's entries are defined as (AB)_{ij} = (A)_{ik} * (B)_{kj}.

        double[][] prod = new double[A.rows][B.cols];
        for (int i = 0; i < A.rows; i++) {
            for (int j = 0; j < B.cols; j++) {
                for (int k = 0; k < A.cols; k++) {
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
     * @throws InvalidMatrixException if:
     * <ul>
     *     <li>1) A isn't square, in general</li>
     *     <li>2) A isn't invertible, if n < 0.</li>
     * </ul>
     */
    public static Matrix power(Matrix A, int n) throws InvalidMatrixException {
        // A matrix power is only defined if the matrix is square.
        if (!isSquare(A)) { throw new InvalidMatrixException("Input must be a square matrix."); }

        // The zeroth power of a matrix is just the identity
        if (n == 0) {return new Matrix(A.rows);}

        else if (n > 0) {
            Matrix product = copy(A);
            for (int i = 1; i < n; i++) {
                // Starting at i = 1 because product is initialized as A, so we're starting at the first power already.
                product = mult(product, A);
            }
            return product;
        }

        else {
            // By definition, A^n = (A^-1)^{-n} when n is negative.
            if (!isInvertible(A)) { throw new InvalidMatrixException("Input must be invertible."); }
            Matrix product = copy(inverse(A));
            for (int i = 1; i < -n; i++) {
                product = mult(product, A);
            }
            return product;
        }
    }


    /**
     * Row reduces A via Gaussian elimination.
     * @param A the matrix being row-reduced
     * @param rref determines if the reduced matrix will be in reduced row echelon form
     * @return an array of these Matrix objects in the following order: <ul>
     *  <li>1) The row-reduced matrix. If rref is true, this will be in reduced row echelon form.</li>
     *  <li>2) A 1x1 matrix containing the determinant-altering factors picked up in the process of row reduction.</li>
     *  <li>3) A row matrix whose entries correspond to the indices of A's pivot columns. Indexing begins at 1. If this
     *  matrix only contains a zero, then A has no pivot columns.</li>
     * </ul>
     *
     * NOT COMPLETE
     */
    public static Matrix[] rowRed(Matrix A, boolean rref) {
        if ( A.equals(zeroMatrix(A.rows, A.cols)) || A.equals(new Matrix(A.cols)) ) {
            // If A is a zero or identity matrix, just give it back to the user.
            return new Matrix[] {copy(A), new Matrix(new double[][] { {1} } ) };
        }

        // Initializing the array that the function will return
        Matrix[] returnedArray = new Matrix[3];

        Matrix reduced = copy(A);

        // From here, this is the process:
        // 1) Start with the leftmost nonzero column. This is the current pivot column,
        // and the pivot position is the top entry.
        // 2) Select a nonzero entry in the pivot column as a pivot. If necessary, interchange rows to move this entry
        // into the pivot position.
        // 3) Use row replacements to zero out the entries below the pivot.
        // 4) Ignore the row containing the pivot position and all rows above it, if any.
        // Repeat steps 1 - 3 on this submatrix until there are no more nonzero rows left to modify.
        // After this step is done, the matrix will be in an echelon form.
        // 5) Beginning with the rightmost pivot and working upward to the leftmost pivot,
        // scale the pivot row to make the pivot a 1, then zero out the entries above the pivot.
        // After this step is done, the matrix will be in RREF.


        int smallerOfTwo = Math.min(A.rows, A.cols); // only using this to determine the length of the loop
        ArrayList<Integer> pivCols = new ArrayList<>(); // tracks the indices of the pivot columns
        double detFactors = 1; // accumulates the determinant-changing factors rising from swaps and scales
        int pivotsFound = 0; // tracks the number of pivots found
        int curColIndex = 1; // tracks the index of the current column
        boolean pivotsRemaining = true; // true if there are still pivots that can be used

        for (int j = 1; j < smallerOfTwo + 1; j++) {
            // Running from j = 1 to j = smallerOfTwo + 1 ensures that we check as many rows/columns as necessary since
            // the number of pivots in A is bounded from above by the lesser of its row count and column count.
            // (Starting from j = 1) to give the getter method calls nicer arguments

            Matrix curCol = getCol(reduced, curColIndex);
            boolean pivFound = false;
            if ( Math.abs(getEntry(curCol, pivotsFound + 1, 1)) < tol ) {
                // If we end up here, the spot where the pivot should be currently has a zero.
                // Look for a nonzero entry below it and swap them if one is found. If one doesn't exist,
                // move to the next loop iteration; this column won't contribute anything meaningful.
                for (int i = pivotsFound + 2; i < reduced.rows + 1; i++) {
                    // Start the search from the row right below where the pivot should be (pivotsFound + 1)
                    if ( Math.abs(getEntry(curCol, i, 1)) >= tol ) {
                        // If a nonzero entry is found, swap it into the pivot position
                        reduced = swapRows(reduced, pivotsFound + 1, i);
                        detFactors *= -1;
                        pivFound = true;
                        pivotsFound++;
                    }
                }
            }
            else {
                pivotsFound++;
                pivFound = true;
            }

            if (!pivFound) {
                // If a pivot wasn't found in this column, move onto the next column and search there instead

                curColIndex++; // Start searching in the column immediately to the right
                while (curColIndex < reduced.cols + 1) {
                    // Start searching in the column directly to the right of the latest one
                    curCol = getCol(reduced, curColIndex);
                    for (int i = pivotsFound + 1; i < reduced.rows + 1; i++) {
                        // Start the search at the pivot row
                        double currentEntry = getEntry(curCol, i, 1);
                        if ( Math.abs(currentEntry) >= tol ) {
                            // If a nonzero entry is found, swap it into the pivot position
                            reduced = swapRows(reduced, pivotsFound + 1, i);
                            detFactors *= -1;
                            pivFound = true;
                            pivotsFound++;
                            break;
                        }
                    }
                    if (pivFound) {
                        // Break out of the loop once a pivot is found
                        break;
                    }
                    if (curColIndex == reduced.cols) {
                        // At this point, all the columns have been searched, but a pivot wasn't found
                        pivotsRemaining = false;
                    }
                    // If we reach this point, a pivot still hasn't been found; increment curColIndex so that
                    // the next column can be checked.
                    curColIndex++;
                }
            }

            if (pivotsRemaining && pivFound) {
                // Using pivotsFound instead of pivotsFound + 1 because we just incremented
                // pivotsFound if we've reached this point
                double pivot = getEntry(reduced, pivotsFound, curColIndex);

                if (pivCols.size() == 1 && pivCols.get(0) == 0) {
                    pivCols.set(0, curColIndex);
                }

                else {
                    pivCols.add(curColIndex);
                }

                // Representing row replacements as a matrix
                Matrix replacements = zeroMatrix(reduced.rows, reduced.cols);

                for (int i = pivotsFound + 1; i < reduced.rows + 1; i++) {
                    // Starting at the row below the pivot that's being used
                    double currentEntry = getEntry(curCol, i, 1);
                    if (Math.abs(currentEntry) >= tol) {
                        // If the current entry in this column is nonzero, determine the appropriate scale factor using it.
                        // (technically I could remove this check since if the current entry is zero, the scale factor
                        // would be zero, but I'd rather not have unnecessary computations)
                        double scaleFactor = -(currentEntry / pivot);
                        Matrix scaledRow = scale(getRow(reduced, pivotsFound), scaleFactor);
                        replacements = replaceRow(replacements, i, scaledRow);
                    }
                }
                // Executing the replacements
                reduced = add(reduced, replacements);
            }


        }

        // At this point, the matrix is in an echelon form.
        if (!rref) {
            // Setting the elements of the returned array
            returnedArray[0] = reduced;
            returnedArray[1] = new Matrix( new double[][] { {detFactors} } );
            double[][] pivotCols = new double[1][pivCols.size()];
            if ( !(pivCols.size() == 1 && pivCols.get(0) == 0) ) {
                for (int j = 0; j < pivCols.size(); j++) {
                    pivotCols[0][j] = pivCols.get(j);
                }
            }

            returnedArray[2] = new Matrix(pivotCols);
            return returnedArray;
        }

        else {
            // This is the rref branch
            // Starting at the rightmost pivot and work up from there
            for (int k = pivotsFound - 1; k > 0; k--) {
                // Just tracking a bunch of useful data with these variables
                int pivColInd = pivCols.get(k);
                Matrix pivRow = getRow(reduced, pivotsFound);
                double pivot = getEntry(reduced, pivotsFound, pivColInd);

                // Scaling the pivot row by the reciprocal of the pivot
                reduced = replaceRow(reduced, pivotsFound, scale(pivRow, 1 / pivot)  );
                detFactors *= ( 1 / pivot );

                // Setting up the matrix of replacements
                Matrix replacements = zeroMatrix(A.rows, A.cols);
                for (int i = pivotsFound - 1; i > 0; i--) {
                    // If the current non-pivot entry in the pivot column is nonzero,
                    // scale the pivot row so that a replacement will zero out the non-pivot entry.
                    double nonzeroEntry = getEntry(reduced, i, pivColInd);
                    Matrix scaledRow = scale(getRow(reduced, pivotsFound), -nonzeroEntry  );
                    replacements = replaceRow(replacements, i, scaledRow);
                }
                reduced = add(reduced, replacements);
                pivotsFound--;
            }

            // Setting the elements of the returned array
            returnedArray[0] = reduced;
            returnedArray[1] = new Matrix( new double[][] { {detFactors} } );
            double[][] pivotCols = new double[1][pivCols.size()];
            if ( !(pivCols.size() == 1 && pivCols.get(0) == 0) ) {
                for (int j = 0; j < pivCols.size(); j++) {
                    pivotCols[0][j] = pivCols.get(j);
                }
            }

            returnedArray[2] = new Matrix(pivotCols);
            return returnedArray;
        }
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
            // By definition, if A is a diagonal matrix, then A^-1 is the matrix whose diagonal entries are
            // the reciprocals of the corresponding entries in A.
            Matrix inv = copy(A);
            for (int i = 0; i < A.rows; i++) {
                inv = replaceRow(inv, i, scale(getRow(inv, i), 1 / A.entries[i][i]) );
            }
            return inv;
        }

        // Augment A with the identity matrix, then row reduce it.
        Matrix reduced = rowRed( append(A, new Matrix(A.cols)), true )[0];

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
            // If A is a triangular matrix, then its determinant is the product of its diagonal entries.
            double det = 1;
            for (int i = 0; i < A.rows; i++) {
                det *= A.entries[i][i];
            }

            return det;
        }

        // You can compute the determinant of a matrix by row reducing it to an echelon form, computing the determinant
        // of the echelon form, and then dividing that by the factors accumulated by row interchanges and scales.
        Matrix[] redAndFactors = rowRed(A, false);
        return det(redAndFactors[0]) / getEntry(redAndFactors[1], 1, 1);
    }

//-------------------------------------------------------------------------------------------------------------
// SUBSPACES  (COMMENTED)

    /**
     * Returns the dimension of the subspace spanned by the vectors in vecs.
     * @param vecs the set of vectors spanning some subspace
     * @return the dimension of the spanned subspace
     * @throws MatrixSizeMismatchException if the vectors in vecs aren't column vectors with the same size.
     */
    public static int dimension(Matrix[] vecs) throws MatrixSizeMismatchException {
        if (vecs.length == 0) {
            return 0;
        }

        if ( !isColVec(vecs[0]) || !sameSize(vecs)) {
            throw new MatrixSizeMismatchException("vecs must contain columns vectors with the same size.");
        }

        // The only subspace defined to have zero dimension is the one only containing the zero vector.
        if ( vecs.length == 1 && vecs[0].equals(zeroMatrix(vecs[0].rows, 1)) ) {
            return 0;
        }

        // Using the Gram-Schmidt process to get a basis for the subspace spanned by vecs
        Matrix[] basis = gs(vecs, false);

        // Counting the number of nonzero vectors in basis
        int dimension = 0;
        Matrix zero = zeroMatrix(vecs[0].rows, 1);
        for (int i = 0; i < basis.length; i++) {
            // gs should remove any zero vectors from the set it returns, but this
            // check is here just in case. I could just return basis.length
            // and be done with it, but I'm too paranoid to do that.
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
        // A basis for a matrix's column space can be obtained by spanning its pivot columns.

        // Getting the indices of the pivot columns
        double[][] pivotColIndices = getEntries(rowRed(A, false)[2]);

        // Obtaining the columns of A corresponding to these indices
        Matrix[] pivotCols = new Matrix[pivotColIndices[0].length];
        for (int i = 0; i < pivotCols.length; i++) {
            pivotCols[i] = getCol(A, (int) pivotColIndices[0][i]);
        }

        return formMatrix(gs(pivotCols, normal));
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
     *
     * NOT COMPLETE
     */
    public static Matrix nullSpace(Matrix A, boolean normal) {
        // TODO: finish implementing this
        // Getting the rref of A and finding its non-pivot columns
        Matrix[] rowRed = rowRed(A, true);
        Matrix reduced = rowRed[0];
        double[][] frees = getEntries(rowRed[3]);
        System.out.println("Reduced: \n" + reduced);
        System.out.print("Frees: ");
        for (int i = 0; i < frees[0].length; i++) {
            System.out.print(frees[i] + ", ");
        }

        // Initializing the basis vectors
        // The dimension of A's null space is the number of non-pivot columns it has
        Matrix[] basis = new Matrix[frees[0].length];
        for (int k = 0; k < basis.length; k++) {
            basis[k] = singleOneCol(A.cols, (int) frees[0][k]);
        }

        // Iterating though the rows of A's rref
        for (int i = 1; i < A.rows + 1; i++) {
            // Getting the current row
            Matrix curRow = getRow(reduced, i);
            boolean pivotFound = false; // tracks whether we've found the pivot in this row yet
            int pivotLoc = 0; // logs the position of the pivot in this row
            int freesFound = 0; // tracks the number of free variables that have been found in this row

            // Iterating through the elements of the current row
            for (int j = 1; j < A.cols + 1; j++) {
                if ( !pivotFound && Math.abs(getEntry(curRow, 1, j)) < tol ) {
                    // If a pivot hasn't been found yet and the current entry is a 0, move to the next iteration;
                    // this isn't a pivot either.
                    continue;
                }
                if ( !pivotFound && Math.abs(1 - getEntry(curRow, 1, j)) < tol ) {
                    // If a pivot hasn't been found yet and the current entry is a 1, then we just found the
                    // pivot. From here, we're going to fill out the entries of each basis vector that
                    // constitute the basic variable the pivot represents.
                    pivotLoc = j;
                    pivotFound = true;
                }
                else if (pivotFound) {
                    if (arrayContains( frees[0], j)) {
                        Matrix coeff = new Matrix(new double[][] {{-getEntry(curRow, 1, j)}} );
                        System.out.println(pivotLoc);
                        basis[freesFound] = replaceRow(basis[freesFound], pivotLoc, coeff);
                        freesFound++;
                    }
                }
            }
        }

        //return formMatrix( gs(basis, normal) );
        return formMatrix(basis);
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
// THE SYSTEM SOLVER  (COMMENTED)

    /**
     * Helper for solve()
     */
    private static boolean arrayContains(double[] arr, int val) {
        for (double j : arr) {
            if (val == j) {
                return true;
            }
        }
        return false;
    }


    /**
     * Returns a particular solution to the equation Ax = b. This solution, if it exists, will be a vector p
     * such that Ap = b.
     * @param A the coefficient matrix
     * @param b the target vector
     * @return <ul>
     *     <li>1) If the system is consistent, a Matrix object representing a vector p such that Ap = b.</li>
     *     <li>2) If the system is inconsistent, a zero vector with as many entries as A has columns. </li>
     * </ul>
     * @throws MatrixSizeMismatchException if b isn't a column vector with as many rows as A.
     */
    public static Matrix solve(Matrix A, Matrix b)
            throws MatrixSizeMismatchException {
        if (b.rows != A.rows || !isColVec(b)) {
            throw new MatrixSizeMismatchException("b must be a column vector with as many rows as A.");
        }
        Matrix equation = append(A, b);

        try {
            int m = A.rows;
            int n = A.cols;
            Matrix[] rowRed = rowRed(equation, true);
            Matrix reduced = rowRed[0];
            Matrix zeRow = zeroMatrix(1, n);
            // Checking if the system is inconsistent
            for (int i = m; i > 0; i--) {
                // Starting from the bottom since any all-zero rows should be towards the bottom
                Matrix curRow = getRow(reduced, i);
                Matrix lhs = getSubmatrix(curRow, 1, 1, 1, n);
                double rhs = getEntry(curRow, 1, n + 1);
                if ( lhs.equals(zeRow) && Math.abs(rhs) >= tol ) {
                    // This branch is reached when there's a row of the form [0 0  ...  0  |  c] for some c != 0.
                    // In this case, the system is inconsistent.
                    throw new InconsistentSystemException("Equation was inconsistent.");
                }
            }

            // Getting the indices of the pivot columns
            double[][] basics = getEntries(rowRed[2]);

            // Getting the rightmost column
            Matrix rightmost = getCol(reduced, n + 1);

            // Getting the particular solution
            if (rightmost.equals(zeroMatrix(m, 1))) {
                return zeroMatrix(n, 1);
            }

            else {
                // The particular solution vector is always the rightmost column in the system's rref, with zeroes
                // injected in the positions corresponding to free variables
                double[][] particularEntries = new double[n][1];

                // Tracking the number of basic variables that have been accounted for
                int rightmostEntriesUsed = 0;

                // Iterate through the columns of A (sort of) and use the pivot columns to
                // fill out the entries of the particular solution.
                for (int j = 0; j < n; j++) {
                    if (arrayContains(basics[0], j)) {
                        particularEntries[j][0] = rightmost.entries[rightmostEntriesUsed][0];
                        rightmostEntriesUsed++;
                    }
                    else {
                        // All particularEntries elements are initialized to zero, so I technically don't need
                        // this else branch; I'm just keeping it here to make understanding the code easier.
                        particularEntries[j][0] = 0;
                    }
                }
                return new Matrix(particularEntries);
            }
        }

        catch (InconsistentSystemException ise) {
            System.out.println("The equation was inconsistent. Returning a zero vector.");
            return zeroMatrix(A.cols, 1);
        }
    }


//-------------------------------------------------------------------------------------------------------------
// ORTHOGONALITY AND LEAST SQUARES  (COMMENTED)

    /**
     * Returns the orthogonal projection of v onto projVec.
     * @param v the vector being projected
     * @param projVec the vector onto which v is being projected
     * @return a new Matrix object representing the orthogonal projection of v onto projVec.
     * @throws InvalidMatrixException if:<ul>
     *     <li>1) v and/or projVec isn't a column vector</li>
     *     <li>2) projVec is the zero vector.</li>
     * </ul>
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

        // Just using the projection formula
        double scaleFactor = dot(v, projVec) / dot(projVec, projVec);
        return scale(projVec, scaleFactor);
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
            // Orthogonal projections can only be computed in this way if
            // the set being projected onto is orthogonal.
            throw new InvalidMatrixException("vecs must be an orthogonal set.");
        }

        // Obtaining the nonzero vectors in vecs
        // (If the set is orthogonal, there shouldn't be any zero vectors in vecs, but this is here just in case.)
        Matrix zeCol = zeroMatrix(vecs[0].rows, 1);
        ArrayList<Matrix> nonzeros = new ArrayList<>();
        for (Matrix vec : vecs) {
            if (!vec.equals(zeCol)) {
                nonzeros.add(vec);
            }
        }

        // The projection of v onto the subspace spanned by vecs is the sum of
        // v's projections onto each nonzero vector in vecs
        Matrix projection = zeroMatrix(vecs[0].rows, 1); // the accumulator
        for (int i = 0; i < nonzeros.size(); i++) {
            Matrix curVec = nonzeros.get(i);
            projection = add(projection, proj(v, curVec));
        }

        return projection;
    }


    /**
     * Returns an orthogonal basis for the subspace spanned by the vectors in vecs.
     * If normal is true, this basis will be orthonormal.
     * @param vecs the set of vectors that the GSP is being applied to
     * @param normal determines whether the returned basis is orthonormal or not
     * @return an orthogonal basis for the subspace spanned by the vectors in vecs. If vecs only contains zero vectors,
     * this function returns an array containing a single zero vector.
     * @throws InvalidMatrixException if vecs doesn't contain only column vectors of the same size
     */
    public static Matrix[] gs(Matrix[] vecs, boolean normal) throws InvalidMatrixException {
        if ( vecs.length == 0 || !isColVec(vecs[0]) || !sameSize(vecs)) {
            throw new InvalidMatrixException("vecs must contain only column vectors of the same size.");
        }

        // Edge case for an array with one vector
        if (vecs.length == 1) {
            // Handling the case of a zero vector separately
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

        // Checking if vecs only has zero vectors
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
        // If vecs only has zero vectors, just return a zero vector
        if ( allZeroes ) {
            return new Matrix[] {zeroMatrix(vecs[0].rows, 1)};
        }

        // The Gram-Schmidt process is as follows:
        // 1) Initialize an empty list of vectors, then add the first nonzero vector in your set to it.
        // 2) Take the next vector in the set, compute its projection onto the span of the vectors currently
        // in the list, and subtract that projection from the vector. If this difference results in a zero vector
        // (i.e., if this vector is already in the span of the vectors in the list),
        // proceed to the next step. If this difference is nonzero, add it to the list of vectors.
        // 3) Repeat step 2 until all the vectors in the initial set have been used.

        // Creating the list that'll store the basis vectors
        ArrayList<Matrix> orthoBasis = new ArrayList<>();

        for (int i = firstNonzero; i < vecs.length; i++) {
            if (i == firstNonzero) {
                // If we're looking at the first nonzero vector, just add it to the basis
                orthoBasis.add(vecs[i]);
            }
            else {
                // Making an array out of the current basis so that I can call proj
                Matrix[] arr = new Matrix[orthoBasis.size()];
                arr = orthoBasis.toArray(arr);

                // Computing the projection
                Matrix proj = proj(vecs[i], arr);
                if (!proj.equals(vecs[i])) {
                    // If the projection of vecs[i] onto Span(arr) is equal to vecs[i],
                    // then vecs[i] is already in Span(arr). In this situation, we would not
                    // add vecs[i] to the basis because it would make the set linearly dependent,
                    // which isn't allowed in a basis.
                    Matrix ortho = sub(vecs[i], proj);
                    orthoBasis.add(ortho);
                }
            }
        }

        // At this point, orthoBasis should contain an orthogonal basis for Span(vecs).

        // Making an array out of the basis so that I can return it
        Matrix[] arr = new Matrix[orthoBasis.size()];
        arr = orthoBasis.toArray(arr);
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
        // Methodology behind this implementation: Suppose vecs contains vectors v_1, v_2, ... , v_k so that they span
        // the subspace Span(v_1, v_2, ... , v_k). This subspace is equivalent to the column space of the
        // matrix A = [v_1  v_2  ...  v_k]. It's provable that for any matrix A, the orthogonal complement
        // of Col(A) is Nul(A^T). So, by finding an orthogonal basis for Nul(A^T), you're also finding
        // an orthogonal basis for the orthogonal complement of Col(A), which then forms an orthogonal
        // basis for Span(v_1, v_2, ... , v_k).

        Matrix A = formMatrix(vecs);
        return nullSpace(transpose(A), normal);
    }


    /**
     * Returns a least-squares solution to the equation Ax = b.
     * If normal is true, this basis will be orthonormal.
     * @param A the coefficient matrix
     * @param b the target vector
     * @return a Matrix object p such that (A^T * A)p = (A^T)b
     * @throws MatrixSizeMismatchException if b isn't a column vector with as many rows as A.
     */
    public static Matrix leastSquares(Matrix A, Matrix b) throws MatrixSizeMismatchException {
        if (b.rows != A.rows || !isColVec(b)) {
            throw new MatrixSizeMismatchException("b must be a column vector with as many rows as A.");
        }
        // By definition, the set of least-squares solutions to Ax = b is the solution set
        // of the equation (A^T * A)x = (A^T)b.

        return solve( mult(transpose(A), A), mult(transpose(A), b));
    }


//-------------------------------------------------------------------------------------------------------------
// EIGENSTUFF  (COMMENTED)

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
            // Only square matrices have eigenvalues.
            throw new InvalidMatrixException("Input matrix must be square.");
        }

        // This method involves computing A's eigenvalues via a sequence of similarity transformations using
        // the QR factorization. As this process iterates, acc approaches a triangular matrix, at which point its
        // eigenvalues are its diagonal entries. Since similarity transformations preserve eigenvalues, these
        // are also A's eigenvalues. If A has complex eigenvalues, though, this process doesn't approach
        // a triangular matrix, but I'm not going to handle that situation.

        Matrix acc = copy(A);
        for (int i = 0; i < 500; i++) {
            Matrix[] curQR = QR(acc);
            acc = mult( curQR[1], curQR[0] );
        }

        if (!isTriangular(acc)) {
            throw new InvalidMatrixException("Input matrix likely has complex eigenvalues.");
        }

        double[] eigens = new double[A.cols];
        for (int i = 0; i < eigens.length; i++) {
            eigens[i] = acc.entries[i][i];
        }

        // Sorting the eigenvalues in increasing order, then reversing the order
        // (I couldn't find a reverseSort function)
        Arrays.sort(eigens);
        double[] copy = Arrays.copyOf(eigens, eigens.length);
        for (int i = 0; i < eigens.length; i++) {
            eigens[i] = copy[eigens.length - i - 1];
        }

        return eigens;
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
        // Getting A's eigenvalues
        double[] eigens = uniques(eigenvalues(A));

        Matrix[] eigenspaces = new Matrix[eigens.length];
        Matrix I = new Matrix(A.cols);
        for (int i = 0; i < eigenspaces.length; i++) {
            // For each eigenvalue L, the L-eigenspace is defined to be Nul(A - LI).
            eigenspaces[i] = nullSpace( sub(A, scale(I, eigens[i])), normal );
        }
        return eigenspaces;
    }


    /**
     * Returns an approximation of A's singular values.
     * @param A the matrix in question
     * @return an array containing the estimation of A's singular values, arranged in decreasing order.
     */
    public static double[] singularValues(Matrix A) {
        // By definition, the singular values of A are the square roots of the eigenvalues of A^TA.
        // While I could just use that fact and be done with it, a generally more optimal method exists,
        // and it involves the fact that A^TA and AA^T has the same nonzero eigenvalues with the same multiplicities.
        // As such, if AA^T is smaller than A^TA, we can compute its eigenvalues instead. From there, we can infer
        // the multiplicity of the zero eigenvalue of A^TA.

        // When A is mxn, A^TA is nxn and AA^T is mxm.
        double[] eigens;
        double[] sings = new double[A.cols];
        if (A.rows < A.cols) {
            // If m < n, use AA^T.
            eigens = eigenvalues( mult(A, transpose(A)) );
        }
        else {
            // If m >= n, use A^TA.
            eigens = eigenvalues( mult( transpose(A), A ) );
        }

        for (int k = 0; k < eigens.length; k++) {
            // I only need to loop up to eigens.length because, in either case, the loop accomplishes its task
            // correctly. If eigens used A^TA, then we can immediately see that this loop does its job. If eigens
            // used AA^T, then eigens only contains the eigenvalues of AA^T. These only account for some of A's
            // singular values; the remaining singular values must be zero. However, the elements of sings are
            // initialized as zero, so this is already taken care of.

            sings[k] = Math.sqrt(eigens[k]);
        }
        return sings;
    }


//-------------------------------------------------------------------------------------------------------------
// MATRIX FACTORIZATIONS  (COMMENTED)

    /**
     * Returns a QR factorization of A
     * @param A the matrix being factored.
     * @return an array with two elements. The first element is the Q matrix, and the second is the R matrix.
     */
    public static Matrix[] QR(Matrix A) {
        // If A is an mxn matrix and rank(A) = r, a QR factorization of A has the following properties:
        // Q: an mxn matrix whose first r columns form an orthonormal basis for Col(A) and whose last m - r columns
        // form an orthonormal basis for Nul(A^T)
        // R: an nxn upper triangular matrix with nonnegative entries on its diagonal

        // Q can be computed by forming an orthonormal basis for Col(A) and putting those vectors into a matrix, then
        // appending an orthonormal basis for Nul(A^T) if needed.
        // R is computed as R = (Q^T)A.

        // ONB for Col(A)
        Matrix Q = columnSpace(A, true);

        if (Q.cols < A.cols) {
            // If we're missing some columns, then A is rank-deficient. Find an orthonormal basis for
            // Nul(A^T) and attach those vectors to the columns already in Q.
            Matrix colAPerp = orthoComp(getCols(Q), true); // Col(A)-perp = Nul(A^T)
            Q = append(Q, colAPerp);
        }
        Matrix R = mult( transpose(Q), A );

        return new Matrix[] {Q, R};
    }


    /**
     * Returns matrices P and D, if they exist, such that D is diagonal and A = PDP^-1. P will be constructed so that
     * the basis of each eigenspace will be orthonormal, and the bases will be arranged from left to right in order
     * of decreasing eigenvalues. The D matrix will be the diagonal matrix of A's eigenvalues arranged down the
     * diagonal in decreasing order.
     * @param A the matrix being diagonalized
     * @return an array containing two Matrix objects. The first element is the P matrix, and the second element is
     * the D matrix.
     * @throws InvalidMatrixException if <ul>
     *     <li>A isn't square.</li>
     *     <li>A has complex eigenvalues (I'm not going to attempt handling complex numbers anytime soon, if ever.)</li>
     *     <li>A cannot be diagonalized (not enough linearly independent eigenvectors).</li>
     * </ul>
     */
    public static Matrix[] diagonalize(Matrix A) throws InvalidMatrixException {
        // Getting the eigenvalues and eigenspaces of A
        double[] eigenvalues = eigenvalues(A);
        Matrix[] eigenvectors = eigenvectors(A, true);

        // Initializing P as the first eigenspace and appending the remaining eigenspaces
        Matrix P = eigenvectors[0];
        for (int i = 1; i < eigenvectors.length; i++) {
            P = append(P, eigenvectors[i]);
        }

        // If P doesn't have as many columns as A, then its eigenspaces can't form a basis for R^n, meaning that
        // A isn't diagonalizable.
        if (P.cols != A.cols) {
            throw new InvalidMatrixException("Input matrix does not have " + A.cols + " linearly " +
                    "independent eigenvectors.");
        }

        // If we reach this point, A is diagonalizable and P's been computed. Just toss A's eigenvalues into
        // a diagonal matrix in the order corresponding to that of the eigenspaces in P.
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
        // If A is an mxn matrix such that rank(A) = r, an SVD of A is the product A = USV^T, where each matrix
        // has the following properties:
        // S: an mxn block diagonal matrix containing A's nonzero singular values. In essence, it's an rxr matrix
        // containing A's nonzero singular values arranged in decreasing order down the diagonal, then padded with
        // zeroes until it's mxn.
        // V: an nxn matrix whose columns are eigenvectors of A^TA that form an ONB for R^n, arranged in order
        // of decreasing eigenvalue.
        // U: an mxn matrix whose first r columns satisfy u_i = (1 / s_i)Av_i--where s_i is the ith nonzero
        // singular value of A and v_i is ith column of V--and the last m-r columns form an orthonormal basis
        // for Nul(A^T).


        // Forming S
        // Getting the nonzero singular values
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
        Matrix S = pad( diag(nonzeroSings), A.rows, A.cols, 0 );


        // Forming V
        Matrix V = diagonalize( mult(transpose(A), A) )[0];


        // Forming U
        // Starting with an orthonormal basis for Col(A) using the columns of V
        Matrix U = zeroMatrix(A.rows, 1);
        for (int j = 0; j < nonzeroSings.length; j++) {
            Matrix uCol = scale(mult( A, getCol(V, j + 1) ), 1 / nonzeroSings[j]);
            if (j == 0) {
                // In this case, U is just a zero vector; replace it with uCol.
                U = replaceCol(U, j + 1, uCol);
            }
            else {
                // In this case, U has some nonzero vectors in it; attach uCol to it.
                U = append(U, uCol);
            }
        }
        if (U.cols < A.cols) {
            // If we're here, we need to fill the rest of U with an orthonormal basis for Nul(A^T)
            Matrix nulAT = orthoComp(getCols(columnSpace(A, true)), true);
            U = append(U, nulAT);
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


    /**
     * Returns an array containing the unique elements of arr without altering the order elements appear in.
     */
    private static double[] uniques(double[] arr) {
        // I'm aware that I could use a Set to easily get around duplicates, but I'm brute-forcing
        // it purely because of the possibility of numerical instability. Two values could be very close to each other
        // and effectively be equal, so I wouldn't want them to be considered distinct.

        // Initializing the list of unique values
        ArrayList<Double> uniquesList = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            if (i == 0) {
                // At this point, uniquesList is empty, so just toss this element in there.
                uniquesList.add(arr[i]);
                continue;
            }

            for (int j = 0; j < uniquesList.size(); j++) {
                // Getting the difference between the current value and
                // comparing it to a value in uniquesList
                double diff = Math.abs(uniquesList.get(j) - arr[i]);
                if (diff <= tol) {
                    // If this difference is within the tolerance range, then the two values are
                    // effectively equal, so arr[i] should be added to uniquesList.
                    break;
                }

                if (j == uniquesList.size() - 1) {
                    // If we reached the final value in uniquesList and the difference was outside
                    // the tolerance range, then arr[i] is considered to be unique from
                    // the values in uniquesList and should thus be added to it.
                    uniquesList.add(arr[i]);
                }

            }
        }
        // toArray doesn't work on primitive types, so I have to do this manually
        double[] uniques = new double[uniquesList.size()];
        for (int i = 0; i < uniques.length; i++) {
            uniques[i] = uniquesList.get(i);
        }

        return uniques;
    }


    private static double round(double num, int places) {
        return Math.round( num * Math.pow(10, places) ) / Math.pow(10, places);
    }
}
