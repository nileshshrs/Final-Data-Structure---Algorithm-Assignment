// b)	Assume you were hired to create an application for an ISP, and there are n network devices, 
// such as routers, that are linked together to provide internet access to users. You are given a 2D array 
// that represents network connections between these network devices. write an algorithm to return impacted 
// network devices, If there is a power outage on a certain device, these impacted device list assist you notify 
// linked consumers that there is a power outage and it will take some time to rectify an issue.
 

 
// Input: edges= {{0,1},{0,2},{1,3},{1,6},{2,4},{4,6},{4,5},{5,7}}
// Target Device (On which power Failure occurred): 4
// Output (Impacted Device List) = {5,7}




import java.util.*;

// Class Q5B for finding impacted devices in a network
class Q5B {
    int[] disc, low; // Arrays to store discovery time and low time during DFS
    int time = 1; // Variable to track time during DFS
    List<List<Integer>> ans = new ArrayList<>(); // List to store connections causing impacts
    Map<Integer, List<Integer>> edgeMap = new HashMap<>(); // Map to represent the network connections

    // Method to find impacted devices given the network parameters and a target device
    public List<Integer> findImpactedDevices(int n, List<List<Integer>> connections, int targetDevice) {
        disc = new int[n];
        low = new int[n];
        for (int i = 0; i < n; i++)
            edgeMap.put(i, new ArrayList<Integer>());
        
        // Populate edgeMap based on the given connections
        for (List<Integer> conn : connections) {
            edgeMap.get(conn.get(0)).add(conn.get(1));
            edgeMap.get(conn.get(1)).add(conn.get(0));
        }

        // Perform DFS to identify connections causing impacts
        dfs(targetDevice, -1);

        // Check if the target device is a source node in any connection
        boolean isSourceNode = false;
        for (List<Integer> conn : connections) {
            if (conn.get(0) == targetDevice) {
                isSourceNode = true;
                break;
            }
        }

        // If the target device is not a source node, return an empty list
        if (!isSourceNode) {
            return new ArrayList<>();
        }

        // Set to store impacted devices
        Set<Integer> impactedDevicesSet = new HashSet<>();
        
        // Identify impacted devices based on the connections causing impacts
        for (List<Integer> connection : ans) {
            int u = connection.get(0);
            int v = connection.get(1);

            if (u == targetDevice) {
                impactedDevicesSet.add(v);
            } else if (v == targetDevice) {
                impactedDevicesSet.add(u);
            }
        }

        // Set to store additional affected devices
        Set<Integer> additionalAffectedDevices = new HashSet<>();
        
        // Identify additional affected devices based on neighbors of impacted devices
        for (int affectedDevice : impactedDevicesSet) {
            for (int neighbor : edgeMap.get(affectedDevice)) {
                if (!impactedDevicesSet.contains(neighbor)) {
                    additionalAffectedDevices.add(neighbor);
                }
            }
        }

        // Combine impacted devices and additional affected devices
        impactedDevicesSet.addAll(additionalAffectedDevices);
        impactedDevicesSet.remove(targetDevice); // Remove the target device from the result

        return new ArrayList<>(impactedDevicesSet);
    }

    // Depth First Search (DFS) to find connections causing impacts
    public void dfs(int curr, int prev) {
        disc[curr] = low[curr] = time++;
        for (int next : edgeMap.get(curr)) {
            if (next == prev)
                continue;
            if (disc[next] == 0) {
                dfs(next, curr);
                low[curr] = Math.min(low[curr], low[next]);
                if (low[next] > disc[curr])
                    ans.add(Arrays.asList(curr, next));
            } else {
                low[curr] = Math.min(low[curr], disc[next]);
            }
        }
    }

    // Main method to demonstrate the functionality
    public static void main(String[] args) {
        Q5B q5B = new Q5B();

        int n = 8;
        List<List<Integer>> connections = new ArrayList<>();
        connections.add(Arrays.asList(0, 1));
        connections.add(Arrays.asList(0, 2));
        connections.add(Arrays.asList(1, 3));
        connections.add(Arrays.asList(1, 6));
        connections.add(Arrays.asList(2, 4));
        connections.add(Arrays.asList(4, 6));
        connections.add(Arrays.asList(4, 5));
        connections.add(Arrays.asList(5, 7));

        int targetDevice = 4;

        List<Integer> impactedDevices = q5B.findImpactedDevices(n, connections, targetDevice);

        System.out.println("Impacted Devices (other than target device " + targetDevice + "): " + impactedDevices);
    }
}
