package graph.dagsp;

import util.Metrics;
import java.util.*;

public class DAGShortestPath {

    private final Map<String, List<Edge>> graph;
    private static Metrics lastMetrics;

    public static class Edge {
        public final String to;
        public final int weight;

        public Edge(String to, int weight) {
            this.to = to;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return to + "(" + weight + ")";
        }
    }

    public DAGShortestPath(Map<String, List<Edge>> graph) {
        this.graph = graph;
    }

    public Map<String, Integer> shortestPath(String source, List<String> topoOrder) {
        Metrics metrics = new Metrics("DAGSP_Shortest");
        metrics.start();

        Map<String, Integer> dist = new HashMap<>();
        for (String v : graph.keySet()) dist.put(v, Integer.MAX_VALUE);
        dist.put(source, 0);

        for (String u : topoOrder) {
            if (dist.get(u) == Integer.MAX_VALUE) continue;
            for (Edge e : graph.getOrDefault(u, Collections.emptyList())) {
                metrics.incRelaxation();
                if (dist.get(e.to) > dist.get(u) + e.weight) {
                    dist.put(e.to, dist.get(u) + e.weight);
                }
            }
        }

        metrics.stop();
        lastMetrics = metrics;
        return dist;
    }

    public Map<String, Integer> longestPath(String source, List<String> topoOrder) {
        Metrics metrics = new Metrics("DAGSP_Longest");
        metrics.start();

        Map<String, Integer> dist = new HashMap<>();
        for (String v : graph.keySet()) dist.put(v, Integer.MIN_VALUE);
        dist.put(source, 0);

        for (String u : topoOrder) {
            if (dist.get(u) == Integer.MIN_VALUE) continue;
            for (Edge e : graph.getOrDefault(u, Collections.emptyList())) {
                metrics.incRelaxation();
                if (dist.get(e.to) < dist.get(u) + e.weight) {
                    dist.put(e.to, dist.get(u) + e.weight);
                }
            }
        }

        metrics.stop();
        lastMetrics = metrics;
        return dist;
    }

    public static Metrics getLastMetrics() {
        return lastMetrics;
    }
}
