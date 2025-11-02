package graph;

import com.google.gson.JsonObject;
import graph.scc.TarjanSCC;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGShortestPath;
import graph.dagsp.DAGShortestPath.Edge;
import util.GraphLoader;
import util.Metrics;

import java.io.FileWriter;
import java.util.*;

public class Main {

    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String BOLD = "\u001B[1m";

    public static void main(String[] args) {

        String path = "src/main/resources/graphs.json";
        List<JsonObject> allGraphs = GraphLoader.loadAllGraphs(path);

        try (FileWriter w = new FileWriter("metrics.csv")) {
            w.write("DATASET,ALGORITHM,TIME(ms),DFS_VISITS,EDGE_CHECKS,QUEUE_OPS,RELAXATIONS\n");
        } catch (Exception e) {
            System.err.println(RED + "⚠ Error initializing metrics.csv: " + e.getMessage() + RESET);
        }

        System.out.println(BOLD + CYAN + "\n Starting Graph Processing...\n" + RESET);

        for (JsonObject dataset : allGraphs) {
            String name = dataset.get("name").getAsString();
            String source = GraphLoader.getSource(dataset);
            var weightedGraph = GraphLoader.parseWeighted(dataset);
            var unweightedGraph = GraphLoader.parseUnweighted(dataset);

            System.out.println(YELLOW + "─────────────────────────────────────────────" + RESET);
            System.out.println(BOLD + " Dataset: " + name + RESET);
            System.out.println("Nodes: " + dataset.get("n").getAsInt() + " | Source: " + source);
            System.out.println("Directed: " + dataset.get("directed").getAsBoolean());
            System.out.println(YELLOW + "─────────────────────────────────────────────" + RESET);

            TarjanSCC scc = new TarjanSCC(unweightedGraph);
            var components = scc.findSCCs();
            writeMetrics(name, "Tarjan_SCC", scc.getMetrics());
            System.out.println(GREEN + "✔ Tarjan SCC done. Components: " + components.size() + RESET);

            var condensation = TarjanSCC.toCondensationGraph(components, unweightedGraph);
            System.out.println(GREEN + "Condensation graph created with " + condensation.size() + " components.");

            TopologicalSort.kahnSort(condensation);
            writeMetrics(name, "Kahn_Topological_Sort", TopologicalSort.getMetrics());
            System.out.println(GREEN + "✔ Topological Sort done." + RESET);

            DAGShortestPath dag = new DAGShortestPath(weightedGraph);
            var simple = toSimple(weightedGraph);
            var topoForSP = TopologicalSort.dfsSort(simple);

            dag.shortestPath(source, topoForSP);
            writeMetrics(name, "DAG_Shortest_Path", DAGShortestPath.getLastMetrics());
            System.out.println(GREEN + "✔ Shortest Path computed." + RESET);

            dag.longestPath(source, topoForSP);
            writeMetrics(name, "DAG_Longest_Path", DAGShortestPath.getLastMetrics());
            System.out.println(GREEN + "✔ Longest Path computed." + RESET);

            System.out.println(CYAN + " Dataset '" + name + "' completed.\n" + RESET);
        }

        System.out.println(BOLD + CYAN + " All graphs processed successfully!" + RESET);
        System.out.println(BOLD + "Results saved to: " + GREEN + "metrics.csv" + RESET);
    }

    private static void writeMetrics(String dataset, String algo, Metrics m) {
        try (FileWriter w = new FileWriter("metrics.csv", true)) {
            w.write(String.format("%s,%s,%.3f,%d,%d,%d,%d\n",
                    dataset,
                    algo,
                    m.getTimeMs(),
                    m.getDFSVisits(),
                    m.getEdgeChecks(),
                    m.getQueueOps(),
                    m.getRelaxations()));
        } catch (Exception e) {
            System.err.println(RED + " Error writing metrics for " + dataset + ": " + e.getMessage() + RESET);
        }
    }

    private static Map<String, List<String>> toSimple(Map<String, List<Edge>> g) {
        Map<String, List<String>> simple = new HashMap<>();
        for (var e : g.entrySet()) {
            List<String> adj = new ArrayList<>();
            for (Edge edge : e.getValue()) adj.add(edge.to);
            simple.put(e.getKey(), adj);
        }
        return simple;
    }
}
