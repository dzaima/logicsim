package logicsim;

import logicsim.gates.*;
import processing.core.*;
import processing.event.*;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.*;
@SuppressWarnings("WeakerAccess") // public things are good
public class Main extends PApplet {
  public static final int SELECTED = 0x606666ff;
  public static final int TEXT_COLOR = 0xff000000;
  public static final int MENUBG = 100;
  public static final int BG = 0xffC8C8C8;
  public static final int SELBG = 180;
  public static final int CIRCUIT_COLOR = 0xffffffff;
  public static final int CIRCUIT_BORDERS = 0xff000000;
  public static final int CIRCUIT_TEXT = 0xff000000;
  public static final int INPUT_COLOR = 0xffffffff;
  public static final int OUTPUT_COLOR = 0xffb0e0ff;
  public static final int OFF_COLOR = 0xffffffff;
  public static final int ON_COLOR = 0xff2090e0;
  public static final int ON_LAMP = 0xff2090e0;
  public static final int OFF_LAMP = 0xffffffff;
  public static int ctr;
  public static Set<Gate> next;
  public static Main instance;
  static Circuit board;
  private SelectionCircuit selection;
  public static HashMap<String, CustomGateFactory> gateLibrary;
  static HashMap<String, Gate.GateHandler> handlers;
  static public void main(String[] passedArgs) {
    handlers = new HashMap<>();
    handlers.put("TrueGate", TrueGate.handler());
    handlers.put("SwitchGate", SwitchGate.handler());
    handlers.put("LampGate", LampGate.handler());
    handlers.put("NandGate", NandGate.handler());
    handlers.put("AndGate", AndGate.handler());
    handlers.put("ButtonGate", ButtonGate.handler());
    handlers.put("LatchGate", LatchGate.handler());
    handlers.put("NumGate", NumGate.handler());
    handlers.put("CustomGate", CustomGate.handler());
    gateLibrary = new HashMap<>();
    String[] appletArgs = new String[]{"logicsim.Main"};
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
  @Override
  public void settings() {
    instance = this;
    size(900, 600);
//    smooth(0);
    
//    size(500, 500, "processing.javafx.PGraphicsFX2D");
    next = new HashSet<>();
    board = new Circuit();
    selection = new SelectionCircuit();
    selection.add(
      new TrueGate(100, 50, 0),
      new NandGate(100, 100, 0),
      new AndGate(100, 150,0),
      new LatchGate(100, 200,0),
      new NumGate(100, 340, 0),
      new SwitchGate(100, 420, 0, ""),
      new LampGate(100, 490, 0, ""),
      new ButtonGate(100, 550, 0)
    );
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
  private boolean clickSelection;
  @Override
  public void setup() {
//    surface.setResizable(true);
  }
  private boolean pmousePressed;
  private void step() {
    Set<Gate> todo = next;
    next = new HashSet<>();
    for (Gate g : todo) {
      g.process();
    }
//    if (todo.size() != 0) println(todo.size());
  }
  static boolean shiftPressed;
  private int mct = 0;
  private int smb;
  @Override
  public void draw() {
    if (mousePressed && !pmousePressed) { // click
      smb = mouseButton;
      mct = millis();
      clickSelection = mouseX < 200;
      Circuit in = clickSelection? selection : board;
      if (smb == LEFT) {
        if (clickSelection) clickSelection = selection.clicked(mouseX, mouseY, board);
        else in.leftPressed(mouseX, mouseY);
      }
      else if (smb == RIGHT) {
        in.rightPressed(mouseX, mouseY);
      } else in.middleClick(mouseX, mouseY);
    }
    if (!mousePressed && pmousePressed) { // release
      Circuit in = clickSelection? selection : board;
      if (smb == RIGHT) {
        in.rightReleased();
      } else if (smb == LEFT) {
        in.leftReleased(mouseX, mouseY);
        if (millis() - mct < 200) {
          in.simpleClick(mouseX, mouseY);
        }
      } else {
        in.unMiddleClick(mouseX, mouseY);
      }
    }
    long ns = System.nanoTime();
    int d = 0;
    do {
      step();
      d++;
    } while (System.nanoTime() - ns < 200000L && next.size() > 0); // .2ms
    background(BG);
    
    board.draw(g, mouseX, mouseY);
    
    g.noStroke();
    g.fill(Main.SELBG);
    g.rectMode(g.CORNER);
    rect(0, 0, 200, height);
    
    g.clip(0, 0, 200, height);
    selection.draw(g, mouseX, mouseY);
    board.drawHeld(g);
    
    if (frameCount%60==0) System.out.println(frameRate+" "+next.size()+" "+d);
    pmousePressed = mousePressed;
//    System.out.println(ctr);
    ctr = 0;
  }
  
  @Override
  public void keyReleased(KeyEvent e) {
    shiftPressed = e.isShiftDown();
  }
  
  @Override
  public void keyPressed(KeyEvent e) {
    shiftPressed = e.isShiftDown(); // save selection
    if (key == 3 && keyCode == 67) {
      StringSelection selection = new StringSelection(Circuit.exportStr(board.selected));
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      clipboard.setContents(selection, selection);
    }
    if (key == 22 && keyCode == 86) { // load clip
      try {
        String s = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        board.importStr(new Scanner(s), board.fmX(mouseX), board.fmY(mouseY));
      } catch (NoSuchElementException | LoadException | UnsupportedFlavorException | IOException err) {
        err.printStackTrace();
      }
    }
    if (key == 9 && keyCode == 73) { // load IC
      try {
        String s = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        CustomGateFactory.load(new Scanner(s));
      } catch (NoSuchElementException | LoadException | UnsupportedFlavorException | IOException err) {
        err.printStackTrace();
      }
    }
//    System.out.println((+key)+" "+ keyCode);
    if (board.selected.size() == 1) {
      if (key >= ' ' && key <= '~' || key == 8) {
        Gate g = board.selected.get(0);
        if (g.name != null) {
          if (key == 8) {
            if (g.name.length() > 0) g.name = g.name.substring(0, g.name.length()-1);
          } else g.name+= key;
        }
      }
    }
    switch (key) {
//      case '1':
//        board.add(new TrueGate(board.fmX(mouseX), board.fmY(mouseY)));
//        break;
//      case '2':
//        board.add(new NandGate(board.fmX(mouseX), board.fmY(mouseY)));
//        break;
//      case '3':
//        board.add(new SwitchGate(board.fmX(mouseX), board.fmY(mouseY), ""));
//        break;
//      case '4':
//        board.add(new LampGate(board.fmX(mouseX), board.fmY(mouseY), ""));
//        break;
      case 'e':
        for (Gate g : board.selected)
          g.rotate((g.rot+1) % 4);
        break;
      case 'q':
        for (Gate g : board.selected)
          g.rotate((g.rot+3) % 4);
        break;
      case 'i': // create IC
        String s = Circuit.exportStr(board.selected);
        Circuit ic = new Circuit();
        try {
          ic.importStr(new Scanner(s), 0, 0);
        } catch (NoSuchElementException | LoadException err) {
          err.printStackTrace();
        }
        break;
      case 'c':
        next = new HashSet<>();
        break;
      case 127:
        board.removeSelected();
        break;
    }
  }
  
  @Override
  public void mouseWheel(MouseEvent event) {
    clickSelection = mouseX < 200;
    Circuit in = clickSelection? selection : board;
    in.mouseWheel(event.getCount(), mouseX, mouseY);
  }
}