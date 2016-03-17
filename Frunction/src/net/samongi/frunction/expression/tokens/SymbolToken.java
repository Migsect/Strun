package net.samongi.frunction.expression.tokens;

/**Represents a symbol in an expression.
 */
public class SymbolToken implements Token
{
	private final String source;
	
	public SymbolToken(String source)
	{
		this.source = source;
	}
	
	@Override public String getSource(){return this.source;}
	
}
