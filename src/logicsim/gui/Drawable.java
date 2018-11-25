package logicsim.gui;

import logicsim.Main;
import processing.core.PGraphics;

import java.util.ArrayList;

public abstract class Drawable {
  protected static Drawable focused;
  
  protected final int x;
  protected final int y;
  public final int w;
  public final int h;
  
  protected Drawable(int x, int y, int w, int h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    sub = new ArrayList<>();
  }
  ArrayList<Drawable> sub;
  
  
  public final void drawAll(PGraphics g) {
    draw(g);
    for (int i = sub.size()-1; i >= 0; i--) {
      sub.get(i).drawAll(g);
    }
  }
  protected abstract void draw(PGraphics g);
  protected void leftPressedI()    { }
  protected void leftReleasedI()   { }
  protected void rightPressedI()   { }
  protected void rightReleasedI()  { }
  protected void middlePressedI()  { }
  protected void middleReleasedI() { }
  protected void simpleClickI()    { }
  
  public final void simpleClick   (){ for(Drawable d:sub) if(d.in(Main.mX,Main.mY)) { d.simpleClick   (); return; } focused = this; simpleClickI  (); }
  public final void leftPressed   (){ for(Drawable d:sub) if(d.in(Main.mX,Main.mY)) { d.leftPressed   (); return; } focused = this; leftPressedI  (); }
  public final void rightPressed  (){ for(Drawable d:sub) if(d.in(Main.mX,Main.mY)) { d.rightPressed  (); return; } focused = this; rightPressedI (); }
  public final void middlePressed (){ for(Drawable d:sub) if(d.in(Main.mX,Main.mY)) { d.middlePressed (); return; } focused = this; middlePressedI(); }
  
  public final void leftReleased   (){ focused.leftReleasedI  (); }
  public final void rightReleased  (){ focused.rightReleasedI (); }
  public final void middleReleased (){ focused.middleReleasedI(); }
  public boolean in(int ix, int iy) {
    return ix > x && iy > y && ix < x+w && iy < y+h;
  }
  
  public void add(Drawable d) {
    sub.add(d);
  }
  protected void remove(Drawable d) {
    sub.remove(d);
    d.stopSelection();
  }
  
  private boolean stopSelection() {
    if (focused == this) {
      focused = null;
      return true;
    }
    for (Drawable d : sub) {
      if (d.stopSelection()) return true;
    }
    return false;
  }
  
  
  protected void key(char key, int keyCode) { }
  public static void keyD(char key, int keyCode) {
    if (focused != null) focused.key(key, keyCode);
  }
  
  protected void mouseWheel(int c) { }
  public static void mouseWheelD(int c) {
    if (focused != null) focused.mouseWheel(c);
  }
}
