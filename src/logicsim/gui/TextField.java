package logicsim.gui;

import logicsim.Main;
import processing.core.*;


public class TextField extends Drawable {
  protected String text;
  
  TextField(int x, int y, int w, int h) {
    super(x, y, w, h);
  }
  
  @Override
  protected void draw(PGraphics g) {
    
    g.rectMode(PConstants.CORNER);
    g.fill(Main.TEXTFIELD_COLOR);
    g.stroke(Main.MENU_STROKE);
    g.strokeWeight(2);
    g.rect(x, y, w, h);
    g.textSize(h*.8f);
    g.fill(Main.TEXT_COLOR);
    g.textAlign(PConstants.LEFT, PConstants.CENTER);
    g.text(text+(Drawable.focused ==this && System.currentTimeMillis()%1000>500? "|" : ""), x+10, y+h/2f);
  }
  
  protected void updated() { } // for overriding
  
  public boolean in(float mX, float mY) {
    return mX > x && mY > y && mX < x+w && mX < x+w && mY < y+h;
  }
  
  @Override
  protected void key(char key, int keyCode) {
    if (key == 8) {
      if (text.length() > 0) text = text.substring(0, text.length()-1);
    } else text+= key;
    updated();
  }
}
