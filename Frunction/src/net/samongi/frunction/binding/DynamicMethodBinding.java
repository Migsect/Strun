package net.samongi.frunction.binding;

import net.samongi.frunction.error.syntax.ExpressionError;
import net.samongi.frunction.error.syntax.SyntaxError;
import net.samongi.frunction.error.syntax.TokenError;
import net.samongi.frunction.expression.tokens.Token;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.literal.BooleanFrunction;
import net.samongi.frunction.parse.ParseUtil;

/** Method bindings bind a set of inputs to an expression.
 * 
 * @author Alex */
public class DynamicMethodBinding implements MethodBinding
{
  public static final boolean DEBUG = false;

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

  /** Parse a section of text and returns a method binding
   * 
   * @param text_section The section of text to parse
   * @param environment The environment the binding will be defined in
   * @return A dynamic symbol binding */
  public static DynamicMethodBinding parseBinding(String section, Container environment)
  {
    if(section == null) throw new NullPointerException("'section' was null");
    if(environment == null) throw new NullPointerException("'environment' was null");

    // System.out.println("Parsing for method: " + section);
    int i = 0; // indexer for the whole section

    // Getting the section prior to the binding.
    String prior = ParseUtil.getSection(section, i, BINDING, Token.getScopeOpenIdentifiers(), Token.getScopeCloseIdentifiers(), Token.getScopeeToggleIdentifiers());
    i += prior.length(); // Adding to the index length to get the expression
    prior = prior.trim(); // Trimming the prior

    // Removing the binding operator from prior
    if(prior.endsWith(BINDING)) prior = prior.substring(0, prior.length() - BINDING.length()).trim();

    // Getting the expression section
    if(section.length() <= i) return null; // This means there is no expression
    String expression = section.substring(i).trim(); // getting the remainder
    if(DEBUG) System.out.println("  Parsing Method - Expr Source: '" + expression + "'");

    // indexer for the prior section
    int j = 0;

    // The input section represents the input symbol defintion
    String input_section = ParseUtil.getSection(prior, j, COND_OPERATOR, Token.getScopeOpenIdentifiers(), Token.getScopeCloseIdentifiers(), Token.getScopeeToggleIdentifiers());
    j += input_section.length(); // incrementing the prior index
    input_section = input_section.trim(); // trimming the input section;

    // Removing the conditional operator
    if(input_section.endsWith(COND_OPERATOR)) input_section = input_section.substring(0, input_section.length() - COND_OPERATOR.length()).trim();

    // boolean for testing if the input was enclosed
    boolean input_enclosed = input_section.startsWith("(") && input_section.endsWith(")");
    if(input_enclosed) input_section = input_section.substring(1, input_section.length() - 1).trim();

    // Creating the condition expression
    // This is the remainder of the input section
    String condition = null;
    if(prior.length() > j) // checking to see if there is a conditional clause.
    {
      condition = prior.substring(j); // getting the condition
      boolean cond_enclosed = condition.startsWith("(") && condition.endsWith(")");
      if(cond_enclosed) condition = condition.substring(1, condition.length() - 1).trim();
      // Now we should have the condition expression;
    }
    if(condition == null) condition = ""; // making sure it aint null;
    
    if(input_section.length() == 0) return new DynamicMethodBinding(environment, new String[0], new String[0], condition, expression);

    // Splitting the input
    String[] split_input = input_section.split(INPUT_SPLITTER);
    String[] input_symbols = new String[split_input.length];
    String[] input_types = new String[split_input.length];
    for(int k = 0; k < split_input.length; k++)
    {
      String part = split_input[k].trim(); // removing whitespace as well as
                                           // squeezing
      String[] parts = part.split(" +"); // splitting the part, this is how
                                         // types are specified.

      input_symbols[k] = parts[0]; // first is going to be the symbol
      if(parts.length > 1) input_types[k] = parts[1]; // second might be the
                                                      // type if it exists
      else input_types[k] = "";;
    }

    // Time to return
    return new DynamicMethodBinding(environment, input_symbols, input_types, condition, expression);
  }

  public DynamicMethodBinding(Container container, String[] input_symbols, String[] input_types, Expression condition, Expression expression)
  {
    if(container == null) throw new NullPointerException("'container' was null");
    if(input_symbols == null) throw new NullPointerException("'input_symbols' was null");
    if(input_types == null) throw new NullPointerException("'input_types' was null");
    if(condition == null) throw new NullPointerException("'condition' was null");
    if(expression == null) throw new NullPointerException("'expression' was null");
    
    this.container = container;
    this.input_symbols = input_symbols;
    this.input_types = input_types;

    this.source = "";
    this.condition_source = "";
    this.condition = condition;
    this.expression = expression;
  }

  public DynamicMethodBinding(Container container, String[] input_symbols, String[] input_types, String cond_source, String source)
  {
    if(container == null) throw new NullPointerException("'container' was null");
    if(input_symbols == null) throw new NullPointerException("'input_symbols' was null");
    if(input_types == null) throw new NullPointerException("'input_types' was null");
    if(cond_source == null) throw new NullPointerException("'cond_source' was null");
    if(source == null) throw new NullPointerException("'source' was null");
    
    this.container = container;
    this.input_symbols = input_symbols;
    this.input_types = input_types;

    this.source = source;
    this.condition_source = cond_source;
  }

  @Override public String[] getInputSymbols()
  {
    return this.input_symbols;
  }

  /** Generates the condition for the method binding
   * 
   * @throws TokenError
   * @throws ExpressionError */
  public void generateCondition() throws SyntaxError
  {
    if(this.condition != null) return; // don't need to reparse the expression
    if(this.condition_source.length() == 0) this.condition = BooleanFrunction.getTautology();
    else this.condition = Expression.parseString(condition_source, container);
  }

  @Override public Expression getConditional() throws SyntaxError
  {
    if(this.condition == null) this.generateCondition();
    return this.condition;
  }

  /** Will generate the expression for method.
   * 
   * @throws TokenError
   * @throws ExpressionError */
  public void generateExpression() throws TokenError, ExpressionError
  {
    if(this.expression != null) return; // don't need to reparse the expression
    this.expression = Expression.parseString(source, container);
    if(this.expression == null) throw new IllegalStateException();

  }

  @Override public Expression getExpression() throws TokenError, ExpressionError
  {
    if(this.expression == null) this.generateExpression();
    return this.expression;
  }

  @Override public String getSource()
  {
    return this.source;
  }

  @Override public Container getContainer()
  {
    return this.container;
  }

  @Override public String toDisplay()
  {
    String inputs = "( ";
    for(String i : this.input_symbols)
      inputs += i + " ";
    inputs += ")";

    String types = "[";
    for(String t : this.input_types)
      types += " '" + t + "' ";
    types += "]";

    try
    {
      return inputs + types + "->'" + this.getExpression().getDisplay() + "'";
    }
    catch(TokenError | ExpressionError e)
    {
      e.printStackTrace();
    }
    return null;
  }

  @Override public String[] getTypes()
  {
    return this.input_types;
  }

  @Override public void evaluate() throws SyntaxError
  {
    this.generateCondition();
    this.generateExpression();
  }

}