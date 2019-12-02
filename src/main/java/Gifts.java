import java.lang.reflect.Array;
import java.util.*;

public class Gifts {
    static Map<String, Node> allNodes = new HashMap<>();
    static Set<String> visited = new HashSet<>();
    static List<String> cycleEdges = new ArrayList<>();
    static List<String> finalCycles = new ArrayList<>();

    static class Node{
        String id;
        List<Node> listNodes;

        public Node(String id) {
            this.id = id;
            this.listNodes = new ArrayList<>();
        }

        public Node() {

        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<Node> getFriends() {
            return listNodes;
        }

        public void addFriend(String friendNode) {
            this.listNodes.add(allNodes.get(friendNode));
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            listNodes.forEach(node -> sb.append(node.getId()).append(" "));
            return "Person " + id + " has friends : " + sb.toString() + " ";
        }
    }

    public static void main(String[] args) {
        addPerson("1","2,4");
        addPerson("2","3,4");
        addPerson("3","4");
        addPerson("4","5");
        System.out.println(allNodes.values());
        //remove all cycles first
        for(String person : allNodes.keySet()){
            boolean cycle = findCycles(person, "");
            System.out.println(cycle);
            if(cycle){
                finalCycles.add(cycleEdges.toString());
                cycleEdges.forEach(edge -> {
                    String friend1 = edge.split("->")[0];
                    String friend2 = edge.split("->")[1];
                    if(!friend1.equals("")){
                    allNodes.get(friend1).getFriends().remove(allNodes.get(friend2));
                    allNodes.get(friend2).getFriends().remove(allNodes.get(friend1));
                    }
                });
            }
            visited.clear();
            cycleEdges.clear();
        }
        System.out.println(finalCycles);
        System.out.println(allNodes.values());
    }

    private static void removeEdge(String friend1, String friend2) {

    }

    private static boolean findCycles(String person, String parent) {
        visited.add(person);
        if(person.length() > 0)
            cycleEdges.add(parent + "->" +person);
        for(Node friend: allNodes.get(person).getFriends()){
            if(!visited.contains(friend.getId())){
                if(findCycles(friend.getId(), person)){
                    return true;
                }
            } else if(!friend.getId().equalsIgnoreCase(parent)){
                {
                    cycleEdges.add(person + "->" + friend.getId());
                return true;
                }
            }
        }
        return false;
    }

    private static void addPerson(String person, String friends) {
        if(!allNodes.containsKey(person)){
            allNodes.put(person, new Node(person));
        }

        for (String friend : friends.split(",")) {
            if(!allNodes.containsKey(friend)){
                allNodes.put(friend, new Node(friend));
            }
            allNodes.get(person).addFriend(friend);
            allNodes.get(friend).addFriend(person);
        }
    }

}
