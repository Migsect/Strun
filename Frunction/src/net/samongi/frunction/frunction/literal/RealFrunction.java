package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.error.runtime.FrunctionNotEvaluatedError;
import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.error.syntax.SyntaxError;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.literal.dictionary.LiteralDictionary;
import net.samongi.frunction.frunction.literal.method.NativeExpression;

public class RealFrunction extends NativeFrunction
{
  private static final String TYPE = "real";

  public static final Frunction parseLiteral(String symbol, Container environment) throws SyntaxError, RunTimeError
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
    catch(SyntaxError | RunTimeError e)
    {
      e.printStackTrace();
    }
    return null;
  }

  private final double state;

  public RealFrunction(Container environment, double state) throws SyntaxError, RunTimeError
  {
    super(environment);

    this.state = state;

    // Adding the methods
    try
    {
      this.addMethods();
    }
    catch(FrunctionNotEvaluatedError e)
    {
      e.printStackTrace();
    }
  }

  private void addMethods() throws SyntaxError, RunTimeError
  {
    this.addSymbol(this.methodEquals());
    this.addSymbol(this.methodGreater());
    this.addSymbol(this.methodLesser());
    
    this.addSymbol(this.methodNegative());
    this.addSymbol(this.methodAbsolute());
    this.addSymbol(this.methodRound());
    this.addSymbol(this.methodCeiling());
    this.addSymbol(this.methodFloor());
    
    this.addSymbol(this.methodString());
    this.addSymbol(this.methodInt());
    
    this.addSymbol(this.methodAddition());
    this.addSymbol(this.methodDivision());
    this.addSymbol(this.methodMultiplication());
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

        if(!left.getType().equals(RealFrunction.TYPE) || !(left instanceof RealFrunction)) return null; 
        if(!right.getType().equals(RealFrunction.TYPE) || !(right instanceof RealFrunction)) return null;
        RealFrunction r_left = (RealFrunction) left;
        RealFrunction r_right = (RealFrunction) right;
        // Performing the native operation.
        return BooleanFrunction.getCached(r_left.getNative() == r_right.getNative());
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

        if(!left.getType().equals(RealFrunction.TYPE) || !(left instanceof RealFrunction)) return null; 
        RealFrunction r_left = (RealFrunction) left;
        // Performing the native operation.
        return StringFrunction.getCached("" + r_left.getNative());
      }

    };
    return expression.getAsBinding("str", this, input, types, condition);
  }
  
  private SymbolBinding methodInt() throws SyntaxError, RunTimeError
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

        if(!left.getType().equals(RealFrunction.TYPE) || !(left instanceof RealFrunction)) return null; 
        RealFrunction r_left = (RealFrunction) left;
        // Performing the native operation.
        return IntegerFrunction.getCached((long)r_left.getNative());
      }

    };
    return expression.getAsBinding("int", this, input, types, condition);
  }
  
  private SymbolBinding methodRound() throws SyntaxError, RunTimeError
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

        if(!left.getType().equals(RealFrunction.TYPE) || !(left instanceof RealFrunction)) return null;
        RealFrunction r_left = (RealFrunction) left;
        // Performing the native operation.
        return RealFrunction.getCached(Math.round(r_left.getNative()));
      }

    };
    return expression.getAsBinding("round", this, input, types, condition);
  }
  
  private SymbolBinding methodFloor() throws SyntaxError, RunTimeError
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

        if(!left.getType().equals(RealFrunction.TYPE) || !(left instanceof RealFrunction)) return null;
        RealFrunction r_left = (RealFrunction) left;
        // Performing the native operation.
        return RealFrunction.getCached(Math.floor(r_left.getNative()));
      }

    };
    return expression.getAsBinding("floor", this, input, types, condition);
  }
  
  private SymbolBinding methodCeiling() throws SyntaxError, RunTimeError
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

        if(!left.getType().equals(RealFrunction.TYPE) || !(left instanceof RealFrunction)) return null;
        RealFrunction r_left = (RealFrunction) left;
        // Performing the native operation.
        return RealFrunction.getCached(Math.ceil(r_left.getNative()));
      }

    };
    return expression.getAsBinding("ceil", this, input, types, condition);
  }
  
  private SymbolBinding methodNegative() throws SyntaxError, RunTimeError
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

        if(!left.getType().equals(RealFrunction.TYPE) || !(left instanceof RealFrunction)) return null; 
        RealFrunction r_left = (RealFrunction) left;
        // Performing the native operation.
        return RealFrunction.getCached( -r_left.getNative());
      }

    };
    return expression.getAsBinding("neg", this, input, types, condition);
  }
  
  private SymbolBinding methodAbsolute() throws SyntaxError, RunTimeError
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

        if(!left.getType().equals(RealFrunction.TYPE) || !(left instanceof RealFrunction)) return null; 
        RealFrunction r_left = (RealFrunction) left;
        // Performing the native operation.
        return RealFrunction.getCached(Math.abs(r_left.getNative()));
      }

    };
    return expression.getAsBinding("abs", this, input, types, condition);
  }
  
  private SymbolBinding methodGreater() throws SyntaxError, RunTimeError
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

        if(!left.getType().equals(RealFrunction.TYPE) || !(left instanceof RealFrunction)) { return null; // We should
                                                                                                          // technically
                                                                                                          // never get to
                                                                                                          // this stage.
        }
        if(!right.getType().equals(RealFrunction.TYPE) || !(right instanceof RealFrunction)) { return null; // We should
                                                                                                            // technically
                                                                                                            // never get to
                                                                                                            // this stage.
        }
        RealFrunction r_left = (RealFrunction) left;
        RealFrunction r_right = (RealFrunction) right;
        // Performing the native operation.
        return BooleanFrunction.getCached(r_left.getNative() > r_right.getNative());
      }

    };
    return expression.getAsBinding("grtr", this, input, types, condition);
  }
  private SymbolBinding methodLesser() throws SyntaxError, RunTimeError
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

        if(!left.getType().equals(RealFrunction.TYPE) || !(left instanceof RealFrunction)) return null;   
        if(!right.getType().equals(RealFrunction.TYPE) || !(right instanceof RealFrunction)) return null;         
        RealFrunction r_left = (RealFrunction) left;
        RealFrunction r_right = (RealFrunction) right;
        // Performing the native operation.
        return BooleanFrunction.getCached(r_left.getNative() < r_right.getNative());
      }

    };
    return expression.getAsBinding("lssr", this, input, types, condition);
  }
  
  private SymbolBinding methodAddition() throws SyntaxError, RunTimeError
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

        if(!left.getType().equals(RealFrunction.TYPE) || !(left instanceof RealFrunction)) { return null; // We should
                                                                                                          // technically
                                                                                                          // never get to
                                                                                                          // this stage.
        }
        if(!right.getType().equals(RealFrunction.TYPE) || !(right instanceof RealFrunction)) { return null; // We should
                                                                                                            // technically
                                                                                                            // never get to
                                                                                                            // this stage.
        }
        RealFrunction r_left = (RealFrunction) left;
        RealFrunction r_right = (RealFrunction) right;
        // Performing the native operation.
        return RealFrunction.getCached(r_left.getNative() + r_right.getNative());
      }

    };
    return expression.getAsBinding("add", this, input, types, condition);
  }
  
  private SymbolBinding methodMultiplication() throws SyntaxError, RunTimeError
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

        if(!left.getType().equals(RealFrunction.TYPE) || !(left instanceof RealFrunction)) { return null; // We should
                                                                                                          // technically
                                                                                                          // never get to
                                                                                                          // this stage.
        }
        if(!right.getType().equals(RealFrunction.TYPE) || !(right instanceof RealFrunction)) { return null; // We should
                                                                                                            // technically
                                                                                                            // never get to
                                                                                                            // this stage.
        }
        RealFrunction r_left = (RealFrunction) left;
        RealFrunction r_right = (RealFrunction) right;
        // Performing the native operation.
        return RealFrunction.getCached(r_left.getNative() * r_right.getNative());
      }

    };
    return expression.getAsBinding("mult", this, input, types, condition);
  }
  
  private SymbolBinding methodDivision() throws SyntaxError, RunTimeError
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

        if(!left.getType().equals(RealFrunction.TYPE) || !(left instanceof RealFrunction)) { return null; // We should
                                                                                                          // technically
                                                                                                          // never get to
                                                                                                          // this stage.
        }
        if(!right.getType().equals(RealFrunction.TYPE) || !(right instanceof RealFrunction)) { return null; // We should
                                                                                                            // technically
                                                                                                            // never get to
                                                                                                            // this stage.
        }
        RealFrunction r_left = (RealFrunction) left;
        RealFrunction r_right = (RealFrunction) right;
        // Performing the native operation.
        return RealFrunction.getCached(r_left.getNative() / r_right.getNative());
      }

    };
    return expression.getAsBinding("div", this, input, types, condition);
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
