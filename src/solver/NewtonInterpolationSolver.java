package solver;

import model.InterpolationResult;

public class NewtonInterpolationSolver {

    /**
     * Membangun polinom interpolasi Newton dari titik-titik data (x, y) menggunakan tabel beda terbagi,
     * lalu mengevaluasi polinom tersebut pada nilai target yang diberikan.
     */
    public static InterpolationResult solve(double[] x, double[] y, double target) {
        int n = x.length;
        double[][] table = new double[n][n];
        for (int i = 0; i < n; i++) table[i][0] = y[i];

        for (int j = 1; j < n; j++) {
            for (int i = 0; i < n - j; i++) {
                double denominator = x[i + j] - x[i];
                if (Math.abs(denominator) < 1e-12) {
                    throw new IllegalArgumentException("Nilai x tidak boleh duplikat.");
                }
                table[i][j] = (table[i + 1][j - 1] - table[i][j - 1]) / denominator;
            }
        }

        double[] coeffs = new double[n];
        for (int i = 0; i < n; i++) coeffs[i] = table[0][i];

        double value = coeffs[0];
        double product = 1.0;
        for (int i = 1; i < n; i++) {
            product *= (target - x[i - 1]);
            value += coeffs[i] * product;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-12s", "x"));
        for (int j = 0; j < n; j++) sb.append(String.format("f[%d]%-12s", j, ""));
        sb.append("\n");

        for (int i = 0; i < n; i++) {
            sb.append(String.format("%-12.6f", x[i]));
            for (int j = 0; j < n - i; j++) sb.append(String.format("%-16.10f", table[i][j]));
            sb.append("\n");
        }

        return new InterpolationResult(coeffs, value, sb.toString());
    }
}
