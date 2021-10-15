import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

/**
 * \brief The Main Game controller class. Controls avatar movements during each level
 */
public class Main{
  static Pacman pacman;
  static Ghost clyde;
  static Ghost blinky;
  static Map map;
  int myTimerDelay;
  int attackTimer;

  Random r = new Random();
  int cherryTimer = r.ints(100, 251).findFirst().getAsInt();

  enum direction{
    UP,
    RIGHT,
    LEFT,
    DOWN,
  };  

  private void levelOneTick(){
    pacman.tick();
    clyde.tick();
    if(map.collision()!=0){
      if(map.isDead()){
        map.reset();
        clyde.reset();
        pacman.reset();
      }
      else if(map.collision() == 2) {
        map.setCollision(0);
      }
      else{
        levelOneReset();
      }
    }
    if(map.getPacmanAttack()){
      clyde.setAttack(false);
      levelOneAttackMode();
    }
  }

  public void levelOneReset(){
    int[][] ghostPos = new int[1][2];
    ghostPos[0] = clyde.getPos();
    map.softReset(pacman.getPos(), ghostPos);
    clyde.reset();
    pacman.reset();
  }

  public void levelOneAttackMode(){
    attackTimer++;
    if(attackTimer == 100){
      attackTimer = 0; 
      map.setPacmanAttack(false);
      clyde.setAttack(true);
      System.out.print("ATTACK OFF");
    }
  }

  private void levelTwoTick(){
    if(map.levelComplete){
      System.out.print("IN LEVEL COMPLETE");
      map.reset();
      pacman.reset();
      clyde.reset(); 
    }
    pacman.tick();
    clyde.tick();
    blinky.tick();
    if(map.collision()!=0){
      if(map.isDead()){
        map.reset();
        clyde.reset();
        blinky.reset();
        pacman.reset();
      }
      else if(map.collision() == 2) {
        map.setCollision(0);
      }
      else{
        levelTwoReset();
      }
    }
    if(map.getPacmanAttack()){
      clyde.setAttack(false);
      blinky.setAttack(false);
      levelTwoAttackMode();
    }
  }

  public void levelTwoReset(){
    int[][] ghostPos = new int[2][2];
    ghostPos[0] = clyde.getPos();
    ghostPos[1] = blinky.getPos(); 
    map.softReset(pacman.getPos(), ghostPos);
    clyde.reset();
    blinky.reset();
    pacman.reset();
  }

  public void levelTwoAttackMode(){
    attackTimer++;
    if(attackTimer == 100){
      attackTimer = 0; 
      map.setPacmanAttack(false);
      clyde.setAttack(true);
      blinky.setAttack(true);
      System.out.print("ATTACK OFF");
    }
  }

  ActionListener gameTimer = new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent theEvent) {
      //randomized timer for spawning a cherry
      if(cherryTimer>0 && pacman.getMap().isCherry == false){
        cherryTimer--;
      } else {
          pacman.getMap().spawnCherry();
          cherryTimer = r.ints(100, 251).findFirst().getAsInt();
      }
      if(map.curLevel == 1){
        myTimerDelay = 100;//Frame per second speed 
        levelOneTick(); 
      }
      else if(map.curLevel == 2){
        myTimerDelay = 16; 
        levelTwoTick();
      }
    }
  };

  /**
   * \brief  The Main game controller. Instantiates all of the game avatars, manages game timer and controls 
   *  all key inputs. 
  */
  public static void main(String[] args) {
    Main main = new Main();
    JFrame frame = new JFrame("Team Transfers - Pacman"); //new JFrame with title
    map = new Map(frame);
    pacman = new Pacman(map);
    clyde = new Ghost(1, map);
    blinky = new Ghost(2, map);
    main.myTimerDelay = 100;
    final Timer myTimer = new Timer(main.myTimerDelay, main.gameTimer); 

    frame.addKeyListener(new KeyListener() {
      @Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}

      @Override
      public void keyPressed(KeyEvent e){        
        int key = e.getKeyCode();
        
        if(key == KeyEvent.VK_RIGHT){
          pacman.setDirection(direction.RIGHT);
        } else if(key == KeyEvent.VK_LEFT){
          pacman.setDirection(direction.LEFT);
        } else if(key == KeyEvent.VK_DOWN){
          pacman.setDirection(direction.DOWN);
        } else if(key == KeyEvent.VK_UP){
          pacman.setDirection(direction.UP);
        } else if(key == KeyEvent.VK_SPACE) {
          myTimer.start();
        } else if(key == KeyEvent.VK_Q) {
          myTimer.stop();
          System.out.print("Quit");
        }    
      }

      @Override
      public void keyReleased(KeyEvent e){       
        // pacman.keyReleased(e);                   
      }
    });
    System.out.println("BEGIN");
  }
  
} 