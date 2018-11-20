package logicsim.gates;

import logicsim.*;
import processing.core.*;

import static logicsim.wiretypes.BasicWire.*;

public class TrueGate extends Gate {
  public TrueGate(float x, float y) {
    super(NOTHING, ONE_WIRE, x, y);
    ips = new PVector[0];
    ops = new PVector[]{new PVector(0, -20)};
  }
  
  @Override
  public void process() {
    ((BasicConnection) os[0]).set(true);
  }
  
  @Override
  public void draw(PGraphics g) {
    g.fill(Main.CIRCUIT_COLOR);
    g.stroke(Main.CIRCUIT_BORDERS);
    g.strokeWeight(3);
    g.rectMode(g.DIAMETER);
    g.rect(x, y, 40, 40);
    
    g.fill(Main.CIRCUIT_TEXT);
    g.textAlign(g.CENTER, g.CENTER);
    g.textSize(20);
    g.text("1", x, y);
    
    drawIO(g);
  }
  
  @Override
  public boolean in(float mx, float my) {
    return Math.abs(mx-x) < 20 && Math.abs(my-y) < 20;
  }
}
