package solver;

import model.TrapezoidResult;
import parser.ExpressionParser;
import java.util.Map;

public class TrapezoidSolver {

    public static TrapezoidResult solve(ExpressionParser f, double a, double b, int n) {
        if (n <= 0) throw new IllegalArgumentException("Jumlah segmen n harus > 0.");
        double h = (b - a) / n;
        double sum = 0.5 * (f.evaluate(Map.of("x", a)) + f.evaluate(Map.of("x", b)));
        StringBuilder log = new StringBuilder("=== METODE TRAPESIUM ===\n\n");
        log.append(String.format("h = %.10f%n", h));
        for (int i = 1; i < n; i++) {
            double xi = a + i * h;
            double fxi = f.evaluate(Map.of("x", xi));
            sum += fxi;
            log.append(String.format("i=%d, x=%.10f, f(x)=%.10f%n", i, xi, fxi));
        }
        return new TrapezoidResult(h * sum, log.toString());
    }
}
