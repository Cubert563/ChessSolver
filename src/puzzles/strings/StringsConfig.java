package puzzles.strings;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class StringsConfig implements Configuration{
    /**
     * The neighbors of a configuration.
     */
    private List<Configuration> neighbors;
    /**
     * The start string.
     */
    private final String start;
    /**
     * The end string.
     */
    private final String end;

    /**
     * Creates an instance of StringsConfig by initializing the start and end strings.
     * @param start the start string.
     * @param end the end string.
     */
    public StringsConfig(String start, String end) {
        this.start = start;
        this.end = end;
    }
    @Override
    public boolean isSolution() {
        return this.start.equals(this.end);
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        this.neighbors = new ArrayList<>();
        for (int i = 0; i < start.length(); i++) {
            StringBuilder str1 = new StringBuilder(start);
            StringBuilder str2 = new StringBuilder(start);
            str1.setCharAt(i, (char) (start.charAt(i) + 1));
            str2.setCharAt(i, (char) (start.charAt(i) - 1));
            if (str1.charAt(i) == 91) {
                str1.setCharAt(i, (char) (65));
            }
            else if (str2.charAt(i) == 64) {
                str2.setCharAt(i, (char) (90));
            }
            neighbors.add(new StringsConfig(str1.toString(), end));
            neighbors.add(new StringsConfig(str2.toString(), end));
        }
        return this.neighbors;
    }

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof StringsConfig) {
            if (((StringsConfig) other).start.equals(this.start) &&
                    ((StringsConfig) other).end.equals(this.end)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.start + this.end);
    }

    @Override
    public String toString() {
        return this.start;
    }
}
