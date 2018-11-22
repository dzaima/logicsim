package logicsim.gates;

import logicsim.*;
import processing.core.PVector;

import java.util.Scanner;

public class CustomGate extends Gate {
  private final CustomGateFactory f;
  Circuit c;
  public CustomGate(WireType[] its, WireType[] ots, float x, float y, Circuit c, CustomGateFactory f) {
    super(its, ots, x, y);
    this.f = f;
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
  
  public static GateHandler handler() {
    return new GateHandler() {
      @Override
      protected Gate createFrom(Scanner s) throws LoadException {
        String name = s.nextLine();
        if (!Main.gateLibrary.containsKey(name)) throw new LoadException("IC "+name+" unknown");
        String[] pos = s.nextLine().split(" ");
        CustomGateFactory f = Main.gateLibrary.get(name);
        return f.create(Float.parseFloat(pos[0]), Float.parseFloat(pos[1]));
      }
    };
  }
  
  @Override
  public void process() {
  
  }
  
  @Override
  public Gate cloneCircuit(float x, float y) {
    return new CustomGate(its, ots, x, y, c, f);
  }
}
