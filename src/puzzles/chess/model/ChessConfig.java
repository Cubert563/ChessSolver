package puzzles.chess.model;

import puzzles.clock.ClockConfig;
import puzzles.common.solver.Solver;
import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * Is a representation of a chess board
 *
 *
 * Author: Jack Werremeyer
 * */

public class ChessConfig implements Configuration {


    /**rows on board*/
    private int rows;
    /**columns on board*/
    private int cols;
    /**String representation of the board state*/
    private String[][] board;

    /**number of pieces on the board*/
    private int pieceNum;

    /** A horizontal divider */
    char HORI_DIVIDE = '-';
    /** A vertical divider */
    char VERT_DIVIDE = '|';



    /**reads the text file and creates a chess board*/
    public ChessConfig(String filename) throws IOException {
        this.pieceNum = 0;

        try(BufferedReader in = new BufferedReader(new FileReader(filename))){

            String[] fields = in.readLine().strip().split("\\s+");
            this.rows = Integer.parseInt(fields[0]);
            this.cols = Integer.parseInt(fields[1]);

            board = new String[rows][cols];
            for(int i = 0; i < rows; i++){
                String[] row = in.readLine().strip().split("\\s+");
                for(int j = 0; j < cols; j++){
                    board[i][j] = row[j];
                    if(!Objects.equals(row[j], ".")){
                        pieceNum += 1;
                    }
                }
            }
        }
    }

    /**Creates a new chess config after a move has been made*/
    public ChessConfig(int startRow, int startCol, int endRow, int endCol, ChessConfig other){
        rows = other.rows;
        cols = other.cols;
        board = new String[rows][cols];
        for(int i = 0; i < rows; i++){
            System.arraycopy(other.board[i], 0, this.board[i], 0, cols);
        }
        pieceNum = other.pieceNum - 1;

        String piece = board[startRow][startCol];
        board[startRow][startCol] = ".";
        board[endRow][endCol] = piece;
    }

    /**checks to see if the puzzle is solved*/
    @Override
    public boolean isSolution() {
        return pieceNum == 1;
    }

    /**checks to see if the puzzle is valid*/
    public boolean isValid(int row, int col){
        if(row < 0 || col < 0){return false;}
        if(row < rows && col < cols &&
                !board[row][col].equals(".")){
//            System.out.println("1");
            return true;
        }
        return false;
    }

