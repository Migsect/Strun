package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.error.syntax.SyntaxError;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.literal.dictionary.LiteralDictionary;
import net.samongi.frunction.frunction.literal.method.NativeExpression;
import net.samongi.frunction.frunction.type.TypeDefiner;

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
        type_frunction.addSymbol(RealFrunction.methodEquals(type_frunction));
        type_frunction.addSymbol(RealFrunction.methodGreater(type_frunction));
        type_frunction.addSymbol(RealFrunction.methodLesser(type_frunction));
        
        type_frunction.addSymbol(RealFrunction.methodNegative(type_frunction));
        type_frunction.addSymbol(RealFrunction.methodAbsolute(type_frunction));
        
        type_frunction.addSymbol(RealFrunction.methodString(type_frunction));
        type_frunction.addSymbol(RealFrunction.methodInt(type_frunction));
        
        type_frunction.addSymbol(RealFrunction.methodFloor(type_frunction));
        type_frunction.addSymbol(RealFrunction.methodCeiling(type_frunction));
        type_frunction.addSymbol(RealFrunction.methodRound(type_frunction));
        
        type_frunction.addSymbol(RealFrunction.methodAddition(type_frunction));
        type_frunction.addSymbol(RealFrunction.methodDivision(type_frunction));
        type_frunction.addSymbol(RealFrunction.methodMultiplication(type_frunction));
      }
    };
  }

  private final double state;

  public RealFrunction(Container environment, double state) throws SyntaxError, RunTimeError
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

        if(!left.getType().equals(RealFrunction.TYPE) || !(left instanceof RealFrunction)) return null; 
        RealFrunction r_left = (RealFrunction) left;
        // Performing the native operation.
        return StringFrunction.getCached("" + r_left.getNative());
      }

    };
    return expression.getAsBinding("str", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodInt(Frunction type_frunction) throws SyntaxError, RunTimeError
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
    return expression.getAsBinding("int", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodRound(Frunction type_frunction) throws SyntaxError, RunTimeError
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
    return expression.getAsBinding("round", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodFloor(Frunction type_frunction) throws SyntaxError, RunTimeError
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
    return expression.getAsBinding("floor", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodCeiling(Frunction type_frunction) throws SyntaxError, RunTimeError
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
    return expression.getAsBinding("ceil", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodNegative(Frunction type_frunction) throws SyntaxError, RunTimeError
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
    return expression.getAsBinding("neg", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodAbsolute(Frunction type_frunction) throws SyntaxError, RunTimeError
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
    return expression.getAsBinding("abs", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodGreater(Frunction type_frunction) throws SyntaxError, RunTimeError
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
        return BooleanFrunction.getCached(r_left.getNative() > r_right.getNative());
      }

    };
    return expression.getAsBinding("gt", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodLesser(Frunction type_frunction) throws SyntaxError, RunTimeError
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
    return expression.getAsBinding("lt", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodAddition(Frunction type_frunction) throws SyntaxError, RunTimeError
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
        return RealFrunction.getCached(r_left.getNative() + r_right.getNative());
      }

    };
    return expression.getAsBinding("add", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodMultiplication(Frunction type_frunction) throws SyntaxError, RunTimeError
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
    return expression.getAsBinding("mult", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodDivision(Frunction type_frunction) throws SyntaxError, RunTimeError
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
    return expression.getAsBinding("div", type_frunction, input, types, condition);
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
