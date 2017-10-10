package MatrixMult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
	public static int NUMBER_OF_THREADS = 2;
	public static int MATRIX_SIZE = 1000;

	public static void main(String[] args) {
		double[][] a = MatrixHelper.randomMatrixGenerator(MATRIX_SIZE, MATRIX_SIZE);
		double[][] b = MatrixHelper.randomMatrixGenerator(MATRIX_SIZE, MATRIX_SIZE);

		// compute sequentially
		long startTime = System.nanoTime();
		double[][] cSequential = sequentialMultiplyMatrix(a, b);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000; // divide by 1000000 to get milliseconds.
		System.out.println("Sequential computation time : " + duration + "ms");

		// compute in parallel
		startTime = System.nanoTime();
		double[][] cParallele = parallelMultiplyMatrix(a, b);
		endTime = System.nanoTime();
		duration = (endTime - startTime) / 1000000; // divide by 1000000 to get milliseconds.
		System.out.println("Parallel computation time : " + duration + "ms");

		// Not necessary validate the result
		System.out.println("Both same result : " + MatrixHelper.validateMatrixResult(cParallele, cSequential));
	}

	public static double[][] sequentialMultiplyMatrix(double[][] a, double[][] b) {

		if (a[0].length != b.length) { // to ensure that the size of a row in A and the size of a column in B is the
										// same. (For clarity)
			return null;
		}
		double[][] result = new double[a.length][b[0].length];

		for (int j = 0; j < result.length; j++) {
			for (int i = 0; i < result[j].length; i++) {
				double entry = 0;

				for (int k = 0; k < a.length; k++) {
					entry += b[k][i] * a[j][k];
				}
				result[i][j] = entry;
			}
		}
		return result;
	}

	public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b) {
		if (a[0].length != b.length) { // to ensure that the size of a row in A and the size of a column in B is the
										// same.
			return null;
		}
		double[][] result = new double[a.length][b[0].length];
		int numOfRowsPerTask = result.length / NUMBER_OF_THREADS; // divide the rows to compute between the threads, the
																	// last thread will take the remaining rows if it's
																	// not even

		ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
		int index = 0;
		for (int i = 0; i < NUMBER_OF_THREADS - 1; i++) {
			int startIndex = index;
			int endIndex = index + numOfRowsPerTask;
			index = endIndex;
			executor.execute(new RowsComputingTask(a, b, startIndex, endIndex, result));
		}
		executor.execute(new RowsComputingTask(a, b, index, result.length, result)); // give the last rows to the last
																						// task

		// Shut down the executor
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS); // wait for it to finish
		} catch (InterruptedException e) {

		}
		return result;
	}

}
