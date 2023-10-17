package puzzles.chess.solver;

import puzzles.chess.model.ChessConfig;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
/**
 * Solves a chess puzzle
 * Author: Jack Werremeyer
 * */
public class Chess {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Chess filename");
        }
        else {
            try {
                ChessConfig chess = new ChessConfig(args[0]);

//                System.out.println(chess.toString());

                Solver solver = new Solver();
                List<Configuration> moves = solver.solverBFS(chess);

//                Collection<Configuration> test = chess.getNeighbors();
                System.out.println(solver.getTotalConfigs());
                System.out.println(solver.getUniqueConfigs());

                for (Configuration config : moves) {
                    System.out.println(config.toString());
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }
    }
}
