package net.samongi.frunction.expression;

/**Used to form expressions which evaluate to Frunctions
 * Expressions occur as a string.
 * 
 * @author Alex
 *
 */
public class ExpressionCreator
{
	private static ExpressionCreator instance = null;
	public static ExpressionCreator instance(){return instance;}
	
	private static final String INPUT_OPEN  		= "("; // Defines input
	private static final String INPUT_CLOSE 		= ")";
	
	private static final String PARTITION_OPEN 	= "["; // Defines sub expressions
	private static final String PARTITION_CLOSE = "]";
	
	private static final String FRUNCTION_OPEN 	= "{"; // Defines frunction definitions
	private static final String FRUNCTION_CLOSE = "}"; // This will cause recusion of the parser
	
	public ExpressionCreator()
	{
		if(ExpressionCreator.instance != null) throw new IllegalStateException(); // it can only be made once
		ExpressionCreator.instance = this;
	}
	
	/**Will parse the expression string
	 * 
	 * @param exp_str The string to parse
	 * @return Returns an expression
	 */
	public Expression parse(String exp_str)
	{
		// First we must get what is contained within parenthesis
		String[] split_str = exp_str.split(INPUT_OPEN);
		
		
		return null;
	}
}
