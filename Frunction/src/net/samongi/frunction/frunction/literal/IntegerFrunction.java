package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.literal.dictionary.LiteralDictionary;

public class IntegerFrunction extends NativeFrunction
{
  private static final String TYPE = "int";
  
  public static final Frunction parseLiteral(String symbol, Container environment)
  {
  	Long converted = null;
  	try{converted = Long.parseLong(symbol.trim());}catch(NumberFormatException e){return null;}
  	return new IntegerFrunction(environment, converted);
  }
  public static Frunction getCached(long integer)
  {
    String sym = "" + integer;
    try{return LiteralDictionary.getInstance().getSymbol(sym).get();}
    catch (TokenException e){e.printStackTrace();}
    return null;
  }
  
  private final long state;
  
  public IntegerFrunction(Container environment, long state)
  {
    super(environment);
    this.state = state;
  }

  @Override public String getType(){return TYPE;}
  
  public long getNative(){return this.state;}

}
