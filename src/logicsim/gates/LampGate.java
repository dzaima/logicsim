package logicsim.gates;

import logicsim.*;
import processing.core.*;

import static logicsim.wiretypes.BasicWire.*;

public class LampGate extends Gate {
  private boolean b;
  
  public LampGate(float x, float y, int rot, String name) {
    super(ONE_WIRE, NOTHING, x, y, rot, new PVector[]{new PVector(-20, 0)}, new PVector[0]);
    this.name = name;
  }
  
  public static GateHandler handler() {
    return s -> {
      String[] a = s.nextLine().split(" ");
      String name = s.nextLine();
      return new LampGate(Float.parseFloat(a[0]), Float.parseFloat(a[1]), Integer.parseInt(a[2]), name);
    };
  }
  
  @Override
  public void process() {
    BasicConnection ca = (BasicConnection) is[0];
    b = ca.b;
    
  }
  
  @Override
  public void draw(PGraphics g) {
    g.ellipseMode(g.RADIUS);
    g.strokeWeight(3);
    if (selected) {
      g.noStroke();
      g.fill(Main.SELECTED);
      g.ellipse(0, 0, 25, 25);
    }
    g.fill(b? Main.ON_LAMP : Main.OFF_LAMP);
    g.stroke(Main.CIRCUIT_BORDERS);
    
    g.ellipse(0, 0, 20, 20);
    drawIO(g);
  }
  
  @Override
  public Gate cloneCircuit(float x, float y) {
    return new LampGate(x, y, rot, name);
  }
  
  public String def(float xoff, float yoff) {
    return "LampGate\n" + (x-xoff) + " " + (y-yoff) + " " + rot + "\n" + name;
  }
  
  @Override
  public Connection giveConnection() {
   return is[0];
  }
}
