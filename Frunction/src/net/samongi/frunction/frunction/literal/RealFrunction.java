package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.frunction.Container;

public class RealFrunction extends NativeFrunction
{
  private static final String TYPE = "Real";
  
  private final double state;
  
  public RealFrunction(Container environment, double state)
  {
    super(environment);
    this.state = state;
  }

  @Override public String getType(){return TYPE;}
  
  public double getNative(){return this.state;}
}
