import java.io.*;

/**
 * Created by S N Rao on 11/18/2016.
 */
public class Main {

    private static final int sizeOfPlan=14;

    public static void main(String[] args){
        FloorCell plan[][]=new FloorCell[sizeOfPlan][sizeOfPlan];



        try {
            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\S N Rao\\IdeaProjects\\IndoorNavigation\\src\\plan.txt"));
            String line = br.readLine();

            int i=0,j=0;
            while (line != null) {
                j=0;
                for(char c: line.toCharArray()){
                    FloorCell cell=new FloorCell();

                    switch (c){
                        case 'P':
                            cell.type=1;
                            cell.identifier=-1;
                            System.out.print(" P  ");
                            break;
                        case 'M':
                            cell.type=3;
                            cell.identifier=1;
                            System.out.print(" RM ");
                            break;
                        case 'F':
                            cell.type=3;
                            cell.identifier=0;
                            System.out.print(" RF ");
                            break;
                        case 'C':
                            cell.type=4;
                            cell.identifier=-1;
                            System.out.print(" CA ");
                            break;
                        case 'S':
                            cell.type=5;
                            cell.identifier=-1;
                            System.out.print(" S  ");
                            break;
                        case 'E':
                            cell.type=6;
                            cell.identifier=-1;
                            System.out.print(" E  ");
                            break;
                        case 'B':
                            cell.type=7;
                            cell.identifier=-1;
                            System.out.print("||||");
                            break;
                        default:
                            cell.type=2;
                            cell.identifier=Character.getNumericValue(c);
                            System.out.print(" C"+Character.getNumericValue(c)+" ");
                    }

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
}
