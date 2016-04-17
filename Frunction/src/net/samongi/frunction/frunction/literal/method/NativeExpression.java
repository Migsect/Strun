package net.samongi.frunction.frunction.literal.method;

import net.samongi.frunction.binding.DynamicMethodBinding;
import net.samongi.frunction.binding.DynamicSymbolBinding;
import net.samongi.frunction.binding.MethodBinding;
import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.error.runtime.FrunctionNotEvaluatedError;
import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.error.runtime.SymbolNotFoundError;
import net.samongi.frunction.error.syntax.ExpressionError;
import net.samongi.frunction.error.syntax.SyntaxError;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.DynamicFrunction;
import net.samongi.frunction.frunction.Frunction;

public abstract class NativeExpression implements Expression
{
  private Container environment = null;

  /** This will evaluate the expression based on the environment if was fed with.
   * 
   * @return a frunction based off of evaluation 
   * @throws ExpressionError 
   * @throws RunTimeError 
   * @throws SyntaxError */
  public abstract Frunction evaluate() throws SyntaxError, RunTimeError;

  @Override public final Frunction evaluate(Container environment) throws SyntaxError, RunTimeError
  {
    this.environment = environment; // Setting the local environment;
    return this.evaluate();
  }

  /** Will return the frunction that is one level up from the current environment. If the environment is null, then this
   * will do little to stop the null pointer exception
   * 
   * @return The upper-level frunction */
  protected Frunction getSelf()
  {
    try
    {
      SymbolBinding binding =  this.environment.getSymbol("^@");
      if(binding == null) throw new SymbolNotFoundError(this.environment, "^@");
      
      return binding.get(this.environment);
    }
    catch(SyntaxError e)
    {
      e.printStackTrace();
    }
    catch(RunTimeError e)
    {
      e.printStackTrace();
    }
    return null;
  }

  /** Will return the frunction with the symbol. If it doesn't exist we will print stack trace and then return null.
   * 
   * @param symbol The symbol to check the environment for
   * @return The frunction at the input */
  protected Frunction getInput(String symbol)
  {
    try
    {
      SymbolBinding binding = this.environment.getSymbol(symbol);
      if(binding == null) throw new SymbolNotFoundError(this.environment, symbol);
      
      return binding.get(this.environment);
    }
    catch(SyntaxError e)
    {
      e.printStackTrace();
    }
    catch(RunTimeError e)
    {
      e.printStackTrace();
    }
    return null;
  }

  /** Gets this expression as a dynamicSymbolbinding
   * 
   * @param key The key for the dynamic symbol
   * @param environment The environment this is defined in
   * @param input The inputs for the inner method binding
   * @param types The types for the inner method binding
   * @param condition The condition for the inner method binding.
   * @return A symbol binding. 
   * @throws RunTimeError 
   * @throws SyntaxError */
  public SymbolBinding getAsBinding(String key, Container environment, String[] input, String[] types, Expression condition) throws SyntaxError, RunTimeError
  {
    Frunction method_holder = new DynamicFrunction(environment);

    MethodBinding method = new DynamicMethodBinding(input, types, condition, this);
    try
    {
      method_holder.addMethod(method);
    }
    catch(FrunctionNotEvaluatedError e)
    {
      e.printStackTrace();
    }

    SymbolBinding binding = new DynamicSymbolBinding(key, method_holder);
    binding.setCountable(false);
    return binding;
  }

  @Override public Expression.Type getType()
  {
    return Expression.Type.NATIVE;
  }
}
