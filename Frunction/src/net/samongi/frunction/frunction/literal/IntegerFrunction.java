package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.frunction.Container;

public class IntegerFrunction extends NativeFrunction
{
  private static final String TYPE = "Boolean";
  
  private final long state;
  
  public IntegerFrunction(Container environment, long state)
  {
    super(environment);
    this.state = state;
  }

  @Override public String getType(){return TYPE;}
  
  public long getNative(){return this.state;}

}
