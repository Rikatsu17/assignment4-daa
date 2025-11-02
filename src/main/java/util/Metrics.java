package util;

import java.io.FileWriter;
import java.io.IOException;

public class Metrics {

    private String algorithmName;
    private long startTime;
    private long endTime;

    private long dfsVisits;
    private long edgeChecks;
    private long queueOps;
    private long relaxations;

    public Metrics() {}

    public Metrics(String name) {
        this.algorithmName = name;
    }

    public void setName(String name) {
        this.algorithmName = name;
    }

    public void start() {
        startTime = System.nanoTime();
    }

    public void stop() {
        endTime = System.nanoTime();
    }

    public double getTimeMs() {
        return (endTime - startTime) / 1_000_000.0;
    }

    public void incDFS() { dfsVisits++; }
    public void incEdge() { edgeChecks++; }
    public void incQueueOp() { queueOps++; }
    public void incRelaxation() { relaxations++; }

    public long getDFSVisits() { return dfsVisits; }
    public long getEdgeChecks() { return edgeChecks; }
    public long getQueueOps() { return queueOps; }
    public long getRelaxations() { return relaxations; }

    public void saveToCSV(String path) {
        try (FileWriter writer = new FileWriter(path, true)) {
            writer.write(String.format("%s,%.3f,%d,%d,%d,%d\n",
                    algorithmName,
                    getTimeMs(),
                    dfsVisits,
                    edgeChecks,
                    queueOps,
                    relaxations));
        } catch (IOException e) {
            System.err.println("Error writing metrics CSV: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return String.format(
                "%s | Time: %.3f ms | DFS=%d | Edges=%d | QueueOps=%d | Relax=%d",
                algorithmName,
                getTimeMs(),
                dfsVisits,
                edgeChecks,
                queueOps,
                relaxations
        );
    }
}
