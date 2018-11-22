package logicsim.gates;

import logicsim.*;
import processing.core.*;

import static logicsim.wiretypes.BasicWire.*;

public class SwitchGate extends Gate {
  
  public SwitchGate(float x, float y, String name) {
    super(NOTHING, ONE_WIRE, x, y);
    this.name = name;
    ips = new PVector[0];
    ops = new PVector[]{new PVector(45, 0)};
  }
  
  public static GateHandler handler() {
    return s -> {
      String[] a = s.nextLine().split(" ");
      String name = s.nextLine();
      return new SwitchGate(Float.parseFloat(a[0]), Float.parseFloat(a[1]), name);
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
    return new SwitchGate(x, y, name);
  }
  
  @Override
  public void draw(PGraphics g) {
    g.rectMode(g.RADIUS);
    if (selected) {
      g.noStroke();
      g.fill(Main.SELECTED);
      g.rect(x, y, 30, 37);
    }
    
    g.stroke(Main.CIRCUIT_BORDERS);
    g.fill(Main.CIRCUIT_COLOR);
    g.strokeWeight(3);
    g.line(x+25, y, x+45, y);
    g.rect(x, y, 25, 32);
    
    g.strokeWeight(1.5f);
    g.fill(on? Main.ON_LAMP : Main.OFF_LAMP);
    g.rect(x, y, 17, 22);
    
    g.fill(Main.CIRCUIT_COLOR);
    g.rectMode(g.CORNERS);
    int bi = on? -1 : 1;
    float yp0 = y + 19 * bi;
    float yp  = y - 14 * bi;
    float yp2 = y - 14.5f * bi;
    float yp3 = y - 18 * bi;
    
    g.rect(x-12, y, x+12, yp0);
    g.strokeCap(g.SQUARE);
    g.beginShape();
      g.vertex(x+15.2f, yp2);
      g.vertex(x+14, yp3);
      g.vertex(x-14, yp3);
      g.vertex(x-15.2f, yp2);
    g.endShape();
    g.quad(x-12, y, x+12, y, x+15, yp, x-15, yp);
//    g.quad(x-15, yp, x+15, yp, x+14, yp2, x-14, yp2);
    drawIO(g);
  }
  
  @Override
  public boolean in(float mx, float my) {
    return Math.abs(mx-x) < 25 && Math.abs(my-y) < 32;
  }
  
  public String def(float xoff, float yoff) {
    return "SwitchGate\n" + (x-xoff) + " " + (y-yoff) + "\n" + name;
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
