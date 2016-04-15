package net.samongi.frunction.error.syntax;

import net.samongi.frunction.expression.types.Expression.Type;

public class MissingExpectedTokensError extends ExpressionError
{
  private static final long serialVersionUID = 7138678976587144535L;

  public MissingExpectedTokensError(Type type, String source)
  {
    super(type, source);
  }

}
