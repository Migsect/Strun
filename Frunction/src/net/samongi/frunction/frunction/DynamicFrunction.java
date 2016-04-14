package net.samongi.frunction.frunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.samongi.frunction.binding.Binding;
import net.samongi.frunction.binding.DynamicMethodBinding;
import net.samongi.frunction.binding.DynamicSymbolBinding;
import net.samongi.frunction.binding.EnvironmentChangeSymbolBinding;
import net.samongi.frunction.binding.MethodBinding;
import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.exceptions.parsing.ParsingException;
import net.samongi.frunction.exceptions.runtime.FrunctionNotEvaluatedException;
import net.samongi.frunction.exceptions.runtime.InvalidTypeException;
import net.samongi.frunction.exceptions.runtime.RunTimeException;
import net.samongi.frunction.expression.tokens.Token;
import net.samongi.frunction.frunction.literal.BooleanFrunction;
import net.samongi.frunction.frunction.literal.StringFrunction;
import net.samongi.frunction.frunction.literal.dictionary.LiteralDictionary;
import net.samongi.frunction.parse.ParseUtil;

public class DynamicFrunction implements Frunction
{
  public static final boolean DEBUG = false;

  private static final String SELF_SYMBOL = "@";
  private static final String CONTAINER_RAISE_SYMBOL = "^";
  private static final String ABSOLUTE_RAISE_SYMBOL = "$";
  private static final String TYPE_RAISE_SYMBOL = "%";
  
  private static final String TYPE_KEY = "type";

  /** A frunction consists of a Map of symbol bindings */
  private Map<String, SymbolBinding> symbol_bindings = null;
  /** A frunction cosists of a Map of String[] to method bindings. The strings in this array are types and not pure
   * symbols. Generally an empty type will be used.
   * 
   * There can be multiple method bindings to a set of types. */
  private Map<Integer, List<MethodBinding>> method_bindings = null;

  /** A set will represent the type that a frunction is. */
  private String type = "";

  /** A Frunction will have a parent environment in which it was defined in. This environment is also a frunction */
  private final Container environment;

  /** Source is the body of the frunction. */
  private final String source;

  /** Constructor of the frunction Consists of the frunction the frunction is defined within (its environment) and the
   * source for the frunction.
   * 
   * @param environment
   * @param source */
  public DynamicFrunction(Container environment, String source)
  {
    // Environment can be null
    if(source == null) throw new NullPointerException("'source' was null");

    this.environment = environment;
    this.source = source;
  }

  /** Constructor for when there is no source. Generally used by native frunction definitions. This will automatically
   * call evaluate to ensure that the hashmaps are made
   * 
   * @param environment 
   * @throws RunTimeException 
   * @throws ParsingException */
  public DynamicFrunction(Container environment) throws ParsingException, RunTimeException
  {
    this.environment = environment;
    this.source = "";
    this.evaluate();
  }
  
  /**Creates a copy of the base frunction with the new environment
   * 
   * @param environment The new environment to set it to
   * @param base The base to base it off of.
   * @throws ParsingException
   * @throws RunTimeException
   */
  public DynamicFrunction(Container environment, DynamicFrunction base) throws ParsingException, RunTimeException
  {
    this.environment = environment;
    this.source = base.source;
    this.type = base.type;
    this.method_bindings = base.method_bindings;
    this.symbol_bindings = base.symbol_bindings;
  }

  @Override public Frunction clone(Container new_environment) throws ParsingException, RunTimeException
  {
    return new DynamicFrunction(new_environment, this);
  }
  
  @Override public void evaluate() throws ParsingException, RunTimeException
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
      String section = ParseUtil.getSection(source, i, Binding.BINDING_SEPERATOR, Token.getScopeOpenIdentifiers(), Token.getScopeCloseIdentifiers(), Token.getScopeeToggleIdentifiers());
      // Increating the index variable for the next section grab
      i += section.length();
      // Removing the binding seperator from the end.
      if(section.endsWith(Binding.BINDING_SEPERATOR)) section = section.substring(0, section.length() - Binding.BINDING_SEPERATOR.length());
      // Now we have a clean binding defintion.

