package net.samongi.frunction.binding;

public class Binding
{
	private static final String DEF_KEY = "_"; 
	private static final String BINDING_PUBLIC = ":";
	private static final String BINDING_PRIVATE = "|";
	
	public static Binding parseBinding(String text_section)
	{
		// Splitting the section based on the first bound binding operator
		String[] split_section = text_section.split("[(" + BINDING_PUBLIC + ")(" + BINDING_PRIVATE + ")]", 1);
		String key = null;
		String text = null;
		if(split_section.length < 2) // This means that the second expression isn't being binding to an actual key
		{
			key = DEF_KEY;
			text = split_section[0]; // The text is all there is.
		}
		else
		{
			key = split_section[0]; // Getting the first element of the split_section
			text = split_section[1]; // The text is second in this case
		}
		
		// Checking to see if the binding is private
		boolean is_private = false;
		for(int i = 0; i < text_section.length(); i++)
		{
			if(text_section.substring(i).startsWith(BINDING_PRIVATE))
			{
				is_private = true;
				break;
			}
		}
		if(key == null) return null;
		if(text == null) return null;
		
		return new Binding(key, text, is_private);
	}
	
	private final String key;
	private final String source;
	private final boolean is_private;
	
	// TODO evaluated binding to put here.
	
	public Binding(String key, String source, boolean is_private)
	{
		this.key = key;
		this.source = source;
		this.is_private = is_private;
	}
	
	public String getKey(){return this.key;}
	public String getSource(){return this.source;}
	public boolean isPrivate(){return this.is_private;}
	
}
