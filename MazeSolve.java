
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

class Node {
    int x; 
    int y; 
    Node previous; 
    public Node(int xPos, int yPos, Node previousNode) {
        this.x = xPos; 
        this.y = yPos; 
        this.previous = previousNode; 
    }
}

public class MazeSolve extends JFrame implements ActionListener{
    private char[][] array2; 
    private int height2; 
    private int width2; 

    private int StartY;
    private int StartX;
    private int EndY; 
    private int EndX;

    private int MAZE_BUTTON_SIZE = 1; 
    private int MAX_BUTTON_WIDTH = 1;
    private int MAX_BUTTON_HEIGHT = 1;

    private boolean START_CHANGE = false; 
    private boolean END_CHANGE = false;

    private JButton changeStart; 
    private JButton changeEnd;
    private JButton solve; 
    private JButton clear; 

    private JPanel mazeBoard; 

    private char startChar = 'X';
    private char endChar = 'X'; 

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

        array2 = array;
        width2 = width;
        height2 = height;

        findStartAndEnd(array2, height2, width2);

        System.out.println(array[EndY][EndX]);
        
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        mazeBoard = new JPanel(new GridLayout(height2, width2));

        JPanel buttonPanel = new JPanel(new GridLayout(0, 4));

        container.add(mazeBoard);
        container.add(buttonPanel);
        
        initMazeGrid(array2, height2, width2, mazeBoard);

        solve = new JButton("Solve");
        solve.setPreferredSize(new Dimension(MAX_BUTTON_HEIGHT, MAX_BUTTON_WIDTH));
        buttonPanel.add(solve);
        solve.addActionListener(this);

        changeStart = new JButton("Select Start");
        changeStart.setPreferredSize(new Dimension(MAX_BUTTON_HEIGHT, MAX_BUTTON_WIDTH)); 
        buttonPanel.add(changeStart);
        changeStart.addActionListener(this);

        changeEnd = new JButton("Select End");
        changeEnd.setPreferredSize(new Dimension(MAX_BUTTON_HEIGHT, MAX_BUTTON_WIDTH));
        buttonPanel.add(changeEnd);
        changeEnd.addActionListener(this);

        clear = new JButton("Clear"); 
        clear.setPreferredSize(new Dimension(MAX_BUTTON_HEIGHT, MAX_BUTTON_WIDTH));
        buttonPanel.add(clear); 
        clear.addActionListener(this);

