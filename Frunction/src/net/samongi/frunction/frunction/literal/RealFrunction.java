package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.exceptions.FrunctionNotEvaluatedException;
import net.samongi.frunction.frunction.literal.dictionary.LiteralDictionary;
import net.samongi.frunction.frunction.literal.method.NativeExpression;

public class RealFrunction extends NativeFrunction
{
  private static final String TYPE = "real";

  public static final Frunction parseLiteral(String symbol,
      Container environment)
  {
    Double converted = null;
    try
    {
      converted = Double.parseDouble(symbol.trim());
    }
    catch(NumberFormatException e)
    {
      return null;
    }
    return new RealFrunction(environment, converted);
  }

  public static Frunction getCached(double real)
  {
    String sym = "" + real;
    try
    {
      return LiteralDictionary.getInstance().getSymbol(sym).get();
    }
    catch(TokenException e)
    {
      e.printStackTrace();
    }
    return null;
  }

  private final double state;

  public RealFrunction(Container environment, double state)
  {
    super(environment);

    this.state = state;

    // Adding the methods
    try
    {
      this.addMethods();
    }
    catch(FrunctionNotEvaluatedException e)
    {
      e.printStackTrace();
    }
  }

  private void addMethods() throws FrunctionNotEvaluatedException
  {
    this.addSymbol(this.methodEquals());
    this.addSymbol(this.methodString());
  }

  /**
   * Will generate a method binding for determining if another method is equal.
   * 
   * @return
   */
  private SymbolBinding methodEquals()
  {
    // Generating the first method
    String[] input = new String[] { "other" };
    String[] types = new String[] { RealFrunction.TYPE };
    Expression condition = BooleanFrunction.getTautology();

    NativeExpression expression = new NativeExpression()
    {
      @Override public Frunction evaluate()
      {
        // Getting the left argument which should be the "@" self binding.
        Frunction left = this.getSelf();

        // Getting the right argument which should be the "other" argument as
        // defined
        Frunction right = this.getInput("other");

        if(!left.getType().equals(RealFrunction.TYPE)
            || !(left instanceof RealFrunction)) { return null; // We should
                                                                // technically
                                                                // never get to
                                                                // this stage.
        }
        if(!right.getType().equals(RealFrunction.TYPE)
            || !(right instanceof RealFrunction)) { return null; // We should
                                                                 // technically
                                                                 // never get to
                                                                 // this stage.
        }
        RealFrunction r_left = (RealFrunction) left;
        RealFrunction r_right = (RealFrunction) right;
        // Performing the native operation.
        return BooleanFrunction.getCached(r_left.getNative() == r_right
            .getNative());
      }

    };
    return expression.getAsBinding("eq", this, input, types, condition);
  }

  private SymbolBinding methodString()
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

        if(!left.getType().equals(RealFrunction.TYPE)
            || !(left instanceof RealFrunction)) { return null; // We should
                                                                // technically
                                                                // never get to
                                                                // this stage.
        }
        RealFrunction r_left = (RealFrunction) left;
        // Performing the native operation.
        return StringFrunction.getCached("" + r_left.getNative());
      }

    };
    return expression.getAsBinding("str", this, input, types, condition);
  }

  @Override public String getType()
  {
    return TYPE;
  }

  public double getNative()
  {
    return this.state;
  }
}
