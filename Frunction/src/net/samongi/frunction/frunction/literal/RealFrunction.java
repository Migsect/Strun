package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.literal.dictionary.LiteralDictionary;

public class RealFrunction extends NativeFrunction
{
  private static final String TYPE = "real";
  
  public static final Frunction parseLiteral(String symbol, Container environment)
  {
  	Double converted = null;
  	try{converted = Double.parseDouble(symbol.trim());}catch(NumberFormatException e){return null;}
  	return new RealFrunction(environment, converted);
  }
  public static Frunction getCached(double real)
  {
    String sym = "" + real;
    try{return LiteralDictionary.getInstance().getSymbol(sym).get();}
    catch (TokenException e){e.printStackTrace();}
    return null;
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
