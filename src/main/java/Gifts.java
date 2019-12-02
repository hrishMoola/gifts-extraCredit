import java.lang.reflect.Array;
import java.util.*;

public class Gifts {
    static Map<String, Node> allNodes = new HashMap<>();
    static Set<String> visited = new HashSet<>();
    static Set<String> cycleEdges = new HashSet<>();
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
//        for(String person : allNodes.keySet()){
            System.out.println(findCycles("1" , ""));
            System.out.println(visited);
            System.out.println(cycleEdges);

//        }
    }

    private static boolean findCycles(String person, String parent) {
        visited.add(person);
        cycleEdges.add(parent + "->" +person);
        for(Node friend: allNodes.get(person).getFriends()){
            if(!visited.contains(friend.getId())){
                if(findCycles(friend.getId(), person))
                    return true;
            } else if(!friend.getId().equalsIgnoreCase(parent)){
//                cycleEdges.add(person + "->" +parent);
                return true;
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
