/******************************************************************************
 *  Compilation:  javac Solver.java
 *  Execution:    java Solver filename.txt...
 *  Dependencies: Board.java
 *
 ******************************************************************************/
import java.util.Comparator;
import java.util.Iterator;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
// import edu.princeton.cs.algs4.In;
// import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private int takenMoves;
    private SearchNode answerLastNode;
    private static final Comparator<SearchNode> BY_MANHATTAN= new priorityComp();
    
    //this is manhattan priority version
    private static class priorityComp implements Comparator<SearchNode> {
        public int compare(SearchNode sn1, SearchNode sn2) {
            // Manhattan priority = Manhattan distance + moves made so far to get to the search node
            int mpriority1 = sn1.board.manhattan() + sn1.moves;
            int mpriority2 = sn2.board.manhattan() + sn2.moves;
            if (mpriority1 < mpriority2)
                return -1;
            else if (mpriority1 == mpriority2)
                return 0;
            else
                return 1;
        }
    }
    // data type SearchNode
    private class SearchNode {
        public Board board;
        public int moves;
        public SearchNode prev;        
        // Constructor
        public SearchNode(Board initBoard) {
            this.board = initBoard;
            this.moves = 0;
            this.prev = null;
        }
        public SearchNode(Board initBoard, int moves, SearchNode prevSearchNode) {
            
            this.board = initBoard;
            this.moves = moves;
            this.prev = prevSearchNode;
        }  
    }
    
    private void insertNeighborsNodes(MinPQ<SearchNode> searchNodesPQ, SearchNode curSearchNode){
          //Iterable<Board> iterable = curSearchNode.board.neighbors();
  //Iterator<Board> iterator = iterable.iterator();
        Iterator<Board> iterator = curSearchNode.board.neighbors().iterator();
        while (iterator.hasNext()){
            SearchNode newSearchNode = new SearchNode(iterator.next(), curSearchNode.moves + 1, curSearchNode);
            // if this is this is the first time, searchNodesPQ must only had a initNode(which prev=n=ull) and is empty now 
            if (curSearchNode.prev == null)
                searchNodesPQ.insert(newSearchNode);
            else if (!newSearchNode.board.equals(curSearchNode.prev.board)) // if has previous node
                searchNodesPQ.insert(newSearchNode);
        }
    }
    
    public Solver(Board initial) {           // find a solution to the initial board (using the A* algorithm)
        if (initial == null)
            throw new java.lang.NullPointerException();
        this.takenMoves = -1;
        this.answerLastNode = null;
        
        Board twin = initial.twin();
        // initial capacit = 1, use user-defined comparator
        MinPQ<SearchNode> searchNodesPQ = new MinPQ<SearchNode>(1, Solver.BY_MANHATTAN);
        searchNodesPQ.insert(new SearchNode(initial));        
        MinPQ<SearchNode> twinSearchNodesPQ = new MinPQ<SearchNode>(1, Solver.BY_MANHATTAN);
        twinSearchNodesPQ.insert(new SearchNode(twin));
        
        // start searching, if twin() is goal, means
        while (!searchNodesPQ.isEmpty() && !twinSearchNodesPQ.isEmpty()) {
            SearchNode minSearchNode = searchNodesPQ.delMin();
            SearchNode twinMinSearchNode = twinSearchNodesPQ.delMin();
            if (minSearchNode.board.isGoal()) {
                this.takenMoves = minSearchNode.moves;
                this.answerLastNode = minSearchNode;
                return;
            }
            else if (twinMinSearchNode.board.isGoal()) {
                this.takenMoves = -1;
                this.answerLastNode = null;
                return;
            }
            else {
                insertNeighborsNodes(searchNodesPQ, minSearchNode);
                insertNeighborsNodes(twinSearchNodesPQ, twinMinSearchNode); 
            }
        }
    }
    
    public boolean isSolvable() {            // is the initial board solvable?
        return (takenMoves != -1);
    }
    public int moves() {                    // min number of moves to solve initial board; -1 if unsolvable
        return takenMoves;
    }
    
    public Iterable<Board> solution() {      // sequence of boards in a shortest solution; null if unsolvable        
        if (!isSolvable())
            return null;
        return new SolutionIterable();
    }
    
    private class SolutionIterable implements Iterable<Board> {
        public Iterator<Board> iterator() {
            return new SolutionIterator();
        }        
    }
    
    private class SolutionIterator implements Iterator<Board> {
        private Stack<Board> solutionSequence;
        
        private SolutionIterator() {
            solutionSequence = new Stack<Board>();
            SearchNode curSN = answerLastNode;
            while (curSN != null) {
                solutionSequence.push(curSN.board);
                //trace back
                curSN = curSN.prev;
            }
        }
        
        public boolean hasNext() {
            return (!solutionSequence.isEmpty());
        }
        
        public Board next() {
            return solutionSequence.pop();
        }
        
        public void remove() {
        }
    }
    
    public static void main(String[] args) {
        /*
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
        */
    }
}