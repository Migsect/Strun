package net.samongi.frunction.expression.types;

import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.tokens.SymbolToken;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.exceptions.SymbolNotFoundException;

public class AccessorExpression implements Expression
{
  private static final boolean DEBUG = false;
  
  private final Expression left;
  private final SymbolToken token;
  
  private Container left_container = null;
  
  public AccessorExpression(Expression left, SymbolToken token)
  {
    this.left = left;
    this.token = token;
  }
  
  public AccessorExpression(Container left, SymbolToken token)
  {
    this.left_container = left;
    this.token = token;
    this.left = null;
  }
  
  @Override public String getDisplay()
  {
    if(this.left == null) return "A<'" + token.getSource() + "':E>"; 
    return "A<'" + token.getSource() + "':" + left.getDisplay() + ">"; 
  }
  
  @Override public Frunction evaluate(Container environment)
  {
    // System.out.println("  " + token.getSource());
    
    // Evaluating the left expression that is going to be accessed
    //   We are evaluating based on the current environment.
    if(this.left_container == null) this.left_container = left.evaluate(environment);
    //if(DEBUG) System.out.println("  A-Evaluate left_frunction_source: " + l_frunction.getSource());
    
    // The symbol that is going to be expressed from the right expression.
    String symbol = token.getSource();
    // Getting the binding associated with the symbol.
    SymbolBinding l_binding = null;
    
    try
    {
      l_binding = this.left_container.getSymbol(symbol);
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
    Frunction accessed = l_expr.evaluate(this.left_container);
    if(DEBUG) System.out.println("  A-Evaluate accessed_source: " + accessed.getSource());
    
    // Returning the accessed expression
    return accessed;
  }
}
