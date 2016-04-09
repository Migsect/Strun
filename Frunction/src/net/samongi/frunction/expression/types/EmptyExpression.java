package net.samongi.frunction.expression.types;

import net.samongi.frunction.exceptions.parsing.ParsingException;
import net.samongi.frunction.exceptions.runtime.RunTimeException;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;

/**
 * 
 * @author Alex
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
