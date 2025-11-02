<h2>Assignment 4 | Design and Algorithm Analysis</h2>
<h4>1. Strongly Connected Components (SCC) & Topological Ordering<br>2. Shortest Paths in DAGs</h4>
<h3>Adilbekov Daniyal, SE-2435</h3>


<h2>Overview</h2>

The purpose of this assignment is to implement and analyze key graph algorithms on various datasets:

Tarjan’s Algorithm — detect Strongly Connected Components (SCCs)

Kahn’s Algorithm — compute Topological Order for Directed Acyclic Graphs (DAGs)

DAG Shortest Path — find Shortest and Longest Paths in a DAG

Each algorithm is instrumented with a Metrics system that captures operation counts and execution time for benchmarking.<br>

<h3>How to Run</h3>
Requirements

Java 17+

Maven 3.8+

Gson library (via Maven dependency)

`mvn clean compile`

`mvn exec:java -Dexec.mainClass="graph.Main"`

<h2>**Project Structure**</h2><br>

| Package / File                | Description                                                                                    |
| ----------------------------- | ---------------------------------------------------------------------------------------------- |
| `graph.scc.TarjanSCC`         | Implements Tarjan’s SCC algorithm using DFS, recursion, and stack tracking.                    |
| `graph.topo.TopologicalSort`  | Implements **Kahn’s Algorithm** and DFS-based Topological Sort.                                |
| `graph.dagsp.DAGShortestPath` | Computes both **shortest** and **longest** paths in DAGs.                                      |
| `util.GraphLoader`            | Loads graphs from JSON files (`/data/*.json`) into in-memory structures.                       |
| `util.Metrics`                | Collects metrics: DFS visits, edge traversals, queue ops, relaxations, and execution time.     |
| `graph.Main`                  | Main entry point — orchestrates all algorithms and writes performance data into `metrics.csv`. |

<h2> Instrumentation Details</h2>

Metrics recorded for each algorithm:

| Metric        | Description                                            |
| ------------- | ------------------------------------------------------ |
| `TIME(ms)`    | Execution time measured via `System.nanoTime()`        |
| `DFS_VISITS`  | Number of DFS calls made                               |
| `EDGE_CHECKS` | Edge traversals during SCC or topo sort                |
| `QUEUE_OPS`   | Push/pop operations in Kahn’s algorithm                |
| `RELAXATIONS` | Relaxation steps in shortest/longest path computations |


All data is automatically written to metrics.csv after running Main.java.

 Dataset Summary<br>
Category	Nodes (n)	Description	Variants<br>
Small	6–10	Simple DAGs and small cycles	5<br>
Medium	10–20	Mixed graphs with several SCCs	5<br>
Large	20–50	Dense graphs for performance testing	4<br>

All datasets are generated in /data/ and include both cyclic and acyclic graphs.

| Dataset    | Algorithm             | Time (ms) | DFS | Edges | QueueOps | Relaxations |
| ---------- | --------------------- | --------- | --- | ----- | -------- | ----------- |
| `small_1`  | Tarjan_SCC            | 0.141     | 7   | 6     | 0        | 0           |
| `small_1`  | Kahn_Topological_Sort | 0.151     | 0   | 3     | 8        | 0           |
| `small_1`  | DAG_Shortest_Path     | 0.052     | 0   | 0     | 0        | 3           |
| `small_1`  | DAG_Longest_Path      | 0.035     | 0   | 0     | 0        | 3           |
| `medium_2` | Tarjan_SCC            | 0.280     | 15  | 15    | 0        | 0           |
| `medium_2` | Kahn_Topological_Sort | 0.171     | 0   | 15    | 29       | 0           |
| `medium_2` | DAG_Shortest_Path     | 0.041     | 0   | 0     | 0        | 15          |
| `medium_2` | DAG_Longest_Path      | 0.051     | 0   | 0     | 0        | 15          |
| `large_2`  | Tarjan_SCC            | 0.066     | 30  | 25    | 0        | 0           |
| `large_2`  | Kahn_Topological_Sort | 0.057     | 0   | 25    | 55       | 0           |
| `large_2`  | DAG_Shortest_Path     | 0.306     | 0   | 0     | 0        | 25          |
| `large_2`  | DAG_Longest_Path      | 0.028     | 0   | 0     | 0        | 25          |

 <h2>Analysis</h2>
<b>Tarjan’s Algorithm (SCC)</b>

Performs a single DFS traversal with linear complexity O(V + E).

Scales very efficiently; performance mainly depends on the number of edges.

Example: For large_3 (35 nodes), it completed under 0.22 ms.

<b>Kahn’s Algorithm (Topological Sort)</b>

Works best on sparse DAGs.

Queue operations (QUEUE_OPS) increase sharply with density.

Average complexity remains O(V + E) but constant factors grow.

<b>DAG Shortest & Longest Paths</b>

Operate only after SCC compression (pure DAG).

Shortest path uses DP relaxation in topological order.

Longest path uses sign inversion or max-DP pattern.

Both complete in negligible time (< 0.1 ms) even for medium graphs.

 <h3>Observations & Insights</h3>
Observation	Explanation<br>
Tarjan’s DFS count correlates with the number of nodes.Each node is visited exactly once<br>
Kahn’s edge checks dominate dense graphs.Every edge is processed during in-degree updates<br>
Relaxations grow with number of edges.Each edge may update a shortest or longest distance<br>
All algorithms scale linearly verified across small → large datasets<br>
<h2> Conclusion</h2>

Implemented and benchmarked Tarjan, Kahn, and DAG Path algorithms.

All algorithms meet expected linear complexity on all test sets.

The metrics framework successfully provides quantitative insights.

| Aspect                           | Method                | When to Use                                        |
| -------------------------------- | --------------------- | -------------------------------------------------- |
| **Cycle Detection**              | Tarjan’s SCC          | When dependency graph may contain mutual tasks     |
| **Ordering**                     | Kahn Topological Sort | For DAG scheduling or task dependency resolution   |
| **Shortest Path**                | DAG DP                | Fastest for acyclic weighted tasks                 |
| **Longest Path (Critical Path)** | Inverted DP           | For identifying task bottlenecks or total duration |

