package logicsim.gates;

import logicsim.*;

import static logicsim.wiretypes.BasicWire.*;

public class TrueGate extends Gate {
  public TrueGate(float x, float y) {
    super(NOTHING, ONE_WIRE, x, y);
  }
  
  @Override
  public void process() {
    ((BasicConnection) os[0]).set(true);
  }
}
