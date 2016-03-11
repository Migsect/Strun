package net.samongi.frunction.parse;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParseTest
{
	@Test
	public void matchesAt_empty_0()
	{
		String input = ""; 
		boolean expected = true;
		
		boolean output = ParseUtil.matchesAt(input, 0, "");
		assertEquals(output, expected);
	}
	@Test
	public void matchesAt_includes_0()
	{
		String input = ""; 
		boolean expected = false;
		
		boolean output = ParseUtil.matchesAt(input, 0, "hello");
		assertEquals(output, expected);
	}
	@Test
	public void matchesAt_includes_1()
	{
		String input = "hello"; 
		boolean expected = true;
		
		boolean output = ParseUtil.matchesAt(input, 0, "hello");
		assertEquals(output, expected);
	}
	@Test
	public void matchesAt_includes_2()
	{
		String input = "hel"; 
		boolean expected = false;
		
		boolean output = ParseUtil.matchesAt(input, 0, "hello");
		assertEquals(output, expected);
	}
	@Test
	public void matchesAt_includes_3()
	{
		String input = "hello world"; 
		boolean expected = false;
		
		boolean output = ParseUtil.matchesAt(input, 1, "hello");
		assertEquals(output, expected);
	}
	@Test
	public void matchesAt_includes_4()
	{
		String input = "hello world"; 
		boolean expected = true;
		
		boolean output = ParseUtil.matchesAt(input, 0, "hello");
		assertEquals(output, expected);
	}
	
	@Test
	public void section_empty_0()
	{
		String input = ""; 
		String expected = "";
		
		String output = ParseUtil.getSection(input, 0, ";", null, null);
		assertEquals(expected, output);
	}
	@Test
	public void section_empty_1()
	{
		String input = ""; 
		String expected = "";
		
		String output = ParseUtil.getSection(input, 0, ";", "", "");
		assertEquals(expected, output);
	}
	@Test
	public void section_empty_2()
	{
		String input = ""; 
		String expected = "";
		
		String output = ParseUtil.getSection(input, 0, ";", "{", "}");
		assertEquals(expected, output);
	}
	
	@Test
	public void section_simple_0()
	{
		String input = "foo;bar"; 
		String expected = "foo;";
		
		String output = ParseUtil.getSection(input, 0, ";", null, null);
		assertEquals(expected, output);
	}
	@Test
	public void section_simple_1()
	{
		String input = "foo;bar"; 
		String expected = "bar";
		
		String output = ParseUtil.getSection(input, 4, ";", null, null);
		assertEquals(expected, output);
	}
}
