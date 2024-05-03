
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
//import javax.swing.border.CompoundBorder;
//import javax.swing.border.EmptyBorder;
//import javax.swing.border.LineBorder;
//import javax.swing.border.Border;

public class MazeSolve extends JFrame implements ActionListener{
    private int StartY;
    private int StartX;
    private int EndY; 
    private int EndX;

    //private int iteratorI = 0; 
    //private int iteratorJ = 0;

    private int MAZE_BUTTON_SIZE = 1; 
    private int MAX_BUTTON_WIDTH = 1;
    private int MAX_BUTTON_HEIGHT = 1;

    private boolean START_CHANGE = false; 
    private boolean END_CHANGE = false; 

    private JButton changeStart; 
    private JButton changeEnd;
    private JButton solve; 

    //private JButton[][] buttonArray; 

    //private JButton[][] mazeButtonArray;
    private void findStartAndEnd (char[][] array, int height, int width){
        for(int i = 0; i < height; i++){
            for(int j = 0; j <= width; j++){
                if(array[i][j] == 'P'){
                    StartX = j; 
                    StartY = i;
                }
                if(array[i][j] == 'K'){
                    EndX = j; 
                    EndY = i;
                }
            }
        }

    }
    private int ifPath(char[][] array, int x, int y, int width, int height){
        int count = 0;
        if ((x == width || x == 0) && (y == height || y == 0)){
            return count = 0; 
        }
        if(x == width){
            if(array[y][x-1] == ' '){
                count++; 
            }
        } else if (x == 0){
            if(array[y][x+1] == ' '){
                count++; 
            }
        } else if (y == height){
            if(array[y-1][x] == ' '){
                count++; 
             }
        } else if (y == 0){
            if(array[y+1][x] == ' '){
                count++; 
            }
        } else {
            if(array[y][x+1] == ' '){
                count++; 
            }
            if(array[y][x-1] == ' '){
                count++; 
            }
            if(array[y+1][x] == ' '){
                count++; 
            }
            if(array[y-1][x] == ' '){
               count++; 
            }
        }
        return count; 
    }

    private boolean ifFree(int x, int y){
        boolean free = true; 

        if((x == StartX && y == StartY) || (x == EndX && y == EndY)){
            free = false; 
        }

        return free; 
    }

    public void initMazeSolveFrame (char[][] array, int height, int width){

        findStartAndEnd(array, height, width);
        
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JPanel mazeBoard = new JPanel(new GridLayout(height, width));
        //Border border = new CompoundBorder(new LineBorder(Color.BLACK),new EmptyBorder(4,4,4,4));
        
        //add(mazeBoard);
        JPanel buttonPanel = new JPanel(new GridLayout(0, 3));

        container.add(mazeBoard);
        container.add(buttonPanel);
        
        //mazeButtonArray = new JButton[height][width];
        //int iteratorI = 0; 
        //int iteratorJ = 0; 

        /*for (int i = 0; i < height; i++) {
            for (int j = 0; j <= width; j++) {
                
                final int finalI = i;
                final int finalJ = j;

                JButton button = new JButton();

                button.setPreferredSize(new Dimension(MAZE_BUTTON_SIZE, MAZE_BUTTON_SIZE));

                if(array[i][j] == 'X'){
                    button.setBackground(Color.black);
                } else if (array[i][j] == ' '){
                    button.setBackground(Color.white);
                } else if (array[i][j] == 'P'){
                    button.setBackground(Color.green);
                } else {
                    button.setBackground(Color.red);
                }
                
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //action to perform
                        if (START_CHANGE == true){

                            array[StartY][StartX] = 'X';
                            array[finalI][finalJ] = 'P';

                            StartY = finalI;
                            StartX = finalJ; 
                            
                            START_CHANGE = false;
                            
                        }
                        if (END_CHANGE == true){

                            array[EndY][EndX] = 'X';
                            array[finalI][finalJ] = 'K';

                            EndY = finalI; 
                            EndX = finalJ; 

                            END_CHANGE = false; 
                        }
                    }
                });
                
 
                mazeBoard.add(button); 
            }
        }*/

        initMazeGrid(array, height, width, mazeBoard);

        solve = new JButton("Solve");
        solve.setPreferredSize(new Dimension(MAX_BUTTON_HEIGHT, MAX_BUTTON_WIDTH));
        //solve.setMaximumSize(new Dimension(MAX_BUTTON_WIDTH, MAX_BUTTON_HEIGHT));
        buttonPanel.add(solve);

        changeStart = new JButton("Select Start");
        changeStart.setPreferredSize(new Dimension(MAX_BUTTON_HEIGHT, MAX_BUTTON_WIDTH)); 
        buttonPanel.add(changeStart);
        //button.setBounds();
        changeStart.addActionListener(this);

        changeEnd = new JButton("Select End");
        changeEnd.setPreferredSize(new Dimension(MAX_BUTTON_HEIGHT, MAX_BUTTON_WIDTH));
        buttonPanel.add(changeEnd);
        changeEnd.addActionListener(this);

        add(container);
        setTitle("Labirynth App");
        setSize((10*width), (10*height + 100)); 
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        getContentPane().setBackground(Color.lightGray);

        System.out.println(array[height-1][width]);

    }

