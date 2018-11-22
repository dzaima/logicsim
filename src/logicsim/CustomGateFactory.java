package logicsim;

import logicsim.gates.CustomGate;

import java.util.ArrayList;

public class CustomGateFactory {
  Circuit c;
  WireType[] its;
  WireType[] ots;
  CustomGateFactory(Circuit c) {
    this.c = c;
    instances = new ArrayList<>();
  }
  private ArrayList<CustomGate> instances;
  CustomGate create(float x, float y) {
    CustomGate cg = new CustomGate(its.clone(), ots.clone(), x, y, c.copy());
    instances.add(cg);
    return cg;
  }
}
