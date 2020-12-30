import java.io.*;

/**
 * This is the Matrix class , operations are designed to handle it.
 * @version 1.10 2020-6-29
 * @author inhann
 */
public class Matrix implements Serializable{
    private int row;
    private int column;
    private double[][] values;
    public Matrix(){}
    public Matrix(double[][] values){
        this.values = values;
        row = values.length;
        column = values[0].length;
    }

    public double[][] getValues(){
        return values;
    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }
}