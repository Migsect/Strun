package net.samongi.frunction.frunction.literal.dictionary;

import java.util.HashMap;
import java.util.List;

import net.samongi.frunction.binding.DynamicSymbolBinding;
import net.samongi.frunction.binding.MethodBinding;
import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.error.syntax.SyntaxError;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.literal.BooleanFrunction;
import net.samongi.frunction.frunction.literal.NativeFrunction;
import net.samongi.frunction.frunction.literal.StringFrunction;

/** Represents a container that stores and creates all instances of literals Literals are native frunctions that are
 * generally defined in their simplistic literla form in the source.
 * 
 * Some types are: - boolean - integer - real - string
 * 
 * This class shall be a singleton and furthermore shall be called for all literal parsing. */
public class LiteralDictionary implements Container
{
  public static final boolean DEBUG = false;

  private static LiteralDictionary instance = null;

  /** Will create a new dictionary if one doesn't exist, otherwise it return the current singleton that does exists.
   * 
   * @return */
  public static LiteralDictionary getInstance()
  {
    if(instance == null) instance = new LiteralDictionary();
    return instance;
  }

  // This will store all the literals for quick lookup
  // This is also what makes all literals the same objects.
  private HashMap<String, SymbolBinding> stored_literals = new HashMap<>();

  @Override public MethodBinding getMethod(String[] types, Frunction[] inputs)
  {
    return null;
  }

  @Override public void addMethod(MethodBinding binding)
  {}

  @Override public List<MethodBinding> getMethods()
  {
    return null;
  }

  @Override public SymbolBinding getSymbol(String symbol)
  {
    // Returning it if it already exists
    if(this.stored_literals.containsKey(symbol)) return this.stored_literals.get(symbol);

    Frunction f = null;
    try
    {
      f = NativeFrunction.parseLiteral(symbol, this);
    }
    catch(SyntaxError | RunTimeError e)
    {
      System.out.println("Failed on: '" + symbol + "'");
      e.printStackTrace();
    }
    
    
    if(DEBUG) System.out.println("  Created literal binding for '" + symbol + "' with type '" + f.getType() + "'");

    // Creating the binding and adding it to the map
    SymbolBinding b = new DynamicSymbolBinding(symbol, f);
    this.stored_literals.put(symbol, b);

    // Returning the new binding.
    return b;
  }

  @Override public void addSymbol(SymbolBinding binding)
  {}

  @Override public List<SymbolBinding> getSymbols()
  {
    return null;
  }

  @Override public Container getEnvironment()
  {
    return null;
  }

  /** Will return true if the symbol can be depicted as a literal.
   * 
   * @param symbol The symbol to check for.
   * @return True if the symbol represents a literal */
  public boolean isLiteral(String symbol)
  {
    symbol = symbol.trim();
    if(symbol.startsWith(StringFrunction.STRING_CAPSULE) && symbol.endsWith(StringFrunction.STRING_CAPSULE) && symbol.length() > 1) return true;
    if(symbol.equals(BooleanFrunction.TRUE_LITERAL) || symbol.equals(BooleanFrunction.FALSE_LITERAL)) return true;

    // testing for double
    Double double_test = null;
    try
    {
      double_test = Double.parseDouble(symbol);
    }
    catch(NumberFormatException e)
    {}
    if(double_test != null) return true;

    // testing for integer
    Integer integer_test = null;
    try
    {
      integer_test = Integer.parseInt(symbol);
    }
    catch(NumberFormatException e)
    {}
    if(integer_test != null) return true;

    return false;
  }

}
