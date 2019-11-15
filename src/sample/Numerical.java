package sample;

public abstract class Numerical {

    Function yPrime;

    public void setxZero(double xZero) throws Exception {
        this.xZero = xZero;
        if(xZero > xFinal){
            throw new Exception("initial value greater then final value");
        }
    }

    public void setyZero(double yZero) {
        this.yZero = yZero;
    }

    public void setxFinal(double xFinal) throws Exception {
        if(xZero > xFinal){
            throw new Exception("initial value greater then final value");
        }
    }

    public void setNumSteps(int numSteps) throws Exception {
        if(numSteps < 0)
            throw new Exception("improper number of steps");
        this.numSteps = numSteps;
    }

    double xZero;
    double yZero;
    double xFinal;
    int numSteps;
    public double getH(){
        return (xFinal - xZero)/numSteps;
    }
    abstract double[] calculate();

    public double[] calculateError(){
        ExactSolution exact = new ExactSolution(xZero, yZero, xFinal, numSteps);

        double[] approximation = calculate();
        double[] solution = exact.calculate();
        double[] error = new double[numSteps + 1];
        for(int i = 0; i < approximation.length; i++){

            error[i] = Math.abs(approximation[i] - solution[i]);

        }
        return error;

    }

    double[] calculateTotalError(int begin, int end){
        int n = numSteps;
        double[] totalError = new double[end];
        for(int i = begin; i < end; i++){
            numSteps = i;
            totalError[i] = calculateError()[i];
        }
        numSteps = n;
        return totalError;

    }

    public Numerical(Function f, double xZero, double yZero, double xFinal, int numSteps) {

        yPrime = f;
        this.xZero = xZero;
        this.yZero = yZero;
        this.xFinal = xFinal;
        this.numSteps = numSteps;

    }

}
