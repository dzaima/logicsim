package logicsim;

import processing.core.*;

class SelectionCircuit extends Circuit {
  
  @Override
  void draw(PGraphics g, int mx, int my) {
    g.pushMatrix();
    if (mmpressed) {
      offX += (pmx-mx) / scale;
      offY += (pmy-my) / scale;
    }
    g.translate(-offX * scale, -offY * scale);
    g.scale(scale);
    for (Gate gate : gates) gate.draw(g);
    pmx = mx;
    pmy = my;
    g.popMatrix();
  }
  
  boolean clicked(int imX, int imY, Circuit c) {
    float mX = fmX(imX);
    float mY = fmY(imY);
    for (Gate g : gates) {
      if (g.in(mX, mY)) {
        Gate n = g.cloneCircuit(c.fmX(imX), c.fmY(imY));
        c.add(n);
        c.held = n;
        c.t = HoldType.gate;
        c.lmpressed = true;
        return false;
      }
    }
    lmpressed = true;
    return true;
  }
  
  @Override
  void rightPressed(int imX, int imY) {
  
  }
  
  @Override
  void rightReleased() {
  
  }
}
