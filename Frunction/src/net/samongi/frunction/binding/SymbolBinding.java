package net.samongi.frunction.binding;

import net.samongi.frunction.expression.Expression;
import net.samongi.frunction.expression.ExpressionCreator;

public class SymbolBinding
{
	private static final String DEF_KEY = "_"; 
	private static final String BINDING_PUBLIC = ":";
	
	public static SymbolBinding parseBinding(String text_section)
	{
		// Splitting the section based on the first bound binding operator
		String[] split_section = text_section.split(BINDING_PUBLIC, 1);
		String key = null;
		String source = null;
		if(split_section.length < 2) // This means that the second expression isn't being binding to an actual key
		{
			key = DEF_KEY;
			source = split_section[0]; // The text is all there is.
		}
		else
		{
			key = split_section[0]; // Getting the first element of the split_section
			source = split_section[1]; // The text is second in this case
		}
		
		if(key == null) return null;
		if(source == null) return null;
		
		return new SymbolBinding(key, source);
	}
	
	private final String key;
	private final String source;
	private final boolean is_private = false; // TODO implement binding privacy
	private final boolean is_global = false; // TODO implement binding globality
	
	private Expression expression = null;
	
	public SymbolBinding(String key, String source)
	{
		this.key = key;
		this.source = source;
	}
	
	public String getKey(){return this.key;}
	public String getSource(){return this.source;}
	public boolean isPrivate(){return this.is_private;}
	public boolean isGlobal(){return this.is_global;}
	
	/**Returns the expression this binding relates to
	 * This will force the binding to update it's expression given
	 * it hasn't created one yet.
	 * 
	 * @return An expression object
	 */
	public Expression getExpression()
	{
		if(this.expression == null) this.expression = ExpressionCreator.instance().parse(this.source);
		return this.expression;
	}
	/**Forces the binding to generate it's related expression.
	 * If it's already created, then this will not generate it.
	 */
	public void generateExpression()
	{
		if(this.expression == null) this.expression = ExpressionCreator.instance().parse(this.source);
	}
	
}
