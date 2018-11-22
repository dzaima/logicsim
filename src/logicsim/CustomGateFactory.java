package logicsim;

import logicsim.gates.CustomGate;
import logicsim.wiretypes.BasicWire;

import java.util.*;

public class CustomGateFactory {
  Circuit c;
  WireType[] its;
  WireType[] ots;
  String name;
  private final String[] ins;
  private final String[] ons;
  private ArrayList<CustomGate> instances;
  
  public CustomGateFactory(Circuit c, String name, String[] ins, String[] ons) {
    this.c = c;
    this.name = name;
    its = new WireType[ins.length];
    for (int i = 0; i < its.length; i++) {
      its[i] = BasicWire.WIRE;
    }
    ots = new WireType[ons.length];
    for (int i = 0; i < ots.length; i++) {
      ots[i] = BasicWire.WIRE;
    }
    this.ins = ins;
    this.ons = ons;
    instances = new ArrayList<>();
  }
  
  static void load(Scanner sc) throws LoadException {
    try {
      int icam = Integer.parseInt(sc.nextLine());
      for (int i = 0; i < icam; i++) {
        String name = sc.nextLine();
        Circuit c = new Circuit();
        c.importStr(sc, 0, 0);
        int iam = Integer.parseInt(sc.nextLine());
        ArrayList<String> is = new ArrayList<>(iam);
        for (int j = 0; j < iam; j++) is.add(sc.nextLine());
        int oam = Integer.parseInt(sc.nextLine());
        ArrayList<String> os = new ArrayList<>(oam);
        for (int j = 0; j < oam; j++) os.add(sc.nextLine());
        sc.nextLine(); sc.nextLine(); // placeholders
  
        CustomGateFactory f = new CustomGateFactory(c, name, is.toArray(new String[0]), os.toArray(new String[0]));
        Main.gateLibrary.put(name, f);
        Main.board.add(f.create(0, 0));
      }
    } catch (NumberFormatException e) {
      throw new LoadException("bad input");
    }
  }
  
  public CustomGate create(float x, float y) {
    CustomGate cg = new CustomGate(its.clone(), ots.clone(), x, y, c.copy(), this);
    instances.add(cg);
    return cg;
  }
}
