package logicsim.wiretypes;

import logicsim.*;
import processing.core.PGraphics;

import java.util.Arrays;

public class BasicWire extends WireType {
  private static WireType WIRE = new BasicWire();
  public static WireType[] TWO_WIRES = new WireType[]{WIRE, WIRE};
  public static WireType[] ONE_WIRE = new WireType[]{WIRE};
  public static WireType[] NOTHING = new WireType[]{};
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
    public void draw(PGraphics g, float x, float y, float x2, float y2) {
      float f = 30;
      g.strokeCap(g.SQUARE);
      g.noFill();
      
      g.strokeWeight(7);
      g.stroke(Main.CIRCUIT_BORDERS);
      g.bezier(x2, y2, x2+f, y2, x-f, y, x, y);
  
  
      g.strokeWeight(4);
      if (b) g.stroke(Main.ON_COLOR);
      else g.stroke(Main.OFF_COLOR);
      g.bezier(x2, y2, x2+f, y2, x-f, y, x, y);
//      g.line(x, y, x2, y2);
    }
    @Override
    public String toString() {
      return "bw @" + Integer.toHexString(hashCode()) + Arrays.toString(out);
    }
  }
  
}
