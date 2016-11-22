import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by S N Rao on 11/18/2016.
 */
public class Main {

    private static final int heightOfPlan = 18;
    private static final int widthOfPlan = 15;
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";


    public static void main(String[] args) {

        //Matrix to store the floorplan
        FloorCell plan[][] = new FloorCell[heightOfPlan][widthOfPlan];

        //read plan from file and store in matrix in required form
        ProcessData(plan);


        //create graph from matrix
        Graph graph = createGraph(plan);

        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter a qyery: ");
        String s = reader.nextLine();

        //Ashish's part: get the source and destination.


        //get path from source to destination using the graph
        FloorCell path[] = graph.findPath(plan[2][2], FloorCell.CellType.STAIRS, -1);


        //print the path
        for (FloorCell k : path) {
            System.out.println(k.type + "[" + k.row + "," + k.column + "]");
        }


        //print path in the plan
        printPath(plan, path);


        //path to language
        String directions = getEnglishDirections(plan, path);
        System.out.println(directions);
    }


    public static String getEnglishDirections(FloorCell plan[][], FloorCell path[]) {
        String result = "";
        if (path == null)
            return "No path. Directions";

        FloorCell before1 = null;
        FloorCell before2 = null;
        int firstPath = 0;
        boolean moveOut=false;

        //get out of room if needed
        FloorCell getOutOf = path[0];
        if (getOutOf.type!= FloorCell.CellType.PATH){
            moveOut=true;
            result += "Move out of " + getOutOf.type + ((getOutOf.identifier != -1) ? " " + getOutOf.identifier : "");
        }

        while (getOutOf.type != FloorCell.CellType.PATH) {
            firstPath++;
            before2 = before1;
            before1 = getOutOf;
            getOutOf = path[firstPath];
        }

        boolean right = false;
        boolean left = false;
        int leftCount = 0;
        int rightCount = 0;
        for (int i = firstPath; i < path.length; i++) {
            FloorCell curr = path[i];
            left = right = false;
            FloorCell left1;
            FloorCell left2;
            FloorCell right1;
            FloorCell right2;

            if (before1 != null && before2 != null && before1.type== FloorCell.CellType.PATH) {
                if (before1.row == before2.row + 1) {
                    if (curr.column == before1.column - 1)
                        right = true;
                } else if (before1.row == before2.row - 1) {
                    if (curr.column == before1.column + 1)
                        right = true;
                } else if (before1.column == before2.column + 1) {
                    if (curr.row == before1.row + 1)
                        right = true;
                } else if (before1.column == before2.column - 1) {
                    if (curr.row == before1.row - 1)
                        right = true;
                }


                if (before1.row == before2.row + 1 && before1.type== FloorCell.CellType.PATH) {
                    if (curr.column == before1.column + 1)
                        left = true;
                } else if (before1.row == before2.row - 1) {
                    if (curr.column == before1.column - 1)
                        left = true;
                } else if (before1.column == before2.column + 1) {
                    if (curr.row == before1.row - 1)
                        left = true;
                } else if (before1.column == before2.column - 1) {
                    if (curr.row == before1.row + 1)
                        left = true;
                }

                if (curr.row == before1.row + 1) {
                    left1 = plan[curr.row][curr.column + 1];
                    left2 = plan[curr.row][curr.column + 2];
                    right1 = plan[curr.row][curr.column - 1];
                    right2 = plan[curr.row][curr.column - 2];

                    if (left1.type == left2.type && left1.type == FloorCell.CellType.PATH && (i==path.length-1 || left1!=path[i+1]))
                        leftCount++;
                    if (right1.type == right2.type && right1.type == FloorCell.CellType.PATH && (i==path.length-1 || right1!=path[i+1]))
                        rightCount++;
                } else if (curr.row == before1.row - 1) {
                    left1 = plan[curr.row][curr.column - 1];
                    left2 = plan[curr.row][curr.column - 2];
                    right1 = plan[curr.row][curr.column + 1];
                    right2 = plan[curr.row][curr.column + 2];

                    if (left1.type == left2.type && left1.type == FloorCell.CellType.PATH && (i==path.length-1 || left1!=path[i+1]))
                        leftCount++;
                    if (right1.type == right2.type && right1.type == FloorCell.CellType.PATH && (i==path.length-1 || right1!=path[i+1]))
                        rightCount++;
                } else if (curr.column == before1.column + 1) {
                    left1 = plan[curr.row - 1][curr.column];
                    left2 = plan[curr.row - 2][curr.column];
                    right1 = plan[curr.row + 1][curr.column];
                    right2 = plan[curr.row + 2][curr.column];

                    if (left1.type == left2.type && left1.type == FloorCell.CellType.PATH && (i==path.length-1 || left1!=path[i+1]))
                        leftCount++;
                    if (right1.type == right2.type && right1.type == FloorCell.CellType.PATH && (i==path.length-1 || right1!=path[i+1]))
                        rightCount++;
                } else if (curr.column == before1.column - 1) {
                    left1 = plan[curr.row + 1][curr.column];
                    left2 = plan[curr.row + 2][curr.column];
                    right1 = plan[curr.row - 1][curr.column];
                    right2 = plan[curr.row - 2][curr.column];

                    if (left1.type == left2.type && left1.type == FloorCell.CellType.PATH && (i==path.length-1 || left1!=path[i+1]))
                        leftCount++;
                    if (right1.type == right2.type && right1.type == FloorCell.CellType.PATH && (i==path.length-1 || right1!=path[i+1]))
                        rightCount++;
                }


            }

            before2 = before1;
            before1 = curr;

            if (right) {
                if(moveOut)
                    result += ", Take a right and go straight";

                else if(rightCount==0)
                    result+=", Take the first right";

                else if(rightCount==1)
                    result+=". After "+ rightCount + " right, take a right and go straight";

                else {

                    if (rightCount !=0)
                        result += ". After "+ rightCount + " rights, take a right and go straight";
                }
                rightCount = leftCount = 0;
                moveOut=false;
            }

            if (left) {
                if(moveOut)
                    result += ", Take a left and go straight";

                else if(leftCount==0)
                    result+=", Take the first left";

                else if(leftCount==1)
                    result+=". After "+ rightCount + " left, take a left and go straight";


                else {
                        if (leftCount !=0)
                            result += ". After "+ leftCount + " lefts, take a left and go straight";
                }
                rightCount = leftCount = 0;
                moveOut=false;
            }
        }


        return result += " into your destination";
    }


