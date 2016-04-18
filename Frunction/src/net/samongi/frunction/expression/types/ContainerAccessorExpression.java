package net.samongi.frunction.expression.types;

import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.error.runtime.SymbolNotFoundError;
import net.samongi.frunction.error.syntax.SyntaxError;
import net.samongi.frunction.expression.tokens.SymbolToken;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;

public class ContainerAccessorExpression implements Expression
{
  private final SymbolToken token;

  public ContainerAccessorExpression(SymbolToken token)
  {
    if(token == null) throw new NullPointerException("'token' was null");

    this.token = token;
  }

  @Override public Type getType()
  {
    return Expression.Type.CONTAINER_ACCESSOR;
  }

  @Override public String getDisplay()
  {
    return "C<'" + token.getSource() + "': E>";
  }

  @Override public Frunction evaluate(Container environment) throws SyntaxError, RunTimeError
  {
    if(environment == null) throw new NullPointerException("'environment' was null");

    // Getting the symbol from the token
    String symbol = token.getSource();
    // Getting the binding associated with the symbol.
    SymbolBinding l_binding = environment.getSymbol(symbol);
    if(l_binding == null) throw new SymbolNotFoundError(environment, symbol);

    // Getting the binded expression

    // We can now evaluate the expression.
    // This is using the environment of the frunction it is apart of.
    Frunction accessed = l_binding.get(environment);
    if(DEBUG) System.out.println("  A-Evaluate accessed_source: " + accessed.getSource());
    if(accessed == null) throw new NullPointerException();

    // Returning the accessed expression
    return accessed;
  }

}
