package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.DynamicFrunction;
import net.samongi.frunction.frunction.Frunction;

public abstract class NativeFrunction extends DynamicFrunction
{
	public NativeFrunction(Container environment)
	{
		super(environment, "");
	}
	
	// Native Frunctions should never need to be evaluated
	@Override public void evaluate(){}
	@Override public boolean isEvaluated(){return true;}
	
	// evaluate
	@Override public Frunction evaluate(Container environment){return this;}

	// Native Frunctions have no source
	@Override public String getSource(){return null;}
	@Override public void setType(String type){} // Cannot change type
	@Override public abstract String getType();

}
