package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UIHelper {

    /** Membuat JTextArea read-only dengan font monospaced untuk menampilkan hasil output/log. */
    public static JTextArea createOutputArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setMargin(new Insets(10, 10, 10, 10));
        return area;
    }

    /** Membuat GridBagConstraints dasar dengan inset, fill horizontal, dan anchor kiri-atas sebagai template layout. */
    public static GridBagConstraints baseGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        return gbc;
    }

    /** Menambahkan JLabel pada posisi grid (x, y) di panel dengan layout GridBag. */
    public static void addLabel(JPanel panel, GridBagConstraints gbc, int x, int y, String text) {
        GridBagConstraints c = (GridBagConstraints) gbc.clone();
        c.gridx = x; c.gridy = y; c.weightx = 0;
        panel.add(new JLabel(text), c);
    }

    /** Menambahkan komponen pada posisi grid (x, y) dengan bobot (wx, wy), gridwidth dan gridheight default 1. */
    public static void addComponent(JPanel panel, GridBagConstraints gbc, int x, int y, JComponent comp, double wx, double wy) {
        addComponent(panel, gbc, x, y, comp, wx, wy, 1, 1);
    }

    /** Menambahkan komponen pada posisi grid (x, y) dengan bobot (wx, wy) dan ukuran grid kustom (gw x gh). */
    public static void addComponent(JPanel panel, GridBagConstraints gbc, int x, int y, JComponent comp, double wx, double wy, int gw, int gh) {
        GridBagConstraints c = (GridBagConstraints) gbc.clone();
        c.gridx = x; c.gridy = y; c.gridwidth = gw; c.gridheight = gh;
        c.weightx = wx; c.weighty = wy;
        c.fill = wy > 0 ? GridBagConstraints.BOTH : GridBagConstraints.HORIZONTAL;
        panel.add(comp, c);
    }

    /** Membungkus form panel dan output area ke dalam JSplitPane vertikal (atas: form, bawah: output). */
    public static JPanel wrapPanel(JPanel formPanel, JTextArea outputArea) {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(formPanel), new JScrollPane(outputArea));
        splitPane.setResizeWeight(0.45);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }

    /** Membuat form panel otomatis dari array label dan field input, ditambah tombol aksi di bawah. */
    public static JPanel createFunctionForm(String[] labels, JComponent[] fields, JButton button) {
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

    /** Menampilkan pesan error dari exception ke output area. */
    public static void showError(JTextArea outputArea, Exception ex) {
        outputArea.setText("Error: " + ex.getMessage());
    }
}
