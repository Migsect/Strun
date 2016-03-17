package net.samongi.frunction.expression.tokens;

public class InputToken implements Token
{
	public static final String OPEN = "(";
	public static final String CLOSE = ")";
	
  private final String source;
  
  private String[] input_symbols = null;
  
  public InputToken(String source)
  {
  	this.source = source;
  }
  
  public void evaluate()
  {
  	// Splitting the input symbols or expressions by the comma.
  	if(this.input_symbols == null) this.input_symbols = source.split(",");
  }
  
  @Override public String getSource(){return this.source;}
}
