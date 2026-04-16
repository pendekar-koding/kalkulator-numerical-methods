import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class NumericalMethodsCalculator extends JFrame {

    public NumericalMethodsCalculator() {
        setTitle("Kalkulator Metode Numerik - Java Swing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 760);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Eliminasi Gauss", createGaussEliminationPanel());
        tabs.addTab("Gauss-Seidel", createGaussSeidelPanel());
        tabs.addTab("Bagi Dua", createBisectionPanel());
        tabs.addTab("Secant", createSecantPanel());
        tabs.addTab("Interpolasi Newton", createNewtonInterpolationPanel());
        tabs.addTab("Trapesium", createTrapezoidPanel());
        tabs.addTab("Runge-Kutta", createRungeKuttaPanel());

        add(tabs);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new NumericalMethodsCalculator().setVisible(true);
        });
    }

    private JPanel createGaussEliminationPanel() {
        JTextArea matrixArea = new JTextArea("2 1 -1\n-3 -1 2\n-2 1 2", 6, 25);
        JTextArea vectorArea = new JTextArea("8\n-11\n-3", 6, 10);
        JTextArea outputArea = createOutputArea();

        JButton calcButton = new JButton("Hitung");
        calcButton.addActionListener(e -> {
            try {
                double[][] A = parseMatrix(matrixArea.getText());
                double[] b = parseVectorByLines(vectorArea.getText());
                if (A.length != b.length) {
                    throw new IllegalArgumentException("Jumlah baris matriks A harus sama dengan jumlah elemen vektor b.");
                }
                GaussResult result = gaussianElimination(A, b);
                StringBuilder sb = new StringBuilder();
                sb.append("=== ELIMINASI GAUSS ===\n\n");
                sb.append("Hasil solusi:\n");
                for (int i = 0; i < result.solution.length; i++) {
                    sb.append(String.format("x%d = %.10f%n", i + 1, result.solution[i]));
                }
                sb.append("\nLangkah eliminasi:\n");
                sb.append(result.steps);
                outputArea.setText(sb.toString());
            } catch (Exception ex) {
                showError(outputArea, ex);
            }
        });

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = baseGbc();

        addLabel(form, gbc, 0, 0, "Matriks A (pisahkan spasi, satu baris per persamaan):");
        addComponent(form, gbc, 0, 1, new JScrollPane(matrixArea), 1.0, 1.0);
        addLabel(form, gbc, 1, 0, "Vektor b (satu nilai per baris):");
        addComponent(form, gbc, 1, 1, new JScrollPane(vectorArea), 0.4, 1.0);
        addComponent(form, gbc, 0, 2, calcButton, 0, 0, 2, 1);

        return wrapPanel(form, outputArea);
    }

    private JPanel createGaussSeidelPanel() {
        JTextArea matrixArea = new JTextArea("4 1 2\n3 5 1\n1 1 3", 6, 25);
        JTextArea vectorArea = new JTextArea("4\n7\n3", 6, 10);
        JTextField initialField = new JTextField("0,0,0");
        JTextField tolField = new JTextField("0.000001");
        JTextField maxIterField = new JTextField("100");
        JTextArea outputArea = createOutputArea();

        JButton calcButton = new JButton("Hitung");
        calcButton.addActionListener(e -> {
            try {
                double[][] A = parseMatrix(matrixArea.getText());
                double[] b = parseVectorByLines(vectorArea.getText());
                double[] x0 = parseVectorByComma(initialField.getText());
                double tol = Double.parseDouble(tolField.getText().trim());
                int maxIter = Integer.parseInt(maxIterField.getText().trim());

                if (A.length != b.length || A.length != x0.length) {
                    throw new IllegalArgumentException("Ukuran matriks A, vektor b, dan tebakan awal harus konsisten.");
                }

                SeidelResult result = gaussSeidel(A, b, x0, tol, maxIter);
                StringBuilder sb = new StringBuilder();
                sb.append("=== GAUSS-SEIDEL ===\n\n");
                sb.append("Iterasi:\n");
                sb.append(result.iterationLog);
                sb.append("\nHasil akhir:\n");
                for (int i = 0; i < result.solution.length; i++) {
                    sb.append(String.format("x%d = %.10f%n", i + 1, result.solution[i]));
                }
                sb.append(String.format("\nKonvergen: %s%n", result.converged ? "Ya" : "Tidak"));
                sb.append(String.format("Error akhir: %.10e%n", result.error));
                outputArea.setText(sb.toString());
            } catch (Exception ex) {
                showError(outputArea, ex);
            }
        });

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = baseGbc();

        addLabel(form, gbc, 0, 0, "Matriks A:");
        addComponent(form, gbc, 0, 1, new JScrollPane(matrixArea), 1.0, 1.0);
        addLabel(form, gbc, 1, 0, "Vektor b:");
        addComponent(form, gbc, 1, 1, new JScrollPane(vectorArea), 0.4, 1.0);
        addLabel(form, gbc, 0, 2, "Tebakan awal x0 (pisah koma):");
        addComponent(form, gbc, 0, 3, initialField, 1.0, 0);
        addLabel(form, gbc, 0, 4, "Toleransi:");
        addComponent(form, gbc, 0, 5, tolField, 1.0, 0);
        addLabel(form, gbc, 1, 4, "Maks iterasi:");
        addComponent(form, gbc, 1, 5, maxIterField, 1.0, 0);
        addComponent(form, gbc, 0, 6, calcButton, 0, 0, 2, 1);

        return wrapPanel(form, outputArea);
    }

    private JPanel createBisectionPanel() {
        JTextField functionField = new JTextField("x^3 - x - 2");
        JTextField aField = new JTextField("1");
        JTextField bField = new JTextField("2");
        JTextField tolField = new JTextField("0.000001");
        JTextField maxIterField = new JTextField("100");
        JTextArea outputArea = createOutputArea();

        JButton calcButton = new JButton("Hitung");
        calcButton.addActionListener(e -> {
            try {
                ExpressionParser parser = new ExpressionParser(functionField.getText().trim());
                double a = Double.parseDouble(aField.getText().trim());
                double b = Double.parseDouble(bField.getText().trim());
                double tol = Double.parseDouble(tolField.getText().trim());
                int maxIter = Integer.parseInt(maxIterField.getText().trim());

                RootResult result = bisection(parser, a, b, tol, maxIter);
                outputArea.setText(result.log + String.format("%nAkar hampiran = %.10f%n", result.root));
            } catch (Exception ex) {
                showError(outputArea, ex);
            }
        });

        JPanel form = createFunctionForm(
                new String[]{"f(x):", "a:", "b:", "Toleransi:", "Maks iterasi:"},
                new JComponent[]{functionField, aField, bField, tolField, maxIterField},
                calcButton
        );
        return wrapPanel(form, outputArea);
    }

    private JPanel createSecantPanel() {
        JTextField functionField = new JTextField("x^3 - x - 2");
        JTextField x0Field = new JTextField("1");
        JTextField x1Field = new JTextField("2");
        JTextField tolField = new JTextField("0.000001");
        JTextField maxIterField = new JTextField("100");
        JTextArea outputArea = createOutputArea();

        JButton calcButton = new JButton("Hitung");
        calcButton.addActionListener(e -> {
            try {
                ExpressionParser parser = new ExpressionParser(functionField.getText().trim());
                double x0 = Double.parseDouble(x0Field.getText().trim());
                double x1 = Double.parseDouble(x1Field.getText().trim());
                double tol = Double.parseDouble(tolField.getText().trim());
                int maxIter = Integer.parseInt(maxIterField.getText().trim());

                RootResult result = secant(parser, x0, x1, tol, maxIter);
                outputArea.setText(result.log + String.format("%nAkar hampiran = %.10f%n", result.root));
            } catch (Exception ex) {
                showError(outputArea, ex);
            }
        });

        JPanel form = createFunctionForm(
                new String[]{"f(x):", "x0:", "x1:", "Toleransi:", "Maks iterasi:"},
                new JComponent[]{functionField, x0Field, x1Field, tolField, maxIterField},
                calcButton
        );
        return wrapPanel(form, outputArea);
    }

    private JPanel createNewtonInterpolationPanel() {
        JTextField xField = new JTextField("1,2,4");
        JTextField yField = new JTextField("1,4,16");
        JTextField targetField = new JTextField("3");
        JTextArea outputArea = createOutputArea();

        JButton calcButton = new JButton("Hitung");
        calcButton.addActionListener(e -> {
            try {
                double[] x = parseVectorByComma(xField.getText());
                double[] y = parseVectorByComma(yField.getText());
                double target = Double.parseDouble(targetField.getText().trim());
                if (x.length != y.length) {
                    throw new IllegalArgumentException("Jumlah data x dan y harus sama.");
                }
                InterpolationResult result = newtonDividedDifferences(x, y, target);
                StringBuilder sb = new StringBuilder();
                sb.append("=== INTERPOLASI BEDA NEWTON ===\n\n");
                sb.append("Koefisien beda terbagi:\n");
                for (int i = 0; i < result.coefficients.length; i++) {
                    sb.append(String.format("a%d = %.10f%n", i, result.coefficients[i]));
                }
                sb.append("\nNilai interpolasi pada x = ").append(target).append(" : ")
                        .append(String.format("%.10f%n", result.value));
                sb.append("\nTabel beda terbagi:\n");
                sb.append(result.table);
                outputArea.setText(sb.toString());
            } catch (Exception ex) {
                showError(outputArea, ex);
            }
        });

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = baseGbc();

        addLabel(form, gbc, 0, 0, "Data x (pisah koma):");
        addComponent(form, gbc, 0, 1, xField, 1.0, 0);
        addLabel(form, gbc, 0, 2, "Data y (pisah koma):");
        addComponent(form, gbc, 0, 3, yField, 1.0, 0);
        addLabel(form, gbc, 0, 4, "x yang dicari:");
        addComponent(form, gbc, 0, 5, targetField, 1.0, 0);
        addComponent(form, gbc, 0, 6, calcButton, 0, 0, 1, 1);

        return wrapPanel(form, outputArea);
    }

    private JPanel createTrapezoidPanel() {
        JTextField functionField = new JTextField("x^2");
        JTextField aField = new JTextField("0");
        JTextField bField = new JTextField("2");
        JTextField nField = new JTextField("8");
        JTextArea outputArea = createOutputArea();

        JButton calcButton = new JButton("Hitung");
        calcButton.addActionListener(e -> {
            try {
                ExpressionParser parser = new ExpressionParser(functionField.getText().trim());
                double a = Double.parseDouble(aField.getText().trim());
                double b = Double.parseDouble(bField.getText().trim());
                int n = Integer.parseInt(nField.getText().trim());
                TrapezoidResult result = trapezoidalIntegration(parser, a, b, n);
                outputArea.setText(result.log + String.format("%nHasil integral ≈ %.10f%n", result.value));
            } catch (Exception ex) {
                showError(outputArea, ex);
            }
        });

        JPanel form = createFunctionForm(
                new String[]{"f(x):", "a:", "b:", "Jumlah segmen n:"},
                new JComponent[]{functionField, aField, bField, nField},
                calcButton
        );
        return wrapPanel(form, outputArea);
    }

    private JPanel createRungeKuttaPanel() {
        JTextField functionField = new JTextField("x + y");
        JTextField x0Field = new JTextField("0");
        JTextField y0Field = new JTextField("1");
        JTextField hField = new JTextField("0.1");
        JTextField stepsField = new JTextField("10");
        JTextArea outputArea = createOutputArea();

        JButton calcButton = new JButton("Hitung");
        calcButton.addActionListener(e -> {
            try {
                ExpressionParser parser = new ExpressionParser(functionField.getText().trim());
                double x0 = Double.parseDouble(x0Field.getText().trim());
                double y0 = Double.parseDouble(y0Field.getText().trim());
                double h = Double.parseDouble(hField.getText().trim());
                int steps = Integer.parseInt(stepsField.getText().trim());
                RungeKuttaResult result = rungeKutta4(parser, x0, y0, h, steps);
                outputArea.setText(result.log + String.format("%nHasil akhir: y(%.10f) = %.10f%n", result.lastX, result.lastY));
            } catch (Exception ex) {
                showError(outputArea, ex);
            }
        });

        JPanel form = createFunctionForm(
                new String[]{"f(x,y) = y':", "x0:", "y0:", "h:", "Jumlah langkah:"},
                new JComponent[]{functionField, x0Field, y0Field, hField, stepsField},
                calcButton
        );
        return wrapPanel(form, outputArea);
    }

    private JPanel createFunctionForm(String[] labels, JComponent[] fields, JButton button) {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = baseGbc();
        for (int i = 0; i < labels.length; i++) {
            addLabel(form, gbc, 0, i * 2, labels[i]);
            addComponent(form, gbc, 0, i * 2 + 1, fields[i], 1.0, 0);
        }
        addComponent(form, gbc, 0, labels.length * 2, button, 0, 0, 1, 1);
        return form;
    }

    private JPanel wrapPanel(JPanel formPanel, JTextArea outputArea) {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(formPanel), new JScrollPane(outputArea));
        splitPane.setResizeWeight(0.45);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }

    private JTextArea createOutputArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setMargin(new Insets(10, 10, 10, 10));
        return area;
    }

    private GridBagConstraints baseGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        return gbc;
    }

    private void addLabel(JPanel panel, GridBagConstraints gbc, int x, int y, String text) {
        GridBagConstraints c = (GridBagConstraints) gbc.clone();
        c.gridx = x;
        c.gridy = y;
        c.weightx = 0;
        panel.add(new JLabel(text), c);
    }

    private void addComponent(JPanel panel, GridBagConstraints gbc, int x, int y, JComponent comp, double wx, double wy) {
        addComponent(panel, gbc, x, y, comp, wx, wy, 1, 1);
    }

    private void addComponent(JPanel panel, GridBagConstraints gbc, int x, int y, JComponent comp, double wx, double wy, int gw, int gh) {
        GridBagConstraints c = (GridBagConstraints) gbc.clone();
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = gw;
        c.gridheight = gh;
        c.weightx = wx;
        c.weighty = wy;
        c.fill = wy > 0 ? GridBagConstraints.BOTH : GridBagConstraints.HORIZONTAL;
        panel.add(comp, c);
    }

    private void showError(JTextArea outputArea, Exception ex) {
        outputArea.setText("Error: " + ex.getMessage());
    }

    private static double[][] parseMatrix(String text) {
        String[] lines = text.trim().split("\\R+");
        List<double[]> rows = new ArrayList<>();
        int cols = -1;
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.trim().split("[,	 ]+");
            if (cols == -1) cols = parts.length;
            if (parts.length != cols) {
                throw new IllegalArgumentException("Jumlah kolom matriks tidak konsisten.");
            }
            double[] row = new double[cols];
            for (int i = 0; i < cols; i++) {
                row[i] = Double.parseDouble(parts[i]);
            }
            rows.add(row);
        }
        if (rows.isEmpty()) throw new IllegalArgumentException("Matriks tidak boleh kosong.");
        if (rows.size() != cols) throw new IllegalArgumentException("Matriks A harus persegi (n x n).");
        return rows.toArray(new double[0][]);
    }

    private static double[] parseVectorByLines(String text) {
        String[] lines = text.trim().split("\\R+");
        List<Double> values = new ArrayList<>();
        for (String line : lines) {
            if (!line.trim().isEmpty()) values.add(Double.parseDouble(line.trim()));
        }
        if (values.isEmpty()) throw new IllegalArgumentException("Vektor tidak boleh kosong.");
        double[] result = new double[values.size()];
        for (int i = 0; i < values.size(); i++) result[i] = values.get(i);
        return result;
    }

    private static double[] parseVectorByComma(String text) {
        String[] parts = text.trim().split("[,	 ]+");
        if (parts.length == 0) throw new IllegalArgumentException("Input vektor tidak valid.");
        double[] result = new double[parts.length];
        for (int i = 0; i < parts.length; i++) result[i] = Double.parseDouble(parts[i].trim());
        return result;
    }

    private static GaussResult gaussianElimination(double[][] A, double[] b) {
        int n = A.length;
        double[][] a = copyMatrix(A);
        double[] rhs = Arrays.copyOf(b, b.length);
        StringBuilder steps = new StringBuilder();

        for (int k = 0; k < n - 1; k++) {
            int pivotRow = k;
            for (int i = k + 1; i < n; i++) {
                if (Math.abs(a[i][k]) > Math.abs(a[pivotRow][k])) {
                    pivotRow = i;
                }
            }
            if (Math.abs(a[pivotRow][k]) < 1e-12) {
                throw new ArithmeticException("Matriks singular atau hampir singular.");
            }
            if (pivotRow != k) {
                double[] tmpRow = a[k];
                a[k] = a[pivotRow];
                a[pivotRow] = tmpRow;
                double tmp = rhs[k];
                rhs[k] = rhs[pivotRow];
                rhs[pivotRow] = tmp;
                steps.append(String.format("Tukar baris %d dengan baris %d%n", k + 1, pivotRow + 1));
            }

            for (int i = k + 1; i < n; i++) {
                double factor = a[i][k] / a[k][k];
                steps.append(String.format("R%d = R%d - (%.10f) * R%d%n", i + 1, i + 1, factor, k + 1));
                for (int j = k; j < n; j++) {
                    a[i][j] -= factor * a[k][j];
                }
                rhs[i] -= factor * rhs[k];
            }
            steps.append("Matriks augmented setelah langkah ini:\n");
            steps.append(formatAugmentedMatrix(a, rhs)).append("\n");
        }

        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = rhs[i];
            for (int j = i + 1; j < n; j++) {
                sum -= a[i][j] * x[j];
            }
            if (Math.abs(a[i][i]) < 1e-12) {
                throw new ArithmeticException("Pivot nol ditemukan saat substitusi balik.");
            }
            x[i] = sum / a[i][i];
        }
        return new GaussResult(x, steps.toString());
    }

    private static SeidelResult gaussSeidel(double[][] A, double[] b, double[] x0, double tol, int maxIter) {
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
            for (int i = 0; i < n; i++) {
                error = Math.max(error, Math.abs(x[i] - old[i]));
            }

            log.append(String.format("Iterasi %d: ", iter));
            for (int i = 0; i < n; i++) {
                log.append(String.format("x%d=%.10f ", i + 1, x[i]));
            }
            log.append(String.format(" | error=%.10e%n", error));

            if (error < tol) {
                converged = true;
                break;
            }
        }

        return new SeidelResult(x, converged, error, log.toString());
    }

    private static RootResult bisection(ExpressionParser f, double a, double b, double tol, int maxIter) {
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

            if (Math.abs(fc) < tol || Math.abs(b - a) / 2.0 < tol) {
                break;
            }
            if (fa * fc < 0) {
                b = c;
                fb = fc;
            } else {
                a = c;
                fa = fc;
            }
        }
        return new RootResult(c, log.toString());
    }

    private static RootResult secant(ExpressionParser f, double x0, double x1, double tol, int maxIter) {
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
            if (Math.abs(x2 - x1) < tol || Math.abs(fx2) < tol) {
                break;
            }
            x0 = x1;
            x1 = x2;
        }
        return new RootResult(x2, log.toString());
    }

    private static InterpolationResult newtonDividedDifferences(double[] x, double[] y, double target) {
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
        for (int j = 0; j < n; j++) {
            sb.append(String.format("f[%d]%-12s", j, ""));
        }
        sb.append("\n");

        for (int i = 0; i < n; i++) {
            sb.append(String.format("%-12.6f", x[i]));
            for (int j = 0; j < n - i; j++) {
                sb.append(String.format("%-16.10f", table[i][j]));
            }
            sb.append("\n");
        }

        return new InterpolationResult(coeffs, value, sb.toString());
    }

    private static TrapezoidResult trapezoidalIntegration(ExpressionParser f, double a, double b, int n) {
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
        double value = h * sum;
        return new TrapezoidResult(value, log.toString());
    }

    private static RungeKuttaResult rungeKutta4(ExpressionParser f, double x0, double y0, double h, int steps) {
        if (steps <= 0) throw new IllegalArgumentException("Jumlah langkah harus > 0.");
        StringBuilder log = new StringBuilder("=== METODE RUNGE-KUTTA ORDE 4 ===\n\n");
        double x = x0;
        double y = y0;
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

    private static double[][] copyMatrix(double[][] src) {
        double[][] copy = new double[src.length][src[0].length];
        for (int i = 0; i < src.length; i++) {
            copy[i] = Arrays.copyOf(src[i], src[i].length);
        }
        return copy;
    }

    private static String formatAugmentedMatrix(double[][] a, double[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                sb.append(String.format("%12.6f", a[i][j]));
            }
            sb.append(" | ").append(String.format("%12.6f", b[i])).append("\n");
        }
        return sb.toString();
    }

    static class GaussResult {
        double[] solution;
        String steps;

        GaussResult(double[] solution, String steps) {
            this.solution = solution;
            this.steps = steps;
        }
    }

    static class SeidelResult {
        double[] solution;
        boolean converged;
        double error;
        String iterationLog;

        SeidelResult(double[] solution, boolean converged, double error, String iterationLog) {
            this.solution = solution;
            this.converged = converged;
            this.error = error;
            this.iterationLog = iterationLog;
        }
    }

    static class RootResult {
        double root;
        String log;

        RootResult(double root, String log) {
            this.root = root;
            this.log = log;
        }
    }

    static class InterpolationResult {
        double[] coefficients;
        double value;
        String table;

        InterpolationResult(double[] coefficients, double value, String table) {
            this.coefficients = coefficients;
            this.value = value;
            this.table = table;
        }
    }

    static class TrapezoidResult {
        double value;
        String log;

        TrapezoidResult(double value, String log) {
            this.value = value;
            this.log = log;
        }
    }

    static class RungeKuttaResult {
        double lastX;
        double lastY;
        String log;

        RungeKuttaResult(double lastX, double lastY, String log) {
            this.lastX = lastX;
            this.lastY = lastY;
            this.log = log;
        }
    }

    static class ExpressionParser {
        private final String input;
        private int pos = -1;
        private int ch;

        ExpressionParser(String input) {
            this.input = input.replaceAll("\\s+", "");
        }

        double evaluate(Map<String, Double> variables) {
            pos = -1;
            nextChar();
            double x = parseExpression(variables);
            if (pos < input.length()) {
                throw new IllegalArgumentException("Karakter tidak dikenali: '" + (char) ch + "'");
            }
            return x;
        }

        private void nextChar() {
            ch = (++pos < input.length()) ? input.charAt(pos) : -1;
        }

        private boolean eat(int charToEat) {
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }

        private double parseExpression(Map<String, Double> vars) {
            double x = parseTerm(vars);
            while (true) {
                if (eat('+')) x += parseTerm(vars);
                else if (eat('-')) x -= parseTerm(vars);
                else return x;
            }
        }

        private double parseTerm(Map<String, Double> vars) {
            double x = parsePower(vars);
            while (true) {
                if (eat('*')) x *= parsePower(vars);
                else if (eat('/')) x /= parsePower(vars);
                else return x;
            }
        }

        private double parsePower(Map<String, Double> vars) {
            double x = parseUnary(vars);
            if (eat('^')) {
                x = Math.pow(x, parsePower(vars));
            }
            return x;
        }

        private double parseUnary(Map<String, Double> vars) {
            if (eat('+')) return parseUnary(vars);
            if (eat('-')) return -parseUnary(vars);
            return parsePrimary(vars);
        }

        private double parsePrimary(Map<String, Double> vars) {
            if (eat('(')) {
                double x = parseExpression(vars);
                if (!eat(')')) throw new IllegalArgumentException("Kurung tutup tidak ditemukan.");
                return x;
            }

            if ((ch >= '0' && ch <= '9') || ch == '.') {
                int start = this.pos;
                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                return Double.parseDouble(input.substring(start, this.pos));
            }

            if (Character.isLetter(ch)) {
                int start = this.pos;
                while (Character.isLetterOrDigit(ch) || ch == '_') nextChar();
                String name = input.substring(start, this.pos).toLowerCase();

                if (eat('(')) {
                    double arg = parseExpression(vars);
                    if (!eat(')')) throw new IllegalArgumentException("Kurung tutup fungsi tidak ditemukan.");
                    return applyFunction(name, arg);
                }

                if (name.equals("pi")) return Math.PI;
                if (name.equals("e")) return Math.E;
                if (vars.containsKey(name)) return vars.get(name);
                throw new IllegalArgumentException("Variabel atau fungsi tidak dikenal: " + name);
            }

            throw new IllegalArgumentException("Ekspresi tidak valid.");
        }

        private double applyFunction(String name, double arg) {
            return switch (name) {
                case "sin" -> Math.sin(arg);
                case "cos" -> Math.cos(arg);
                case "tan" -> Math.tan(arg);
                case "sqrt" -> Math.sqrt(arg);
                case "abs" -> Math.abs(arg);
                case "exp" -> Math.exp(arg);
                case "ln" -> Math.log(arg);
                case "log" -> Math.log10(arg);
                default -> throw new IllegalArgumentException("Fungsi tidak dikenal: " + name);
            };
        }
    }
}