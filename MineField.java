// Name: Yueqin Li
// USC NetID: yueqinli
// CS 455 PA3
// Fall 2022

import java.util.Random;
/** 
   MineField
      class with locations of mines for a game.
      This class is mutable, because we sometimes need to change it once it's created.
      mutators: populateMineField, resetEmpty
      includes convenience method to tell the number of mines adjacent to a location.
 */
public class MineField {
   
   // <put instance variables here>
   private int numMines;
   private boolean[][] hasMine;

   /**
      Create a minefield with same dimensions as the given array, and populate it with the mines in the array
      such that if mineData[row][col] is true, then hasMine(row,col) will be true and vice versa.  numMines() for
      this minefield will correspond to the number of 'true' values in mineData.
      @param mineData  the data for the mines; must have at least one row and one col,
                       and must be rectangular (i.e., every row is the same length)
    */
   public MineField(boolean[][] mineData) {
      assert isValidMineData(mineData);

      hasMine = new boolean[mineData.length][mineData[0].length];

      for (int row = 0; row < mineData.length; row++) {
         for (int col = 0; col < mineData[0].length; col++) {
            hasMine[row][col] = mineData[row][col];
            if (mineData[row][col]) {
               numMines += 1;
            }
         }
      }
   }

   
   /**
      Create an empty minefield (i.e. no mines anywhere), that may later have numMines mines (once 
      populateMineField is called on this object).  Until populateMineField is called on such a MineField, 
      numMines() will not correspond to the number of mines currently in the MineField.
      @param numRows  number of rows this minefield will have, must be positive
      @param numCols  number of columns this minefield will have, must be positive
      @param numMines   number of mines this minefield will have,  once we populate it.
      PRE: numRows > 0 and numCols > 0 and 0 <= numMines < (1/3 of total number of field locations). 
    */
   public MineField(int numRows, int numCols, int numMines) {
      assert numRows > 0 && numCols > 0 && numMines >= 0 && numMines < (1/3 * numRows() * numCols());

      this.numMines = numMines;
      hasMine = new boolean[numRows][numCols];
   }
   

   /**
      Removes any current mines on the minefield, and puts numMines() mines in random locations on the minefield,
      ensuring that no mine is placed at (row, col).
      @param row the row of the location to avoid placing a mine
      @param col the column of the location to avoid placing a mine
      PRE: inRange(row, col) and numMines() < (1/3 * numRows() * numCols())
    */
   public void populateMineField(int row, int col) {
      assert inRange(row, col) && numMines() < (1/3 * numRows() * numCols());

      resetEmpty();
      Random rand = new Random();
      int i = 0;
      int pickedRow = rand.nextInt(numRows());
      int pickedCol = rand.nextInt(numCols());
      while (i < numMines) {
         // if not mine && not the picked position, set it to mine.
         if (!hasMine(pickedRow, pickedCol) && (pickedRow != row || pickedCol != col)) {
            hasMine[pickedRow][pickedCol] = true;
            i += 1;
         }
         pickedRow = rand.nextInt(numRows());
         pickedCol = rand.nextInt(numCols());
      }
   }
   
   
   /**
      Reset the minefield to all empty squares.  This does not affect numMines(), numRows() or numCols()
      Thus, after this call, the actual number of mines in the minefield does not match numMines().  
      Note: This is the state a minefield created with the three-arg constructor is in 
         at the beginning of a game.
    */
   public void resetEmpty() {
      hasMine = new boolean[this.numRows()][this.numCols()];
   }

   
  /**
     Returns the number of mines adjacent to the specified mine location (not counting a possible 
     mine at (row, col) itself).
     Diagonals are also considered adjacent, so the return value will be in the range [0,8]
     @param row  row of the location to check
     @param col  column of the location to check
     @return  the number of mines adjacent to the square at (row, col)
     PRE: inRange(row, col)
   */
   public int numAdjacentMines(int row, int col) {
      assert inRange(row, col);

      int result = 0;
      for (int i = row - 1; i < row + 2; i++) {
         for (int j = col - 1; j < col + 2; j++) {
            if (inRange(i, j)) {
               if (hasMine(i, j)) {
                  result += 1;
               }
            }
         }
      }
      if (hasMine(row, col)) {
         result -= 1;
      }
      return result;
   }
   
   
   /**
      Returns true iff (row,col) is a valid field location.  Row numbers and column numbers
      start from 0.
      @param row  row of the location to consider
      @param col  column of the location to consider
      @return whether (row, col) is a valid field location
   */
   public boolean inRange(int row, int col) {
      return (row >= 0 && row < hasMine.length) && (col >= 0 && col < hasMine[0].length);
   }
   
   
   /**
      Returns the number of rows in the field.
      @return number of rows in the field
   */  
   public int numRows() {
      return hasMine.length;
   }
   
   
   /**
      Returns the number of columns in the field.
      @return number of columns in the field
   */    
   public int numCols() {
      return hasMine[0].length;
   }
   
   
   /**
      Returns whether there is a mine in this square
      @param row  row of the location to check
      @param col  column of the location to check
      @return whether there is a mine in this square
      PRE: inRange(row, col)   
   */    
   public boolean hasMine(int row, int col) {
      assert inRange(row, col);
      return hasMine[row][col];
   }
   
   
   /**
      Returns the number of mines you can have in this minefield.  For mines created with the 3-arg constructor,
      some of the time this value does not match the actual number of mines currently on the field.  See doc for that
      constructor, resetEmpty, and populateMineField for more details.
    * @return number of mines.
    */
   public int numMines() {
      return numMines;
   }

   
   // <put private methods here>
   //todo: delete
   private static void printMineField(MineField mf) {
      for (int i = 0; i < mf.numRows(); i++) {
         for (int j = 0; j < mf.numCols(); j++) {
            System.out.print(mf.hasMine(i, j) + " ");
         }
         System.out.println();
      }
   }


   /**
    * Check if input is valide minedata.
    * Returns true if parameter is valid minedata, false otherwise.
    * Definition of valid minedata: must have at least one row and one col,
    * and must be rectangular (i.e., every row is the same length)
    *
    * @param minedata the data for the mines.
    * @return Returns true if parameter is valid minedata, false otherwise.
    */
   private boolean isValidMineData(boolean[][] minedata) {
      // not valid minedata if it is null.
      if (minedata == null) {
         return false;
      }

      // not valid minedata if row number is less than one.
      if (minedata.length == 0) {
         return false;
      }

      // not valid minedata if col number is less than one.
      if (minedata.length == 1) {
          if (minedata[0].length == 0) {
             return false;
          }
      }

      // not valid minedata if it is not a rectangular mine.
      int benchmarkColNum = minedata[0].length;
      for (int row = 1; row < minedata.length; row++) {
         if (benchmarkColNum != minedata[row].length) {
            return false;
         }
      }

      return true;
   }
         
}

