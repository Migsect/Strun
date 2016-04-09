package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.exceptions.FrunctionNotEvaluatedException;
import net.samongi.frunction.frunction.literal.dictionary.LiteralDictionary;
import net.samongi.frunction.frunction.literal.method.NativeExpression;

public class IntegerFrunction extends NativeFrunction
{
  private static final String TYPE = "int";

  public static final Frunction parseLiteral(String symbol,
      Container environment)
  {
    Long converted = null;
    try
    {
      converted = Long.parseLong(symbol.trim());
    }
    catch(NumberFormatException e)
    {
      return null;
    }
    return new IntegerFrunction(environment, converted);
  }

  public static Frunction getCached(long integer)
  {
    String sym = "" + integer;
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

  private final long state;

  public IntegerFrunction(Container environment, long state)
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
    String[] types = new String[] { IntegerFrunction.TYPE };
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

        if(!left.getType().equals(IntegerFrunction.TYPE)
            || !(left instanceof IntegerFrunction)) { return null; // We should
                                                                   // technically
                                                                   // never get
                                                                   // to this
                                                                   // stage.
        }
        if(!right.getType().equals(IntegerFrunction.TYPE)
            || !(right instanceof IntegerFrunction)) { return null; // We should
                                                                    // technically
                                                                    // never get
                                                                    // to this
                                                                    // stage.
        }
        IntegerFrunction i_left = (IntegerFrunction) left;
        IntegerFrunction i_right = (IntegerFrunction) right;
        // Performing the native operation.
        return BooleanFrunction.getCached(i_left.getNative() == i_right
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

        if(!left.getType().equals(IntegerFrunction.TYPE)
            || !(left instanceof IntegerFrunction)) { return null; // We should
                                                                   // technically
                                                                   // never get
                                                                   // to this
                                                                   // stage.
        }
        IntegerFrunction i_left = (IntegerFrunction) left;
        // Performing the native operation.
        return StringFrunction.getCached("" + i_left.getNative());
      }

    };
    return expression.getAsBinding("str", this, input, types, condition);
  }

  @Override public String getType()
  {
    return TYPE;
  }

  public long getNative()
  {
    return this.state;
  }

}
