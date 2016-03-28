package net.samongi.frunction.frunction;

import net.samongi.frunction.binding.MethodBinding;
import net.samongi.frunction.binding.SymbolBinding;

/**A helper class that assists in the creation of simple methods.
 * These generally will have one (or more) methods to the binding but will not
 * have any symbols attached to them.
 * 
 * @author Alex
 *
 */
public class MethodFrunction implements Frunction
{
	
	private final Container environment;
	private final Frunction inner;
	
	public MethodFrunction(Container environment)
	{
		this.environment = environment;
		// This will also have an inner dynamic frunction
		this.inner = new DynamicFrunction(environment, "");
	}
	
	@Override
	public MethodBinding getMethod(String[] types, Frunction[] inputs){return this.inner.getMethod(types, inputs);}

	@Override
	public SymbolBinding getSymbol(String symbol){return null;}

	@Override
	public Container getEnvironment(){return this.environment;}

	@Override
	public Frunction evaluate(Container environment)
	{
		// TODO Auto-generated method stub
		return null;
	}

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
