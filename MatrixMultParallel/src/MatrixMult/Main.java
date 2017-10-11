package MatrixMult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Main {
	public static int NUMBER_OF_THREADS;
	public static int MATRIX_SIZE;

	public static void main(String[] args) {
		NUMBER_OF_THREADS = 8;
		MATRIX_SIZE = 50;
		// double[][] a = MatrixHelper.randomMatrixGenerator(MATRIX_SIZE, MATRIX_SIZE);
		// double[][] b = MatrixHelper.randomMatrixGenerator(MATRIX_SIZE, MATRIX_SIZE);
		//
		// // compute sequentially
		// long duration = getAverageSequentialTime(a, b);
		// System.out.println("Sequential computation time : " + duration + "ms");
		//
		// // compute in parallel
		// duration = getAverageParallelTime(a, b);
		// System.out.println("Parallel computation time : " + duration + "ms");
		plot15();
		plot15HighScale();

	}

	public static void plot14() {
		double[][] a = MatrixHelper.randomMatrixGenerator(MATRIX_SIZE, MATRIX_SIZE);
		double[][] b = MatrixHelper.randomMatrixGenerator(MATRIX_SIZE, MATRIX_SIZE);
		// plot parallel time by num of threads
		for (int i = 2; i < 8; i++) {
			System.out.println(NUMBER_OF_THREADS + ";" + getAverageParallelTime(a, b));
			NUMBER_OF_THREADS = i * 2;
		}

	}

	public static void plot15() {
		// plot parallel time by num of threads
		for (int i = 0; i < 25; i++) {
			double[][] a = MatrixHelper.randomMatrixGenerator(MATRIX_SIZE, MATRIX_SIZE);
			double[][] b = MatrixHelper.randomMatrixGenerator(MATRIX_SIZE, MATRIX_SIZE);
			
			System.out.println(
					MATRIX_SIZE + "   " + getAverageParallelTime(a, b) + "   " + getAverageSequentialTime(a, b));
			MATRIX_SIZE+=50;

		}
	}
	public static void plot15HighScale() {
		System.out.println("High scale");
		// plot parallel time by num of threads
		for (int i = 0; i < 15; i++) {
			double[][] a = MatrixHelper.randomMatrixGenerator(MATRIX_SIZE, MATRIX_SIZE);
			double[][] b = MatrixHelper.randomMatrixGenerator(MATRIX_SIZE, MATRIX_SIZE);
			
			System.out.println(
					MATRIX_SIZE + "   " + getAverageParallelTime(a, b) + "   " + getAverageSequentialTime(a, b));
			MATRIX_SIZE+=200;

		}
	}
	public static long getAverageSequentialTime(double[][] a, double[][] b) {
		long averageDurationMs = 0;
		for (int i = 0; i < 3; i++) {
			long startTime = System.nanoTime();
			sequentialMultiplyMatrix(a, b);
			long endTime = System.nanoTime();
			long duration = (endTime - startTime) / 1000000; // divide by 1000000 to get milliseconds.
			averageDurationMs+=duration;
		}
		return averageDurationMs / 3;
	}

	public static long getAverageParallelTime(double[][] a, double[][] b) {
		long averageDurationMs = 0;
		for (int i = 0; i < 3; i++) {
			long startTime = System.nanoTime();
			parallelMultiplyMatrix(a, b);
			long endTime = System.nanoTime();
			long duration = (endTime - startTime) / 1000000;// divide by 1000000 to get milliseconds.
			averageDurationMs+=duration;
		}
		return averageDurationMs / 3; 
	}

	public static double[][] sequentialMultiplyMatrix(double[][] a, double[][] b) {
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
		double[][] result = new double[a.length][b[0].length];
		int numOfRowsPerTask = result.length / NUMBER_OF_THREADS;
		ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
		int rowIndex = 0;
		for (int i = 0; i < NUMBER_OF_THREADS - 1; i++) {
			int startIndex = rowIndex;
			int endIndex = rowIndex + numOfRowsPerTask;
			rowIndex = endIndex;
			executor.execute(new RowsComputingTask(a, b, startIndex, endIndex, result));
		}
		executor.execute(new RowsComputingTask(a, b, rowIndex, result.length, result)); // give the last rows to the
																						// last task
		// Shut down the executor
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS); // wait for it to finish
		} catch (InterruptedException e) {

		}
		return result;
	}

}
