package logicsim;

import org.jetbrains.annotations.Contract;
import processing.core.*;

import java.util.Scanner;

public abstract class Gate {
  public boolean selected = false;
  public int rot;
  public Connection[] is;
  protected Connection[] os;
  protected final WireType[] its;
  protected final WireType[] ots;
  protected float x, y;
  public String name;
  final protected PVector[] ips, ops;
  PVector[] ipsr, opsr;
  
  
  protected Gate(WireType[] its, WireType[] ots, float x, float y, int rot, PVector[] ips, PVector[] ops) {
    this.x = x;
    this.y = y;
    
    this.its = its;
    this.ots = ots;
  
    is = new Connection[its.length];
    os = new Connection[ots.length];
    this.ips = ips;
    this.ops = ops;
//    System.out.println("NG " + this);
    for (int i = 0; i < its.length; i++) {
      is[i] = its[i].noConnection();
      is[i].addWarnable(this);
    }
    for (int i = 0; i < ots.length; i++) {
      os[i] = ots[i].newConnection(this, i);
    }
  
  
    Main.next.add(this);
    ipsr = new PVector[ips.length];
    opsr = new PVector[ops.length];
    rotate(rot);
  }
  
  void rotate(int r) {
    rot = r;
    for (int i = 0; i < ips.length; i++) ipsr[i] = rotate(ips[i], r);
    for (int i = 0; i < ops.length; i++) opsr[i] = rotate(ops[i], r);
  }
  
  void setInput(int to, Connection newc) {
    Connection old = is[to];
    if (old == newc) return;
    
    is[to] = newc;
    newc.addWarnable(this);
  
    for (Connection c : is) if (c == old) return;
    old.removeWarnable(this);
  }
  
  public abstract void process();
  
  public void warn() {
    Main.next.add(this);
  }
  public void draw(PGraphics g) {
    g.strokeWeight(3);
    
    g.rectMode(g.RADIUS);
    g.rect(x, y, 20, 20);
    
    drawIO(g);
  }
  @Contract(pure = true)
  public boolean in(float mx, float my) {
    return Math.abs(mx-x) < 20 && Math.abs(my-y) < 20;
  }
  protected void drawIO(PGraphics g) {
    g.ellipseMode(g.RADIUS);
    g.strokeWeight(2);
    g.stroke(Main.CIRCUIT_BORDERS);
  
    g.fill(Main.INPUT_COLOR);
    for (PVector ip : ips) {
      g.ellipse(ip.x, ip.y, 7, 7);
    }
    
    g.fill(Main.OUTPUT_COLOR);
    for (PVector op : ops) {
      g.ellipse(op.x, op.y, 7, 7);
    }
  }
  
  void drawConnections(PGraphics g) {
    for (int i = 0; i < is.length; i++) {
      Connection c = is[i];
      PVector p = ipsr[i];
      if (c.in == null) continue;
      PVector p2 = c.in.opsr[c.ip];
      c.draw(g, x+p.x, y+p.y, p2.x+c.in.x, p2.y+c.in.y, rot, c.in.rot);
    }
  }
  
  @SuppressWarnings("SuspiciousNameCombination") // rot is strange
  private static PVector rotate(PVector p, int rotation) {
    switch (rotation) {
      case 0: return p;
      case 1: return new PVector(-p.y, p.x);
      case 2: return new PVector(-p.x, -p.y);
      case 3: return new PVector(p.y, -p.x);
    }
    throw new IllegalStateException("what");
  }
  
  int inIn(float mx, float my) {
    for (int i = 0; i < ips.length; i++) {
      PVector p = ipsr[i];
      if ((p.x+x-mx)*(p.x+x-mx) + (p.y+y-my)*(p.y+y-my) <= 14*14) return i;
    }
    return -1;
  }
  int outIn(float mx, float my) {
    for (int i = 0; i < ops.length; i++) {
      PVector p = opsr[i];
      if ((p.x+x-mx)*(p.x+x-mx) + (p.y+y-my)*(p.y+y-my) <= 14*14) return i;
    }
    return -1;
  }
  
  public void click() {
    // do nothing
  }
  
  public abstract Gate cloneCircuit(float x, float y);
  
  void delete() {
    for (Connection c : os) {
      for (Gate g : c.out) {
        for (int i = 0; i < g.is.length; i++) {
          if (g.is[i] == c) g.setInput(i, g.its[i].noConnection());
        }
      }
    }
    for (int i = 0; i < is.length; i++) {
      setInput(i, its[i].noConnection());
    }
  }
  
  public void unclick() {
    // do nothing
  }
  
  public String def(float xoff, float yoff) {
    String[] ss = getClass().getName().split("\\.");
    return ss[ss.length-1] + "\n"+(x-xoff)+" "+(y-yoff)+" "+ rot;
  }
  
  public void forwardConnection(Connection c) {
    throw new Error(this+" wasn't made to receive connections");
  }
  
  public Connection giveConnection() {
    throw new Error(this+" wasn't made to send connections");
  }
  
  protected interface GateHandler {
    Gate createFrom(Scanner s) throws LoadException;
  }
}