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

  @Override public Type getType(){return Expression.Type.FRUNCTION_ACCESSOR;}
  
  @Override public String getDisplay()
  {
    return "A<'" + token.getSource() + "':" + left.getDisplay() + ">";
  }

  @Override public Frunction evaluate(Container environment)
  {
    if(environment == null) throw new NullPointerException("'environment' was null");

    // Evaluate the left expression such that we can access its bindings
  	//System.out.println("Is left null?: " + (left == null));
    System.out.println("What is left's type?: " + left.getClass().getCanonicalName());
    Frunction eval = left.evaluate(environment);
    
    //System.out.println("Is eval null?: " + (eval == null));
    
    // The symbol that is going to be expressed from the right expression.
    String symbol = token.getSource();
    // Getting the binding associated with the symbol.
    SymbolBinding l_binding = null;
    try
    {
      l_binding = eval.getSymbol(symbol);
    }
    catch(SymbolNotFoundException e)
    {
      e.displayError();
      return null;
    }

    // We can now evaluate the expression.
    // This is using the environment of the frunction it is apart of.
    Frunction accessed = null;
    try
    {
      accessed = l_binding.get();
    }
    catch(TokenException e)
    {
      e.printStackTrace();
    }
    if(DEBUG)
      System.out.println("  A-Evaluate accessed_source: "
          + accessed.getSource());

    // Returning the accessed expression
    return accessed;
  }
}
