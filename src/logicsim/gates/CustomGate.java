package logicsim.gates;

import logicsim.*;
import processing.core.PVector;

public class CustomGate extends Gate {
  Circuit c;
  public CustomGate(WireType[] its, WireType[] ots, float x, float y, Circuit c) {
    super(its, ots, x, y);
    ips = new PVector[its.length];
    for (int i = 0; i < its.length; i++) {
      ips[i] = new PVector(-30, i*10);
    }
    ops = new PVector[ots.length];
    for (int i = 0; i < ots.length; i++) {
      ops[i] = new PVector(30, i*10);
    }
    this.c = c;
  }
  
  @Override
  public void process() {
  
  }
  
  @Override
  public Gate cloneCircuit(float x, float y) {
    return new CustomGate(its, ots, x, y, c);
  }
}