    public void bfs(char[][] array, int height, int width){

        int[][] queue = new int[height*width][1];

        int xPos = -1; 
        int yPos = -1; 

        if(StartX == 0){
            xPos = StartX + 1; 
            yPos = StartY;
        } else if(StartX == width){
            xPos = StartX - 1; 
            yPos = StartY;
        } else if(StartY == 0){
            xPos = StartX; 
            yPos = StartY + 1;
        } else if(StartY == height){
            xPos = StartX; 
            yPos = StartY - 1;
        } else {
            xPos = StartX;
            yPos = StartY; 
        }

        while (xPos != EndX && yPos != EndY){
            START_CHANGE = false; 
            END_CHANGE = false; 

        }
    }

    public void initMazeGrid(char[][] array, int height, int width, JPanel mazeBoard){
        for (int i = 0; i < height; i++) {
            for (int j = 0; j <= width; j++) {
                
                final int finalI = i;
                final int finalJ = j;

                JButton button = new JButton();

                button.setPreferredSize(new Dimension(MAZE_BUTTON_SIZE, MAZE_BUTTON_SIZE));

                if(array[i][j] == 'X'){
                    button.setBackground(Color.black);
                } else if (array[i][j] == ' '){
                    button.setBackground(Color.white);
                } else if (array[i][j] == 'P'){
                    button.setBackground(Color.green);
                } else if (array[i][j] == 'K'){
                    button.setBackground(Color.red);
                } else {
                    button.setBackground(Color.MAGENTA);
                }
                
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //action to perform
                        if (START_CHANGE == true && ifPath(array, finalJ, finalI, width, height) != 0 && ifFree(finalJ, finalI) == true){

                            array[StartY][StartX] = 'X';
                            array[finalI][finalJ] = 'P';

                            StartY = finalI;
                            StartX = finalJ; 
                            
                            START_CHANGE = false;

                            mazeBoard.removeAll();
                            
                            initMazeGrid(array, height, width, mazeBoard);

                            mazeBoard.revalidate();
                            mazeBoard.repaint();
                        }
                        if (END_CHANGE == true && ifPath(array, finalJ, finalI, width, height) != 0  && ifFree(finalJ, finalI) == true){

                            array[EndY][EndX] = 'X';
                            array[finalI][finalJ] = 'K';

                            EndY = finalI; 
                            EndX = finalJ; 

                            END_CHANGE = false; 

                            mazeBoard.removeAll();

                            initMazeGrid(array, height, width, mazeBoard);

                            mazeBoard.revalidate();
                            mazeBoard.repaint();
                        }
                    }
                });
                
 
                mazeBoard.add(button); 
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()== changeStart){
            START_CHANGE = true; 
        }
        if(e.getSource()== changeEnd){
            END_CHANGE = true; 
        }
        if(e.getSource()== solve){
            //bfs 
        }
    }
}
