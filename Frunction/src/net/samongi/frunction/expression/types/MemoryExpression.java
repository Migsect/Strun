package net.samongi.frunction.expression.types;

import net.samongi.frunction.exceptions.parsing.ParsingException;
import net.samongi.frunction.exceptions.runtime.RunTimeException;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;

/** Used to remember the frunction output from an expression This generally encapsulates other expressions after they
 * have been parsed. This does not really break any expressions but should introduce memoization
 * 
 * @author Alex */
public class MemoryExpression implements Expression
{
  private final Expression inner;
  private Frunction evaluated = null;

  public MemoryExpression(Expression expr)
  {
    this.inner = expr;
  }

  @Override public Type getType()
  {
    return Expression.Type.MEMORY;
  }

  @Override public String getDisplay()
  {
    return this.inner.getDisplay();
  }

  @Override public Frunction evaluate(Container environment) throws ParsingException, RunTimeException
  {
    if(environment == null) throw new NullPointerException("'environment' was null");

    if(evaluated == null) this.evaluated = this.inner.evaluate(environment);
    return this.evaluated;
  }

}
