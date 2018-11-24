package logicsim.gates;

import logicsim.*;
import processing.core.*;

import static logicsim.wiretypes.BasicWire.*;

public class NandGate extends Gate {

  public NandGate(float x, float y, int rot) {
    super(TWO_WIRES, ONE_WIRE, x, y, rot,
      new PVector[]{new PVector(-35, 10), new PVector(-35, -10)},
      new PVector[]{new PVector(40, 0)}
    );
  }
  
  public static GateHandler handler() {
    return s -> {
      String[] a = s.nextLine().split(" ");
      return new NandGate(Float.parseFloat(a[0]), Float.parseFloat(a[1]), Integer.parseInt(a[2]));
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
        g.vertex(-0, +25);
        g.vertex(-25, +25);
        g.vertex(-25, -25);
        g.vertex(-0, -25);
        for(int i = 0; i < 31; i++) {
          double r = i/30d * Math.PI;
          g.vertex((float) Math.sin(r) * 25, (float) Math.cos(r) * 25);
        }
      g.endShape(PConstants.CLOSE);
    }
  
    g.fill(Main.CIRCUIT_COLOR);
    g.stroke(Main.CIRCUIT_BORDERS);
  
    g.line( 20,   0,  40,   0); // output line
    g.line(-20,  10, -35,  10); // input lines
    g.line(-20, -10, -35, -10); // input lines
    g.ellipse(24, 0, 4, 4); // invert
  
    g.ellipse(0, 0, 20, 20); // elliptical body
  
    g.beginShape(); // rectangular body
      g.vertex(  0,  20);
      g.vertex(-20,  20);
      g.vertex(-20, -20);
      g.vertex(  0, -20);
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
    return new NandGate(x, y, rot);
  }
}
