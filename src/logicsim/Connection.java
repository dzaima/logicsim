package logicsim;

public class Connection {
  protected Gate[] out;
  
  protected Connection(Gate out) {
    this.out = new Gate[]{out};
  }
}
