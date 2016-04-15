package net.samongi.frunction.binding;

import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.error.syntax.ExpressionError;
import net.samongi.frunction.error.syntax.SyntaxError;
import net.samongi.frunction.error.syntax.TokenError;
import net.samongi.frunction.expression.tokens.Token;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.parse.ParseUtil;

public class DynamicSymbolBinding implements SymbolBinding
{
  public static final boolean DEBUG = false;

  private static final String DEF_KEY = "_";
  private static final String BINDING = ":";
  private static final String DELAY_EVAL = "!";

  /** Parse a section of text and returns a symbol binding
   * 
   * @param text_section The section of text to parse
   * @param environment The environment of which the binding will be made
   * @return A dynamic symbol bidning
   * @throws RunTimeError 
   * @throws ExpressionError
   * @throws TokenError */
  public static DynamicSymbolBinding parseBinding(String text_section, Container environment) throws SyntaxError, RunTimeError
  {
    if(text_section == null) throw new NullPointerException("'text_section' was null");
    if(environment == null) throw new NullPointerException("'environment' was null");
    
    int i = 0;
    
    // Getting the prior section
    String prior = ParseUtil.getSection(text_section, i, BINDING, Token.getScopeOpenIdentifiers(), Token.getScopeCloseIdentifiers(), Token.getScopeeToggleIdentifiers());
    i += prior.length();
    prior.trim();
    // Removing the binding if it exists
    if(prior.endsWith(BINDING)) prior = prior.substring(0, prior.length() - BINDING.length()).trim();
    
    // if i == text_section.length then that means prior represents everything
    String key = null;
    String source = null;
    if(i >= text_section.length())
    {
      key = DEF_KEY;
      source = prior;
    }
    else
    {
      key = prior;
      source = text_section.substring(i);
    }
    
    // Splitting the section based on the first bound binding operator
    /*
    String[] split_section = text_section.split(BINDING, 2);
    if(split_section.length < 2) 
    {
      key = DEF_KEY;
      source = split_section[0].trim(); 
    }
    else
    {
      key = split_section[0].trim();
      source = split_section[1].trim();
    }
    */
    if(key == null) throw new NullPointerException("'key' was null");
    if(source == null) throw new NullPointerException("'source' was null");

    boolean do_eval = !key.startsWith(DELAY_EVAL);
    if(!do_eval) key = key.replaceFirst(DELAY_EVAL, "");

    DynamicSymbolBinding binding = new DynamicSymbolBinding(key, source, environment);
    if(do_eval) binding.collapse();

    return binding;
  }

  private final String key;
  private final String source;
  private final boolean is_private = false; // TODO implement binding privacy
  private boolean countable = true;

  private Expression expression = null;
  private Frunction collapsed = null;

  // The container of this
  private final Container environment;

  public DynamicSymbolBinding(String key, String source, Container environment)
  {
    if(key == null) throw new NullPointerException("'key' was null");
    if(environment == null) throw new NullPointerException("environment was null");
    if(source == null) throw new NullPointerException("'source' was null");

    this.key = key;
    this.source = source;
    this.environment = environment;
  }

  public DynamicSymbolBinding(String key, Frunction evaluated, Container environment)
  {
    if(key == null) throw new NullPointerException("key was null");
    if(evaluated == null) throw new NullPointerException("evaluated was null");
    if(environment == null) throw new NullPointerException("environment was null");

    this.key = key;
    this.environment = environment;
    this.collapsed = evaluated;
    this.expression = evaluated.toExpression();
    this.source = "";

  }

  @Override public String getKey()
  {
    return this.key;
  }

  @Override public String getSource()
  {
    return this.source;
  }

  @Override public boolean isPrivate()
  {
    return this.is_private;
  }

  /** Returns the expression this binding relates to This will force the binding to update it's expression given it
   * hasn't created one yet.
   * 
   * @return An expression object
   * @throws TokenError
   * @throws ExpressionError */
  @Override public Expression getExpression() throws SyntaxError
  {
    if(this.expression == null) this.evaluate();
    if(this.expression == null) throw new IllegalStateException();
    return this.expression;
  }

  @Override public Container getContainer()
  {
    return this.environment;
  }

  @Override public String toDisplay()
  {
    String display = "'" + this.key + "':'" + this.source + "'";
    return display;
  }

  @Override public void evaluate() throws SyntaxError
  {
    if(this.expression == null) try
    {
      this.expression = Expression.parseString(this.source, this.environment);
    }
    catch (SyntaxError e)
    {
      System.out.println("Under Binding : '" + this.getKey() + "'");
      throw e;
    }
    if(this.expression == null) throw new IllegalStateException();
  }

  @Override public void collapse() throws SyntaxError, RunTimeError
  {
    // First we need to evaluate the symbol.
    this.evaluate();
    // Now we need to set collapsed
    if(this.collapsed == null) this.collapsed = this.expression.evaluate(this.environment);
  }

  @Override public Frunction get() throws SyntaxError, RunTimeError
  {
    if(this.collapsed != null) return this.collapsed;
    this.collapse();
    return this.collapsed;
  }

  @Override public boolean isCountable()
  {
    return this.countable;
  }

  @Override public void setCountable(boolean is_countable)
  {
    this.countable = is_countable;
  }

}
