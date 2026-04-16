import panel.*;

import javax.swing.*;

public class NumericalMethodsCalculator extends JFrame {

    public NumericalMethodsCalculator() {
        setTitle("Kalkulator Metode Numerik - Java Swing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 760);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Eliminasi Gauss", GaussEliminationPanel.create());
        tabs.addTab("Gauss-Seidel", GaussSeidelPanel.create());
        tabs.addTab("Bagi Dua", BisectionPanel.create());
        tabs.addTab("Secant", SecantPanel.create());
        tabs.addTab("Interpolasi Newton", NewtonInterpolationPanel.create());
        tabs.addTab("Trapesium", TrapezoidPanel.create());
        tabs.addTab("Runge-Kutta", RungeKuttaPanel.create());

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
}
