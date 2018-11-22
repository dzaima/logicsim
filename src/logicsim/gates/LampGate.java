package logicsim.gates;

import logicsim.*;
import processing.core.*;

import java.util.Scanner;

import static logicsim.wiretypes.BasicWire.*;

public class LampGate extends Gate {
  private final String name;
  private boolean b;
  
  public LampGate(float x, float y, String name) {
    super(ONE_WIRE, NOTHING, x, y);
    this.name = name;
    ips = new PVector[]{new PVector(-20, 0)};
    ops = new PVector[0];
  }
  
  public static GateHandler handler() {
    return new GateHandler() {
      @Override
      protected Gate createFrom(Scanner s) {
        String[] a = s.nextLine().split(" ");
        String name = s.nextLine();
        return new LampGate(Float.parseFloat(a[0]), Float.parseFloat(a[1]), name);
      }
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
      g.ellipse(x, y, 25, 25);
    }
    g.fill(b? Main.ON_LAMP : Main.OFF_LAMP);
    g.stroke(Main.CIRCUIT_BORDERS);
    
    g.ellipse(x, y, 20, 20);
    drawIO(g);
  }
  
  @Override
  public Gate cloneCircuit(float x, float y) {
    return new LampGate(x, y, name);
  }
  
  
}
