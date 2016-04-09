package net.samongi.frunction.exceptions.parsing;

import net.samongi.frunction.expression.types.Expression.Type;

public class MissingExpressionException extends ExpressionException
{
  private static final long serialVersionUID = 3926670230021509231L;

  public MissingExpressionException(Type type, String source)
  {
    super(type, source);
  }

}
