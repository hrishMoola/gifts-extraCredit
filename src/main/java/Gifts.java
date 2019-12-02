import java.util.*;

public class Gifts {
    private static Map<String, Node> allNodes = new HashMap<>();
    private static Set<String> visited = new HashSet<>();
    private static List<String> cycleEdges = new ArrayList<>();
    private static List<String> finalAssignments = new ArrayList<>();

    static class Node{
        String id;
        Integer gifts = 0;
        List<Node> listNodes;

        public Node(String id) {
            this.id = id;
            this.listNodes = new ArrayList<>();
        }

        public Node() {

        }

        public Integer getGifts() {
            return gifts;
        }

        public void setGifts(Integer gifts) {
            this.gifts += gifts;
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
            listNodes.forEach(node -> sb.append(node.getId()).append(" (" + node.getGifts() +") "));
            return "Person " + id + " has friends : " + sb.toString() + " ";
        }
    }

    public static void main(String[] args) {
//        addPerson("1","2,4");
        addPerson("1","2,4,5,6,7");
        addPerson("2","3,4,7");
        addPerson("3","4");
        addPerson("4","5");
        System.out.println(allNodes.values());

        //remove all cycles first
        for(String person : allNodes.keySet()){
            boolean cycle = findCycles(person, "");
            if(cycle){
                finalAssignments.add(cycleEdges.toString());
                cycleEdges.forEach(edge -> {
                    String friend1 = edge.split("->")[0];
                    String friend2 = edge.split("->")[1];
                    if(!friend1.equals("")){
                        removeEdge(friend1, friend2);
                    }
                });
            }
            visited.clear();
            cycleEdges.clear();
        }
        System.out.println(finalAssignments);
        System.out.println("Any cycles present have been removed");
        System.out.println(allNodes.values());

        //gonna get greedy now and assign
        Queue<Node> bfsQueue = new LinkedList<>();
        List<Node> remainingNodes = new ArrayList<>(allNodes.values());
        bfsQueue.add(remainingNodes.get(0));

        while(!bfsQueue.isEmpty()){
            Node person = bfsQueue.remove();
            List<Node> friends = person.getFriends();
            List<String> assignments = new ArrayList<>();
            for (Node friend : friends) {
                if (person.getGifts() == 0 || person.getGifts() == -1) {
                    assignments.add(person.getId() + "->" + friend.getId());
                    person.setGifts(1);
                    friend.setGifts(-1);
                    friend.getFriends().remove(person);
                } else if (person.getGifts() == 1) {
                    assignments.add(friend.getId() + "->" + person.getId());
                    person.setGifts(-1);
                    friend.setGifts(1);
                    friend.getFriends().remove(person);
                }
                bfsQueue.add(friend);
            }
            finalAssignments.add(assignments.toString());
        }
        System.out.println(finalAssignments);
        System.out.println(remainingNodes);

        //voila!
    }

    private static void removeEdge(String friend1, String friend2) {
        allNodes.get(friend1).getFriends().remove(allNodes.get(friend2));
        allNodes.get(friend2).getFriends().remove(allNodes.get(friend1));
    }

    private static boolean findCycles(String person, String parent) {
        visited.add(person);
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
        cycleEdges.remove(parent + "->" +person);
        return false;
    }

    private static void addPerson(String person, String friends) {
        if(!allNodes.containsKey(person)){
            allNodes.put(person, new Node(person));
        }

        for (String fatFriend : friends.split(",")) {
            String friend = fatFriend.trim();
            if(!allNodes.containsKey(friend)){
                allNodes.put(friend, new Node(friend));
            }
            allNodes.get(person).addFriend(friend);
            allNodes.get(friend).addFriend(person);
        }
    }

}
