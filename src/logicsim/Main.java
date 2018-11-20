package logicsim;

import logicsim.gates.*;
import processing.core.*;
import processing.event.MouseEvent;

import java.util.*;
public class Main extends PApplet {
  public static final int CIRCUIT_COLOR = 0xffffffff;
  public static final int CIRCUIT_BORDERS = 0xff000000;
  public static final int CIRCUIT_TEXT = 0xff000000;
  public static final int INPUT_COLOR = 0xffffffff;
  public static final int OUTPUT_COLOR = 0xffddddff;
  public static final int OFF_COLOR = 0xff000000;
  public static final int ON_COLOR = 0xff6666ff;
  public static final int ON_LAMP = 0xff6666ff;
  public static final int OFF_LAMP = 0xffffffff;
  public static Set<Gate> next;
  private static Circuit board;
  
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[]{"logicsim.Main"};
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
  @Override
  public void settings() {
    size(500, 500);
//    smooth(0);
    
//    size(500, 500, "processing.javafx.PGraphicsFX2D");
    next = new HashSet<>();
    board = new Circuit();
    
//    TrueGate tg = new TrueGate(100, 200);
//
//    NandGate fg = new NandGate(100, 100);
//    fg.setInput(0, tg, 0);
//    fg.setInput(1, tg, 0);
//
//    NandGate nand = new NandGate(200, 50);
//    nand.setInput(0, tg, 0);
//    nand.setInput(1, fg, 0);
    
//    board.add(tg, fg, nand);
  }
  
  @Override
  public void setup() {
//    NandGate.createShape(this);
    println(next.size());
    
//    frameRate(2);
  }
  private boolean pmousePressed;
  private void step() {
    Set<Gate> todo = next;
    next = new HashSet<>();
    for (Gate g : todo) {
      g.process();
    }
    if (todo.size() != 0) println(todo.size());
  }
  @Override
  public void draw() {
    if (mousePressed && !pmousePressed) {
      if (mouseButton == LEFT) board.clicked(mouseX, mouseY);
      else board.rightClick(mouseX, mouseY);
    }
    if (!mousePressed && pmousePressed) {
      board.released(mouseX, mouseY);
    }
    step();
    background(200);
    board.draw(g, mouseX, mouseY);
    pmousePressed = mousePressed;
  }
  
  @Override
  public void keyPressed() {
    switch (key) {
      case '1':
        board.add(new TrueGate(board.fmx(mouseX), board.fmy(mouseY)));
        break;
      case '2':
        board.add(new NandGate(board.fmx(mouseX), board.fmy(mouseY)));
        break;
      case '3':
        board.add(new SwitchGate(board.fmx(mouseX), board.fmy(mouseY)));
        break;
      case '4':
        board.add(new LampGate(board.fmx(mouseX), board.fmy(mouseY)));
        break;
    }
  }
  
  @Override
  public void mouseWheel(MouseEvent event) {
    board.mouseWheel(event.getCount(), mouseX, mouseY);
  }
}