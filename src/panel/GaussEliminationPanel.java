package panel;

import model.GaussResult;
import parser.InputParser;
import solver.GaussEliminationSolver;
import ui.UIHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GaussEliminationPanel {

    public static JPanel create() {
        JTextArea matrixArea = new JTextArea("2 1 -1\n-3 -1 2\n-2 1 2", 6, 25);
        JTextArea vectorArea = new JTextArea("8\n-11\n-3", 6, 10);
        JTextArea outputArea = UIHelper.createOutputArea();

        JButton calcButton = new JButton("Hitung");
        calcButton.addActionListener(e -> {
            try {
                double[][] A = InputParser.parseMatrix(matrixArea.getText());
                double[] b = InputParser.parseVectorByLines(vectorArea.getText());
                if (A.length != b.length) {
                    throw new IllegalArgumentException("Jumlah baris matriks A harus sama dengan jumlah elemen vektor b.");
                }
                GaussResult result = GaussEliminationSolver.solve(A, b);
                StringBuilder sb = new StringBuilder("=== ELIMINASI GAUSS ===\n\nHasil solusi:\n");
                for (int i = 0; i < result.solution.length; i++) {
                    sb.append(String.format("x%d = %.10f%n", i + 1, result.solution[i]));
                }
                sb.append("\nLangkah eliminasi:\n").append(result.steps);
                outputArea.setText(sb.toString());
            } catch (Exception ex) {
                UIHelper.showError(outputArea, ex);
            }
        });

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = UIHelper.baseGbc();

        UIHelper.addLabel(form, gbc, 0, 0, "Matriks A (pisahkan spasi, satu baris per persamaan):");
        UIHelper.addComponent(form, gbc, 0, 1, new JScrollPane(matrixArea), 1.0, 1.0);
        UIHelper.addLabel(form, gbc, 1, 0, "Vektor b (satu nilai per baris):");
        UIHelper.addComponent(form, gbc, 1, 1, new JScrollPane(vectorArea), 0.4, 1.0);
        UIHelper.addComponent(form, gbc, 0, 2, calcButton, 0, 0, 2, 1);

        return UIHelper.wrapPanel(form, outputArea);
    }
}
