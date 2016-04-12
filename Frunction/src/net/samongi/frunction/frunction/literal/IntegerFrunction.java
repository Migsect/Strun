package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.exceptions.parsing.ExpressionException;
import net.samongi.frunction.exceptions.parsing.ParsingException;
import net.samongi.frunction.exceptions.runtime.RunTimeException;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.literal.dictionary.LiteralDictionary;
import net.samongi.frunction.frunction.literal.method.NativeExpression;

public class IntegerFrunction extends NativeFrunction
{
  public static final String TYPE = "int";

  public static final Frunction parseLiteral(String symbol, Container environment) throws ParsingException, RunTimeException
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
    catch(ParsingException | RunTimeException e)
    {
      e.printStackTrace();
    }
    return null;
  }

  private final long state;

  public IntegerFrunction(Container environment, long state) throws ParsingException, RunTimeException
  {
    super(environment);

    this.state = state;

    // Adding the methods
    try
    {
      this.addMethods();
    }
    catch(ParsingException | RunTimeException e)
    {
      e.printStackTrace();
    }
  }

  private void addMethods() throws ParsingException, RunTimeException
  {
    this.addSymbol(this.methodEquals());
    this.addSymbol(this.methodString());
    this.addSymbol(this.methodReal());
    this.addSymbol(this.methodGreater());
    this.addSymbol(this.methodLesser());
    this.addSymbol(this.methodNegative());
    this.addSymbol(this.methodAbsolute());
    this.addSymbol(this.methodAddition());
    this.addSymbol(this.methodMultiplication());
    this.addSymbol(this.methodDivision());
    this.addSymbol(this.methodRemainder());
  }

  /** Will generate a method binding for determining if another method is equal.
   * 
   * @return 
   * @throws RunTimeException 
   * @throws ParsingException */
  private SymbolBinding methodEquals() throws ParsingException, RunTimeException
  {
    // Generating the first method
    String[] input = new String[] { "other" };
    String[] types = new String[] { IntegerFrunction.TYPE };
    Expression condition = BooleanFrunction.getTautology();

    NativeExpression expression = new NativeExpression()
    {
      @Override public Frunction evaluate() throws ExpressionException
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
    return expression.getAsBinding("eq", this, input, types, condition);
  }

  private SymbolBinding methodString() throws ParsingException, RunTimeException
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
    return expression.getAsBinding("str", this, input, types, condition);
  }
  
  private SymbolBinding methodReal() throws ParsingException, RunTimeException
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
    return expression.getAsBinding("real", this, input, types, condition);
  }
  
  private SymbolBinding methodGreater() throws ParsingException, RunTimeException
  {
    // Generating the first method
    String[] input = new String[] { "other" };
    String[] types = new String[] { IntegerFrunction.TYPE };
    Expression condition = BooleanFrunction.getTautology();

    NativeExpression expression = new NativeExpression()
    {
      @Override public Frunction evaluate() throws ExpressionException
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
    return expression.getAsBinding("grtr", this, input, types, condition);
  }
  
  private SymbolBinding methodLesser() throws ParsingException, RunTimeException
  {
    // Generating the first method
    String[] input = new String[] { "other" };
    String[] types = new String[] { IntegerFrunction.TYPE };
    Expression condition = BooleanFrunction.getTautology();

    NativeExpression expression = new NativeExpression()
    {
      @Override public Frunction evaluate() throws ExpressionException
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
    return expression.getAsBinding("lssr", this, input, types, condition);
  }
  
  private SymbolBinding methodNegative() throws ParsingException, RunTimeException
  {
    // Generating the first method
    String[] input = new String[] {};
    String[] types = new String[] {};
    Expression condition = BooleanFrunction.getTautology();

    NativeExpression expression = new NativeExpression()
    {
      @Override public Frunction evaluate() throws ExpressionException
      {
        // Getting the left argument which should be the "@" self binding.
        Frunction left = this.getSelf();

        if(!left.getType().equals(IntegerFrunction.TYPE) || !(left instanceof IntegerFrunction)) throw new IllegalStateException();
        
        IntegerFrunction i_left = (IntegerFrunction) left;
        // Performing the native operation.
        return IntegerFrunction.getCached(-i_left.getNative());
      }
    };
    return expression.getAsBinding("neg", this, input, types, condition);
  }
  
  private SymbolBinding methodAbsolute() throws ParsingException, RunTimeException
  {
    // Generating the first method
    String[] input = new String[] {};
    String[] types = new String[] {};
    Expression condition = BooleanFrunction.getTautology();

    NativeExpression expression = new NativeExpression()
    {
      @Override public Frunction evaluate() throws ExpressionException
      {
        // Getting the left argument which should be the "@" self binding.
        Frunction left = this.getSelf();

        if(!left.getType().equals(IntegerFrunction.TYPE) || !(left instanceof IntegerFrunction)) throw new IllegalStateException();
        
        IntegerFrunction i_left = (IntegerFrunction) left;
        // Performing the native operation.
        return IntegerFrunction.getCached(Math.abs(i_left.getNative()));
      }
    };
    return expression.getAsBinding("abs", this, input, types, condition);
  }
  
  private SymbolBinding methodAddition() throws ParsingException, RunTimeException
  {
    // Generating the first method
    String[] input = new String[] { "other" };
    String[] types = new String[] { IntegerFrunction.TYPE };
    Expression condition = BooleanFrunction.getTautology();

    NativeExpression expression = new NativeExpression()
    {
      @Override public Frunction evaluate() throws ExpressionException
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
    return expression.getAsBinding("add", this, input, types, condition);
  }
  
  private SymbolBinding methodMultiplication() throws ParsingException, RunTimeException
  {
    // Generating the first method
    String[] input = new String[] { "other" };
    String[] types = new String[] { IntegerFrunction.TYPE };
    Expression condition = BooleanFrunction.getTautology();

    NativeExpression expression = new NativeExpression()
    {
      @Override public Frunction evaluate() throws ExpressionException
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
    return expression.getAsBinding("mult", this, input, types, condition);
  }
  
  private SymbolBinding methodDivision() throws ParsingException, RunTimeException
  {
    // Generating the first method
    String[] input = new String[] { "other" };
    String[] types = new String[] { IntegerFrunction.TYPE };
    Expression condition = BooleanFrunction.getTautology();

    NativeExpression expression = new NativeExpression()
    {
      @Override public Frunction evaluate() throws ExpressionException
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
    return expression.getAsBinding("div", this, input, types, condition);
  }
  
  private SymbolBinding methodRemainder() throws ParsingException, RunTimeException
  {
    // Generating the first method
    String[] input = new String[] { "other" };
    String[] types = new String[] { IntegerFrunction.TYPE };
    Expression condition = BooleanFrunction.getTautology();

    NativeExpression expression = new NativeExpression()
    {
      @Override public Frunction evaluate() throws ExpressionException
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
    return expression.getAsBinding("rem", this, input, types, condition);
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
