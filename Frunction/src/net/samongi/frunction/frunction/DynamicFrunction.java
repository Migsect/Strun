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
import net.samongi.frunction.frunction.exceptions.FrunctionNotEvaluatedException;
import net.samongi.frunction.frunction.exceptions.SymbolNotFoundException;
import net.samongi.frunction.frunction.literal.BooleanFrunction;
import net.samongi.frunction.frunction.literal.dictionary.LiteralDictionary;
import net.samongi.frunction.parse.ParseUtil;

public class DynamicFrunction implements Frunction
{
  public static final boolean DEBUG = false;

  private static final String SELF_SYMBOL = "@";
  private static final String CONTAINER_ENV_SYMBOL = "^";

  /**
   * A frunction consists of a Map of symbol bindings
   */
  private Map<String, SymbolBinding> symbol_bindings = null;
  /**
   * A frunction cosists of a Map of String[] to method bindings. The strings in
   * this array are types and not pure symbols. Generally an empty type will be
   * used.
   * 
   * There can be multiple method bindings to a set of types.
   */
  private Map<Integer, List<MethodBinding>> method_bindings = null;

  /**
   * A set will represent the type that a frunction is.
   */
  private String type = "";

  /**
   * A Frunction will have a parent environment in which it was defined in. This
   * environment is also a frunction
   */
  private final Container environment;

  /**
   * Source is the body of the frunction.
   */
  private final String source;

  /**
   * Constructor of the frunction Consists of the frunction the frunction is
   * defined within (its environment) and the source for the frunction.
   * 
   * @param environment
   * @param source
   */
  public DynamicFrunction(Container environment, String source)
  {
  	if(environment == null) throw new NullPointerException("'environment' was null");
  	if(source == null) throw new NullPointerException("'source' was null");
  	
    this.environment = environment;
    this.source = source;
  }

  /**
   * Constructor for when there is no source. Generally used by native frunction
   * definitions. This will automatically call evaluate to ensure that the
   * hashmaps are made
   * 
   * @param environment
   */
  public DynamicFrunction(Container environment)
  {
    this.environment = environment;
    this.source = null;
    this.evaluate();
  }

  @Override public void evaluate()
  {
    if(this.isEvaluated()) return;

    // Creating the hashmaps
    this.symbol_bindings = new HashMap<String, SymbolBinding>();
    this.method_bindings = new HashMap<Integer, List<MethodBinding>>();

    if(source == null) return;
    if(source.length() <= 0) return;

    int i = 0;

    while(i < source.length())
    {
      // Getting the first section.
      String section = ParseUtil.getSection(source, i,
          Binding.BINDING_SEPERATOR, Token.getScopeOpenIdentifiers(),
          Token.getScopeCloseIdentifiers(), Token.getScopeeToggleIdentifiers());
      // Increating the index variable for the next section grab
      i += section.length();
      // Removing the binding seperator from the end.
      if(section.endsWith(Binding.BINDING_SEPERATOR))
        section = section.substring(0, section.length()
            - Binding.BINDING_SEPERATOR.length());
      // Now we have a clean binding defintion.

      MethodBinding met_binding = DynamicMethodBinding.parseBinding(section,this);
      if(met_binding != null)
      {
        // System.out.println("Found met_b in: '" + section.trim() + "'");

        try
        {
          this.addMethod(met_binding);
        }
        catch(FrunctionNotEvaluatedException e)
        {
          e.printStackTrace();
        }
        continue;
      }
      SymbolBinding sym_binding = DynamicSymbolBinding.parseBinding(section,
          this);
      if(sym_binding != null)
      {
        // System.out.println("Found sym_b '" + sym_binding.getKey() + "' in: '"
        // + section.trim() + "'");

        try
        {
          this.addSymbol(sym_binding);
        }
        catch(FrunctionNotEvaluatedException e)
        {
          e.printStackTrace();
        }
        continue;
      }

      // Here is here we throw an exception or a warning that a binding couldn't
      // be found in a section
      // TODO error messages
      System.out.println("Didn't find binding in: '" + section + "'");
    }
  }

  @Override public boolean isEvaluated()
  {
    if(this.symbol_bindings == null) return false;
    if(this.method_bindings == null) return false;
    return true;
  }

