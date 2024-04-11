// Name: Yueqin Li
// USC NetID: yueqinli
// CS 455 PA3
// Fall 2022


import java.util.Arrays;

/**
  VisibleField class
  This is the data that's being displayed at any one point in the game (i.e., visible field, because it's what the
  user can see about the minefield). Client can call getStatus(row, col) for any square.
  It actually has data about the whole current state of the game, including  
  the underlying mine field (getMineField()).  Other accessors related to game status: numMinesLeft(), isGameOver().
  It also has mutators related to actions the player could do (resetGameDisplay(), cycleGuess(), uncover()),
  and changes the game state accordingly.
  
  It, along with the MineField (accessible in mineField instance variable), forms
  the Model for the game application, whereas GameBoardPanel is the View and Controller in the MVC design pattern.
  It contains the MineField that it's partially displaying.  That MineField can be accessed (or modified) from 
  outside this class via the getMineField accessor.  
 */
public class VisibleField {
   // ----------------------------------------------------------   
   // The following public constants (plus numbers mentioned in comments below) are the possible states of one
   // location (a "square") in the visible field (all are values that can be returned by public method 
   // getStatus(row, col)).
   
   // The following are the covered states (all negative values):
   public static final int COVERED = -1;   // initial value of all squares
   public static final int MINE_GUESS = -2;
   public static final int QUESTION = -3;

   // The following are the uncovered states (all non-negative values):
   
   // values in the range [0,8] corresponds to number of mines adjacent to this opened square
   
   public static final int MINE = 9;      // this loc is a mine that hasn't been guessed already (end of losing game)
   public static final int INCORRECT_GUESS = 10;  // is displayed a specific way at the end of losing game
   public static final int EXPLODED_MINE = 11;   // the one you uncovered by mistake (that caused you to lose)
   // ----------------------------------------------------------   
  
   // <put instance variables here>
   private MineField mineField;
   private int numMinesLeft;
   private int[][] status;
   private boolean isGameOver;
   private int uncoveredNonMine;
   private static final int ZERO = 0;



   /**
      Create a visible field that has the given underlying mineField.
      The initial state will have all the locations covered, no mines guessed, and the game
      not over.
      @param mineField  the minefield to use for this VisibleField
    */
   public VisibleField(MineField mineField) {
      this.mineField = mineField;
      numMinesLeft = mineField.numMines();
      status = new int[mineField.numRows()][mineField.numCols()];
      status = fill2DArray(status, COVERED);
      isGameOver = false;
      uncoveredNonMine = mineField.numCols() * mineField.numRows() - mineField.numMines();
   }


   /**
      Reset the object to its initial state (see constructor comments), using the same underlying
      MineField.
   */
   public void resetGameDisplay() {
      numMinesLeft = mineField.numMines();
      status = new int[mineField.numRows()][mineField.numCols()];
      status = fill2DArray(status, COVERED);
      isGameOver = false;
      uncoveredNonMine = mineField.numCols() * mineField.numRows() - mineField.numMines();
   }
  
   
   /**
      Returns a reference to the mineField that this VisibleField "covers"
      @return the minefield
    */
   public MineField getMineField() {
      return mineField;
   }
   
   
   /**
      Returns the visible status of the square indicated.
      @param row  row of the square
      @param col  col of the square
      @return the status of the square at location (row, col).  See the public constants at the beginning of the class
      for the possible values that may be returned, and their meanings.
      PRE: getMineField().inRange(row, col)
    */
   public int getStatus(int row, int col) {
      assert getMineField().inRange(row, col);
      return status[row][col];
   }

   
   /**
      Returns the number of mines left to guess.  This has nothing to do with whether the mines guessed are correct
      or not.  Just gives the user an indication of how many more mines the user might want to guess.  This value will
      be negative if they have guessed more than the number of mines in the minefield.     
      @return the number of mines left to guess.
    */
   public int numMinesLeft() {
      return numMinesLeft;
   }
 
   
   /**
      Cycles through covered states for a square, updating number of guesses as necessary.  Call on a COVERED square
      changes its status to MINE_GUESS; call on a MINE_GUESS square changes it to QUESTION;  call on a QUESTION square
      changes it to COVERED again; call on an uncovered square has no effect.  
      @param row  row of the square
      @param col  col of the square
      PRE: getMineField().inRange(row, col)
    */
   public void cycleGuess(int row, int col) {
      assert getMineField().inRange(row, col);

      if (status[row][col] == COVERED) {
         status[row][col] = MINE_GUESS;
         numMinesLeft -= 1;
      }
      else if (status[row][col] == MINE_GUESS) {
         status[row][col] = QUESTION;
         numMinesLeft += 1;
      }
      else if (status[row][col] == QUESTION){
         status[row][col] = COVERED;
      }
      else {
         return;
      }
   }

   
   /**
      Uncovers this square and returns false iff you uncover a mine here.
      If the square wasn't a mine or adjacent to a mine it also uncovers all the squares in 
      the neighboring area that are also not next to any mines, possibly uncovering a large region.
      Any mine-adjacent squares you reach will also be uncovered, and form 
      (possibly along with parts of the edge of the whole field) the boundary of this region.
      Does not uncover, or keep searching through, squares that have the status MINE_GUESS. 
      Note: this action may cause the game to end: either in a win (opened all the non-mine squares)
      or a loss (opened a mine).
      @param row  of the square
      @param col  of the square
      @return false   iff you uncover a mine at (row, col)
      PRE: getMineField().inRange(row, col)
    */
   public boolean uncover(int row, int col) {
      assert getMineField().inRange(row, col);

      if (mineField.hasMine(row, col)) {
         status[row][col] = EXPLODED_MINE;
         isGameOver = true;
         updateLosingDetails();
         return false;
      }

      recurUncoverNoneMines(row, col);

      if (uncoveredNonMine == ZERO) {
         isGameOver = true;
      }
      return true;
   }

