package logicsim;

import logicsim.gates.*;
import processing.core.PApplet;

import java.util.*;

public class Main extends PApplet {
  public static Queue<Gate> next;
  
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
    TrueGate tg = new TrueGate(0, 0);
    Gate fg = new NandGate(100, 100);
    fg.addInput(0, tg, 0);
    fg.addInput(1, tg, 0);
  }
  
  @Override
  public void setup() {
    next = new ArrayDeque<>();
  }
  
  @Override
  public void draw() {
    Queue<Gate> todo = next;
    next = new ArrayDeque<>();
    for (Gate g : todo) {
      g.process();
    }
    println(todo.size());
  }
}