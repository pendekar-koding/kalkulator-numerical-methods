package solver;

import model.RootResult;
import parser.ExpressionParser;
import java.util.Map;

public class SecantSolver {

    /**
     * Mencari akar persamaan f(x) = 0 menggunakan metode Secant.
     * Menggunakan dua tebakan awal (x0, x1) dan menarik garis secant untuk memperkirakan akar baru.
     * Tidak memerlukan turunan fungsi. Berhenti saat selisih iterasi atau f(x) < toleransi.
     */
    public static RootResult solve(ExpressionParser f, double x0, double x1, double tol, int maxIter) {
        StringBuilder log = new StringBuilder("=== METODE SECANT ===\n\n");
        double x2 = x1;
        for (int iter = 1; iter <= maxIter; iter++) {
            double fx0 = f.evaluate(Map.of("x", x0));
            double fx1 = f.evaluate(Map.of("x", x1));
            if (Math.abs(fx1 - fx0) < 1e-12) {
                throw new ArithmeticException("Pembagian dengan nol pada rumus secant.");
            }
            x2 = x1 - fx1 * (x1 - x0) / (fx1 - fx0);
            double fx2 = f.evaluate(Map.of("x", x2));
            log.append(String.format("Iterasi %d: x0=%.10f, x1=%.10f, x2=%.10f, f(x2)=%.10f%n", iter, x0, x1, x2, fx2));
            if (Math.abs(x2 - x1) < tol || Math.abs(fx2) < tol) break;
            x0 = x1;
            x1 = x2;
        }
        return new RootResult(x2, log.toString());
    }
}
