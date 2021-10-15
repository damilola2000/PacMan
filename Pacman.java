import java.util.Scanner; 
import java.util.concurrent.TimeUnit;
import javax.imageio.*;
import javax.swing.ImageIcon;
import java.io.*;
import java.awt.*;

/**
* \brief   The Pacman Class, controls all acman related movement and graphics 
*/
class Pacman {
  private int xCoord;
  private int yCoord;
  private Main.direction facing;
  private boolean attacking;
  Image dPacmanLeft = null;
	Image dPacmanRight = null;
	Image dPacmanUp = null;
	Image dPacmanDown = null;
  Map map;


  /**
  * \brief   Initializes the Pacman Class 
  * \param[in]  map  An instance of the Map Class
  */
  public Pacman(Map map){
    xCoord = 9;
    yCoord = 12;
    facing = Main.direction.LEFT;
    attacking = false; 
    this.map = map; 
    loadImages();
  }

  /**
  * \brief  Manages the postion Pacman moves into. 
  */
  public void move(){
    switch(facing){
      case UP:
        if(map.canMoveInto(xCoord,yCoord-1)){
          yCoord -= 1;
          this.consume(map.getGridValue(xCoord,yCoord));
          drawpacman();
        }
        break;
      case DOWN:
        if(map.canMoveInto(xCoord,yCoord+1)){
          yCoord += 1;
          this.consume(map.getGridValue(xCoord,yCoord));
          drawpacman();
        }
        break;
      case RIGHT:
        if(map.getGridValue(xCoord, yCoord) == "5" && xCoord == 18) {
          tunnelMove();
        }
        else if(map.canMoveInto(xCoord+1,yCoord)){
          xCoord += 1;
          this.consume(map.getGridValue(xCoord,yCoord));
          drawpacman();
        }
        break;
      case LEFT:
        if(map.getGridValue(xCoord, yCoord) == "5" && xCoord == 0) {
          tunnelMove();
        }
        else if(map.canMoveInto(xCoord-1,yCoord)){
          xCoord -= 1;
          this.consume(map.getGridValue(xCoord,yCoord));
          drawpacman();
        }
        break;
    }
  }

  /**
  * \brief  Manages Pacmans Movements through a tunnel 
  */
  public void tunnelMove(){
    switch(facing){
      case LEFT:
        xCoord = 18; 
        this.consume(map.getGridValue(xCoord,yCoord));
        int[] pastL = new int[]{0,yCoord};
        int[] futureL = new int[]{xCoord,yCoord};
        map.pacmanMove(pastL, futureL, dPacmanLeft);
        break;
      case RIGHT:
        xCoord = 0;
        this.consume(map.getGridValue(xCoord,yCoord));
        int[] pastR = new int[]{18,yCoord};
        int[] futureR = new int[]{xCoord,yCoord};
        map.pacmanMove(pastR, futureR, dPacmanRight);
        break;
    }
  }

  /**
  * \brief  Manages Pacmans consumption of points and put Pacman into attack mode 
  * \param[in]     item The id of the block Pacman has moved into
  */
  private void consume(String item){
      map.consume(xCoord,yCoord);
      if(item == "3") {
        attacking = true;
      }
  }

  /**
  * \brief Draws the appropriate Pacman image onto the Map board 
  */
  private void drawpacman(){
    switch(facing){
      case UP:
        int[] past = new int[]{xCoord,yCoord+1};
        int[] future = new int[]{xCoord,yCoord};
        map.pacmanMove(past, future, dPacmanUp);
        break;
      case DOWN:
        int[] pastD = new int[]{xCoord,yCoord-1};
        int[] futureD = new int[]{xCoord,yCoord};
        map.pacmanMove(pastD, futureD, dPacmanDown);
        break;
      case LEFT:
        int[] pastL = new int[]{xCoord+1,yCoord};
        int[] futureL = new int[]{xCoord,yCoord};
        map.pacmanMove(pastL, futureL, dPacmanLeft);
        break;
      case RIGHT:
        int[] pastR = new int[]{xCoord-1,yCoord};
        int[] futureR = new int[]{xCoord,yCoord};
        map.pacmanMove(pastR, futureR, dPacmanRight);
        break;
    }
  }

  /**
  * \brief Returns Pacmans current x-coordinate positon
  * \param[out] Returns an integer of the xCoord 
  */
  private int getxCoord(){
    return xCoord;
  }

  /**
  * \brief Returns Pacmans current y-coordinate positon
  * \param[out] Returns an integer of the yCoord 
  */
  private int getyCoord(){
    return yCoord;
  }

  /**
  * \brief Returns Pacmans current x-coordinate and y-coordinate positon
  * \param[out] Returns an array of integers of the xCoord and yCoord
  */
  public int[] getPos(){
    return new int[]{xCoord,yCoord};
  }

  /**
  * \brief Returns the direction Pacmans is facing 
  * \param[out] Returns a direction enum  
  */
  private Main.direction getDirection(){
    return facing;
  }

  /**
  * \brief Set the direction Pacman is facing 
  */
  public void setDirection(Main.direction direction){
    this.facing = direction;
  }

  /**
  * \brief Returns If Pacman is in attack mode
  * \param[out] Returns an a boolean. True for attck mode, False if not in attack mode
  */
  private boolean attacking(){
    return attacking;
  }

  /**
  * \brief Resets Pacman current possiotn to starting positon
  */
  public void reset(){
    xCoord = 9;
    yCoord = 12;
  }

  public void tick(){
    move();
  }

  public Map getMap(){
    return map;
  }

  /**
  * \brief Loads all of Pacmans images
  */
  private void loadImages(){    
    dPacmanLeft = new ImageIcon("images/left.gif").getImage();
		dPacmanRight = new ImageIcon("images/right.gif").getImage();
		dPacmanUp = new ImageIcon("images/up.gif").getImage();
    dPacmanDown = new ImageIcon("images/down.gif").getImage(); 
  }

} 