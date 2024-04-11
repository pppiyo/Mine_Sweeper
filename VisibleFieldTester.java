import static org.junit.Assert.*;
import org.junit.Test;

public class VisibleFieldTester {
   private static boolean[][] smallMineField =
      {{false, false, false, false},
       {true, false, false, false},
       {false, true, true, false},
       {false, true, false, true}}; // smf: 5 mines

   private static boolean[][] emptyMineField =
      {{false, false, false, false},
       {false, false, false, false},
       {false, false, false, false},
       {false, false, false, false}}; // 0 mines

   private static boolean[][] almostEmptyMineField =
      {{false, false, false, false},
       {false, false, false, false},
       {false, false, false, false},
       {false, true, false, false}}; // 1 mine

   private static boolean[][] smallestMineField =
      {{false, false},
       {false, false}};

   @Test
   /**
      Create a visible field that has the given underlying mineField.
      The initial state will have all the locations covered, no mines guessed, and the game
      not over.
      @param mineField  the minefield to use for this VisibleField
    */
   public void testConstructor() {
      MineField smf = new MineField(smallMineField);
      VisibleField vf = new VisibleField(smf);
      int minGuessed = vf.numMinesLeft();
      assertEquals(5, minGuessed);
      boolean gameOver = vf.isGameOver();
      assertEquals(false, gameOver);
   }

   @Test
   /**
      Reset the object to its initial state (see constructor comments), using the same underlying
      MineField.
   */
   public void testResetGameDisplay() {
      MineField smf = new MineField(smallMineField);
      VisibleField vf = new VisibleField(smf);
   }

   @Test
   /**
      Returns the visible status of the square indicated.
      @param row  row of the square
      @param col  col of the square
      @return the status of the square at location (row, col).  See the public constants at the beginning of the class
      for the possible values that may be returned, and their meanings.
      PRE: getMineField().inRange(row, col)
    */
   public void testGetStatus() {

   }

   @Test
   /**
      Returns the number of mines left to guess.  This has nothing to do with whether the mines guessed are correct
      or not.  Just gives the user an indication of how many more mines the user might want to guess.  This value will
      be negative if they have guessed more than the number of mines in the minefield.
      @return the number of mines left to guess.
    */
   public void testNumMinesLeft() {

   }

   @Test
   public void testUncover() {

   }

   @Test
   public void testIsGameOver() {

   }

   public static void main(String[] args) {}


}
