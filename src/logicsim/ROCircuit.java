package logicsim;

import processing.core.PGraphics;

import java.util.Scanner;

public class ROCircuit extends Circuit {
  public float lx, ly, bx, by;
  
  public ROCircuit() {
    super(0, 0, 0, 0);
  }
  
  public void calculateEdges() {
    lx = ly = Float.POSITIVE_INFINITY;
    bx = by = Float.NEGATIVE_INFINITY;
    for (Gate g : gates) {
      if (g.x < lx) lx = g.x-20;
      if (g.x > bx) bx = g.x+20;
      if (g.y < ly) ly = g.y-20;
      if (g.y > by) by = g.y+20;
    }
  }
  
  @Override
  public void leftPressedI() { }
  
  @Override
  public void leftReleasedI() { }
  
  @Override
  public ROCircuit readOnlyCopy(PGraphics g) {
    ROCircuit n = new ROCircuit();
    try {
      n.importStr(new Scanner(exportStr(gates)), 0, 0);
    } catch (LoadException e) {
      e.printStackTrace();
      throw new IllegalStateException("Circuit::readOnlyCopy failed to export & import");
    }
    n.unselectAll();
    return n;
  }
}
