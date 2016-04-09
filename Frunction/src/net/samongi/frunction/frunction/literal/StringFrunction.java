package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.exceptions.parsing.ExpressionException;
import net.samongi.frunction.exceptions.parsing.ParsingException;
import net.samongi.frunction.exceptions.runtime.FrunctionNotEvaluatedException;
import net.samongi.frunction.exceptions.runtime.RunTimeException;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.literal.dictionary.LiteralDictionary;
import net.samongi.frunction.frunction.literal.method.NativeExpression;

public class StringFrunction extends NativeFrunction
{
  public static final String STRING_CAPSULE = "\"";

  private static final String TYPE = "string";

  public static Frunction parseLiteral(String symbol, Container environment) throws ParsingException, RunTimeException
  {
    if(!symbol.startsWith(STRING_CAPSULE) || !symbol.endsWith(STRING_CAPSULE)) return null;
    if(symbol.length() < 2) return null;
    
    String str = symbol.substring(1, symbol.length() - 1);
    return new StringFrunction(environment, str);
  }

  public static Frunction getCached(String str)
  {
    String sym = "\"" + str + "\"";
    try
    {
      return LiteralDictionary.getInstance().getSymbol(sym).get();
    }
    catch(ParsingException | RunTimeException e)
    {
      e.printStackTrace();
    }
    return null;
  }

  private final String state;

  public StringFrunction(Container environment, String state) throws ParsingException, RunTimeException
  {
    super(environment);

    // setting the state of the string
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

  private void addMethods() throws ParsingException, RunTimeException
  {
    this.addSymbol(this.methodEquals());
    this.addSymbol(this.methodString());
    this.addSymbol(this.methodPrint());
    this.addSymbol(this.methodPrintln());
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
    String[] types = new String[] { StringFrunction.TYPE };
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

        if(!left.getType().equals(StringFrunction.TYPE) || !(left instanceof StringFrunction)) { return null; // We should
                                                                                                              // technically
                                                                                                              // never get
                                                                                                              // to this
                                                                                                              // stage.
        }
        if(!right.getType().equals(StringFrunction.TYPE) || !(right instanceof StringFrunction)) { return null; // We should
                                                                                                                // technically
                                                                                                                // never get
                                                                                                                // to this
                                                                                                                // stage.
        }
        StringFrunction s_left = (StringFrunction) left;
        StringFrunction s_right = (StringFrunction) right;
        // Performing the native operation.
        return BooleanFrunction.getCached(s_left.getNative().equals(s_right.getNative()));
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

        if(!left.getType().equals(StringFrunction.TYPE) || !(left instanceof StringFrunction)) { return null; // We should
                                                                                                              // technically
                                                                                                              // never get
                                                                                                              // to this
                                                                                                              // stage.
        }
        return left;
      }

    };
    return expression.getAsBinding("str", this, input, types, condition);
  }

  private SymbolBinding methodPrint() throws ParsingException, RunTimeException
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

        if(!left.getType().equals(StringFrunction.TYPE) || !(left instanceof StringFrunction)) { return null; // We should
                                                                                                              // technically
                                                                                                              // never get
                                                                                                              // to this
                                                                                                              // stage.
        }

        // Converting the type
        StringFrunction s_left = (StringFrunction) left;

        // performing the action
        System.out.print(s_left.getNative());

        return left;
      }

    };
    return expression.getAsBinding("print", this, input, types, condition);
  }

  private SymbolBinding methodPrintln() throws ParsingException, RunTimeException
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

        if(!left.getType().equals(StringFrunction.TYPE) || !(left instanceof StringFrunction)) { return null; // We should
                                                                                                              // technically
                                                                                                              // never get
                                                                                                              // to this
                                                                                                              // stage.
        }

        // Converting the type
        StringFrunction s_left = (StringFrunction) left;

        // performing the action
        System.out.println(s_left.getNative());

        return left;
      }

    };
    return expression.getAsBinding("println", this, input, types, condition);
  }

  @Override public String getType()
  {
    return TYPE;
  }

  public String getNative()
  {
    return this.state;
  }

}
