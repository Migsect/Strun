package net.samongi.frunction.expression.types;

import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.tokens.SymbolToken;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.exceptions.SymbolNotFoundException;

public class FrunctionAccessorExpression implements Expression
{
  private static final boolean DEBUG = false;
  
  private final Expression left;
  private final SymbolToken token;
  
  public FrunctionAccessorExpression(Expression left, SymbolToken token)
  {
    this.left = left;
    this.token = token;
  }
  
  @Override public String getDisplay()
  {
    return "A<'" + token.getSource() + "':" + left.getDisplay() + ">"; 
  }
  
  @Override public Frunction evaluate(Container environment)
  {
    System.out.println("  AccessorExpression: " + this.getDisplay());
    if(this.left == null) System.out.println("    Left Expression: null expression");
    else System.out.println("    Left Expression: " + this.left.getDisplay());
    
    // Evaluating the left expression that is going to be accessed
    //   We are evaluating based on the current environment.
    // What happens if the left expression is the same 
    Frunction eval = left.evaluate(environment);
    
    //if(DEBUG) System.out.println("  A-Evaluate left_frunction_source: " + l_frunction.getSource());
    
    // The symbol that is going to be expressed from the right expression.
    String symbol = token.getSource();
    // Getting the binding associated with the symbol.
    SymbolBinding l_binding = null;
    
    try
    {
      l_binding = eval.getSymbol(symbol);
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
    Frunction accessed = l_expr.evaluate(eval);
    if(DEBUG) System.out.println("  A-Evaluate accessed_source: " + accessed.getSource());
    
    // Returning the accessed expression
    return accessed;
  }
}
