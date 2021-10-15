import javax.imageio.*;
import javax.swing.ImageIcon;
import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
/**
 * \brief The Ghost object class. Dictates ghost behaviour and functionality.
 */
class Ghost {
  private int xCoord;
  private int yCoord;
  private boolean shouldReset;
  private boolean attack;
  private Main.direction facing;
  Image ghostImg = null;
  Image redGhost = null; //Blinky
	Image orangeGhost = null; //Clyde
	Image pinkGhost = null; //Pinky
	Image blueGhost = null; //Inky
  Image attackGhost = null; 
  Map map;
  int fixTimer = 0;
  boolean force = false;
  Main.direction hold;
  int id;

  /**
   * \brief   Initializes the Ghost Class 
   * \param[in]  map  An instance of the Map Class
  */
  public Ghost(int id, Map map){
    xCoord = 9;
    yCoord = 8;
    attack = true; 
    loadImages(id);
    this.map = map;
    facing = Main.direction.LEFT; 
    shouldReset = false; 
    this.id = id;
  }

  /**
   * \brief   Defines the images that will be passed to the draw methods in order to provide the right graphics 
   * \param[in]  id  An integer depicting the ghost, ex. clyde, pinky, blinky etc...
  */
  private void loadImages(int id){
    switch(id){
      case 1:
        ghostImg = new ImageIcon("images/orange_ghost.gif").getImage();
        BufferedImage dattack = null;
        try {
          dattack = ImageIO.read(new File("images/blue_ghost.png"));
		    } catch (IOException e) {
			      e.printStackTrace();
		    }
        attackGhost = dattack.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
      break;
      case 2:
        ghostImg = new ImageIcon("images/pink_ghost.gif").getImage();
        BufferedImage dattack2 = null;
        try {
          dattack2 = ImageIO.read(new File("images/blue_ghost.png"));
		    } catch (IOException e) {
			      e.printStackTrace();
		    }
        attackGhost = dattack2.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
      break;
    }
		// redGhost = new ImageIcon("images/red_ghost.gif").getImage();
		// orangeGhost = new ImageIcon("images/orange_ghost.gif").getImage();
		// pinkGhost = new ImageIcon("images/pink_ghost.gif").getImage();
    // blueGhost = new ImageIcon("images/blue_ghost.gif").getImage(); 
    // attackGhost = new ImageIcon("images/attack_ghost.gif").getImage(); 
  }

  /**
   * \brief   randomly determines which way clyde (ghost id: 1) is facing.
  */
  private void chooseDirection(){
    Random rand = new Random();
    switch(rand.nextInt(4)){
      case 0:
        this.facing = Main.direction.RIGHT;
        break;
      case 1: 
        this.facing = Main.direction.LEFT;
        break;
      case 2:
        this.facing = Main.direction.DOWN;
        break;
      case 3:
        this.facing = Main.direction.UP;
        break;
    }
  }

  /**
   * \brief   resets the coordinates of the ghost
  */
  public void reset(){
    xCoord = 9;
    yCoord = 8;
  }

  /**
   * \brief   returns the position of the ghost in an int array.
   * \param[out]  position  an integer array containing the coordinates of the ghost.
  */
  public int[] getPos(){
    int[] position = new int[]{xCoord,yCoord};
    return position;
  }
  

  /**
   * \brief  Depending on the state variable facing, verifies if the potential space is available to move into and  * then calls upon the Map.consume method, and then calls the drawGhost method.
  */
  public void move(){
    switch(facing){
      case UP:
        if(map.canGhostMoveInto(xCoord,yCoord-1)){
          yCoord -= 1;
          drawGhost();
        }
        else{
          chooseDirection();
          move();
        }
        break;
      case DOWN:
        if(map.canGhostMoveInto(xCoord,yCoord+1)){
          yCoord += 1;
          drawGhost();
        }
        else{
          chooseDirection();
          move();
        }
        break;
      case RIGHT:
        if(map.canGhostMoveInto(xCoord+1,yCoord)){
          xCoord += 1;
          drawGhost();
        }
        else{
          chooseDirection();
          move();
        }
        break;
      case LEFT:
        if(map.canGhostMoveInto(xCoord-1,yCoord)){
          xCoord -= 1;
          drawGhost();
        }
        else{
          chooseDirection();
          move();
        }
        break;
    }
  }


  public void tick(){
    if(this.id == 1) {
      move();
    }
    else {
      ghostSmartMove();
    }

  }

