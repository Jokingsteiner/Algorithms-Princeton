import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.Comparator;
import java.util.ArrayList;

public class KdTree {
    private Node root;
    private int numOfNode;
    private static final boolean VERTICAL = true; // if the node splits area with a vertical line
    private static final boolean HORIZONTAL = false; // if the node splits area with a horizontal line
    
    private static class Node {
        private Point2D p;      // the point
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        
        public Node(Point2D p) {
            this.p = p;              
        }
    }
        
    public KdTree() {                               // construct an empty set of points 
        root = null;
        numOfNode = 0;            
    }
    
    public boolean isEmpty() {                      // is the set empty? 
        return numOfNode == 0;
    }
    
    public int size() {                         // number of points in the set 
        return numOfNode;
    }
    
    public void insert(Point2D p) {              // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new java.lang.NullPointerException("input point is null");
        root = insert(root, p, VERTICAL);
    }
    
    private Node insert(Node lastNode, Point2D newPoint, boolean nodeType) {
        // for first insert(create root)
        if (lastNode == null) {
            this.numOfNode++;
            return new Node(newPoint);
        }
        
        // don't insert if equal to lastNode
        if (lastNode.p.equals(newPoint)) {
            return lastNode;
        }
        
        // use comparator in Point2D data type to locate which half of the area should the new point go
        Comparator<Point2D> comparator = Point2D.Y_ORDER;
        if (nodeType == VERTICAL) {
            comparator = Point2D.X_ORDER;
        }
        
        // if didn't find
        int cmp = comparator.compare(newPoint, lastNode.p);
        if (cmp < 0) {
            lastNode.lb = insert(lastNode.lb, newPoint, !nodeType);
        }
        else {
            lastNode.rt = insert(lastNode.rt, newPoint, !nodeType);
        }
        return lastNode;
        
    }
    
    public boolean contains(Point2D p) {            // does the set contain point p? 
        if (p == null)
            throw new java.lang.NullPointerException("input point is null");
        return contains(root, p, VERTICAL);
    }
    
    private boolean contains(Node lastNode, Point2D queryPoint, boolean nodeType) {
        // the KdTree was empty or finally reach the null leaves
        if (lastNode == null)
            return false;
        
        // found the point
        if (lastNode.p.equals(queryPoint)) {
            return true;
        }
        
        // use comparator in Point2D data type to locate which half of the area should the new point go
        Comparator<Point2D> comparator = Point2D.Y_ORDER;
        // if is vertical
        if (nodeType == VERTICAL) {
            comparator = Point2D.X_ORDER;
        }
        
        // if didn't find        
        int cmp = comparator.compare(queryPoint, lastNode.p);
        if (cmp < 0) {
            return contains(lastNode.lb, queryPoint, !nodeType);
        }
        else {
            return contains(lastNode.rt, queryPoint, !nodeType);
        }
        
    }

    public void draw() {                         // draw all points to standard draw 
        draw(root, new RectHV(0.0, 0.0, 1.0, 1.0), VERTICAL);
    }
    
