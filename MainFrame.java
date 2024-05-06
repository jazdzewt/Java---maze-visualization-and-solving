import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class MainFrame extends JFrame implements ActionListener{

    private int MAX_LINE_BUFFER = 1024; 

    private JButton file_button;
    private File file;  
    public char[][] array;

    public int MAZE_HEIGHT;
    public int MAZE_WIDTH; 

    public void init(){

        JPanel panel = new JPanel();

        file_button = new JButton("Select File");

        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));
        panel.add(file_button);

        file_button.addActionListener(this);

        panel.add(file_button);

        add(panel, BorderLayout.CENTER);
        setTitle("Labirynth App");
        setSize(220, 110); 
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        getContentPane().setBackground(Color.LIGHT_GRAY);        
    }

    public void readToArray (File file) throws FileNotFoundException{
        
        array = new char[MAX_LINE_BUFFER][MAX_LINE_BUFFER];

        Scanner in = new Scanner(file);

        MAZE_HEIGHT = 0; 
        MAZE_WIDTH = 0; 

        while (in.hasNextLine()) {
            String currentLine = in.nextLine().trim(); 
            for (int i = 0; i < currentLine.length(); i++) {
                array[MAZE_HEIGHT][i] = currentLine.charAt(i);
                MAZE_WIDTH = i;   
            }
            MAZE_HEIGHT++;
        }
        
        in.close();
        
        MazeSolve mazeSolve = new MazeSolve(); 
        mazeSolve.initMazeSolveFrame(array, MAZE_HEIGHT, MAZE_WIDTH); 

    }

    private void binToArray(File file) throws IOException{

        FileInputStream file2 = new FileInputStream(file);
        DataInputStream in = new DataInputStream(file2);

        in.skipBytes(5); 

        byte columnsByte1 = (byte) in.readByte();
        byte columnsByte2 = (byte) in.readByte();

        int columns = (columnsByte2 << 8) | columnsByte1;//reader.read();
        

        byte rowsByte1 = in.readByte();
        byte rowsByte2 = in.readByte();

        int rows = (rowsByte2 << 8) | rowsByte1;//reader.read();

        array = new char[rows+1][columns+1];


        byte entryXByte1 = (byte) in.readByte();
        byte entryXByte2 = (byte) in.readByte();

        int entryX = (entryXByte2 << 8) | entryXByte1;//reader.read();
        

        byte entryYByte1 = in.readByte();
        byte entryYByte2 = in.readByte();

        int entryY = (entryYByte2 << 8) | entryYByte1;//reader.read();

        array[entryY-1][entryX-1] = 'P'; 

        byte exitXByte1 = in.readByte();
        byte exitXByte2 = in.readByte();

        int exitX = (exitXByte2 << 8) | exitXByte1;

        byte exitYByte1 = in.readByte(); 
        byte exitYByte2 = in.readByte();

        int exitY = (exitYByte2 << 8) | exitYByte1;

        array[exitY-1][exitX-1] = 'K';

        in.skipBytes(12);

        //byte countByte1 = in.readByte(); 
        //byte countByte2 = in.readByte();
        //byte countByte3 = in.readByte();
        //byte countByte4 = in.readByte();

        //int count = (countByte4 << 24) | (countByte3 << 16) | (countByte2 << 8) | countByte1;

        in.skipBytes(4);  
        
        char separator = (char) in.readByte(); 

        //char wall = (char) in.readByte(); 

        //char path = (char) in.readByte(); 

        char currentChar; 
        int charCount; 

        int occuranceCount = 0; 

        separator = (char) in.readByte();
        currentChar = (char) in.readByte();
        charCount = in.readByte(); 

        for(int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                if(array[i][j] == 'K' || array[i][j] == 'P'){
                    j++; 
                } else {
                    array[i][j] = currentChar;
                    occuranceCount++;  
                }
                if(occuranceCount == charCount){
                    separator = (char) in.readByte();
                    currentChar = (char) in.readByte();
                    charCount = in.readByte();
                    occuranceCount = 0; 
                }

            }
        } 

        MAZE_HEIGHT = rows-1; 
        MAZE_WIDTH = columns-1; 

        MazeSolve mazeSolve = new MazeSolve(); 
        mazeSolve.initMazeSolveFrame(array, MAZE_HEIGHT, MAZE_WIDTH);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==file_button){

            JFileChooser fileChooser = new JFileChooser();

            int response = fileChooser.showOpenDialog(null); 

            if(response == JFileChooser.APPROVE_OPTION){

                //tutaj wczytac plik 
                file = new File(fileChooser.getSelectedFile().getAbsolutePath());

                try {
                    if(getFileExtension(file).equals("txt")){
                        readToArray(file);
                    } else if (getFileExtension(file).equals("bin")){
                        binToArray(file);

                    } else {
                        JOptionPane.showMessageDialog(null, "Wrong File Type!");
                    }

                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }

    private String getFileExtension(File file2) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }    
}
