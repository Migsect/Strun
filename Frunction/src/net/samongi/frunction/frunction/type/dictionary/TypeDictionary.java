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
  
  public void generateType(String type)
  {
    SymbolBinding binding = new DynamicSymbolBinding(type, "");
    try
    {
      this.addSymbol(binding);
    }
    catch(FrunctionNotEvaluatedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  public void addType(String type)
  {
    
  }
}
