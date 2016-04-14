package net.samongi.frunction.frunction.type.dictionary;

import net.samongi.frunction.binding.DynamicSymbolBinding;
import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.exceptions.parsing.ParsingException;
import net.samongi.frunction.exceptions.runtime.RunTimeException;
import net.samongi.frunction.frunction.DynamicFrunction;
import net.samongi.frunction.frunction.Frunction;

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
  
  /**Creating the type dictionary
   * This will have no container
   * 
   * @throws ParsingException
   * @throws RunTimeException
   */
  private TypeDictionary() throws ParsingException, RunTimeException
  {
    super(null);
  }
  
  public boolean containsType(String type)
  {
    return this.hasLocalSymbol(type);
  }
  
  /**Will generate the type in the library.
   * This will set the type as the symbol
   * The type will not overwrite an already pre-existing instantiation
   * of the type frunction
   * 
   * @param type
   * @throws RunTimeException 
   * @throws ParsingException 
   */
  public void generateType(String type) throws ParsingException, RunTimeException
  {
    Frunction type_frunction = new DynamicFrunction(this);
    SymbolBinding binding = new DynamicSymbolBinding(type, type_frunction);
    this.addSymbol(binding);
  }
  public Frunction getType(String type) throws ParsingException, RunTimeException
  {
    if(!this.containsType(type)) this.generateType(type);
    SymbolBinding binding = this.getSymbol(type);
    //System.out.println("Fetched type : '" + type + "'");
    return binding.get(this);
  }
}
