package logicsim;

import logicsim.gui.Drawable;
import processing.core.*;

import java.util.*;

public abstract class Circuit extends Drawable {
  public ArrayList<Gate> gates;
  
  Circuit(int x, int y, int w, int h) {
    super(x, y, w, h);
    gates = new ArrayList<>();
  }
  
  void add(Gate... gates) {
    Collections.addAll(this.gates, gates);
  }
  
  int pmx, pmy;
  float offX, offY;
  float scale = 1; // 2 = 1/4th seen from before
  
  @Override
  protected void mouseWheel(int i) {
    double sc = i==1? .8 : 1/.8;
    double pS = scale;
    scale*= sc;
    double scaleChange = 1/scale - 1/pS;
    offX -= (Main.mX * scaleChange);
    offY -= (Main.mY * scaleChange);
  }
  
  void drawHeld(PGraphics g) {
    if (held == null) return;
    g.pushMatrix();
      g.translate(-offX * scale, -offY * scale);
      g.scale(scale);
      g.translate(held.x, held.y);
      held.draw(g);
    g.popMatrix();
  }
  
  public void draw(PGraphics g) {
    g.pushMatrix();
    if (mmpressed) {
      offX += (pmx-Main.mX) / scale;
      offY += (pmy-Main.mY) / scale;
    }
    g.translate(-offX * scale, -offY * scale);
    g.scale(scale);
    if (held != null) {
      if (t == HoldType.gate) {
        float dx = (Main.mX-pmx) / scale;
        float dy = (Main.mY-pmy) / scale;
        if (!held.selected) {
//          if(!selected.isEmpty()) unselectAll();
          held.x+= dx;
          held.y+= dy;
        } else {
          for (Gate cg : selected) {
            cg.x+= dx;
            cg.y+= dy;
          }
        }
      }
    }
    for (Gate gate : gates) gate.drawConnections(g);
    for (Gate cg : gates) {
      g.pushMatrix();
        g.translate(cg.x, cg.y);
        g.rotate(cg.rot * PConstants.HALF_PI);
        cg.draw(g);
      g.popMatrix();
    }
    if (t == HoldType.select) {
      g.noStroke();
      g.fill(Main.SELECTED);
      g.rectMode(g.CORNERS);
      g.rect(selectX, selectY, fX(Main.mX), fY(Main.mY));
    }
    if (held != null) {
      if (t == HoldType.out) {
        PVector p = held.opsr[heldPos];
        g.stroke(Main.CIRCUIT_BORDERS);
        g.line(fX(Main.mX), fY(Main.mY), p.x+held.x, p.y+held.y);
      }
      if (t == HoldType.in) {
        PVector p = held.ipsr[heldPos];
        g.stroke(Main.CIRCUIT_BORDERS);
        g.line(fX(Main.mX), fY(Main.mY), p.x+held.x, p.y+held.y);
      }
    }
    pmx = Main.mX;
    pmy = Main.mY;
    g.popMatrix();
  }
  
  void unselectAll() {
    while (!selected.isEmpty()) unselect(selected.get(0));
  }
  
  boolean lmpressed = false;
  boolean mmpressed;
  Gate held;
  
  public float fX(float mX) {
    return mX/scale + offX;
  }
  public float fY(float mY) {
    return mY/scale + offY;
  }
  
  private Gate clicked;
  @Override
  public void rightPressedI() {
    float mX = fX(Main.mX);
    float mY = fY(Main.mY);
    for (int i = gates.size()-1; i>=0; i--) {
      Gate g = gates.get(i);
      if (g.in(mX, mY)) {
        clicked = g;
        g.click();
        break;
      }
    }
  }
  @Override
  public void rightReleasedI() {
    if (clicked != null) {
      clicked.unclick();
      clicked = null;
    }
  }
  
  void removeSelected() {
    while(!selected.isEmpty()) {
      Gate g = selected.get(0);
      unselect(g);
      g.delete();
      gates.remove(g);
    }
  }
  
  ArrayList<Gate> selected = new ArrayList<>();
  
  @Override
  public void simpleClickI() {
    float mX = fX(Main.mX);
    float mY = fY(Main.mY);
    for (int i = gates.size()-1; i>=0; i--) {
      Gate g = gates.get(i);
      if (g.in(mX, mY)) {
//        g.click(); // uncomment to have left click click things
        if (selected.contains(g)) unselect(g);
        else {
          if (!Main.shiftPressed) unselectAll();
          select(g);
        }
        break;
      }
    }
  }
  
  private void select(Gate g) {
    selected.add(g);
    g.selected = true;
  }
  private void unselect(Gate g) {
    selected.remove(g);
    g.selected = false;
  }
  
  
  @Override
  public void middlePressedI() {
    mmpressed = true;
  }
  
