
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class was provided by the professor, Ryan Hankins, and the execute method
 * was changed to run concurrently rather than sequentially. This class 
 * creates a randomized matrix, solves the matrix in measured seconds, and 
 * does a compare to make sure the solve is valid.
 * @author Sharon Shin
 *
 */

public class FloydWarshall {
    private static final int I = Integer.MAX_VALUE; // Infinity
    private static final int dim = 6000;
    private static double fill = 0.3;
    private static int maxDistance = 100;
    private static int adjacencyMatrix[][] = new int[dim][dim];
    private static int d[][] = new int[dim][dim];

    /**
     * This method generates a randomized matrix of dim size.
     */
    private static void generateMatrix() {
        Random random = new Random();
        for (int i = 0; i < dim; i++)
        {
            for (int j = 0; j < dim; j++)
            {
                if (i != j)
                    adjacencyMatrix[i][j] = I;
            }
        }
        for (int i = 0; i < dim * dim * fill; i++)
        {
            adjacencyMatrix[random.nextInt(dim)][random.nextInt(dim)] =
                random.nextInt(maxDistance + 1);
        }
    }

    /**
     * This method is executed and solves the Floyd Warhall algorithm
     * concurrently with a fixed thread pool. 
     */
    private static void execute() {
    	ExecutorService exec = Executors.newFixedThreadPool(10);
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                d[i][j] = adjacencyMatrix[i][j]; // using new matrix c
                if (i == j) {
                    d[i][j] = 0;
                }
            }
        }
        for (int k = 0; k < dim; k++) {
            LinkedList<Future<Boolean>> futureList = new LinkedList<Future<Boolean>>();
            for (int i = 0; i < dim; i++) {
                Callable<Boolean> callable = new FloydThread(k, i, dim, d);
                Future<Boolean> future = exec.submit(callable);
                futureList.add(future);

            }
            for (Future<Boolean> fut : futureList) {
                while (!(fut.isDone()) || fut == null) {};
            }
        }
        exec.shutdown();
    }

    /**
     * This method prints the matrix in the parameter.
     * @param integer matrix
     */
    private static void print(int matrix[][]) {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (matrix[i][j] == I) {
                    System.out.print("I" + " ");
                } else {
                    System.out.print(matrix[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    /**
     * This method compares two parameter matrixes to make sure
     * they are the same. 
     * @param matrix1
     * @param matrix2
     */
    private static void compare (int matrix1[][], int matrix2[][]) {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (matrix1[i][j] != matrix2[i][j])
                {
                    System.out.println("Comparison failed");
                }
            }
        }
        System.out.println("Comparison succeeded");
    }
    
    /**
     * Acts as the start button when running this class.
     * @param args
     */
    public static void main(String[] args) {
        long start, end;
        System.out.println("Generating Matrix");
        generateMatrix();
        start = System.nanoTime();
        execute();
        end = System.nanoTime();
        System.out.println("time consumed: " + (double)(end - start) / 1000000000);
        compare(d, d);
    }
}