        add(container);
        setTitle("Labirynth App");
        setSize((10*width2), (10*height2 + 100)); 
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        getContentPane().setBackground(Color.lightGray);

    }

    public void enqueue(int[][] queue, int x, int y){
        
        int iterator = 0; 
        while(queue[iterator][0] != 0 && queue[iterator][1] != 0){
            iterator++; 
        }
        queue[iterator][0] = x; 
        queue[iterator][1] = y; 
    }
    public void enqueueNode(Node[] nodeArray, Node prev, int x, int y){

        Node node = new Node(x, y, prev); 
        
        int iterator = 0; 

        while(nodeArray[iterator] != null){
            iterator++; 
        }
        nodeArray[iterator] = node; 
    }

    public int dequeueX (int[][] queue){
        int x = 0; 
        x = queue[0][0]; 
        return x; 
    }
    public int dequeueY(int[][] queue){

        int y = 0; 
        y = queue[0][1];

        int iterator = 1;

        while(queue[iterator][0] != 0 && queue[iterator][1] != 0){
            queue[iterator - 1][0] = queue[iterator][0];
            queue[iterator - 1][1] = queue[iterator][1];
            iterator++; 
        } 
        return y; 
    }
    public Node dequeueNode (Node[] nodeArray){

        Node node = nodeArray[0];

        int iterator = 1; 
        while(nodeArray[iterator] != null){
            nodeArray[iterator - 1] = nodeArray[iterator];
            iterator++;
        }

        return node; 
    }
    public void checkSurroundings(char[][] array, int x, int y, int[][] queue, Node[] nodeArray, Node prevNode){
        if(array[y-1][x] == ' '){
            array[y-1][x] = 'O';
            enqueue(queue, x, y-1);
            enqueueNode(nodeArray, prevNode, x, y-1);  
        }
        if(array[y+1][x] == ' '){
            array[y+1][x] = 'O';
            enqueue(queue, x, y+1);
            enqueueNode(nodeArray, prevNode, x, y+1); 
        }
        if(array[y][x-1] == ' '){
            array[y][x-1] = 'O';
            enqueue(queue, x-1, y);
            enqueueNode(nodeArray, prevNode, x-1, y); 
        }
        if(array[y][x+1] == ' '){
            array[y][x+1] = 'O';
            enqueue(queue, x+1, y);
            enqueueNode(nodeArray, prevNode, x+1, y); 
        }
    }
    public int isEnd(char[][] array, int x, int y){
        int end = 0; 
        if(array[y-1][x] == 'K'){
            end++;
        }
        if(array[y+1][x] == 'K'){
            end++;
        }
        if(array[y][x-1] == 'K'){
            end++;
        }
        if(array[y][x+1] == 'K'){
            end++;
        }

        return end; 
    }

    public void bfs(char[][] array, int height, int width){

        int queueSize = height*width; 

        Node[] nodeQueue = new Node[queueSize];

        int[][] queue = new int[queueSize][2];

        int xPos = -1; 
        int yPos = -1; 

        Node currentNode = new Node (0, 0, null);

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

        array[yPos][xPos] = 'O';
        enqueue(queue, xPos, yPos);
        enqueueNode(nodeQueue, null, xPos, yPos); 

        while (isEnd(array, xPos, yPos) == 0){
            START_CHANGE = false; 
            END_CHANGE = false; 
            xPos = dequeueX(queue);
            yPos = dequeueY(queue);
            currentNode = dequeueNode(nodeQueue);
            checkSurroundings(array, xPos, yPos, queue, nodeQueue, currentNode);
        }

        clearMaze(array2, height2, width2);

        while(currentNode != null){
            System.out.println(currentNode.x + - +currentNode.y);
            array2[currentNode.y][currentNode.x] = 'O';
            currentNode = currentNode.previous; 
        }

        mazeBoard.removeAll();
        initMazeGrid(array, height, width, mazeBoard);
        mazeBoard.revalidate();
        mazeBoard.repaint();
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
                } else if (array[i][j] == 'O'){
                    button.setBackground(Color.MAGENTA);
                }
                
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //action to perform
                        if (START_CHANGE == true && ifPath(array, finalJ, finalI, width, height) != 0 && ifFree(finalJ, finalI) == true){
                            
                            if(startChar == 'X'){
                                array[StartY][StartX] = 'X';
                            } else {
                                array[StartY][StartX] = ' ';
                            }

                            if(array[finalI][finalJ] == 'X'){
                                startChar = 'X'; 
                            } else {
                                startChar = ' '; 
                            }

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

                            if(endChar == 'X'){
                                array[EndY][EndX] = 'X';
                            } else {
                                array[EndY][EndX] = ' ';
                            }

                            if(array[finalI][finalJ] == 'X'){
                                endChar = 'X'; 
                            } else {
                                endChar = ' '; 
                            }

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

    public void clearMaze(char[][] array, int height, int width){
        for(int i = 0; i < height; i++){
            for (int j=0; j <= width; j++){
                if(array[i][j] == 'O'){
                    array[i][j] = ' '; 
                }
            }
        }
        mazeBoard.removeAll();

        initMazeGrid(array, height, width, mazeBoard);

        mazeBoard.revalidate();
        mazeBoard.repaint();
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
            clearMaze(array2, height2, width2);
            bfs(array2, height2, width2);
        }
        if(e.getSource()== clear){
            //clear the maze 
            clearMaze(array2, height2, width2); 
        }
    }
}
