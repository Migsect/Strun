package net.samongi.frunction.expression.types;

import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.tokens.SymbolToken;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.exceptions.SymbolNotFoundException;

public class AccessorExpression implements Expression
{
  private final Expression left;
  private final SymbolToken token;
  
  public AccessorExpression(Expression left, SymbolToken token)
  {
    this.left = left;
    this.token = token;
  }
  
  @Override public Frunction evaluate(Container environment)
  {
    // Evaluating the left expression that is going to be accessed
    //   We are evaluating based on the current environment.
    Frunction l_frunction = left.evaluate(environment);
    
    // The symbol that is going to be expressed from the right expression.
    String symbol = token.getSource();
    // Getting the binding associated with the symbol.
    SymbolBinding l_binding = null;
    
    // Starting a loop, peeling back scopes as neccessary
    Container current_scope = l_frunction;
    try
    {
      l_binding = current_scope.getSymbol(symbol);
    }
    catch (SymbolNotFoundException e)
    {
      e.displayError();
      return null;
    }
    while(l_binding == null)
    {
      current_scope = current_scope.getEnvironment();
      // TODO actually make exceptions?
      if(current_scope == null) return null; // We have failed to evaluate this expression
      try
      {
        l_binding = current_scope.getSymbol(symbol);
      }
      catch (SymbolNotFoundException e)
      {
        e.displayError();
        return null;
      }
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
    Frunction accessed = l_expr.evaluate(l_frunction);
    
    // Returning the accessed expression
    return accessed;
  }
}
