package net.samongi.frunction.frunction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.samongi.frunction.binding.MethodBinding;
import net.samongi.frunction.binding.SymbolBinding;

public class Frunction
{
	/**A frunction consists of a Map of symbol bindings
	 */
	private Map<String, SymbolBinding> symbol_bindings = null;
	/**A frunction cosists of a Map of String[] to method bindings.
	 * The strings in this array are types and not pure symbols.
	 * Generally an empty type will be used.
	 */
	private Map<String[], MethodBinding> method_bindings = null;
	
	/**A set will represent the types that a frunction may have representing it.
	 */
	private Set<String> types = new HashSet<>();
	
	/**A Frunction will have a parent environment in which it was defined in.
	 * This environment is also a frunction
	 */
	private final Frunction environment;
	
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
	public Frunction(Frunction environment, String source)
	{
		this.environment = environment;
		this.source = source;
	}
	
	/**Will evaluate the source of this frunction and construct the
	 * bindings for it.
	 */
	public void evaluate()
	{
		if(this.isEvaluated()) return;
		// ELSE TODO Evaluation of source
	}
	
	/**Checks to see if the frunction is evaluated.
	 * 
	 * @return True if the frunction is already evaluated
	 */
	public boolean isEvaluated()
	{
		return false;
	}
	
	/**Gets the method binding for the corresponding types
	 * 
	 * @param types THe types to get a method for
	 * @return A MethodBinding, otherwise null
	 */
	public MethodBinding getMethod(String[] types)
	{
		if(!this.isEvaluated()) this.evaluate();
		return this.method_bindings.get(types);
	}
	/**Gets the symbol binding for the corresponding symbol
	 * 
	 * @param symbol The symbol to retrieve
	 * @return A SymbolBinding, otherwise null
	 */
	public SymbolBinding getSymbol(String symbol)
	{
		if(!this.isEvaluated()) this.evaluate();
		return this.symbol_bindings.get(symbol);
	}
}
