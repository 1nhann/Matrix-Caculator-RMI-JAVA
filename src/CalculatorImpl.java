import java.rmi.server.*;
import java.rmi.*;

/**
 * This class is the implementation for the remote Calculator interface.
 * @version 1.10 2020-6-30
 * @author inhann
 */
public class CalculatorImpl extends UnicastRemoteObject implements Calculator{
    
    public CalculatorImpl() throws Exception{}

    @Override
    public Matrix add(Matrix a, Matrix b) throws RemoteException{
        double[][] valuesA = a.getValues();
        double[][] valuesB = b.getValues();

        int row = a.getRow();
        int column = a.getColumn();

        double[][] values = new double[row][column];

        for (int i = 0 ; i<row ; i++){
            for (int j = 0 ; j<column ; j++){
                values[i][j] = valuesA[i][j] + valuesB[i][j];
            }
        }

        return new Matrix(values);
    }

    @Override
    public Matrix min(Matrix a, Matrix b) throws RemoteException{
        double[][] valuesA = a.getValues();
        double[][] valuesB = b.getValues();

        int row = a.getRow();
        int column = a.getColumn();

        double[][] values = new double[row][column];

        for (int i = 0 ; i<row ; i++){
            for (int j = 0 ; j<column ; j++){
                values[i][j] = valuesA[i][j] - valuesB[i][j];
            }
        }

        return new Matrix(values);
    }

    @Override
    public Matrix mul(Matrix a, double b) throws RemoteException{
        double[][] valuesA = a.getValues();

        int row = a.getRow();
        int column = a.getColumn();

        double[][] values = new double[row][column];

        for (int i = 0 ; i<row ; i++){
            for (int j = 0 ; j<column ; j++){
                values[i][j] = valuesA[i][j] * b;
            }
        }

        return new Matrix(values);
    }

    @Override
    public Matrix mul(Matrix a, Matrix b) throws RemoteException {
        double[][] valuesA = a.getValues();
        double[][] valuesB = b.getValues();

        int row = a.getRow();
        int colomn = b.getColumn();

        int colomnA = a.getColumn();
        int rowB = b.getRow();

        double[][] values = new double[row][colomn];
        for (int i = 0; i < row; i++){
            for (int j = 0; j < colomn;j++){
                for (int h = 0; h < a.getColumn(); h++){
                    values[i][j]+=valuesB[h][j]*valuesA[i][h];
                }
            }
        }

        return new Matrix(values);
    }

}