package net.samongi.frunction.frunction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.samongi.frunction.binding.DynamicSymbolBinding;
import net.samongi.frunction.binding.MethodBinding;
import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.frunction.exceptions.SymbolNotFoundException;

/**Used as an intermeditary for methods to contain the extra symbols.
 * 
 * @author Alex
 *
 */
public class MethodContainer implements Container
{
  private Map<String, SymbolBinding> override_symbols = new HashMap<>();;
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
  public void addSymbol(String symbol, Frunction frunction)
  {
    SymbolBinding s_binding = new DynamicSymbolBinding(symbol, frunction, this.environment);
    this.override_symbols.put(symbol, s_binding);
  }

  // Shouldn't do anything yet
  @Override public MethodBinding getMethod(String[] types, Frunction[] inputs){return null;}
  
  @Override public SymbolBinding getSymbol(String symbol) throws SymbolNotFoundException
  {
    if(override_symbols.containsKey(symbol)) return override_symbols.get(symbol);
    return this.environment.getSymbol(symbol);
  }

	@Override public Container getEnvironment(){return this.environment;}

	@Override public void addMethod(MethodBinding binding){}
	@Override public void addSymbol(SymbolBinding binding)
	{
		this.override_symbols.put(binding.getKey(), binding);
	}

	@Override public List<MethodBinding> getMethods()
	{
		// TODO 
		return null;
	}

	@Override public List<SymbolBinding> getSymbols()
	{
		// TODO 
		return null;
	}
}
