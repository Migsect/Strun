package net.samongi.frunction.binding;

import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.DynamicFrunction;
import net.samongi.frunction.frunction.Frunction;

public class DynamicSymbolBinding implements SymbolBinding
{
	private static final String DEF_KEY = "_"; 
	private static final String BINDING_PUBLIC = ":";
	
	public static DynamicSymbolBinding parseBinding(String text_section, Container environment)
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
		
		return new DynamicSymbolBinding(key, source, environment);
	}
	
	private final String key;
	private final String source;
	private final boolean is_private = false; // TODO implement binding privacy
	
	private Expression expression = null;
	
	// The container of this
	private final Container environment;
	
	public DynamicSymbolBinding(String key, String source, Container environment)
	{
		this.key = key;
		this.source = source;
		this.environment = environment;
	}
	public DynamicSymbolBinding(String key, Frunction evaluated, Container environment)
	{
	  this.key = key;
	  this.expression = evaluated;
	  this.environment = environment;
	  this.source = null;
	}
	
	@Override public String getKey(){return this.key;}
	@Override public String getSource(){return this.source;}
	@Override public boolean isPrivate(){return this.is_private;}
	
	/**Returns the expression this binding relates to
	 * This will force the binding to update it's expression given
	 * it hasn't created one yet.
	 * 
	 * @return An expression object
	 * @throws TokenException 
	 */
	@Override public Expression getExpression() throws TokenException
	{
		if(this.expression == null) this.generateExpression();
		return this.expression;
	}
	/**Forces the binding to generate it's related expression.
	 * If it's already created, then this will not generate it.
	 * @throws TokenException 
	 */
	public void generateExpression() throws TokenException
	{
		if(this.expression == null) this.expression = Expression.parseString(this.source, this.environment);
	}
	
	@Override public Container getContainer(){return this.environment;}
	
}
