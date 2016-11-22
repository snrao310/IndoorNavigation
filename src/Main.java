import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by S N Rao on 11/18/2016.
 */
public class Main {

    private static final int heightOfPlan=16;
    private static final int widthOfPlan=13;
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";



    public static void main(String[] args){

        //Matrix to store the floorplan
        FloorCell plan[][]=new FloorCell[heightOfPlan][widthOfPlan];

        //read plan from file and store in matrix in required form
        ProcessData(plan);


        //create graph from matrix
        Graph graph = createGraph(plan);


        //Ashish's part: get the source and destination.


        //get path from source to destination using the graph
        FloorCell path[]= graph.findPath(plan[9][2], FloorCell.CellType.STAIRS,-1);


        //print the path
        for(FloorCell k: path){
                System.out.println(k.type+"["+k.row+","+k.column+"]");
        }


        //print path in the plan
        printPath(plan, path);
    }




    public static void printPath(FloorCell plan[][], FloorCell path[]){
        for(int i=0;i<plan.length;i++){
            for(int j=0;j<plan[i].length;j++){
                if(Arrays.asList(path).contains(plan[i][j]))
                    System.out.print(ANSI_RED);
                FloorCell.CellType cellType=plan[i][j].type;
                switch (cellType){
                    case PATH:
                        System.out.print(" P  ");
                        break;
                    case WASHROOM:
                        if(plan[i][j].identifier==1)
                            System.out.print(" RM ");
                        else
                            System.out.print(" RF ");
                        break;
                    case COMMONAREA:
                        System.out.print(" CA ");
                        break;
                    case STAIRS:
                        System.out.print(" S  ");
                        break;
                    case ELEVATOR:
                        System.out.print(" E"+plan[i][j].identifier+" ");
                        break;
                    case BOUNDARY:
                        System.out.print("||||");
                        break;
                    default:
                        System.out.print(" C"+plan[i][j].identifier+" ");
                }
                System.out.print(ANSI_RESET);
            }
            System.out.println();
        }
    }



    public static void ProcessData(FloorCell[][] plan){

        //read from file and store data in the matrix
        try {
            BufferedReader br = new BufferedReader(new FileReader("/home/snrao/IDE/IntelliJProjects/IndoorNavigation/src/plan.txt"));
            String line = br.readLine();
            int elvNumber=1;

            System.out.println("C=Common Area, P=Path, RM=Restroom Male, RF=Restroom Female, CX=Classroom X, S=Stairs, E=Elevator");
            int i=0,j=0;
            while (line != null) {
                j=0;
                Boolean elevatorYes=false;
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
                            cell.identifier=elvNumber;
                            elevatorYes=true;
                            System.out.print(" E"+elvNumber+" ");
                            break;
                        case 'B':
                            cell.type= FloorCell.CellType.BOUNDARY;
                            cell.identifier=-1;
                            System.out.print("||||");
                            if(j!=0 && plan[i][j-1].type== FloorCell.CellType.ELEVATOR)
                                elvNumber++;
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
                if(elevatorYes)
                    elvNumber++;
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
