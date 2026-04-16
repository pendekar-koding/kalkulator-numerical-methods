package panel;

import model.InterpolationResult;
import parser.InputParser;
import solver.NewtonInterpolationSolver;
import ui.UIHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NewtonInterpolationPanel {

    public static JPanel create() {
        JTextField xField = new JTextField("1,2,4");
        JTextField yField = new JTextField("1,4,16");
        JTextField targetField = new JTextField("3");
        JTextArea outputArea = UIHelper.createOutputArea();

        JButton calcButton = new JButton("Hitung");
        calcButton.addActionListener(e -> {
            try {
                double[] x = InputParser.parseVectorByComma(xField.getText());
                double[] y = InputParser.parseVectorByComma(yField.getText());
                double target = Double.parseDouble(targetField.getText().trim());
                if (x.length != y.length) {
                    throw new IllegalArgumentException("Jumlah data x dan y harus sama.");
                }
                InterpolationResult result = NewtonInterpolationSolver.solve(x, y, target);
                StringBuilder sb = new StringBuilder("=== INTERPOLASI BEDA NEWTON ===\n\nKoefisien beda terbagi:\n");
                for (int i = 0; i < result.coefficients.length; i++) {
                    sb.append(String.format("a%d = %.10f%n", i, result.coefficients[i]));
                }
                sb.append("\nNilai interpolasi pada x = ").append(target).append(" : ")
                        .append(String.format("%.10f%n", result.value));
                sb.append("\nTabel beda terbagi:\n").append(result.table);
                outputArea.setText(sb.toString());
            } catch (Exception ex) {
                UIHelper.showError(outputArea, ex);
            }
        });

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = UIHelper.baseGbc();

        UIHelper.addLabel(form, gbc, 0, 0, "Data x (pisah koma):");
        UIHelper.addComponent(form, gbc, 0, 1, xField, 1.0, 0);
        UIHelper.addLabel(form, gbc, 0, 2, "Data y (pisah koma):");
        UIHelper.addComponent(form, gbc, 0, 3, yField, 1.0, 0);
        UIHelper.addLabel(form, gbc, 0, 4, "x yang dicari:");
        UIHelper.addComponent(form, gbc, 0, 5, targetField, 1.0, 0);
        UIHelper.addComponent(form, gbc, 0, 6, calcButton, 0, 0, 1, 1);

        return UIHelper.wrapPanel(form, outputArea);
    }
}
