package logicsim.gui;

import logicsim.Main;
import processing.core.*;

import java.util.ArrayList;


public class TextField {
  private final int w, h;
  private float x, y;
  private final int sz;
  
  public ArrayList<Character> toAppend;
  
  private boolean wasDrawn = true;
  
  public TextField(int w, int h, int sz) {
    
    this.w = w;
    this.h = h;
    this.sz = sz;
    toAppend = new ArrayList<>();
  }
  
  public String update(PGraphics g, String text, int x, int y) {
    this.x = x;
    this.y = y-h/2f;
    g.rectMode(PConstants.CORNER);
    g.fill(Main.SELBG);
    g.rect(x, this.y, w, h);
    g.textAlign(PConstants.LEFT, PConstants.CENTER);
    g.fill(Main.TEXT_COLOR);
    g.textSize(sz);
    g.text(text+(Main.textField==this && System.nanoTime()%1000000000L > 500000000L? "|" : ""), x, y);
    wasDrawn = true;
    for (char c : toAppend) {
      if (c == 8) {
        if (text.length() > 0) text = text.substring(0, text.length()-1);
      } else {
        //noinspection StringConcatenationInLoop 's not that bad yknow
        text+= c;
      }
    }
    toAppend.clear();
    return text;
  }
  public boolean wasDrawn() {
    boolean b = wasDrawn;
    wasDrawn = false;
    return b;
  }
  
  public boolean in(float mX, float mY) {
    return mX > x && mY > y && mX < x+w && mX < x+w && mY < y+h;
  }
  
  public void click() {
    if (Main.textField == this) Main.textField = null;
    else Main.textField = this;
  }
}
