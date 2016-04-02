package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.frunction.Container;

public class StringFrunction extends NativeFrunction
{
  public static final String STRING_CAPSULE = "\"";
  
  private static final String TYPE = "String";
  
  private final String state;
  
  public StringFrunction(Container environment, String state)
  {
    super(environment);
    this.state = state;
  }

  @Override public String getType(){return TYPE;}
  
  public String getNative(){return this.state;}

}
