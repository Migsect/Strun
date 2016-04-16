package net.samongi.frunction.exceptions.runtime;

import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.frunction.Frunction;

public class InvalidTypeException extends RunTimeError
{
  private static final long serialVersionUID = -6143663680877152359L;

  private final Frunction type;
  
  public InvalidTypeException(Frunction type)
  {
    this.type = type;
  }
  
  public Frunction getTriedType(){return this.type;}
}
