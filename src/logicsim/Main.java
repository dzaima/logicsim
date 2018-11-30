package logicsim;

import logicsim.gates.*;
import logicsim.gui.*;
import processing.core.*;
import processing.event.*;

import java.util.*;
@SuppressWarnings("WeakerAccess") // public things are good
public class Main extends PApplet {
  public static /* final but not because intellij too smart */ String MODE = JAVA2D;
  
  public static final int TEXTFIELD_HEIGHT = 20;
  public static final int BUTTON_TEXT_COLOR = 0xffd2d2d2;
  public static final float TEXTFIELD_SIZE_FACTOR = .8f;
  
  public static final int BUTTON_COLOR = 0xff444444;
  public static final int BUTTON_STROKE = 0xff333333;
  
  public static final int SELECTED = 0x606666ff;
  public static final int TEXT_COLOR = 0xff000000;
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
  
  public static final int MENUBG = 100;
  public static final int MENU_STROKE = 40;
  public static final int TEXTFIELD_COLOR = 180;
  public static final int ICBG = 100;
  public static int ctr;
  public static Set<Gate> next;
  public static Main instance;
  public static HashMap<String, CustomGateFactory> gateLibrary;
  static HashMap<String, Gate.GateHandler> handlers;
  public static EditableCircuit mainBoard;
  public static Drawable window;
  public static int mX;
  public static int mY;
  
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
    size(1400, 800, MODE);
  }
  @Override
  public void setup() {
//    surface.setResizable(true);
    next = new HashSet<>();
    
    window = new Window(0, 0, width, height);
    
    mainBoard = new EditableCircuit(200, 0, width - 200, height);
    window.add(mainBoard);
    
    SelectionCircuit sc = new SelectionCircuit(0, 0, 200, height);
    window.add(sc);
    sc.add(
      new TrueGate(100, 50, 0),
      new NandGate(100, 100, 0),
      new AndGate(100, 150,0),
      new LatchGate(100, 200,0),
      new NumGate(100, 340, 0),
      new SwitchGate(100, 420, 0, ""),
      new LampGate(100, 490, 0, ""),
      new ButtonGate(100, 550, 0)
    );
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
    if (!MODE.equals(FX2D)) g.noClip();
//    try {
//      GraphicsContext n = (GraphicsContext) g.getNative();
//      Canvas c = n.getCanvas();
//      Method m = Canvas.class.getDeclaredMethod("getBuffer");
//      m.setAccessible(true);// Abracadabra
//      GrowableDataBuffer o = (GrowableDataBuffer) m.invoke(c);// now its OK
////      System.out.println(o.writeValuePosition() +" "+o.writeObjectPosition()+" "+o.objectCapacity());
//    } catch (Throwable e) {
//      e.printStackTrace();
//    }
    mX = mouseX;
    mY = mouseY;
    if (mousePressed && !pmousePressed) { // click
      smb = mouseButton;
      mct = millis();
      if (smb == LEFT) {
        window.leftPressed();
      }
      else if (smb == RIGHT) {
        window.rightPressed();
      } else window.middlePressed();
    }
    if (!mousePressed && pmousePressed) { // release
      if (smb == RIGHT) {
        window.rightReleased();
      } else if (smb == LEFT) {
        window.leftReleased();
        if (millis() - mct < 200) {
          window.simpleClick();
        }
      } else {
        window.middleReleased();
      }
    }
    long ns = System.nanoTime();
    int d = 0;
    do {
      step();
      d++;
    } while (System.nanoTime() - ns < 200000L && next.size() > 0); // .2ms
    background(BG);
    
    window.drawAll(g);
    
    
    
    if (!MODE.equals(FX2D)) g.clip(0, 0, 200, height);
    mainBoard.drawHeld(g);
    
//    if (frameCount%60==0) System.out.println(frameRate+" "+next.size()+" "+d+" "+frameCount);
//    if (frameCount>= 5000 && MODE.equals(PConstants.FX2D)) {
//      System.exit(0);
//    }
    pmousePressed = mousePressed;
//    System.out.println(ctr);
    ctr = 0;
  }
  
  @Override
  public void keyReleased(KeyEvent e) {
    shiftPressed = e.isShiftDown();
  }
  
  @Override
  public void keyTyped(KeyEvent e) {
//    System.out.println("T " + (+key)+" "+ keyCode+" "+e.isControlDown());
    Drawable.keyD(key, keyCode, e);
  }
  
  @Override
  public void keyPressed(KeyEvent e) {
//    System.out.println("P " + (+key)+" "+ keyCode+" "+e.isControlDown());
    shiftPressed = e.isShiftDown();
//    Drawable.keyD(key, keyCode, e);
    
    
  }
  
  @Override
  public void mouseWheel(MouseEvent event) {
    Drawable.mouseWheelD(event.getCount());
  }
}