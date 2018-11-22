package logicsim.gates;

import logicsim.*;
import processing.core.*;

import static logicsim.wiretypes.BasicWire.BasicConnection;

public class CustomGate extends Gate {
  private final CustomGateFactory f;
  private Gate[] igs;
  private Gate[] ogs;
  private Circuit c;
  private int height;
  
  public CustomGate(WireType[] its, WireType[] ots, float x, float y, CustomGateFactory f) {
    super(its, ots, x, y);
    this.f = f;
    ips = new PVector[its.length];
    for (int i = 0; i < its.length; i++) {
      ips[i] = new PVector(-40, (i-its.length/2f+.5f)*20);
    }
    ops = new PVector[ots.length];
    for (int i = 0; i < ots.length; i++) {
      ops[i] = new PVector(40, (i-ots.length/2f+.5f)*20);
    }
    refresh();
  }
  
  private void refresh() {
    c = f.c.copy();
    igs = new Gate[its.length];
    for (int i = 0; i < f.ins.length; i++) {
      String name = f.ins[i];
      for (Gate g : c.gates)
        if (name.equals(g.name)) {
          igs[i] = g;
          break;
        }
      if (igs[i] == null) throw new Error("No input labeled "+name+" found");
    }
    ogs = new Gate[ots.length];
    for (int i = 0; i < f.ons.length; i++) {
      String name = f.ons[i];
      for (Gate g : c.gates)
        if (name.equals(g.name)) {
          ogs[i] = g;
          assert g instanceof LampGate;
          g.is[0].addWarnable(this);
          break;
        }
      if (ogs[i] == null) throw new Error("No output labeled "+name+" found");
    }
    height = Math.max(Math.max(is.length, os.length)*10, 20);
  }
  
  public static GateHandler handler() {
    return s -> {
      String name = s.nextLine();
      if (!Main.gateLibrary.containsKey(name)) throw new LoadException("IC "+name+" unknown");
      String[] pos = s.nextLine().split(" ");
      CustomGateFactory f = Main.gateLibrary.get(name);
      return f.create(Float.parseFloat(pos[0]), Float.parseFloat(pos[1]));
    };
  }
  
  @Override
  public void process() {
    for (int i = 0; i < igs.length; i++) {
      Gate g = igs[i];
//      System.out.println(i+"--"+g+"--"+ Arrays.toString(is));
      g.forwardConnection(is[i]);
    }
    for (int i = 0; i < ogs.length; i++) {
      Gate g = ogs[i];
      BasicConnection c = (BasicConnection) g.giveConnection();
      BasicConnection oc = (BasicConnection) os[i];
      oc.set(c.b);
    }
  }
  
  @Override
  public Gate cloneCircuit(float x, float y) {
    return new CustomGate(its, ots, x, y, f);
  }
  
  @Override
  public void draw(PGraphics g) {
    g.rectMode(g.RADIUS);
    if (selected) {
      g.noStroke();
      g.fill(Main.SELECTED);
      g.rect(x, y, 25, height + 5);
    }
    g.stroke(Main.CIRCUIT_BORDERS);
    g.fill(Main.CIRCUIT_COLOR);
    g.strokeWeight(3);
    g.rect(x, y, 20, height);
    for (int i = 0; i < its.length; i++) {
      float cy = y+(i-its.length/2f+.5f)*20;
      g.line(x-20, cy, x-40, cy);
    }
    for (int i = 0; i < ots.length; i++) {
      float cy = y+(i-ots.length/2f+.5f)*20;
      g.line(x+20, cy, x+40, cy);
    }
    drawIO(g);
  }
  
  @Override
  public boolean in(float mx, float my) {
    return Math.abs(mx-x) < 20 && Math.abs(my-y) < height;
  }
  
  @Override
  public String def(float xoff, float yoff) {
    String[] ss = getClass().getName().split("\\.");
    return ss[ss.length-1] + "\n" + f.name + "\n" + (x-xoff)+" "+(y-yoff);
  }
}
