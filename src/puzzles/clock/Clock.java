package puzzles.clock;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.List;

public class Clock {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Clock hours stop end"));
        } else {
            int hours = Integer.parseInt(args[0]);
            int start = Integer.parseInt(args[1]);
            int end = Integer.parseInt(args[2]);
            Solver solver = new Solver();
            ClockConfig clock = new ClockConfig(start, end, hours);
            List<Configuration> path = solver.solverBFS(clock);
            System.out.println("Hours: " + hours + ", Start: " + start + ", End: " + end);
            System.out.println("Total Configs: " + solver.getTotalConfigs());
            System.out.println("Unique Configs: " + solver.getUniqueConfigs());
            if (end > hours || start > hours) {
                System.out.println("No solution");
            }
            for (int i = 0; i < path.size(); i++) {
                System.out.println("Step " + i + ": " + path.get(i).toString());
            }
        }
    }
}
