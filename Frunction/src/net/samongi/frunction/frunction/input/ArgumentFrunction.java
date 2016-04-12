package net.samongi.frunction.frunction.input;

import net.samongi.frunction.exceptions.parsing.ParsingException;
import net.samongi.frunction.exceptions.runtime.RunTimeException;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.DynamicFrunction;

/**Used to wrap the input arguments passed in by command line
 * this will generally be binded to "Args" at the beginning of the program. 
 * The symbols "1","2", ... will return the first, second, and third arguments.
 * The (n) method will also return the nth argument.u r the best <3<3<3<3
 */
public class ArgumentFrunction extends DynamicFrunction
{

  public ArgumentFrunction(Container environment) throws ParsingException, RunTimeException
  {
    super(environment);
  }

}
