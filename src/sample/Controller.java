package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.*;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    final int N = 30;
    final int minN = 4;

    @FXML
    private TextField xZero, yZero, xFinal, numSteps;
    @FXML
    private LineChart<Number, Number> functionGraph, errorGraph, totalErrorGraph;
    private Series eulerSeries, analyticalSeries, improvedEulerSeries, rungeKuttaSeries;
    private Series eulerErrorSeries, improvedEulerErrorSeries, rungeKuttaErrorSeries;
    private Series eulerTotalErrorSeries, improvedEulerTotalErrorSeries, rungeKuttaTotalErrorSeries;
    private LinkedHashMap<Series, Numerical> seriesMap;
    private LinkedHashMap<Series, Numerical> errorSeriesMap;


    @FXML
    void handleButtonCalculate(){
        double xInitial, yInitial, xTarget;
        int n;
        xInitial = Double.parseDouble(xZero.getText());
        yInitial = Double.parseDouble(yZero.getText());
        xTarget = Double.parseDouble(xFinal.getText());
        n = Integer.parseInt(numSteps.getText());

        analyticalSolution.xZero = xInitial;
        analyticalSolution.yZero = yInitial;
        analyticalSolution.xFinal = xTarget;
        updateSeries(analyticalSeries, analyticalSolution);

        for(Series i : seriesMap.keySet()){
            Numerical method = seriesMap.get(i);
            method.yZero = yInitial;
            method.xZero = xInitial;
            method.xFinal = xTarget;
            method.numSteps = n;
            updateSeries(i, method);
        }
        for(Series i : errorSeriesMap.keySet()){
            updateErrorSeries(i, errorSeriesMap.get(i));
        }
        updateTotalErrorSeries(eulerTotalErrorSeries, euler);
        updateTotalErrorSeries(improvedEulerTotalErrorSeries, improvedEuler);
        updateTotalErrorSeries(rungeKuttaTotalErrorSeries, rungeKutta);

    }

    Function yPrime;
    Numerical euler, improvedEuler, rungeKutta, analyticalSolution;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        yPrime = new Function() {
            @Override
            double f(double x, double y) {
                return -(y*y)/3 - 2/(3*x*x);
            }
        };
        try {
            double xInitial = Double.parseDouble(xZero.getText());
            double yInitial = Double.parseDouble(yZero.getText());
            double xTarget = Double.parseDouble(xFinal.getText());
            int n = Integer.parseInt(numSteps.getText());
            euler = new EulerMethod(yPrime, xInitial, yInitial, xTarget, n);
            improvedEuler = new ImprovedEulerMethod(yPrime, xInitial, yInitial, xTarget, n);
            rungeKutta = new RungeKuttaMethod(yPrime, xInitial, yInitial, xTarget, n);

            analyticalSolution = new ExactSolution(xInitial, yInitial, xTarget, N);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }


        seriesMap = new LinkedHashMap<>();
        eulerSeries = new Series();
        eulerSeries.setName("Euler's Method");
        seriesMap.put(eulerSeries, euler);

        analyticalSeries = new Series();
        analyticalSeries.setName("Analytical Solution");

        improvedEulerSeries = new Series();
        improvedEulerSeries.setName("Improved Euler's method");
        seriesMap.put(improvedEulerSeries, improvedEuler);

        rungeKuttaSeries = new Series();
        rungeKuttaSeries.setName("Runge-Kutta method");
        seriesMap.put(rungeKuttaSeries, rungeKutta);

        errorSeriesMap = new LinkedHashMap<>();
        eulerErrorSeries = new Series();
        eulerErrorSeries.setName("Euler's Method");
        errorSeriesMap.put(eulerErrorSeries, euler);

        improvedEulerErrorSeries = new Series();
        improvedEulerErrorSeries.setName("Improved Euler's Method");
        errorSeriesMap.put(improvedEulerErrorSeries, improvedEuler);

        rungeKuttaErrorSeries = new Series();
        rungeKuttaErrorSeries.setName("Runge-Kutta method");
        errorSeriesMap.put(rungeKuttaErrorSeries, rungeKutta);

        eulerTotalErrorSeries = new Series();
        eulerTotalErrorSeries.setName("Euler's Method");

        improvedEulerTotalErrorSeries = new Series();
        improvedEulerTotalErrorSeries.setName("Improved Euler's Method");

        rungeKuttaTotalErrorSeries = new Series();
        rungeKuttaTotalErrorSeries.setName("Runge-Kutta method");


        functionGraph.getData().addAll(analyticalSeries, eulerSeries, improvedEulerSeries, rungeKuttaSeries);
        errorGraph.getData().addAll(eulerErrorSeries, improvedEulerErrorSeries, rungeKuttaErrorSeries);
        totalErrorGraph.getData().addAll(eulerTotalErrorSeries, improvedEulerTotalErrorSeries, rungeKuttaTotalErrorSeries);


        updateSeries(analyticalSeries, analyticalSolution);
        updateSeries(eulerSeries, euler);
        updateSeries(improvedEulerSeries, improvedEuler);
        updateSeries(rungeKuttaSeries, rungeKutta);

        updateErrorSeries(eulerErrorSeries, euler);
        updateErrorSeries(improvedEulerErrorSeries, improvedEuler);
        updateErrorSeries(rungeKuttaErrorSeries, rungeKutta);

        updateTotalErrorSeries(eulerTotalErrorSeries, euler);
        updateTotalErrorSeries(improvedEulerTotalErrorSeries, improvedEuler);
        updateTotalErrorSeries(rungeKuttaTotalErrorSeries, rungeKutta);
    }

    void updateSeries(Series series, Numerical method){
        double x = method.xZero;
        double[] y = method.calculate();
        double h = method.getH();
        series.getData().clear();
        for(int i = 0; i < method.numSteps + 1; i++){
            series.getData().add(new Data(x, y[i]));
            x += h;
        }
    }

    void updateErrorSeries(Series series, Numerical method){
        double x = method.xZero;
        double[] y = method.calculateError();
        double h = method.getH();
        series.getData().clear();
        for(int i = 0; i < method.numSteps + 1; i++){
            series.getData().add(new Data(x, y[i]));
            x += h;
        }
    }

    void updateTotalErrorSeries(Series series, Numerical method){
        double[] y = method.calculateTotalError(minN, N);
        series.getData().clear();
        for(int i = 0; i < y.length; i++){
            series.getData().add(new Data(i, y[i]));
        }
    }
}

