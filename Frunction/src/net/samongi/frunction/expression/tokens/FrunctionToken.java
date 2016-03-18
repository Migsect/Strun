package net.samongi.frunction.expression.tokens;

public class FrunctionToken implements Token
{
	public static final String OPEN = "{";
	public static final String CLOSE = "}";
	
	private final String source;
	
	public FrunctionToken(String source)
	{
		this.source = source;
	}
	
	@Override public String getSource(){return this.source;}
  @Override public Token.Type getType(){return Token.Type.FRUNCTION;}
}
