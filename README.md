This is my attempt at a Java implementation of a calculator that can perform operations on real-valued matrices.
For the time being, efficiency isn't the goal; functionality is. I want to get the functions working, then optimize them afterwards.

Last Updated: August 16, 2026

Currently implemented functionality (NT = not tested):
- Matrix Addition
- Matrix Multiplication
- Matrix Transpose
- Matrix Scaling
- Submatrices
- Matrix Padding
- Row/Column Removal/Swapping/Replacement/Appending
- Dot Products on Vectors
- Gaussian Elimination
- Determinants
- Matrix Inverse
- Orthogonal Projections NT
- The Gram-Schmidt Process NT
- Linear System Solver
- Column/Null Space Basis Computations NT
- Least Squares Approximation NT
- Orthogonal Complements of Subspaces NT
- Eigenvalue/Eigenvector Computations NT
- Diagonalization NT
- QR Factorization NT
- Singular Value Decomposition NT

Functionality that I may implement:
- Change-of-basis matrix computations
- Checking for linear independence of vectors

AI Usage:
- Gemini
  - Used to verify my thought process for the row reduction algorithm
  - Used to teach myself how to algorithmically compute eigenvalues
  - Used to generate test cases for several functions
