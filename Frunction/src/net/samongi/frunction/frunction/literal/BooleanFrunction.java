package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.error.syntax.ExpressionError;
import net.samongi.frunction.error.syntax.SyntaxError;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.literal.dictionary.LiteralDictionary;
import net.samongi.frunction.frunction.literal.method.NativeExpression;

public class BooleanFrunction extends NativeFrunction
{
  public static final String TRUE_LITERAL = "true";
  public static final String FALSE_LITERAL = "false";

  public static final String TYPE = "bool";

  public static Frunction parseLiteral(String symbol, Container environment) throws SyntaxError, RunTimeError
  {
    if(symbol.toLowerCase().equals(TRUE_LITERAL)) return new BooleanFrunction(environment, true);
    if(symbol.toLowerCase().equals(FALSE_LITERAL)) return new BooleanFrunction(environment, false);
    return null;
  }

  public static Frunction getCached(boolean bool)
  {
    String sym = null;
    if(bool) sym = TRUE_LITERAL;
    else sym = FALSE_LITERAL;
    try
    {
      return LiteralDictionary.getInstance().getSymbol(sym).get();
    }
    catch(SyntaxError | RunTimeError e)
    {
      e.printStackTrace();
    }
    return null;
  }

  /** Returns an expression that is a tautology which means it will always return true. This expression returns a boolean
   * frunction that is true more specifically.
   * 
   * @return */
  public static Expression getTautology()
  {
    return new NativeExpression()
    {
      @Override public Frunction evaluate() throws SyntaxError, RunTimeError
      {
        return LiteralDictionary.getInstance().getSymbol(TRUE_LITERAL).get();
      }
    };
  }

  /** Returns an expression that is a contradiction This means it will always return false. This expression returns a
   * boolean frunction that is false more specifically.
   * 
   * @return */
  public static Expression getContradiction()
  {
    return new NativeExpression()
    {
      @Override public Frunction evaluate() throws SyntaxError, RunTimeError
      {
        return LiteralDictionary.getInstance().getSymbol(FALSE_LITERAL).get();
      }
    };
  }

  // This is a frunction that the boolean frunction wraps.
  private final boolean state;

  public BooleanFrunction(Container environment, boolean state) throws SyntaxError, RunTimeError
  {
    super(environment);

    // Setting the state of the boolean.
    this.state = state;

    // Adding the methods
    try
    {
      this.addMethods();
    }
    catch(SyntaxError | RunTimeError e)
    {
      e.printStackTrace();
    }
  }

  private void addMethods() throws SyntaxError, RunTimeError
  {
    this.addSymbol(this.methodEquals());
    this.addSymbol(this.methodString());
    this.addSymbol(this.methodAnd());
    this.addSymbol(this.methodOr());
    this.addSymbol(this.methodNot());
  }

  /** Will generate a method binding for determining if another method is equal.
   * 
   * @return 
   * @throws RunTimeError 
   * @throws SyntaxError */
  private SymbolBinding methodEquals() throws SyntaxError, RunTimeError
  {
    // Generating the first method
    String[] input = new String[] { "other" };
    String[] types = new String[] { BooleanFrunction.TYPE };
    Expression condition = BooleanFrunction.getTautology();

    NativeExpression expression = new NativeExpression()
    {
      @Override public Frunction evaluate() throws ExpressionError
      {
        // Getting the left argument which should be the "@" self binding.
        Frunction left = this.getSelf();

        // Getting the right argument which should be the "other" argument as
        // defined
        Frunction right = this.getInput("other");

        if(!left.getType().equals(BooleanFrunction.TYPE) || !(left instanceof BooleanFrunction)) throw new IllegalStateException();
        if(!right.getType().equals(BooleanFrunction.TYPE) || !(right instanceof BooleanFrunction)) throw new IllegalStateException();
        BooleanFrunction b_left = (BooleanFrunction) left;
        BooleanFrunction b_right = (BooleanFrunction) right;
        // Performing the native operation.
        return BooleanFrunction.getCached(b_left.getNative() == b_right.getNative());
      }

    };
    return expression.getAsBinding("eq", this, input, types, condition);
  }

