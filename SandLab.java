import java.awt.*;

public class SandLab {
  public static void main(String[] args)
  {
    new SandLab(120, 80).run();
  }
  
  public static class Particles {
    public String name;
    public Color color;
    public Particles(String n, Color c) { name = n; color = c;}
  }
  
  public static final int METAL = 0; public static final int[] METALCOLISSION = {0,1,11};
  public static final int WOOD = 1; public static final int[] WOODCOLISSION = {0,1,11};
  public static final int SAND = 2; public static final int[] SANDCOLISSION = {-1,0,1,3,9,11,13,14,15,16};
  public static final int WATER = 3; public static final int[] WATERCOLISSION = {-1,0,1,2,3,9,11,13,14,15,16};
  public static final int SMOKE = 4; public static final int[] SMOKECOLISSION = {0,1,2,11};
  public static final int FIRE = 5; public static final int[] FIRECOLISSION = {0,11};
  public static final int EMPTY = 6; public static final int[] EMPTYCOLISSION = {6};
  public static final int CONFETTI = 7; public static final int[] CONFETTICOLISSION = {-1,0,1,2,3,9,11,13,14,15,16};
  public static final int SPRINKLES = 8; public static final int[] SPRINKLESCOLISSION = {-1,0,1,2,3,8};
  public static final int DIRT = 9; public static final int[] DIRTCOLISSION = {-1,0,1,2,3,11,13,14,15,16};
  public static final int ACIDRAIN = 10; public static final int[] ACIDRAINCOLISSION = {-1,0,1,2,3,4,5,7,8,9,10,11,12,13,14,15,16};
  public static final int BOMB = 11; public static final int[] BOMBCOLISSION = {-1,0,1,2,3,4,5,7,8,9,10,11,12,13,14,15,16};
  public static final int STEAM = 12; public static final int[] STEAMCOLISSION = {-1,0,1,11};
  public static final int GRASS = 13; public static final int[] GRASSCOLISSION = {-1,0,1,11};
  public static final int PLANT = 14; public static final int[] PLANTCOLISSION = {-1,0,1,12};
  public static final int FLOWER1 = 15; public static final int[] FLOWER1COLISSION = {-1,0,1,13};
  public static final int FLOWER2 = 16; public static final int[] FLOWER2COLISSION = {-1,0,1};
  
  public static final int NUM_PARTICLES = 13;
  
  private int[][] grid;
  private SandDisplay display;
  private int numBombs;
  
