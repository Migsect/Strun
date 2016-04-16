package net.samongi.frunction.frunction.type;

import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.error.syntax.SyntaxError;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.type.dictionary.TypeDictionary;

public abstract class TypeDefiner
{
  private final String type;
  
  public TypeDefiner(String type)
  {
    this.type = type;
  }
  
  /**Will execute this type definer
   * 
   * @throws ParsingException
   * @throws RunTimeException
   */
  public void define() throws RunTimeError, SyntaxError
  {
    Frunction type_frunction = TypeDictionary.getInstance().getType(this.type);  
    this.defineType(type_frunction);
  }
  
  protected abstract void defineType(Frunction type_frunction) throws RunTimeError, SyntaxError;
}
