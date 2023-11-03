package com.mycompany.nonlinearroots;
import java.io.*;
import java.util.*;

/*
Graph each original function
Graph percent error y-axis compared to number of iterations
-All four methods on one graph^
-Y axis needs to be logarithmic
Maybe remove arraylist

How are intervals determined [a, b], xN, [xN, xN-1]???
Write report showing print out table of values along with graphs
-Discuss starting points and convergence of the root
-Discuss strange observation
-Discuss data ypes used
 */

public class NonlinearRoots {
    private BufferedWriter outFile;
    

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
        p3.Bisection(A, 0, 1, rootsA, 0.365, "Aroot1-Bisection.txt");
        p3.Bisection(A, 1, 2, rootsA, 1.922, "Aroot2-Bisection.txt");
        p3.Bisection(A, 2, 4, rootsA, 3.563, "Aroot3-Bisection.txt");
//        System.out.println(rootsA);
//        rootsA = new ArrayList<>();
        //Function B
        p3.Bisection(B, 120, 130, rootsB, 126.632, "Broot1-Bisection.txt");
//        System.out.println(rootsB);
//        rootsB = new ArrayList<>();
        
        //Falsi Method Function A
        p3.Falsi(A, 0, 1, rootsA, 0.365, "Aroot1-Falsi.txt");
        p3.Falsi(A, 1, 2, rootsA, 1.922, "Aroot2-Falsi.txt");
        p3.Falsi(A, 2, 4, rootsA, 3.563, "Aroot3-Falsi.txt");
//        System.out.println(rootsA);
//        rootsA = new ArrayList<>();
        //Function B
        p3.Falsi(B, 120, 130, rootsB, 126.632, "Broot1-Falsi.txt");
//        System.out.println(rootsB);
//        rootsB = new ArrayList<>();
        
        //Newton Rhapson Method Function A
        p3.Newton(A, Aprime, 1, rootsA, 0.365, "Aroot1-Newton.txt");
        p3.Newton(A, Aprime, 2, rootsA, 1.922, "Aroot2-Newton.txt");
        p3.Newton(A, Aprime, 4, rootsA, 3.563, "Aroot3-Newton.txt");
//        System.out.println(rootsA);
//        rootsA = new ArrayList<>();
        
        //Secant Method Function A
        p3.Secant(A, 0, 1, rootsA, 0.365, "Aroot1-Secant.txt");
        p3.Secant(A, 1, 2, rootsA, 1.922, "Aroot2-Secant.txt");
        p3.Secant(A, 3, 4, rootsA, 3.563, "Aroot3-Secant.txt");
//        System.out.println(rootsA);
//        rootsA = new ArrayList<>();
    }
    
    public NonlinearRoots(){
        
    }
    
    public void Bisection(Function func, double a, double b, ArrayList<Double> roots, double accurate, String outFile){
        double c = 0, cXa, cXb, error; 
        openFile(outFile);
        for(int i = 0; i < 100; i++){
            c = (a+b)/2;
            cXa = func.eval(c)*func.eval(a);
            cXb = func.eval(c)*func.eval(b);
            error = absError(accurate,c);
            outputErr(error);
            if(cXa < 0)
                b = c;
            else if (cXb < 0)
                a = c;
            else{
                roots.add(c);
            }
            //End bisection if error is less than 1 percent
            if(error < .01)
                break;
        }
        closeFile();
        roots.add(c);
    }
    
    public void Falsi(Function func, double a, double b, ArrayList<Double> roots, double accurate, String outFile){
        double c = 0, cXa, cXb, error;
        openFile(outFile);
        for(int i = 0; i < 100; i++){
            c = (a * func.eval(b) - b * func.eval(a)) / (func.eval(b) - func.eval(a));
            cXa = func.eval(c)*func.eval(a);
            cXb = func.eval(c)*func.eval(b);
            error = absError(accurate,c);
            outputErr(error);
            if(cXa < 0)
                b = c;
            else if (cXb < 0)
                a = c;
            //
            else{
                roots.add(c);
            }
            //End falsi if error is less than 1 percent
            if(error < .01)
                break;
        }
        closeFile();
        roots.add(c);
    }
        
    public void Newton(Function func, Function funcPrime, double x, ArrayList<Double> roots, double accurate, String outFile){
        double error;
        double xNp1 = 0;
        double xN = x;
        openFile(outFile);
        for(int i = 0; i < 100; i++){
            xNp1 = xN - func.eval(xN)/funcPrime.eval(xN);
            xN = xNp1;
            error = absError(accurate,xNp1);
            outputErr(error);
            //End Newton method if less than 1 percent
            if(error < .01)
                break;
        }
        closeFile();
        roots.add(xNp1);
    }
    
    public void Secant(Function func, double xN, double xNm1, ArrayList<Double> roots, double accurate, String outFile){
        double error;
        double xNp1 = 0;
        openFile(outFile);
        for(int i = 0; i < 100; i++){
            xNp1 = xN - ((xN - xNm1)/(func.eval(xN)-func.eval(xNm1))) * func.eval(xN);
            xNm1 = xN;
            xN = xNp1;
            error = absError(accurate,xNp1);
            outputErr(error);
            //End Newton method if less than 1 percent
            if(error < .01)
                break;
        }
        closeFile();
        roots.add(xNp1);
    }
    
    public double absError(double accurate, double approximate){
        double absErr; 
        
        absErr = Math.abs(accurate - approximate)/Math.abs(accurate);
        
        return absErr;
    }
    
    public void openFile(String fileName){
        try{
            outFile = new BufferedWriter(new FileWriter(fileName));
        }catch(IOException e){}
    }
    
    public void outputErr(double error){
        try{
            outFile.write(Double.toString(error)+"\n");
        }catch(IOException e){}
    }
    
    public void closeFile(){
        try{
            outFile.close();
        }catch(IOException e){}
    }
}
