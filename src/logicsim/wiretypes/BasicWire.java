package logicsim.wiretypes;

import logicsim.*;

public class BasicWire extends WireType {
  private static WireType WIRE = new BasicWire();
  public static WireType[] TWO_WIRES = new WireType[]{WIRE, WIRE};
  public static WireType[] ONE_WIRE = new WireType[]{WIRE};
  public static WireType[] NOTHING = new WireType[]{};
  
  @Override
  public Connection newConnection(Gate g) {
    return new BasicConnection(g);
  }
  public static class BasicConnection extends Connection {
    public boolean b;
  
    BasicConnection(Gate g) {
      super(g);
    }
  
    public void set(boolean b) {
      if (b != this.b) {
        this.b = b;
        for (Gate g : out) g.warn();
      }
    }
  }
}
