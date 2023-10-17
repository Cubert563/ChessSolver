package puzzles.chess.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
/**
 * Commmunicates with the PTUI and GUI, telling it what to do
 * Author: Jack Werremeyer
 * */
public class ChessModel {
    /** the collection of observers of this model */
    private final List<Observer<ChessModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private ChessConfig currentConfig;

    /**the string name of the current file*/
    private String filename;

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<ChessModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    /**constructor*/
    public ChessModel(String filename) throws IOException {
        this.filename = filename;
        load(filename);
    }

    /**the load command*/
    public void load(String filename){
        try{
            this.currentConfig = new ChessConfig(filename);
            alertObservers("Loaded: " + filename);
        } catch(IOException ioe){
            alertObservers("Failed to load: " + filename);

        }
    }

    /**hint command*/
    public void hint(){
        Solver solver = new Solver();
        List<Configuration> answer = solver.solverBFS(currentConfig);
        String message = "";
        if(answer.size() == 1 || answer.size() == 0){
            message = "No Solution";
        }else{
            currentConfig = (ChessConfig) answer.get(1);
            message = "Next Step";
        }
        this.alertObservers(message);
    }


    /**checks if the select is valid*/
    public boolean validSelect(String row, String col, int moveNum){
        if(moveNum == 0) {
            if (currentConfig.isValid(Integer.parseInt(row), Integer.parseInt(col))) {
                alertObservers("Selected (" + row + "," + col + ")");
                return true;
            } else {
                alertObservers("Invalid Selection (" + row + "," + col + ")");
                return false;
            }
        }
        else{
            if (currentConfig.isValid(Integer.parseInt(row), Integer.parseInt(col))) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**checks if the game is won*/
    public boolean isWin(){
        return currentConfig.isSolution();
    }

    /**'moves' a piece*/
    public void move(int startRow, int startCol, int endRow, int endCol){
        ChessConfig newConfig = new ChessConfig(startRow, startCol,
                endRow, endCol, currentConfig);

        Collection<Configuration> neighbors = currentConfig.getNeighbors();

        if(neighbors.contains(newConfig)){
            currentConfig = newConfig;
            alertObservers("Captured from (" + startRow + "," + startCol + ") to (" + endRow + "," + endCol + ")");
        }
        else{alertObservers("Can't capture from (" + startRow + "," + startCol + ") to (" + endRow + "," + endCol + ")");}
    }

    /**resets the game*/
    public void reset() throws IOException {
        load(filename);
        alertObservers("Puzzle Reset!");
    }

    /**returns a string of the chess board*/
    public String boardState(){
        return currentConfig.toString();
    }

    /**returns filename*/
    public String getFilename() {
        return filename;
    }

    /**returns rows*/
    public int getRows() {return currentConfig.getRows();}
    /**returns columns*/
    public int getCols() {return currentConfig.getCols();}
    /**returns cell*/
    public String getCell(int row, int col){return currentConfig.getCell(row, col);}
}
