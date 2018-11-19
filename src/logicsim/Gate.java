package logicsim;

public abstract class Gate {
  protected Connection[] is;
  private final WireType[] its;
  protected Connection[] os;
  private final WireType[] ots;
  private float x;
  private float y;
  
  protected Gate(WireType[] its, WireType[] ots, float x, float y) {
    this.x = x;
    this.y = y;
    
    this.its = its;
    this.ots = ots;
  
    is = new Connection[its.length];
    os = new Connection[ots.length];
    
    Main.next.add(this);
  }
  
  void addInput(int to, Gate og, int opos) {
    Connection c = new Connection(this);
  }
  
  public abstract void process();
  
  public void warn() {
    Main.next.add(this);
  }
}