    /**gets the possible combinations of the puzzle*/
    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> list = new ArrayList<Configuration>();

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                if(!board[i][j].equals(".")){


                    //Dynamic moves pieces
                    //Queen is just rook and bishop combined

                    //Rook and +Queen
                    if(board[i][j].equals("R") || board[i][j].equals("Q")){
                        int count = 0;
                        int[] block = {0,0,0,0};
                        while(count < rows && count < cols){
                            count++;

                            //horizontal
                            if(isValid(i, j-count) && block[0] != 1){
                                list.add(new ChessConfig(i,j,i,j-count,this));
                                block[0] = 1;
                            }
                            if(isValid(i, j+count) && block[1] != 1){
                                list.add(new ChessConfig(i,j,i,j+count,this));
                                block[1] = 1;
                            }

                            //Vertical
                            if(isValid(i-count, j) && block[2] != 1){
                                list.add(new ChessConfig(i,j,i-count,j,this));
                                block[2] = 1;
                            }
                            if(isValid(i+count, j) && block[3] != 1){
                                list.add(new ChessConfig(i,j,i+count,j,this));
                                block[3] = 1;
                            }
                        }
                    }

                    //Bishop and xQueen
                    if(board[i][j].equals("B") || board[i][j].equals("Q")){
                        int count = 0;
                        int[] block = {0,0,0,0};
                        while(count < rows && count < cols){
                            count++;

                            //left
                            if(isValid(i-count, j-count) && block[0] != 1){
                                list.add(new ChessConfig(i,j,i-count,j-count,this));
                                block[0] = 1;
                            }
                            if(isValid(i+count, j-count) && block[1] != 1){
                                list.add(new ChessConfig(i,j,i+count,j-count,this));
                                block[1] = 1;
                            }

                            //right
                            if(isValid(i-count, j+count) && block[2] != 1){
                                list.add(new ChessConfig(i,j,i-count,j+count,this));
                                block[2] = 1;
                            }
                            if(isValid(i+count, j+count) && block[3] != 1){
                                list.add(new ChessConfig(i,j,i+count,j+count,this));
                                block[3] = 1;
                            }
                        }
                    }

                    //Set moves pieces

                    //Pawn
                    if(board[i][j].equals("P")){
                        if(isValid(i-1, j-1)){
                            list.add(new ChessConfig(i,j,i-1,j-1,this));
                        }
                        if(isValid(i-1, j+1)){
                            list.add(new ChessConfig(i,j,i-1,j+1,this));
                        }
                    }

                    //Knight
                    if(board[i][j].equals("N")){
                        // L & reverse L
                        if(isValid(i+2, j-1)){
                            list.add(new ChessConfig(i,j,i+2,j-1,this));
                        }
                        if(isValid(i+2, j+1)){
                            list.add(new ChessConfig(i,j,i+2,j+1,this));
                        }
                        //up Ls
                        if(isValid(i-2, j-1)){
                            list.add(new ChessConfig(i,j,i-2,j-1,this));
                        }
                        if(isValid(i-2, j+1)){
                            list.add(new ChessConfig(i,j,i-2,j+1,this));
                        }

                        //right Ls
                        if(isValid(i+1, j+2)){
                            list.add(new ChessConfig(i,j,i+1,j+2,this));
                        }
                        if(isValid(i-1, j+2)){
                            list.add(new ChessConfig(i,j,i-1,j+2,this));
                        }
                        //left Ls
                        if(isValid(i+1, j-2)){
                            list.add(new ChessConfig(i,j,i+1,j-2,this));
                        }
                        if(isValid(i-1, j-2)){
                            list.add(new ChessConfig(i,j,i-1,j-2,this));
                        }
                    }

                    //King
                    if(board[i][j].equals("K")){
                        //top three
                        if(isValid(i-1, j-1)){
                            list.add(new ChessConfig(i,j,i-1,j-1,this));
                        }
                        if(isValid(i-1, j)){
                            list.add(new ChessConfig(i,j,i-1,j,this));
                        }
                        if(isValid(i-1, j+1)){
                            list.add(new ChessConfig(i,j,i-1,j+1,this));
                        }


                        //mid two
                        if(isValid(i, j-1)){
                            list.add(new ChessConfig(i,j,i,j-1,this));
                        }
                        if(isValid(i, j+1)){
                            list.add(new ChessConfig(i,j,i,j+1,this));
                        }

                        //bottom three
                        if(isValid(i+1, j-1)){
                            list.add(new ChessConfig(i,j,i+1,j-1,this));
                        }
                        if(isValid(i+1, j)){
                            list.add(new ChessConfig(i,j,i+1,j,this));
                        }
                        if(isValid(i+1, j+1)){
                            list.add(new ChessConfig(i,j,i+1,j+1,this));
                        }
                    }




                }
            }
        }
        return list;
    }

    public int getRows(){return rows;}
    public int getCols(){return cols;}
    public String getCell(int row, int col){
        return board[row][col];
    }


    /**checks if two puzzles are equal*/
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof ChessConfig) {
            if (Arrays.deepEquals(this.board, ((ChessConfig) other).board)) {
                result = true;
            }
        }
        return result;
    }

    /**hashes the puzzle*/
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.board);
    }

    /**converts the puzzle to a string*/
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder("   ");

        for(int i =0; i < cols; i++){
            result.append(String.valueOf(i)).append(" ");
        }


        result.append(System.lineSeparator());
        result.append("   ");
        // top row, horizontal divider
        result.append(String.valueOf(HORI_DIVIDE).repeat(Math.max(0, getRows() * 2)));

        result.append(System.lineSeparator());

        // field rows
        for (int row=0; row<getRows() ; ++row) {
            result.append(String.valueOf(row)).append(" ");
            result.append(VERT_DIVIDE);
            for (int col = 0; col<getCols() ; ++col) {
                if (col != getCols() -1) {
                    result.append(getCell(row, col)).append(" ");
                } else {
                    result.append(getCell(row, col)).append(System.lineSeparator());
                }
            }
        }

        return result.toString();
    }

}

