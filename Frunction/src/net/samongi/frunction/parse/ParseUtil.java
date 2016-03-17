package net.samongi.frunction.parse;

public class ParseUtil
{
	
	/**Will iterate through the text and return a string which is a section
	 * of the original string up until the pattern is found.
	 * This takes into account scoping up and down (given they exist)
	 * 
	 * This will include the seperator in the section.
	 * 
	 * If there is a scope (determined by scoping up and down) then seperators
	 * will not be discovered in the scope.
	 * 
	 * @param text The text to get a section of
	 * @param start The index at which to start the sectioning
	 * @param seperator The seperator to look for
	 * @param scope_up Will scope up if this is matched.
	 * @param scope_down Will scope down if this is matched
	 * @return The section found
	 */
	public static String getSection(String text, int start, String seperator, String[] scope_up, String[] scope_down)
	{
		// Checking to see if we have any scope up methods
		boolean has_scope_up = scope_up != null &&  scope_up.length > 0;
		boolean has_scope_down = scope_down != null && scope_down.length > 0;;
		
		int scope = 0; // The scope incrementor
		for(int i = start; i < text.length(); i++)
		{
			if(has_scope_up && ParseUtil.matchesAt(text, i, scope_up)) scope++;
			if(has_scope_down && ParseUtil.matchesAt(text, i, scope_down)) scope--;
			if(ParseUtil.matchesAt(text, i, seperator) && scope == 0)
			{
				int end = i + seperator.length(); 
				return text.substring(start, end);
			}
		}
		
		// TODO scoping exceptions?
		// May make another method to test for scoping.
		
		return text.substring(start);
	}
	/**Gets section for a singular string.
	 * 
	 * @param text
	 * @param start
	 * @param seperator
	 * @param scope_up
	 * @param scope_down
	 * @return
	 */
	public static String getSection(String text, int start, String seperator, String scope_up, String scope_down)
	{
		return ParseUtil.getSection(text, start, seperator, new String[]{scope_up}, new String[]{scope_down});
	}
	/**Reduced and simplified form of getSection
	 * 
	 * @param text
	 * @param start
	 * @param seperator
	 * @return
	 */
	public static String getSection(String text, int start, String seperator)
	{
		return ParseUtil.getSection(text, start, seperator, new String[0], new String[0]);
	}
	
	/**Tests to see if the index of the string matches the pattern
	 * This goes character by character for matching.
	 * 
	 * @param text The text to match at
	 * @param start The index to start the match at
	 * @param pattern The pattern to test the match against
	 * @return
	 */
	public static boolean matchesAt(String text, int start, String pattern)
	{
		// Iterating through the pattern.
		if(pattern.length() == 0) return true;
		if(text.length() - start < pattern.length()) return false; // remaining is less than the pattern
		for(int i = 0; i < pattern.length(); i++)
		{
			int text_i = start + i; // The text index to compare against
			if(text_i >= text.length()) return false;
			if(pattern.charAt(i) != text.charAt(text_i)) return false;
		}
		return true;
	}
	public static boolean matchesAt(String text, int start, String[] patterns)
	{
		for(String s : patterns) if(matchesAt(text, start, s)) return true;
		return false;
	}
}
