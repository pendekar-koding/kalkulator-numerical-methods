package panel;

import parser.ExpressionParser;
import solver.RungeKuttaSolver;
import ui.UIHelper;

import javax.swing.*;

public class RungeKuttaPanel {

    public static JPanel create() {
        JTextField functionField = new JTextField("x + y");
        JTextField x0Field = new JTextField("0");
        JTextField y0Field = new JTextField("1");
        JTextField hField = new JTextField("0.1");
        JTextField stepsField = new JTextField("10");
        JTextArea outputArea = UIHelper.createOutputArea();

        JButton calcButton = new JButton("Hitung");
        calcButton.addActionListener(e -> {
            try {
                ExpressionParser parser = new ExpressionParser(functionField.getText().trim());
                double x0 = Double.parseDouble(x0Field.getText().trim());
                double y0 = Double.parseDouble(y0Field.getText().trim());
                double h = Double.parseDouble(hField.getText().trim());
                int steps = Integer.parseInt(stepsField.getText().trim());
                var result = RungeKuttaSolver.solve(parser, x0, y0, h, steps);
                outputArea.setText(result.log + String.format("%nHasil akhir: y(%.10f) = %.10f%n", result.lastX, result.lastY));
            } catch (Exception ex) {
                UIHelper.showError(outputArea, ex);
            }
        });

        JPanel form = UIHelper.createFunctionForm(
                new String[]{"f(x,y) = y':", "x0:", "y0:", "h:", "Jumlah langkah:"},
                new JComponent[]{functionField, x0Field, y0Field, hField, stepsField},
                calcButton
        );
        return UIHelper.wrapPanel(form, outputArea);
    }
}
