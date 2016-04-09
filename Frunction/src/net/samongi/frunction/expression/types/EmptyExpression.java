package net.samongi.frunction.expression.types;

import net.samongi.frunction.exceptions.parsing.ParsingException;
import net.samongi.frunction.exceptions.runtime.RunTimeException;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;

/**At the moment this class is a theoretical one
 * May not be used
 * meant to return from empty inputs for the expression parse
 * but chose not to yet since that would allow empty expressions (which have no purpose yet)
 *
 */
public class EmptyExpression implements Expression
{

  @Override public Frunction evaluate(Container environment) throws ParsingException, RunTimeException
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override public Type getType()
  {
    // TODO Auto-generated method stub
    return null;
  }

}
