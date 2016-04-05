package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.binding.DynamicMethodBinding;
import net.samongi.frunction.binding.DynamicSymbolBinding;
import net.samongi.frunction.binding.MethodBinding;
import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.DynamicFrunction;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.exceptions.FrunctionNotEvaluatedException;
import net.samongi.frunction.frunction.exceptions.SymbolNotFoundException;
import net.samongi.frunction.frunction.literal.dictionary.LiteralDictionary;

public class BooleanFrunction extends NativeFrunction
{
  public static final String TRUE_LITERAL = "true";
  public static final String FALSE_LITERAL = "false";
  
	private static final String TYPE = "bool";
	
	
	public static Frunction parseLiteral(String symbol, Container environment)
	{
		if(symbol.toLowerCase().equals(TRUE_LITERAL)) return new BooleanFrunction(environment, true);
		if(symbol.toLowerCase().equals(FALSE_LITERAL)) return new BooleanFrunction(environment, false);
		return null;
		
	}
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
				try{return LiteralDictionary.getInstance().getSymbol(TRUE_LITERAL).getExpression().evaluate(environment);}
        catch (TokenException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
				return null;
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
			  try{return LiteralDictionary.getInstance().getSymbol(FALSE_LITERAL).getExpression().evaluate(environment);}
        catch (TokenException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
			  return null;
			}
		};
	}
	
	// This is a frunction that the boolean frunction wraps.
	private final boolean state;
	
	public BooleanFrunction(Container environment, boolean state)
	{
		super(environment);
		
		// Setting the state of the boolean.
		this.state = state;
		
		// Adding the methods
		try{this.addSymbol(this.methodEquals());}
		catch(FrunctionNotEvaluatedException e){e.printStackTrace();}
	}
	
	/**Will generate a method binding for determining if another method is equal.
	 * 
	 * @return
	 */
	private SymbolBinding methodEquals()
	{
	  // The frunction that will hold the method
		Frunction method_holder = new DynamicFrunction(this, null);
		try{method_holder.evaluate();} // We need to evaluate this
		catch (TokenException e1){}
		
		// Generating the first method
		String[] input_0 = new String[]{"other"};
		String[] types_0 = new String[]{"bool"};
		Expression condition_0 = BooleanFrunction.getTautology();
		Expression expression_0 = new Expression()
		{
			@Override public Frunction evaluate(Container environment)
			{
				// Getting the left argument which should be the "@" self binding.
				Frunction left = null;
				try{left = environment.getSymbol("^@").getExpression().evaluate(environment);}
				catch (TokenException e){return null;}
				catch (SymbolNotFoundException e){return null;}
				
				// Getting the right argument which should be the "other" argument as defined
				Frunction right = null;
				try{right = environment.getSymbol("other").getExpression().evaluate(environment);}
				catch (TokenException e){return null;}
        catch (SymbolNotFoundException e){return null;}
				
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
		try{method_holder.addMethod(method_0);}
		catch(FrunctionNotEvaluatedException e){e.printStackTrace();}
		
		// Creating the binding
		SymbolBinding binding = new DynamicSymbolBinding("eq", method_holder, this);
		
		return binding;
	}


	@Override public String getType(){return BooleanFrunction.TYPE;}

	public boolean getNative(){return this.state;}
}
