package net.samongi.frunction.exceptions.parsing;

import net.samongi.frunction.expression.types.Expression.Type;

public class MissingExpectedTokensException extends ExpressionException
{
  private static final long serialVersionUID = 7138678976587144535L;

  public MissingExpectedTokensException(Type type, String source)
  {
    super(type, source);
  }

}
