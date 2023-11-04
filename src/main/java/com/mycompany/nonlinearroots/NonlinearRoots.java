package com.mycompany.nonlinearroots;
import java.io.*;
import java.util.*;

/* Future tasks
Graph each original function
Graph percent error y-axis compared to number of iterations
-All four methods on one graph^
-Y axis needs to be logarithmic

divergence test

How are intervals determined [a, b], xN, [xN, xN-1]
Write report showing print out table of values along with graphs
-Discuss starting points and convergence of the root
-Discuss strange observation
-Discuss data ypes used
 */

public class NonlinearRoots {
    private static BufferedWriter outFile;

    @FunctionalInterface
    public interface Function{
        double eval(double x);
    }

    public static void main(String[] args) {
        NonlinearRoots p3 = new NonlinearRoots();
        ArrayList<Double> rootsA = new ArrayList<>();
        ArrayList<Double> rootsB = new ArrayList<>();
        
        //Hard Coded function to be passed to our methods
        Function A = (double x) -> 2 * Math.pow(x, 3) - 11.7 * Math.pow(x, 2) + 17.7 * x - 5;
        Function B = (double x) -> x + 10 - x * Math.cosh(50/x);
        Function Aprime = (double x) -> 6 * Math.pow(x, 2) - 23.4 * x + 17.7;
        Function Bprime = (double x) ->(50 * Math.sinh(50/x)/x)- Math.cosh(50/x) + 1;
        
        
        
        //Bisection Method Function A
        applyMethod("Bisection", "A", 1, A, 0, 1, 0.365, 0, 0, A);
        applyMethod("Bisection", "A", 2, A, 1, 2, 1.922, 0, 0, A);
        applyMethod("Bisection", "A", 3, A, 3, 4, 3.563, 0, 0, A);
        
        //Bisection Method Function B
        applyMethod("Bisection", "B", 1, B, 120, 130, 126.632, 0, 0, A);
        
//        
        //Falsi Method Function A
        applyMethod("Falsi", "A", 1, A, 0, 1, 0.365, 0, 0, A);
        applyMethod("Falsi", "A", 2, A, 1, 2, 1.922, 0, 0, A);
        applyMethod("Falsi", "A", 3, A, 3, 4, 3.563, 0, 0, A);
        
        //Falsi Method Function B
        applyMethod("Falsi", "B", 1, B, 120, 130, 126.632, 0, 0, B);
        
        //Newton Method Function A
        applyMethod("Newton", "A", 1, A, 0, 1, 0.365, 1, 0, Aprime);
        applyMethod("Newton", "A", 2, A, 1, 2, 1.922, 2, 0, Aprime);
        applyMethod("Newton", "A", 3, A, 3, 4, 3.563, 4, 0, Aprime);
        
        //Newton Method Function B
        applyMethod("Newton", "B", 1, B, 120, 130, 126.632, 130, 0, Bprime);

        //Newton Method Function A
        applyMethod("Secant", "A", 1, A, 0, 1, 0.365, 0, 1, Aprime);
        applyMethod("Secant", "A", 2, A, 1, 2, 1.922, 1, 2, Aprime);
        applyMethod("Secant", "A", 3, A, 3, 4, 3.563, 3, 4, Aprime);
        
        //Newton Method Function B
        applyMethod("Secant", "B", 1, B, 120, 130, 126.632, 127, 130, Bprime);
    }
    
    public static void applyMethod(String mName, String fName, int rNum, Function f, double a, double b, double accurate, double Xn, double Xnm1, Function fp){
        printTableHeader(String.format("%s Method - Function %s - Root %d", mName, fName, rNum));
        switch (mName) {
            case "Bisection" -> Bisection(f, a, b, accurate, String.format("output/%sroot%d/%s.txt", fName, rNum, mName));
            case "Falsi" -> Falsi(f, a, b, accurate, String.format("output/%sroot%d/%s.txt", fName, rNum, mName));
            case "Newton" -> Newton(f, fp, Xn, accurate, String.format("output/%sroot%d/%s.txt", fName, rNum, mName));
            case "Secant" -> Secant(f, Xn, Xnm1, accurate, String.format("output/%sroot%d/%s.txt", fName, rNum, mName));
            default -> {
            }
        }
    }
    
