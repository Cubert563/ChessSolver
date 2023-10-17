package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.List;

public class Strings {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Strings start finish"));
        } else {
            String start = args[0];
            String end = args[1];
            Solver solver = new Solver();
            StringsConfig strings = new StringsConfig(start, end);
            List<Configuration> path = solver.solverBFS(strings);
            System.out.println("Start: " + start + ", End: " + end);
            System.out.println("Total configs: " + solver.getTotalConfigs());
            System.out.println("Unique configs: " + solver.getUniqueConfigs());
            if (path.isEmpty()) {
                System.out.println("No solution");
            }
            for (int i = 0; i < path.size(); i++) {
                System.out.println("Step " + i + ": " + path.get(i));
            }
        }
    }
}
