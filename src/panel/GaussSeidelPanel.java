package panel;

import model.SeidelResult;
import parser.InputParser;
import solver.GaussSeidelSolver;
import ui.UIHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GaussSeidelPanel {

    public static JPanel create() {
        JTextArea matrixArea = new JTextArea("4 1 2\n3 5 1\n1 1 3", 6, 25);
        JTextArea vectorArea = new JTextArea("4\n7\n3", 6, 10);
        JTextField initialField = new JTextField("0,0,0");
        JTextField tolField = new JTextField("0.000001");
        JTextField maxIterField = new JTextField("100");
        JTextArea outputArea = UIHelper.createOutputArea();

        JButton calcButton = new JButton("Hitung");
        calcButton.addActionListener(e -> {
            try {
                double[][] A = InputParser.parseMatrix(matrixArea.getText());
                double[] b = InputParser.parseVectorByLines(vectorArea.getText());
                double[] x0 = InputParser.parseVectorByComma(initialField.getText());
                double tol = Double.parseDouble(tolField.getText().trim());
                int maxIter = Integer.parseInt(maxIterField.getText().trim());

                if (A.length != b.length || A.length != x0.length) {
                    throw new IllegalArgumentException("Ukuran matriks A, vektor b, dan tebakan awal harus konsisten.");
                }

                SeidelResult result = GaussSeidelSolver.solve(A, b, x0, tol, maxIter);
                StringBuilder sb = new StringBuilder("=== GAUSS-SEIDEL ===\n\nIterasi:\n");
                sb.append(result.iterationLog).append("\nHasil akhir:\n");
                for (int i = 0; i < result.solution.length; i++) {
                    sb.append(String.format("x%d = %.10f%n", i + 1, result.solution[i]));
                }
                sb.append(String.format("\nKonvergen: %s%n", result.converged ? "Ya" : "Tidak"));
                sb.append(String.format("Error akhir: %.10e%n", result.error));
                outputArea.setText(sb.toString());
            } catch (Exception ex) {
                UIHelper.showError(outputArea, ex);
            }
        });

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = UIHelper.baseGbc();

        UIHelper.addLabel(form, gbc, 0, 0, "Matriks A:");
        UIHelper.addComponent(form, gbc, 0, 1, new JScrollPane(matrixArea), 1.0, 1.0);
        UIHelper.addLabel(form, gbc, 1, 0, "Vektor b:");
        UIHelper.addComponent(form, gbc, 1, 1, new JScrollPane(vectorArea), 0.4, 1.0);
        UIHelper.addLabel(form, gbc, 0, 2, "Tebakan awal x0 (pisah koma):");
        UIHelper.addComponent(form, gbc, 0, 3, initialField, 1.0, 0);
        UIHelper.addLabel(form, gbc, 0, 4, "Toleransi:");
        UIHelper.addComponent(form, gbc, 0, 5, tolField, 1.0, 0);
        UIHelper.addLabel(form, gbc, 1, 4, "Maks iterasi:");
        UIHelper.addComponent(form, gbc, 1, 5, maxIterField, 1.0, 0);
        UIHelper.addComponent(form, gbc, 0, 6, calcButton, 0, 0, 2, 1);

        return UIHelper.wrapPanel(form, outputArea);
    }
}
