package net.samongi.frunction.frunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.samongi.frunction.binding.Binding;
import net.samongi.frunction.binding.DynamicMethodBinding;
import net.samongi.frunction.binding.DynamicSymbolBinding;
import net.samongi.frunction.binding.MethodBinding;
import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.tokens.Token;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.exceptions.SymbolNotFoundException;
import net.samongi.frunction.frunction.literal.BooleanFrunction;
import net.samongi.frunction.frunction.literal.dictionary.LiteralDictionary;
import net.samongi.frunction.parse.ParseUtil;

public class DynamicFrunction implements Expression, Frunction
{
  private static final String SELF_SYMBOL = "@";
  private static final String CONTAINER_ENV_SYMBOL = "^";
  
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
	
	@Override public void evaluate()
	{
		if(this.isEvaluated()) return;
		
		// Creating the hashmaps
    this.symbol_bindings = new HashMap<>();
		this.method_bindings = new HashMap<>();
		
		int i = 0;
		
		while(i < source.length())
		{
		  // Getting the first section.
		  String section = ParseUtil.getSection(source, i, Binding.BINDING_SEPERATOR, 
		      Token.getScopeOpenIdentifiers(), Token.getScopeCloseIdentifiers(), Token.getScopeeToggleIdentifiers());
		  // Increating the index variable for the next section grab
		  i += section.length();
		  // Removing the binding seperator from the end.
		  if(section.endsWith(Binding.BINDING_SEPERATOR)) section = section.substring(0, section.length() - Binding.BINDING_SEPERATOR.length());
		  // Now we have a clean binding defintion.
		  
		  MethodBinding met_binding = DynamicMethodBinding.parseBinding(section, this);
      if(met_binding != null)
      {
        System.out.println("Found met_b in: '" + section.trim() + "'");
        
        this.addMethod(met_binding);
        continue;
      }
		  SymbolBinding sym_binding = DynamicSymbolBinding.parseBinding(section, this);
		  if(sym_binding != null)
		  {
		    System.out.println("Found sym_b '" + sym_binding.getKey() + "' in: '" + section.trim() + "'");
		    
		    this.addSymbol(sym_binding);
		    continue;
		  }
		  
		  
		  // Here is here we throw an exception or a warning that a binding couldn't be found in a section
		  // TODO error messages
		  System.out.println("Didn't find binding in: '" + section + "'");
		}
	}
	
	@Override public boolean isEvaluated(){return symbol_bindings != null && method_bindings != null;}
	
	@Override public MethodBinding getMethod(String[] types, Frunction[] inputs)
	{
	  if(types.length != inputs.length)
	  {
	    // TODO proper exception
	    return null;
	  }
		if(!this.isEvaluated()) this.evaluate();
		List<MethodBinding> methods = this.method_bindings.get(types);
		if(methods == null || methods.isEmpty()) return null; // We didn't find a method
		
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
	
	@Override public void addMethod(MethodBinding binding)
	{
		String[] types = binding.getTypes();
		System.out.println("Binding with types: " + ParseUtil.concatStringArray(types));
		// Generating the list if it doesn't exist
		if(!this.method_bindings.containsKey(types)) this.method_bindings.put(types, new ArrayList<MethodBinding>());
		// Retrieving the list
		List<MethodBinding> binding_list = this.method_bindings.get(types);
		// Adding the binding to the list
		binding_list.add(binding);
	}
	
	@Override public SymbolBinding getSymbol(String symbol) throws SymbolNotFoundException
	{
		if(!this.isEvaluated()) this.evaluate();
		
		// First step is to consult if the symbol is a literal
		if(LiteralDictionary.getInstance().isLiteral(symbol)) return LiteralDictionary.getInstance().getSymbol(symbol);
		
		if(symbol.equals(SELF_SYMBOL)) // time to create the self binding that will need to be accessed.
		{
		  SymbolBinding self_bind = new DynamicSymbolBinding(SELF_SYMBOL, this, this);
		  self_bind.setCountable(false); // it shouldn't be countable
		  this.addSymbol(self_bind); // creating the new self-binded symbol;
		}
		
		SymbolBinding binding = null;
		// First case is to check if it will force an environment pop-up
		if(symbol.startsWith(CONTAINER_ENV_SYMBOL)) 
		{
      if(environment == null) throw new SymbolNotFoundException(symbol);
		  binding = this.environment.getSymbol(symbol.replaceFirst(CONTAINER_ENV_SYMBOL, ""));
		}
		else 
		{
		  binding = this.symbol_bindings.get(symbol);
		  if(binding == null) 
		  {
	      if(environment == null) throw new SymbolNotFoundException(symbol);
		    binding = this.environment.getSymbol(symbol);
		  }
		}
		
		
		
		if(binding == null) throw new SymbolNotFoundException(symbol);
		
		// Returning the binding, it may be null
		return binding;
	}
	
	@Override public void addSymbol(SymbolBinding binding)
	{
		// Simply adding the symbol
		// This will override any existing symbols in that place, but it is expected
		this.symbol_bindings.put(binding.getKey(), binding);
	}
	
	@Override public void setType(String type){this.type = type;}
	
	@Override public String getType(){return this.type;}
	
	@Override public Container getEnvironment(){return this.environment;}

  @Override public Frunction evaluate(Container environment){return this;}
  
  @Override public String getSource(){return this.source;}

	@Override public List<MethodBinding> getMethods()
	{
		TreeMap<String[], List<MethodBinding>> sorted_bindings = new TreeMap<>();
		sorted_bindings.putAll(this.method_bindings);

		List<List<MethodBinding>> sorted_list = new ArrayList<>();
		sorted_list.addAll(sorted_bindings.values());
		
		List<MethodBinding> squished_list = new ArrayList<>();
		for(List<MethodBinding> l : sorted_list) squished_list.addAll(l);
		
		return squished_list;
	}

	@Override public List<SymbolBinding> getSymbols()
	{

		TreeMap<String, SymbolBinding> sorted_bindings = new TreeMap<>();
		sorted_bindings.putAll(this.symbol_bindings);
		
		List<SymbolBinding> sorted_list = new ArrayList<>();
		sorted_list.addAll(sorted_bindings.values());
		return sorted_list;
	}
}
