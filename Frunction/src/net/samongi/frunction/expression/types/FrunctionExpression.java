package net.samongi.frunction.expression.types;

import net.samongi.frunction.expression.tokens.FrunctionToken;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.DynamicFrunction;
import net.samongi.frunction.frunction.Frunction;

public class FrunctionExpression implements Expression
{
  private final FrunctionToken token;

  public FrunctionExpression(FrunctionToken token)
  {
    this.token = token;
  }

  @Override public Type getType(){return Expression.Type.FRUNCTION;}
  
  @Override public String getDisplay()
  {
    return "F<" + token.getSource() + ">";
  }

  @Override public Frunction evaluate(Container environment)
  {
    if(environment == null) throw new NullPointerException("'environment' was null");

    DynamicFrunction frunction = new DynamicFrunction(environment,
        token.getSource());
    frunction.evaluate();
    return frunction;
  }

}