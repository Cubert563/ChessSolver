package puzzles.hoppers.model;

import puzzles.common.Coordinates;
import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class HoppersConfig implements Configuration{
    private String[][] grid;
    private int numrows;
    private int numcols;
    private static final String NO_FROG = ".";
    private static final String Water = "*";
    private static final String GREEN_FROG = "G";
    private static final String RED_FROG = "R";

    /**
     * Creates the starting hoppers configuration based on the given file.
     * @param filename The file.
     * @throws IOException
     */
    public HoppersConfig(String filename) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))){
            String[] line1 = in.readLine().split(" ");
            this.numrows = Integer.parseInt(line1[0]);
            this.numcols = Integer.parseInt(line1[1]);
            this.grid = new String[this.numrows][this.numcols];
            for (int row = 0; row < this.numrows; row++) {
                String[] line = in.readLine().split(" ");
                for (int col = 0; col < this.numcols; col++) {
                    if (line[col].equals(".")) {
                        this.grid[row][col] = NO_FROG;
                    }
                    if (line[col].equals("*")) {
                        this.grid[row][col] = Water;
                    }
                    if (line[col].equals("G")) {
                        this.grid[row][col] = GREEN_FROG;
                    }
                    if (line[col].equals("R")) {
                        this.grid[row][col] = RED_FROG;
                    }
                }
            }
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    /**
     * Creates a copy of the current config.
     * @param other The current config.
     * @param startRow The row a frog is jumping from.
     * @param startCol The column a frog is jumping from.
     * @param endRow The row a frog is jumping to.
     * @param endCol The column a frog is jumping to.
     */
    public HoppersConfig(HoppersConfig other, int startRow, int startCol, int endRow,
                          int endCol) {
        this.numrows = other.numrows;
        this.numcols = other.numcols;
        this.grid = new String[numrows][numcols];
        for (int row = 0; row < this.numrows; row++) {
            System.arraycopy(other.grid[row], 0, this.grid[row], 0, this.numcols);
        }
        this.grid[endRow][endCol] = this.grid[startRow][startCol];
        this.grid[startRow][startCol] = NO_FROG;
        this.grid[(startRow + endRow)/2][(startCol + endCol)/2] = NO_FROG;
    }

    /**
     * Gets the current grid.
     * @return The current grid.
     */
    public String[][] getGrid() {
        return this.grid;
    }

    /**
     * Gets the number of rows.
     * @return The number of rows.
     */
    public int getNumrows() {
        return this.numrows;
    }

    /**
     * Gets the number of columns.
     * @return The number of columns.
     */
    public int getNumcols() {
        return this.numcols;
    }

    @Override
    public boolean isSolution() {
        int counter = 0;
        for (int row = 0; row < this.numrows; row++) {
            for (int col = 0; col < this.numcols; col++) {
                if (this.grid[row][col].equals(GREEN_FROG)) {
                    counter += 1;
                }
            }
        }
        return counter == 0;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> neighbors = new ArrayList<>();
        //Set<Coordinates> jumps = new HashSet<>();
        ArrayList<Coordinates> coord = new ArrayList<>();
        int[][] cross_jumps = new int[][] {{2, 2}, {-2, 2}, {-2, -2}, {2, -2}, {4, 0},
                {-4, 0}, {0, 4}, {0, -4}};
        for (int row = 0; row < this.numrows; row++) {
            for (int col = 0; col < this.numcols; col++) {
                Coordinates point = new Coordinates(row, col);
                for (int[] c : cross_jumps) {
                    Coordinates jump = new Coordinates(point.row() + c[0], point.col()
                            + c[1]);
                    if (jump.row() > 0 && jump.col() > 0 && jump.row()
                            < this.numrows && jump.col() < this.numcols &&
                            this.grid[(jump.row() + point.row())/2][(jump.col() +
                                    point.col())/2].equals(GREEN_FROG)&&
                            this.grid[jump.row()][jump.col()].equals(NO_FROG)) {
                        coord.add(jump);
                    }
                }
                for (int l = 0; l < coord.size(); l++) {
                    if (this.grid[row][col].equals(GREEN_FROG) ||
                            this.grid[row][col].equals(RED_FROG)) {
                        HoppersConfig newConfig = new HoppersConfig(this, row, col,
                                coord.get(l).row(), coord.get(l).col());
                        neighbors.add(newConfig);
                    }
                }
            }
        }
        return neighbors;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int row = 0; row < this.numrows; row++) {
            //result.append(row);
            for (int col = 0; col < this.numcols; col++) {
                //string += (this.grid[row][col]);
                if (col != this.numcols - 1) {
                    result.append(this.grid[row][col]).append(" ");

                }
                else {
                    result.append(this.grid[row][col]).append(System.lineSeparator());

                }
            }
        }
        return result.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        HoppersConfig config = (HoppersConfig) other;
        for (int row = 0; row < this.numrows; row++) {
            for (int col = 0; col < this.numcols; col++) {
                if (!this.grid[row][col].equals(config.grid[row][col])) {
                    return false;
                }
            }
        }
        return (this.numrows == config.numrows && this.numcols == config.numcols);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.grid);
    }
}
