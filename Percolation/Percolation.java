/*----------------------------------------------------------------
 *  Author:        Junkai Cai
 *  Written:       12/4/2016
 *  Last updated:  12/4/2016
 *
 *  Compilation:   javac Percolation.java
 *  Execution:     java Percolation
 *  
 *----------------------------------------------------------------*/
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
   private int szOfGrid; //size of grid 
   private char[] openMap;
   private WeightedQuickUnionUF ufMap;
   private boolean percolateFlag;
   private static final char OPENMASK = 0x01;
   private static final char TOPMASK = 0x04;
   private static final char BOTTOMMASK = 0x02;
   /**
   *  Constructor  
   *  create n-by-n grid, with all sites blocked
   *   
   */
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("Input n should be greater than 0");
        szOfGrid = n;
        percolateFlag = false;
        //no virtual top/bottom implementation
        ufMap = new WeightedQuickUnionUF(szOfGrid * szOfGrid);
        //initialize openMap to track if a site is open
        openMap = new char[szOfGrid * szOfGrid];
        for (int i = 0; i < szOfGrid * szOfGrid - 1; i++)
            openMap[i] = 0x00; //blocked, not top, not bottom
    }
    
   /**
   *  xyTo1D(int, int)  
   *  convert (1,1) convention 2D index to 1D for WQU data type
   *   
   */
    private int xyTo1D(int row, int col) {
        //ufMap[szOfGrid] for virtual top, ufMap[szOfGrid+1] for virtual bottom
        return szOfGrid * (row - 1) + (col - 1);
    }
    
   /**
   *  validIndices (int row, int col) 
   *  valid index outbounded or not
   *   
   */
private boolean validIndices(int row, int col) {
        if (row <= 0 || row > szOfGrid)
           return false;
        if (col <= 0 || col > szOfGrid)
           return false;
        return true;
    }
    
   /**
   *  validIndicesThrowEx (int row, int col) 
   *  valid index outbounded or not and throw exception
   *   
   */    
    private void validIndicesThrowEx(int row, int col) {
        if (row <= 0 || row > szOfGrid) {
           //System.out.println ("row = " + row);
           throw new IndexOutOfBoundsException("row index i out of bounds");
        }
        if (col <= 0 || col > szOfGrid) {
            //System.out.println ("col = " + col);
            throw new IndexOutOfBoundsException("col index i out of bounds");            
        }
    }
    
   /**
   *  open(int row, int col)  
   *  open site (row, col) if it is not open already
   *  1. it should validate the indices of the site that it receives.
   *  2. it should somehow mark the site as open.
   *  3. it should perform some sequence of WeightedQuickUnionUF operations
   *     that links the site in question to its open neighbors.   *   
   */
   public void open(int row, int col) {       
       validIndicesThrowEx(row, col);
       openMap[xyTo1D(row, col)] |= OPENMASK;
       char tempStatus = OPENMASK;
       
       //if this site at top row
       if (row == 1)
           tempStatus |= TOPMASK;
       //if this site at bottom row
       if (row == szOfGrid)
           tempStatus |= BOTTOMMASK;
       
       tempStatus |= openMap[ufMap.find(xyTo1D(row, col))];
       //down
       if (validIndices(row - 1, col) && isOpen(row - 1, col)) {
           tempStatus |= openMap[ufMap.find(xyTo1D(row - 1, col))];         
           ufMap.union(xyTo1D(row, col), xyTo1D (row - 1, col));
       }
       //up
       if (validIndices(row + 1, col) && isOpen(row + 1, col)) {
           tempStatus |= openMap[ufMap.find(xyTo1D(row + 1, col))];         
           ufMap.union(xyTo1D(row, col), xyTo1D (row + 1, col));
       }
       //left
       if (validIndices(row, col - 1) && isOpen(row, col - 1)) {
           tempStatus |= openMap[ufMap.find(xyTo1D(row, col - 1))];         
           ufMap.union(xyTo1D(row, col), xyTo1D (row, col - 1));
       }
       //right
       if (validIndices(row, col + 1) && isOpen(row, col + 1)) {
           tempStatus |= openMap[ufMap.find(xyTo1D(row, col + 1))];         
           ufMap.union(xyTo1D(row, col), xyTo1D (row, col + 1));
       }
       
       openMap[ufMap.find(xyTo1D(row, col))] = tempStatus;
       if  ((openMap[ufMap.find(xyTo1D(row, col))] & TOPMASK) != 0
               &&
            (openMap[ufMap.find(xyTo1D(row, col))] & BOTTOMMASK) != 0
           )
           percolateFlag = true;
   }
   
   /**
   *  isOpen(int row, int col)  
   *  is site (row, col) open?
   *   
   */ 
   public boolean isOpen(int row, int col) {  
       validIndicesThrowEx(row, col);
       if ((openMap[xyTo1D(row, col)] & OPENMASK) != 0)
           return true;
       else
           return false;
   }
   
   /**
   *  isFull(int row, int col)
   *  is site (row, col) full?
   *   
   */    
   public boolean isFull(int row, int col) { 
       validIndicesThrowEx(row, col);
       if ((openMap[ufMap.find(xyTo1D(row, col))] & TOPMASK) != 0)
           return true;
       else
           return false;
   }
   
   /**
   *  percolates()
   *  does the system percolate?
   *   
   */   
   public boolean percolates() {
       return percolateFlag;
   }
   
   /**
   *  
   *   
   */   
   public static void main(String[] args) {
    /*   
        System.out.println("Testing!");
        Percolation textObject = new Percolation (3);
        textObject.open(1, 1);
        textObject.open(1, 2);
        textObject.open(2, 2);
        textObject.open(3, 2);
        System.out.println("Testing!" + textObject.ufMap.connected (1, 8));
        System.out.println("percolates?" + textObject.percolates());
    */   
    }
}