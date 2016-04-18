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
import net.samongi.frunction.frunction.type.TypeDefiner;

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
  
  public static TypeDefiner getTypeDefiner()
  {
    return new TypeDefiner(TYPE)
    {
      @Override protected void defineType(Frunction type_frunction) throws RunTimeError, SyntaxError
      {
        type_frunction.setType(NativeFrunction.TYPE);
        type_frunction.addSymbol(BooleanFrunction.methodAnd(type_frunction));
        type_frunction.addSymbol(BooleanFrunction.methodOr(type_frunction));
        
        type_frunction.addSymbol(BooleanFrunction.methodEquals(type_frunction));
        type_frunction.addSymbol(BooleanFrunction.methodNot(type_frunction));
      }
    };
  }

  public static Frunction getCached(boolean bool)
  {
    String sym = null;
    if(bool) sym = TRUE_LITERAL;
    else sym = FALSE_LITERAL;
    try
    {
      return LiteralDictionary.getInstance().getSymbol(sym).get(LiteralDictionary.getInstance());
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
        return LiteralDictionary.getInstance().getSymbol(TRUE_LITERAL).get(LiteralDictionary.getInstance());
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
        return LiteralDictionary.getInstance().getSymbol(FALSE_LITERAL).get(LiteralDictionary.getInstance());
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
  }

  /** Will generate a method binding for determining if another method is equal.
   * 
   * @return
   * @throws RunTimeException 
   * @throws ParsingException */
  private static SymbolBinding methodEquals(Frunction type_frunction) throws SyntaxError, RunTimeError
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

        if(!left.isType(BooleanFrunction.TYPE) || !(left instanceof BooleanFrunction)) throw new IllegalStateException();
        if(!right.isType(BooleanFrunction.TYPE) || !(right instanceof BooleanFrunction)) throw new IllegalStateException();
        BooleanFrunction b_left = (BooleanFrunction) left;
        BooleanFrunction b_right = (BooleanFrunction) right;
        // Performing the native operation.
        return BooleanFrunction.getCached(b_left.getNative() == b_right.getNative());
      }

    };
    return expression.getAsSymbolBinding("eq", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodOr(Frunction type_frunction) throws SyntaxError, RunTimeError
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

        if(!left.isType(BooleanFrunction.TYPE) || !(left instanceof BooleanFrunction)) throw new IllegalStateException();
        if(!right.isType(BooleanFrunction.TYPE) || !(right instanceof BooleanFrunction)) throw new IllegalStateException();
        BooleanFrunction b_left = (BooleanFrunction) left;
        BooleanFrunction b_right = (BooleanFrunction) right;
        // Performing the native operation.
        return BooleanFrunction.getCached(b_left.getNative() || b_right.getNative());
      }

    };
    return expression.getAsSymbolBinding("or", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodAnd(Frunction type_frunction) throws SyntaxError, RunTimeError
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

        if(!left.isType(BooleanFrunction.TYPE) || !(left instanceof BooleanFrunction)) throw new IllegalStateException();
        if(!right.isType(BooleanFrunction.TYPE) || !(right instanceof BooleanFrunction)) throw new IllegalStateException();
        BooleanFrunction b_left = (BooleanFrunction) left;
        BooleanFrunction b_right = (BooleanFrunction) right;
        // Performing the native operation.
        return BooleanFrunction.getCached(b_left.getNative() && b_right.getNative());
      }

    };
    return expression.getAsSymbolBinding("and", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodNot(Frunction type_frunction) throws SyntaxError, RunTimeError
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


        if(!left.isType(BooleanFrunction.TYPE) || !(left instanceof BooleanFrunction)) throw new IllegalStateException();
        BooleanFrunction b_left = (BooleanFrunction) left;
        // Performing the native operation.
        return BooleanFrunction.getCached(!b_left.getNative());
      }

    };
    return expression.getAsSymbolBinding("not", type_frunction, input, types, condition);
  }

  

  @Override public String getType()
  {
    return BooleanFrunction.TYPE;
  }

  public boolean getNative()
  {
    return this.state;
  }
  
  @Override public String asString()
  {
    return "" + this.state;
  }
}
