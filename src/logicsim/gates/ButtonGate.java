package logicsim.gates;

import logicsim.*;
import processing.core.*;

import java.util.Scanner;

import static logicsim.wiretypes.BasicWire.*;

public class ButtonGate extends Gate {
  private boolean on;
  
  public ButtonGate(float x, float y) {
    super(NOTHING, ONE_WIRE, x, y);
    ips = new PVector[0];
    ops = new PVector[]{new PVector(50, 0)};
  }
  
  public static GateHandler handler() {
    return new GateHandler() {
      @Override
      protected Gate createFrom(Scanner s) {
        String[] a = s.nextLine().split(" ");
        return new ButtonGate(Float.parseFloat(a[0]), Float.parseFloat(a[1]));
      }
    };
  }
  
  @Override
  public void process() {
    BasicConnection oc = (BasicConnection) os[0];
    oc.set(on);
  }
  
  @Override
  public void draw(PGraphics g) {
    g.strokeWeight(3);
    g.stroke(Main.CIRCUIT_BORDERS);
    g.line(x+25, y, x+50, y);
  
    g.fill(Main.CIRCUIT_COLOR);
    g.stroke(Main.CIRCUIT_BORDERS);
    g.strokeWeight(3);
    g.rectMode(g.RADIUS);
    g.ellipseMode(g.RADIUS);
    g.rect(x, y, 25, 25);
  
    g.strokeWeight(1.5f);
    g.fill(on? Main.ON_LAMP : Main.OFF_LAMP);
    g.ellipse(x, y, 20, 20);
    g.fill(Main.CIRCUIT_COLOR);
    g.ellipse(x, y, 15, 15);
    drawIO(g);
  }
  
  @Override
  public void click() {
    on = true;
    warn();
  }
  
  @Override
  public void unclick() {
    on = false;
    warn();
  }
  
  @Override
  public boolean in(float mx, float my) {
    return Math.abs(mx-x) < 25 && Math.abs(my-y) < 25;
  }
  
  @Override
  public Gate cloneCircuit(float x, float y) {
    return new ButtonGate(x, y);
  }
}
