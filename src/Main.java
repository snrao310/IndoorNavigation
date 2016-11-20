import java.io.*;
import java.util.HashMap;

/**
 * Created by S N Rao on 11/18/2016.
 */
public class Main {

    private static final int sizeOfPlan=14;



    public static void main(String[] args){

        //Matrix to store the floorplan
        FloorCell plan[][]=new FloorCell[sizeOfPlan][sizeOfPlan];

        //read plan from file and store in matrix in required form
        ProcessData(plan);


        //create graph from matrix
        Graph graph = createGraph(plan);


        //Ashish's part: get the source and destination.


        //get path from source to destination using the graph
        FloorCell path[]= graph.findPath(plan[1][1], FloorCell.CellType.WASHROOM);


        //print the path
        for(FloorCell k: path){
                System.out.println(k.type+"["+k.row+","+k.column+"]");
        }
    }






    public static void ProcessData(FloorCell[][] plan){

        //read from file and store data in the matrix
        try {
            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\S N Rao\\IdeaProjects\\IndoorNavigation\\src\\plan.txt"));
            String line = br.readLine();

            System.out.println("C=Common Area, P=Path, RM=Restroom Male, RF=Restroom Female, CX=Classroom X, S=Stairs, E=Elevator");
            int i=0,j=0;
            while (line != null) {
                j=0;
                for(char c: line.toCharArray()){
                    FloorCell cell=new FloorCell();

                    switch (c){
                        case 'P':
                            cell.type= FloorCell.CellType.PATH;
                            cell.identifier=-1;
                            System.out.print(" P  ");
                            break;
                        case 'M':
                            cell.type= FloorCell.CellType.WASHROOM;
                            cell.identifier=1;
                            System.out.print(" RM ");
                            break;
                        case 'F':
                            cell.type= FloorCell.CellType.WASHROOM;
                            cell.identifier=0;
                            System.out.print(" RF ");
                            break;
                        case 'C':
                            cell.type= FloorCell.CellType.COMMONAREA;
                            cell.identifier=-1;
                            System.out.print(" CA ");
                            break;
                        case 'S':
                            cell.type= FloorCell.CellType.STAIRS;
                            cell.identifier=-1;
                            System.out.print(" S  ");
                            break;
                        case 'E':
                            cell.type= FloorCell.CellType.ELEVATOR;
                            cell.identifier=-1;
                            System.out.print(" E  ");
                            break;
                        case 'B':
                            cell.type= FloorCell.CellType.BOUNDARY;
                            cell.identifier=-1;
                            System.out.print("||||");
                            break;
                        default:
                            cell.type= FloorCell.CellType.CLASSROOM;
                            cell.identifier=Character.getNumericValue(c);
                            System.out.print(" C"+Character.getNumericValue(c)+" ");
                    }

                    cell.row=i;
                    cell.column=j;
                    plan[i][j]=cell;
                    j++;
                }
                line = br.readLine();
                i++;
                System.out.println();
            }
        }catch(FileNotFoundException ex){
            System.out.println("Not Found");
        }catch(IOException ex){
            System.out.println("IOEXception");
        }
    }





    public static Graph createGraph(FloorCell[][] plan){
        Graph graph=new Graph();

        //create node for all types except boundary
        for(FloorCell[] cellArray: plan){
            for(FloorCell cell: cellArray){
                if(cell.type== FloorCell.CellType.BOUNDARY)
                    continue;

                graph.addNode(cell);
            }
        }

        //connect each node with all surrounding nodes except boundary nodes and null nodes (for last nodes in row/column)
        for(FloorCell[] cellArray: plan){
            for(FloorCell cell: cellArray){
                if(cell.type== FloorCell.CellType.BOUNDARY)
                    continue;

                FloorCell cellUp=plan[cell.row][cell.column-1];
                FloorCell cellDown=plan[cell.row][cell.column+1];
                FloorCell cellRight=plan[cell.row+1][cell.column];
                FloorCell cellLeft=plan[cell.row-1][cell.column];
                if(cellUp!=null && cellUp.type!= FloorCell.CellType.BOUNDARY)
                    graph.addEdge(cell, cellUp);
                if(cellDown!=null && cellDown.type!= FloorCell.CellType.BOUNDARY)
                    graph.addEdge(cell, cellDown);
                if(cellRight!=null && cellRight.type!= FloorCell.CellType.BOUNDARY)
                    graph.addEdge(cell, cellRight);
                if(cellLeft!=null && cellLeft.type!= FloorCell.CellType.BOUNDARY)
                    graph.addEdge(cell, cellLeft);
            }
        }
        return graph;
    }
}
