import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by S N Rao on 11/18/2016.
 */
public class Graph {

    public static class Queue{

        public static class QueueNode{
            FloorCell data;
            QueueNode next;
        }

        public QueueNode front=null;
        public QueueNode rear=null;
        int size=0;

        public void enqueue(FloorCell node){
            size++;
            QueueNode newNode=new QueueNode();
            newNode.data=node;
            newNode.next=null;

            if(rear!=null)
                rear.next=newNode;
            else
                front=newNode;
            rear=newNode;
        }

        public FloorCell dequeue(){
            if(size==0)
                throw new EmptyStackException();

            FloorCell deq=front.data;
            front=front.next;
            if(front==null)
                rear=null;
            size--;
            return deq;
        }

        public FloorCell peek(){
            if(size==0)
                throw new EmptyStackException();
            return front.data;
        }

        public int getSize(){
            return size;
        }

        public boolean isEmpty(){
            return size==0;
        }
    }


    HashMap<FloorCell, LinkedList<FloorCell>> nodes=new HashMap<FloorCell,LinkedList<FloorCell>>();

    public void addNode(FloorCell newNode) {
        nodes.put(newNode, new LinkedList<FloorCell>());
    }

    public void removeNode(FloorCell node) {
        if (nodes.containsKey(node)) {
            nodes.remove(node);
        }
        for(FloorCell i:nodes.keySet()){
            LinkedList ll=nodes.get(i);
            ll.remove(node);
        }
    }

    public void addEdge(FloorCell node1, FloorCell node2) {
        if (nodes.containsKey(node1) && nodes.containsKey(node2)) {
            nodes.get(node1).addLast(node2);
        }
    }

    public void deleteEdge(FloorCell node1, FloorCell node2) {
        if (nodes.containsKey(node1) && nodes.containsKey(node2)) {
            nodes.get(node1).remove(node2);
        }
    }

    public LinkedList getConnectedEdges(FloorCell node) {
        return nodes.get(node);
    }


    //finds the path to the nearest celltype specified.
    public FloorCell[] findPath(FloorCell start, FloorCell.CellType cellType, int identifier) {
        Queue queue = new Queue();
        FloorCell curr=null;
        HashMap<FloorCell, FloorCell> parent = new HashMap<>();
        HashMap<FloorCell, Boolean> visited = new HashMap<>();
        for (FloorCell node : nodes.keySet()) {
            visited.put(node, false);
        }
        queue.enqueue(start);
        visited.replace(start, true);
        parent.put(start, null);

        while (!queue.isEmpty()) {
            curr = queue.dequeue();
            if(curr.type==cellType && (identifier==-1 || curr.identifier==identifier))
                break;
            LinkedList<FloorCell> neighbours = nodes.get(curr);
            for (FloorCell n : neighbours) {
                if (!visited.get(n)) {
                    queue.enqueue(n);
                    visited.replace(n, true);
                    parent.put(n, curr);
                }
            }
        }

        FloorCell temp=curr;
        int i=0;
        while(temp!=null){
            temp=parent.get(temp);
            i++;
        }

        temp=curr;
        int j=0;
        FloorCell path[] = new FloorCell[i];
        while(temp!=null){
            path[i-j-1]=temp;
            temp=parent.get(temp);
            j++;
        }

        return path;
    }

}