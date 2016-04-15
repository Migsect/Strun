package net.samongi.frunction.error.syntax;

public class UnexpectedTokenError extends TokenError
{
  private static final long serialVersionUID = 6701808902626381307L;

  public UnexpectedTokenError(String problem_source)
  {
    super(problem_source);
  }

}
