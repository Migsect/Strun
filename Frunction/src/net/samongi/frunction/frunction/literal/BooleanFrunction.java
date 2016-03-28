package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.binding.MethodBinding;
import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.DynamicFrunction;
import net.samongi.frunction.frunction.Frunction;

public class BooleanFrunction implements Frunction
{
	private static final String TYPE = "Boolean";
	private final Container environment;
	// This is a frunction that the boolean frunction wraps.
	private final Frunction inner;
	private final boolean state;
	
	public BooleanFrunction(Container environment, boolean state)
	{
		this.environment = environment;
		// Creating the frunction that the boolean frunction will wrap.
		this.inner = new DynamicFrunction(this.environment, "");
		
		// Setting the state of the boolean.
		this.state = state;
	}
	
	/**Will generate a method binding for determining if another method is equal.
	 * 
	 * @return
	 */
	private SymbolBinding methodEquals()
	{
		Expression expression = new Expression(){

			@Override public Frunction evaluate(Container environment)
			{
				// TODO Auto-generated method stub
				return null;
			}
			
		};
		
		return null;
	}

	@Override public MethodBinding getMethod(String[] types, Frunction[] inputs){return this.inner.getMethod(types, inputs);}

	@Override public SymbolBinding getSymbol(String symbol){return this.inner.getSymbol(symbol);}

	@Override public Container getEnvironment(){return this.environment;}

	@Override public void setType(String type){} // Cannot change coolean type

	@Override public String getType(){return BooleanFrunction.TYPE;}

	@Override public void evaluate(){}

	@Override public boolean isEvaluated(){return true;}

	@Override public Frunction evaluate(Container environment){return this;}
	
	public boolean getNative(){return this.state;}

}
