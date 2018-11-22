package logicsim.gates;

import logicsim.*;
import processing.core.*;

import java.util.Scanner;

import static logicsim.wiretypes.BasicWire.*;

public class TrueGate extends Gate {
  public TrueGate(float x, float y) {
    super(NOTHING, ONE_WIRE, x, y);
    ips = new PVector[0];
    ops = new PVector[]{new PVector(40, 0)};
  }
  
  public static GateHandler handler() {
    return s -> {
      String[] a = s.nextLine().split(" ");
      return new TrueGate(Float.parseFloat(a[0]), Float.parseFloat(a[1]));
    };
  }
  
  @Override
  public void process() {
    ((BasicConnection) os[0]).set(true);
  }
  
  @Override
  public void draw(PGraphics g) {
    g.rectMode(g.RADIUS);
    if (selected) {
      g.noStroke();
      g.fill(Main.SELECTED);
      g.rect(x, y, 25, 25);
    }
    
    g.stroke(Main.CIRCUIT_BORDERS);
    g.fill(Main.CIRCUIT_COLOR);
    g.strokeWeight(3);
  
    g.line(x+20, y, x+40, y);
  
    g.rect(x, y, 20, 20);
    
    g.fill(Main.CIRCUIT_TEXT);
    g.textAlign(g.CENTER, g.CENTER);
    g.textSize(20);
    g.text("1", x, y);
    
    drawIO(g);
  }
  
  @Override
  public boolean in(float mx, float my) {
    return Math.abs(mx-x) < 20 && Math.abs(my-y) < 20;
  }
  
  @Override
  public Gate cloneCircuit(float x, float y) {
    return new TrueGate(x, y);
  }
}
