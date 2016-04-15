package net.samongi.frunction.binding;

import net.samongi.frunction.error.syntax.ExpressionError;
import net.samongi.frunction.error.syntax.SyntaxError;
import net.samongi.frunction.frunction.Container;

public interface Binding
{
  public static final String BINDING_SEPERATOR = ";";

  /** Returns the source of the binding
   * 
   * @return */
  public String getSource();

  /** Returns the container of the binding
   * 
   * @return */
  public Container getContainer();

  /** Will force an evaluation of the symbol's expressions
   * 
   * @throws ExpressionError 
   * @throws SyntaxError */
  public void evaluate() throws SyntaxError;
}
