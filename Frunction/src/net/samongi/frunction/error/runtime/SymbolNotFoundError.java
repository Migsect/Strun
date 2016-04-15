package net.samongi.frunction.error.runtime;

public class SymbolNotFoundError extends RunTimeError
{
  private static final long serialVersionUID = -4459736847132831999L;

  private final String symbol;

  public SymbolNotFoundError(String symbol)
  {
    this.symbol = symbol;
  }

  public String getSymbol()
  {
    return this.symbol;
  }

  public void displayError()
  {
    System.out.println("Syntax Error! Symbol not found: '" + this.symbol + "'");
  }
}