      MethodBinding met_binding = DynamicMethodBinding.parseBinding(section);
      if(met_binding != null)
      {
        this.addMethod(met_binding);
        continue;
      }
      SymbolBinding sym_binding = DynamicSymbolBinding.parseBinding(section, this);
      if(sym_binding != null)
      {
        this.addSymbol(sym_binding);
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

  @Override public MethodBinding getMethod(String[] types, Frunction[] inputs) throws ParsingException, RunTimeException
  {
    if(DEBUG) System.out.println("  Fetching method with types:" + ParseUtil.concatStringArray(types));
    if(types.length != inputs.length)
    {
      if(DEBUG) System.out.println("  Issue in getMethod: types and input legnths missmatch");
      if(DEBUG) System.out.println("    with ypes:" + ParseUtil.concatStringArray(types));
      // TODO proper exception
      return null;
    }
    if(!this.isEvaluated()) this.evaluate(); // evaluating the frunction if it
                                             // hasn't yet been.

    List<MethodBinding> methods = this.method_bindings.get(types.length);
    if(methods == null || methods.isEmpty())
    {
      if(DEBUG) System.out.println("  Issue in getMethod: Returned Binding list returned empty or null");
      if(DEBUG) System.out.println("    with types:" + ParseUtil.concatStringArray(types));
      return null;
    }

    MethodBinding b = this.searchMethodsTyped(methods, types, inputs);
    if(b != null) return b;

    b = this.searchMethodsUntyped(methods, types, inputs);
    if(b != null) return b;

    if(DEBUG) System.out.println("  Issue in getMethod: Couldn't find the method.");
    if(DEBUG) System.out.println("    with types:" + ParseUtil.concatStringArray(types));
    return null; // We didn't find the method for the types
  }

  private MethodBinding searchMethodsUntyped(List<MethodBinding> bindings, String[] types, Frunction[] inputs) throws ParsingException, RunTimeException
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
        if(DEBUG) System.out.println("  Issue in getMethod: Input symbols length did not match inputs length");
        if(DEBUG) System.out.println("    with types:" + ParseUtil.concatStringArray(types));
        return null;
      }
      // Adding the input symbols to the container
      for(int i = 0; i < input_symbols.length; i++)
      {
        m_container.addSymbol(input_symbols[i], inputs[i]);
      }

      // Getting the boolean result.
      Frunction result = b.getConditional().evaluate(m_container);;
      // Evaluated the conditional expression using the inputs

      // We are now going to test and see if the method is a boolean type
      // If it is not, we are going to assume the expression evaluates to true
      // This is done such that only false-hood will prevent a method from
      // working.
      if(result.getType().equals(BooleanFrunction.TYPE) && result instanceof BooleanFrunction)
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

