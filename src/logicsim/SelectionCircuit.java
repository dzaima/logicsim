package logicsim;

import logicsim.gui.Drawable;
import processing.core.*;

public class SelectionCircuit extends Circuit {
  
  
  public SelectionCircuit(int x, int y, int w, int h) {
    super(x, y, w, h);
  }
  
  @Override
  public void draw(PGraphics g) {
    g.noStroke();
    g.fill(Main.SELBG);
    g.rectMode(g.CORNER);
    g.rect(x, y, w, h);
    g.pushMatrix();
    if (mmpressed) {
      offX += (pmx-Main.mX) / scale;
      offY += (pmy-Main.mY) / scale;
    }
    g.translate(-offX * scale, -offY * scale);
    g.scale(scale);
    for (Gate cg : gates) {
      g.pushMatrix();
      g.translate(cg.x, cg.y);
      g.rotate(cg.rot * PConstants.HALF_PI);
      cg.draw(g);
      g.popMatrix();
    }
    g.popMatrix();
    pmx = Main.mX;
    pmy = Main.mY;
  }
  
  @Override
  protected void leftPressedI() {
    float mX = fX(Main.mX);
    float mY = fY(Main.mY);
    EditableCircuit mb = Main.mainBoard;
    for (Gate g : gates) {
      if (g.in(mX, mY)) {
        Gate n = g.cloneCircuit(mb.fX(Main.mX), mb.fY(Main.mY));
        mb.add(n);
        mb.held = n;
        mb.t = HoldType.gate;
        mb.lmpressed = true;
        Drawable.focused = mb;
      }
    }
    lmpressed = true;
  }
  
  @Override
  public void rightPressedI() { }
  @Override
  public void rightReleasedI() { }
  @Override
  public void leftReleasedI() { }
  @Override
  public void simpleClickI() { }
}
