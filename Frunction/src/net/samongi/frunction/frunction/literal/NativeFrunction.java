package net.samongi.frunction.frunction.literal;

import net.samongi.frunction.frunction.Frunction;

/**Represents a native frunction to the runtime environment
 * This generally deals with literals.
 * 
 * @author Alex
 *
 */
public interface NativeFrunction extends Frunction
{
	@Override public default void evaluate(){}
	@Override public default boolean isEvaluated(){return true;}
}
