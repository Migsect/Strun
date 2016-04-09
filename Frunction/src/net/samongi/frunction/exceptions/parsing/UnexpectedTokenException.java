package net.samongi.frunction.exceptions.parsing;

public class UnexpectedTokenException extends TokenException
{
  private static final long serialVersionUID = 6701808902626381307L;

  public UnexpectedTokenException(String problem_source)
  {
    super(problem_source);
  }

}
