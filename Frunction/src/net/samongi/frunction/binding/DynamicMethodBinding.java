package net.samongi.frunction.binding;

import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Container;

/**Method bindings bind a set of inputs to an expression.
 * 
 * @author Alex
 *
 */
public class DynamicMethodBinding implements MethodBinding
{
	private final Container container;
	
	private final String[] input_symbols;
	private final String[] input_types; // TODO type checking
	
	private final String source;
	private final String condition_source;
	
	private Expression condition = null;
	private Expression expression = null;

	public DynamicMethodBinding(Container container, String[] input_symbols, String[] input_types, Expression condition, Expression expression)
	{
		this.container = container;
		this.input_symbols = input_symbols;
		this.input_types = input_types;
		
		this.source = null;
		this.condition_source = null;
		this.condition = condition;
		this.expression = expression;
	}
	public DynamicMethodBinding(Container container, String[] input_symbols, String[] input_types, String cond_source, String source)
	{
		this.container = container;
		this.input_symbols = input_symbols;
		this.input_types = input_types;
		
		this.source = source;
		this.condition_source = cond_source;
	}
	
	@Override public String[] getInputSymbols(){return this.input_symbols;}
	
	/**Generates the condition for the method binding
	 * 
	 * @throws TokenException
	 */
	public void generateCondition() throws TokenException
	{
    if(this.expression != null) return;
	  this.condition = Expression.parseString(condition_source, container);
	}
	
	@Override public Expression getConditional() throws TokenException
	{
	  if(this.condition == null) this.generateCondition();
	  return this.condition;
	}
	/**Will generate the expression for method.
	 * 
	 * @throws TokenException
	 */
	public void generateExpression() throws TokenException
	{
    if(this.expression != null) return;
	  this.expression = Expression.parseString(source, container);
	  
	}
	@Override public Expression getExpression() throws TokenException
	{
	  if(this.expression == null) this.generateCondition();
	  return this.expression;
	}
	
	@Override public String getSource(){return this.source;}

	@Override public Container getContainer(){return this.container;}
	@Override public String toDisplay()
	{
		String inputs = "( ";
		for(String i : this.input_symbols) inputs += i + " ";
		inputs += ")";
		
		String types = "[";
		for(String t : this.input_types) types += t + " ";
		types += "]";
		return inputs + types;
	}
	@Override public String[] getTypes(){return this.input_types;}

}