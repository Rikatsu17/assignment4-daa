
import graph.topo.TopologicalSort;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class TopoSortTest {

    @Test
    void testKahnSort_simpleDAG() {
        Map<String, List<String>> graph = new HashMap<>();
        graph.put("A", List.of("B", "C"));
        graph.put("B", List.of("D"));
        graph.put("C", List.of("D"));
        graph.put("D", new ArrayList<>());

        List<String> order = TopologicalSort.kahnSort(graph);

        assertTrue(order.indexOf("A") < order.indexOf("B"));
        assertTrue(order.indexOf("A") < order.indexOf("C"));
        assertTrue(order.indexOf("B") < order.indexOf("D"));
        assertTrue(order.indexOf("C") < order.indexOf("D"));
    }

    @Test
    void testKahnSort_cycleDetection() {
        Map<String, List<String>> cyclic = new HashMap<>();
        cyclic.put("A", List.of("B"));
        cyclic.put("B", List.of("A"));

        assertThrows(IllegalStateException.class, () ->
                TopologicalSort.kahnSort(cyclic));
    }
}
