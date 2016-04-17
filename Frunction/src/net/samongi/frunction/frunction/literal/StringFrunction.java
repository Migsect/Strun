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

public class StringFrunction extends NativeFrunction
{
  public static final String STRING_CAPSULE = "\"";

  private static final String TYPE = "string";

  public static Frunction parseLiteral(String symbol, Container environment) throws SyntaxError, RunTimeError
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
        type_frunction.addSymbol(StringFrunction.methodEquals(type_frunction));
        
        type_frunction.addSymbol(StringFrunction.methodConcatinate(type_frunction));
        type_frunction.addSymbol(StringFrunction.methodSubstring(type_frunction));

        type_frunction.addSymbol(StringFrunction.methodLength(type_frunction));
        
        type_frunction.addSymbol(StringFrunction.methodPrint(type_frunction));
        type_frunction.addSymbol(StringFrunction.methodPrintln(type_frunction));
        
      }
    };
  }

  private final String state;

  public StringFrunction(Container environment, String state) throws SyntaxError, RunTimeError
  {
    super(environment);

    // setting the state of the string
    this.state = state.replace("\\n", System.getProperty("line.separator"));
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
    String[] types = new String[] { StringFrunction.TYPE };
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

        if(!left.isType(StringFrunction.TYPE) || !(left instanceof StringFrunction)) return null; 
        if(!right.isType(StringFrunction.TYPE) || !(right instanceof StringFrunction)) return null; 
        StringFrunction s_left = (StringFrunction) left;
        StringFrunction s_right = (StringFrunction) right;
        // Performing the native operation.
        return BooleanFrunction.getCached(s_left.getNative().equals(s_right.getNative()));
      }

    };
    return expression.getAsBinding("eq", type_frunction, input, types, condition);
  }

  private static SymbolBinding methodPrint(Frunction type_frunction) throws SyntaxError, RunTimeError
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

        if(!left.isType(StringFrunction.TYPE) || !(left instanceof StringFrunction)) return null;

        // Converting the type
        StringFrunction s_left = (StringFrunction) left;

        // performing the action
        System.out.print(s_left.getNative());

        return left;
      }

    };
    return expression.getAsBinding("print", type_frunction, input, types, condition);
  }

  private static SymbolBinding methodPrintln(Frunction type_frunction) throws SyntaxError, RunTimeError
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

        if(!left.isType(StringFrunction.TYPE) || !(left instanceof StringFrunction)) return null; 

        // Converting the type
        StringFrunction s_left = (StringFrunction) left;

        // performing the action
        System.out.println(s_left.getNative());

        return left;
      }

    };
    return expression.getAsBinding("println", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodLength(Frunction type_frunction) throws SyntaxError, RunTimeError
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

        if(!left.isType(StringFrunction.TYPE) || !(left instanceof StringFrunction)) return null;

        // Converting the type
        StringFrunction s_left = (StringFrunction) left;
        
        return IntegerFrunction.getCached(s_left.getNative().length());
      }

    };
    return expression.getAsBinding("len", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodConcatinate(Frunction type_frunction) throws SyntaxError, RunTimeError
  {
    // Generating the first method
    String[] input = new String[] { "other" };
    String[] types = new String[] { StringFrunction.TYPE };
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

        if(!left.isType(StringFrunction.TYPE) || !(left instanceof StringFrunction)) return null; 
        if(!right.isType(StringFrunction.TYPE) || !(right instanceof StringFrunction)) return null; 
        StringFrunction s_left = (StringFrunction) left;
        StringFrunction s_right = (StringFrunction) right;
        // Performing the native operation.
        return StringFrunction.getCached(s_left.getNative().concat(s_right.getNative()));
      }

    };
    return expression.getAsBinding("con", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodSubstring(Frunction type_frunction) throws SyntaxError, RunTimeError
  {
    // Generating the first method
    String[] input = new String[] { "start" , "end" };
    String[] types = new String[] { IntegerFrunction.TYPE, IntegerFrunction.TYPE };
    Expression condition = BooleanFrunction.getTautology();

    NativeExpression expression = new NativeExpression()
    {
      @Override public Frunction evaluate() throws ExpressionError
      {
        // Getting the left argument which should be the "@" self binding.
        Frunction left = this.getSelf();

        // Getting the right argument which should be the "other" argument as
        // defined
        Frunction start = this.getInput("start");
        Frunction end = this.getInput("end");

        if(!left.isType(StringFrunction.TYPE) || !(left instanceof StringFrunction)) return null; 
        if(!start.isType(IntegerFrunction.TYPE) || !(start instanceof IntegerFrunction)) return null; 
        if(!end.isType(IntegerFrunction.TYPE) || !(end instanceof IntegerFrunction)) return null; 
        
        StringFrunction s_left = (StringFrunction) left;
        IntegerFrunction i_start = (IntegerFrunction) start;
        IntegerFrunction i_end = (IntegerFrunction) end;
        // Performing the native operation.
        return StringFrunction.getCached(s_left.getNative().substring((int)i_start.getNative(), (int)i_end.getNative()));
      }

    };
    return expression.getAsBinding("sub", type_frunction, input, types, condition);
  }

  @Override public String getType()
  {
    return TYPE;
  }

  public String getNative()
  {
    return this.state;
  }
  
  @Override public String asString()
  {
    return this.state;
  }

}
