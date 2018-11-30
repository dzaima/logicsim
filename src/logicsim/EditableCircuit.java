package logicsim;

import logicsim.gates.CustomGate;
import logicsim.gui.*;
import logicsim.gui.Menu;
import processing.core.*;
import processing.event.KeyEvent;

import java.awt.*;
import java.awt.datatransfer.*;
import java.util.*;

public class EditableCircuit extends Circuit {
  private final Menu menu;
  
  EditableCircuit(int x, int y, int w, int h) {
    super(x, y, w, h);
    menu = new Menu(x+w-300, y+h-120, 280, 100);
  }
  
  private Gate menuGate;
  
  @Override
  public void draw(PGraphics g) {
    if (selected.size() == 1) {
      if (menuGate == null) add(menu);
      menuGate = selected.get(0);
      if (menuGate != menu.tf.g) {
        menu.bind(menuGate);
      }
    } else {
      if (menuGate != null) {
        remove(menu);
        menuGate = null;
      }
    }
    super.draw(g);
  }
  
  
  @Override
  public void key(char key, int keyCode, KeyEvent e) {
    boolean ctrl = e.isControlDown();
    if ((key==3 || key == 'C') && ctrl) { // copy selection
      StringSelection selection = new StringSelection(Circuit.exportStr(selected));
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      clipboard.setContents(selection, selection);
    }
    if ((key==22 || key == 'V') && ctrl) { // load clip
      try {
        String s = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        importStr(new Scanner(s), fX(Main.mX), fY(Main.mY));
      } catch (Throwable err) {
        err.printStackTrace();
      }
    }
    if ((key==9 || key == 'I') && ctrl) { // load IC
      try {
        String s = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        CustomGateFactory.load(new Scanner(s));
      } catch (Throwable err) {
        err.printStackTrace();
      }
    }
    switch (key >= 'A' && key <= 'Z'? Character.toLowerCase(key) : key) {
      case 'e':
        for (Gate g : selected)
          g.rotate((g.rot+1) % 4);
        break;
      case 'q':
        for (Gate g : selected)
          g.rotate((g.rot+3) % 4);
        break;
      case 'i': // create IC
        String s = Circuit.exportStr(selected);
        ROCircuit ic = new ROCircuit();
        try {
          ic.importStr(new Scanner(s), 0, 0);
          int w = Main.instance.width/10;
          int h = Main.instance.height/10;
          Main.window.add(new ICCreator(w*3, h*2, w*4, h*6, ic));
        } catch (Throwable err) {
          err.printStackTrace();
        }
        break;
      case 'c':
        Main.next = new HashSet<>();
        break;
      case 'l':
        Lib l = new Lib();
        for (Gate g : Main.mainBoard.gates) {
          l.add(g);
        }
        StringSelection selection = new StringSelection(l.gen());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        break;
      case 127:
        removeSelected();
        break;
    }
  }
  
  @Override
  public void rightPressedI() {
    super.rightPressedI();
    unselectAll();
  }
  
  static class Lib {
    Set<CustomGateFactory> done = new HashSet<>();
    ArrayList<CustomGateFactory> reqs = new ArrayList<CustomGateFactory>();
    public void add(Gate g) {
      if (g instanceof CustomGate) {
        CustomGate cg = (CustomGate) g;
        CustomGateFactory f = cg.f;
        if (!done.contains(f)) {
          for(Gate sg : f.c.gates) add(sg);
          done.add(f);
          reqs.add(f);
        }
      }
    }
    
    String gen() {
      StringBuilder res = new StringBuilder(reqs.size() + "\n");
      for(CustomGateFactory s : reqs) {
        res.append(s.generateLib()).append("\n");
      }
      return res.toString();
    }
  }
}
