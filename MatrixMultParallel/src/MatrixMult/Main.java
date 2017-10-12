package MatrixMult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Main {
	public static int NUMBER_OF_THREADS;
	public static int MATRIX_SIZE;

	public static void main(String[] args) {

		if (args.length == 2) { // take input parameters
			NUMBER_OF_THREADS = Integer.parseInt(args[0]);
			MATRIX_SIZE = Integer.parseInt(args[1]);
		} else { // put default parameters, 4 core, 2000X2000 matrix
			NUMBER_OF_THREADS = 8;
			MATRIX_SIZE = 2000;
		}
		System.out.println("Q 1.3)");
		System.out.println();
		q13();
		System.out.println();
		System.out.println("--------------------------------");
		System.out.println();
		System.out.println("Q 1.4)");

		System.out.println();
		q14();
		System.out.println();
		System.out.println("--------------------------------");
		System.out.println();
		System.out.println("Q 1.5)");

		System.out.println();
		q15();
		q15HighScale();
		System.out.println();
		System.out.println("--------------------------------");
		System.out.println();

	}

	public static void q13() {
		double[][] a = MatrixHelper.randomMatrixGenerator(MATRIX_SIZE, MATRIX_SIZE);
		double[][] b = MatrixHelper.randomMatrixGenerator(MATRIX_SIZE, MATRIX_SIZE);
		System.out.println("Computing a " + MATRIX_SIZE + "X" + MATRIX_SIZE + " matrix sequentially... ");
		System.out.println("Average computation time : " + getAverageSequentialTime(a, b) + " ms");
		System.out.println("Computing a " + MATRIX_SIZE + "X" + MATRIX_SIZE + " matrix in parallel with "
				+ NUMBER_OF_THREADS + " threads... ");
		System.out.println("Average computation time : " + getAverageParallelTime(a, b) + " ms");
	}

	public static void q14() {
		double[][] a = MatrixHelper.randomMatrixGenerator(MATRIX_SIZE, MATRIX_SIZE);
		double[][] b = MatrixHelper.randomMatrixGenerator(MATRIX_SIZE, MATRIX_SIZE);
		System.out.println("Computing a " + MATRIX_SIZE + "X" + MATRIX_SIZE + " in parallel");
		System.out.println("Num of threads:	average ms");
		for (int i = 1; i < 8; i++) {
			NUMBER_OF_THREADS = i * 2;
			System.out.println(NUMBER_OF_THREADS + "		:" + getAverageParallelTime(a, b) );
		}

	}

	public static void q15() {
		// plot parallel time by size of matrix
		MATRIX_SIZE = 0;
		NUMBER_OF_THREADS  = 8;
		System.out.println("matrix size : sequential ms : parallel(8 threads) ms");
		
		for (int i = 0; i < 15; i++) {
			MATRIX_SIZE += 50;
			double[][] a = MatrixHelper.randomMatrixGenerator(MATRIX_SIZE, MATRIX_SIZE);
			double[][] b = MatrixHelper.randomMatrixGenerator(MATRIX_SIZE, MATRIX_SIZE);
			System.out.println(MATRIX_SIZE + "  		:" + getAverageSequentialTime(a, b) + " 		:"
					+ getAverageParallelTime(a, b));

		}
	}

	public static void q15HighScale() {
		System.out.println("Higher scale");
		// plot parallel time by num of threads
		MATRIX_SIZE = 0;
		NUMBER_OF_THREADS = 8;
		System.out.println("matrix size : sequential ms : parallel (8 threads) ms");
		
		for (int i = 0; i < 15; i++) {
			MATRIX_SIZE += 200;
			double[][] a = MatrixHelper.randomMatrixGenerator(MATRIX_SIZE, MATRIX_SIZE);
			double[][] b = MatrixHelper.randomMatrixGenerator(MATRIX_SIZE, MATRIX_SIZE);
			System.out.println(MATRIX_SIZE + "  		:" + getAverageSequentialTime(a, b) + " 		:"
					+ getAverageParallelTime(a, b));

		}
	}

	public static long getAverageSequentialTime(double[][] a, double[][] b) {
		long averageDurationMs = 0;
		for (int i = 0; i < 3; i++) {
			long startTime = System.nanoTime();
			sequentialMultiplyMatrix(a, b);
			long endTime = System.nanoTime();
			long duration = (endTime - startTime) / 1000000; // divide by 1000000 to get milliseconds.
			averageDurationMs += duration;
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
			averageDurationMs += duration;
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
