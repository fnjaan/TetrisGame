/*
    Classic GameUI Project (8 days left!)
So far:
 Collision detector  - done!
 Piece Rotation - done!
 FixPieceToGround method - done!
 Randomized pieces - done!
 ClearingRow/s method - done!
 Scoring System - done!
 potentialDrop method - done!
 showTheNextPiece method - done!
 A StoragePiece that can contain at least three pieces - done!
 SwapUnwantedPiece replaced by holdCurrentPiece method - done!
 GameOver Method - done!
 Usernames(SQLITE database) - done!
 GameUI Options - done!
 Usernames(SQLITE database) - done!
 Adding sounds(optional) - done!
 LeaderboardsOptions(SQLITE database) - done!
 Front UI = done!

Things left to do:
5. Difficulty level, Powers up, and Mods
7. Documentation
*/


package ama.tetris.proj;

import static ama.tetris.proj.GameUI.close;
import static ama.tetris.proj.GameUI.gameUI;
import static ama.tetris.proj.GameUI.pause;
import static ama.tetris.proj.GameUI.piece;
import static ama.tetris.proj.GameUI.ret;
import static ama.tetris.proj.GameUI.retry;
import static ama.tetris.proj.GameUI.score;
import static ama.tetris.proj.GameUI.speed;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

class GameThread extends Thread {
    
    public boolean running;
    private final int FPS = 60;
    private double averageFPS;
    public boolean gameover;
    public  static boolean playonlyonce;
    public static SoundEffects bg;
    @Override
    public void run() {       
        running = true;
        long startTime;
        long URDTimeMilis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        int maxFrameCount = 60;
        long targetTime = 1000 / FPS;
        int gamespeed = 1000;
        playonlyonce=true;
        bg = new SoundEffects();
                while(running) {
                    startTime = System.nanoTime();
                    URDTimeMilis = (System.nanoTime() - startTime) / 1000000;
                    waitTime = targetTime - URDTimeMilis;
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GameUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    totalTime += System.nanoTime() - startTime;
                    frameCount++;
                    //game code here
                    if(retry == true) {
                        bg.playBG();
                        piece.lines = 0;
                        piece.score = 0;
                        piece.resetgrid();
                        retry = false;
                    }
                    if(ret == true) {
                        bg.closeAll();
                        ret = false;
                        piece.level = 1;
                        gameUI.dispose();
                        break;
                    }
                    if(piece.isGameover() || close == true) {
                        bg.stopBG();
                        bg.playGameover();
                        score = piece.score;
                        GameDB data  = new GameDB();
                        data.openconnection();
                        data.updatePlayerScore((int) score, UserUI.id);
                        data.closeconnection();
                        GameOverUI over = new GameOverUI(gameUI,true);                    
                        over.setVisible(true);
                        speed = 60;
                        gameover = true;
                        bg.closeAll();
                        break;  
                    }
//                    if(piece.levelchanged){
//                        if(piece.level > 1 && piece.level <=10) {
//                            speed-=6;
//                        } else if(piece.level > 10) {
//                            speed-=1;
//                        }
//                        piece.levelchanged = false;
//                    }     
                    if(frameCount == speed){
                        if(!pause){
                            gameUI.setVisible(true);
                            if(playonlyonce) {
                                bg.playBG();
                                playonlyonce = false;
                            }
                            piece.softDrop();
                            averageFPS = 1000.0 / ((totalTime / frameCount) / 1000000);
                            frameCount = 0;
                            totalTime = 0;
                        }
                    }
                    
                    if(frameCount == maxFrameCount) {
                        averageFPS = 1000.0 / ((totalTime / frameCount) / 1000000);
                        frameCount = 0;
                        totalTime = 0;
                       
                    }
                }
                gameUI.dispose();
            }
}

public class GameUI {
    private static boolean running;
    private static final int FPS = 60;
    private static double avarageFPS;
    public static boolean pause;
    public static boolean close;
    public static boolean retry,ret,speedchanged = false;
    public static long score;
    public static int speed=60;
    public static Thread gameThread;
    public static Tetrimino piece;
    public static JFrame gameUI;
    public static void rungame() {
       GameThread gT = new GameThread();
       gT.start();
       pause = false;
       close = false;
       retry = false;
       ret = false;
       gameUI = new JFrame("Classic Tetris");
       gameUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       gameUI.setSize((12*26+9)+ 397, 26*23+29); 
       //321 627
       gameUI.setIconImage(Toolkit.getDefaultToolkit().getImage(GameUI.class.getResource("icon.png")));
       gameUI.setLocationRelativeTo(null);
       gameUI.setResizable(false);
       piece = new Tetrimino(); 
       gameUI.add(piece);
       gameUI.addKeyListener(new KeyListener(){
            
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()){
                    case KeyEvent.VK_UP:
                        piece.rotatePiece(-1);
                        GameThread.bg.playRotate();
                        break;
                    case KeyEvent.VK_DOWN:
                        piece.softDrop();
                        GameThread.bg.playMove();
                        break;
                    case KeyEvent.VK_LEFT:
                        piece.movePiece(-1);
                        GameThread.bg.playMove();
                        break;
                    case KeyEvent.VK_RIGHT:
                        piece.movePiece(+1);
                        GameThread.bg.playMove();
                        break;
                    case KeyEvent.VK_SPACE:
                        piece.hardDrop();
                        GameThread.bg.playHD();
                        break;
                    case KeyEvent.VK_Z:
                        piece.holdSwapPiece();
                        break;
                    case KeyEvent.VK_C:
                        piece.consolepoints();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        pause = !pause;
                        GamePauseDialog game = new GamePauseDialog(gameUI,true);
                        GameThread.bg.stopBG();
                        game.setVisible(true);
                        
                }
            
            }
            @Override
            public void keyReleased(KeyEvent e) {
               
            }
        });
       
    }
   
}
