package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.error.syntax.ExpressionError;
import net.samongi.frunction.error.syntax.SyntaxError;
import net.samongi.frunction.error.syntax.TokenError;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.DynamicFrunction;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.literal.method.NativeExpression;
import net.samongi.frunction.frunction.type.TypeDefiner;

public abstract class NativeFrunction extends DynamicFrunction
{
  public static final String TYPE = "native";
  
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
  
  public static TypeDefiner getTypeDefiner()
  {
    return new TypeDefiner(TYPE)
    {
      @Override protected void defineType(Frunction type_frunction) throws RunTimeError, SyntaxError
      {
        type_frunction.setType(EmptyFrunction.TYPE);
        type_frunction.addSymbol(NativeFrunction.methodString(type_frunction));
        
      }
    };
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
  
  private static SymbolBinding methodString(Frunction type_frunction) throws SyntaxError, RunTimeError
  {
    // Generating the first method
    String[] input = new String[] {};
    String[] types = new String[] {};
    Expression condition = BooleanFrunction.getTautology();

    NativeExpression expression = new NativeExpression()
    {
      @Override public Frunction evaluate()
      {
        // Getting the left argument which should be the "@" self binding.
        Frunction left = this.getSelf();

        if(!left.isType(NativeFrunction.TYPE) || !(left instanceof NativeFrunction)) return null; 
        NativeFrunction n_left = (NativeFrunction) left;
        return StringFrunction.getCached(n_left.asString());
      }

    };
    return expression.getAsSymbolBinding("str", type_frunction, input, types, condition);
  }

  // Native Frunctions have no source
  @Override public String getSource()
  {
    return null;
  }

  @Override public void setType(String type)
  {} // Cannot change type

  @Override public abstract String getType();
  
  public abstract String asString();

}
