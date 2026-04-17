package solver;

import model.RungeKuttaResult;
import parser.ExpressionParser;
import java.util.Map;

public class RungeKuttaSolver {

    /**
     * Menyelesaikan ODE y' = f(x, y) menggunakan metode Runge-Kutta orde 4 (RK4).
     * Dari kondisi awal (x0, y0), menghitung 4 koefisien (k1-k4) setiap langkah
     * untuk memperbarui nilai y dengan akurasi O(h⁴).
     */
    public static RungeKuttaResult solve(ExpressionParser f, double x0, double y0, double h, int steps) {
        if (steps <= 0) throw new IllegalArgumentException("Jumlah langkah harus > 0.");
        StringBuilder log = new StringBuilder("=== METODE RUNGE-KUTTA ORDE 4 ===\n\n");
        double x = x0, y = y0;
        log.append(String.format("%-8s %-15s %-15s %-15s %-15s %-15s %-15s%n",
                "Langkah", "x", "y", "k1", "k2", "k3", "k4"));

        for (int i = 1; i <= steps; i++) {
            double k1 = h * f.evaluate(Map.of("x", x, "y", y));
            double k2 = h * f.evaluate(Map.of("x", x + h / 2.0, "y", y + k1 / 2.0));
            double k3 = h * f.evaluate(Map.of("x", x + h / 2.0, "y", y + k2 / 2.0));
            double k4 = h * f.evaluate(Map.of("x", x + h, "y", y + k3));
            y = y + (k1 + 2 * k2 + 2 * k3 + k4) / 6.0;
            x = x + h;
            log.append(String.format("%-8d %-15.10f %-15.10f %-15.10f %-15.10f %-15.10f %-15.10f%n",
                    i, x, y, k1, k2, k3, k4));
        }
        return new RungeKuttaResult(x, y, log.toString());
    }
}
