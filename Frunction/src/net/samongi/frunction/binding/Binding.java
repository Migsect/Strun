package net.samongi.frunction.binding;

import net.samongi.frunction.frunction.Container;

public interface Binding
{
	/**Returns the source of the binding
	 * 
	 * @return
	 */
	public String getSource();
	/**Returns the container of the binding
	 * 
	 * @return
	 */
	public Container getContainer();
	
}
