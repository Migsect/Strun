package net.samongi.frunction.frunction.type.dictionary;

import net.samongi.frunction.binding.DynamicSymbolBinding;
import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.error.syntax.SyntaxError;
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
    catch(SyntaxError | RunTimeError e)
    {
      e.printStackTrace();
    }
    return instance;
  }
  
  /**Creating the type dictionary
   * This will have no container
   * 
   * @throws SyntaxError 
   * @throws RunTimeError 
   */
  private TypeDictionary() throws SyntaxError, RunTimeError
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
   * @throws SyntaxError 
   * @throws RunTimeError 
   */
  public void generateType(String type) throws SyntaxError, RunTimeError
  {
    // The environment of the type frunction will be the type dictionary
    Frunction type_frunction = new DynamicFrunction(this);
    SymbolBinding binding = new DynamicSymbolBinding(type, type_frunction);
    this.addSymbol(binding);
  }
  public Frunction getType(String type) throws SyntaxError, RunTimeError
  {
    if(!this.containsType(type)) this.generateType(type);
    SymbolBinding binding = this.getSymbol(type);
    return binding.get(this);
  }
}
