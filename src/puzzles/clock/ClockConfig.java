package puzzles.clock;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClockConfig implements Configuration{
    /**
     * The neighbors of a configuration.
     */
    private List<Configuration> neighbors;
    private final int hours;
    /**
     * The start hour.
     */
    private final int start;
    /**
     * The end hour.
     */
    private final int end;

    /**
     * Creates an instance of ClockConfig by initializing the start time, end time, and number
     * of hours.
     * @param start the start hour.
     * @param end the end hour.
     * @param hours the number of hours in the clock.
     */
    public ClockConfig(int start, int end, int hours) {
        this.start = start;
        this.end = end;
        this.hours = hours;
    }

    @Override
    public boolean isSolution() {
        return this.start == this.end;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        this.neighbors = new ArrayList<>();
        int neighbor1 = this.start - 1;
        int neighbor2 = this.start + 1;
        if (neighbor1 == 0) {
            neighbor1 = this.hours;
            neighbors.add(new ClockConfig(neighbor1, this.end, this.hours));
            neighbors.add(new ClockConfig(this.start + 1, this.end, this.hours));
        }
        else if (neighbor2 > this.hours) {
            neighbor2 = 1;
            neighbors.add(new ClockConfig(this.start - 1, this.end, this.hours));
            neighbors.add(new ClockConfig(neighbor2, this.end, this.hours));
        }
        else {
            neighbors.add(new ClockConfig(this.start - 1, this.end, this.hours));
            neighbors.add(new ClockConfig(this.start + 1, this.end, this.hours));
        }
        return this.neighbors;
    }

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof ClockConfig) {
            if (((ClockConfig) other).start == this.start &&
                    (((ClockConfig) other).end == this.end &&
                            ((ClockConfig)  other).hours == this.hours)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return this.start + this.end + this.hours;
    }

    @Override
    public String toString() {
        return "" + this.start + "";
    }
}
