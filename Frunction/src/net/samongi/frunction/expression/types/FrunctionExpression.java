package net.samongi.frunction.expression.types;

import net.samongi.frunction.expression.tokens.FrunctionToken;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.DynamicFrunction;

public class FrunctionExpression implements Expression
{
  private final FrunctionToken token;
  
  public FrunctionExpression(FrunctionToken token)
  {
    this.token = token;
  }
  
  @Override public DynamicFrunction evaluate(Container environment)
  {
    DynamicFrunction frunction = new DynamicFrunction(environment, token.getSource());
    return frunction;
  }

}
