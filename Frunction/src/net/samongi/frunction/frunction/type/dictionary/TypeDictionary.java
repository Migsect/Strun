package net.samongi.frunction.frunction.type.dictionary;

import net.samongi.frunction.binding.DynamicSymbolBinding;
import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.exceptions.parsing.ParsingException;
import net.samongi.frunction.exceptions.runtime.FrunctionNotEvaluatedException;
import net.samongi.frunction.exceptions.runtime.RunTimeException;
import net.samongi.frunction.frunction.DynamicFrunction;

public class TypeDictionary extends DynamicFrunction
{
  private static TypeDictionary instance = null;
  public static TypeDictionary getInstance()
  {
    if(instance == null) try
    {
      instance = new TypeDictionary();
    }
    catch(ParsingException | RunTimeException e)
    {
      e.printStackTrace();
    }
    return instance;
    
  }
  
  private TypeDictionary() throws ParsingException, RunTimeException
  {
    super(null);
  }
  
  public boolean containsType(String type)
  {
    return this.hasSymbol(type);
  }
  
  /**Will generate the type in the library.
   * This will set the type as the symbol
   * The type will not overwrite an already pre-existing instantiation
   * of the type frunction
   * 
   * @param type
   */
  public void generateType(String type)
  {
    SymbolBinding binding = new DynamicSymbolBinding(type, "");
    try
    {
      this.addSymbol(binding);
    }
    catch(FrunctionNotEvaluatedException e)
    {
      e.printStackTrace();
    }
  }
  public void addType(String type)
  {
    
  }
}
