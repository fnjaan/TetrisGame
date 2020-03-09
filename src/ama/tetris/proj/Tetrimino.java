package ama.tetris.proj;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public final class Tetrimino extends JPanel {
    /*
    0 = I
    1 = J
    2 = L
    3 = O
    4 = S
    5 = Z
    6 = T
    */
    private final Point[][][] TetriminoPoint = {
        {
            //I
            { new Point(1,0), new Point(1,1), new Point(1,2), new Point(1,3) },
            { new Point(0,2), new Point(1,2), new Point(2,2), new Point(3,2) },
            { new Point(1,0), new Point(1,1), new Point(1,2), new Point(1,3) },
            { new Point(0,1), new Point(1,1), new Point(2,1), new Point(3,1) }
        },
        {
            //J
            { new Point(0,0), new Point(1,0), new Point(1,1), new Point(1,2) },
            { new Point(0,1), new Point(0,2), new Point(1,1), new Point(2,1) },
            { new Point(1,0), new Point(1,1), new Point(1,2), new Point(2,2) },
            { new Point(0,1), new Point(1,1), new Point(2,0), new Point(2,1) }
        },
        {
            //L
            { new Point(0,2), new Point(1,0), new Point(1,1), new Point(1,2) },
            { new Point(0,1), new Point(1,1), new Point(2,1), new Point(2,2) },
            { new Point(1,0), new Point(1,1), new Point(1,2), new Point(2,0) },
            { new Point(0,0), new Point(0,1), new Point(1,1), new Point(2,1) }
        },
        {
            //O
            { new Point(0,1), new Point(0,2), new Point(1,1), new Point(1,2) },
            { new Point(0,1), new Point(0,2), new Point(1,1), new Point(1,2) },
            { new Point(0,1), new Point(0,2), new Point(1,1), new Point(1,2) },
            { new Point(0,1), new Point(0,2), new Point(1,1), new Point(1,2) }

        },
        {
            //S
            { new Point(0,1), new Point(0,2), new Point(1,0), new Point(1,1) },
            { new Point(0,1), new Point(1,1), new Point(1,2), new Point(2,2) },
            { new Point(1,1), new Point(1,2), new Point(2,0), new Point(2,1) },
            { new Point(0,0), new Point(1,0), new Point(1,1), new Point(2,1) }
            

        },
        {
            //Z
            { new Point(0,0), new Point(0,1), new Point(1,1), new Point(1,2) },
            { new Point(0,2), new Point(1,1), new Point(1,2), new Point(2,1) },
            { new Point(1,0), new Point(1,1), new Point(2,1), new Point(2,2) },
            { new Point(0,1), new Point(1,0), new Point(1,1), new Point(2,0) }

        },
        {
            //T
            { new Point(0,1), new Point(1,0), new Point(1,1), new Point(1,2) },
            { new Point(0,1), new Point(1,1), new Point(1,2), new Point(2,1) },
            { new Point(1,0), new Point(1,1), new Point(1,2), new Point(2,1) },
            { new Point(0,1), new Point(1,0), new Point(1,1), new Point(2,1) }

        }
    };
    
    private final Color[] blocksColor = {Color.CYAN, Color.ORANGE, Color.BLUE, Color.YELLOW, Color.RED, Color.GREEN, Color.MAGENTA};
    private final Random rand = new Random();
    private final Color[][] grid;
    private final int[] nextPieces = new int[3];
    private int currentPiece, rotation, holdPiece = 0;
    private boolean isSwap = false, swapDelimiter, rotate =false, move=false;
    public int lines;
    private int flag = 5;
    public int level = 1;
    public int targetline = 10;
    private ArrayList<Integer> pieceStorage = new ArrayList<>();
    private BufferedImage scorePanel = null;
    private Point pt;
    private Point[][] test;
    public long score;
    public boolean levelchanged = false;
    
    //INITIALIZE GAME INTERFACE INSIDE THE CONSTRUCTOR
    Tetrimino() {
        grid = new Color[12][23];
        for(int i = 0; i < 12; i++) {
            for(int j = 0; j < 23; j++) {
                if(i == 0 || i == 11 || j == 22) {
                    grid[i][j] = Color.LIGHT_GRAY;
                } else {
                    grid[i][j] = Color.BLACK;
                }
            }
        }
        newPiece();
    }
    
    public void resetgrid() {
        for(int i = 0; i < 12; i++) {
            for(int j = 0; j < 23; j++) {
                if(i == 0 || i == 11 || j == 22) {
                    grid[i][j] = Color.LIGHT_GRAY;
                } else {
                    grid[i][j] = Color.BLACK;
                }
            }
        }
        newPiece();
    }
    //BUILDING THE PIECE/TETRIMINO
    public void newPiece() {
            pt = new Point(5,0);
            rotation = 0;
            if(pieceStorage.isEmpty()){
                pieceStorage = generateRandomSequence();
            }
            
            if(isSwap) {
                if(holdPiece == 0) {
                    holdPiece = currentPiece+1;
                    adjustPiecesStorage();
                    
                } else {
                    int temp = holdPiece;
                    holdPiece = currentPiece + 1;
                    currentPiece = temp - 1;
                    swapDelimiter = true;
                    
                }
                isSwap = false;
            } else {
                adjustPiecesStorage();
            }
            
            switch(currentPiece) {
                case 0:
                    rotation = 1;
                    break;
                case 1:
                    rotation = 3;
                    break;
                case 2:
                    rotation = 3;
                    break;
                case 4:
                    rotation = 1;
                    break;
                case 5:
                    rotation = 1;
                    break;
                case 6:
                    rotation = 3;
            }
    }

    private ArrayList<Integer> generateRandomSequence() {
        //Randomize pieces only
        ArrayList<Integer> storage = new ArrayList<>();
        Collections.addAll(storage, 0,1,2,3,4,5,6);
        Collections.shuffle(storage);
        return (storage);
    }
    
    private void adjustPiecesStorage() {
        currentPiece = pieceStorage.get(0);
        pieceStorage.remove(0);
        if(pieceStorage.size() >= 3) {
            nextPieces[0] = pieceStorage.get(0);
            nextPieces[1] = pieceStorage.get(1);
            nextPieces[2] = pieceStorage.get(2);
        } else {
            pieceStorage = generateRandomSequence();
            pieceStorage.set(0, nextPieces[1]);
            pieceStorage.set(1, nextPieces[2]);
            nextPieces[0] = pieceStorage.get(0);
            nextPieces[1] = pieceStorage.get(1);
            nextPieces[2] = pieceStorage.get(2);
        }
    }
    
    //PIECE/TETRIMINO CONTROLS
    public void softDrop() {
        if(!isCollide(pt.x, pt.y + 1, rotation)) {
            pt.y+=1;
        } else {
            if(flag !=0) {
                flag--;
            } else{
                fixToblocks();
                flag=5;
            }
            
        }
        repaint();
    }
    
    public void movePiece(int i) {
        move = true;
        if(!isCollide(pt.x + i, pt.y, rotation)) {
            pt.x+=i;
        }
        repaint();
    }
    
    void rotatePiece(int i) {
        rotate=true;
        int newRotation = (rotation + i)% 4;
        
        if(newRotation < 0) {
            newRotation = 3;
        }
        if(!isCollide(pt.x, pt.y, newRotation)) {
            rotation = newRotation;
        } else {
            if(pt.x < 12/2) {
                if(!isCollide(pt.x+1, pt.y, newRotation)) {
                    pt.x++;
                    rotation = newRotation;
                }
               
            }
            else if(pt.x > 12/2) {
               if(!isCollide(pt.x-1, pt.y, newRotation)) {
                    pt.x--;
                    rotation = newRotation;
                }
            }
        }
//        } else if(isCollide(pt.x, pt.y, newRotation) && pt.x == 0){
//            pt.x++;
//            rotation = newRotation;
//        } else if(isCollide(pt.x, pt.y, newRotation) && pt.x == 9){
//            if(currentPiece == 0){
//                pt.x = pt.x - 2;
//                rotation = newRotation;
//            }else{
//                pt.x--;
//                rotation = newRotation;
//            }
//            
//        } else if(isCollide(pt.x, pt.y+1, newRotation)){
//            pt.y--;
//            rotation = newRotation;
//        }
        repaint();
    }
    
    public void holdSwapPiece() {
        if(!swapDelimiter) {
            isSwap = true;
            newPiece();
        }
    }
    
    public void hardDrop() {
        while(true) {
            if(!isCollide(pt.x, pt.y + 1, rotation)) {
                pt.y+=1;
            } else {
                fixToblocks();
                break;
            }
        }
        repaint();
    }
    
    //COLLISION DETECTION
    private boolean isCollide(int x, int y, int rotation) {
        for(Point p:TetriminoPoint[currentPiece][rotation]) {
            if(grid[p.x+x][p.y+y] != Color.BLACK || x < 0 || y < 0 ) {
                return true;
            }
        }
        return false;     
    }
    
    public void fixToblocks() {
        for(Point p:TetriminoPoint[currentPiece][rotation]) {
            grid[pt.x + p.x][pt.y + p.y] = blocksColor[currentPiece];
        }
        score+=21*level;
        if(!isGameover()) {
            clearRows();
            newPiece();
        }
        swapDelimiter = false;
    }
    
    public void clearRows() {
        boolean gap;
        int numClear = 0;
        for(int j = 21; j > 0; j--) {
            gap = false;
            for(int i = 1; i < 11; i++) {
                if(grid[i][j] == Color.BLACK) {
                    gap = true;           
                    break;
                }
            }
            if(!gap) {
            deleteRow(j);
            j+=1;
            numClear+=1;
            }
        }
        //COUNT NO. OF LINES CLEARED
        switch(numClear) {
                case 1:
                    lines+=1;
                    score+=100*level;
                    GameThread.bg.playClearline();
                    break;
                case 2:
                    lines+=2;
                    score+=200*level;
                    GameThread.bg.playClearline();
                    break;
                case 3:
                    lines+=3;
                    score+=300*level;
                    GameThread.bg.playClearline();
                    break;
                case 4:
                    lines+=4;
                    score+=400*level;
                    GameThread.bg.playClearline();
            }
        
        if(lines >= targetline) {
            level++;
            levelchanged = true;
            targetline+=10;
        }
    }
    
    public void deleteRow(int row) {
        for(int j = row-1;j > 0; j--) {
            for(int i =1; i < 11; i++) {
                grid[i][j+1] = grid[i][j];
            }
        }
    }
    
    //GAME OVER
    public boolean isGameover() {
        for(int i = 1; i < 11; i++) {
            if(grid[i][2] != Color.BLACK) {
                return true;
            }
        }
        return false;
    }
    
    //RENDERING AND DRAWING THE GAME      
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        super.paintComponent(g2);
        drawGameField(g2);
        drawScorePanel(g2);
        
        //RED LINE INDICATOR
        g2.setColor(Color.RED); 
        g2.drawLine(0, 77, 310, 77);
        
         //NO. OF LINES TEXT
        g2.setColor(Color.decode("#23374d"));
        g2.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 28));
        
        g2.drawString(Integer.toString(lines), 523, 179);
        g2.drawString(Long.toString(score), 523, 98);
        g2.setColor(Color.decode("#e5e5e5"));
        g2.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 47));
        g2.drawString(Integer.toString(level), 588, 303);
        
        g2.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 18));
        g2.drawString("Press ESC to pause...", 26, 18);
        
        drawPotentialDrop(g2);
        drawPiece(g2);
        drawpieceStorage(g2);
        if(holdPiece != 0) {
            drawholdPiece(g2);
        }
        g2.dispose();
    }

    private void drawGameField(Graphics g) {
        g.setColor(Color.decode("#191919"));
        g.fillRect(0,0,26*12,26*23);
        for(int i = 0; i < 12; i++) {
            for(int j = 0; j < 23; j++) {
                g.setColor(grid[i][j]);
                g.fillRect(26*i, 26*j, 24, 24);
            }
        }
    }
    
    private void drawScorePanel(Graphics g) {
        try {
                scorePanel = ImageIO.read(new File("res/scorepanel.png"));
        } catch ( IOException exc ) {
                System.out.println(exc);
        }
        g.drawImage(scorePanel, 312,0,null); 
    }
    
    private void drawPiece(Graphics g) {
        g.setColor(blocksColor[currentPiece]);
        for(Point p:TetriminoPoint[currentPiece][rotation]) {
            g.fillRect((p.x+pt.x)*26,(p.y+pt.y)*26, 24, 24);
        }
    }
    
    private void drawPotentialDrop(Graphics g) {
        int x = pt.x;
        int y = pt.y;
        g.setColor(Color.DARK_GRAY);
        for(Point p:TetriminoPoint[currentPiece][rotation]) {
            while(true) {
                if(!isCollide(x,y + 1, rotation)) {
                    y+=1;
                } else { 
                    break;
                }
            }
            g.fillRect((p.x+x)*26,(p.y+y)*26, 24, 24);
        }
    }
      /*
    0 = I
    1 = J
    2 = L
    3 = O
    4 = S
    5 = Z
    6 = T
    */
    private void drawpieceStorage(Graphics g) {
        int adjustY = 11;
	for(int i = 0; i < 3; i++) {
            int posx = 15;
            int posy = 12;
            int rot = 0;
            g.setColor(blocksColor[nextPieces[i]]);
            switch(nextPieces[i]) {
                case 0:
                    posx = -1;
                    posy = -20;
                    rot=1;
                    break;
                case 1:
                    rot = 3;
                    break;
                case 2:
                    rot = 3;
                    break;
                case 3:
                    posy = -10;
                    posx = 28;
                    break;
                case 4:
                    posy = -10;
                    rot = 1;
                    break;
                case 5:
                    posy = -10;
                    rot = 1;
                    break;
                case 6:
                    rot = 3;
                    break;
                }
            for(Point p:TetriminoPoint[nextPieces[i]][rot]) {
                g.fillRect(((p.x+14)*26)+posx,((p.y+(adjustY))*26)+ posy, 24, 24);
            }
            adjustY = adjustY + 4;
        }
    }
 
    private void drawholdPiece(Graphics g) {
        int rot = 0;
        int posx = 15;
        int posy = 12;
        switch(holdPiece-1) {
                case 0:
                    posx = -1;
                    posy = -18;
                    rot=1;
                    break;
                case 1:
                    rot = 3;
                    break;
                case 2:
                    rot = 3;
                    break;
                case 3:
                    posy = -8;
                    posx = 28;
                    break;
                case 4:
                    posy = -13;
                    rot = 1;
                    break;
                case 5:
                    posy = -5;
                    rot = 1;
                    break;
                case 6:
                    rot = 3;
                    posy = +15;
                    break;
                }
        g.setColor(blocksColor[holdPiece-1]);
        //System.out.println("-----------------------------");
        for(Point p:TetriminoPoint[holdPiece-1][rot]) {
            g.fillRect(((p.x+14)*26)+posx,((p.y+3)*26)+ posy, 24, 24);
            //System.out.println(((p.x+14)*26)+posx + " " + ((p.y)*26)+posy);
        }
    }

    public void consolepoints() {
        System.out.println(pt.x + ", "+pt.y);
    }
}
