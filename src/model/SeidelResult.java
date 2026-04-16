package model;

public class SeidelResult {
    public final double[] solution;
    public final boolean converged;
    public final double error;
    public final String iterationLog;

    public SeidelResult(double[] solution, boolean converged, double error, String iterationLog) {
        this.solution = solution;
        this.converged = converged;
        this.error = error;
        this.iterationLog = iterationLog;
    }
}
