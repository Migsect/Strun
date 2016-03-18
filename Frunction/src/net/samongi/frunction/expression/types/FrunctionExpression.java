package net.samongi.frunction.expression.types;

import net.samongi.frunction.expression.tokens.FrunctionToken;
import net.samongi.frunction.frunction.Frunction;

public class FrunctionExpression implements Expression
{
  private final FrunctionToken token;
  
  public FrunctionExpression(FrunctionToken token)
  {
    this.token = token;
  }
  
  @Override public Frunction evaluate(Frunction environment)
  {
    Frunction frunction = new Frunction(environment, token.getSource());
    return frunction;
  }

}
