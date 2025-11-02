package graph.scc;

import util.Metrics;
import java.util.*;

public class TarjanSCC {
    private final Map<String, List<String>> graph;
    private final Metrics metrics = new Metrics("TarjanSCC");
    private int time = 0;

    private final Map<String, Integer> disc = new HashMap<>();
    private final Map<String, Integer> low = new HashMap<>();
    private final Deque<String> stack = new ArrayDeque<>();
    private final Set<String> onStack = new HashSet<>();
    private final List<List<String>> components = new ArrayList<>();

    public TarjanSCC(Map<String, List<String>> graph) {
        this.graph = graph;
    }

    public List<List<String>> findSCCs() {
        metrics.start();

        for (String v : graph.keySet()) {
            if (!disc.containsKey(v)) dfs(v);
        }

        metrics.stop();

        System.out.println("[Tarjan Metrics] " + metrics);
        metrics.saveToCSV("metrics.csv");
        return components;
    }

    private void dfs(String u) {
        metrics.incDFS();
        disc.put(u, time);
        low.put(u, time);
        time++;
        stack.push(u);
        onStack.add(u);

        for (String v : graph.getOrDefault(u, Collections.emptyList())) {
            metrics.incEdge();
            if (!disc.containsKey(v)) {
                dfs(v);
                low.put(u, Math.min(low.get(u), low.get(v)));
            } else if (onStack.contains(v)) {
                low.put(u, Math.min(low.get(u), disc.get(v)));
            }
        }

        if (Objects.equals(low.get(u), disc.get(u))) {
            List<String> comp = new ArrayList<>();
            String w;
            do {
                w = stack.pop();
                onStack.remove(w);
                comp.add(w);
            } while (!w.equals(u));
            components.add(comp);
        }
    }

    public static Map<String, List<String>> toCondensationGraph(List<List<String>> sccList,
                                                                Map<String, List<String>> graph) {
        Map<String, Integer> compIndex = new HashMap<>();
        for (int i = 0; i < sccList.size(); i++) {
            for (String v : sccList.get(i)) {
                compIndex.put(v, i);
            }
        }

        Map<String, List<String>> condensed = new HashMap<>();
        for (int i = 0; i < sccList.size(); i++) {
            condensed.put("C" + i, new ArrayList<>());
        }

        for (var entry : graph.entrySet()) {
            String u = entry.getKey();
            for (String v : entry.getValue()) {
                int cu = compIndex.get(u);
                int cv = compIndex.get(v);
                if (cu != cv) {
                    String from = "C" + cu;
                    String to = "C" + cv;
                    if (!condensed.get(from).contains(to)) {
                        condensed.get(from).add(to);
                    }
                }
            }
        }
        return condensed;
    }

    public Metrics getMetrics() {
        return metrics;
    }
}
