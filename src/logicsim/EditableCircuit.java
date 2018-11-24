package logicsim;

import logicsim.gui.TextField;
import processing.core.PGraphics;

public class EditableCircuit extends Circuit {
  private final TextField labelName;
  
  EditableCircuit() {
    labelName = new TextField(160, 20, 15);
  }
  
  @Override
  void leftPressed(int imX, int imY) {
    if (inMenu(imX, imY)) {
      if (labelName.in(imX, imY)) labelName.click();
    } else super.leftPressed(imX, imY);
  }
  
  @Override
  void leftReleased(int imX, int imY) {
    if (!inMenu(imX, imY)) {
      super.leftReleased(imX, imY);
    }
  }
  
  @Override
  void simpleClick(float mX, float mY) {
    if (!inMenu(mX, mY)) {
      super.simpleClick(mX, mY);
    }
  }
  
  @Override
  public void draw(PGraphics g, int mx, int my) {
    super.draw(g, mx, my);
    if (selected.size() == 1) {
      Gate cg = selected.get(0);
      if (cg.name == null) return;
      g.fill(Main.MENUBG);
      g.noStroke();
      g.rectMode(g.CORNER);
      int sx = g.width - 200;
      int sy = g.height - 100;
      int w = 180;
      int h = 80;
      g.rect(sx, sy, w, h);
      cg.name = labelName.update(g, cg.name, sx + 10, sy + h/2);
      g.text("I/O name", sx+10, sy + h/2f - 25);
    }
  }
  
  private boolean inMenu(float mx, float my) {
    int w = Main.instance.width;
    int h = Main.instance.height;
    return selected.size() == 1 && mx > w-200 && my > h-100 && mx < w-20 && my < h-20;
  }
}
