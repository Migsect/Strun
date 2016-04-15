package net.samongi.frunction.error.syntax;

import net.samongi.frunction.expression.types.Expression.Type;

public class UnexpectedExpressionError extends ExpressionError
{
  private static final long serialVersionUID = 749793243989719105L;

  public UnexpectedExpressionError(Type type, String source)
  {
    super(type, source);
    // TODO Auto-generated constructor stub
  }

}
