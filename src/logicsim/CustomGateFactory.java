package logicsim;

import logicsim.gates.CustomGate;
import logicsim.wiretypes.BasicWire;
import processing.core.PVector;

import java.util.*;

public class CustomGateFactory {
  public ROCircuit c;
  private WireType[] its;
  private WireType[] ots;
  public String name;
  public final String[] ins;
  public final String[] ons;
  private ArrayList<CustomGate> instances;
  
  public CustomGateFactory(ROCircuit c, String name, String[] ins, String[] ons) {
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
        ROCircuit c = new ROCircuit();
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
        Main.mainBoard.add(f.create(0, 0, 0));
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
      throw new LoadException("bad input");
    } catch (NoSuchElementException e) {
      e.printStackTrace();
      throw new LoadException("input ended early");
    }
  }
  
  public CustomGate create(float x, float y, int rot) {
    int width = 30;
    int height = Math.max(Math.max(its.length, ots.length) * 10, 20);
    PVector[] ips = new PVector[its.length];
    for (int i = 0; i < its.length; i++) {
      ips[i] = new PVector(-width-20, (i-its.length/2f+.5f)*20);
    }
    PVector[] ops = new PVector[ots.length];
    for (int i = 0; i < ots.length; i++) {
      ops[i] = new PVector(width+20, (i-ots.length/2f+.5f)*20);
    }
    CustomGate cg = new CustomGate(its.clone(), ots.clone(), x, y, rot, this, ips, ops, width, height);
    instances.add(cg);
    return cg;
  }
}