    private void draw(Node currentNode, RectHV rect, boolean nodeStyle) {
        // finish draw recursion(down to the bottom of the tree)
        if (currentNode == null)
            return;
        
        //set pen before drawing points
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        currentNode.p.draw();
        
        if (nodeStyle == VERTICAL) {
            // set pen before drawing (horizontal) splitting line
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(currentNode.p.x(), rect.ymin(), currentNode.p.x(), rect.ymax());
            draw(currentNode.lb, new RectHV(rect.xmin(), rect.ymin(), currentNode.p.x(), rect.ymax()), !nodeStyle);
            draw(currentNode.rt, new RectHV(currentNode.p.x(), rect.ymin(), rect.xmax(), rect.ymax()), !nodeStyle);
        }
        else if (nodeStyle == HORIZONTAL) {
            // set pen before drawing (vertical) splitting line
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(rect.xmin(), currentNode.p.y(), rect.xmax(), currentNode.p.y());
            draw(currentNode.lb, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), currentNode.p.y()), !nodeStyle);
            draw(currentNode.rt, new RectHV(rect.xmin(), currentNode.p.y(), rect.xmax(), rect.ymax()), !nodeStyle);
        }
    }

    /**
     * Locates all points in the set that are inside the rectangle.
     */ 

    public Iterable<Point2D> range(RectHV rect) {             // all points that are inside the rectangle 
        ArrayList<Point2D> rangeResult = new ArrayList<Point2D>();
        range(root, rect, rangeResult, VERTICAL);
        return rangeResult;
    }
    
    private void range(Node lastNode, RectHV rect, ArrayList<Point2D> rangeResult, boolean nodeStyle) {
        if (lastNode == null)
            return;
        
        // add the found one into the list and continue range search
        if (rect.contains(lastNode.p))
            rangeResult.add(lastNode.p);
        
        double pointCoord = lastNode.p.y();
        double rectCoordMin = rect.ymin();
        double rectCoordMax = rect.ymax();
        if (nodeStyle == VERTICAL) {
            pointCoord = lastNode.p.x();
            rectCoordMin = rect.xmin();
            rectCoordMax = rect.xmax();
        }

        if (rectCoordMin < pointCoord) {
            range(lastNode.lb, rect, rangeResult, !nodeStyle);
        }
        if (rectCoordMax >= pointCoord) {
            range(lastNode.rt, rect, rangeResult, !nodeStyle);
        }
    }
    
    public Point2D nearest(Point2D p) {            // a nearest neighbor in the set to point p; null if the set is empty 
        if (p == null)
            throw new java.lang.NullPointerException("input point is null");
        return nearest(root, p, Double.POSITIVE_INFINITY, new RectHV(0.0, 0.0, 1.0, 1.0), VERTICAL);
    }

    private Point2D nearest(Node currentNode, Point2D queryPoint, double distanceSQ, RectHV thisRect, boolean nodeType) {
        if (currentNode == null){
            return null;
        }      
        
        // means it is impossible to find a nearer point in both squares(subtrees)
        if (thisRect.distanceSquaredTo(queryPoint) >= distanceSQ) {
            return null;
        }
        
        Point2D nearestPoint = null;
        double nearestDistanceSQ = distanceSQ;
        double d;
        
        d = queryPoint.distanceSquaredTo(currentNode.p);
        // so we have a new nearestPoint, set to currentNode and ready to return
        if (d < nearestDistanceSQ) {
            nearestPoint = currentNode.p;
            nearestDistanceSQ = d;
        }
        
        // continue to search in the subtree
        // firstNode is the children node that closed to the query point
        // cuz it is still possible to find a nearer point in the other half(secondNode)
        // if no nearer point in this half(firstNode)
        Node firstNode = currentNode.lb;
        Node secondNode = currentNode.rt;
        RectHV firstNodeRect = null;
        RectHV secondNodeRect = null;
        if (nodeType == VERTICAL) {
            firstNodeRect = new RectHV(thisRect.xmin(), thisRect.ymin(), currentNode.p.x(), thisRect.ymax());
            secondNodeRect = new RectHV(currentNode.p.x(), thisRect.ymin(), thisRect.xmax(), thisRect.ymax());
        }
        else if (nodeType == HORIZONTAL) {
            firstNodeRect = new RectHV(thisRect.xmin(), thisRect.ymin(), thisRect.xmax(), currentNode.p.y());
            secondNodeRect = new RectHV(thisRect.xmin(), currentNode.p.y(), thisRect.xmax(), thisRect.ymax());
        }
        
        if (firstNode != null && secondNode != null) {
            firstNode = currentNode.rt;
            secondNode = currentNode.lb;            
            RectHV tempRect = firstNodeRect;
            firstNodeRect = secondNodeRect;
            secondNodeRect = tempRect;
            tempRect = null;            
        }

        Point2D firstNearestPoint = nearest(firstNode, queryPoint, nearestDistanceSQ, firstNodeRect, !nodeType);
        if (firstNearestPoint != null) {
            d = queryPoint.distanceSquaredTo(firstNearestPoint);
            if (d < nearestDistanceSQ) {
                nearestPoint = firstNearestPoint;
                nearestDistanceSQ = d;
            }
        }
        
        if (secondNodeRect.distanceSquaredTo(queryPoint) < nearestDistanceSQ){
            Point2D secondNearestPoint = nearest(secondNode, queryPoint, nearestDistanceSQ, secondNodeRect, !nodeType);
            if (secondNearestPoint != null) {
                d = queryPoint.distanceSquaredTo(secondNearestPoint);
                if (d < nearestDistanceSQ) {
                    nearestPoint = secondNearestPoint;
                    nearestDistanceSQ = d;
                }
            }
        }
        
        return nearestPoint;
    }

    public static void main(String[] args) {                 // unit testing of the methods (optional) 
        /*
        KdTree kdtree = new KdTree();
        assert kdtree.size() == 0;
        kdtree.insert(new Point2D(.7, .2));
        assert kdtree.size() == 1;
        kdtree.insert(new Point2D(.5, .4));
        kdtree.insert(new Point2D(.2, .3));
        kdtree.insert(new Point2D(.4, .7));
        kdtree.insert(new Point2D(.9, .6));
        assert kdtree.size() == 5;
        //StdOut.println(kdtree);

        kdtree = new KdTree();
        kdtree.insert(new Point2D(0.206107, 0.095492));
        kdtree.insert(new Point2D(0.975528, 0.654508));
        kdtree.insert(new Point2D(0.024472, 0.345492));
        kdtree.insert(new Point2D(0.793893, 0.095492));
        kdtree.insert(new Point2D(0.793893, 0.904508));
        kdtree.insert(new Point2D(0.975528, 0.345492));
        assert kdtree.size() == 6;
        kdtree.insert(new Point2D(0.206107, 0.904508));
        assert kdtree.size() == 7;

        StdOut.println("size: " + kdtree.size());
        */
        /*
        KdTree test = new KdTree();
        Point2D point1 = new Point2D(0.5, 0.6);
        Point2D point2 = new Point2D(0.1, 0.8);
        Point2D point3 = new Point2D(0.9, 0.1);
        Point2D point4 = new Point2D(0.5, 0.6);
        Point2D point5 = new Point2D(0.7, 0.95);
        test.insert(point1);
        test.insert(point2);
        test.insert(point3);
        test.insert(point4);
        test.insert(point5);
        test.draw();
        */
    }
}