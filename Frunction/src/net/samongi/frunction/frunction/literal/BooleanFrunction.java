package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.binding.MethodBinding;
import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.DynamicFrunction;
import net.samongi.frunction.frunction.Frunction;

public class BooleanFrunction implements Frunction
{
	private static final String TYPE = "Boolean";
	private final Container environment;
	// This is a frunction that the boolean frunction wraps.
	private final Frunction wrapped;
	private final boolean state;
	
	public BooleanFrunction(Container environment, boolean state)
	{
		this.environment = environment;
		// Creating the frunction that the boolean frunction will wrap.
		this.wrapped = new DynamicFrunction(this.environment, "");
		
		// Setting the state of the boolean.
		this.state = state;
	}

	@Override public MethodBinding getMethod(String[] types, DynamicFrunction[] inputs)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override public SymbolBinding getSymbol(String symbol)
	{
		// There are no symbols under the boolean class.
		return null;
	}

	@Override public Container getEnvironment(){return this.environment;}

	@Override
	public void setType(String type)
	{
		// TODO Auto-generated method stub
		
	}

	@Override public String getType(){return BooleanFrunction.TYPE;}

	@Override
	public void evaluate()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isEvaluated()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Frunction evaluate(Container environment)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
