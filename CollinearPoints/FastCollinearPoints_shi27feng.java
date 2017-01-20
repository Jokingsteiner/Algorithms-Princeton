import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import edu.princeton.cs.algs4.In;
 import edu.princeton.cs.algs4.StdDraw;
 import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    // finds all line segments containing 4 or more points
    private ArrayList<LineSegment> segments = new ArrayList<LineSegment>();
    private HashMap<Double, ArrayList<Point>> slopeEndPoints = new HashMap<Double, ArrayList<Point>>();

    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new java.lang.NullPointerException("Null Pointer");
        
        // Arrays.sort(points); 
        Point[] ptsClone = points.clone();
        // the array is firstly sorted along the y-axis, 
        // this is for eliminating unnecessary "look back", such as p->r (p.y < r.y),
        Arrays.sort(ptsClone);
        check(ptsClone);
        // there's no need to look back r->p.

        for (int i = 0; i < points.length - 3; i++) {
            Point[] pts = Arrays.copyOfRange(ptsClone, i, points.length);
            Arrays.sort(pts, pts[0].slopeOrder());
            int first = 1, last = 2;
            for (; last < pts.length; last++) {
                double slope = pts[0].slopeTo(pts[first]);
                while (last < pts.length && 
                        Double.compare(slope, pts[0].slopeTo(pts[last])) == 0) last++;
                if (last - first >= 3) {
                    if (endPointAvailable(slope, pts, last - 1)) 
                        segments.add(new LineSegment(pts[0], pts[last - 1]));
                }
                first = last;
            }
        }

    }

    private boolean endPointAvailable(double slope, Point[] pts, int end) {
        ArrayList<Point> ends = this.slopeEndPoints.get(slope);
        if (ends == null) {
            ends = new ArrayList<Point>();
            ends.add(pts[end]);
            this.slopeEndPoints.put(slope, ends);
            return true;
        } else if (ends.contains(pts[end])) return false;
        else {
            ends.add(ends.size(), pts[end]);
            this.slopeEndPoints.put(slope, ends);
            return true;
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }

    private void check(Point[] points) { 
        // the following only applies to already sorted array of points, say sorted by y-axis
        for (int i = 0; i < points.length - 1; i++)
            if (points[i] == null)
                throw new java.lang.NullPointerException("Null Pointer inside points at index " + i);
            else if (points[i].compareTo(points[i + 1]) == 0)
                throw new IllegalArgumentException("Array has duplicate points");
    }
    
    public static void main(String[] args) {
/*
        // read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenRadius(0.015);
        StdDraw.setPenColor(Color.BLUE);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            StdDraw.setPenRadius(0.005);
            StdDraw.setPenColor(Color.GREEN);
            segment.draw();
        }
        */
    }
    
}