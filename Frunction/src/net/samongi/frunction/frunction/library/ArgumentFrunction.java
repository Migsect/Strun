package net.samongi.frunction.frunction.library;

import net.samongi.frunction.binding.MethodBinding;
import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.error.syntax.ExpressionError;
import net.samongi.frunction.error.syntax.SyntaxError;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.DynamicFrunction;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.literal.BooleanFrunction;
import net.samongi.frunction.frunction.literal.IntegerFrunction;
import net.samongi.frunction.frunction.literal.StringFrunction;
import net.samongi.frunction.frunction.literal.method.NativeExpression;

public class ArgumentFrunction
{
  public static FrunctionConstructor getConstructor(String[] args)
  {
    return new FrunctionConstructor()
    {
      String[] arguments = args;
      
      @Override public Frunction constructFrunction(Container environment) throws SyntaxError, RunTimeError
      {
        DynamicFrunction frunction = new DynamicFrunction(environment);
        frunction.evaluate();
        frunction.addMethod(ArgumentFrunction.methodBindingAccessor(arguments));
        frunction.addSymbol(ArgumentFrunction.SymbolBindingLength(arguments, environment));
        
        return frunction;
      }
    };
  }
  
  private static MethodBinding methodBindingAccessor(String[] arguments)
  {
    String[] input = new String[] { "index" };
    String[] types = new String[] { IntegerFrunction.TYPE };
    Expression condition = BooleanFrunction.getTautology();
    NativeExpression expression = new NativeExpression()
    {

      @Override public Frunction evaluate() throws SyntaxError, RunTimeError
      {
        // Getting the index
        Frunction index = this.getInput("index");
        
        if(!index.isType(IntegerFrunction.TYPE) || !(index instanceof IntegerFrunction)) throw new IllegalStateException();
        IntegerFrunction i_index = (IntegerFrunction) index;
        long i = i_index.getNative();
        return StringFrunction.getCached(arguments[(int) i]);
      }
      
    };
    return expression.getAsMethodBinding(input, types, condition);
  }
  
  private static SymbolBinding SymbolBindingLength(String[] arguments, Container environment) throws SyntaxError, RunTimeError
  {
    // Generating the first method
    String[] input = new String[] {};
    String[] types = new String[] {};
    Expression condition = BooleanFrunction.getTautology();

    NativeExpression expression = new NativeExpression()
    {
      @Override public Frunction evaluate() throws ExpressionError
      {
        return IntegerFrunction.getCached(arguments.length);
      }
    };
    return expression.getAsSymbolBinding("len", environment, input, types, condition);
  }}