  @Override
  public void middleReleasedI() {
    mmpressed = false;
  }
  
  
  public void importStr(Scanner sc, float x, float y) throws LoadException {
    try {
      unselectAll();
      HashMap<Integer, Gate> m = new HashMap<>();
      String key = sc.nextLine();
      if (!key.equals("G")) throw new LoadException("Not a gate list!");
      int gam = Integer.parseInt(sc.nextLine());
      for (int i = 0; i < gam; i++) {
        String name = sc.nextLine();
        if (!Main.handlers.containsKey(name)) throw new LoadException("No gate "+name+" found in the library");
//        System.out.println(name);
        Gate g = Main.handlers.get(name).createFrom(sc);
//        System.out.println("done");
        m.put(i, g);
        g.x+= x;
        g.y+= y;
        gates.add(g);
        select(g);
      }
      int cam = Integer.parseInt(sc.nextLine());
      for (int i = 0; i < cam; i++) {
        String[] ln = sc.nextLine().split(" ");
        Gate og = m.get(Integer.parseInt(ln[0])); // outputting gate
        int oi = Integer.parseInt(ln[1]);
        Gate ig = m.get(Integer.parseInt(ln[2]));
        int ii = Integer.parseInt(ln[3]);
        ig.setInput(ii, og.os[oi]);
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
      throw new LoadException("badly formatted input");
    }
  }
  
  public static String exportStr(ArrayList<Gate> gs) { // outputs a trailing newline
    HashMap<Gate, Integer> m = new HashMap<>();
    StringBuilder o = new StringBuilder("G\n" + gs.size() + "\n");
    float lx = Float.POSITIVE_INFINITY, ly = Float.POSITIVE_INFINITY, bx = Float.NEGATIVE_INFINITY, by = Float.NEGATIVE_INFINITY;
    for (Gate g : gs) {
      if (g.x < lx) lx = g.x;
      if (g.y < ly) ly = g.y;
      if (g.x > bx) bx = g.x;
      if (g.y > by) by = g.y;
    }
    for (int i = 0; i < gs.size(); i++) {
      Gate g = gs.get(i);
      o.append(g.def((lx+bx) / 2, (ly+by)/2)).append("\n");
      m.put(g, i);
    }
    StringBuilder cs = new StringBuilder();
    int ctr = 0;
    for (Gate g : gs) {
      Connection[] is = g.is;
      for (int i = 0; i < is.length; i++) {
        Connection c = is[i];
        if (m.containsKey(c.in)) {
          cs.append(m.get(c.in)).append(" ").append(c.ip).append(" ").append(m.get(g)).append(" ").append(i).append("\n");
          ctr++;
        }
      }
    }
    o.append(ctr).append("\n");
    o.append(cs);
    return o.toString();
  }
  
  public Circuit readOnlyCopy(PGraphics g) {
    Circuit n = new ROCircuit();
    try {
      n.importStr(new Scanner(exportStr(gates)), 0, 0);
    } catch (LoadException e) {
      e.printStackTrace();
      throw new IllegalStateException("Circuit::readOnlyCopy failed to export & import");
    }
    return n;
  }
  
  enum HoldType {
    gate, in, out, select
  }
  private float selectX, selectY;
  HoldType t;
  private int heldPos;
  @Override
  protected void leftPressedI() {
    float mX = fX(Main.mX);
    float mY = fY(Main.mY);
    lmpressed = true;
    for (int i = gates.size()-1; i>=0; i--) {
      Gate g = gates.get(i);
      if (g.inIn(mX, mY) != -1) {
        held = g;
        heldPos = g.inIn(mX, mY);
        t = HoldType.in;
        return;
      }
      if (g.outIn(mX, mY) != -1) {
        held = g;
        heldPos = g.outIn(mX, mY);
        t = HoldType.out;
        return;
      }
      if (g.in(mX, mY)) {
        held = g;
        t = HoldType.gate;
        return;
      }
    }
    selectX = mX;
    selectY = mY;
    t = HoldType.select;
  }
  
  @Override
  public void leftReleasedI() {
//    unselectAll();
    float mX = fX(Main.mX);
    float mY = fY(Main.mY);
    if (t == HoldType.out) {
      WireType type = held.ots[heldPos];
      for (int i = gates.size()-1; i>=0; i--) {
        Gate g = gates.get(i);
        if (g.inIn(mX, mY) != -1) {
          int p = g.inIn(mX, mY);
          if (type.equals(g.its[p]))
            g.setInput(p, held.os[heldPos]);
          g.warn();
          held.warn();
          break;
        }
      }
    }
    if (t == HoldType.in) {
      WireType type = held.its[heldPos];
      boolean found = false;
      for (int i = gates.size()-1; i>=0; i--) {
        Gate g = gates.get(i);
        if (g.outIn(mX, mY) != -1) {
          int p = g.outIn(mX, mY);
          if (type.equals(g.ots[p]))
            held.setInput(heldPos, g.os[p]);
          g.warn();
          held.warn();
          found = true;
          break;
        }
      }
      if (!found) {
        int i = 0;
        for (Connection c : held.is) if (c == held.is[heldPos]) i++;
        if (i == 1) held.is[heldPos].removeWarnable(held);
        held.is[heldPos] = held.its[heldPos].noConnection();
        held.is[heldPos].addWarnable(held);
        held.warn();
      }
    }
    if (t == HoldType.gate) {
      if (Main.mX < 200) {
        held.delete();
        gates.remove(held);
      }
    }
    if (t == HoldType.select) {
      unselectAll();
      float lx = Math.min(selectX, mX);
      float bx = Math.max(selectX, mX);
      float ly = Math.min(selectY, mY);
      float by = Math.max(selectY, mY);
      for (int i = gates.size()-1; i>=0; i--) {
        Gate g = gates.get(i);
        if (g.x>lx && g.x<bx  &&  g.y>ly && g.y<by) {
          select(g);
        }
      }
    }
    lmpressed = false;
    held = null;
    t = null;
    heldPos = -1;
  }
}