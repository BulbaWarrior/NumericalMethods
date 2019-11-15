package sample;

public class RungeKuttaMethod extends Numerical {
    public RungeKuttaMethod(Function f, double xZero, double yZero, double xFinal, int numSteps) throws Exception {
        super(f, xZero, yZero, xFinal, numSteps);
    }

    @Override
    double[] calculate() {
        double[] y = new double[numSteps + 1];
        y[0] = yZero;
        double x = xZero;
        double h = getH();
        for(int i = 1; i < numSteps + 1; i++){
            double k1 = yPrime.f(x, y[i-1]);
            double k2 = yPrime.f(x + h/2, y[i-1] + h*k1/2);
            double k3 = yPrime.f(x + h/2, y[i-1] + h*k2/2);
            double k4 = yPrime.f(x + h, y[i-1] + h*k3);
            y[i] = y[i-1] + h * (k1 + 2*k2 + 2*k3 + k4)/6;
            x += h;

        }
        return y;
    }
}
