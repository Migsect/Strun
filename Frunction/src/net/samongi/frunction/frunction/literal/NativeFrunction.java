package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.error.syntax.ExpressionError;
import net.samongi.frunction.error.syntax.SyntaxError;
import net.samongi.frunction.error.syntax.TokenError;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.DynamicFrunction;
import net.samongi.frunction.frunction.Frunction;

public abstract class NativeFrunction extends DynamicFrunction
{
  /** Will attempt to parse a symbol and try to forulate a literal from it. This will return null if it could not
   * forumate a frunction fro the symbol.
   * 
   * @param symbol The symbol to parse
   * @param environment The environment that the frunction will be made in
   * @return A frunction of the literal, otherwise null 
   * @throws RunTimeError 
   * @throws SyntaxError */
  public static Frunction parseLiteral(String symbol, Container environment) throws SyntaxError, RunTimeError
  {
    symbol = symbol.trim();
    Frunction f = null;

    f = BooleanFrunction.parseLiteral(symbol, environment);
    if(f != null) return f;

    f = IntegerFrunction.parseLiteral(symbol, environment);
    if(f != null) return f;

    f = RealFrunction.parseLiteral(symbol, environment);
    if(f != null) return f;

    f = StringFrunction.parseLiteral(symbol, environment);
    if(f != null) return f;

    throw new SyntaxError();
  }

  public NativeFrunction(Container environment) throws SyntaxError, RunTimeError
  {
    super(environment);
    try
    {
      super.evaluate();
    }
    catch(TokenError | ExpressionError e)
    {
      e.printStackTrace();
    }
  }

  // Native Frunctions have no source
  @Override public String getSource()
  {
    return null;
  }

  @Override public void setType(String type)
  {} // Cannot change type

  @Override public abstract String getType();

}