  /**
   * \brief   Pulls the loaded images and coordinates, passes them to the Map class, and changes the position of the * ghost graphics.
  */
  private void drawGhost(){
    Image gImg = null;
    if (attack){
      gImg = ghostImg; 
    }else {
      gImg = attackGhost; 
    }
    switch(facing){
      case UP:
        int[] past = new int[]{xCoord,yCoord+1};
        int[] future = new int[]{xCoord,yCoord};
        shouldReset = map.ghostMove(past, future, gImg);
        break;
      case DOWN:
        int[] pastD = new int[]{xCoord,yCoord-1};
        int[] futureD = new int[]{xCoord,yCoord};
        shouldReset = map.ghostMove(pastD, futureD, gImg); 
        break;
      case LEFT:
        int[] pastL = new int[]{xCoord+1,yCoord};
        int[] futureL = new int[]{xCoord,yCoord};
        shouldReset = map.ghostMove(pastL, futureL, gImg);
        break;
      case RIGHT:
        int[] pastR = new int[]{xCoord-1,yCoord};
        int[] futureR = new int[]{xCoord,yCoord};
        shouldReset = map.ghostMove(pastR, futureR, gImg);
        break;
    }
    if(shouldReset){
      int[] erase = new int[]{xCoord,yCoord};
      map.ghostReset(erase, gImg);
      reset();
    }
  }

  /**
   * \brief   Defines the attack state of the ghost, (whether or not it can be damaged)
   * \param[in]  state  boolean value that determines what the attack value becomes.
  */
  public void setAttack(boolean state) {
    attack = state; 
  }

