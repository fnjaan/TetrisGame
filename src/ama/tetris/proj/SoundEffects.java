package ama.tetris.proj;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class SoundEffects {
    private Clip bgClip;
    private Clip moveClip;
    private Clip rotateClip;
    private Clip hdClip;
    private Clip goClip;
    private Clip clClip;
    private final File bgFile, moveFile, rotateFile, hdFile, GOFile, CLFile;
    
    SoundEffects() {
        bgFile = new File("res/BG.wav");
        moveFile = new File("res/move.wav");
        rotateFile = new File("res/rotate.wav");
        hdFile = new File("res/harddrop.wav");
        GOFile = new File("res/gameover.wav");
        CLFile = new File("res/clearline.wav");
        
        //Clear Line
         try {      
            if(CLFile.exists()) {
                AudioInputStream audioBG = AudioSystem.getAudioInputStream(CLFile);
                clClip = AudioSystem.getClip();
                clClip.open(audioBG);
                audioBG.close();
            } else {
                System.out.println("Cannot find file...");
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        
        //Game Over
         try {      
            if(GOFile.exists()) {
                AudioInputStream audioBG = AudioSystem.getAudioInputStream(GOFile);
                goClip = AudioSystem.getClip();
                goClip.open(audioBG);
                audioBG.close();
            } else {
                System.out.println("Cannot find file...");
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        
        //BG
        try {      
            if(bgFile.exists()) {
                AudioInputStream audioBG = AudioSystem.getAudioInputStream(bgFile);
                bgClip = AudioSystem.getClip();
                bgClip.open(audioBG);
                audioBG.close();
            } else {
                System.out.println("Cannot find file...");
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        
        //MOVE
        try {      
            if(moveFile.exists()) {
                AudioInputStream audioMove = AudioSystem.getAudioInputStream(moveFile);
                moveClip = AudioSystem.getClip();
                moveClip.open(audioMove);
                audioMove.close();
            } else {
                System.out.println("Cannot find file...");
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        
        
        //ROTATE
        try {      
            if(rotateFile.exists()) {
                AudioInputStream audioRotate = AudioSystem.getAudioInputStream(rotateFile);
                rotateClip = AudioSystem.getClip();
                rotateClip.open(audioRotate);
                audioRotate.close();
            } else {
                System.out.println("Cannot find file...");
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        
        //HARDDROP
        try {      
            if(hdFile.exists()) {
                AudioInputStream audioHD = AudioSystem.getAudioInputStream(hdFile);
                hdClip = AudioSystem.getClip();
                hdClip.open(audioHD);
                audioHD.close();
            } else {
                System.out.println("Cannot find file...");
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void playBG() {
        if(bgClip.isRunning()) bgClip.stop();
        bgClip.setFramePosition(0);
        bgClip.start();
        bgClip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    
    public void continueBG() {
        if(bgClip.isRunning()) bgClip.stop();
        bgClip.start();
        bgClip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    
    public void stopBG() {
        bgClip.stop();
    }
    
    public void playGameover() {
        if(goClip.isRunning()) goClip.stop();
        goClip.setFramePosition(0);
        goClip.start();
    }
    
    public void playClearline() {
        if(clClip.isRunning()) clClip.stop();
        clClip.setFramePosition(0);
        clClip.start();
    }
    
    public void playMove() {
        if(moveClip.isRunning()) moveClip.stop();
        moveClip.setFramePosition(0);
        moveClip.start();
    }
    
    public void playRotate() {
        if(rotateClip.isRunning()) rotateClip.stop();
        rotateClip.setFramePosition(0);
        rotateClip.start();
    }
    
    public void playHD() {
        if(hdClip.isRunning()) hdClip.stop();
        hdClip.setFramePosition(0);
        hdClip.start();
    }
    
    public void closeAll() {
        hdClip.close();
        rotateClip.close();
        moveClip.close();
        bgClip.close();
        clClip.close();
        goClip.close();
    }
}
