package net.samongi.frunction.binding;

import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.DynamicFrunction;

/**Method bindings bind a set of inputs to an expression.
 * 
 * @author Alex
 *
 */
public class MethodBinding implements Binding
{
	private final DynamicFrunction container;
	
	private final String[] input_symbols;
	private final String[] input_types;
	
	private final String source;
	private final String condition_source;
	
	private Expression condition = null;
	private Expression expression = null;

	public MethodBinding(DynamicFrunction container, String[] input_symbols, String[] input_types, String cond_source, String source)
	{
		this.container = container;
		this.input_symbols = input_symbols;
		this.input_types = input_types;
		this.source = source;
		this.condition_source = cond_source;
	}
	
	public String[] getInputSymbols(){return this.input_symbols;}
	
	/**Generates the condition for the method binding
	 * 
	 * @throws TokenException
	 */
	public void generateCondition() throws TokenException
	{
    if(this.expression != null) return;
	  this.condition = Expression.parseString(condition_source, container);
	}
	/**Gets the conditional expression for this method binding
	 * This will call the generate method if the expression is not yet created
	 * 
	 * @return The condition expression
	 * @throws TokenException
	 */
	public Expression getConditional() throws TokenException
	{
	  if(this.condition == null) this.generateCondition();
	  return this.condition;
	}
	public void generateExpression() throws TokenException
	{
    if(this.expression != null) return;
	  this.expression = Expression.parseString(source, container);
	  
	}
	public Expression getExpression() throws TokenException
	{
	  if(this.expression == null) this.generateCondition();
	  return this.expression;
	}
	
	public void evaluate()
	{
		
	}
	
	@Override public String getSource(){return this.source;}

	@Override public DynamicFrunction getContainer(){return this.container;}

}