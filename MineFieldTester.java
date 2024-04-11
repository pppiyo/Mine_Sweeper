import static org.junit.Assert.*;
import org.junit.Test;

public class MineFieldTester {
   private static boolean[][] smallMineField =
      {{false, false, false, false},
       {true, false, false, false},
       {false, true, true, false},
       {false, true, false, true}};

   private static boolean[][] emptyMineField =
      {{false, false, false, false},
       {false, false, false, false},
       {false, false, false, false},
       {false, false, false, false}};

   private static boolean[][] almostEmptyMineField =
      {{false, false, false, false},
       {false, false, false, false},
       {false, false, false, false},
       {false, true, false, false}};

   private static boolean[][] oneMineField =
      {{false}};

   private static boolean[][] mfs1 =
      {{false}};

   private static boolean[][] mfs2 =
      {{false, false, false, false, true}};

   private static boolean[][] mfs3 =
      {{false}, {false}, {false}, {false}, {true}};

   private static boolean[][] mfs4 =
      {{true, true, true, true},
       {true, true, true, true},
       {true, true, true, true},
       {true, true, true, true}};

   @Test
   /**
     Returns the number of mines adjacent to the specified mine location (not counting a possible
     mine at (row, col) itself).
     Diagonals are also considered adjacent, so the return value will be in the range [0,8]
     @param row  row of the location to check
     @param col  column of the location to check
     @return  the number of mines adjacent to the square at (row, col)
     PRE: inRange(row, col)
   */
   public void testNumAdjacentMines() {
      MineField aemf = new MineField(almostEmptyMineField);
      assertEquals(0, aemf.numAdjacentMines(0, 0));
      assertEquals(0, aemf.numAdjacentMines(0, 1));
      assertEquals(0, aemf.numAdjacentMines(0, 2));
      assertEquals(0, aemf.numAdjacentMines(0, 3));

      assertEquals(0, aemf.numAdjacentMines(1, 0));
      assertEquals(0, aemf.numAdjacentMines(1, 1));
      assertEquals(0, aemf.numAdjacentMines(1, 2));
      assertEquals(0, aemf.numAdjacentMines(1, 3));

      assertEquals(1, aemf.numAdjacentMines(2, 0));
      assertEquals(1, aemf.numAdjacentMines(2, 1));
      assertEquals(1, aemf.numAdjacentMines(2, 2));
      assertEquals(0, aemf.numAdjacentMines(2, 3));

      assertEquals(1, aemf.numAdjacentMines(3 , 0));
      assertEquals(0, aemf.numAdjacentMines(3, 1));
      assertEquals(1, aemf.numAdjacentMines(3 , 2));
      assertEquals(0, aemf.numAdjacentMines(3 , 3));
   }

   public void printNumAdjacentMines(MineField mf) {
      for (int i = 0; i < mf.numRows(); i++) {
         for (int j = 0; j < mf.numCols(); j++) {
            System.out.print(mf.numAdjacentMines(i, j) + " ");
         }
         System.out.println();
      }
      System.out.println();
   }

   @Test
   public void testNumAdjacentMinesByJux() {
      MineField mf = new MineField(mfs4);
//      MineField mf = new MineField(mfs3);
//      MineField mf = new MineField(mfs2);
//      MineField mf = new MineField(mfs1);
      printMineField(mf);
      printNumAdjacentMines(mf);
   }


   public static void main(String[] args) {}

   @Test
   public void test3argsConstr() {
      MineField mf = new MineField(4, 4, 3);
      assertEquals(3, mf.numMines());
      printMineField(mf);
   }

   @Test
   // numMines() < (1/3 * numRows() * numCols())
   public void testPopulateMineField() {
      testPopulateMineField(4, 4, 3);
      testPopulateMineField(1, 1, 0);
      testPopulateMineField(2, 4, 2);
      testPopulateMineField(1, 4, 1);
      testPopulateMineField(10, 1, 3);
   }

   private void testPopulateMineField(int numRows, int numCols, int numMines) {
      MineField mf = new MineField(numRows, numCols, numMines);
      System.out.println("before populating: ");
      printMineField(mf);
      mf.populateMineField(0, 0);
      System.out.println("after populating: " + numMines + " true(s)");
      int mines = printMineField(mf);
      assertEquals(numMines, mines);
      assertEquals(false, mf.hasMine(0, 0));
   }

   // print the mindfield && return the number of mines in the field.
   private static int printMineField(MineField mf) {
      int cnt = 0;
      for (int i = 0; i < mf.numRows(); i++) {
         for (int j = 0; j < mf.numCols(); j++) {
            if (mf.hasMine(i, j)) {
               cnt += 1;
            }
            System.out.print(mf.hasMine(i, j) + " ");
         }
         System.out.println();
      }
      System.out.println();
      return cnt;
   }

   @Test
   /**
      Reset the minefield to all empty squares.  This does not affect numMines(), numRows() or numCols()
      Thus, after this call, the actual number of mines in the minefield does not match numMines().
      Note: This is the state a minefield created with the three-arg constructor is in
         at the beginning of a game.
    */
   public void testResetEmpty() {
      MineField mf = new MineField(smallMineField);
      printMineField(mf);
      mf.resetEmpty();
      printMineField(mf);
      assertEquals(5, mf.numMines());

      MineField mf1 = new MineField(oneMineField);
      printMineField(mf1);
      mf1.resetEmpty();
      printMineField(mf1);
      assertEquals(0, mf1.numMines());
   }


   /*
      return the number of true in the given boolean array.
    */
   private int numTrues(boolean[][] arr) {
      int cntTrue = 0;
      for (int i = 0; i < arr.length; i++) {
         for (int j = 0; j< arr.length; j++) {
            if (arr[i][j]) {
               cntTrue += 1;
            }
         }
      }
      return cntTrue;
   }
}

/**
   @Test
   public void testSmf() {
      MineField smf = new MineField(smallMineField);
      boolean[][] expected = {{false, false, false, false},
            {true, false, false, false},
            {false, true, true, false},
            {false, true, false, true}};
      boolean[][] actual = smf.getHasMine();
      assertEquals(expected, actual);
      assertEquals(5, smf.numMines());
   }

   @Test
   public void testEmf() {
      MineField emf = new MineField(emptyMineField);
      boolean[][] expected = new boolean[][]{{false, false, false, false},
            {false, false, false, false},
            {false, false, false, false},
            {false, false, false, false}};
      boolean[][] actual = emf.getHasMine();
      assertEquals(expected, actual);
      assertEquals(0, emf.numMines());
   }

   @Test
   public void testAemf() {
      MineField aemf = new MineField(almostEmptyMineField);
      boolean[][] expected = new boolean[][] {{false, false, false, false},
       {false, false, false, false},
       {false, false, false, false},
       {false, true, false, false}};
      boolean[][] actual = aemf.getHasMine();
      assertEquals(expected, actual);
      assertEquals(1, aemf.numMines());
   }
   **/
