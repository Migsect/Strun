package net.samongi.frunction.parse;

import static org.junit.Assert.*;

import org.junit.Test;

public class CommentingTest
{

  @Test public void commenting_emptyString_0()
  {
    String input = "";
    String expected = "";

    String output = Commenting.removeComments(input);
    assertEquals(expected, output);
  }

  @Test public void commenting_lineComment_0()
  {
    String input = "# Hello World";
    String expected = "";

    String output = Commenting.removeComments(input);
    assertEquals(expected, output);
  }

  @Test public void commenting_lineComment_1()
  {
    String input = "From the other side # Hello World";
    String expected = "From the other side ";

    String output = Commenting.removeComments(input);
    assertEquals(expected, output);
  }

  @Test public void commenting_lineComment_2()
  {
    String input = "This # Hello World\nthat";
    String expected = "This that";

    String output = Commenting.removeComments(input);
    assertEquals(expected, output);
  }

  @Test public void commenting_lineComment_3()
  {
    String input = "This # Hello World\nthat #but not this";
    String expected = "This that ";

    String output = Commenting.removeComments(input);
    assertEquals(expected, output);
  }

  @Test public void commenting_lineComment_4()
  {
    String input = "This # Hello World\nthat #but not this\nand them";
    String expected = "This that and them";

    String output = Commenting.removeComments(input);
    assertEquals(expected, output);
  }

  @Test public void commenting_inlineComment_0()
  {
    String input = "#<Hello World>#";
    String expected = "";

    String output = Commenting.removeComments(input);
    assertEquals(expected, output);
  }

  @Test public void commenting_inlineComment_1()
  {
    String input = "Hello #<Ignore Me!>#World!";
    String expected = "Hello World!";

    String output = Commenting.removeComments(input);
    assertEquals(expected, output);
  }

  @Test public void commenting_bothComment_0()
  {
    String input = "Hello #<Ignore Me!>#World!#Ignore me also!";
    String expected = "Hello World!";

    String output = Commenting.removeComments(input);
    assertEquals(expected, output);
  }
}
