package logicsim.gates;

import logicsim.*;
import processing.awt.PGraphicsJava2D;
import processing.core.*;

import java.awt.*;

import static logicsim.wiretypes.BasicWire.BasicConnection;

public class CustomGate extends Gate {
  private final CustomGateFactory f;
  private Gate[] igs;
  private Gate[] ogs;
  private ROCircuit c;
  private int width;
  private int height;
  
  public CustomGate(WireType[] its, WireType[] ots, float x, float y, CustomGateFactory f) {
    super(its, ots, x, y);
    this.f = f;
    refresh();
  }
  
  private void refresh() {
    width = 30;
    height = Math.max(Math.max(is.length, os.length)*10, 20);
    ips = new PVector[its.length];
    for (int i = 0; i < its.length; i++) {
      ips[i] = new PVector(-width-20, (i-its.length/2f+.5f)*20);
    }
    ops = new PVector[ots.length];
    for (int i = 0; i < ots.length; i++) {
      ops[i] = new PVector(width+20, (i-ots.length/2f+.5f)*20);
    }
    c = f.c.copy();
    c.calculateEdges();
    igs = new Gate[its.length];
    for (int i = 0; i < f.ins.length; i++) {
      String name = f.ins[i];
      for (Gate g : c.gates)
        if (name.equals(g.name)) {
          igs[i] = g;
          break;
        }
      if (igs[i] == null) throw new Error("No input labeled "+name+" found");
    }
    ogs = new Gate[ots.length];
    for (int i = 0; i < f.ons.length; i++) {
      String name = f.ons[i];
      for (Gate g : c.gates)
        if (name.equals(g.name)) {
          ogs[i] = g;
          assert g instanceof LampGate;
          g.is[0].addWarnable(this);
          break;
        }
      if (ogs[i] == null) throw new Error("No output labeled "+name+" found");
    }
  }
  
  public static GateHandler handler() {
    return s -> {
      String name = s.nextLine();
      if (!Main.gateLibrary.containsKey(name)) throw new LoadException("IC "+name+" unknown");
      String[] pos = s.nextLine().split(" ");
      CustomGateFactory f = Main.gateLibrary.get(name);
      return f.create(Float.parseFloat(pos[0]), Float.parseFloat(pos[1]));
    };
  }
  
  @Override
  public void process() {
    for (int i = 0; i < igs.length; i++) {
      Gate g = igs[i];
//      System.out.println(i+"--"+g+"--"+ Arrays.toString(is));
      g.forwardConnection(is[i]);
    }
    for (int i = 0; i < ogs.length; i++) {
      Gate g = ogs[i];
      BasicConnection c = (BasicConnection) g.giveConnection();
      BasicConnection oc = (BasicConnection) os[i];
      oc.set(c.b);
    }
  }
  
  @Override
  public Gate cloneCircuit(float x, float y) {
    return new CustomGate(its, ots, x, y, f);
  }
  
  @Override
  public void draw(PGraphics g) {
    float change;
    if (g instanceof PGraphicsJava2D) {
      g.pushMatrix();
        g.translate(x, y);
        g.scale(.05f);
        Graphics2D i = ((PGraphicsJava2D) g).g2;
        float[] pts = new float[]{c.lx, c.ly, c.bx, c.by, 0, 0, 10, 0};
        float[] opts = new float[8];
        i.getTransform().transform(pts, 0, opts, 0, 4);
        float lX = Math.max(opts[0], 0);
        float rX = Math.min(opts[2], g.width);
        float bY = Math.max(opts[1], 0);
        float tY = Math.min(opts[3], g.height);
        float t = opts[6] - opts[4];
        if (rX > lX && tY > bY) {
          change = PApplet.constrain(PApplet.map(t, 1, 5, 0, 1), 0, 1);
        } else {
          change = 0;
        }
//      System.out.println(t+" "+change);
      g.popMatrix();
    } else change = 0; // :(
    g.rectMode(g.RADIUS);
    if (selected) {
      g.noStroke();
      g.fill(Main.SELECTED);
      g.rect(x, y, width+5, height+5);
    }
    g.stroke(Main.CIRCUIT_BORDERS);
    int rgb = Main.instance.lerpColor(Main.CIRCUIT_COLOR, Main.BG, change);
    g.fill(rgb);
    g.strokeWeight(3);
    g.rect(x, y, width, height); // main rect
    
    
    int mask = ((int) (255-change*255) << 24);
    
    if (change > 0) { // draw inner circuit
      Main.ctr++;
      g.pushMatrix();
        g.translate(x, y);
        g.scale(.05f);
        c.draw(g, 0, 0);
      g.popMatrix();
      g.rectMode(g.RADIUS);
      g.fill(rgb&0xffffff | mask);
      g.strokeWeight(3);
      g.rect(x, y, width, height); // main rect
    }
    
    g.textSize(10);
    g.textAlign(g.LEFT, g.CENTER);
    int textcol = Main.TEXT_COLOR & 0xffffff | mask;
    if (textcol < 0 || textcol > 255) {
      for (int i = 0; i < its.length; i++) {
        float cy = y + (i - its.length / 2f + .5f) * 20;
        g.fill(Main.CIRCUIT_COLOR);
        g.line(x - width, cy, x - width - 20, cy);
    
        g.fill(textcol);
        g.text(f.ins[i], x - width + 2, cy - 1);
      }
      g.textAlign(g.RIGHT, g.CENTER);
      for (int i = 0; i < ots.length; i++) { // labels
        float cy = y + (i - ots.length / 2f + .5f) * 20;
        g.fill(Main.CIRCUIT_COLOR);
        g.line(x + width, cy, x + width + 20, cy);
        g.fill(textcol);
        g.text(f.ons[i], x + width - 2, cy - 1);
      }
    }
    g.pushMatrix();
      g.translate(x, y);
      g.rotate(PConstants.HALF_PI);
      g.textAlign(PConstants.CENTER, PConstants.CENTER);
      g.text(f.name, 0, 0);
    g.popMatrix();
    drawIO(g);
  }
  
  @Override
  public boolean in(float mx, float my) {
    return Math.abs(mx-x) < 20 && Math.abs(my-y) < height;
  }
  
  @Override
  public String def(float xoff, float yoff) {
    String[] ss = getClass().getName().split("\\.");
    return ss[ss.length-1] + "\n" + f.name + "\n" + (x-xoff)+" "+(y-yoff);
  }
}
