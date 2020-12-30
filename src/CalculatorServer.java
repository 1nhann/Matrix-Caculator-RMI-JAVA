import java.rmi.*;
import java.rmi.registry.*;
import javax.naming.*;

/**
 * This server program instantiates a remote Calculator object, registers it with the 
 * naming service and waits for invocations from clents.
 * @version 1.10 2020-6-30
 * @author inhann
 */
public class CalculatorServer {

    CalculatorImpl server;

    public static void main(String[] args) {
        new CalculatorServer().TurnOn();
    }
    /**
     * Turn on the service.
     */
    public void TurnOn(){
        Registry registry = null;
        try{
            registry = LocateRegistry.createRegistry(8189);
            System.out.println("Constructing server implementation....");
            server = new CalculatorImpl();

            System.out.println("Binding server implementation to registry");
            Naming.bind("rmi://localhost:8189/calculator", server);

            System.out.println("Waiting for invocations from client...");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        
    }
}