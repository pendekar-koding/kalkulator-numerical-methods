package solver;

import model.SeidelResult;
import java.util.Arrays;

public class GaussSeidelSolver {

    /**
     * Menyelesaikan sistem persamaan linear Ax = b secara iteratif menggunakan metode Gauss-Seidel.
     * Setiap iterasi memperbarui nilai x menggunakan nilai terbaru yang sudah dihitung.
     * Berhenti saat error < toleransi atau mencapai batas iterasi maksimum.
     */
    public static SeidelResult solve(double[][] A, double[] b, double[] x0, double tol, int maxIter) {
        int n = A.length;
        double[] x = Arrays.copyOf(x0, x0.length);
        StringBuilder log = new StringBuilder();
        double error = Double.POSITIVE_INFINITY;
        boolean converged = false;

        for (int i = 0; i < n; i++) {
            if (Math.abs(A[i][i]) < 1e-12) {
                throw new ArithmeticException("Elemen diagonal nol terdeteksi. Gauss-Seidel tidak dapat dilanjutkan.");
            }
        }

        for (int iter = 1; iter <= maxIter; iter++) {
            double[] old = Arrays.copyOf(x, n);
            for (int i = 0; i < n; i++) {
                double sum = b[i];
                for (int j = 0; j < n; j++) {
                    if (j != i) sum -= A[i][j] * x[j];
                }
                x[i] = sum / A[i][i];
            }

            error = 0;
            for (int i = 0; i < n; i++) error = Math.max(error, Math.abs(x[i] - old[i]));

            log.append(String.format("Iterasi %d: ", iter));
            for (int i = 0; i < n; i++) log.append(String.format("x%d=%.10f ", i + 1, x[i]));
            log.append(String.format(" | error=%.10e%n", error));

            if (error < tol) { converged = true; break; }
        }

        return new SeidelResult(x, converged, error, log.toString());
    }
}
