package panel;

import parser.ExpressionParser;
import solver.TrapezoidSolver;
import ui.UIHelper;

import javax.swing.*;

public class TrapezoidPanel {

    public static JPanel create() {
        JTextField functionField = new JTextField("x^2");
        JTextField aField = new JTextField("0");
        JTextField bField = new JTextField("2");
        JTextField nField = new JTextField("8");
        JTextArea outputArea = UIHelper.createOutputArea();

        JButton calcButton = new JButton("Hitung");
        calcButton.addActionListener(e -> {
            try {
                ExpressionParser parser = new ExpressionParser(functionField.getText().trim());
                double a = Double.parseDouble(aField.getText().trim());
                double b = Double.parseDouble(bField.getText().trim());
                int n = Integer.parseInt(nField.getText().trim());
                var result = TrapezoidSolver.solve(parser, a, b, n);
                outputArea.setText(result.log + String.format("%nHasil integral ≈ %.10f%n", result.value));
            } catch (Exception ex) {
                UIHelper.showError(outputArea, ex);
            }
        });

        JPanel form = UIHelper.createFunctionForm(
                new String[]{"f(x):", "a:", "b:", "Jumlah segmen n:"},
                new JComponent[]{functionField, aField, bField, nField},
                calcButton
        );
        return UIHelper.wrapPanel(form, outputArea);
    }
}
