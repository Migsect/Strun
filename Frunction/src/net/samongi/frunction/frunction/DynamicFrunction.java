package net.samongi.frunction.frunction;

import java.util.List;
import java.util.Map;

import net.samongi.frunction.binding.MethodBinding;
import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.types.Expression;

public class DynamicFrunction implements Expression, Frunction
{
	/**A frunction consists of a Map of symbol bindings
	 */
	private Map<String, SymbolBinding> symbol_bindings = null;
	/**A frunction cosists of a Map of String[] to method bindings.
	 * The strings in this array are types and not pure symbols.
	 * Generally an empty type will be used.
	 * 
	 * There can be multiple method bindings to a set of types.
	 */
	private Map<String[], List<MethodBinding>> method_bindings = null;
	
	/**A set will represent the type that a frunction is.
	 */
	private String type = "";
	
	/**A Frunction will have a parent environment in which it was defined in.
	 * This environment is also a frunction
	 */
	private final Container environment;
	
	/**Source is the body of the frunction.
	 */
	private final String source;
	
	/**Constructor of the frunction
	 * Consists of the frunction the frunction is defined within (its environment)
	 * and the source for the frunction.
	 * 
	 * @param environment
	 * @param source
	 */
	public DynamicFrunction(Container environment, String source)
	{
		this.environment = environment;
		this.source = source;
	}
	
	/**Will evaluate the source of this frunction and construct the
	 * bindings for it.
	 */
	@Override public void evaluate()
	{
		if(this.isEvaluated()) return;
		// ELSE TODO Evaluation of source
	}
	
	/**Checks to see if the frunction is evaluated.
	 * 
	 * @return True if the frunction is already evaluated
	 */
	@Override public boolean isEvaluated()
	{
		return false;
	}
	
	/**Gets the method binding for the corresponding types
	 * 
	 * @param types THe types to get a method for
	 * @return A MethodBinding, otherwise null
	 */
	@Override public MethodBinding getMethod(String[] types, DynamicFrunction[] inputs)
	{
	  if(types.length != inputs.length)
	  {
	    // TODO proper exception
	    return null;
	  }
		if(!this.isEvaluated()) this.evaluate();
		List<MethodBinding> methods = this.method_bindings.get(types);
		if(methods.isEmpty()) return null; // We didn't find a method
		
		for(MethodBinding b : methods)
		{
			MethodContainer m_container = new MethodContainer(this);
			String[] input_symbols = b.getInputSymbols();
			if(input_symbols.length != inputs.length)
			{
				// TODO proper exception
				return null;
			}
			// Adding the input symbols to the container
			for(int i = 0; i < input_symbols.length; i++){m_container.addSymbol(input_symbols[i], inputs[i]);}
			DynamicFrunction result = null;
			try{result = b.getConditional().evaluate(m_container);}
			catch (TokenException e){return null;}
		}
		
		
		return null;
	}
	/**Gets the symbol binding for the corresponding symbol
	 * 
	 * @param symbol The symbol to retrieve
	 * @return A SymbolBinding, otherwise null
	 */
	@Override public SymbolBinding getSymbol(String symbol)
	{
		if(!this.isEvaluated()) this.evaluate();
		return this.symbol_bindings.get(symbol);
	}
	/**Sets the type to this Frunction
	 * 
	 * @param type
	 */
	@Override public void setType(String type){this.type = type;}
	/**Gets an array of all the types that this frunction is.
	 * This may return empty if the frunction has no types.
	 * 
	 * @return The type of this frunction.
	 */
	@Override public String getType()
	{
	  return this.type;
	}
	
	/**Returns the environment that this frunction was defined in.
	 * 
	 * @return The frunctional environment this frunction was defined in.
	 */
	@Override public Container getEnvironment(){return this.environment;}

	// While it is technically not an expression, it is needed to be one for accessors
  @Override public DynamicFrunction evaluate(Container environment)
  {
    return this;
  }
}
