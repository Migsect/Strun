package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.DynamicFrunction;
import net.samongi.frunction.frunction.Frunction;

public abstract class NativeFrunction extends DynamicFrunction
{
	/**Will attempt to parse a symbol and try to forulate a literal from it.
	 * This will return null if it could not forumate a frunction fro the
	 * symbol.
	 * 
	 * @param symbol The symbol to parse
	 * @param environment The environment that the frunction will be made in
	 * @return A frunction of the literal, otherwise null
	 */
	public static Frunction parseLiteral(String symbol, Container environment)
	{
		symbol = symbol.trim();
		Frunction f = null;
		
		f = BooleanFrunction.parseLiteral(symbol, environment);
		if(f != null) return f;
		
		f = IntegerFrunction.parseLiteral(symbol, environment);
		if(f != null) return f;
		
		f = RealFrunction.parseLiteral(symbol, environment);
		if(f != null) return f;
		
		f = StringFrunction.parseLiteral(symbol, environment);
		if(f != null) return f;
		
		return null;
	}
	
	public NativeFrunction(Container environment)
	{
		super(environment, "");
	}
	
	// Native Frunctions should never need to be evaluated
	@Override public void evaluate(){}
	@Override public boolean isEvaluated(){return true;}

	// Native Frunctions have no source
	@Override public String getSource(){return null;}
	@Override public void setType(String type){} // Cannot change type
	@Override public abstract String getType();

}
