package net.samongi.frunction.frunction.library;

import net.samongi.frunction.binding.DynamicSymbolBinding;
import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.error.syntax.SyntaxError;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;

/**Frunctions constructors are an interface which will define a frunction and then add it to the
 * the main frunction
 * 
 * @author Alex
 *
 */
public interface FrunctionConstructor
{
  /**Will construct a frunction which is defined generally by java code.
   * 
   * @param environment The environment this will be defined in.
   * @return The fruction it constructs
   * @throws RunTimeError 
   * @throws SyntaxError 
   */
  public Frunction constructFrunction(Container environment) throws SyntaxError, RunTimeError;
  
  /**constructs the frunction and sets it as a symbol binding.
   * 
   * @param symbol The symbol to create the binding as
   * @return A symbol binding
   * @throws RunTimeError 
   * @throws SyntaxError 
   */
  public default SymbolBinding asBinding(String symbol, Container environment) throws SyntaxError, RunTimeError
  {
    return new DynamicSymbolBinding(symbol, this.constructFrunction(environment));
  }
}
