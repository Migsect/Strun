package net.samongi.frunction.binding;

import java.util.List;

import net.samongi.frunction.expression.Expression;
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
	private final String source;
	
	private Expression expression = null;

	public MethodBinding(Frunction container, String[] input_symbols, String source)
	{
		this.container = container;
		this.input_symbols = input_symbols;
		this.source = source;
	}

	@Override public String getSource(){return this.source;}

	@Override public Frunction getContainer(){return this.container;}

}