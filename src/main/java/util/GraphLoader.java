package util;

import com.google.gson.*;
import graph.dagsp.DAGShortestPath;

import java.io.FileReader;
import java.util.*;

public class GraphLoader {

    public static List<JsonObject> loadAllGraphs(String path) {
        try (FileReader reader = new FileReader(path)) {
            JsonArray arr = JsonParser.parseReader(reader).getAsJsonArray();
            List<JsonObject> graphs = new ArrayList<>();
            for (JsonElement e : arr) graphs.add(e.getAsJsonObject());
            return graphs;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load all graphs: " + e.getMessage());
        }
    }

    public static Map<String, List<String>> parseUnweighted(JsonObject json) {
        Map<String, List<String>> graph = new HashMap<>();
        JsonArray edges = json.getAsJsonArray("edges");

        for (JsonElement e : edges) {
            JsonObject edge = e.getAsJsonObject();
            String u = String.valueOf(edge.get("u").getAsInt());
            String v = String.valueOf(edge.get("v").getAsInt());

            graph.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            graph.computeIfAbsent(v, k -> new ArrayList<>());
        }

        int n = json.get("n").getAsInt();
        for (int i = 0; i < n; i++) graph.putIfAbsent(String.valueOf(i), new ArrayList<>());
        return graph;
    }

    public static Map<String, List<DAGShortestPath.Edge>> parseWeighted(JsonObject json) {
        Map<String, List<DAGShortestPath.Edge>> graph = new HashMap<>();
        JsonArray edges = json.getAsJsonArray("edges");

        for (JsonElement e : edges) {
            JsonObject edge = e.getAsJsonObject();
            String u = String.valueOf(edge.get("u").getAsInt());
            String v = String.valueOf(edge.get("v").getAsInt());
            int w = edge.get("w").getAsInt();

            graph.computeIfAbsent(u, k -> new ArrayList<>()).add(new DAGShortestPath.Edge(v, w));
            graph.computeIfAbsent(v, k -> new ArrayList<>());
        }

        int n = json.get("n").getAsInt();
        for (int i = 0; i < n; i++) graph.putIfAbsent(String.valueOf(i), new ArrayList<>());
        return graph;
    }

    public static String getSource(JsonObject json) {
        return String.valueOf(json.get("source").getAsInt());
    }
}
