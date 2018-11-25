package logicsim;

import logicsim.gui.Menu;
import processing.core.PGraphics;

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
  public void key(char key, int keyCode) {
    if (key == 3 && keyCode == 67) { // copy selection
      StringSelection selection = new StringSelection(Circuit.exportStr(selected));
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      clipboard.setContents(selection, selection);
    }
    if (key == 22 && keyCode == 86) { // load clip
      try {
        String s = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        importStr(new Scanner(s), fmX(Main.mX), fmY(Main.mY));
      } catch (Throwable err) {
        err.printStackTrace();
      }
    }
    if (key == 9 && keyCode == 73) { // load IC
      try {
        String s = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        CustomGateFactory.load(new Scanner(s));
      } catch (Throwable err) {
        err.printStackTrace();
      }
    }
    switch (key) {
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
        Circuit ic = new ROCircuit();
        try {
          ic.importStr(new Scanner(s), 0, 0);
        } catch (Throwable err) {
          err.printStackTrace();
        }
        break;
      case 'c':
        Main.next = new HashSet<>();
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
}
