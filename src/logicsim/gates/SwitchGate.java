package logicsim.gates;

import logicsim.*;
import processing.core.*;

import static logicsim.wiretypes.BasicWire.*;

public class SwitchGate extends Gate {
  
  public SwitchGate(float x, float y, int rot, String name) {
    super(NOTHING, ONE_WIRE, x, y, rot, new PVector[0], new PVector[]{new PVector(45, 0)});
    this.name = name;
  }
  
  public static GateHandler handler() {
    return s -> {
      String[] a = s.nextLine().split(" ");
      String name = s.nextLine();
      return new SwitchGate(Float.parseFloat(a[0]), Float.parseFloat(a[1]), Integer.parseInt(a[2]), name);
    };
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
  public Gate cloneCircuit(float x, float y) {
    return new SwitchGate(x, y, rot, name);
  }
  
  @Override
  public void draw(PGraphics g) {
    g.rectMode(g.RADIUS);
    if (selected) {
      g.noStroke();
      g.fill(Main.SELECTED);
      g.rect(0, 0, 30, 37);
    }
    
    g.stroke(Main.CIRCUIT_BORDERS);
    g.fill(Main.CIRCUIT_COLOR);
    g.strokeWeight(3);
    g.line(25, 0, 45, 0);
    g.rect(0, 0, 25, 32);
    
    g.strokeWeight(1.5f);
    g.fill(on? Main.ON_LAMP : Main.OFF_LAMP);
    g.rect(0, 0, 17, 22);
    
    g.fill(Main.CIRCUIT_COLOR);
    g.rectMode(g.CORNERS);
    int bi = on? -1 : 1;
    float yp0 =  19 * bi;
    float yp  = -14 * bi;
    float yp2 = -14.5f * bi;
    float yp3 = -18 * bi;
    
    g.rect(-12, 0, 12, yp0);
    g.strokeCap(g.SQUARE);
    g.beginShape();
      g.vertex(15.2f, yp2);
      g.vertex(14, yp3);
      g.vertex(-14, yp3);
      g.vertex(-15.2f, yp2);
    g.endShape();
    g.quad(-12, 0, 12, 0, +15, yp, -15, yp);
//    g.quad(x-15, yp, x+15, yp, x+14, yp2, x-14, yp2);
    drawIO(g);
  }
  
  @Override
  public boolean in(float mx, float my) {
    return Math.abs(mx-x) < 25 && Math.abs(my-y) < 32;
  }
  
  public String def(float xoff, float yoff) {
    return "SwitchGate\n" + (x-xoff) + " " + (y-yoff) + " " + rot + "\n" + name;
  }
  
  @Override
  public void forwardConnection(Connection c) {
    boolean n = ((BasicConnection) c).b;
    if (n != on) {
      on = n;
      warn();
    }
  }
}
