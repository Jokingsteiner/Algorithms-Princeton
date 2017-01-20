import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Queue;

public class Board {
    private int N;
    private int board[][];
    private int emptySquareRow;
    private int emptySquareCol;
    public Board(int[][] blocks) {           // construct a board from an n-by-n array of blocks
                                             // (where blocks[i][j] = block in row i, column j)
        N = blocks.length;
        board = new int[blocks.length][blocks.length];
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                board[i][j] = blocks[i][j];
                if (blocks[i][j] == 0){
                    emptySquareRow = i;
                    emptySquareCol = j;
                }
            }
        }
    }
     
    public int dimension() {                // board dimension n
        return N;
    }
    
    public int hamming() {                  // number of blocks out of place
        int numOfWrongBlock = 0;
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                // including last slop, either equal to (blank) or not, since board[i][j] never be N * N + 1
                if (board[i][j] != 0 && board[i][j] != i * N + j + 1){
                    numOfWrongBlock++;
                }
            }
        }
        return numOfWrongBlock;
    }
    
    public int manhattan() {                 // sum of Manhattan distances between blocks and goal
        int distanceToGoal = 0;
        int correctRow, correctCol;
        for (int row = 0; row < N; row++){
            for (int col = 0; col < N; col++){
                if (board[row][col] == 0)
                    continue;
                correctRow = (board[row][col] - 1) / N;
                correctCol = (board[row][col] - 1) % N;
                distanceToGoal += Math.abs(correctRow - row) + Math.abs(correctCol - col);
            }
        }
        return distanceToGoal;
    }
    public boolean isGoal() {               // is this board the goal board?
        return (hamming() == 0);
    }
    public Board twin() {                    // a board that is obtained by exchanging any pair of blocks
        Board twin = new Board(this.board);
        boolean exchange = false;
        while (!exchange) {
            int i = StdRandom.uniform(N);
            int j = StdRandom.uniform(N);
            int jTOex;
            if (j == N - 1) {
                jTOex = j - 1;
            }
            else {
                jTOex = j + 1;
            }
            if (board[i][j] != 0 && board[i][jTOex] != 0) {
                int temp = twin.board[i][j];
                twin.board[i][j] = board[i][jTOex];
                twin.board[i][jTOex] = temp;
                exchange = true;
            }
        }
        return twin;
    }
    public boolean equals(Object y) {        // does this board equal y?
        if (y == this)
            return true;
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;
        Board that = (Board) y;
        if (this.N != that.N)
            return false;
        for (int i = 0; i < N; ++i){
            for (int j = 0; j < N; ++j) {
                if (this.board[i][j] != that.board[i][j])
                    return false;
            }
        }
        return true;
    }

    private class NeighborIterable implements Iterable<Board> { //class is Iterable, for neighbors() to return
        // this is a iterator
        public Iterator<Board> iterator() {
            NeighborIterator iter = new NeighborIterator();
            return iter;
        }
    }
    
    public Iterable<Board> neighbors() {
        Iterable<Board> iter = new NeighborIterable();
        return iter;
    }
    private class NeighborIterator implements Iterator<Board> {
        private Queue<Board> neighbors;
        public NeighborIterator() {
            neighbors = new Queue<Board>();
            if (emptySquareRow > 0) {
                Board leftNeighbor = new Board(board);
                leftNeighbor.board[emptySquareRow][emptySquareCol] = leftNeighbor.board[emptySquareRow - 1][emptySquareCol];
                leftNeighbor.board[emptySquareRow - 1][emptySquareCol] = 0;
                --leftNeighbor.emptySquareRow;
                neighbors.enqueue(leftNeighbor);
            }
            if (emptySquareRow < N - 1) {
                Board rightNeighbor = new Board(board);
                rightNeighbor.board[emptySquareRow][emptySquareCol] = rightNeighbor.board[emptySquareRow + 1][emptySquareCol];
                rightNeighbor.board[emptySquareRow + 1][emptySquareCol] = 0;
                ++rightNeighbor.emptySquareRow;
                neighbors.enqueue(rightNeighbor);
            }
            if (emptySquareCol > 0) {
                Board upperNeighbor = new Board(board);
                upperNeighbor.board[emptySquareRow][emptySquareCol] = upperNeighbor.board[emptySquareRow][emptySquareCol - 1];
                upperNeighbor.board[emptySquareRow][emptySquareCol - 1] = 0;
                --upperNeighbor.emptySquareCol;
                neighbors.enqueue(upperNeighbor);
            }
            if (emptySquareCol < N - 1) {
                Board downNeighbor = new Board(board);
                downNeighbor.board[emptySquareRow][emptySquareCol] = downNeighbor.board[emptySquareRow][emptySquareCol + 1];
                downNeighbor.board[emptySquareRow][emptySquareCol + 1] = 0;
                ++downNeighbor.emptySquareCol;
                neighbors.enqueue(downNeighbor);
            }
        }
        public boolean hasNext() {
            return (neighbors.isEmpty() == false);
        }
        
        public Board next() {
            return neighbors.dequeue();
        }
        
        public void remove() {
            
        }
    }
    
    public String toString() {               // string representation of this board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) { // unit tests (not graded)
    }
}