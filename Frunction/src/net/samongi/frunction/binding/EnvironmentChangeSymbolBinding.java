package net.samongi.frunction.binding;

import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.error.syntax.SyntaxError;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;

public class EnvironmentChangeSymbolBinding extends DynamicSymbolBinding
{ 
  private final Container new_environment;
  
  public EnvironmentChangeSymbolBinding(DynamicSymbolBinding binding, Container new_environment)
  {
    super(binding);
    
    this.new_environment = new_environment;
  }
  
  // This will basically clone the frunction returned and instead set it as a part of the new environment
  @Override public Frunction get(Container environment) throws SyntaxError, RunTimeError
  {
    return super.get(environment).clone(this.new_environment);
  }

}
