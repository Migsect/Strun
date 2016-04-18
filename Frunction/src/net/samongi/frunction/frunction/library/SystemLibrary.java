package net.samongi.frunction.frunction.library;

import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.error.syntax.SyntaxError;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.DynamicFrunction;
import net.samongi.frunction.frunction.Frunction;

public class SystemLibrary
{
  public FrunctionConstructor getConstructor()
  {
    return new FrunctionConstructor()
    {
      @Override public Frunction constructFrunction(Container environment) throws SyntaxError, RunTimeError
      {
        DynamicFrunction frunction = new DynamicFrunction(environment);
        
        return frunction;
      }
    };
  }
}