    public static void printPath(FloorCell plan[][], FloorCell path[]) {
        for (int i = 0; i < plan.length; i++) {
            for (int j = 0; j < plan[i].length; j++) {
                if (Arrays.asList(path).contains(plan[i][j]))
                    System.out.print(ANSI_RED);
                FloorCell.CellType cellType = plan[i][j].type;
                switch (cellType) {
                    case PATH:
                        System.out.print(" P  ");
                        break;
                    case WASHROOM:
                        if (plan[i][j].identifier == 1)
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
                        System.out.print(" E" + plan[i][j].identifier + " ");
                        break;
                    case BOUNDARY:
                        System.out.print("||||");
                        break;
                    default:
                        System.out.print(" C" + plan[i][j].identifier + " ");
                }
                System.out.print(ANSI_RESET);
            }
            System.out.println();
        }
    }


    public static void ProcessData(FloorCell[][] plan) {

        //read from file and store data in the matrix
        try {
            BufferedReader br = new BufferedReader(new FileReader("/home/snrao/IDE/IntelliJProjects/IndoorNavigation/src/plan.txt"));
            String line = br.readLine();
            int elvNumber = 1;

            System.out.println("C=Common Area, P=Path, RM=Restroom Male, RF=Restroom Female, CX=Classroom X, S=Stairs, E=Elevator");
            int i = 0, j = 0;
            while (line != null) {
                j = 0;
                Boolean elevatorYes = false;
                for (char c : line.toCharArray()) {
                    FloorCell cell = new FloorCell();

                    switch (c) {
                        case 'P':
                            cell.type = FloorCell.CellType.PATH;
                            cell.identifier = -1;
                            System.out.print(" P  ");
                            break;
                        case 'M':
                            cell.type = FloorCell.CellType.WASHROOM;
                            cell.identifier = 1;
                            System.out.print(" RM ");
                            break;
                        case 'F':
                            cell.type = FloorCell.CellType.WASHROOM;
                            cell.identifier = 0;
                            System.out.print(" RF ");
                            break;
                        case 'C':
                            cell.type = FloorCell.CellType.COMMONAREA;
                            cell.identifier = -1;
                            System.out.print(" CA ");
                            break;
                        case 'S':
                            cell.type = FloorCell.CellType.STAIRS;
                            cell.identifier = -1;
                            System.out.print(" S  ");
                            break;
                        case 'E':
                            cell.type = FloorCell.CellType.ELEVATOR;
                            cell.identifier = elvNumber;
                            elevatorYes = true;
                            System.out.print(" E" + elvNumber + " ");
                            break;
                        case 'B':
                            cell.type = FloorCell.CellType.BOUNDARY;
                            cell.identifier = -1;
                            System.out.print("||||");
                            if (j != 0 && plan[i][j - 1].type == FloorCell.CellType.ELEVATOR)
                                elvNumber++;
                            break;
                        default:
                            cell.type = FloorCell.CellType.CLASSROOM;
                            cell.identifier = Character.getNumericValue(c);
                            System.out.print(" C" + Character.getNumericValue(c) + " ");
                    }

                    cell.row = i;
                    cell.column = j;
                    plan[i][j] = cell;
                    j++;
                }
                line = br.readLine();
                if (elevatorYes)
                    elvNumber++;
                i++;
                System.out.println();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Not Found");
        } catch (IOException ex) {
            System.out.println("IOEXception");
        }
    }


    public static Graph createGraph(FloorCell[][] plan) {
        Graph graph = new Graph();

        //create node for all types except boundary
        for (FloorCell[] cellArray : plan) {
            for (FloorCell cell : cellArray) {
                if (cell.type == FloorCell.CellType.BOUNDARY)
                    continue;

                graph.addNode(cell);
            }
        }

        //connect each node with all surrounding nodes except boundary nodes and null nodes (for last nodes in row/column)
        for (FloorCell[] cellArray : plan) {
            for (FloorCell cell : cellArray) {
                if (cell.type == FloorCell.CellType.BOUNDARY)
                    continue;

                FloorCell cellUp = plan[cell.row][cell.column - 1];
                FloorCell cellDown = plan[cell.row][cell.column + 1];
                FloorCell cellRight = plan[cell.row + 1][cell.column];
                FloorCell cellLeft = plan[cell.row - 1][cell.column];
                if (cellUp != null && cellUp.type != FloorCell.CellType.BOUNDARY)
                    graph.addEdge(cell, cellUp);
                if (cellDown != null && cellDown.type != FloorCell.CellType.BOUNDARY)
                    graph.addEdge(cell, cellDown);
                if (cellRight != null && cellRight.type != FloorCell.CellType.BOUNDARY)
                    graph.addEdge(cell, cellRight);
                if (cellLeft != null && cellLeft.type != FloorCell.CellType.BOUNDARY)
                    graph.addEdge(cell, cellLeft);
            }
        }
        return graph;
    }
}