  private SymbolBinding methodString() throws SyntaxError, RunTimeError
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

        if(!left.getType().equals(BooleanFrunction.TYPE) || !(left instanceof BooleanFrunction)) throw new IllegalStateException();
        BooleanFrunction b_left = (BooleanFrunction) left;
        // Performing the native operation.
        return StringFrunction.getCached("" + b_left.getNative());
      }

    };
    return expression.getAsBinding("str", this, input, types, condition);
  }
  
  private SymbolBinding methodOr() throws SyntaxError, RunTimeError
  {
    // Generating the first method
    String[] input = new String[] { "other" };
    String[] types = new String[] { BooleanFrunction.TYPE };
    Expression condition = BooleanFrunction.getTautology();

    NativeExpression expression = new NativeExpression()
    {
      @Override public Frunction evaluate() throws ExpressionError
      {
        // Getting the left argument which should be the "@" self binding.
        Frunction left = this.getSelf();

        // Getting the right argument which should be the "other" argument as
        // defined
        Frunction right = this.getInput("other");

        if(!left.getType().equals(BooleanFrunction.TYPE) || !(left instanceof BooleanFrunction)) throw new IllegalStateException();
        if(!right.getType().equals(BooleanFrunction.TYPE) || !(right instanceof BooleanFrunction)) throw new IllegalStateException();
        BooleanFrunction b_left = (BooleanFrunction) left;
        BooleanFrunction b_right = (BooleanFrunction) right;
        // Performing the native operation.
        return BooleanFrunction.getCached(b_left.getNative() || b_right.getNative());
      }

    };
    return expression.getAsBinding("or", this, input, types, condition);
  }
  
  private SymbolBinding methodAnd() throws SyntaxError, RunTimeError
  {
    // Generating the first method
    String[] input = new String[] { "other" };
    String[] types = new String[] { BooleanFrunction.TYPE };
    Expression condition = BooleanFrunction.getTautology();

    NativeExpression expression = new NativeExpression()
    {
      @Override public Frunction evaluate() throws ExpressionError
      {
        // Getting the left argument which should be the "@" self binding.
        Frunction left = this.getSelf();

        // Getting the right argument which should be the "other" argument as
        // defined
        Frunction right = this.getInput("other");

        if(!left.getType().equals(BooleanFrunction.TYPE) || !(left instanceof BooleanFrunction)) throw new IllegalStateException();
        if(!right.getType().equals(BooleanFrunction.TYPE) || !(right instanceof BooleanFrunction)) throw new IllegalStateException();
        BooleanFrunction b_left = (BooleanFrunction) left;
        BooleanFrunction b_right = (BooleanFrunction) right;
        // Performing the native operation.
        return BooleanFrunction.getCached(b_left.getNative() && b_right.getNative());
      }

    };
    return expression.getAsBinding("and", this, input, types, condition);
  }
  
  private SymbolBinding methodNot() throws SyntaxError, RunTimeError
  {
    // Generating the first method
    String[] input = new String[] {};
    String[] types = new String[] {};
    Expression condition = BooleanFrunction.getTautology();

    NativeExpression expression = new NativeExpression()
    {
      @Override public Frunction evaluate() throws ExpressionError
      {
        // Getting the left argument which should be the "@" self binding.
        Frunction left = this.getSelf();


        if(!left.getType().equals(BooleanFrunction.TYPE) || !(left instanceof BooleanFrunction)) throw new IllegalStateException();
        BooleanFrunction b_left = (BooleanFrunction) left;
        // Performing the native operation.
        return BooleanFrunction.getCached(!b_left.getNative());
      }

    };
    return expression.getAsBinding("not", this, input, types, condition);
  }

  

  @Override public String getType()
  {
    return BooleanFrunction.TYPE;
  }

  public boolean getNative()
  {
    return this.state;
  }
}
