package net.samongi.frunction.binding;

import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Frunction;

/**Method bindings bind a set of inputs to an expression.
 * 
 * @author Alex
 *
 */
public class MethodBinding implements Binding
{
	private final Frunction container;
	private final String[] input_symbols;
	private final String[][] input_types;
	
	private final String source;
	
	private Expression condition = null;
	private Expression expression = null;

	public MethodBinding(Frunction container, String[] input_symbols, String[][] input_types, String cond_source, String source)
	{
		this.container = container;
		this.input_symbols = input_symbols;
		this.input_types = input_types;
		this.source = source;
	}
	
	public void generateCondition()
	{
    if(this.expression != null) return;
	  
	}
	public Expression getConditional()
	{
	  if(this.condition == null) this.generateCondition();
	  return this.condition;
	}
	public void generateExpression()
	{
    if(this.expression != null) return;
	  
	}
	public Expression getExpression()
	{
	  if(this.expression == null) this.generateCondition();
	  return this.expression;
	}
	
	@Override public String getSource(){return this.source;}

	@Override public Frunction getContainer(){return this.container;}

}