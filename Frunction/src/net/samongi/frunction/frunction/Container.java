package net.samongi.frunction.frunction;

import net.samongi.frunction.binding.DynamicMethodBinding;
import net.samongi.frunction.binding.DynamicSymbolBinding;

public interface Container
{
  /**Gets the method binding for the corresponding types
   * 
   * @param types THe types to get a method for
   * @return A MethodBinding, otherwise null
   */
  public DynamicMethodBinding getMethod(String[] types, DynamicFrunction[] inputs);
  
  /**Gets the symbol binding for the corresponding symbol
   * 
   * @param symbol The symbol to retrieve
   * @return A SymbolBinding, otherwise null
   */
  public DynamicSymbolBinding getSymbol(String symbol);
  
  /**Returns the environment that the container is contained within.
   * This may be null if the container is a top level container
   * 
   * @return The container
   */
  public Container getEnvironment();
}