  private MethodBinding searchMethodsTyped(List<MethodBinding> bindings, String[] types, Frunction[] inputs) throws ParsingException, RunTimeException
  {
    if(bindings == null) throw new NullPointerException("'bindings' was null");
    if(types == null) throw new NullPointerException("'types' was null");
    if(inputs == null) throw new NullPointerException("'inputs' was null");

    for(MethodBinding b : bindings)
    {
      String[] input_types = b.getTypes();
      boolean do_skip = false; // Used as a flag to skip on to the next binding.
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
        if(DEBUG) System.out.println("  Issue in getMethod: Input symbols length did not match inputs length");
        if(DEBUG) System.out.println("    with types:" + ParseUtil.concatStringArray(types));
        return null;
      }
      // Adding the input symbols to the container
      for(int i = 0; i < input_symbols.length; i++)
      {
        m_container.addSymbol(input_symbols[i], inputs[i]);
      }

      // Getting the boolean result.
      Frunction result = b.getConditional().evaluate(m_container);
      // Evaluated the conditional expression using the inputs

      // We are now going to test and see if the method is a boolean type
      // If it is not, we are going to assume the expression evaluates to true
      // This is done such that only false-hood will prevent a method from
      // working.
      if(result.getType().equals(BooleanFrunction.TYPE) && result instanceof BooleanFrunction)
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

  @Override public void addMethod(MethodBinding binding) throws RunTimeException
  {
    if(!this.isEvaluated()) throw new FrunctionNotEvaluatedException();

    String[] types = binding.getTypes();
    if(DEBUG) System.out.println("  Adding with types:" + ParseUtil.concatStringArray(types));
    // Generating the list if it doesn't exist
    if(!this.method_bindings.containsKey(types.length)) this.method_bindings.put(types.length, new ArrayList<MethodBinding>());
    // Retrieving the list
    List<MethodBinding> binding_list = this.method_bindings.get(types.length);
    // Adding the binding to the list
    binding_list.add(binding);
    if(DEBUG) System.out.println("    List size: " + binding_list.size());
  }

  // We need to grab the symbol
  @Override public SymbolBinding getSymbol(String symbol) throws ParsingException, RunTimeException
  {
    // cleaning up the symbol
    symbol = symbol.trim();
    // Evaluating the frunction if it isn't already
    if(!this.isEvaluated()) this.evaluate();

    // <<< Check to see if it is a literal >>>
    if(LiteralDictionary.getInstance().isLiteral(symbol)) return LiteralDictionary.getInstance().getSymbol(symbol);

    // <<< Check to see if it is the self-binding >>>
    if(symbol.equals(SELF_SYMBOL)) 
    {
      SymbolBinding self_bind = new DynamicSymbolBinding(SELF_SYMBOL, this);
      self_bind.setCountable(false); // it shouldn't be countable
      self_bind.setPrivate(true);
      
      this.addSymbol(self_bind);
    }
    
    
    // <<< Starts with the raise modifier >>>
    if(symbol.startsWith(CONTAINER_RAISE_SYMBOL) && this.environment != null)
    {
      return this.environment.getSymbol(symbol.substring(CONTAINER_RAISE_SYMBOL.length()));
    }
    // <<< Starts with the type raise modifier >>>
    if(symbol.startsWith(TYPE_RAISE_SYMBOL))
    {
      Frunction type_frunction = this.getTypeFrunction();
      if(type_frunction == null) return null;
      return new EnvironmentChangeSymbolBinding((DynamicSymbolBinding) type_frunction.getSymbol(symbol.substring(TYPE_RAISE_SYMBOL.length())), this);
    }
    // <<< Starts with the absolute raise modifier >>>
    if(symbol.startsWith(ABSOLUTE_RAISE_SYMBOL))
    {
      if(this.environment == null) return this.getSymbol(symbol.substring(ABSOLUTE_RAISE_SYMBOL.length()));
      else return this.environment.getSymbol(symbol);
    }

    SymbolBinding binding = null;
    // <<< Local environment check >>>
    binding = this.symbol_bindings.get(symbol);
    if(binding != null) return binding;
    
    // <<< Type environment check >>>
    Frunction type_frunction = this.getTypeFrunction();
    // TODO possible problems here if we ever have another base type of symbol binding.
    if(type_frunction != null) binding = type_frunction.getSymbol(symbol); 
    if(binding != null) return new EnvironmentChangeSymbolBinding((DynamicSymbolBinding) binding, this);
    
    System.out.println("Symbol: '" + symbol + "'");
    
    // <<< Raise environment check >>>
    if(this.environment == null) return null;
    binding = this.environment.getSymbol(symbol);

    // Returning the binding, it may be null
    return binding;
  }

  @Override public void addSymbol(SymbolBinding binding) throws RunTimeException, ParsingException
  {
    if(binding == null) throw new NullPointerException("'binding' was null");
    if(!this.isEvaluated()) throw new FrunctionNotEvaluatedException();
    
    // Setting the native-based identifier of type if it is a type.
    if(binding.getKey().toLowerCase().equals(TYPE_KEY))
    {
      Frunction inner = binding.get(this);
      if(!(inner instanceof StringFrunction)) throw new InvalidTypeException(inner);
      StringFrunction s_inner = (StringFrunction) inner;
      this.setType(s_inner.getNative().toLowerCase());
    }
    
    // Checking if the binding should be set to the type
    if(binding.getKey().startsWith(TYPE_RAISE_SYMBOL))
    {
      Frunction type_frunction = this.getTypeFrunction();
      type_frunction.addSymbol(binding.clone(binding.getKey().substring(TYPE_RAISE_SYMBOL.length())));
    }
    else this.symbol_bindings.put(binding.getKey(), binding);
    // Simply adding the symbol
    // This will override any existing symbols in that place, but it is expected
    
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
  
  /**Checks to see if the frunction contains the symbol.
   * This will detect any symbol and does not take into account "countable"
   * 
   * @param symbol The symbol to check for
   * @return True if the frunction has the symbol
   */
  public boolean hasLocalSymbol(String symbol)
  {
    return this.symbol_bindings.containsKey(symbol);
  }
  /**Will check to see if it has either a local symbol or a type symbol.
   * 
   * @param symbol
   * @return
   * @throws RunTimeException 
   * @throws ParsingException 
   */
  @Override public boolean hasAccessibleSymbol(String symbol) throws ParsingException, RunTimeException
  {
    if(this.symbol_bindings.containsKey(symbol)) return true;
    Frunction type_frunction = this.getTypeFrunction();
    if(type_frunction != null && type_frunction.hasAccessibleSymbol(symbol)) return true;
    return false;
  }
  
  /**Will count the symbols in the frunction.
   * This will take into account if a symbol is "countable" or not.
   * The symbol will specify if it is countable or not.
   * 
   * @return The number of countable symbols
   */
  public int countSymbols()
  {
    int sum = 0;
    for(SymbolBinding b : this.symbol_bindings.values())
    {
      if(b.isCountable()) sum ++;
    }
    return sum;
  }
  /**Will return the total amount of symbols in this frunction
   * These symbols will be defined to this frunction and not the
   * type frunction.
   * 
   * @return The total number of symbols
   */
  public int countAllSymbols()
  {
    return this.symbol_bindings.size();
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
