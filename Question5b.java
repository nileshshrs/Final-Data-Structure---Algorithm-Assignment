import java.util.*;

class ISP {

    public static List<Integer> findImpactedDevices(int[][] edges, int targetDevice) {
        Set<Integer> impactedDevices = new HashSet<>();
        Set<Integer> visited = new HashSet<>();

        for (int[] edge : edges) {
            if (edge[0] == targetDevice || edge[1] == targetDevice) {
                dfs(edges, edge[0], targetDevice, visited, impactedDevices);
                dfs(edges, edge[1], targetDevice, visited, impactedDevices);
            }
        }

        // Remove the target device from the impacted set
        impactedDevices.remove(targetDevice);
        return new ArrayList<>(impactedDevices);
    }

    private static void dfs(int[][] edges, int current, int targetDevice, Set<Integer> visited, Set<Integer> impacted) {
        if (!visited.contains(current)) {
            visited.add(current);

            for (int[] edge : edges) {
                if (edge[0] == current && edge[1] != targetDevice) {
                    dfs(edges, edge[1], targetDevice, visited, impacted);
                    impacted.add(edge[1]); // Add non-targetDevice neighbor to impacted set
                } else if (edge[1] == current && edge[0] != targetDevice) {
                    dfs(edges, edge[0], targetDevice, visited, impacted);
                    impacted.add(edge[0]); // Add non-targetDevice neighbor to impacted set
                }
            }
        }
    }

    public static void main(String[] args) {
        int[][] edges = { { 0, 1 }, { 0, 2 }, { 1, 3 }, { 1, 6 }, { 2, 4 }, { 4, 6 }, { 4, 5 }, { 5, 7 } };
        int targetDevice = 4;

        List<Integer> impactedDevices = findImpactedDevices(edges, targetDevice);

        System.out.println("Impacted Device List: " + impactedDevices);
    }
}