    public static void Bisection(Function func, double a, double b, double accurate, String outFile){
        System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s%-15s\n", "Iteration", "a", "b", "c", "f(a)*f(c)", "f(b)*f(c)", "error");
        double c = 0, cXa, cXb, error; 
        openFile(outFile);
        for(int i = 0; i < 100; i++){
            c = (a+b)/2;
            cXa = func.eval(c)*func.eval(a);
            cXb = func.eval(c)*func.eval(b);
            error = findError(accurate,c);
            outputErr(error);
            System.out.printf("%-15d%-15f%-15f%-15f%-15f%-15f%-15f\n", i+1, a, b, c, cXa, cXb, error);
            if(cXa < 0)
                b = c;
            else if (cXb < 0)
                a = c;
            else{
            }
            //End bisection if error is less than 1 percent
            if(error < .01)
                break;
        }
        closeFile();
        printResult(c);
    }
    
    public static void Falsi(Function func, double a, double b, double accurate, String outFile){
        System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s%-15s\n", "Iteration", "a", "b", "c", "f(a)*f(c)", "f(b)*f(c)", "error");
        double c = 0, cXa, cXb, error;
        openFile(outFile);
        for(int i = 0; i < 100; i++){
            c = (a * func.eval(b) - b * func.eval(a)) / (func.eval(b) - func.eval(a));
            cXa = func.eval(c)*func.eval(a);
            cXb = func.eval(c)*func.eval(b);
            error = findError(accurate,c);
            outputErr(error);
            System.out.printf("%-15d%-15f%-15f%-15f%-15f%-15f%-15f\n", i+1, a, b, c, cXa, cXb, error);
            if(cXa < 0)
                b = c;
            else if (cXb < 0)
                a = c;
            //
            else{
            }
            //End falsi if error is less than 1 percent
            if(error < .01)
                break;
        }
        closeFile();
        printResult(c);
    }
        
    public static void Newton(Function func, Function funcPrime, double x, double accurate, String outFile){
        System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s%-15s\n", "Iteration", "Xn", "f(Xn)", "f'(Xn)", "f(Xn)/f'(Xn)", "Xn+1", "error");
        double error;
        double xNp1 = 0;
        double xN = x;
        openFile(outFile);
        for(int i = 0; i < 100; i++){
            xNp1 = xN - func.eval(xN)/funcPrime.eval(xN);
            xN = xNp1;
            error = findError(accurate,xNp1);
            outputErr(error);
            System.out.printf("%-15d%-15f%-15f%-15f%-15f%-15f%-15f\n", i+1, xN, func.eval(xN), funcPrime.eval(xN), func.eval(xN)/funcPrime.eval(xN), xNp1, error);
            //End Newton method if less than 1 percent
            if(error < .01)
                break;
        }
        closeFile();
        printResult(xNp1);
    }
    
    public static void Secant(Function func, double xN, double xNm1, double accurate, String outFile){
        System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s%-15s\n", "Iteration", "Xn", "Xn-1", "f(Xn)", "f(Xn-1)", "Xn+1", "error");
        double error;
        double xNp1 = 0;
        openFile(outFile);
        for(int i = 0; i < 100; i++){
            xNp1 = xN - ((xN - xNm1)/(func.eval(xN)-func.eval(xNm1))) * func.eval(xN);
            xNm1 = xN;
            xN = xNp1;
            error = findError(accurate,xNp1);
            System.out.printf("%-15d%-15f%-15f%-15f%-15f%-15f%-15f\n", i+1, xN,xNm1, func.eval(xN), func.eval(xN-1), xNp1, error);
            outputErr(error);
            //End Newton method if less than 1 percent
            if(error < .01)
                break;
        }
        closeFile();
        printResult(xNp1);
    }
    
    public static void printResult(double finalApproximation){
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        System.out.printf("Final Approximation: %f\n", finalApproximation);
        System.out.println("-----------------------------------------------------------------------------------------------------------\n");
    }
    
    public static void printTableHeader(String title){
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        System.out.println(title);
        System.out.println("-----------------------------------------------------------------------------------------------------------");
    }
    
    public static double findError(double accurate, double approximate){
        double absErr; 
        
        absErr = Math.abs(accurate - approximate)/Math.abs(accurate);
        
        return absErr;
    }
    
    public static void openFile(String fileName){
        try{
            outFile = new BufferedWriter(new FileWriter(fileName));
        }catch(IOException e){}
    }
    
    public static void outputErr(double error){
        try{
            outFile.write(String.format("%.10f\n", error));
        }catch(IOException e){}
    }
    
    public static void closeFile(){
        try{
            outFile.close();
        }catch(IOException e){}
    }
}
