package MatrixMult;

import java.util.Random;

public class MatrixHelper {

	public static void printMatrix(double[][] matrix) {
		for (int j = 0; j < matrix.length; j++) {
			for (int i = 0; i < matrix[j].length; i++) {
				System.out.print(matrix[i][j] + " " + i + "," + j + "   ");
			}
			System.out.println();
		}
	}

	public static double[][] randomMatrixGenerator(int m, int n) {
		Random rand = new Random();

		double[][] randomMatrix = new double[n][m];
		for (int j = 0; j < n; j++) {
			for (int i = 0; i < m; i++) {
				randomMatrix[j][i] = rand.nextDouble() + 1;
			}
		}
		return randomMatrix;
	}

	public static boolean validateMatrixResult(double[][] a, double[][] b) {
		for (int j = 0; j < a.length; j++) {
			for (int i = 0; i < a[j].length; i++) {
				if (a[i][j] != b[i][j]) {
					return false;
				}

			}
		}
		return true;
	}

}