  /**
   * \brief   Depending on the state variable facing, verifies if the potential space is available to move into and * then calls upon the Map.consume method, and then calls the drawGhost method. This method vaires from move() as * it applies only to the ghost with id = 2.
  */
  public void ghostSmartMove() {
    int[] pacmanLocation = map.getPacmanLocation();
    int prioDirections = 0;
    int checkX = Integer.compare(xCoord, pacmanLocation[0]);
    int checkY = Integer.compare(yCoord, pacmanLocation[1]);
    if(checkX < 0){
      prioDirections += 1;
    } else if(checkX > 0) {
      prioDirections += 2;
    } else {
      prioDirections += 0;
    }
    if(checkY < 0){
      prioDirections += 4;
    } else if(checkY > 0) {
      prioDirections += 8;
    } else{
      prioDirections += 0;
    }
    if(!force){
      switch(prioDirections){
        case 5:
          if(map.canGhostMoveInto(xCoord+1,yCoord)){
            System.out.println("I BROKE IT 1");
            xCoord += 1;
            facing = Main.direction.RIGHT;
            drawGhost();
          }
          else{
            if(map.canGhostMoveInto(xCoord,yCoord+1)){
              System.out.println("I BROKE IT 2");
              yCoord += 1;
              facing = Main.direction.DOWN;
              drawGhost();
            }
            else{
              force = true;
              fixTimer += 3;
              hold = Main.direction.LEFT;
              ghostSmartMove();
            }
          }
          break;
        case 6:
          if(map.canGhostMoveInto(xCoord-1,yCoord)){
            System.out.println("I BROKE IT 3");
            xCoord -= 1;
            facing = Main.direction.LEFT;
            drawGhost();
          }
          else{
            if(map.canGhostMoveInto(xCoord,yCoord+1)){
              System.out.println("I BROKE IT 4");
              yCoord += 1;
              facing = Main.direction.DOWN;
              drawGhost();
            } else{
              force = true;
              fixTimer += 3;
              hold = Main.direction.RIGHT;
              ghostSmartMove();
            }
          }
          break;
        case 9:
          if(map.canGhostMoveInto(xCoord+1,yCoord)){
            System.out.println("I BROKE IT 5");
            xCoord += 1;
            facing = Main.direction.RIGHT;
            drawGhost();
          }
          else{
            if(map.canGhostMoveInto(xCoord,yCoord-1)){
              System.out.println("I BROKE IT 6");
              yCoord -= 1;
              facing = Main.direction.UP;
              drawGhost();
            }
            else{
              force = true;
              fixTimer += 3;
              hold = Main.direction.LEFT;
              ghostSmartMove();
            }
          }
          break;
        case 10:
          if(map.canGhostMoveInto(xCoord-1,yCoord)){
            System.out.println("I BROKE IT 7");
            xCoord -= 1;
            facing = Main.direction.LEFT;
            drawGhost();
          }
          else{
            if(map.canGhostMoveInto(xCoord,yCoord-1)){
              System.out.println("I BROKE IT 8");
              yCoord -= 1;
              facing = Main.direction.UP;
              drawGhost();
            } else{
              force = true;
              fixTimer += 3;
              hold = Main.direction.RIGHT;
              ghostSmartMove();
            }
          }
          break;
        case 1:
          if(map.canGhostMoveInto(xCoord+1,yCoord)){
              System.out.println("I BROKE IT 9");
              xCoord += 1;
              facing = Main.direction.RIGHT;
              drawGhost();
            } else{
              force = true;
              fixTimer += 3;
              hold = Main.direction.LEFT;
              ghostSmartMove();
            }
          break;
        case 2:
          if(map.canGhostMoveInto(xCoord-1,yCoord)){
              System.out.println("I BROKE IT 10");
              xCoord -= 1;
              facing = Main.direction.LEFT;
              drawGhost();
          }
          else{
              force = true;
              fixTimer += 3;
              hold = Main.direction.RIGHT;
              ghostSmartMove();
            }
          break;
        case 4:
          if(map.canGhostMoveInto(xCoord,yCoord+1)){
              System.out.println("I BROKE IT 11");
              yCoord += 1;
              facing = Main.direction.DOWN;
              drawGhost();
          }
          else{
              force = true;
              fixTimer += 3;
              hold = Main.direction.RIGHT;
              ghostSmartMove();
            }
          break;
        case 8:
          if(map.canGhostMoveInto(xCoord,yCoord-1)){
              System.out.println("I BROKE IT 12");
              yCoord -= 1;
              facing = Main.direction.UP;
              drawGhost();
          }
          else{
              force = true;
              fixTimer += 3;
              hold = Main.direction.LEFT;
              ghostSmartMove();
            }
          break;
      }
    } else{
      while(fixTimer>0){
        move2(hold);
        switch(hold){
          case RIGHT:
            if(map.canGhostMoveInto(xCoord,yCoord-1)){
              hold = Main.direction.UP;
            } else{
              if(map.canGhostMoveInto(xCoord,yCoord+1)){
                hold = Main.direction.DOWN;
              } 
            }
            break;
          case LEFT:
            if(map.canGhostMoveInto(xCoord,yCoord-1)){
              hold = Main.direction.UP;
            } else{
              if(map.canGhostMoveInto(xCoord,yCoord+1)){
                hold = Main.direction.DOWN;
              } 
            }
            break;
          case UP:
            if(map.canGhostMoveInto(xCoord+1,yCoord)){
              hold = Main.direction.RIGHT;
            } else {
              if(map.canGhostMoveInto(xCoord-1,yCoord)){
                hold = Main.direction.LEFT;
              } 
            }
            break;
          case DOWN:
            if(map.canGhostMoveInto(xCoord+1,yCoord)){
              hold = Main.direction.RIGHT;
            } else {
              if(map.canGhostMoveInto(xCoord-1,yCoord)){
                hold = Main.direction.LEFT;
              } 
            }
            break;
        }
        fixTimer -= 1;
        if(fixTimer == 0){
          force = false;
        }
      }

    }

  }
 

   /**
    * \brief   helper method for the ghostSmartMove() that helps it from getting stuck.
    * \param[in]  direction  enumerator from Main: direction, that dictates the path in which the ghost is travelling
   */
  public void move2(Main.direction direction){
    switch(direction){
      case UP:
        if(map.canGhostMoveInto(xCoord,yCoord-1)){
          yCoord -= 1;
          hold = Main.direction.UP;
          facing = Main.direction.UP;
          drawGhost();
        }
        else{
          chooseDirection();
          move();
        }
        break;
      case DOWN: 
        if(map.canGhostMoveInto(xCoord,yCoord+1)){
          yCoord += 1;
          hold = Main.direction.DOWN;
          facing = Main.direction.DOWN;
          drawGhost();
        }
        else{
          chooseDirection();
          move();
        }
        break;
      case RIGHT:
        if(map.canGhostMoveInto(xCoord+1,yCoord)){
          xCoord += 1;
          hold = Main.direction.RIGHT;
          facing = Main.direction.RIGHT;
          drawGhost();
        }
        else{
          chooseDirection();
          move();
        }
        break;
      case LEFT:
        if(map.canGhostMoveInto(xCoord-1,yCoord)){
          xCoord -= 1;
          hold = Main.direction.LEFT;
          facing = Main.direction.LEFT;
          drawGhost();
        }
        else{
          chooseDirection();
          move();
        }
        break;
    }
  }
}