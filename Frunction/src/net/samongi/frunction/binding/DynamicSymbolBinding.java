package net.samongi.frunction.binding;

import net.samongi.frunction.exceptions.parsing.ExpressionException;
import net.samongi.frunction.exceptions.parsing.ParsingException;
import net.samongi.frunction.exceptions.parsing.TokenException;
import net.samongi.frunction.exceptions.runtime.RunTimeException;
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
   * @throws RunTimeException 
   * @throws ExpressionException
   * @throws TokenException */
  public static DynamicSymbolBinding parseBinding(String text_section, Container environment) throws ParsingException, RunTimeException
  {
    if(text_section == null) throw new NullPointerException("'text_section' was null");
    
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
    
    if(key == null) throw new NullPointerException("'key' was null");
    if(source == null) throw new NullPointerException("'source' was null");

    boolean do_eval = !key.startsWith(DELAY_EVAL);
    if(!do_eval) key = key.replaceFirst(DELAY_EVAL, "");

    DynamicSymbolBinding binding = new DynamicSymbolBinding(key, source);
    if(do_eval) binding.collapse(environment);

    return binding;
  }

  private final String key;
  private final String source;
  private final boolean is_private = false; // TODO implement binding privacy
  private boolean countable = true;

  private Expression expression = null;
  private Frunction collapsed = null;

  public DynamicSymbolBinding(String key, String source)
  {
    if(key == null) throw new NullPointerException("'key' was null");
    if(source == null) throw new NullPointerException("'source' was null");

    this.key = key;
    this.source = source;
  }

  public DynamicSymbolBinding(String key, Frunction evaluated)
  {
    if(key == null) throw new NullPointerException("key was null");
    if(evaluated == null) throw new NullPointerException("evaluated was null");

    this.key = key;
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
   * @throws TokenException
   * @throws ExpressionException */
  @Override public Expression getExpression(Container environment) throws ParsingException
  {
    if(this.expression == null) this.evaluate(environment);
    if(this.expression == null) throw new IllegalStateException();
    return this.expression;
  }

  @Override public String toDisplay()
  {
    String display = "'" + this.key + "':'" + this.source + "'";
    return display;
  }

  @Override public void evaluate(Container environment) throws ParsingException
  {
    if(this.expression == null) try
    {
      this.expression = Expression.parseString(this.source, environment);
    }
    catch (ParsingException e)
    {
      System.out.println("Under Binding : '" + this.getKey() + "'");
      throw e;
    }
    if(this.expression == null) throw new IllegalStateException();
  }

  @Override public void collapse(Container environment) throws ParsingException, RunTimeException
  {
    // First we need to evaluate the symbol.
    this.evaluate(environment);
    // Now we need to set collapsed
    if(this.collapsed == null) this.collapsed = this.expression.evaluate(environment);
  }

  @Override public Frunction get(Container environment) throws ParsingException, RunTimeException
  {
    if(this.collapsed != null) return this.collapsed;
    this.collapse(environment);
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
