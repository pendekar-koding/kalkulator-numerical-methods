package solver;

import model.GaussResult;
import java.util.Arrays;

public class GaussEliminationSolver {

    /**
     * Menyelesaikan sistem persamaan linear Ax = b menggunakan Eliminasi Gauss dengan partial pivoting.
     * Matriks direduksi ke bentuk segitiga atas, lalu solusi diperoleh melalui substitusi balik.
     */
    public static GaussResult solve(double[][] A, double[] b) {
        int n = A.length;
        double[][] a = copyMatrix(A);
        double[] rhs = Arrays.copyOf(b, b.length);
        StringBuilder steps = new StringBuilder();

        for (int k = 0; k < n - 1; k++) {
            int pivotRow = k;
            for (int i = k + 1; i < n; i++) {
                if (Math.abs(a[i][k]) > Math.abs(a[pivotRow][k])) pivotRow = i;
            }
            if (Math.abs(a[pivotRow][k]) < 1e-12) {
                throw new ArithmeticException("Matriks singular atau hampir singular.");
            }
            if (pivotRow != k) {
                double[] tmpRow = a[k]; a[k] = a[pivotRow]; a[pivotRow] = tmpRow;
                double tmp = rhs[k]; rhs[k] = rhs[pivotRow]; rhs[pivotRow] = tmp;
                steps.append(String.format("Tukar baris %d dengan baris %d%n", k + 1, pivotRow + 1));
            }

            for (int i = k + 1; i < n; i++) {
                double factor = a[i][k] / a[k][k];
                steps.append(String.format("R%d = R%d - (%.10f) * R%d%n", i + 1, i + 1, factor, k + 1));
                for (int j = k; j < n; j++) a[i][j] -= factor * a[k][j];
                rhs[i] -= factor * rhs[k];
            }
            steps.append("Matriks augmented setelah langkah ini:\n");
            steps.append(formatAugmentedMatrix(a, rhs)).append("\n");
        }

        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = rhs[i];
            for (int j = i + 1; j < n; j++) sum -= a[i][j] * x[j];
            if (Math.abs(a[i][i]) < 1e-12) {
                throw new ArithmeticException("Pivot nol ditemukan saat substitusi balik.");
            }
            x[i] = sum / a[i][i];
        }
        return new GaussResult(x, steps.toString());
    }

    /** Membuat deep copy dari matriks 2D agar matriks asli tidak termodifikasi. */
    private static double[][] copyMatrix(double[][] src) {
        double[][] copy = new double[src.length][src[0].length];
        for (int i = 0; i < src.length; i++) copy[i] = Arrays.copyOf(src[i], src[i].length);
        return copy;
    }

    /** Memformat matriks augmented [A|b] menjadi string untuk ditampilkan sebagai log langkah. */
    private static String formatAugmentedMatrix(double[][] a, double[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) sb.append(String.format("%12.6f", a[i][j]));
            sb.append(" | ").append(String.format("%12.6f", b[i])).append("\n");
        }
        return sb.toString();
    }
}
