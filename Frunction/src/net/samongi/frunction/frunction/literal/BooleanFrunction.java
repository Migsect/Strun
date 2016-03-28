package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.binding.DynamicMethodBinding;
import net.samongi.frunction.binding.DynamicSymbolBinding;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.DynamicFrunction;

public class BooleanFrunction implements NativeFrunction
{
	
	private final Container environment;
	
	public BooleanFrunction(Container environment)
	{
		this.environment = environment;
	}

	@Override
	public DynamicMethodBinding getMethod(String[] types, DynamicFrunction[] inputs)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DynamicSymbolBinding getSymbol(String symbol)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Container getEnvironment()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setType(String type)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getType()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
