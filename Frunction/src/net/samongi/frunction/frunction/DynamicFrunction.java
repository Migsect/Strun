package net.samongi.frunction.frunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.samongi.frunction.binding.MethodBinding;
import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.literal.BooleanFrunction;

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
	@Override public MethodBinding getMethod(String[] types, Frunction[] inputs)
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
			// Generating the method container to be used with the inputs
			MethodContainer m_container = new MethodContainer(this);
			String[] input_symbols = b.getInputSymbols();
			// If for some reason the inputs are wrong for both this should be a hard
			//  exception for something went seriously wrong.
			if(input_symbols.length != inputs.length)
			{
				// TODO proper exception
				return null;
			}
			// Adding the input symbols to the container
			for(int i = 0; i < input_symbols.length; i++){m_container.addSymbol(input_symbols[i], inputs[i]);}
			
			// Getting the boolean result.
			Frunction result = null;
			// Evaluated the conditional expression using the inputs
			try{result = b.getConditional().evaluate(m_container);}
			catch (TokenException e){return null;}
			
			// We are now going to test and see if the method is a boolean type
			//   If it is not, we are going to assume the expression evaluates to true
			//   This is done such that only false-hood will prevent a method from
			//   working.
			if(result.getType().equals("Boolean") && result instanceof BooleanFrunction)
			{
		    // Casting to a boolean frunction
				BooleanFrunction b_result = (BooleanFrunction) result;
				boolean state = b_result.getNative();
				if(!state) continue; // if it is false, then we will skip this method
			}
			// Returning the found binding as a result.
			return b;
		}
		return null; // We didn't find the method for the types
	}
	
	@Override public void addMethod(String[] types, MethodBinding binding)
	{
		// Generating the list if it doesn't exist
		if(!this.method_bindings.containsKey(types)) this.method_bindings.put(types, new ArrayList<MethodBinding>());
		// Retrieving the list
		List<MethodBinding> binding_list = this.method_bindings.get(types);
		// Adding the binding to the list
		binding_list.add(binding);
	}
	
	@Override public SymbolBinding getSymbol(String symbol)
	{
		if(!this.isEvaluated()) this.evaluate();
		return this.symbol_bindings.get(symbol);
	}
	
	@Override public void addSymbol(String symbol, SymbolBinding binding)
	{
		// Simply adding the symbol
		// This will override any existing symbols in that place, but it is expected
		this.symbol_bindings.put(symbol, binding);
	}
	
	@Override public void setType(String type){this.type = type;}
	
	@Override public String getType(){return this.type;}
	
	@Override public Container getEnvironment(){return this.environment;}

  @Override public Frunction evaluate(Container environment){return this;}
  
  @Override public String getSource(){return this.source;}
}
