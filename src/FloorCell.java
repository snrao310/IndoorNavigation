/**
 * Created by S N Rao on 11/18/2016.
 */
public class FloorCell {

    /*
    Type of cell

    1: Path
    2: Classroom
    3: Washroom
    4: Common Area
    5: Stairs
    6: Elevator
    7: Boundary
     */
    public int type;



    /*
    Identifier numbers

    Path: -1
    Classroom: Classroom number
    Washroom: 1=male, 0=female
    Common Area: -1
    Stairs: -1
    Elevator: Elevator number
    Boundary: -1
     */
    public int identifier;


    //position in the plan
    public int column;
    public int row;

}