  public SandLab(int numRows, int numCols)
  {
    String[] names = new String[NUM_PARTICLES];
    for (int i = 0; i < names.length; i++) {
      names[i] = getParticle(i).name;
    }
    display = new SandDisplay("Falling Sand", numRows, numCols, names);
    grid = new int[numRows][numCols];
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        grid[i][j] = EMPTY;
      }
    }
  }
  
  private Particles getParticle(int tool) {
    switch (tool) {
      case EMPTY:
      return new Particles("Empty", Color.BLACK);
      case METAL:
      return new Particles("Metal", Color.GRAY);
      case SMOKE:
      return new Particles("Smoke", Color.WHITE);
      case SAND:
      return new Particles("Sand", Color.YELLOW);
      case WOOD:
      return new Particles("Wood", new Color(210, 105, 30));
      case FIRE:
      return new Particles("Fire", Color.RED);
      case WATER:
      return new Particles("Water", Color.BLUE);
      case CONFETTI:
      return new Particles("Confetti", new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255)));
      case SPRINKLES:
      return new Particles("Sprinkles", new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255)));
      case DIRT:
      return new Particles("Dirt", new Color(58, 29, 0));
      case GRASS:
      return new Particles("Grass", Color.GREEN);
      case PLANT:
      return new Particles("Plant", new Color(38, 94, 0));
      case FLOWER1:
      return new Particles("Flower 1", Color.PINK);
      case FLOWER2:
      return new Particles("Flower 2", Color.MAGENTA);
      case ACIDRAIN:
      return new Particles("Acid Rain", new Color(102, 255, 0));
      case BOMB:
      return new Particles("Bomb", Color.BLUE);
      case STEAM:
      return new Particles("Steam", Color.LIGHT_GRAY);
      default:
      return new Particles("Empty", Color.BLACK);
    }
  }
  
  private int getGrid(int row, int col) {
    if (row >= grid.length || row < 0) return -1;
    if (col >= grid[0].length || col < 0) return -1;
    return grid[row][col];
  }
  
  private int setGrid(int row, int col, int tool, boolean override, int[] collision) {
    if (!override && getCollision(getGrid(row, col), collision)) return -1;
    if (row < 0 || row >= grid.length) return -1;
    if (col < 0 || col >= grid[0].length) return -1;
    grid[row][col] = tool;
    return 1;
  }
  
  private boolean getCollision(int tool, int[] collision) {
    for (int i = 0; i < collision.length; i++) {
      if (tool == collision[i]) return true;
    }
    return false;
  }
  
  private boolean random(int num) {
    return 0 == (int)(Math.random() * num);
  }
  
  //called when the user clicks on a location using the given tool
  private void locationClicked(int row, int col, int tool, boolean override, int size)
  {
    if (tool == ACIDRAIN && random(5)) return;
    if (grid[row][col] == tool && !override) return;
    if (tool == BOMB && numBombs > 0) return;
    if (tool == BOMB) numBombs++;
    grid[row][col] = tool;
    if (size > 1) {
      locationClicked((row+1 >= grid.length)?row:row+1, col, tool, override, size-1);
      locationClicked((row-1 < 0)?row:row-1, col, tool, override, size-1);
      locationClicked(row, (col+1 >= grid[0].length)?col:col+1, tool, override, size-1);
      locationClicked(row, (col-1 < 0)?col:col-1, tool, override, size-1);
    }
  }
  
  //copies each element of grid into the display
  public void updateDisplay()
  {				
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[j].length; j++) {
        display.setColor(i, j, getParticle(grid[i][j]).color);
      }
    }
  }
  
  //called repeatedly.
  //causes one random particle to maybe do something.
  public void step()
  {
    int row = (int)(Math.random() * grid.length);
    int col = (int)(Math.random() * grid[0].length);
    switch (grid[row][col]) {
      case EMPTY:
      break;
      case METAL:
      break;
      case SMOKE:
      if (random(10)) {
        if (random(7)) {setGrid(row, col, EMPTY, true, EMPTYCOLISSION); break;}
        if (random(4)) {
          if (-1 != setGrid(row, (random(2))?col+1:col-1, SMOKE, false, SMOKECOLISSION))
          setGrid(row, col, EMPTY, false, EMPTYCOLISSION);
        }
        if (-1 != setGrid(row-1, col, SMOKE, false, SMOKECOLISSION))
        setGrid(row, col, EMPTY, false, EMPTYCOLISSION);
      }
      break;
      case SAND:
      if (getGrid(row+1, col) == SAND) {
        if (getGrid(row+1, col+1) == EMPTY) {
          if (getGrid(row, col+1) == WATER) {
            setGrid(row, col+1, SAND, true, SANDCOLISSION);
            setGrid(row, col, WATER, true, WATERCOLISSION);
          }
          else {
            if (-1 != setGrid(row, col+1, SAND, false, SANDCOLISSION))
            setGrid(row, col, EMPTY, false, EMPTYCOLISSION);
          }
        }
        else if (getGrid(row+1, col-1) == EMPTY) {
          if (getGrid(row, col-1) == WATER) {
            setGrid(row, col-1, SAND, true, SANDCOLISSION);
            setGrid(row, col, WATER, true, WATERCOLISSION);
          }
          else {
            if (-1 != setGrid(row, col-1, SAND, false, SANDCOLISSION))
            setGrid(row, col, EMPTY, false, EMPTYCOLISSION);
          }
        }
      }
      else if (getGrid(row+1, col) == WATER) {
        setGrid(row+1, col, SAND, true, SANDCOLISSION);
        setGrid(row, col, WATER, true, WATERCOLISSION);
      }
      else {
        if (-1 != setGrid(row+1, col, SAND, false, SANDCOLISSION))
        setGrid(row, col, EMPTY, false, EMPTYCOLISSION);
      }
      break;
      case WOOD:
      if (random(10)) {
        if (getGrid(row, col+1) == FIRE) {
          setGrid(row, col, FIRE, true, FIRECOLISSION);
          setGrid(row+1, col, SMOKE, false, SMOKECOLISSION);
          setGrid(row-1, col, SMOKE, false, SMOKECOLISSION);
          setGrid(row, col+1, SMOKE, false, SMOKECOLISSION);
          setGrid(row, col-1, SMOKE, false, SMOKECOLISSION);
          break;
        }
        if (getGrid(row, col-1) == FIRE) {
          setGrid(row, col, FIRE, true, FIRECOLISSION);
          setGrid(row+1, col, SMOKE, false, SMOKECOLISSION);
          setGrid(row-1, col, SMOKE, false, SMOKECOLISSION);
          setGrid(row, col+1, SMOKE, false, SMOKECOLISSION);
          setGrid(row, col-1, SMOKE, false, SMOKECOLISSION);
          break;
        }
        if (getGrid(row+1, col) == FIRE) {
          setGrid(row, col, FIRE, true, FIRECOLISSION);
          setGrid(row+1, col, SMOKE, false, SMOKECOLISSION);
          setGrid(row-1, col, SMOKE, false, SMOKECOLISSION);
          setGrid(row, col+1, SMOKE, false, SMOKECOLISSION);
          setGrid(row, col-1, SMOKE, false, SMOKECOLISSION);
          break;
        }
        if (getGrid(row-1, col) == FIRE) {
          setGrid(row, col, FIRE, true, FIRECOLISSION);
          setGrid(row+1, col, SMOKE, false, SMOKECOLISSION);
          setGrid(row-1, col, SMOKE, false, SMOKECOLISSION);
          setGrid(row, col+1, SMOKE, false, SMOKECOLISSION);
          setGrid(row, col-1, SMOKE, false, SMOKECOLISSION);
          break;
        }
      }
      break;
      case FIRE:
      if (random(10)) {
        if (random(4)) {setGrid(row, col, EMPTY, true, EMPTYCOLISSION); break;}
        if (random(3)) {
          if (-1 != setGrid(row, (random(2))?col+1:col-1, FIRE, false, FIRECOLISSION))
          setGrid(row, col, EMPTY, false, EMPTYCOLISSION);
        }
        if (-1 != setGrid(row-1, col, FIRE, false, FIRECOLISSION))
        setGrid(row, col, EMPTY, true, EMPTYCOLISSION);
      }
      break;
      case WATER:
      if (random(2)) { 
        if (-1 != setGrid(row+1, col, WATER, false, WATERCOLISSION))
        setGrid(row, col, EMPTY, true, EMPTYCOLISSION);
        break;
      }
      if (random(2)) { 
        if (-1 != setGrid(row, col+1, WATER, false, WATERCOLISSION))
        setGrid(row, col, EMPTY, true, EMPTYCOLISSION);
      }
      else { 
        if (-1 != setGrid(row, col-1, WATER, false, WATERCOLISSION))
        setGrid(row, col, EMPTY, true, EMPTYCOLISSION);
      }
      break;
      case CONFETTI:
      if (random(20)) {setGrid(row, col, EMPTY, true, EMPTYCOLISSION); break;}
      int confettiRandom = (int)(Math.random() * 3);
      if (confettiRandom == 0) {
        if (-1 != setGrid(row-1, col, CONFETTI, false, CONFETTICOLISSION))
        if (!random(5)) setGrid(row, col, EMPTY, true, EMPTYCOLISSION);
      }
      if (confettiRandom == 1) {
        if (-1 != setGrid(row, col+1, CONFETTI, false, CONFETTICOLISSION))
        if (!random(5)) setGrid(row, col, EMPTY, true, EMPTYCOLISSION);
      }
      if (confettiRandom == 2) {
        if (-1 != setGrid(row, col-1, CONFETTI, false, CONFETTICOLISSION))
        if (!random(5)) setGrid(row, col, EMPTY, true, EMPTYCOLISSION);
      }
      break;
      case SPRINKLES:
      if (random(400)) {setGrid(row, col, EMPTY, true, EMPTYCOLISSION); break;}
      if (random(2)) { 
        if (-1 != setGrid(row+1, col, SPRINKLES, false, SPRINKLESCOLISSION))
        setGrid(row, col, EMPTY, true, EMPTYCOLISSION);
        break;
      }
      if (random(2)) { 
        if (-1 != setGrid(row, col+1, SPRINKLES, false, SPRINKLESCOLISSION))
        setGrid(row, col, EMPTY, true, EMPTYCOLISSION);
      }
      else { 
        if (-1 != setGrid(row, col-1, SPRINKLES, false, SPRINKLESCOLISSION))
        setGrid(row, col, EMPTY, true, EMPTYCOLISSION);
      }
      break;
      case DIRT:
      if (getGrid(row+1, col) == DIRT) {
        if (getGrid(row+1, col+1) == EMPTY) {
          if (getGrid(row, col+1) == WATER) {
            setGrid(row, col+1, DIRT, true, DIRTCOLISSION);
            setGrid(row, col, WATER, true, WATERCOLISSION);
          }
          else {
            if (-1 != setGrid(row, col+1, DIRT, false, DIRTCOLISSION))
            setGrid(row, col, EMPTY, false, EMPTYCOLISSION);
          }
        }
        else if (getGrid(row+1, col-1) == EMPTY) {
          if (getGrid(row, col-1) == WATER) {
            setGrid(row, col-1, DIRT, true, DIRTCOLISSION);
            setGrid(row, col, WATER, true, WATERCOLISSION);
          }
          else {
            if (-1 != setGrid(row, col-1, DIRT, false, DIRTCOLISSION))
            setGrid(row, col, EMPTY, false, EMPTYCOLISSION);
          }
        }
      }
      else if (getGrid(row+1, col) == WATER) {
        setGrid(row+1, col, DIRT, true, DIRTCOLISSION);
        setGrid(row, col, WATER, true, WATERCOLISSION);
      }
      else {
        if (-1 != setGrid(row+1, col, DIRT, false, DIRTCOLISSION))
        setGrid(row, col, EMPTY, false, EMPTYCOLISSION);
      }
      if (getGrid(row-1, col) == WATER) {
        setGrid(row-1, col, EMPTY, true, EMPTYCOLISSION);
        if (random(10)) {
          setGrid(row-1, col, GRASS, true, GRASSCOLISSION);
        }
      }
      break;
      case GRASS:
      if (getGrid(row-1, col) == WATER) {
        setGrid(row-1, col, EMPTY, true, EMPTYCOLISSION);
        if (random(10)) {
          setGrid(row-1, col, PLANT, true, PLANTCOLISSION);
        }
      }
      break;
      case PLANT:
      if (getGrid(row-1, col) == WATER) {
        setGrid(row-1, col, EMPTY, true, EMPTYCOLISSION);
        if (random(10)) {
          int choice = (int)(Math.random() * 6);
          if (choice == 0) {
            int random = (int)(Math.random() * 3);
            if (random == 0) {
              setGrid(row-1, col, PLANT, true, PLANTCOLISSION);
              break;
            }
            if (random == 1) {
              setGrid(row-1, col-1, PLANT, true, PLANTCOLISSION);
              break;
            }
            if (random == 2) {
              setGrid(row-1, col+1, PLANT, true, PLANTCOLISSION);
              break;
            }
          }
          if (choice == 1) {
            setGrid(row-1, col, FLOWER1, true, FLOWER1COLISSION);
            break;
          }
          if (choice == 2) {
            setGrid(row-1, col, FLOWER2, true, FLOWER2COLISSION);
            break;
          }
        }
      }
      break;
      case FLOWER1:
      if (getGrid(row-1, col) == WATER) {
        setGrid(row-1, col, EMPTY, true, EMPTYCOLISSION);
        if (random(10)) {
          int random1 = (int)(Math.random() * 3);
          if (random1 == 0) {
            setGrid(row-1, col, PLANT, true, PLANTCOLISSION);
            break;
          }
          if (random1 == 1) {
            setGrid(row-1, col-1, PLANT, true, PLANTCOLISSION);
            break;
          }
          if (random1 == 2) {
            setGrid(row-1, col+1, PLANT, true, PLANTCOLISSION);
            break;
          }
        }
      }
      if (random(500)) {
        if (1 == setGrid(row+1, col, CONFETTI, false, CONFETTICOLISSION)) break;
        if (1 == setGrid(row-1, col, CONFETTI, false, CONFETTICOLISSION)) break;
        if (1 == setGrid(row, col+1, CONFETTI, false, CONFETTICOLISSION)) break;
        if (1 == setGrid(row, col-1, CONFETTI, false, CONFETTICOLISSION)) break;
      }
      break;
      case FLOWER2:
      if (getGrid(row-1, col) == WATER) {
        setGrid(row-1, col, EMPTY, true, EMPTYCOLISSION);
        if (random(10)) {
          int random2 = (int)(Math.random() * 3);
          if (random2 == 0) {
            setGrid(row-1, col, PLANT, true, PLANTCOLISSION);
            break;
          }
          if (random2 == 1) {
            setGrid(row-1, col-1, PLANT, true, PLANTCOLISSION);
            break;
          }
          if (random2 == 2) {
            setGrid(row-1, col+1, PLANT, true, PLANTCOLISSION);
            break;
          }
        }
      }
      if (random(500)) {
        if (1 == setGrid(row+1, col, CONFETTI, false, CONFETTICOLISSION)) break;
        if (1 == setGrid(row-1, col, CONFETTI, false, CONFETTICOLISSION)) break;
        if (1 == setGrid(row, col+1, CONFETTI, false, CONFETTICOLISSION)) break;
        if (1 == setGrid(row, col-1, CONFETTI, false, CONFETTICOLISSION)) break;
      }
      break;
      case ACIDRAIN:
      int acidRandom = (int)(Math.random() * 3);
      if (acidRandom == 0) {
        if (-1 != setGrid(row+1, col, ACIDRAIN, false, ACIDRAINCOLISSION)) {
          setGrid(row, col, EMPTY, true, EMPTYCOLISSION);
        }
        else {
          setGrid(row, col, EMPTY, true, EMPTYCOLISSION);
          setGrid(row+1, col, EMPTY, true, EMPTYCOLISSION);
        }
      }
      if (acidRandom == 1) {
        if (-1 != setGrid(row, col+1, ACIDRAIN, false, ACIDRAINCOLISSION)) {
          setGrid(row, col, EMPTY, true, EMPTYCOLISSION);
        }
        else {
          setGrid(row, col, EMPTY, true, EMPTYCOLISSION);
          setGrid(row, col+1, EMPTY, true, EMPTYCOLISSION);
        }
      }
      if (acidRandom == 2) {
        if (-1 != setGrid(row, col-1, ACIDRAIN, false, ACIDRAINCOLISSION)) {
          setGrid(row, col, EMPTY, true, EMPTYCOLISSION);
        }
        else {
          setGrid(row, col, EMPTY, true, EMPTYCOLISSION);
          setGrid(row, col-1, EMPTY, true, EMPTYCOLISSION);
        }
      }
      break;
      case BOMB:
      if (random(5)) {
        if (getGrid(row+1, col) != EMPTY || getGrid(row-1, col) != EMPTY || getGrid(row, col+1) != EMPTY || getGrid(row, col-1) != EMPTY) {
          setGrid(row, col, EMPTY, true, EMPTYCOLISSION);
          locationClicked(row, col, EMPTY, true, 10);
          locationClicked(row, col, FIRE, true, 4);
          locationClicked(row, col, SMOKE, true, 2);
          numBombs--;
        }
        if (getGrid(row+1, col) == EMPTY) {
          setGrid(row, col, EMPTY, true, EMPTYCOLISSION);
          setGrid(row+1, col, BOMB, true, BOMBCOLISSION);
        }
      }
      break;
      case STEAM:
      if (random(2)) {
        if (getGrid(row-1, col) == EMPTY)  {
          int steamRandom = (int)(Math.random() * 3);
          if (steamRandom == 0) {
            if (-1 != setGrid(row-1, col, STEAM, false, STEAMCOLISSION))
            setGrid(row, col, EMPTY, true, EMPTYCOLISSION);
          }
          if (steamRandom == 1) {
            if (-1 != setGrid(row, col+1, STEAM, false, STEAMCOLISSION))
            setGrid(row, col, EMPTY, true, EMPTYCOLISSION);
          }
          if (steamRandom == 2) {
            if (-1 != setGrid(row, col-1, STEAM, false, STEAMCOLISSION))
            setGrid(row, col, EMPTY, true, EMPTYCOLISSION);
          }
        }
        if (random(800)){
          setGrid(row, col, WATER, true, WATERCOLISSION); 
        }
        if (random(500)) {
          setGrid(row, col, EMPTY, true, EMPTYCOLISSION); 
        }
      }
      break;
      default:
      break;
    }
  }
  
  public void run()
  {
    while (true)
    {
      for (int i = 0; i < display.getSpeed(); i++)
      step();
      updateDisplay();
      display.repaint();
      //display.pause(1);  //wait for redrawing and for mouse
      int[] mouseLoc = display.getMouseLocation();
      if (mouseLoc != null)
      locationClicked(mouseLoc[0], mouseLoc[1], display.getTool(), true, display.getToolSize());
    }
  }
}
