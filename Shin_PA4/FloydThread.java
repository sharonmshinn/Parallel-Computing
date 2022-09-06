import java.util.concurrent.Callable;

/**
 * This class represents the work that a single Callable will do when 
 * executing. This class has a constructor that can pass in the i, k, dim, and 
 * matrix to solve it's portion of the matrix.
 * @author Sharon Shin
 *
 */

public class FloydThread implements Callable<Boolean>{
	int i, k;
	int dim;
	int d[][];
	private static final int I = Integer.MAX_VALUE;
	
	/**
	 * Constructor that will pass in the necessary information to solve
	 * the problem row by row.
	 * @param i
	 * @param k
	 * @param dim
	 * @param matrix
	 */
	FloydThread(int i, int k, int dim, int matrix[][]) {
		this.i = i;
		this.k = k;
		this.dim = dim;
		this.d = matrix;
	}

	@Override
	public Boolean call() {
        for (int j = 0; j < dim; j++) {
            if (d[i][k] == I || d[k][j] == I) {
                continue;
            } else if (d[i][j] > d[i][k] + d[k][j]) {
                d[i][j] = d[i][k] + d[k][j];
            }
        }
		return true;
	}

}
