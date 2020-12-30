import java.rmi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;
import javax.naming.*;

/**
 * A client that invokes the remote methods the compute on Matrices.
 * @version 1.10 2020-6-30
 * @author inhann
 */
public class CalculatorClient {

    private Calculator calculator;

    public static void main(String[] args) {
        CalculatorClient client = new CalculatorClient();
        client.geCalculator();
        JFrame frame = client.new ClientFrame();
        frame.setTitle("Simple Matrix Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Get the remote object that is aimed to figure out the operations on matrices
     */
    public void geCalculator(){
        try{
            Context namingContext = new InitialContext();
            calculator = (Calculator) namingContext.lookup("rmi://localhost:8189/calculator");
            System.out.println("Calculator got !");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        
    }

    /**
     * Add two matrices whose sizes are the same.
     * @param a the Matrix a
     * @param b the Matrix b
     * @return a Matrix that is the sum of a and b
     * @throws Exception if the size of each matrix are different from each other or there is somthing 
     * wrong with the net connecting or anything else,then throw an Exception 
     */
    public Matrix addMatrix(Matrix a, Matrix b) throws Exception{
        if ((a.getRow()!=b.getRow())||(a.getColumn()!=b.getColumn())){
            throw new Exception("warning : Size Dismatch");
        }
        return calculator.add(a, b);       
    }

    /**
     * Matrix number multiplication
     * @param a the Matrix 
     * @param b the number
     * @return a Matrix that is the result of this operation
     * @throws Exception if there is somthing wrong with the net connecting or anything else,then throw an Exception 
     */
    public Matrix mulMatrix(Matrix a, double b) throws Exception {
        return calculator.mul(a,b);
    }

    /**
     * Matrix mulplication
     * @param a the first Matrix
     * @param b the latter Matrix
     * @return the result of this operation
     * @throws Exception if the column number of Matrix a is not eqaul to the row number
     * of Matrix b the throw the Exception.
     */
    public Matrix mulMatrix(Matrix a, Matrix b) throws Exception{
        if (a.getColumn()!=b.getRow()){
            throw new Exception("warning : Size Dismatch");
        }
        return calculator.mul(a, b);
    }

    /**
     * Matrix subtraction
     * @param a the matrix that is minused
     * @param b the matrix that is substracted from matrix a
     * @return the result of the operation 
     * @throws Exception if the size of each matrix does not match each other then an Exception will be thrown.
     */
    public Matrix minMatrix(Matrix a, Matrix b) throws Exception {
        if ((a.getRow()!=b.getRow())||(a.getColumn()!=b.getColumn())){
            throw new Exception("warning : Size Dismatch");
        }
        return calculator.min(a, b);
    }

    /**
     * Inner class , the GUI for the client.
     */
    class ClientFrame extends JFrame {
        private JTextArea matrixA;
        private JTextArea matrixB;
        private JTextArea matrixC;
        private JButton buttonAplsB;
        private JButton buttonAminB;
        private JButton buttonAmulB;
        private JLabel statusLabel;
        private JPanel matricesPane;
        private JPanel buttonsPane;
        

        public ClientFrame(){
            //INITIALIZE STATUS
            statusLabel = new JLabel("tip:Enter a matrix or number to each textArea.");
            //CREATE MATRICES
            createMatricesPane();
            //CREATE BUTTONS
            createButtonsPane();
            //BIND BUTTONS WITH LISTENERS
            try{
                bindButtonsWithListeners();
            }
            catch(Exception e){
                e.printStackTrace();
            }

            setSize(new Dimension(600,200));
            add(statusLabel,BorderLayout.NORTH);
            add(matricesPane,BorderLayout.CENTER);
            add(buttonsPane,BorderLayout.SOUTH);

            try{
                URL imagUrl = getClass().getResource("Calculator.jpg");
                Image img = new ImageIcon(imagUrl).getImage();
                setIconImage(img);}
            catch(Exception e){
                System.out.println("turn to /bin/client , then  type java CalculatorClient in the terminal to see the image of this app");
            }
        }

        /**
         * Read the matrix from the text area.
         * @param matrixArea the text area where the matrices are lying in
         * @return a Matrix object which owns the values based on the given text
         * @throws Exception
         */
        public Matrix readMatrix(JTextArea matrixArea) throws Exception{
            //DETERMINE THE SIZE VALID AND NOT EMPTY
            String rawText = matrixArea.getText();
            StringTokenizer rowsText = new StringTokenizer(rawText,"\n");

            int rowNum = rowsText.countTokens();

            int[] colomnsNum = new int[rowNum];

            for (int i = 0;i < rowNum;i++) {
                StringTokenizer rowText = new StringTokenizer(rowsText.nextToken());
                colomnsNum[i] = rowText.countTokens();
            }

            if (rowNum==0||colomnsNum[0]==0){
                statusLabel.setText("warning : Can't Find Matrix !");
                throw new Exception("warning : Can't Find Matrix !");
            }

            for (int colomnNum : colomnsNum){
                if (colomnNum != colomnsNum[0]){
                    statusLabel.setText("warning : Invalid Matrix !");
                    throw new Exception("warning : Invalid Matrix !");
                }
            }

            //INITIALIZE THE MATRIX
            int colomnNum = colomnsNum[0];
            Matrix matrix = new Matrix(new double[1][1]);
            rowsText = new StringTokenizer(rawText,"\n");
            try{
                double[][] values = new double[rowNum][colomnNum];
                for (int i = 0; i < rowNum; i++){
                    StringTokenizer rowText = new StringTokenizer(rowsText.nextToken());
                    for (int j = 0; j < colomnNum; j++){
                        values[i][j] = Double.parseDouble(rowText.nextToken());
                    }
                }
                matrix = new Matrix(values);
            }
            catch (Exception e){
                statusLabel.setText("waring : Invalid Number Format !");
                throw new Exception("waring : Invalid Number Format !");
            }

            return matrix;

        }
        
        /**
         * Display a matrix to the text area C , as the result of the operations.
         * @param matrixToDisplay the matrix that need to be represented in matrixC i.e. the text area C
         */
        public void disPlayMatrixToC(Matrix matrixToDisplay){
            StringBuilder rawText = new StringBuilder();
            for (int i = 0; i < matrixToDisplay.getRow(); i++){
                for (int j = 0; j<matrixToDisplay.getColumn(); j++){
                    rawText.append(matrixToDisplay.getValues()[i][j]+"  ");
                }
                rawText.append("\n");
            }
            matrixC.setText(rawText.toString());
        }
        
        /**
         * Represent the matrxi by a pane with a scrollbar and a concerning label
         * @param matrix the text area where the matrix is entered.
         * @param name the name for the pane .
         * @return a JPanel that will ultimately show the matrix
         */
        public JPanel matrixPane(JTextArea matrix,String name){
            JScrollPane scrollPane = new JScrollPane(matrix);
            JLabel nameLabel = new JLabel(name);
            scrollPane.setSize(new Dimension(200,200));
            
            JPanel pane = new JPanel();
            pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
            pane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            pane.add(nameLabel);
            pane.add(scrollPane);

            return pane;
        }

        /**
         * Create the complete pane to show three matrices
         */
        public void createMatricesPane(){
            //CREATE MATRICES 
            matrixA = new JTextArea();
            matrixB = new JTextArea();
            matrixC = new JTextArea();
            JPanel mAPane = matrixPane(matrixA, "Matrix A");
            JPanel mBPane = matrixPane(matrixB, "Matrix B");
            JPanel mCPane = matrixPane(matrixC, "Matrix C");
            matricesPane = new JPanel();
            matricesPane.setLayout(new BoxLayout(matricesPane, BoxLayout.X_AXIS));
            matricesPane.add(mAPane);
            matricesPane.add(mBPane);
            matricesPane.add(mCPane);
        }

        /**
         * Create a pane with three buttons 
         */
        public void createButtonsPane(){
            //CREATE BUTTONS
            buttonAplsB = new JButton("A + B = C");
            buttonAminB = new JButton("A - B = C");
            buttonAmulB = new JButton("A * B = C");

            buttonsPane = new JPanel();
            buttonsPane.setLayout(new GridLayout(1,3));
            buttonsPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            buttonsPane.add(buttonAplsB);
            buttonsPane.add(buttonAminB);
            buttonsPane.add(buttonAmulB);
        }

        /**
         * Bind the three buttons with three individual operations 
         */
        public void bindButtonsWithListeners() {
            //BIND BUTTONS WITH THEIR OWN LISTENERS
            buttonAplsB.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    try{
                        Matrix a = readMatrix(matrixA);
                        Matrix b = readMatrix(matrixB);
                        disPlayMatrixToC(addMatrix(a, b));
                        statusLabel.setText("tip:Enter a matrix or number to each textArea.");

                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                        String warning = ex.getMessage();
                        if (warning!=null){
                            statusLabel.setText(warning);
                        }
                    }
                    
                }
            });

            buttonAminB.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        Matrix a = readMatrix(matrixA);
                        Matrix b = readMatrix(matrixB);
                        disPlayMatrixToC(minMatrix(a,b));
                        statusLabel.setText("tip:Enter a matrix or number to each textArea.");

                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                        String warning = ex.getMessage();
                        if (warning!=null){
                            statusLabel.setText(warning);
                        }
                    }
                }
            });

            buttonAmulB.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        Matrix a = readMatrix(matrixA);
                        Matrix b = readMatrix(matrixB);
                        
                        int aRow = a.getRow();
                        int aColumn = a.getColumn();
                        int bRow = b.getRow();
                        int bColumn = b.getColumn();

                        if ((aRow+aColumn!=2)&&(bRow+bColumn!=2)){
                            disPlayMatrixToC(mulMatrix(a, b));
                            statusLabel.setText("tip:Enter a matrix or number to each textArea.");
                        }
                        else if ((aRow+aColumn==2)&&(bRow+bColumn!=2)){
                            disPlayMatrixToC(mulMatrix(b,a.getValues()[0][0]));
                            statusLabel.setText("tip:Enter a matrix or number to each textArea.");
                        }
                        else{
                            disPlayMatrixToC(mulMatrix(a, b.getValues()[0][0]));
                            statusLabel.setText("tip:Enter a matrix or number to each textArea.");

                        }

                        
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                        String warning = ex.getMessage();
                        if (warning!=null){
                            statusLabel.setText(warning);
                        }
                    }
                }
            });
        }
    }
}