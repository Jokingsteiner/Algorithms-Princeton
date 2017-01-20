/*************************************************************************
 *  Compilation:  javac BruteCollinearPoints.java
 *  Execution:    none
 *  Dependencies: BruteCollinearPoints.java
 *
 *************************************************************************/
import java.util.Arrays;
import java.util.ArrayList;
/*
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
*/

public class BruteCollinearPoints {
    // delay in miliseconds (controls animation speed)
    //private static final int DELAY = 100;
    private LineSegment segments[];
    
    public BruteCollinearPoints(Point[] points) {    // finds all line segments containing 4 points
        //exceptionHandler1
        if (points == null)
            throw new java.lang.NullPointerException("Null Pointer");
        Point[] pointsCopy = Arrays.copyOf(points, points.length);          
        Arrays.sort(pointsCopy);
//exceptionHandler2
        exceptionHandler(pointsCopy);       
        
        ArrayList<LineSegment> lines = new ArrayList<LineSegment>();
        for (int i = 0; i < pointsCopy.length - 3; i++) {
            Point p = pointsCopy[i];
            for (int j = i + 1; j < pointsCopy.length - 2; j++) {
                Point q = pointsCopy[j];
                double pq = p.slopeTo(q);
                for (int m = j + 1; m < pointsCopy.length - 1; m++) {
                    Point r = pointsCopy[m];
                    double pr = p.slopeTo(r);
                    if (pq == pr) {
                        for (int n = m + 1; n < pointsCopy.length; n++) {
                            Point s = pointsCopy[n];
                            double ps = p.slopeTo(s);
                            if (pq == ps) {
                                LineSegment line = new LineSegment(p, s);
                                lines.add(line);
                            }
                        }
                    }
                }
            }
        }
        segments = lines.toArray(new LineSegment[lines.size()]);
    }
    public int numberOfSegments() {       // the number of line segments
        return segments.length;
    }
    public LineSegment[] segments() {                // the line segments
        return Arrays.copyOf(segments, numberOfSegments());
    }
    
    private void exceptionHandler(Point[] points){    //Exception handler
        // the following only applies to already sorted array of points, say sorted by y-axis
        // gurantee to O(n)
        for (int i = 0; i < points.length - 1; i++)
            if (points[i] == null)
                throw new java.lang.NullPointerException("Null Pointer inside points at index " + i);
            else if (points[i].compareTo(points[i + 1]) == 0)
                throw new IllegalArgumentException("Array has duplicate points");
    }
/*    
    public static void main(String[] args) {
        In in = new In(args[0]);      // input file
        int index = 0;
        // turn on animation mode
        StdDraw.enableDoubleBuffering();
        Point points[] = new Point[in.readInt()];
        
        // repeatedly read in sites to open and draw resulting system
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            points[index++] = new Point(i, j);
        }
        for (Point i : points)
            System.out.println(i.toString());
        
        BruteCollinearPoints test = new BruteCollinearPoints(points);
        StdDraw.show();
        StdDraw.pause(DELAY);
        System.out.println ("Size = " + test.numberOfSegments());
        for (LineSegment i : test.segments()){
            System.out.println(i.toString());
            i.draw();
        }
        
    }
    */
}