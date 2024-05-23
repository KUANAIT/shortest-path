import java.util.*;

class Graph {
    private final Map<String, List<Edge>> adjacencyList;
    private Map<String, String> previousNodes;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    public void addEdge(String source, String destination, int weight) {
        adjacencyList.computeIfAbsent(source, k -> new ArrayList<>()).add(new Edge(destination, weight));
        adjacencyList.computeIfAbsent(destination, k -> new ArrayList<>()).add(new Edge(source, weight));
    }

    public Map<String, Integer> dijkstra(String start) {
        Map<String, Integer> distances = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(node -> node.distance));
        previousNodes = new HashMap<>();

        for (String vertex : adjacencyList.keySet()) {
            if (vertex.equals(start)) {
                distances.put(vertex, 0);
                pq.add(new Node(vertex, 0));
            } else {
                distances.put(vertex, Integer.MAX_VALUE);
                pq.add(new Node(vertex, Integer.MAX_VALUE));
            }
            previousNodes.put(vertex, null);
        }

        while (!pq.isEmpty()) {
            Node currentNode = pq.poll();
            String currentVertex = currentNode.vertex;

            for (Edge edge : adjacencyList.getOrDefault(currentVertex, new ArrayList<>())) {
                String neighbor = edge.destination;
                int newDist = distances.get(currentVertex) + edge.weight;

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    pq.add(new Node(neighbor, newDist));
                    previousNodes.put(neighbor, currentVertex);
                }
            }
        }

        return distances;
    }

    public List<String> shortestPath(String start, String end) {
        dijkstra(start);
        List<String> path = new ArrayList<>();
        for (String at = end; at != null; at = previousNodes.get(at)) {
            path.add(at);
        }
            Collections.reverse(path);
        return path;
    }

    private static class Node {
        String vertex;
        int distance;

        Node(String vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }

    private static class Edge {
        String destination;
        int weight;

        Edge(String destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.addEdge("Edinburgh", "Stirling", 50);
        graph.addEdge("Edinburgh", "Perth", 100);
        graph.addEdge("Stirling", "Glasgow", 50);
        graph.addEdge("Stirling", "Perth", 40);
        graph.addEdge("Glasgow", "Perth", 70);
        graph.addEdge("Perth", "Dundee", 60);

        List<String> path = graph.shortestPath("Edinburgh", "Dundee");
        System.out.println("Shortest path from Edinburgh to Dundee: " + path);
        System.out.println("Total distance: " + graph.dijkstra("Edinburgh").get("Dundee"));
    }
}
