package sample;

public class EulerMethod extends Numerical {

    public EulerMethod(Function f, double xZero, double yZero, double xFinal, int numSteps) throws Exception {
        super(f, xZero, yZero, xFinal, numSteps);
    }

    @Override
    double[] calculate() {
        double[] y = new double[numSteps + 1];
        y[0] = yZero;
        double h = getH();
        double x  = xZero;
        for(int i = 1; i < numSteps + 1; i++){
            y[i] = y[i-1] + h * yPrime.f(x, y[i-1]);
            x += h;
        }
        return y;
    }
}
