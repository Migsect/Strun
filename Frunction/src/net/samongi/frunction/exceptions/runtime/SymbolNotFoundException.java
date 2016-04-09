package net.samongi.frunction.exceptions.runtime;

public class SymbolNotFoundException extends RunTimeException
{
  private static final long serialVersionUID = -4459736847132831999L;

  private final String symbol;

  public SymbolNotFoundException(String symbol)
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
