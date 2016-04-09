package net.samongi.frunction.exceptions.parsing;

import net.samongi.frunction.expression.types.Expression.Type;

public class BadlyPlacedExpressionException extends ExpressionException
{
  private static final long serialVersionUID = 749793243989719105L;

  public BadlyPlacedExpressionException(Type type, String source)
  {
    super(type, source);
    // TODO Auto-generated constructor stub
  }

}
