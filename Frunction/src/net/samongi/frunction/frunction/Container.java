package net.samongi.frunction.frunction;

import net.samongi.frunction.binding.MethodBinding;
import net.samongi.frunction.binding.SymbolBinding;

public interface Container
{
  /**Gets the method binding for the corresponding types
   * 
   * @param types THe types to get a method for
   * @return A MethodBinding, otherwise null
   */
  public MethodBinding getMethod(String[] types, Frunction[] inputs);
  
  /**Adds the binding to this container
   * 
   * @param types The types of arguments the binding takes.
   * @param binding The binding to add
   */
  public void addMethod(String[] types, MethodBinding binding);
  
  /**Gets the symbol binding for the corresponding symbol
   * 
   * @param symbol The symbol to retrieve
   * @return A SymbolBinding, otherwise null
   */
  public SymbolBinding getSymbol(String symbol);
  
  /**Adds the binding to this container
   * 
   * @param symbol The symbol's string to add
   * @param binding The binding to add
   */
  public void addSymbol(String symbol, SymbolBinding binding);
  
  /**Returns the environment that the container is contained within.
   * This may be null if the container is a top level container
   * 
   * @return The container
   */
  public Container getEnvironment();
}
