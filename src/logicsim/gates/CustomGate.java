package logicsim.gates;

import logicsim.*;
import processing.awt.PGraphicsJava2D;
import processing.core.*;

import java.awt.*;

import static logicsim.wiretypes.BasicWire.BasicConnection;

public class CustomGate extends Gate {
  public final CustomGateFactory f;
  private Gate[] igs;
  private Gate[] ogs;
  private ROCircuit c;
  private int width;
  private int height;
  
  public CustomGate(WireType[] its, WireType[] ots, float x, float y, int rot, CustomGateFactory f, PVector[] ips, PVector[] ops, int width, int height) {
    super(its, ots, x, y, rot, ips, ops);
    this.width = width;
    this.height = height;
    this.f = f;
    refresh();
  }
  
  private void refresh() {
    c = f.c.readOnlyCopy(Main.instance.g);
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
      String[] a = s.nextLine().split(" ");
      CustomGateFactory f = Main.gateLibrary.get(name);
      return f.create(Float.parseFloat(a[0]), Float.parseFloat(a[1]), Integer.parseInt(a[2]));
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
    return new CustomGate(its, ots, x, y, rot, f, ips, ops, width, height);
  }
  
  @Override
  public void draw(PGraphics g) {
    float change;
    if (g instanceof PGraphicsJava2D) {
      g.pushMatrix();
//        g.translate(x, y);
        g.scale(.05f);
        Graphics2D i = ((PGraphicsJava2D) g).g2;
        float[] pts = new float[]{c.lx, c.ly, c.bx, c.by, 0, 0, 10, 0};
        float[] opts = new float[8];
        i.getTransform().transform(pts, 0, opts, 0, 4);
        if (opts[0] > opts[2]) {
          float t = opts[0];
          opts[0] = opts[2];
          opts[2] = t;
        }
        if (opts[1] > opts[3]) {
          float t = opts[1];
          opts[1] = opts[3];
          opts[3] = t;
        }
        float lX = Math.max(opts[0], 0);
        float rX = Math.min(opts[2], g.width);
        float bY = Math.max(opts[1], 0);
        float tY = Math.min(opts[3], g.height);
        float t = Math.abs(opts[6] - opts[4] + opts[7]-opts[5]);
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
      g.rect(0, 0, width+5, height+5);
    }
    g.stroke(Main.CIRCUIT_BORDERS);
    int rgb = Main.instance.lerpColor(Main.CIRCUIT_COLOR, Main.BG, change);
    g.fill(rgb);
    g.strokeWeight(3);
    g.rect(0, 0, width, height); // main rect
    
    
    int mask = ((int) (255-change*255) << 24);
    
    if (change > 0) { // update inner circuit
      Main.ctr++;
      g.pushMatrix();
//        g.translate(x, y);
        g.scale(.05f);
        c.draw(g);
      g.popMatrix();
      g.rectMode(g.RADIUS);
      g.fill(rgb&0xffffff | mask);
      g.strokeWeight(3);
      g.rect(0, 0, width, height); // main rect
    }
    
    g.textSize(10);
    
    
    g.textAlign(g.LEFT, g.CENTER);
    int textcol = Main.TEXT_COLOR & 0xffffff | mask;
    
    g.stroke(Main.CIRCUIT_BORDERS);
    for (int i = 0; i < its.length; i++) {
      float cy = (i - its.length / 2f + .5f) * 20;
      g.line(-width, cy, -width - 20, cy);
    }
    for (int i = 0; i < ots.length; i++) {
      float cy = (i - ots.length / 2f + .5f) * 20;
      g.line(width, cy, width + 20, cy);
    }
  
  
  
    g.fill(textcol);
    if (textcol < 0 || textcol > 255) {
      for (int i = 0; i < its.length; i++) {
        float cy = (i - its.length / 2f + .5f) * 20;
        g.text(f.ins[i], -width + 2, cy - 1);
      }
  
      g.textAlign(g.RIGHT, g.CENTER);
      for (int i = 0; i < ots.length; i++) {
        float cy = (i - ots.length / 2f + .5f) * 20;
        g.text(f.ons[i], width - 2, cy - 1);
      }
    }
    g.pushMatrix();
      g.rotate(PConstants.HALF_PI);
      g.textAlign(PConstants.CENTER, PConstants.CENTER);
      g.fill(textcol==0? 0x00ffffff : textcol);
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
    return ss[ss.length-1] + "\n" + f.name + "\n" + (x-xoff)+" "+(y-yoff)+" "+rot;
  }
}
