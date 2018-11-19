package logicsim.gates;

import logicsim.*;

import static logicsim.wiretypes.BasicWire.*;

public class NandGate extends Gate {
  public NandGate(float x, float y) {
    super(TWO_WIRES, ONE_WIRE, x, y);
  }


  @Override
  public void process() {
    BasicConnection ca = (BasicConnection) is[0];
    BasicConnection cb = (BasicConnection) is[1];
    BasicConnection oc = (BasicConnection) os[0];
    boolean a = (ca).b;
    boolean b = cb.b;
    oc.set(!(a & b));
  }
}
