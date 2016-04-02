package net.samongi.frunction.binding;

import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Frunction;

public interface SymbolBinding extends Binding
{
	/**Returns the key of the symbol binding
	 * This key will be the string that it is accessed with
	 * 
	 * @return The key of the symbol binding
	 */
	public String getKey();
	/**Returns the privacy state of the binding.
	 * This determines if the key can be accessed from outside of its container
	 * 
	 * @return True if the symbol is private
	 */
	public boolean isPrivate();
	
	/**Returns the expression that this symbol binding represents.
	 * 
	 * @return An expression the symbol binding represents
	 * @throws TokenException
	 */
	public Expression getExpression() throws TokenException;
	
	/**Returns a string representation of the binding that is more human readable.
	 * 
	 * @return
	 */
	public String toDisplay();
	
	/**This will collapse the symbol to have its expression forced to 
	 * evaluate to a frunction.
	 */
	public void collapse() throws TokenException;
	
	/**Returns a frunction that this symbol may be representing.
	 * This will force a collapse as well as an evaluation of symbol binding.
	 * 
	 * @return
	 */
	public Frunction get() throws TokenException;
	
	/**Determines if the symbol is countable for certain methods
	 * 
	 * @return True if it can be counted
	 */
	public boolean isCountable();
	
	/**Sets the countability state of the symbol
	 * 
	 * @param is_countable The state to set it to
	 */
	public void setCountable(boolean is_countable);
	
}
