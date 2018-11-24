package logicsim.gates;

import logicsim.*;
import processing.core.*;

import static logicsim.wiretypes.BasicWire.*;

public class ButtonGate extends Gate {
  private boolean on;
  
  public ButtonGate(float x, float y, int rot) {
    super(NOTHING, ONE_WIRE, x, y, rot, new PVector[0], new PVector[]{new PVector(50, 0)});
  }
  
  public static GateHandler handler() {
    return s -> {
      String[] a = s.nextLine().split(" ");
      return new ButtonGate(Float.parseFloat(a[0]), Float.parseFloat(a[1]), Integer.parseInt(a[2]));
    };
  }
  
  @Override
  public void process() {
    BasicConnection oc = (BasicConnection) os[0];
    oc.set(on);
  }
  
  @Override
  public void draw(PGraphics g) {
    g.rectMode(g.RADIUS);
    if (selected) {
      g.noStroke();
      g.fill(Main.SELECTED);
      g.rect(0, 0, 30, 30);
    }
    
    g.strokeWeight(3);
    g.stroke(Main.CIRCUIT_BORDERS);
    g.line(25, 0, 50, 0);
  
    g.fill(Main.CIRCUIT_COLOR);
    g.stroke(Main.CIRCUIT_BORDERS);
    g.strokeWeight(3);
    g.ellipseMode(g.RADIUS);
    g.rect(0, 0, 25, 25);
  
    g.strokeWeight(1.5f);
    g.fill(on? Main.ON_LAMP : Main.OFF_LAMP);
    g.ellipse(0, 0, 20, 20);
    g.fill(Main.CIRCUIT_COLOR);
    g.ellipse(0, 0, 15, 15);
    drawIO(g);
  }
  
  @Override
  public void click() {
    on = true;
    warn();
  }
  
  @Override
  public void unclick() {
    on = false;
    warn();
  }
  
  @Override
  public boolean in(float mx, float my) {
    return Math.abs(mx-x) < 25 && Math.abs(my-y) < 25;
  }
  
  @Override
  public Gate cloneCircuit(float x, float y) {
    return new ButtonGate(x, y, rot);
  }
}
