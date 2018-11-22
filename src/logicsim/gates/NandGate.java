package logicsim.gates;

import logicsim.*;
import processing.core.*;

import java.util.Scanner;

import static logicsim.wiretypes.BasicWire.*;

public class NandGate extends Gate {
  public NandGate(float x, float y) {
    super(TWO_WIRES, ONE_WIRE, x, y);
    ips = new PVector[]{new PVector(-35, 10), new PVector(-35, -10)};
    ops = new PVector[]{new PVector(40, 0)};
  }
  
  public static GateHandler handler() {
    return s -> {
      String[] a = s.nextLine().split(" ");
      return new NandGate(Float.parseFloat(a[0]), Float.parseFloat(a[1]));
    };
  }
  
  
  @Override
  public void process() {
    BasicConnection ca = (BasicConnection) is[0];
    BasicConnection cb = (BasicConnection) is[1];
    BasicConnection oc = (BasicConnection) os[0];
    boolean a = (ca).b;
    boolean b = cb.b;
    oc.set(!(a & b));
  }
  
  @Override
  public void draw(PGraphics g) {
    g.ellipseMode(g.RADIUS);
    g.strokeWeight(3);
    if (selected) {
      g.noStroke();
      g.fill(Main.SELECTED);
      g.beginShape();
      g.vertex(x-0, y+25);
      g.vertex(x-25, y+25);
      g.vertex(x-25, y-25);
      g.vertex(x-0, y-25);
      for(int i = 0; i < 31; i++) {
        double r = i/30d * Math.PI;
        g.vertex(x + (float) Math.sin(r) * 25, y + (float) Math.cos(r) * 25);
      }
      g.endShape(PConstants.CLOSE);
    }
    
    g.fill(Main.CIRCUIT_COLOR);
    g.stroke(Main.CIRCUIT_BORDERS);
    
    g.line(x+20 , y, x+40, y); // output line
    g.line(x-20, y+10, x-35, y+10); // input lines
    g.line(x-20, y-10, x-35, y-10); // input lines
    g.ellipse(x+24, y, 4, 4); // invert
    
    g.ellipse(x, y, 20, 20); // elliptical body
    
    g.beginShape(); // rectangular body
    g.vertex(x-0, y+20);
    g.vertex(x-20, y+20);
    g.vertex(x-20, y-20);
    g.vertex(x-0, y-20);
    g.endShape();
    drawIO(g);
  }
  
  @Override
  public boolean in(float mx, float my) {
    mx-=x;
    my-=y;
    return mx*mx + my*my <= 20*20  ||  my>0 && my<20 && Math.abs(mx)<20;
  }
  
  @Override
  public Gate cloneCircuit(float x, float y) {
    return new NandGate(x, y);
  }
}
