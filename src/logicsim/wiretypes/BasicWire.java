package logicsim.wiretypes;

import logicsim.*;
import processing.core.PGraphics;

import java.util.Arrays;

public class BasicWire extends WireType {
  public static WireType WIRE = new BasicWire();
  public static WireType[] NOTHING = new WireType[]{};
  public static WireType[] ONE_WIRE = new WireType[]{WIRE};
  public static WireType[] TWO_WIRES = new WireType[]{WIRE, WIRE};
  public static final WireType[] FOUR_WIRES = new WireType[]{WIRE, WIRE, WIRE, WIRE};
  private static Connection NO_CONNECTION = new BasicConnection(null, -1) {
    @Override
    public String toString() {
      return "SINKHOLE "+super.toString();
    }
  };
  
  @Override
  public Connection noConnection() {
    return NO_CONNECTION;
  }
  
  @Override
  public Connection newConnection(Gate g, int ip) {
    return new BasicConnection(g, ip);
  }
  public static class BasicConnection extends Connection {
    public boolean b = false;
  
    BasicConnection(Gate in, int ip) {
      super(in, ip);
    }
  
    public void set(boolean b) {
      if (b != this.b) {
        this.b = b;
        for (Gate g : out) g.warn();
      }
    }
  
    @Override
    public void draw(PGraphics g, float x1, float y1, float x2, float y2, int r1, int r2) {
      float f = 30;
      g.strokeCap(g.SQUARE);
      g.noFill();
      int[] ri = Connection.rotations[r1];
      int[] ro = Connection.rotations[r2];
      
      g.strokeWeight(7);
      g.stroke(Main.CIRCUIT_BORDERS);
      g.bezier(
        x1, y1, x1 + f*ri[0], y1 + f*ri[1],
                x2 - f*ro[0], y2 - f*ro[1], x2, y2);
  
  
      g.strokeWeight(4);
      if (b) g.stroke(Main.ON_COLOR);
      else g.stroke(Main.OFF_COLOR);
      g.bezier(
        x1, y1, x1 + f*ri[0], y1 + f*ri[1],
                x2 - f*ro[0], y2 - f*ro[1], x2, y2);
    }
    @Override
    public String toString() {
      return "bw @" + Integer.toHexString(hashCode()) + Arrays.toString(out);
    }
  }
  
}
