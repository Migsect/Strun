package net.samongi.frunction.error.runtime;

import net.samongi.frunction.frunction.Container;

public class MethodNotFoundError extends RunTimeError
{

  private static final long serialVersionUID = -891107163114309698L;
  
  private final String[] types;
  private final Container environment;
  
  public MethodNotFoundError(Container environment, String[] types)
  {
    this.types = types;
    this.environment = environment;
  }
  
  public String[] getTypes(){return this.types;}
  public Container getContainer(){return this.environment;}

}
