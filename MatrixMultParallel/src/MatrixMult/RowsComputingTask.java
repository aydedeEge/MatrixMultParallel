package MatrixMult;

public class RowsComputingTask implements Runnable {

	private double[][] a;
	private double[][] b;
	private int indexStart;
	private int indexEnd;
	private double[][] result;

	public RowsComputingTask(double[][] a, double[][] b, int indexStart, int indexEnd, double[][] result) {

		this.a = a;
		this.b = b;
		this.indexStart = indexStart;
		this.indexEnd = indexEnd;
		this.result = result;
	}

	@Override
	public void run() {
		for (int j = indexStart; j < indexEnd; j++) {
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
