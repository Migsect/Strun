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

public class IntegerFrunction extends NativeFrunction
{
  public static final String TYPE = "int";

  public static final Frunction parseLiteral(String symbol, Container environment) throws SyntaxError, RunTimeError
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
      return LiteralDictionary.getInstance().getSymbol(sym).get(LiteralDictionary.getInstance());
    }
    catch(SyntaxError | RunTimeError e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  public static TypeDefiner getTypeDefiner()
  {
    return new TypeDefiner(TYPE)
    {
      @Override protected void defineType(Frunction type_frunction) throws RunTimeError, SyntaxError
      {
        type_frunction.setType(NativeFrunction.TYPE);
        type_frunction.addSymbol(IntegerFrunction.methodEquals(type_frunction));
        type_frunction.addSymbol(IntegerFrunction.methodNegative(type_frunction));
        type_frunction.addSymbol(IntegerFrunction.methodString(type_frunction));
        type_frunction.addSymbol(IntegerFrunction.methodAbsolute(type_frunction));
        type_frunction.addSymbol(IntegerFrunction.methodAddition(type_frunction));
        type_frunction.addSymbol(IntegerFrunction.methodDivision(type_frunction));
        type_frunction.addSymbol(IntegerFrunction.methodGreater(type_frunction));
        type_frunction.addSymbol(IntegerFrunction.methodLesser(type_frunction));
        type_frunction.addSymbol(IntegerFrunction.methodReal(type_frunction));
        type_frunction.addSymbol(IntegerFrunction.methodRemainder(type_frunction));
        type_frunction.addSymbol(IntegerFrunction.methodMultiplication(type_frunction));
      }
    };
  }

  private final long state;

  public IntegerFrunction(Container environment, long state) throws SyntaxError, RunTimeError
  {
    super(environment);

    this.state = state;
  }

  /** Will generate a method binding for determining if another method is equal.
   * 
   * @return 
   * @throws RunTimeError 
   * @throws SyntaxError */
  private static SymbolBinding methodEquals(Frunction type_frunction) throws SyntaxError, RunTimeError
  {
    // Generating the first method
    String[] input = new String[] { "other" };
    String[] types = new String[] { IntegerFrunction.TYPE };
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

        if(!left.getType().equals(IntegerFrunction.TYPE) || !(left instanceof IntegerFrunction)) throw new IllegalStateException();
        if(!right.getType().equals(IntegerFrunction.TYPE) || !(right instanceof IntegerFrunction)) throw new IllegalStateException();
        
        IntegerFrunction i_left = (IntegerFrunction) left;
        IntegerFrunction i_right = (IntegerFrunction) right;
        // Performing the native operation.
        return BooleanFrunction.getCached(i_left.getNative() == i_right.getNative());
      }
    };
    return expression.getAsBinding("eq", type_frunction, input, types, condition);
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

        if(!left.getType().equals(IntegerFrunction.TYPE) || !(left instanceof IntegerFrunction)) throw new IllegalStateException();
        
        IntegerFrunction i_left = (IntegerFrunction) left;
        // Performing the native operation.
        return StringFrunction.getCached("" + i_left.getNative());
      }

    };
    return expression.getAsBinding("str", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodReal(Frunction type_frunction) throws SyntaxError, RunTimeError
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

        if(!left.getType().equals(IntegerFrunction.TYPE) || !(left instanceof IntegerFrunction)) throw new IllegalStateException();
        
        IntegerFrunction i_left = (IntegerFrunction) left;
        // Performing the native operation.
        return RealFrunction.getCached(0.0 + i_left.getNative());
      }

    };
    return expression.getAsBinding("real", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodGreater(Frunction type_frunction) throws SyntaxError, RunTimeError
  {
    // Generating the first method
    String[] input = new String[] { "other" };
    String[] types = new String[] { IntegerFrunction.TYPE };
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

        if(!left.getType().equals(IntegerFrunction.TYPE) || !(left instanceof IntegerFrunction)) throw new IllegalStateException();
        if(!right.getType().equals(IntegerFrunction.TYPE) || !(right instanceof IntegerFrunction)) throw new IllegalStateException();
        
        IntegerFrunction i_left = (IntegerFrunction) left;
        IntegerFrunction i_right = (IntegerFrunction) right;
        // Performing the native operation.
        return BooleanFrunction.getCached(i_left.getNative() > i_right.getNative());
      }
    };
    return expression.getAsBinding("gt", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodLesser(Frunction type_frunction) throws SyntaxError, RunTimeError
  {
    // Generating the first method
    String[] input = new String[] { "other" };
    String[] types = new String[] { IntegerFrunction.TYPE };
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

        if(!left.getType().equals(IntegerFrunction.TYPE) || !(left instanceof IntegerFrunction)) throw new IllegalStateException();
        if(!right.getType().equals(IntegerFrunction.TYPE) || !(right instanceof IntegerFrunction)) throw new IllegalStateException();
        
        IntegerFrunction i_left = (IntegerFrunction) left;
        IntegerFrunction i_right = (IntegerFrunction) right;
        // Performing the native operation.
        return BooleanFrunction.getCached(i_left.getNative() < i_right.getNative());
      }
    };
    return expression.getAsBinding("lt", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodNegative(Frunction type_frunction) throws SyntaxError, RunTimeError
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

        if(!left.getType().equals(IntegerFrunction.TYPE) || !(left instanceof IntegerFrunction)) throw new IllegalStateException();
        
        IntegerFrunction i_left = (IntegerFrunction) left;
        // Performing the native operation.
        return IntegerFrunction.getCached(-i_left.getNative());
      }
    };
    return expression.getAsBinding("neg", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodAbsolute(Frunction type_frunction)throws SyntaxError, RunTimeError
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

        if(!left.getType().equals(IntegerFrunction.TYPE) || !(left instanceof IntegerFrunction)) throw new IllegalStateException();
        
        IntegerFrunction i_left = (IntegerFrunction) left;
        // Performing the native operation.
        return IntegerFrunction.getCached(Math.abs(i_left.getNative()));
      }
    };
    return expression.getAsBinding("abs", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodAddition(Frunction type_frunction) throws SyntaxError, RunTimeError
  {
    // Generating the first method
    String[] input = new String[] { "other" };
    String[] types = new String[] { IntegerFrunction.TYPE };
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

        if(!left.getType().equals(IntegerFrunction.TYPE) || !(left instanceof IntegerFrunction)) throw new IllegalStateException();
        if(!right.getType().equals(IntegerFrunction.TYPE) || !(right instanceof IntegerFrunction)) throw new IllegalStateException();
        
        IntegerFrunction i_left = (IntegerFrunction) left;
        IntegerFrunction i_right = (IntegerFrunction) right;
        // Performing the native operation.
        return IntegerFrunction.getCached(i_left.getNative() + i_right.getNative());
      }
    };
    return expression.getAsBinding("add", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodMultiplication(Frunction type_frunction) throws SyntaxError, RunTimeError
  {
    // Generating the first method
    String[] input = new String[] { "other" };
    String[] types = new String[] { IntegerFrunction.TYPE };
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

        if(!left.getType().equals(IntegerFrunction.TYPE) || !(left instanceof IntegerFrunction)) throw new IllegalStateException();
        if(!right.getType().equals(IntegerFrunction.TYPE) || !(right instanceof IntegerFrunction)) throw new IllegalStateException();
        
        IntegerFrunction i_left = (IntegerFrunction) left;
        IntegerFrunction i_right = (IntegerFrunction) right;
        // Performing the native operation.
        return IntegerFrunction.getCached(i_left.getNative() * i_right.getNative());
      }
    };
    return expression.getAsBinding("mult", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodDivision(Frunction type_frunction) throws SyntaxError, RunTimeError
  {
    // Generating the first method
    String[] input = new String[] { "other" };
    String[] types = new String[] { IntegerFrunction.TYPE };
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

        if(!left.getType().equals(IntegerFrunction.TYPE) || !(left instanceof IntegerFrunction)) throw new IllegalStateException();
        if(!right.getType().equals(IntegerFrunction.TYPE) || !(right instanceof IntegerFrunction)) throw new IllegalStateException();
        
        IntegerFrunction i_left = (IntegerFrunction) left;
        IntegerFrunction i_right = (IntegerFrunction) right;
        // Performing the native operation.
        return IntegerFrunction.getCached(i_left.getNative() / i_right.getNative());
      }
    };
    return expression.getAsBinding("div", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodRemainder(Frunction type_frunction) throws SyntaxError, RunTimeError
  {
    // Generating the first method
    String[] input = new String[] { "other" };
    String[] types = new String[] { IntegerFrunction.TYPE };
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

        if(!left.getType().equals(IntegerFrunction.TYPE) || !(left instanceof IntegerFrunction)) throw new IllegalStateException();
        if(!right.getType().equals(IntegerFrunction.TYPE) || !(right instanceof IntegerFrunction)) throw new IllegalStateException();
        
        IntegerFrunction i_left = (IntegerFrunction) left;
        IntegerFrunction i_right = (IntegerFrunction) right;
        // Performing the native operation.
        return IntegerFrunction.getCached(i_left.getNative() % i_right.getNative());
      }
    };
    return expression.getAsBinding("rem", type_frunction, input, types, condition);
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
