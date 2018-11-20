package logicsim;

import org.jetbrains.annotations.*;
import processing.core.*;

public abstract class Gate {
  protected Connection[] is;
  protected final WireType[] its;
  protected Connection[] os;
  protected final WireType[] ots;
  protected float x;
  protected float y;
  
  protected Gate(@NotNull WireType[] its, @NotNull WireType[] ots, float x, float y) {
    this.x = x;
    this.y = y;
    
    this.its = its;
    this.ots = ots;
  
    is = new Connection[its.length];
    os = new Connection[ots.length];
//    System.out.println("NG " + this);
    for (int i = 0; i < its.length; i++) {
      is[i] = its[i].noConnection();
      is[i].addWarnable(this);
    }
    for (int i = 0; i < ots.length; i++) {
      os[i] = ots[i].newConnection(this, i);
//      System.out.println("OUT " + os[i]);
    }
//    System.out.println();
  
  
    Main.next.add(this);
  }
  
  protected void setInput(int to, Connection newc) {
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
  protected PVector[] ips;
  protected PVector[] ops;
  public void draw(PGraphics g) {
    g.strokeWeight(3);
    g.fill(Main.CIRCUIT_COLOR);
    g.stroke(Main.CIRCUIT_BORDERS);
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
    g.stroke(Main.CIRCUIT_BORDERS);
  
    g.fill(Main.INPUT_COLOR);
    for (PVector ip : ips) {
      g.ellipse(x + ip.x, y + ip.y, 7, 7);
    }
    
    g.fill(Main.OUTPUT_COLOR);
    for (PVector ip : ops) {
      g.ellipse(x + ip.x, y + ip.y, 7, 7);
    }
  
    for (int i = 0; i < is.length; i++) {
      Connection c = is[i];
      PVector p = ips[i];
      if (c.in == null) continue;
      PVector p2 = c.in.ops[c.ip];
      c.draw(g, x+p.x, y+p.y, p2.x+c.in.x, p2.y+c.in.y);
    }
    
  }
  
  int inIn(float mx, float my) {
    for (int i = 0; i < ips.length; i++) {
      PVector p = ips[i];
      if ((p.x+x-mx)*(p.x+x-mx) + (p.y+y-my)*(p.y+y-my) <= 8*8) return i;
    }
    return -1;
  }
  int outIn(float mx, float my) {
    for (int i = 0; i < ops.length; i++) {
      PVector p = ops[i];
      if ((p.x+x-mx)*(p.x+x-mx) + (p.y+y-my)*(p.y+y-my) <= 8*8) return i;
    }
    return -1;
  }
  
  public void click() {
    // do nothing
  }
}