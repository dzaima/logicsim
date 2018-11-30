package logicsim.gui;

import logicsim.Main;
import processing.core.*;

public abstract class Button extends Drawable {
  private final String text;
  private final int fontSize;
  
  Button(int x, int y, int w, int h, String text, int fontSize) {
    super(x, y, w, h);
    this.text = text;
    this.fontSize = fontSize;
  }
  
  @Override
  protected void draw(PGraphics g) {
    g.fill(Main.BUTTON_COLOR);
    g.stroke(Main.BUTTON_STROKE);
    g.rectMode(PConstants.CORNER);
    g.rect(x, y, w, h);
    g.fill(Main.BUTTON_TEXT_COLOR);
    g.textSize(fontSize);
    g.textAlign(PConstants.CENTER, PConstants.CENTER);
    g.text(text, x + w/2f, y + h/2f);
  }
  
  @Override
  protected void simpleClickI() {
    clicked();
  }
  
  public abstract void clicked();
}
