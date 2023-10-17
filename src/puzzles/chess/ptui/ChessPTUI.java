package puzzles.chess.ptui;

import puzzles.common.Observer;
import puzzles.chess.model.ChessModel;

import java.io.IOException;
import java.util.Scanner;
/**
 * Text based chess game
 * Author: Jack Werremeyer
 * */
public class ChessPTUI implements Observer<ChessModel, String> {
    /**the model*/
    private ChessModel model;

    /**checks which variation of select is to be used*/
    private int moveTracker;

    /**piece that taketh the other's coords*/
    private int firstRow;
    private int firstCol;


    /**constructor*/
    public void init(String filename) throws IOException {
        this.moveTracker = 0;
        this.firstRow = 0;
        this.firstCol = 0;
        this.model = new ChessModel(filename);
        this.model.addObserver(this);
    }

    /**prompts for new command and displays board state*/
    @Override
    public void update(ChessModel model, String data) {
        // for demonstration purposes
        System.out.println(">" + data);
        System.out.println(model.boardState());

    }

    /**commands*/
    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    /**reads commands*/
    public void run() throws IOException {
        Scanner in = new Scanner( System.in );
        this.model.load(this.model.getFilename());
        displayHelp();
        for ( ; ; ) {
            System.out.print( "> " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );
            if (words.length > 0) {
                if (words[0].startsWith( "q" )) {
                    break;
                }
                else if(words[0].startsWith( "h" )){
                    this.model.hint();
                }
                else if(words[0].startsWith("s")){
                    if(this.model.validSelect(words[1], words[2], moveTracker)) {
                        if (moveTracker == 0) {
                            moveTracker = 1;
                            firstRow = Integer.parseInt(words[1]);
                            firstCol = Integer.parseInt(words[2]);
                        } else if (moveTracker == 1) {
                            moveTracker = 0;
                            int endRow = Integer.parseInt(words[1]);
                            int endCol = Integer.parseInt(words[2]);
                            this.model.move(firstRow, firstCol, endRow, endCol);
                        }
                    }
                }
                else if(words[0].startsWith("r")){
                    this.model.reset();
                }
                else if(words[0].startsWith("l")){
                    this.model.load(words[1]);
                }
                else {
                    displayHelp();
                }
            }
        }
    }

    /**main*/
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java ChessPTUI filename");
        } else {
            try {
                ChessPTUI ptui = new ChessPTUI();
                ptui.init(args[0]);
                ptui.run();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}

