package net.samongi.frunction.error.syntax;

import net.samongi.frunction.expression.types.Expression;

/** Represents when something goes wrong with an expression parsing May also represent a generic expression issue */
public class ExpressionError extends SyntaxError
{
  private static final long serialVersionUID = -7841774179464158697L;

  private final String source;
  private final Expression.Type type;

  public ExpressionError(Expression.Type type, String source)
  {
    this.type = type;
    this.source = source;
  }

  public String getSource()
  {
    return this.source;
  }

  public Expression.Type getType()
  {
    return this.type;
  }
}
