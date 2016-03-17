package net.samongi.frunction.expression.tokens;

public interface Token
{
	/**Returns the source of this token
	 * The source will be a string that the token is generated from.
	 * This will not include identifiers for the token such as parens for
	 * input tokens.
	 * 
	 * @return
	 */
	public String getSource();
}
