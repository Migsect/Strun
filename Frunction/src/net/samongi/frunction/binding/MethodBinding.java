package net.samongi.frunction.binding;

import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.types.Expression;

public interface MethodBinding extends Binding
{
  /**
   * Returns a list of the symbols used in the method binding
   * 
   * @return
   */
  public String[] getInputSymbols();

  /**
   * Gets the conditional expression for this method binding This will call the
   * generate method if the expression is not yet created
   * 
   * @return The condition expression
   * @throws TokenException
   */
  public Expression getConditional() throws TokenException;

  /**
   * Gets the expression defined by this method binding
   * 
   * @return
   * @throws TokenException
   */
  public Expression getExpression() throws TokenException;

  public String[] getTypes();

  /**
   * Returns a string representation of the method binding that is easier for
   * humans to read.
   * 
   * @return
   */
  public String toDisplay();
}
