package com.mycompany.nonlinearroots;
import java.io.*;


public class NonlinearRoots {
    private static BufferedWriter outFile;

    @FunctionalInterface
    public interface Function{
        double eval(double x);
    }

    public static void main(String[] args) {    
        //Hard Coded function to be passed to our methods
        Function A = (double x) -> 2 * Math.pow(x, 3) - 11.7 * Math.pow(x, 2) + 17.7 * x - 5;
        Function B = (double x) -> x + 10 - x * Math.cosh(50/x);
        Function Aprime = (double x) -> 6 * Math.pow(x, 2) - 23.4 * x + 17.7;
        Function Bprime = (double x) ->(50 * Math.sinh(50/x)/x)- Math.cosh(50/x) + 1;
        
        //Function A - Root 1
        applyMethod("Bisection", "A", 1, A, 0, 1,  0, 0, A);
        applyMethod("Falsi", "A", 1, A, 0, 1,  0, 0, A);
        applyMethod("Newton", "A", 1, A, 0, 1, .5, 0, Aprime);
        applyMethod("Secant", "A", 1, A, 0, 1,  0, .5, Aprime);

        //Function A - Root 2
        applyMethod("Bisection", "A", 2, A, 1, 2,  0, 0, A);
        applyMethod("Falsi", "A", 2, A, 1, 2, 0, 0, A);
        applyMethod("Newton", "A", 2, A, 1, 2,  2, 0, Aprime);
        applyMethod("Secant", "A", 2, A, 1, 2,  1, 2, Aprime);

        //Function A - Root 3
        applyMethod("Bisection", "A", 3, A, 3, 4,  0, 0, A);
        applyMethod("Falsi", "A", 3, A, 3, 4,  0, 0, A);
        applyMethod("Newton", "A", 3, A, 3, 4,  4, 0, Aprime);
        applyMethod("Secant", "A", 3, A, 3, 4,  3, 4, Aprime);
        
        //Function B - Root 1
        applyMethod("Bisection", "B", 1, B, 120, 130,  0, 0, A);
        applyMethod("Falsi", "B", 1, B, 120, 130,  0, 0, B);
        applyMethod("Newton", "B", 1, B, 120, 130,  130, 0, Bprime);
        applyMethod("Secant", "B", 1, B, 120, 130,  120, 130, Bprime);
    }
    
    public static void applyMethod(String mName, String fName, int rNum, Function f, double a, double b, double Xn, double Xnm1, Function fp){
        printTableHeader(String.format("%s Method - Function %s - Root %d", mName, fName, rNum));
        switch (mName) {
            case "Bisection" -> Bisection(f, a, b, String.format("output/%sroot%d/%s.txt", fName, rNum, mName));
            case "Falsi" -> Falsi(f, a, b,  String.format("output/%sroot%d/%s.txt", fName, rNum, mName));
            case "Newton" -> Newton(f, fp, Xn,  String.format("output/%sroot%d/%s.txt", fName, rNum, mName));
            case "Secant" -> Secant(f, Xn, Xnm1,  String.format("output/%sroot%d/%s.txt", fName, rNum, mName));
            default -> {
            }
        }
    }
    
    public static void Bisection(Function func, double a, double b, String outFile){
        System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s%-15s\n", "Iteration", "a", "b", "c", "f(a)*f(c)", "f(b)*f(c)", "error");
        double c = 0, cXa, cXb, error; 
        double prev = 0;
        openFile(outFile);
        for(int i = 0; i < 100; i++){
            c = (a+b)/2;
            cXa = func.eval(c)*func.eval(a);
            cXb = func.eval(c)*func.eval(b);
            error = findError(c,prev);
            outputErr(error);
            prev = c;
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
    
    public static void Falsi(Function func, double a, double b, String outFile){
        System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s%-15s\n", "Iteration", "a", "b", "c", "f(a)*f(c)", "f(b)*f(c)", "error");
        double c = 0, cXa, cXb, error;
        double prev = 0;
        openFile(outFile);
        for(int i = 0; i < 100; i++){
            c = (a * func.eval(b) - b * func.eval(a)) / (func.eval(b) - func.eval(a));
            cXa = func.eval(c)*func.eval(a);
            cXb = func.eval(c)*func.eval(b);
            error = findError(c,prev);
            outputErr(error);
            prev = c;
            System.out.printf("%-15d%-15f%-15f%-15f%-15f%-15f%-15f\n", i+1, a, b, c, cXa, cXb, error);
            if(cXa < 0)
                b = c;
            else if (cXb < 0)
                a = c;
            else{
            }
            //End falsi if error is less than 1 percent
            if(error < .01)
                break;
        }
        closeFile();
        printResult(c);
    }
        
    public static void Newton(Function func, Function funcPrime, double x,  String outFile){
        System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s%-15s\n", "Iteration", "Xn", "f(Xn)", "f'(Xn)", "f(Xn)/f'(Xn)", "Xn+1", "error");
        double error;
        double xNp1 = 0;
        double xN = x;
        double prev = 0;
        openFile(outFile);
        for(int i = 0; i < 100; i++){
            xNp1 = xN - func.eval(xN)/funcPrime.eval(xN);
            error = findError(xNp1,xN);
            outputErr(error);
            System.out.printf("%-15d%-15f%-15f%-15f%-15f%-15f%-15f\n", i+1, xN, func.eval(xN), funcPrime.eval(xN), func.eval(xN)/funcPrime.eval(xN), xNp1, error);
            xN = xNp1;
            //End Newton method if less than 1 percent
            if(error < .01)
                break;
        }
        closeFile();
        printResult(xNp1);
    }
    
    public static void Secant(Function func, double xN, double xNm1, String outFile){
        System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s%-15s\n", "Iteration", "Xn", "Xn-1", "f(Xn)", "f(Xn-1)", "Xn+1", "error");
        double error;
        double xNp1 = 0;
        openFile(outFile);
        for(int i = 0; i < 100; i++){
            xNp1 = xN - ((xN - xNm1)/(func.eval(xN)-func.eval(xNm1))) * func.eval(xN);
            error = findError(xNp1,xN);
            outputErr(error);
            System.out.printf("%-15d%-15f%-15f%-15f%-15f%-15f%-15f\n", i+1, xN,xNm1, func.eval(xN), func.eval(xN-1), xNp1, error);
            xNm1 = xN;
            xN = xNp1;
            //End Secant method if less than 1 percent
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
    
    public static double findError(double curr, double prev){
        double absErr; 
        
        absErr = Math.abs(curr - prev)/Math.abs(curr);
        
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
