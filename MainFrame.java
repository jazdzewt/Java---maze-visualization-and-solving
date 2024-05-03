import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
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

        //int height = 0;
        //int width = 0;

        MAZE_HEIGHT = 0; 
        MAZE_WIDTH = 0; 

        while (in.hasNextLine()) {
            String currentLine = in.nextLine().trim(); 
            for (int i = 0; i < currentLine.length(); i++) {
                array[MAZE_HEIGHT][i] = currentLine.charAt(i);
                MAZE_WIDTH = i;  
                //System.out.println(currentLine.length()); 
                //System.out.println(" - " + currentLine.charAt(i));
                //System.out.println(" - " + array[MAZE_HEIGHT][i]);   
            }
            MAZE_HEIGHT++;
        }
        
        in.close();
        
        MazeSolve mazeSolve = new MazeSolve(); 
        mazeSolve.initMazeSolveFrame(array, MAZE_HEIGHT, MAZE_WIDTH); 

    }

    public char[][] returnArray(){
        return array; 
    }

    public int returnHeight(){
        return MAZE_HEIGHT;
    }

    public int returnWidth(){
        return MAZE_WIDTH;
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
                    readToArray(file);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }    
}
