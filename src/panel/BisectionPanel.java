package panel;

import model.RootResult;
import parser.ExpressionParser;
import solver.BisectionSolver;
import ui.UIHelper;

import javax.swing.*;

public class BisectionPanel {

    public static JPanel create() {
        JTextField functionField = new JTextField("x^3 - x - 2");
        JTextField aField = new JTextField("1");
        JTextField bField = new JTextField("2");
        JTextField tolField = new JTextField("0.000001");
        JTextField maxIterField = new JTextField("100");
        JTextArea outputArea = UIHelper.createOutputArea();

        JButton calcButton = new JButton("Hitung");
        calcButton.addActionListener(e -> {
            try {
                ExpressionParser parser = new ExpressionParser(functionField.getText().trim());
                double a = Double.parseDouble(aField.getText().trim());
                double b = Double.parseDouble(bField.getText().trim());
                double tol = Double.parseDouble(tolField.getText().trim());
                int maxIter = Integer.parseInt(maxIterField.getText().trim());

                RootResult result = BisectionSolver.solve(parser, a, b, tol, maxIter);
                outputArea.setText(result.log + String.format("%nAkar hampiran = %.10f%n", result.root));
            } catch (Exception ex) {
                UIHelper.showError(outputArea, ex);
            }
        });

        JPanel form = UIHelper.createFunctionForm(
                new String[]{"f(x):", "a:", "b:", "Toleransi:", "Maks iterasi:"},
                new JComponent[]{functionField, aField, bField, tolField, maxIterField},
                calcButton
        );
        return UIHelper.wrapPanel(form, outputArea);
    }
}
