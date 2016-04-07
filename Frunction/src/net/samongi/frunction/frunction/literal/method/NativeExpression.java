package net.samongi.frunction.frunction.literal.method;

import net.samongi.frunction.binding.DynamicMethodBinding;
import net.samongi.frunction.binding.DynamicSymbolBinding;
import net.samongi.frunction.binding.MethodBinding;
import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.DynamicFrunction;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.exceptions.FrunctionNotEvaluatedException;
import net.samongi.frunction.frunction.exceptions.SymbolNotFoundException;

public abstract class NativeExpression implements Expression
{
  private Container environment = null;
  
  /**This will evaluate the expression based on the environment if was fed with.
   * 
   * @return a frunction based off of evaluation
   */
  public abstract Frunction evaluate();
  
  @Override public final Frunction evaluate(Container environment)
  {
    this.environment = environment; //  Setting the local environment;
    return this.evaluate();
  }
  
  /**Will return the frunction that is one level up from the current environment.
   * If the environment is null, then this will do little to stop the null pointer exception
   * 
   * @return The upper-level frunction
   */
  protected Frunction getSelf()
  {
    try{return this.environment.getSymbol("^@").get();}
    catch (TokenException e){e.printStackTrace();}
    catch (SymbolNotFoundException e){e.printStackTrace();}
    return null;
  }
  
  /**Will return the frunction with the symbol.  If it doesn't
   * exist we will print stack trace and then return null.
   * 
   * @param symbol The symbol to check the environment for
   * @return The frunction at the input
   */
  protected Frunction getInput(String symbol)
  {
    try{return this.environment.getSymbol(symbol).get();}
    catch (TokenException e){e.printStackTrace();}
    catch (SymbolNotFoundException e){e.printStackTrace();}
    return null;
  }
  
  /**Gets this expression as a dynamicSymbolbinding
   * 
   * @param key The key for the dynamic symbol
   * @param environment The environment this is defined in
   * @param input The inputs for the inner method binding
   * @param types The types for the inner method binding
   * @param condition The condition for the inner method binding.
   * @return A symbol binding.
   */
  public SymbolBinding getAsBinding(String key, Container environment, String[] input, String[] types, Expression condition)
  {
    Frunction method_holder = new DynamicFrunction(environment);
    
    MethodBinding method = new DynamicMethodBinding(environment, types, types, condition, this);
    try{method_holder.addMethod(method);}
    catch(FrunctionNotEvaluatedException e){e.printStackTrace();}
    
    return new DynamicSymbolBinding(key, method_holder, environment);
  }
}
