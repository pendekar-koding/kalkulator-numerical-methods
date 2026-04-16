package panel;

import model.RootResult;
import parser.ExpressionParser;
import solver.SecantSolver;
import ui.UIHelper;

import javax.swing.*;

public class SecantPanel {

    public static JPanel create() {
        JTextField functionField = new JTextField("x^3 - x - 2");
        JTextField x0Field = new JTextField("1");
        JTextField x1Field = new JTextField("2");
        JTextField tolField = new JTextField("0.000001");
        JTextField maxIterField = new JTextField("100");
        JTextArea outputArea = UIHelper.createOutputArea();

        JButton calcButton = new JButton("Hitung");
        calcButton.addActionListener(e -> {
            try {
                ExpressionParser parser = new ExpressionParser(functionField.getText().trim());
                double x0 = Double.parseDouble(x0Field.getText().trim());
                double x1 = Double.parseDouble(x1Field.getText().trim());
                double tol = Double.parseDouble(tolField.getText().trim());
                int maxIter = Integer.parseInt(maxIterField.getText().trim());

                RootResult result = SecantSolver.solve(parser, x0, x1, tol, maxIter);
                outputArea.setText(result.log + String.format("%nAkar hampiran = %.10f%n", result.root));
            } catch (Exception ex) {
                UIHelper.showError(outputArea, ex);
            }
        });

        JPanel form = UIHelper.createFunctionForm(
                new String[]{"f(x):", "x0:", "x1:", "Toleransi:", "Maks iterasi:"},
                new JComponent[]{functionField, x0Field, x1Field, tolField, maxIterField},
                calcButton
        );
        return UIHelper.wrapPanel(form, outputArea);
    }
}