  @Override public MethodBinding getMethod(String[] types, Frunction[] inputs)
  {
    if(DEBUG)
      System.out.println("  Fetching method with types:"
          + ParseUtil.concatStringArray(types));
    if(types.length != inputs.length)
    {
      if(DEBUG)
        System.out
            .println("  Issue in getMethod: types and input legnths missmatch");
      if(DEBUG)
        System.out.println("    with ypes:"
            + ParseUtil.concatStringArray(types));
      // TODO proper exception
      return null;
    }
    if(!this.isEvaluated()) this.evaluate(); // evaluating the frunction if it
                                             // hasn't yet been.

    List<MethodBinding> methods = this.method_bindings.get(types.length);
    if(methods == null || methods.isEmpty())
    {
      if(DEBUG)
        System.out
            .println("  Issue in getMethod: Returned Binding list returned empty or null");
      if(DEBUG)
        System.out.println("    with types:"
            + ParseUtil.concatStringArray(types));
      return null;
    }

    MethodBinding b = this.searchMethodsTyped(methods, types, inputs);
    if(b != null) return b;

    b = this.searchMethodsUntyped(methods, types, inputs);
    if(b != null) return b;

    if(DEBUG)
      System.out.println("  Issue in getMethod: Couldn't find the method.");
    if(DEBUG)
      System.out
          .println("    with types:" + ParseUtil.concatStringArray(types));
    return null; // We didn't find the method for the types
  }

  private MethodBinding searchMethodsUntyped(List<MethodBinding> bindings, String[] types, Frunction[] inputs)
  {
  	if(bindings == null) throw new NullPointerException("'bindings' was null");
  	if(types == null) throw new NullPointerException("'types' was null");
  	if(inputs == null) throw new NullPointerException("'inputs' was null");
  	
    for(MethodBinding b : bindings)
    {
      // Generating the method container to be used with the inputs
      MethodContainer m_container = new MethodContainer(this);
      String[] input_symbols = b.getInputSymbols();
      // If for some reason the inputs are wrong for both this should be a hard
      // exception for something went seriously wrong.
      if(input_symbols.length != inputs.length)
      {
        if(DEBUG)
          System.out
              .println("  Issue in getMethod: Input symbols length did not match inputs length");
        if(DEBUG)
          System.out.println("    with types:"
              + ParseUtil.concatStringArray(types));
        return null;
      }
      // Adding the input symbols to the container
      for(int i = 0; i < input_symbols.length; i++)
      {
        m_container.addSymbol(input_symbols[i], inputs[i]);
      }

      // Getting the boolean result.
      Frunction result = null;
      // Evaluated the conditional expression using the inputs
      try
      {
      	if(b.getConditional() == null) System.out.println("Method Search: Found binding conditional to be null");
        result = b.getConditional().evaluate(m_container);
      }
      catch(TokenException e)
      {
        e.printError();
        return null;
      }

      // We are now going to test and see if the method is a boolean type
      // If it is not, we are going to assume the expression evaluates to true
      // This is done such that only false-hood will prevent a method from
      // working.
      if(result.getType().equals("Boolean")
          && result instanceof BooleanFrunction)
      {
        // Casting to a boolean frunction
        BooleanFrunction b_result = (BooleanFrunction) result;
        boolean state = b_result.getNative();
        if(!state) continue; // if it is false, then we will skip this method
      }
      // Returning the found binding as a result.
      if(DEBUG) System.out.println("  Finished Fetching method.");
      return b;
    }
    return null;
  }

  private MethodBinding searchMethodsTyped(List<MethodBinding> bindings, String[] types, Frunction[] inputs)
  {
  	if(bindings == null) throw new NullPointerException("'bindings' was null");
  	if(types == null) throw new NullPointerException("'types' was null");
  	if(inputs == null) throw new NullPointerException("'inputs' was null");
  	
    for(MethodBinding b : bindings)
    {
      String[] input_types = b.getTypes();
      boolean do_skip = false;
      for(int i = 0; i < input_types.length; i++)
      {
        if(!types[i].equals(input_types[i]))
        {
          do_skip = true;
          break;
        }
      }
      if(do_skip) continue;

      // Generating the method container to be used with the inputs
      MethodContainer m_container = new MethodContainer(this);
      String[] input_symbols = b.getInputSymbols();
      // If for some reason the inputs are wrong for both this should be a hard
      // exception for something went seriously wrong.
      if(input_symbols.length != inputs.length)
      {
        if(DEBUG)
          System.out
              .println("  Issue in getMethod: Input symbols length did not match inputs length");
        if(DEBUG)
          System.out.println("    with types:"
              + ParseUtil.concatStringArray(types));
        return null;
      }
      // Adding the input symbols to the container
      for(int i = 0; i < input_symbols.length; i++)
      {
        m_container.addSymbol(input_symbols[i], inputs[i]);
      }

      // Getting the boolean result.
      Frunction result = null;
      // Evaluated the conditional expression using the inputs
      try
      {
        result = b.getConditional().evaluate(m_container);
      }
      catch(TokenException e)
      {
        e.printError();
        return null;
      }

      // We are now going to test and see if the method is a boolean type
      // If it is not, we are going to assume the expression evaluates to true
      // This is done such that only false-hood will prevent a method from
      // working.
      if(result.getType().equals("Boolean")
          && result instanceof BooleanFrunction)
      {
        // Casting to a boolean frunction
        BooleanFrunction b_result = (BooleanFrunction) result;
        boolean state = b_result.getNative();
        if(!state) continue; // if it is false, then we will skip this method
      }
      // Returning the found binding as a result.
      if(DEBUG) System.out.println("  Finished Fetching method.");
      return b;
    }
    return null;
  }

