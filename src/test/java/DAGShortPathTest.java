
import graph.dagsp.DAGShortestPath;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class DAGShortestPathTest {

    @Test
    void testShortestPath() {
        Map<String, List<DAGShortestPath.Edge>> graph = new HashMap<>();
        graph.put("S", List.of(new DAGShortestPath.Edge("A", 3),
                new DAGShortestPath.Edge("B", 6)));
        graph.put("A", List.of(new DAGShortestPath.Edge("C", 4)));
        graph.put("B", List.of(new DAGShortestPath.Edge("C", 2)));
        graph.put("C", List.of(new DAGShortestPath.Edge("D", 5)));
        graph.put("D", new ArrayList<>());

        DAGShortestPath dagsp = new DAGShortestPath(graph);
        List<String> topo = List.of("S", "A", "B", "C", "D");

        var dist = dagsp.shortestPath("S", topo);

        assertEquals(0, dist.get("S"));
        assertEquals(3, dist.get("A"));
        assertEquals(6, dist.get("B"));
        assertEquals(7, dist.get("C"));
        assertEquals(12, dist.get("D"));
    }

    @Test
    void testLongestPath() {
        Map<String, List<DAGShortestPath.Edge>> graph = new HashMap<>();
        graph.put("S", List.of(new DAGShortestPath.Edge("A", 2),
                new DAGShortestPath.Edge("B", 3)));
        graph.put("A", List.of(new DAGShortestPath.Edge("C", 4)));
        graph.put("B", List.of(new DAGShortestPath.Edge("C", 1)));
        graph.put("C", new ArrayList<>());

        DAGShortestPath dagsp = new DAGShortestPath(graph);
        List<String> topo = List.of("S", "A", "B", "C");

        var longest = dagsp.longestPath("S", topo);
        assertEquals(6, longest.get("C"));
    }
}
