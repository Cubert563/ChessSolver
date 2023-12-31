package puzzles.hoppers.ptui;

import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

import java.io.IOException;
import java.util.Scanner;

public class HoppersPTUI implements Observer<HoppersModel, String> {
    private HoppersModel model;


    private static int counter;

    public void init(String filename) throws IOException {
        this.model = new HoppersModel(filename);
        this.model.addObserver(this);
        this.model.load(filename);

        displayHelp();
    }

    @Override
    public void update(HoppersModel model, String data) {
        // for demonstration purposes
        System.out.println(data);
        System.out.println(model);
    }

    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    public void run() throws IOException {
        Scanner in = new Scanner(System.in);
        //this.model.load(this.model.getFilename());
        for (; ; ) {
            System.out.print("> ");
            String line = in.nextLine();
            String[] words = line.split("\\s+");
            if (words.length > 0) {
                //this.model.load(words[0]);
                if (words[0].startsWith("q")) {
                    break;
                } else if (words[0].startsWith("s")) {
                    int row = Integer.parseInt(words[1]);
                    int col = Integer.parseInt(words[2]);
                    this.model.select(row, col);
                }
                else if (words[0].startsWith("r")) {
                    this.model.reset();
                }
                else if (words[0].startsWith("h")) {
                    this.model.hint();
                }
                else if (words[0].startsWith("l")) {
                    this.model.load(words[1]);
                }
                else {
                    displayHelp();
                }
            }
        }
    }




    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            try {
                HoppersPTUI ptui = new HoppersPTUI();
                ptui.init(args[0]);
                ptui.run();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}
