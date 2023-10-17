package puzzles.hoppers.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.IOException;
import java.util.List;

public class Hoppers {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        }
        else {
            Solver solver = new Solver();
            HoppersConfig hop = new HoppersConfig(args[0]);
            List<Configuration> path = solver.solverBFS(hop);
            System.out.println("File: " + args[0]);
            System.out.println(hop);
            System.out.println("Total configs: " + solver.getTotalConfigs());
            System.out.println("Unique configs: " + solver.getUniqueConfigs());
            for (int i = 0; i < path.size(); i++) {
                System.out.println("Step " + i + ": ");
                System.out.println(path.get(i).toString());
            }
        }
    }
}
