package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.literal.dictionary.LiteralDictionary;

public class StringFrunction extends NativeFrunction
{
  public static final String STRING_CAPSULE = "\"";
  
  private static final String TYPE = "string";
  
  public static Frunction parseLiteral(String symbol, Container environment)
  {
  	if(!symbol.startsWith(STRING_CAPSULE) || !symbol.endsWith(STRING_CAPSULE)) return null;
  	String str = symbol.substring(1, symbol.length() - 1); // Getting the inner string
  	return new StringFrunction(environment, str);
  }
  public static Frunction getCached(String str)
  {
    String sym = "\"" + str + "\""; 
    try{return LiteralDictionary.getInstance().getSymbol(sym).get();}
    catch (TokenException e){e.printStackTrace();}
    return null;
  }
  
  private final String state;
  
  public StringFrunction(Container environment, String state)
  {
    super(environment);
    this.state = state;
  }

  @Override public String getType(){return TYPE;}
  
  public String getNative(){return this.state;}

}
