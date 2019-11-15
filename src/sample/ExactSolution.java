package sample;

public class ExactSolution extends Numerical {
    public ExactSolution(double xZero, double yZero, double xFinal, int numSteps) {
        super(new Function() {
            @Override
            double f(double x, double y) {
                return 0;
            }
        }, xZero, yZero, xFinal, numSteps);
    }

    @Override
    double[] calculate() {
        double c = Math.pow(xZero, 1/3)*(yZero*xZero - 2)/(1 - yZero*xZero);
        double[] y = new double[numSteps+1];
        double x = xZero;
        for(int i = 0; i < numSteps + 1; i++){

            y[i] = (2*Math.pow(x, 1/3) + c)/ (x*(Math.pow(x, 1/3) + c));
            x += getH();
        }
        return y;
    }
}
