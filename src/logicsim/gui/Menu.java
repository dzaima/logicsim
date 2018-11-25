package logicsim.gui;

import logicsim.*;
import processing.core.*;

public class Menu extends Drawable {
  
  public final GateTF tf;
  
  public Menu(int x, int y, int w, int h) {
    super(x, y, w, h);
    tf = new GateTF(x+20, y+60, w-40, 30);
    add(tf);
  }
  
  @Override
  protected void draw(PGraphics g) {
    g.rectMode(PConstants.CORNER);
    g.strokeWeight(3);
    g.stroke(Main.MENU_STROKE);
    g.fill(Main.MENUBG);
    g.rect(x, y, w, h);
    
    g.textSize(20);
    g.textAlign(PConstants.LEFT, PConstants.CENTER);
    g.fill(Main.TEXT_COLOR);
    g.text("I/O name", x+20, y+30);
  }
  
  public void bind(Gate g) {
    tf.bind(g);
  }
  
  
  public static class GateTF extends TextField {
    public Gate g;
    GateTF(int x, int y, int w, int h) {
      super(x, y, w, h);
    }
    void bind(Gate g) {
      this.g = g;
      text = g.name;
    }
    @Override
    protected void updated() {
      g.name = text;
    }
  }
}
