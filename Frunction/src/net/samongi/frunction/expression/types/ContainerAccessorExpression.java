package net.samongi.frunction.expression.types;

import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.tokens.SymbolToken;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.exceptions.SymbolNotFoundException;

public class ContainerAccessorExpression implements Expression
{
	
	private final Container left;
	private final SymbolToken token;
	
	public ContainerAccessorExpression(Container left, SymbolToken token)
	{
		this.left = left;
		this.token = token;
	}
	
	@Override public String getDisplay()
	{
		return "C<'" + token.getSource() + "': E" + ">"; 
	}
	
	@Override public Frunction evaluate(Container environment)
	{
		// Getting the symbol from the token
		String symbol = token.getSource();
    // Getting the binding associated with the symbol.
    SymbolBinding l_binding = null;
    
    try
    {
      l_binding = left.getSymbol(symbol);
    }
    catch (SymbolNotFoundException e)
    {
      e.displayError();
      return null;
    }
    
    // Getting the binded expression
    Expression l_expr;
    try{l_expr = l_binding.getExpression();}
    catch (TokenException e)
    {
      // TODO actual error here
      return null;
    }
    
    // We can now evaluate the expression.
    //   This is using the environment of the frunction it is apart of.
    System.out.println("  Left Expression: " + left.getClass().toGenericString());
    Frunction accessed = l_expr.evaluate(left);
    if(DEBUG) System.out.println("  A-Evaluate accessed_source: " + accessed.getSource());
    
    // Returning the accessed expression
    return accessed;
	}

}
