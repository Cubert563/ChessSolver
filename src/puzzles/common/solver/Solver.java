package puzzles.common.solver;

import java.util.*;

public class Solver {
    /**
     * The bfs queue.
     */
    private final Queue<Configuration> queue;
    /**
     * The bfs map.
     */
    private final HashMap<Configuration, Configuration> predMap;
    /**
     * The total number of configurations.
     */
    private int totalConfigs;
    /**
     * The number of unique configurations.
     */
    private int uniqueConfigs;

    /**
     * Creates an instance of the Solver class by initializing the bfs queue and hashmap.
     */
    public Solver() {
        this.queue = new LinkedList<>();
        this.predMap = new HashMap<>();
    }

    /**
     * Performs a basic breadth first search.
     * @return The shortest path.
     */
    public List<Configuration> solverBFS(Configuration start) {
        this.totalConfigs = 1;
        this.uniqueConfigs = 1;
        this.predMap.put(start, null);
        this.queue.offer(start);
        while (!queue.isEmpty()) {
            Configuration config = this.queue.remove();
            if (config.isSolution()){
                List<Configuration> path = new ArrayList<>();
                path.add(config);
                Configuration previous = this.predMap.get(config);
                while (previous != null) {
                    path.add(0, previous);
                    previous = this.predMap.get(previous);
                }
                return path;
            }
            else {
                for (Configuration nbr : config.getNeighbors()) {
                    totalConfigs += 1;
                    if (!this.predMap.containsKey(nbr)) {
                        uniqueConfigs += 1;
                        this.predMap.put(nbr, config);
                        this.queue.offer(nbr);
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * Getter for the total configurations.
     * @return totalConfigs
     */
    public int getTotalConfigs() {
        return this.totalConfigs;
    }

    /**
     * Getter for the unique configurations.
     * @return uniqueConfigs
     */
    public int getUniqueConfigs() {
        return this.uniqueConfigs;
    }
}
