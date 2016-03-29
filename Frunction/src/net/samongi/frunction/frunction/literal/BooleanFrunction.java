package net.samongi.frunction.frunction.literal;

import java.util.List;

import net.samongi.frunction.binding.DynamicMethodBinding;
import net.samongi.frunction.binding.DynamicSymbolBinding;
import net.samongi.frunction.binding.MethodBinding;
import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.DynamicFrunction;
import net.samongi.frunction.frunction.Frunction;

public class BooleanFrunction implements Frunction
{
	private static final String TYPE = "Boolean";
	
	/**Returns an expression that is a tautology which means
	 * it will always return true.
	 * This expression returns a boolean frunction that is true
	 * more specifically.
	 * 
	 * @return
	 */
	public static Expression getTautology()
	{
		return new Expression()
		{
			@Override public Frunction evaluate(Container environment)
			{
				return new BooleanFrunction(environment, true);
			}
		};
	}
	/**Returns an expression that is a contradiction
	 * This means it will always return false.
	 * This expression returns a boolean frunction that is false
	 * more specifically.
	 * 
	 * @return
	 */
	public static Expression getContradiction()
	{
		return new Expression()
		{
			@Override public Frunction evaluate(Container environment)
			{
				return new BooleanFrunction(environment, false);
			}
		};
	}
	
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
		
		// Adding the methods
		this.addSymbol(this.methodEquals());
	}
	
	/**Will generate a method binding for determining if another method is equal.
	 * 
	 * @return
	 */
	private SymbolBinding methodEquals()
	{
		Frunction method_holder = new DynamicFrunction(this, null);
		
		// Generating the first method
		String[] input_0 = new String[]{"other"};
		String[] types_0 = new String[]{"Boolean"};
		Expression condition_0 = BooleanFrunction.getTautology();
		Expression expression_0 = new Expression()
		{
			@Override public Frunction evaluate(Container environment)
			{
				// Getting the left argument which should be the "@" self binding.
				Frunction left = null;
				try{left = environment.getSymbol("@").getExpression().evaluate(environment);}
				catch (TokenException e){return null;}
				
				// Getting the right argument which should be the "other" argument as defined
				Frunction right = null;
				try{right = environment.getSymbol("other").getExpression().evaluate(environment);}
				catch (TokenException e){return null;}
				
				if(!left.getType().equals(BooleanFrunction.TYPE) || !(left instanceof BooleanFrunction))
				{
					return null; // We should technically never get to this stage.
				}
				if(!right.getType().equals(BooleanFrunction.TYPE) || !(right instanceof BooleanFrunction))
				{
					return null; // We should technically never get to this stage.
				}
				BooleanFrunction b_left = (BooleanFrunction) left;
				BooleanFrunction b_right = (BooleanFrunction) right;
				// Performing the native operation.
				return new BooleanFrunction(environment, b_left.getNative() == b_right.getNative());
			}
			
		};
		MethodBinding method_0 = new DynamicMethodBinding(this, input_0, types_0, condition_0, expression_0);
		method_holder.addMethod(method_0);
		
		// Creating the binding
		SymbolBinding binding = new DynamicSymbolBinding("eq", method_holder, this);
		
		return binding;
	}

	@Override public MethodBinding getMethod(String[] types, Frunction[] inputs){return this.inner.getMethod(types, inputs);}

	@Override public SymbolBinding getSymbol(String symbol){return this.inner.getSymbol(symbol);}

	@Override public Container getEnvironment(){return this.environment;}

	@Override public void setType(String type){} // Cannot change type

	@Override public String getType(){return BooleanFrunction.TYPE;}

	@Override public void evaluate(){}

	@Override public boolean isEvaluated(){return true;}

	@Override public Frunction evaluate(Container environment){return this;}
	
	public boolean getNative(){return this.state;}

	@Override public void addMethod(MethodBinding binding){this.inner.addMethod(binding);}
	@Override public void addSymbol(SymbolBinding binding){} // Do nothing
	@Override public String getSource(){return null;}
	@Override public List<MethodBinding> getMethods(){return this.inner.getMethods();}
	@Override public List<SymbolBinding> getSymbols(){return this.inner.getSymbols();}
}
