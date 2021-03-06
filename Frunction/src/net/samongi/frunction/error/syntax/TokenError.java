package net.samongi.frunction.error.syntax;

public class TokenError extends SyntaxError
{
  private static final long serialVersionUID = 7302187024945500408L;

  private final String problem_source;

  public TokenError(String problem_source)
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
