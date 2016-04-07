package net.samongi.frunction.expression.tokens;

public class AccessorToken implements Token
{
  public static final String OPERATOR = ".";

  @Override public String getSource()
  {
    return OPERATOR;
  }

  @Override public Token.Type getType()
  {
    return Token.Type.ACCESSOR;
  }

}