  @Override public void addMethod(MethodBinding binding) throws FrunctionNotEvaluatedException
  {
    if(!this.isEvaluated()) throw new FrunctionNotEvaluatedException();

    String[] types = binding.getTypes();
    if(DEBUG)
      System.out.println("  Adding with types:"
          + ParseUtil.concatStringArray(types));
    // System.out.println("Binding with types: " +
    // ParseUtil.concatStringArray(types));
    // Generating the list if it doesn't exist
    if(!this.method_bindings.containsKey(types.length))
      this.method_bindings.put(types.length, new ArrayList<MethodBinding>());
    // Retrieving the list
    List<MethodBinding> binding_list = this.method_bindings.get(types.length);
    // Adding the binding to the list
    binding_list.add(binding);
    if(DEBUG) System.out.println("    List size: " + binding_list.size());
  }

  @Override public SymbolBinding getSymbol(String symbol)
      throws SymbolNotFoundException
  {
    symbol = symbol.trim();
    if(!this.isEvaluated()) this.evaluate();

    // First step is to consult if the symbol is a literal
    if(LiteralDictionary.getInstance().isLiteral(symbol))
      return LiteralDictionary.getInstance().getSymbol(symbol);

    if(symbol.equals(SELF_SYMBOL)) // time to create the self binding that will
                                   // need to be accessed.
    {
      // System.out.println("  Frunction: getSymbol > Symbol was self accessor");
      SymbolBinding self_bind = new DynamicSymbolBinding(SELF_SYMBOL, this,
          this);
      self_bind.setCountable(false); // it shouldn't be countable

      // Adding the new symbol for the self
      try
      {
        this.addSymbol(self_bind);
      }
      catch(FrunctionNotEvaluatedException e)
      {
        e.printStackTrace();
      }
    }

    SymbolBinding binding = null;
    // First case is to check if it will force an environment pop-up
    if(symbol.startsWith(CONTAINER_ENV_SYMBOL))
    {
      // System.out.println("  .  .  Found use of '^', upping the environment. New Sym: '"
      // + symbol.substring(CONTAINER_ENV_SYMBOL.length()) + "'");
      if(this.environment == null) throw new SymbolNotFoundException(symbol);
      binding = this.environment.getSymbol(symbol
          .substring(CONTAINER_ENV_SYMBOL.length()));
    }
    else
    {
      binding = this.symbol_bindings.get(symbol);
      if(binding == null)
      {
      	this.displayHierarchy(2);
        if(this.environment == null) throw new SymbolNotFoundException(symbol);
        binding = this.environment.getSymbol(symbol);
      }
    }
    
    if(binding == null) 
    {
    	throw new SymbolNotFoundException(symbol);
    }

    // Returning the binding, it may be null
    return binding;
  }

  @Override public void addSymbol(SymbolBinding binding) throws FrunctionNotEvaluatedException
  {
  	if(binding == null) throw new NullPointerException("'binding' was null");
    if(!this.isEvaluated()) throw new FrunctionNotEvaluatedException();
    
    // Simply adding the symbol
    // This will override any existing symbols in that place, but it is expected
    // System.out.println("  Adding sym with key '" + binding.getKey() +
    // "' to frunction with type '" + this.getType() + "'");
    this.symbol_bindings.put(binding.getKey(), binding);
  }

  @Override public void setType(String type)
  {
    this.type = type;
  }

  @Override public String getType()
  {
    return this.type;
  }

  @Override public Container getEnvironment()
  {
    return this.environment;
  }

  @Override public String getSource()
  {
    return this.source;
  }

  @Override public List<MethodBinding> getMethods()
  {
    TreeMap<Integer, List<MethodBinding>> sorted_bindings = new TreeMap<>();
    sorted_bindings.putAll(this.method_bindings);

    List<List<MethodBinding>> sorted_list = new ArrayList<>();
    sorted_list.addAll(sorted_bindings.values());

    List<MethodBinding> squished_list = new ArrayList<>();
    for(List<MethodBinding> l : sorted_list)
      squished_list.addAll(l);

    return squished_list;
  }

  @Override public List<SymbolBinding> getSymbols()
  {
    TreeMap<String, SymbolBinding> sorted_bindings = new TreeMap<>();
    sorted_bindings.putAll(this.symbol_bindings);

    List<SymbolBinding> sorted_list = new ArrayList<>();
    sorted_list.addAll(sorted_bindings.values());

    for(int i = sorted_list.size() - 1; i >= 0; i--)
    {
      if(!sorted_list.get(i).isCountable()) sorted_list.remove(i);
    }

    return sorted_list;
  }
}
