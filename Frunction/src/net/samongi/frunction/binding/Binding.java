package net.samongi.frunction.binding;

import net.samongi.frunction.exceptions.parsing.ExpressionException;
import net.samongi.frunction.exceptions.parsing.ParsingException;
import net.samongi.frunction.frunction.Container;

public interface Binding
{
  public static final String BINDING_SEPERATOR = ";";

  /** Returns the source of the binding
   * 
   * @return */
  public String getSource();

  /** Will force an evaluation of the symbol's expressions
   * 
   * @throws ExpressionException 
   * @throws ParsingException */
  public void evaluate(Container environment) throws ParsingException;
}