   /**
      Recursively uncovers the square:
      If the square wasn't a mine or adjacent to a mine it also uncovers all the squares in
      the neighboring area that are also not next to any mines, possibly uncovering a large region.
      Any mine-adjacent squares you reach will also be uncovered, and form
      (possibly along with parts of the edge of the whole field) the boundary of this region.
      Does not uncover, or keep searching through, squares that have the status MINE_GUESS.
      Note: this action may cause the game to end: either in a win (opened all the non-mine squares)
      or a loss (opened a mine).
      @param row  of the square
      @param col  of the square
      PRE: getMineField().inRange(row, col)
    */
   private void recurUncoverNoneMines(int row, int col) {
      assert getMineField().inRange(row, col);

      // if not in valid range, return
      if (!mineField.inRange(row, col)) {
         return;
      }
      // if already uncovered, return
      if (isUncovered(row, col)) {
         return;
      }
      // if is MINE_GUESS, return
      if (status[row][col] == MINE_GUESS) {
         return;
      }
      // if number of ajacent mines is in range (0, 8], open it (change status
      // to the corresponding numAdjacentMines.
      if (mineField.numAdjacentMines(row, col) > ZERO &&
            mineField.numAdjacentMines(row, col) < MINE) {
         status[row][col] = mineField.numAdjacentMines(row, col);
         uncoveredNonMine -= 1;
      }
      // If the square wasn't a mine or adjacent to a mine it also uncovers all the squares in
      // the neighboring area that are also not next to any mines, possibly
      // uncovering a large region.
      if (mineField.numAdjacentMines(row, col) == 0) {
         status[row][col] = ZERO;
         uncoveredNonMine -= 1;

         recurUncoverNoneMines(row - 1, col - 1);
         recurUncoverNoneMines(row - 1, col);
         recurUncoverNoneMines(row - 1, col + 1);
         recurUncoverNoneMines(row, col - 1);
         recurUncoverNoneMines(row, col + 1);
         recurUncoverNoneMines(row + 1, col - 1);
         recurUncoverNoneMines(row + 1, col);
         recurUncoverNoneMines(row + 1, col + 1);
      }

   }
   
   /**
      Returns whether the game is over.
      (Note: This is not a mutator.)
      @return whether game has ended
    */
   public boolean isGameOver() {
      return isGameOver;
   }
 
   
   /**
      Returns whether this square has been uncovered.  (i.e., is in any one of the uncovered states, 
      vs. any one of the covered states).
      @param row of the square
      @param col of the square
      @return whether the square is uncovered
      PRE: getMineField().inRange(row, col)
    */
   public boolean isUncovered(int row, int col) {
      assert getMineField().inRange(row, col);
      return getStatus(row, col) > COVERED;
   }
   
 
   // <put private methods here>
   /**
    * Fill a 2D array with a given integer.
    * @param arr the 2D integer array to be filled in.
    * @param n the integer to fill in the 2D array
    * @return the 2D array filled with n.
    */
   private static int[][] fill2DArray(int[][] arr, int n) {
      for (int[] row : arr) {
         Arrays.fill(row, n);
      }
      return arr;
   }


   /**
    * Upon game lose, keep correct MINE_GUESS state,
    * turn other undiscovered mines' status to MINE,
    * turn incorrect MINE_GUESS's status to MINE_GUESS.
    */
   private void updateLosingDetails() {
      for (int row = 0; row < mineField.numRows(); row++) {
         for (int col = 0; col < mineField.numCols(); col++) {
            if (mineField.hasMine(row, col) && status[row][col] != EXPLODED_MINE) {
               if (status[row][col] != MINE_GUESS) {
                  status[row][col] = MINE;
               }
            }
            else if (status[row][col] == MINE_GUESS) {
               status[row][col] = INCORRECT_GUESS;
            }
         }
      }
   }


   // todo: delete
   public void printMineFieldStatus() {
      for (int row = 0; row < mineField.numRows(); row++) {
         for (int col = 0; col < mineField.numCols(); col++) {
            System.out.print(status[row][col] + " ");
         }
         System.out.println();
      }
   }

   // todo: delete
   /**
    * Iff it is already uncovered, do nothing.
    * Iff it is MINE_GUESS, do nothing.
    * Iff it is QUESTION or COVERED, uncover it.
    */
   private void uncoverNoneMines(int row, int col) {
      if (isUncovered(row, col)) {
         return;
      }

      if (status[row][col] == MINE_GUESS) {
         return;
      }

      status[row][col] = mineField.numAdjacentMines(row,
            col);
      uncoveredNonMine -= 1;
   }
}
