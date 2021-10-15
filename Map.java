import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import java.util.Random;

/**
* \brief   The Map controller class, controls the display and operations to the gameplay map.
*/
public class Map extends JFrame{
  private JFrame frame; 
  JLabel scoreLabel;
	JLabel livesLabel;
	JLabel levelLabel;
	JLabel[][] grid; //grid overlay for map

	int curScore = 0;
	int curLives = 3;
	int curLevel = 1;
  int[] pacmanLocation = null;
  int collision = 0;
  boolean pacmanAttack = false;

  Boolean isCherry = false;
	JLabel[] possibleCherry = new JLabel[182]; //array for possible cherry locations
	int cherryArrayCount = 0;

  int nextLvlCount = 0;
  Boolean levelComplete = false;

	Random r = new Random();
	
	int[][] staticMap = new int[][] { 
		//19 x 22 map grid
		//0 = empty
		//1 = wall
		//2 = small dot
		//3 = large dot
		//4 = impassable zone
		//5 = tunnel right
    //6 = clyde
    //7 = cherry
	 	{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		{1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1},
		{1, 3, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 3, 1},
		{1, 2, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 2, 1},
		{1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
		{1, 2, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 2, 1},
		{1, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 1},
		{1, 1, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 1, 1},
		{0, 0, 0, 1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1, 0, 0, 0},
		{1, 1, 1, 1, 2, 1, 2, 1, 1, 4, 1, 1, 2, 1, 2, 1, 1, 1, 1},
		{5, 0, 0, 0, 2, 2, 2, 1, 4, 4, 4, 1, 2, 2, 2, 0, 0, 0, 5},
		{1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1},
		{0, 0, 0, 1, 2, 1, 2, 2, 2, 0, 2, 2, 2, 1, 2, 1, 0, 0, 0},
		{1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1},
		{1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1},
		{1, 2, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 2, 1},
		{1, 3, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 3, 1},
		{1, 1, 2, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 2, 1, 1},
		{1, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 1},
		{1, 2, 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 2, 1},
		{1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
	};



	int length = staticMap[0].length;
	int width = staticMap.length;
  boolean force = false;
  int holdDirection = 0;

	//define images
  BufferedImage ghostClyde = null;
  BufferedImage pacmanLeft = null;
	BufferedImage wallIMG = null;
	BufferedImage emptyIMG = null;
	BufferedImage sDotIMG = null;
	BufferedImage lDotIMG = null;
	BufferedImage cherryIMG = null;

  /**
  * \brief   Constructor of the Map class
  * \param[in]     frame     JFrame base entity
  */
  public Map(JFrame frame) {
    collision = 0;
    pacmanAttack = false; 
    this.frame = frame; 
		reset(); //call reset method to create map
	}

  /**
  * \brief   Getter method for map location object types
  * \param[in]     x     Integer for x coordinate of map
  * \param[in]     y     Integer for y coordinate of map
  * \param[out]    staticMap[y][x]     Integer for type of map object at x, y
  */
	public int getMapValue(int x, int y) {
     return staticMap[y][x]; //origin top left corner, x is horizontal position, y is vertical position
	}

  /**
  * \brief   Getter method for map location object names
  * \param[in]     x     Integer for x coordinate of map
  * \param[in]     y     Integer for y coordinate of map
  * \param[out]    grid[x][y].getName()     String for name of map object at x, y
  */
  public String getGridValue(int x, int y){
    return grid[x][y].getName();
  }

  /**
  * \brief   Check for whether it is possible to move into a map location
  * \param[in]     x     Integer for x coordinate of map
  * \param[in]     y     Integer for y coordinate of map
  * \param[out]    Boolean     true: location is movable into, false: location is not movable into
  */
	public Boolean canMoveInto(int x, int y) {
		if(getMapValue(x, y) == 1 || getMapValue(x, y) == 4) { //cannot move into wall or impassable tiles
			return false;
		} else {return true;}
	}
  
  /**
  * \brief   Check for whether it is possible for a ghost to move into a map location
  * \param[in]     x     Integer for x coordinate of map
  * \param[in]     y     Integer for y coordinate of map
  * \param[out]    Boolean     true: location is movable into, false: location is not movable into
  */
  public Boolean canGhostMoveInto(int x, int y) {
		if(getMapValue(x, y) == 1 || getMapValue(x, y) == 4 || getMapValue(x, y) == 5) { //cannot move into wall or impassable tiles
			return false;
		} else {return true;}
	}

  /**
  * \brief   Remove the object at a map location
  * \details   Add points if the object is a dot, cherry, or ghost. For large dots, enable Pacman's attack. For ghosts, either kill the ghost or Pacman depending on the attack state of Pacman. If the last point is consumed, progress to the next level.
  * \param[in]     x     Integer for x coordinate of map
  * \param[in]     y     Integer for y coordinate of map
  */
	public void consume(int x, int y) {
		if(grid[x][y].getName() == "2" || grid[x][y].getName() == "3" || grid[x][y].getName() == "7") { //only small dots, large dots, and cherries can be consumed

			//get image from the file folder
			try {
				emptyIMG = ImageIO.read(new File("images/empty.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			Image dEmptyIMG = emptyIMG.getScaledInstance(25, 25, Image.SCALE_SMOOTH); //scale image to 25 x 25

      if(grid[x][y].getName()=="2"){
				curScore+=10;
				scoreLabel.setText("Score:"+curScore);
        //add the consumed dot location as a potential cherry location
				possibleCherry[cherryArrayCount]=grid[x][y];
				cherryArrayCount++;
        
        nextLvlCount++;
        //186 dots on the map
				if(nextLvlCount==186){
					curLevel++;
          levelComplete = true;
				}
			} else if(grid[x][y].getName()=="3"){
        nextLvlCount++;
				curScore+=50;
				scoreLabel.setText("Score:"+curScore);
        //add the consumed dot location as a potential cherry location
				possibleCherry[cherryArrayCount]=grid[x][y];
				cherryArrayCount++;
        
        nextLvlCount++;
        //186 dots on the map
				if(nextLvlCount==186){
					curLevel++;
          levelComplete = true;
				}
        pacmanAttack = true; 
			} else if(grid[x][y].getName()=="7"){
			  	curScore+=100;
			  	scoreLabel.setText("Score:"+curScore);
			  	isCherry=false;
			}

			grid[x][y].setIcon(new ImageIcon(dEmptyIMG));
      grid[x][y].setName("0");
       //update grid with an empty image at the consume location
		} else {
			System.out.println("no dot at ("+x+", "+y+")");
		}
	}
  
  /**
  * \brief   Called when Pacman loses a life, resets ghost and Pacman positions.
  * \param[in]     pacmanPos     Integer array for Pacman's position
  * \param[in]     ghostPos     Integer 2D array for ghost positions
  */
  public void softReset(int[] pacmanPos, int[][] ghostPos) {
    Image ghostImg = new ImageIcon("images/orange_ghost.gif").getImage();
    Image pacImg = new ImageIcon("images/left.gif").getImage();
    curLives-=1;
    Image dEmptyIMG = emptyIMG.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
    for(int i = 0; i < ghostPos.length; i++){
      grid[ghostPos[i][0]][ghostPos[i][1]].setIcon(new ImageIcon(dEmptyIMG));
    }
    grid[9][8].setIcon(new ImageIcon(ghostImg));
    grid[pacmanPos[0]][pacmanPos[1]].setIcon(new ImageIcon(dEmptyIMG));
    grid[9][12].setIcon(new ImageIcon(pacImg));
    livesLabel.setText("Lives:"+curLives);
    collision = 0;
  }
	
  /**
  * \brief   Called when starting the game or when Pacman loses all lives. Resets all variables, positions, and visual objects.
  */
  public void reset() {
    //reset all value
    nextLvlCount = 0;
    cherryArrayCount = 0;
    curLives = 3; 
    collision = 0; 
    possibleCherry = new JLabel[182]; //array for possible cherry locations
    isCherry = false;
    levelComplete = false;

		//get images from the file folder
		try {
      ghostClyde = ImageIO.read(new File("images/orange_ghost.gif"));
			pacmanLeft = ImageIO.read(new File("images/Left.png"));
			wallIMG = ImageIO.read(new File("images/wall.png"));
			emptyIMG = ImageIO.read(new File("images/empty.png"));
			sDotIMG = ImageIO.read(new File("images/smallDot.png"));
			lDotIMG = ImageIO.read(new File("images/largeDot.png"));
      cherryIMG = ImageIO.read(new File("images/cherry.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//scale images to 25 x 25
		Image dPacmanLeft = pacmanLeft.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		Image dWallIMG = wallIMG.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		Image dEmptyIMG = emptyIMG.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		Image dSDot = sDotIMG.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		Image dLDot = lDotIMG.getScaledInstance(25, 25, Image.SCALE_SMOOTH);

		frame.getContentPane().removeAll(); //remove previous content in frame
		frame.setSize(476,610);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false); //prevent resizing
		frame.getContentPane().setBackground(Color.black);

		JPanel gPanel = new JPanel(new GridLayout(width, length)); //apply grid layout to game panel
		gPanel.setBackground(Color.black);
		frame.add(gPanel, BorderLayout.PAGE_START);
		JPanel iPanel = new JPanel(new GridLayout(1, 3)); //create interface panel for text information
		iPanel.setBackground(Color.black);
		frame.add(iPanel, BorderLayout.PAGE_END);

    Boolean isCherry = false;
  	JLabel[] possibleCherry = new JLabel[182]; //array for possible cherry locations
  	Integer cherryArrayCount = 0;

		//game grid
		grid = new JLabel[length][width]; //dimension the grid
		for(int y=0;y<width;y++) {
			for(int x=0;x<length;x++){
				if(staticMap[y][x] == 1) {
					JLabel tile = new JLabel(new ImageIcon(dWallIMG)); //if grid value = 1, display wall
          grid[x][y] = tile;
          grid[x][y].setName("1");
					gPanel.add(grid[x][y]);
				} else if(staticMap[y][x] == 2){
					JLabel tile = new JLabel(new ImageIcon(dSDot)); //if grid value = 2, display small dot
					grid[x][y] = tile;
					grid[x][y].setName("2");
					gPanel.add(grid[x][y]);
				} else if(staticMap[y][x] == 3){
					JLabel tile = new JLabel(new ImageIcon(dLDot)); //if grid value = 3, display large dot
					grid[x][y] = tile;
					grid[x][y].setName("3");
					gPanel.add(grid[x][y]);
				} else if(staticMap[y][x] == 5){
					JLabel tile = new JLabel(new ImageIcon(dEmptyIMG)); //if grid value = 3, display large dot
					grid[x][y] = tile;
					grid[x][y].setName("5");
					gPanel.add(grid[x][y]);
				} else if(staticMap[y][x] == 0){
					JLabel tile = new JLabel(new ImageIcon(dEmptyIMG)); //if grid value = 3, display large dot
					grid[x][y] = tile;
					grid[x][y].setName("0");
					gPanel.add(grid[x][y]);
				} else {
					JLabel tile = new JLabel(new ImageIcon(dEmptyIMG)); //else, display empty
					grid[x][y] = tile;
					gPanel.add(grid[x][y]);
				}
			}
		}

		grid[9][12].setIcon(new ImageIcon(dPacmanLeft));
    grid[9][8].setIcon(new ImageIcon(ghostClyde));

		//create interface labels
		scoreLabel = new JLabel("Score:"+curScore);
		scoreLabel.setFont(new Font("Sans-Serif", Font.BOLD, 15));
		scoreLabel.setForeground(Color.yellow);
		scoreLabel.setHorizontalAlignment(JLabel.CENTER);
    iPanel.add(scoreLabel);
    
    livesLabel = new JLabel("Lives:"+curLives);
    livesLabel.setFont(new Font("Sans-Serif", Font.BOLD, 15));
    livesLabel.setForeground(Color.yellow);
    livesLabel.setHorizontalAlignment(JLabel.CENTER);
    iPanel.add(livesLabel);
    
    levelLabel = new JLabel("Level:"+curLevel);
    levelLabel.setFont(new Font("Sans-Serif", Font.BOLD, 15));
    levelLabel.setForeground(Color.yellow);
    levelLabel.setHorizontalAlignment(JLabel.CENTER);
    iPanel.add(levelLabel);

    frame.setVisible(true); //makes frame visible
    levelLabel.setText("Level:"+curLevel); //update level

	}

  /**
  * \brief   Pick a random location to spawn a cherry from eligible locations.
  */
  public void spawnCherry() {
  	if(isCherry==false){ //if no cherry on map
  		try {
			cherryIMG = ImageIO.read(new File("images/cherry.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image cherry = cherryIMG.getScaledInstance(25, 25, Image.SCALE_SMOOTH); //scale image to 25 x 25

		//pick a random consumed dot location to spawn cherry on
		Integer cherryPos = r.ints(0, cherryArrayCount).findFirst().getAsInt();
		possibleCherry[cherryPos].setIcon(new ImageIcon(cherry));
		possibleCherry[cherryPos].setName("7");
  		isCherry=true;
  	}
  }

  /**
  * \brief   Update Pacman's visual object on the map to correspond to its new location.
  * \param[in]     past     Integer array for Pacman's previous position
  * \param[in]     future     Integer array for Pacman's future position
  * \param[in]     pacImg     Image object for Pacman's visual image
  */
  public void pacmanMove(int[] past, int[] future, Image pacImg) {
    try {
			emptyIMG = ImageIO.read(new File("images/empty.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image dEmptyIMG = emptyIMG.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
    if(staticMap[past[1]][past[0]] != 4 && staticMap[past[1]][past[0]] != 1){
      grid[past[0]][past[1]].setIcon(new ImageIcon(dEmptyIMG));
    }
    grid[future[0]][future[1]].setIcon(new ImageIcon(pacImg));
    pacmanLocation = new int[]{future[0],future[1]};
  }

  /**
  * \brief   Update a ghost's visual object on the map to correspond to its new location.
  * \param[in]     past     Integer array for the ghost's previous position
  * \param[in]     future     Integer array for the ghost's future position
  * \param[in]     ghostImg     Image object for the ghost's visual image
  * \param[out]    Boolean     Boolean for whether the ghost has moved into Pacman's location
  */
  public boolean ghostMove(int[] past, int[] future, Image ghostImg) {
    String item = getGridValue(past[0], past[1]);
    Image dEmptyIMG = emptyIMG.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		Image dSDot = sDotIMG.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		Image dLDot = lDotIMG.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
    Image processCherry = cherryIMG.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
    Image pastImg = null;
    switch(item){
      case "0": 
        pastImg = dEmptyIMG;
        break;
      case "2":
        pastImg = dSDot; 
        break;
      case "3":
        pastImg = dLDot; 
        break;
      case "7":
        pastImg = processCherry;
        break;
    }
    if((pacmanLocation[0]==past[0] && pacmanLocation[1]==past[1]) || (pacmanLocation[0]==future[0] && pacmanLocation[1]==future[1])){
      if(pacmanAttack){
        Image pacImg = new ImageIcon("images/left.gif").getImage();
        grid[9][8].setIcon(new ImageIcon(ghostImg));
        grid[past[0]][past[1]].setIcon(new ImageIcon(pacImg));
        curScore+=200;
				scoreLabel.setText("Score:"+curScore);
        collision = 2;
        return true;
      }
      else{
        collision = 1;
        grid[past[0]][past[1]].setIcon(new ImageIcon(pastImg));
        grid[future[0]][future[1]].setIcon(new ImageIcon(ghostImg));
        return true;
      }
    }
    else {
      grid[past[0]][past[1]].setIcon(new ImageIcon(pastImg));
      grid[future[0]][future[1]].setIcon(new ImageIcon(ghostImg));
      return false;
    }
  }

  /**
  * \brief   Reset a ghost's location and image after it is consumed by Pacman
  * \param[in]     past     Integer array for the ghost's previous position
  * \param[in]     ghostImg     Image object for the ghost's visual image
  */
  public void ghostReset(int[] past, Image ghostImg){
    Image dEmptyIMG = emptyIMG.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
    grid[past[0]][past[1]].setIcon(new ImageIcon(dEmptyIMG));
    grid[9][8].setIcon(new ImageIcon(ghostImg));
  }

  /**
  * \brief   Method to set the type of object collision
  * \param[in]     thing     Integer type of collision object 
  */
  public void setCollision(int thing){
    collision = thing;
  }

  /**
  * \brief   Getter method to get collision object type
  * \param[out]     collision     Integer type of collision object 
  */
  public int collision() {
    return collision; 
  }

  /**
  * \brief   Checker method for if Pacman has any lives left
  * \param[out]     Boolean     Boolean for if Pacman has 0 lives
  */
  public Boolean isDead(){
    return curLives==0 ? true : false;
  }

  /**
  * \brief   Getter method to get Pacman's attack state
  * \param[out]     pacmanAttack     Boolean of whether Pacman's attack is activated 
  */
  public Boolean getPacmanAttack(){
    return pacmanAttack; 
  }

  /**
  * \brief   Setter method to set Pacman's attack state
  * \param[in]     state     Boolean of Pacman's attack state 
  */
  public void setPacmanAttack(Boolean state){
    pacmanAttack = state; 
  }

  /**
  * \brief   Getter method to get Pacman's location
  * \param[out]     pacmanLocation     Integer 2D array of Pacman's location coordinates 
  */
  public int[] getPacmanLocation(){
    return pacmanLocation;
  }

}
