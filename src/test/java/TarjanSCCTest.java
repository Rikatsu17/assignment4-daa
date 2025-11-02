
import graph.scc.TarjanSCC;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TarjanSCCTest {

    @Test
    void testFindSCCs_simpleCycle() {
        Map<String, List<String>> graph = new HashMap<>();
        graph.put("A", List.of("B"));
        graph.put("B", List.of("C"));
        graph.put("C", List.of("A"));

        TarjanSCC scc = new TarjanSCC(graph);
        var components = scc.findSCCs();

        assertEquals(1, components.size());
        assertTrue(components.get(0).containsAll(List.of("A", "B", "C")));
    }

    @Test
    void testFindSCCs_multipleComponents() {
        Map<String, List<String>> graph = new HashMap<>();
        graph.put("A", List.of("B"));
        graph.put("B", List.of("A"));
        graph.put("C", List.of("D"));
        graph.put("D", List.of("E"));
        graph.put("E", List.of("C"));

        TarjanSCC scc = new TarjanSCC(graph);
        var comps = scc.findSCCs();

        assertEquals(2, comps.size());
    }
}
