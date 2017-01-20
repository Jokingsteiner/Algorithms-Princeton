/*----------------------------------------------------------------
 *  Author:        Junkai Cai
 *  Written:       12/4/2016
 *  Last updated:  12/4/2016
 *
 *  Compilation:   javac Percolation.java
 *  Execution:     java Percolation
 *  
 *----------------------------------------------------------------*/
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] myPData;
    private int T;
    
    public PercolationStats(int n, int trials) {    // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("Input n and trials should be greater than 0");
        this.T = trials;
        myPData = new double[trials];
        int runCount = 0;
        double opened = 0;
        Percolation perc = new Percolation(n);
        
        while (runCount < trials) {
            int currentRow = StdRandom.uniform(n) + 1;
            int currentCol = StdRandom.uniform(n) + 1;
            //System.out.println ("row = " + currentRow + ", col = " + currentCol);
            if (!perc.isOpen(currentRow, currentCol)){
                perc.open(currentRow, currentCol);
                opened++;                         
                if (perc.percolates()){                    
                    myPData[runCount] = opened / (n * n);
                    opened = 0; //reset opened count
                    runCount++;
                    if (runCount < trials)
                        perc = new Percolation(n);
                }
            }
        }
        
    }
   
    public double mean() {                          // sample mean of percolation threshold
        return StdStats.mean(myPData);
    }
    public double stddev() {                        // sample standard deviation of percolation threshold
        return StdStats.stddev(myPData);
    }
    public double confidenceLo() {                  // low  endpoint of 95% confidence interval
        return (mean() - 1.96 * stddev() / Math.sqrt(T));
    }
    public double confidenceHi() {                  // high endpoint of 95% confidence interval
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

    public static void main(String[] args) {    // test client (described below)
       int n, T;
       n = Integer.parseInt(args[0]);
       T = Integer.parseInt(args[1]);
       PercolationStats testObject = new PercolationStats(n, T);//

       StdOut.println ("mean                    = " + testObject.mean());
       StdOut.println ("stddev                  = " + testObject.stddev());
       StdOut.println ("95% confidence interval = " + testObject.confidenceLo() + ", " + testObject.confidenceHi());
   }
}