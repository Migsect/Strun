package net.samongi.frunction.frunction.literal.dictionary;

import java.util.List;

import net.samongi.frunction.binding.MethodBinding;
import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.literal.BooleanFrunction;
import net.samongi.frunction.frunction.literal.StringFrunction;

/**Represents a container that stores and creates all instances of literals
 * Literals are native frunctions that are generally defined in their simplistic literla
 * form in the source.
 * 
 * Some types are:
 *  - boolean
 *  - integer
 *  - real
 *  - string
 * 
 * This class shall be a singleton and furthermore shall be called for all literal parsing.
 */
public class LiteralDictionary implements Container
{
  private static LiteralDictionary instance = null;
  /**Will create a new dictionary if one doesn't exist, otherwise it 
   * return the current singleton that does exists.
   * 
   * @return
   */
  public static LiteralDictionary getInstance()
  {
    if(instance == null) instance = new LiteralDictionary();
    return instance;
  }

  @Override public MethodBinding getMethod(String[] types, Frunction[] inputs)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override public void addMethod(MethodBinding binding)
  {
    // TODO Auto-generated method stub
    
  }

  @Override public List<MethodBinding> getMethods()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override public SymbolBinding getSymbol(String symbol)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override public void addSymbol(SymbolBinding binding)
  {
    // TODO Auto-generated method stub
    
  }

  @Override public List<SymbolBinding> getSymbols()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override public Container getEnvironment()
  {
    // TODO Auto-generated method stub
    return null;
  }
  /**Will return true if the symbol can be depicted as a literal.
   * 
   * @param symbol The symbol to check for.
   * @return True if the symbol represents a literal
   */
  public boolean isLiteral(String symbol)
  {
    if(symbol.startsWith(StringFrunction.STRING_CAPSULE) && symbol.endsWith(StringFrunction.STRING_CAPSULE)) return true;
    if(symbol.equals(BooleanFrunction.TRUE_LITERAL) || symbol.equals(BooleanFrunction.FALSE_LITERAL)) return true;
    
    // testing for double
    Double double_test = null;
    try{double_test = Double.parseDouble(symbol);}catch(NumberFormatException e){}
    if(double_test != null) return true;
    
    // testing for integer
    Integer integer_test = null;
    try{integer_test = Integer.parseInt(symbol);}catch(NumberFormatException e){}
    if(integer_test != null) return true;
    
    return false;
  }

}
