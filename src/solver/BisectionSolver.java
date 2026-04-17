package solver;

import model.RootResult;
import parser.ExpressionParser;
import java.util.Map;

public class BisectionSolver {

    /**
     * Mencari akar persamaan f(x) = 0 menggunakan metode Bagi Dua (Bisection).
     * Interval [a, b] dibagi dua berulang kali, dipilih sub-interval yang mengandung akar
     * berdasarkan perubahan tanda f(a) dan f(b). Berhenti saat f(c) atau lebar interval < toleransi.
     */
    public static RootResult solve(ExpressionParser f, double a, double b, double tol, int maxIter) {
        double fa = f.evaluate(Map.of("x", a));
        double fb = f.evaluate(Map.of("x", b));
        if (fa * fb > 0) {
            throw new IllegalArgumentException("f(a) dan f(b) harus memiliki tanda berbeda.");
        }

        StringBuilder log = new StringBuilder("=== METODE BAGI DUA ===\n\n");
        double c = a;
        for (int iter = 1; iter <= maxIter; iter++) {
            c = (a + b) / 2.0;
            double fc = f.evaluate(Map.of("x", c));
            log.append(String.format("Iterasi %d: a=%.10f, b=%.10f, c=%.10f, f(c)=%.10f%n", iter, a, b, c, fc));

            if (Math.abs(fc) < tol || Math.abs(b - a) / 2.0 < tol) break;
            if (fa * fc < 0) { b = c; fb = fc; } else { a = c; fa = fc; }
        }
        return new RootResult(c, log.toString());
    }
}
