package logicsim.gates;

import logicsim.*;
import processing.core.*;

import static logicsim.wiretypes.BasicWire.*;

public class NandGate extends Gate {
//  private static PShape sh;
//  public static void createShape(PApplet a) {
//    sh = a.createShape();
//    sh.beginShape();
//    sh.fill(Main.CIRCUIT_COLOR);
//    sh.strokeWeight(2);
//    sh.stroke(Main.CIRCUIT_BORDERS);
//    sh.vertex(-20, 0);
//    sh.vertex(-20, 20);
//    sh.vertex(20, 20);
//    sh.vertex(20, 0);
//    for(int i = 0; i < 30; i++) {
//      double r = i/30d * Math.PI + Math.PI/2;
//      sh.vertex((float) Math.sin(r) * 20, (float) Math.cos(r) * 20);
//    }
//    sh.endShape(PConstants.CLOSE);
//  }
  public NandGate(float x, float y) {
    super(TWO_WIRES, ONE_WIRE, x, y);
    ips = new PVector[]{new PVector(10, 35), new PVector(-10, 35)};
    ops = new PVector[]{new PVector(0, -40)};
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
    g.fill(Main.CIRCUIT_COLOR);
    g.stroke(Main.CIRCUIT_BORDERS);
    
    g.line(x, y-20, x, y-40); // output line
    g.line(x+10, y+20, x+10, y+35); // input lines
    g.line(x-10, y+20, x-10, y+35); // input lines
    g.ellipse(x, y-24, 4, 4); // invert
    
    g.ellipse(x, y, 20, 20); // elliptical body
    
    g.beginShape(); // rectangular body
    g.vertex(x-20, y+0);
    g.vertex(x-20, y+20);
    g.vertex(x+20, y+20);
    g.vertex(x+20, y+0);
    g.endShape();
    drawIO(g);
  }
  
  @Override
  public boolean in(float mx, float my) {
    mx-=x;
    my-=y;
    return mx*mx + my*my <= 20*20  ||  my>0 && my<20 && Math.abs(mx)<20;
  }
}
