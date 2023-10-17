package puzzles.hoppers.model;

import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private HoppersConfig currentConfig;

    private final HoppersConfig startConfig;
    private int startRow;
    private int startCol;
    private int endRow;
    private int endCol;

    private String filename;

    private int counter;


    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }


    /**
     * Creates the hoppers model based on the given file.
     * @param filename The file.
     * @throws IOException
     */
    public HoppersModel(String filename) throws IOException {
        this.startConfig = new HoppersConfig(filename);
        this.currentConfig = startConfig;
        this.counter = 0;
    }

    /**
     * Creates a possible jump for the user.
     */
    public void hint() {
        if (this.currentConfig.isSolution()) {
            this.alertObservers("Game already solved");
        }
        else {
            Solver solver = new Solver();
            List<Configuration> con = solver.solverBFS(this.currentConfig);
            this.currentConfig = (HoppersConfig) con.get(0);
            con.remove(0);
            this.alertObservers("Next Step!");
        }
    }

    /**
     * Loads a new file to use.
     * @param filename The file.
     */
    public void load(String filename) {
        try {
            this.filename = filename;
            this.currentConfig = new HoppersConfig(filename);
            this.alertObservers("Loaded: " + filename);
            System.out.println(this);
        } catch (IOException e) {
            this.alertObservers("Failed to Load: " + e.getMessage());
        }
    }

    /**
     * Checks if the current space is on the grid/
     * @param row The row
     * @param col The column
     * @return True if the space is part of the grid and false otherwise.
     */
    public boolean Bound(int row, int col){
        int bordrow= currentConfig.getNumrows();
        int bordrcol= currentConfig.getNumcols();
        if (row >= 0 && row<bordrow && col >=0 && col<bordrcol ){
            return true;
        }
        return false;
    }

    /**
     * Checks if the jump is possible or not.
     * @param srow The row you jump to.
     * @param scol The comumn you jump to.
     * @param frow The row you jump from.
     * @param fcol The column you jump from.
     * @return True if the jump is possible and false otherwise.
     */
    public boolean possiblejump(int srow, int scol, int frow, int fcol) {
        if (Math.abs(frow - srow) == Math.abs(fcol - scol)) {
            //System.out.println("purple");
            if (Math.abs(frow - srow) != 2 && Math.abs(fcol - scol) != 2) {
                return false;
            }
        }
        if (Math.abs(frow - srow) != Math.abs(fcol - scol)) {
            if (Math.abs(frow - srow) != 4 && Math.abs(fcol - scol) != 4) {
                return false;
            }
        }
        return true;
    }

    /**
     * Selects a frog to move or a lily pad to move to;
     * @param row The row
     * @param col The column
     */
    public void select(int row, int col) {
        String[][] grid = this.currentConfig.getGrid();
        if (counter % 2 == 0) {
            if (Bound(row, col) && ((grid[row][col].equals("G") || grid[row][col].equals("R")))) {
                this.startRow = row;
                this.startCol = col;
                counter += 1;
                this.alertObservers("Selected: (" + startRow + ", " + startCol + ")");
            } else {
                this.alertObservers("Cannot select (" + startRow + ", " + startCol + ")");
            }
        }else {
            if (grid[row][col].equals(".") && possiblejump(startRow, startCol, row, col)) {
                String toBeEater = grid[(row + startRow) / 2][(col + startCol) / 2];
                if (toBeEater.equals("G") || toBeEater.equals("R")) {
                    String toEat = grid[startRow][startCol];
                    if (toEat.equals("G") && toBeEater.equals("R")) {
                        this.alertObservers("Cannot eat a red frog");
                    } else {
                        currentConfig = new HoppersConfig(currentConfig, startRow, startCol, endRow, endCol);
                        this.alertObservers("Jumped from (" + startRow + ", " + endRow + ") to (" + endRow + ", " +
                                endCol + ")");
                        counter += 1;
                    }
                } else {
                    alertObservers("Cannot select (" + startRow + ", " + startCol + ")");
                }
            } else {
                this.alertObservers("Cannot select (" + startRow + ", " + startCol + ")");
            }
        }
    }

    /**
     * Resets the current puzzle.
     */
    public void reset() {
        this.load(filename);
        this.currentConfig = this.startConfig;
        this.alertObservers("Puzzle Reset!");
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("   ");
        String HORI_WALL = "-";
        String VERT_WALL = "|";
        for (int col = 0; col < currentConfig.getNumcols(); col++) {
            result.append(col).append(" ");
        }
        result.append(System.lineSeparator()).append("  ");
        result.append(HORI_WALL.repeat(Math.max(0, currentConfig.getNumcols()*2-1)));
        result.append(System.lineSeparator());
        for (int row = 0; row < currentConfig.getNumrows(); row++) {
            result.append(row).append(VERT_WALL).append(" ");
            for (int col = 0; col < currentConfig.getNumcols(); col++) {
                if (col != currentConfig.getNumrows() - 1) {
                    result.append(this.currentConfig.getGrid()[row][col]).append(" ");

                }
                else {
                    result.append(this.currentConfig.getGrid()[row][col]).
                            append(System.lineSeparator());

                }
            }
        }
        return result.toString();
    }

    /**
     * Gets the name of the file.
     * @return The name of the file.
     */
    public String getFilename() {
        return this.filename;
    }

    /**
     * Gets the grid of the current configuration.
     * @return The grid of the current configuration.
     */
    public String[][] getConfigArray() {
        return this.currentConfig.getGrid();
    }

    /**
     * Gets the number of rows in the grid.
     * @return The number of rows.
     */
    public int getnumRows() {
        return this.currentConfig.getNumcols();
    }

    /**
     * Gets the number of columns in the grid.
     * @return The number of columns.
     */
    public int getNumCols() {
        return this.currentConfig.getNumcols();
    }
}
