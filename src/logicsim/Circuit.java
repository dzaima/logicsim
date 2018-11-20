package logicsim;

import processing.core.*;

import java.util.*;

class Circuit {
  private ArrayList<Gate> gates;
  Circuit() {
    gates = new ArrayList<>();
  }
  
  void add(Gate... gates) {
    Collections.addAll(this.gates, gates);
  }
  
  private int pmx, pmy;
  private float offX, offY;
  private float scale = 1; // 2 = 1/4th seen from before
  
  void mouseWheel(int i, int mx, int my) {
    double sc = i==1? .8 : 1/.8;
    double pS = scale;
    scale*= sc;
    double scaleChange = 1/scale - 1/pS;
    offX -= (mx * scaleChange);
    offY -= (my * scaleChange);
  }
  
  void draw(PGraphics g, int mx, int my) {for(Gate cg:gates) cg.in(fmx(mx),fmy(my));
    if (mpressed && held == null) {
      offX += (pmx-mx) / scale;
      offY += (pmy-my) / scale;
    }
    g.translate(-offX * scale, -offY * scale);
    g.scale(scale);
    for (Gate gate : gates) {
      gate.draw(g);
    }
    if (held != null) {
      if (t == HoldType.gate) {
        held.x+= (mx-pmx) / scale;
        held.y+= (my-pmy) / scale;
      }
      if (t == HoldType.out) {
        PVector p = held.ops[heldPos];
        g.stroke(Main.OFF_COLOR);
        g.line(fmx(mx), fmy(my), p.x+held.x, p.y+held.y);
      }
      if (t == HoldType.in) {
        PVector p = held.ips[heldPos];
        g.stroke(Main.OFF_COLOR);
        g.line(fmx(mx), fmy(my), p.x+held.x, p.y+held.y);
      }
    }
    pmx = mx;
    pmy = my;
  }
  private boolean mpressed = false;
  private Gate held;
  
  float fmx(float mX) {
    return mX/scale + offX;
  }
  float fmy(float mY) {
    return mY/scale + offY;
  }
  
  void rightClick(int imX, int imY) {
    float mX = fmx(imX);
    float mY = fmy(imY);
    for (Gate g : gates) {
      if (g.in(mX, mY)) g.click();
    }
  }
  
  enum HoldType {
    gate, in, out
  }
  private HoldType t;
  int heldPos;
  void clicked(int imX, int imY) {
    float mX = fmx(imX);
    float mY = fmy(imY);
    mpressed = true;
    for(Gate g : gates) {
      if (g.inIn(mX, mY) != -1) {
        held = g;
        heldPos = g.inIn(mX, mY);
        t = HoldType.in;
        break;
      }
      if (g.outIn(mX, mY) != -1) {
        held = g;
        heldPos = g.outIn(mX, mY);
        t = HoldType.out;
        break;
      }
      if (g.in(mX, mY)) {
        held = g;
        t = HoldType.gate;
        break;
      }
    }
  }
  
  void released(int imX, int imY) {
    float mX = fmx(imX);
    float mY = fmy(imY);
    if (t == HoldType.out) {
      WireType type = held.ots[heldPos];
      for (Gate g : gates) {
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
      for (Gate g : gates) {
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
    mpressed = false;
    held = null;
    t = null;
    heldPos = -1;
  }
}
