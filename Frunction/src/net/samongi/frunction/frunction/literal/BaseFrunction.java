package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.error.syntax.ExpressionError;
import net.samongi.frunction.error.syntax.SyntaxError;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.literal.method.NativeExpression;
import net.samongi.frunction.frunction.type.TypeDefiner;

public class BaseFrunction
{
  public static final String TYPE = "struct";
  
  public static TypeDefiner getTypeDefiner()
  {
    return new TypeDefiner(TYPE)
    {
      @Override protected void defineType(Frunction type_frunction) throws RunTimeError, SyntaxError
      {
        type_frunction.setType("");
        type_frunction.addSymbol(BaseFrunction.methodInstanceOf(type_frunction));
        type_frunction.addSymbol(BaseFrunction.methodIsAccessible(type_frunction));

        type_frunction.addSymbol(BaseFrunction.methodSize(type_frunction));
        type_frunction.addSymbol(BaseFrunction.methodMethodSize(type_frunction));
        type_frunction.addSymbol(BaseFrunction.methodSymbolSize(type_frunction));
        type_frunction.addSymbol(BaseFrunction.methodEmpty(type_frunction));
      }
    };
  }
  
  private static SymbolBinding methodSize(Frunction type_frunction) throws SyntaxError, RunTimeError
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
        
        return IntegerFrunction.getCached(left.countLocalMethods() + left.countLocalSymbols());
      }

    };
    return expression.getAsSymbolBinding("size", type_frunction, input, types, condition);
  }
  private static SymbolBinding methodEmpty(Frunction type_frunction) throws SyntaxError, RunTimeError
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
        //System.out.println("Syms: '" + left.countLocalSymbols() + "'");
        //System.out.println("Meth: '" + left.countLocalMethods() + "'");
        return BooleanFrunction.getCached(0 == (left.countLocalMethods() + left.countLocalSymbols()));
      }

    };
    return expression.getAsSymbolBinding("empt", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodSymbolSize(Frunction type_frunction) throws SyntaxError, RunTimeError
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
        
        return IntegerFrunction.getCached(left.countLocalSymbols());
      }

    };
    return expression.getAsSymbolBinding("ssize", type_frunction, input, types, condition);
  }
  private static SymbolBinding methodMethodSize(Frunction type_frunction) throws SyntaxError, RunTimeError
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
        
        return IntegerFrunction.getCached(left.countLocalMethods());
      }

    };
    return expression.getAsSymbolBinding("msize", type_frunction, input, types, condition);
  }
  /*
  private static SymbolBinding methodType(Frunction type_frunction) throws SyntaxError, RunTimeError
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
        
        return StringFrunction.getCached(left.getType());
      }

    };
    return expression.getAsBinding("type", type_frunction, input, types, condition);
  }
  */
  private static SymbolBinding methodInstanceOf(Frunction type_frunction) throws SyntaxError, RunTimeError
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

        if(!right.isType(StringFrunction.TYPE) || !(right instanceof StringFrunction)) return null; 
        StringFrunction s_right = (StringFrunction) right;
        // Performing the native operation.
        return BooleanFrunction.getCached(left.isType(s_right.getNative()));
      }

    };
    return expression.getAsSymbolBinding("inst", type_frunction, input, types, condition);
  }
  
  private static SymbolBinding methodIsAccessible(Frunction type_frunction) throws SyntaxError, RunTimeError
  {
    // Generating the first method
    String[] input = new String[] { "other" };
    String[] types = new String[] { StringFrunction.TYPE };
    Expression condition = BooleanFrunction.getTautology();

    NativeExpression expression = new NativeExpression()
    {
      @Override public Frunction evaluate() throws SyntaxError, RunTimeError
      {
        // Getting the left argument which should be the "@" self binding.
        Frunction left = this.getSelf();

        // Getting the right argument which should be the "other" argument as
        // defined
        Frunction right = this.getInput("other");

        if(!right.isType(StringFrunction.TYPE) || !(right instanceof StringFrunction)) return null; 
        StringFrunction s_right = (StringFrunction) right;
        // Performing the native operation.
        return BooleanFrunction.getCached(left.hasAccessibleSymbol(s_right.getNative()));
      }

    };
    return expression.getAsSymbolBinding("accs", type_frunction, input, types, condition);
  }
}
