package logicsim;

public abstract class WireType {
  public abstract Connection newConnection(Gate g, int ip);
  
  public abstract Connection noConnection();
}
