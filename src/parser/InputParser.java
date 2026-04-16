package parser;

import java.util.ArrayList;
import java.util.List;

public class InputParser {

    public static double[][] parseMatrix(String text) {
        String[] lines = text.trim().split("\\R+");
        List<double[]> rows = new ArrayList<>();
        int cols = -1;
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.trim().split("[,\t ]+");
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

    public static double[] parseVectorByLines(String text) {
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

    public static double[] parseVectorByComma(String text) {
        String[] parts = text.trim().split("[,\t ]+");
        if (parts.length == 0) throw new IllegalArgumentException("Input vektor tidak valid.");
        double[] result = new double[parts.length];
        for (int i = 0; i < parts.length; i++) result[i] = Double.parseDouble(parts[i].trim());
        return result;
    }
}
