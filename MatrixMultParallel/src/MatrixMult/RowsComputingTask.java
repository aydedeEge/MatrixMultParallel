package MatrixMult;

public class RowsComputingTask implements Runnable {

	private double[][] a;
	private double[][] b;
	private int indexRowStart;
	private int indexRowEnd;
	private double[][] result;

	public RowsComputingTask(double[][] a, double[][] b, int indexRowStart, int indexRowEnd, double[][] result) {
		this.a = a;
		this.b = b;
		this.indexRowStart = indexRowStart;
		this.indexRowEnd = indexRowEnd;
		this.result = result;
	}

	@Override
	public void run() { // compute the rows assigned to this task and put it in the result matrix
		for (int j = indexRowStart; j < indexRowEnd; j++) {
			for (int i = 0; i < result[j].length; i++) {
				double entry = 0;
				for (int k = 0; k < a.length; k++) {
					entry += b[k][i] * a[j][k];
				}
				result[i][j] = entry;
			}
		}

	}

}
