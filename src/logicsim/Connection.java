package logicsim;

import org.jetbrains.annotations.*;
import processing.core.PGraphics;

public abstract class Connection {
  protected final Gate in;
  protected final int ip;
  public Gate[] out;
  
  protected Connection(Gate in, int ip) {
    this.in = in;
    this.ip = ip;
    this.out = new Gate[0];
  }
  
  public void addWarnable(@NotNull Gate g) {
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
  
  protected static int[][] rotations = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};
  
  @Contract(pure = true)
  public void draw(PGraphics g, float x1, float y1, float x2, float y2, int r1, int r2) {
    g.strokeWeight(5);
    g.stroke(Main.OFF_COLOR);
    float f = 30;
    int[] ri = rotations[r1];
    int[] ro = rotations[r2];
    g.bezier(
      x1, y1, x1 + f*ri[0], y1 + f*ri[1],
      x2, y2, x2 - f*ro[0], y2 - f*ro[1]);
  }
}
