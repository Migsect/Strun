package net.samongi.frunction.frunction;

import java.util.HashMap;
import java.util.Map;

import net.samongi.frunction.binding.DynamicMethodBinding;
import net.samongi.frunction.binding.DynamicSymbolBinding;

/**Used as an intermeditary for methods to contain the extra symbols.
 * 
 * @author Alex
 *
 */
public class MethodContainer implements Container
{
  private Map<String, DynamicSymbolBinding> override_symbols = new HashMap<>();;
  private final Container environment;
  
  public MethodContainer(Container environment)
  {
    this.environment = environment;
  }
  
  /**Adds a symbol to this method container
   * This will override symbols in the environment
   * 
   * @param symbol
   * @param frunction
   */
  public void addSymbol(String symbol, DynamicFrunction frunction)
  {
    DynamicSymbolBinding s_binding = new DynamicSymbolBinding(symbol, frunction, this.environment);
    this.override_symbols.put(symbol, s_binding);
  }

  // Shouldn't do anything yet
  @Override public DynamicMethodBinding getMethod(String[] types, DynamicFrunction[] inputs){return null;}
  
  @Override public DynamicSymbolBinding getSymbol(String symbol)
  {
    if(override_symbols.containsKey(symbol)) return override_symbols.get(symbol);
    return this.environment.getSymbol(symbol);
  }

	@Override public Container getEnvironment(){return this.environment;}
}
