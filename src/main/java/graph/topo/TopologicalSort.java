package graph.topo;

import util.Metrics;
import java.util.*;

public class TopologicalSort {

    private static Metrics metrics;

    public static List<String> kahnSort(Map<String, List<String>> graph) {
        metrics = new Metrics("KahnTopo");
        metrics.start();

        Map<String, Integer> indegree = new HashMap<>();
        for (String u : graph.keySet()) indegree.put(u, 0);
        for (List<String> adj : graph.values()) {
            for (String v : adj) indegree.put(v, indegree.getOrDefault(v, 0) + 1);
        }

        Queue<String> q = new ArrayDeque<>();
        for (var e : indegree.entrySet()) {
            if (e.getValue() == 0) q.offer(e.getKey());
        }

        List<String> order = new ArrayList<>();
        while (!q.isEmpty()) {
            String u = q.poll();
            metrics.incQueueOp();
            order.add(u);
            for (String v : graph.getOrDefault(u, Collections.emptyList())) {
                indegree.put(v, indegree.get(v) - 1);
                metrics.incEdge();
                if (indegree.get(v) == 0) {
                    q.offer(v);
                    metrics.incQueueOp();
                }
            }
        }

        metrics.stop();
        if (order.size() != graph.size()) {
            throw new IllegalStateException("Graph contains a cycle — not a DAG!");
        }

        return order;
    }

    public static List<String> dfsSort(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();
        List<String> result = new ArrayList<>();
        for (String v : graph.keySet()) {
            if (!visited.contains(v)) dfs(v, graph, visited, result);
        }
        Collections.reverse(result);
        return result;
    }

    private static void dfs(String u, Map<String, List<String>> graph,
                            Set<String> visited, List<String> result) {
        visited.add(u);
        for (String v : graph.getOrDefault(u, Collections.emptyList())) {
            if (!visited.contains(v)) dfs(v, graph, visited, result);
        }
        result.add(u);
    }

    public static Metrics getMetrics() {
        return metrics;
    }
}
