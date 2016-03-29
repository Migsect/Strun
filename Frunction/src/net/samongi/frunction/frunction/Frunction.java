package net.samongi.frunction.frunction;

import net.samongi.frunction.expression.types.Expression;

public interface Frunction extends Container, Expression
{
	/**Evaluates the frunction if it needs to be evaluated
	 * This will do nothing on native frunctions but on source-based frunctions
	 * this will parse the source to generate the frunction
	 */
	public void evaluate();
	/**This will return the evaluation state of the frunction
	 * If the frunction is native then this will generally return true
	 * 
	 * @return True if the frunction has been evaluated
	 */
	public boolean isEvaluated();
	/**Sets the type of the frunction to the type specified
	 * 
	 * @param type A string representing the type to set the type to
	 */
	public void setType(String type);
	/**Gets the type of the frunction.
	 * 
	 * @return A string representing the type of the frunction
	 */
	public String getType();
	/**Returns the source of this frunction
	 * If there is no source returned (null or empty) then the frunction might
	 * very well be a native frunction
	 * 
	 * @return The source that was used to defined the frunction
	 */
	public String getSource();
}
