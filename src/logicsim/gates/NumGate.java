package logicsim.gates;

import logicsim.*;
import processing.core.*;

import static logicsim.wiretypes.BasicWire.*;

public class NumGate extends Gate {
  private int i;
  
  public NumGate(float x, float y) {
    super(FOUR_WIRES, NOTHING, x, y);
    ips = new PVector[]{
      new PVector(-40, -30),
      new PVector(-40, -10),
      new PVector(-40,  10),
      new PVector(-40,  30),
    };
    ops = new PVector[0];
  }
  
  public static GateHandler handler() {
    return s -> {
      String[] a = s.nextLine().split(" ");
      return new NumGate(Float.parseFloat(a[0]), Float.parseFloat(a[1]));
    };
  }
  
  @Override
  public void process() {
    boolean a = ((BasicConnection) is[0]).b;
    boolean b = ((BasicConnection) is[1]).b;
    boolean c = ((BasicConnection) is[2]).b;
    boolean d = ((BasicConnection) is[3]).b;
    i = (a?1:0) + (b?2:0) + (c?4:0) + (d?8:0);
    
  }
  
  @Override
  public void draw(PGraphics g) {
    g.rectMode(g.RADIUS);
    if (selected) {
      g.noStroke();
      g.fill(Main.SELECTED);
      g.rect(x, y, 30, 40);
    }
  
    g.stroke(Main.CIRCUIT_BORDERS);
    g.fill(Main.CIRCUIT_COLOR);
    g.strokeWeight(3);
    g.rect(x, y, 25, 35);
    char s = "0123456789ABCDEF".charAt(i);
    g.fill(Main.TEXT_COLOR);
    g.textSize(60);
    g.textAlign(g.CENTER, g.CENTER);
    g.text(s, x, y-7);
    for (int i = 0; i < 4; i++) g.line(x-40, y-30+i*20, x-25, y-30+i*20);
    drawIO(g);
  }
  
  @Override
  public boolean in(float mx, float my) {
    return Math.abs(mx-x) < 25 && Math.abs(my-y) < 35;
  }
  
  @Override
  public Gate cloneCircuit(float x, float y) {
    return new NumGate(x, y);
  }
}
