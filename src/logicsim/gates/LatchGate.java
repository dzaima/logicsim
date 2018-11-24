package logicsim.gates;

import logicsim.*;
import processing.core.*;

import static logicsim.wiretypes.BasicWire.*;

public class LatchGate extends Gate {
  public LatchGate(float x, float y, int rot) {
    this(x, y, rot, false);
  }
  
  private boolean on;
  
  private LatchGate(float x, float y, int rot, boolean on) {
    super(TWO_WIRES, ONE_WIRE, x, y, rot,
      new PVector[]{new PVector(-40, -10), new PVector(-40, 10)},
      new PVector[]{new PVector(40, 0)}
    );
    this.on = on;
    if (on) warn();
  }
  
  public static GateHandler handler() {
    return s -> {
      String[] a = s.nextLine().split(" ");
      String on = s.nextLine();
      return new LatchGate(Float.parseFloat(a[0]), Float.parseFloat(a[1]), Integer.parseInt(a[2]), on.equals("1"));
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
    return new LatchGate(x, y, rot, on);
  }
  
  @Override
  public String def(float xoff, float yoff) {
    return "LatchGate\n"+(x-xoff)+" "+(y-yoff)+" "+rot+"\n"+(on?"1":"0");
  }
  
  @Override
  public void draw(PGraphics g) {
    g.stroke(Main.CIRCUIT_BORDERS);
    g.fill(Main.CIRCUIT_COLOR);
    g.strokeWeight(3);
  
    g.rectMode(g.RADIUS);
    g.rect(0, 0, 20, 20);
    g.line(20, 0, 40, 0);
    g.line(-20,  10, -40,  10);
    g.line(-20, -10, -40, -10);
    g.fill(Main.TEXT_COLOR);
    g.textAlign(g.LEFT, g.CENTER);
    g.textSize(10);
    g.text("I", -18, -10);
    g.text("save", -18, 10);
    drawIO(g);
  }
}
