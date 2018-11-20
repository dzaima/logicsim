package logicsim.gates;

import logicsim.*;
import processing.core.*;

import static logicsim.wiretypes.BasicWire.*;

public class SwitchGate extends Gate {
  public SwitchGate(float x, float y) {
    super(NOTHING, ONE_WIRE, x, y);
    ips = new PVector[0];
    ops = new PVector[]{new PVector(0, -20)};
  }
  
  @Override
  public void process() {
    BasicConnection oc = (BasicConnection) os[0];
    oc.set(on);
  }
  
  private boolean on;
  
  @Override
  public void click() {
    on = !on;
    warn();
  }
  
  @Override
  public void draw(PGraphics g) {
    g.strokeWeight(3);
    g.fill(Main.CIRCUIT_COLOR);
    g.stroke(Main.CIRCUIT_BORDERS);
    g.rectMode(g.RADIUS);
    g.rect(x, y, 20, 20);
    g.rect(x, y, 15, 15);
    drawIO(g);
  }
}
