/*************************************************************************
 *  Compilation:  javac FastCollinearPoints.java
 *  Execution:    none
 *  Dependencies: FastCollinearPoints.java
 *
 *************************************************************************/
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

public class FastCollinearPoints {
// finds all line segments containing 4 or more points
    private ArrayList<LineSegment> segments = new ArrayList<LineSegment>();// easy to add element in
    private HashMap<Double, ArrayList<Point>> slopeEndPoints = new HashMap<Double, ArrayList<Point>>(); // use to find duplicate segment (same endpoint when add), element is endpoints with same slope (key)
    
    public FastCollinearPoints(Point[] points) {    // finds all line segments containing 4 points
        //exceptionHandler1
        if (points == null)
            throw new java.lang.NullPointerException("Null Pointer");
        Point[] pointsCopy = points.clone();
        Arrays.sort(pointsCopy);
        // exceptionHandler2
        exceptionHandler(pointsCopy);
        
        for (int i = 0; i < points.length - 3; i++){
            // do not need to iterate the point viewed before!
            Point[] pointsRemain = Arrays.copyOfRange(pointsCopy, i, points.length);
            //sort the pointsRemain by SLOPE_ORDER comparator
            Arrays.sort(pointsRemain, pointsRemain[0].slopeOrder());
            int secondPoint = 1, last = 2;
            for (; last < pointsRemain.length; last++) {
                double slopeTo2ndPoint = pointsRemain[0].slopeTo(pointsRemain[secondPoint]);
                //error: double slopeToLast = pointsRemain[0].slopeTo(pointsRemain[last];
                //0~1, 0~2, 0~3 (4 points), 0~4(5 points), last is changing in every loop
                while (last < pointsRemain.length && Double.compare(slopeTo2ndPoint, pointsRemain[0].slopeTo(pointsRemain[last])) == 0) // while loop until the new point is out of line
                    last++;
                if (last - secondPoint >= 3) {
                    if (checkDuplicate(slopeTo2ndPoint, pointsRemain, last - 1)) // need minus 1 to compensate the last++
                        segments.add(new LineSegment(pointsRemain[0], pointsRemain[last - 1]));
                }
                // last has already been increased by last++, and in next loop will be increased by the for loop so it is enough
                secondPoint = last;
                //last++; // error
            }
        }
    }
    
    private boolean checkDuplicate(double slope, Point[] pts, int newEndPoint) {
        ArrayList<Point> endPointsWithSameSlope = this.slopeEndPoints.get(slope); // get(if there is) the endpoints(may more than 1 ) found before with the same slope
        if (endPointsWithSameSlope == null) { // good, no same slope, OK to add new segment
            endPointsWithSameSlope = new ArrayList<Point>(); // create new arraylist to store the endpoints with this slope
            endPointsWithSameSlope.add(pts[newEndPoint]);
            this.slopeEndPoints.put(slope, endPointsWithSameSlope); // use HashMap to save the slope and endpoints
            return true;
        }
        else if (endPointsWithSameSlope.contains(pts[newEndPoint])) //this newEndPoint has been included before with same slope (means the new segment is covered by old segment)
            return false;
        else { // new segment is paralled to the old segment
            //endPointsWithSameSlope.add(endPointsWithSameSlope.size(), pts[newEndPoint]);
            endPointsWithSameSlope.add(pts[newEndPoint]);
            this.slopeEndPoints.put(slope, endPointsWithSameSlope); // update the endpoint collection with this slope in HashMap
            return true;
        }
    }

    
    public int numberOfSegments() {       // the number of line segments
        return segments.size();
    }
    public LineSegment[] segments() {                // the line segments
        return segments.toArray(new LineSegment[segments.size()]);
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
}