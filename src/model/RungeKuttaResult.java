package model;

public class RungeKuttaResult {
    public final double lastX;
    public final double lastY;
    public final String log;

    public RungeKuttaResult(double lastX, double lastY, String log) {
        this.lastX = lastX;
        this.lastY = lastY;
        this.log = log;
    }
}
