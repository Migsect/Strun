package net.samongi.frunction.frunction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.samongi.frunction.binding.DynamicSymbolBinding;
import net.samongi.frunction.binding.MethodBinding;
import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.frunction.exceptions.FrunctionNotEvaluatedException;
import net.samongi.frunction.frunction.exceptions.SymbolNotFoundException;

/**
 * Used as an intermeditary for methods to contain the extra symbols.
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

  /**
   * Adds a symbol to this method container This will override symbols in the
   * environment
   * 
   * @param symbol
   * @param frunction
   */
  public void addSymbol(String symbol, Frunction frunction)
  {
    SymbolBinding s_binding = new DynamicSymbolBinding(symbol, frunction,
        this.environment);
    this.override_symbols.put(symbol, s_binding);
  }

  // Calling the inner environment for get method
  @Override public MethodBinding getMethod(String[] types, Frunction[] inputs)
  {
    return this.environment.getMethod(types, inputs);
  }

  // Calling the inner environment for the get symbol given that this doesn't
  // wish to override it.
  @Override public SymbolBinding getSymbol(String symbol)
      throws SymbolNotFoundException
  {
    symbol = symbol.trim();
    if(override_symbols.containsKey(symbol))
      return override_symbols.get(symbol);
    return this.environment.getSymbol(symbol);
  }

  // This will return the environment above the environment this wraps
  // This is because the method container acts as a wrapper around its
  // environment.
  @Override public Container getEnvironment()
  {
    return this.environment.getEnvironment();
  }

  /**
   * Will return the environment that this wraps.
   * 
   * @return The environment that the method container wraps.
   */
  public Container getWrappedEnvironment()
  {
    return this.environment;
  }

  @Override public void addMethod(MethodBinding binding)
  {
    try
    {
      // Calling the inner's method for this.
      this.environment.addMethod(binding);
    }
    catch(FrunctionNotEvaluatedException e)
    {
      e.printStackTrace();
    }
  }

  @Override public void addSymbol(SymbolBinding binding)
  {
    this.override_symbols.put(binding.getKey(), binding);
  }

  @Override public List<MethodBinding> getMethods()
  {
    return this.environment.getMethods();
  }

  @Override public List<SymbolBinding> getSymbols()
  {
    return this.environment.getSymbols();
  }
}
