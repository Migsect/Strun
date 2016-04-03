package net.samongi.frunction.binding;

import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.tokens.Token;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.literal.BooleanFrunction;
import net.samongi.frunction.parse.ParseUtil;

/**Method bindings bind a set of inputs to an expression.
 * 
 * @author Alex
 *
 */
public class DynamicMethodBinding implements MethodBinding
{
	private static final String BINDING = "->";
	private static final String COND_OPERATOR = "?";
	private static final String INPUT_SPLITTER = ",";
	
	private final Container container;
	
	private final String[] input_symbols;
	private final String[] input_types; // TODO type checking
	
	private final String source;
	private final String condition_source;
	
	private Expression condition = null;
	private Expression expression = null;

	/**Parse a section of text and returns a method binding
	 * 
	 * @param text_section The section of text to parse
	 * @param environment The environment the binding will be defined in
	 * @return A dynamic symbol binding
	 */
	public static DynamicMethodBinding parseBinding(String section, Container environment)
	{
	  //System.out.println("Parsing for method: " + section);
		int i = 0; // indexer for the whole section
		
		
		// Getting the section prior to the binding.
	  String prior = ParseUtil.getSection(section, i, BINDING, 
	  		Token.getScopeOpenIdentifiers(), 
	  		Token.getScopeCloseIdentifiers(), 
	  		Token.getScopeeToggleIdentifiers());
	  i += prior.length(); // Adding to the index length to get the expression
    prior = prior.trim(); // Trimming the prior

	  // Removing the binding operator from prior
	  if(prior.endsWith(BINDING)) prior = prior.substring(0, prior.length() - BINDING.length()).trim();
	  
	  // Getting the expression section
	  if(section.length() <= i) return null; // This means there is no expression
	  String expression = section.substring(i).trim(); // getting the remainder
	  System.out.println("  Expression is: '" + expression + "'");
	  
	  // indexer for the prior section
	  int j = 0; 
	  
	  // The input section represents the input symbol defintion
	  String input_section = ParseUtil.getSection(prior, j, COND_OPERATOR, 
	  		Token.getScopeOpenIdentifiers(), 
	  		Token.getScopeCloseIdentifiers(), 
	  		Token.getScopeeToggleIdentifiers());
	  j += input_section.length(); // incrementing the prior index
    input_section = input_section.trim(); // trimming the input section;
	  
	  // Removing the conditional operator
	  if(input_section.endsWith(COND_OPERATOR)) input_section = input_section.substring(0, input_section.length() - COND_OPERATOR.length()).trim();
	  
	  // boolean for testing if the input was enclosed
	  boolean input_enclosed = input_section.startsWith("(") && input_section.endsWith(")");
  	if(input_enclosed) input_section = input_section.substring(1, input_section.length() - 1).trim();
	  
	  // Creating the condition expression
  	//  This is the remainder of the input section
	  String condition = null;
	  if(prior.length() > j) // checking to see if there is a conditional clause.
	  {
	    System.out.println("  Found Condition");
	  	condition = prior.substring(j); // getting the condition
	  	boolean cond_enclosed = condition.startsWith("(") && condition.endsWith(")");
	  	if(cond_enclosed) condition = condition.substring(1, condition.length() - 1).trim();
	  	// Now we should have the condition expression;
	  }
	  if(condition == null) condition = ""; // making sure it aint null; 
	  System.out.println("  Condition is: '" + condition + "'");
	  
	  if(input_section.length() == 0) return new DynamicMethodBinding(environment, new String[0], new String[0], condition, expression);
	  
	  // Splitting the input
	  String[] split_input = input_section.split(INPUT_SPLITTER);
	  String[] input_symbols = new String[split_input.length];
	  String[] input_types = new String[split_input.length];
	  for(int k = 0; k < split_input.length; k++)
	  {
	  	String part = split_input[k].trim(); // removing whitespace as well as squeezing
	  	System.out.println("  " + k + " Part: '" + part + "'");
	  	String[] parts = part.split(" +"); // splitting the part, this is how types are specified.
	  	
	  	input_symbols[k] = parts[0]; // first is going to be the symbol
	  	if(parts.length > 1) input_types[k] = parts[1]; // second might be the type if it exists
	  	else input_types[k] = "";;
	  }
	  
	  // Time to return 
	  return new DynamicMethodBinding(environment, input_symbols, input_types, condition, expression);
	}
	
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
	  if(this.condition.equals(null)) this.condition = BooleanFrunction.getTautology();
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

  @Override public void evaluate() throws TokenException
  {
    this.generateCondition();
    this.generateExpression();
  }

}