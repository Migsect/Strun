package net.samongi.frunction.binding;

import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.types.Expression;

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
	
}
