package net.samongi.frunction.exceptions.parsing;

public class TokenException extends ParsingException
{
  private static final long serialVersionUID = 7302187024945500408L;

  private final String problem_source;

  public TokenException(String problem_source)
  {
    this.problem_source = problem_source;
  }

  public String getSource()
  {
    return this.problem_source;
  }

  public void printError()
  {
    System.out.println("Syntax Error found in:");
    System.out.println("  " + this.problem_source);
  }
}
