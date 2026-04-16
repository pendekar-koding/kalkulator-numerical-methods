package model;

public class InterpolationResult {
    public final double[] coefficients;
    public final double value;
    public final String table;

    public InterpolationResult(double[] coefficients, double value, String table) {
        this.coefficients = coefficients;
        this.value = value;
        this.table = table;
    }
}
