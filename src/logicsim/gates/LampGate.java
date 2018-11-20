package logicsim.gates;

import logicsim.*;
import processing.core.*;

import static logicsim.wiretypes.BasicWire.*;

public class LampGate extends Gate {
  private boolean b;
  
  public LampGate(float x, float y) {
    super(ONE_WIRE, NOTHING, x, y);
    ips = new PVector[]{new PVector(0, 20)};
    ops = new PVector[0];
  }
  
  @Override
  public void process() {
    BasicConnection ca = (BasicConnection) is[0];
    b = ca.b;
    
  }
  
  @Override
  public void draw(PGraphics g) {
    g.ellipseMode(g.CENTER);
    g.textAlign(g.CENTER, g.CENTER);
    
    g.fill(b? Main.ON_LAMP : Main.OFF_LAMP);
    g.stroke(Main.CIRCUIT_BORDERS);
    
    g.ellipse(x, y, 40, 40);
    drawIO(g);
  }
}
