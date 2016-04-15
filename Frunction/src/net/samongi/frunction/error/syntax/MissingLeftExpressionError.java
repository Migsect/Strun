package net.samongi.frunction.error.syntax;

import net.samongi.frunction.expression.types.Expression.Type;

public class MissingLeftExpressionError extends ExpressionError
{
  private static final long serialVersionUID = 3926670230021509231L;

  public MissingLeftExpressionError(Type type, String source)
  {
    super(type, source);
  }

}
