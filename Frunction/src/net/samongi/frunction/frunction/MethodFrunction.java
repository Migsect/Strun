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

	// Expression is already evaluated since it is a native expression
	@Override
	public void evaluate(){}

	@Override public boolean isEvaluated(){return true;}
	@Override public void setType(String type){this.inner.setType(type);}

	@Override public String getType(){return this.inner.getType();}

	@Override
	public void addMethod(String[] types, MethodBinding binding){}

	@Override
	public void addSymbol(String symbol, SymbolBinding binding){}

	@Override public String getSource(){return null;}

}
