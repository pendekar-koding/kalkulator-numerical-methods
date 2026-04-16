package model;

public class GaussResult {
    public final double[] solution;
    public final String steps;

    public GaussResult(double[] solution, String steps) {
        this.solution = solution;
        this.steps = steps;
    }
}
