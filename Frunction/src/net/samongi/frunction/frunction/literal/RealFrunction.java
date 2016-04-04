package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;

public class RealFrunction extends NativeFrunction
{
  private static final String TYPE = "real";
  
  public static final Frunction parseLiteral(String symbol, Container environment)
  {
  	Double converted = null;
  	try{converted = Double.parseDouble(symbol.trim());}catch(NumberFormatException e){return null;}
  	return new RealFrunction(environment, converted);
  }
  
  private final double state;
  
  public RealFrunction(Container environment, double state)
  {
    super(environment);
    this.state = state;
  }

  @Override public String getType(){return TYPE;}
  
  public double getNative(){return this.state;}
}
