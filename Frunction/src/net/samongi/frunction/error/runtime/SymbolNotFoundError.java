package net.samongi.frunction.error.runtime;

import net.samongi.frunction.frunction.Container;

public class SymbolNotFoundError extends RunTimeError
{
  private static final long serialVersionUID = -4459736847132831999L;

  private final String symbol;
  private final Container environment;

  public SymbolNotFoundError(Container environment, String symbol)
  {
    this.symbol = symbol;
    this.environment = environment;
  }

  public String getSymbol()
  {
    return this.symbol;
  }
  public Container getContainer()
  {
    return this.environment;
  }

  public void displayError()
  {
    System.out.println("Syntax Error! Symbol not found: '" + this.symbol + "'");
  }
}
