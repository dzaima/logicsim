package logicsim.gui;

import logicsim.*;
import logicsim.gates.*;
import processing.core.*;

import java.util.Scanner;

public class ICCreator extends Drawable {
  private final ROCircuit ic;
  private final TextField namef, inpf, outf;
  private final int border;
  private final int buttonH;
  private final int buttonW;
  private final int textOff;
  private final int tHeight;
  
  public ICCreator(int x, int y, int w, int h, ROCircuit ic) {
    super(x, y, w, h);
    this.ic = ic;
    border = 20;
    buttonH = 60;
    buttonW = 200;
    tHeight = Main.TEXTFIELD_HEIGHT;
    Main.instance.textSize(tHeight*Main.TEXTFIELD_SIZE_FACTOR);
    textOff = border + (int) Main.instance.textWidth("Outputs hi");
    add(new Button(x + border, y + h - border - buttonH, buttonW, buttonH, "create", buttonH / 2) {
      @Override
      public void clicked() {
        try {
          CustomGateFactory.load(new Scanner(generateIC()));
//          Main.mainBoard.importStr(
//            new Scanner("1\nCustomGate\n"+getName()+"\n0 0 0\n0"),
//            Main.mainBoard.fX(Main.mX),
//            Main.mainBoard.fY(Main.mY)
//          );
          Main.window.remove(ICCreator.this);
        } catch (LoadException e) {
          e.printStackTrace();
        }
      }
    });
    add(new Button(x+w-border-buttonW, y+h-border-buttonH, buttonW, buttonH, "exit", buttonH/2) {
      @Override
      public void clicked() {
        Main.window.remove(ICCreator.this);
      }
    });
    int yoff = y + h/3;
    StringBuilder i = new StringBuilder();
    StringBuilder o = new StringBuilder();
    for (Gate g : ic.gates) {
      if (g instanceof SwitchGate) {
        i.append(g.name).append(" ");
      } else if (g instanceof LampGate) {
        o.append(g.name).append(" ");
      }
    }
    add(namef = new TextField(x + textOff, yoff+= tHeight*1.3, buttonW, tHeight));
    add(inpf  = new TextField(x + textOff, yoff+= tHeight*1.3, buttonW, tHeight));
    add(outf  = new TextField(x + textOff, yoff+= tHeight*1.3, buttonW, tHeight));
    inpf.setText(i.length()==0?"":i.substring(0, i.length()-1));
    outf.setText(o.length()==0?"":o.substring(0, o.length()-1));
  }
  
  private String getName() {
    return namef.getText();
  }
  
  private String generateIC() {
    String res = "I\n1\n" + getName() + "\n" + Circuit.exportStr(Main.mainBoard.gates);
    String[] is = inpf.getText().split(" ");
    String[] os = outf.getText().split(" ");
    res+= is.length + "\n" + Main.join(is, "\n") + "\n"
       +  os.length + "\n" + Main.join(os, "\n") + "\n--\n--";
//    System.out.println(res);
    return res;
  }
  
  @Override
  protected void draw(PGraphics g) {
    g.fill(Main.ICBG);
    g.noStroke();
//    System.out.println(x+" "+y+" "+w+" "+h);
    g.rectMode(PConstants.CORNER);
    g.rect(x, y, w, h);
    g.textSize(Main.TEXTFIELD_HEIGHT*Main.TEXTFIELD_SIZE_FACTOR);
    g.textAlign(PConstants.RIGHT, PConstants.TOP);
    g.fill(Main.TEXT_COLOR);
    g.text("Name  ", x + textOff, namef.y);
    g.text("Inputs  ", x + textOff, inpf.y);
    g.text("Outputs  ", x + textOff, outf.y);
  }
}
