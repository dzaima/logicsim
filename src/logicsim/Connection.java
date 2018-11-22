package logicsim;

import org.jetbrains.annotations.*;
import processing.core.PGraphics;

public abstract class Connection {
  protected final Gate in;
  protected final int ip;
  protected Gate[] out;
  
  protected Connection(Gate in, int ip) {
    this.in = in;
    this.ip = ip;
    this.out = new Gate[0];
  }
  
  void addWarnable(@NotNull Gate g) {
//    System.out.println(this+" ADD "+g);
    if (contains(g)) return;
    Gate[] nout = new Gate[out.length+1];
    System.arraycopy(out, 0, nout, 0, out.length);
    nout[out.length] = g;
    out = nout;
  }
  
  void removeWarnable(@NotNull Gate g) {
//    System.out.println(this+" RM "+g);
    if (!contains(g)) return;
    Gate[] nout = new Gate[out.length-1];
    int p = 0;
    for (Gate c : out) {
      if (c != g) nout[p++] = c;
    }
    out = nout;
//    System.out.println(this+" RMED");
  }
  
  
  @Contract(pure = true)
  private boolean contains(@NotNull Gate g) {
    for (Gate c : out) {
      if (c == g) return true;
    }
    return false;
  }
  
  @Contract(pure = true)
  public void draw(PGraphics g, float x, float y, float x2, float y2) {
    g.strokeWeight(5);
    g.stroke(Main.OFF_COLOR);
    float f = 30;
    g.bezier(x, y, x+f, y, x2-f, y2, x2, y2);
//    g.line(x, y, x2, y2);
  }
}
