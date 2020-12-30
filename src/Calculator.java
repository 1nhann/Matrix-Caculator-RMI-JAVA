import java.rmi.*;

/**
 * The remote interface for a simple MatrixCalculator
 * @version 1.10 2020-6-29
 * @author inhann
 */
public interface Calculator extends Remote{
    Matrix add(Matrix a , Matrix b) throws RemoteException;
    Matrix min(Matrix a , Matrix b) throws RemoteException;
    Matrix mul(Matrix a , double b) throws RemoteException;
    Matrix mul(Matrix a , Matrix b) throws RemoteException;
}

