package net.samongi.frunction.binding;

import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.error.runtime.SymbolNotFoundError;
import net.samongi.frunction.error.syntax.ExpressionError;
import net.samongi.frunction.error.syntax.SyntaxError;
import net.samongi.frunction.error.syntax.TokenError;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Frunction;

public interface SymbolBinding extends Binding
{
  /** Returns the key of the symbol binding This key will be the string that it is accessed with
   * 
   * @return The key of the symbol binding */
  public String getKey();

  /** Returns the privacy state of the binding. This determines if the key can be accessed from outside of its container
   * 
   * @return True if the symbol is private */
  public boolean isPrivate();

  /** Returns the expression that this symbol binding represents.
   * 
   * @return An expression the symbol binding represents
   * @throws TokenError
   * @throws ExpressionError */
  public Expression getExpression() throws SyntaxError;

  /** Returns a string representation of the binding that is more human readable.
   * 
   * @return */
  public String toDisplay();

  /** This will collapse the symbol to have its expression forced to evaluate to a frunction.
   * @throws RunTimeError 
   * 
   * @throws ExpressionError 
   * @throws SymbolNotFoundError */
  public void collapse() throws SyntaxError, RunTimeError;

  /** Returns a frunction that this symbol may be representing. This will force a collapse as well as an evaluation of
   * symbol binding.
   * 
   * @return
   * @throws RunTimeError 
   * @throws ExpressionError 
   * @throws SymbolNotFoundError */
  public Frunction get() throws SyntaxError, RunTimeError;

  /** Determines if the symbol is countable for certain methods
   * 
   * @return True if it can be counted */
  public boolean isCountable();

  /** Sets the countability state of the symbol
   * 
   * @param is_countable The state to set it to */
  public void setCountable(boolean is_countable);

}
