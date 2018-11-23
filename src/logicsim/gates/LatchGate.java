package logicsim.gates;

import logicsim.*;
import processing.core.*;

import static logicsim.wiretypes.BasicWire.*;

public class LatchGate extends Gate {
  public LatchGate(float x, float y) {
    this(x, y, false);
  }
  
  private boolean on;
  
  private LatchGate(float x, float y, boolean on) {
    super(TWO_WIRES, ONE_WIRE, x, y);
    this.on = on;
    ips = new PVector[]{new PVector(-40, -10), new PVector(-40, 10)};
    ops = new PVector[]{new PVector(40, 0)};
    if (on) warn();
  }
  
  public static GateHandler handler() {
    return s -> {
      String[] a = s.nextLine().split(" ");
      String on = s.nextLine();
      return new LatchGate(Float.parseFloat(a[0]), Float.parseFloat(a[1]), on.equals("1"));
    };
  }
  
  @Override
  public void process() {
    BasicConnection oc = (BasicConnection) os[0];
    boolean a = ((BasicConnection) is[0]).b;
    boolean b = ((BasicConnection) is[1]).b;
    if (b) on = a;
    oc.set(on);
  }
  
  @Override
  public Gate cloneCircuit(float x, float y) {
    return new LatchGate(x, y, on);
  }
  
  @Override
  public String def(float xoff, float yoff) {
    return "LatchGate\n"+(x-xoff)+" "+(y-yoff)+"\n"+(on?"1":"0");
  }
  
  @Override
  public void draw(PGraphics g) {
    g.stroke(Main.CIRCUIT_BORDERS);
    g.fill(Main.CIRCUIT_COLOR);
    g.strokeWeight(3);
  
    g.rectMode(g.RADIUS);
    g.rect(x, y, 20, 20);
    g.line(x+20, y, x+40, y);
    g.line(x-20, y+10, x-40, y+10);
    g.line(x-20, y-10, x-40, y-10);
    g.fill(Main.TEXT_COLOR);
    g.textAlign(g.LEFT, g.CENTER);
    g.textSize(10);
    g.text("I", x-18, y-10);
    g.text("save", x-18, y+10);
    drawIO(g);
  }
}
