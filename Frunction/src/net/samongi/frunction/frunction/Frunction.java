package net.samongi.frunction.frunction;

import net.samongi.frunction.exceptions.parsing.ExpressionException;
import net.samongi.frunction.exceptions.parsing.ParsingException;
import net.samongi.frunction.exceptions.runtime.RunTimeException;
import net.samongi.frunction.expression.types.Expression;
import net.samongi.frunction.frunction.type.dictionary.TypeDictionary;

public interface Frunction extends Container
{
  /** Evaluates the frunction if it needs to be evaluated This will do nothing on native frunctions but on source-based
   * frunctions this will parse the source to generate the frunction 
   * @throws ExpressionException 
   * @throws RunTimeException 
   * @throws ParsingException */
  public void evaluate() throws ParsingException, RunTimeException;

  /** This will return the evaluation state of the frunction If the frunction is native then this will generally return
   * true
   * 
   * @return True if the frunction has been evaluated */
  public boolean isEvaluated();

  /** Sets the type of the frunction to the type specified
   * 
   * @param type A string representing the type to set the type to */
  public void setType(String type);

  /** Gets the type of the frunction.
   * 
   * @return A string representing the type of the frunction */
  public String getType();
  
  /**Will attempt to retrieve the frunction that determines the type.
   * This will return null if the type frunction is currently the frunction being called.
   * This can occur if the type frunction is the empty-type's frunction. 
   * 
   * @return The type frunction
   * @throws ParsingException
   * @throws RunTimeException
   */
  public default Frunction getTypeFrunction() throws ParsingException, RunTimeException
  {
    Frunction type_frunction = TypeDictionary.getInstance().getType(this.getType());
    if(type_frunction == this) return null;
    if(type_frunction.getType().equals(this.getType())) return null;
    return type_frunction;
  }
  
  /**Will clone the frunction and base it in the new specified environment
   * 
   * @param new_environment The new environment to base it in
   * @return The new frunction that has a new environment
   */
  public Frunction clone(Container new_environment) throws ParsingException, RunTimeException;
  
  /**Will check to see if there is an accessible symbol in the frunction.
   * This also includes the frunction's type.
   * 
   * @param symbol
   * @return
   * @throws ParsingException
   * @throws RunTimeException
   */
  public boolean hasAccessibleSymbol(String symbol) throws ParsingException, RunTimeException;

  /** Returns the source of this frunction If there is no source returned (null or empty) then the frunction might very
   * well be a native frunction
   * 
   * @return The source that was used to defined the frunction */
  public String getSource();

  /** Converts the frunction to an expression that will evaluate to the frunction
   * 
   * @return An expression that returns this frunction */
  public default Expression toExpression()
  {
    Frunction this_frunction = this;

    return new Expression()
    {
      @Override public String getDisplay()
      {
        return "FC";
      }

      @Override public Type getType()
      {
        return Expression.Type.FRUNCTION_CONVERSION;
      }

      @Override public Frunction evaluate(Container environment)
      {
        if(environment == null) throw new NullPointerException("'environment' was null");

        return this_frunction;
      }
    };
  }
}
