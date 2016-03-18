package net.samongi.frunction.parse;

import static org.junit.Assert.*;
import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.tokens.GroupToken;
import net.samongi.frunction.expression.tokens.Token;

import org.junit.Test;

public class TokenTest
{
  @Test
  public void empty_tokens_0()
  {
    String input = ""; 
    Token.Type[] expected = new Token.Type[]{};
    
    GroupToken parsed = new GroupToken(input);
    Token.Type[] output = null;
    try{output = Token.arrayToTypeArray(parsed.getTokens()); }
    catch (TokenException e)
    {
      e.printStackTrace();
      fail();
    }
    
    assertArrayEquals(expected, output);
  }
  
  @Test
  public void symbol_token_0()
  {
    String input = "symbol"; 
    Token.Type[] expected = new Token.Type[]{Token.Type.SYMBOL};
    
    GroupToken parsed = new GroupToken(input);
    Token.Type[] output = null;
    try{output = Token.arrayToTypeArray(parsed.getTokens()); }
    catch (TokenException e)
    {
      e.printStackTrace();
      fail();
    }
    
    assertArrayEquals(expected, output);
  }
  @Test
  public void symbol_token_1()
  {
    String input = "symbol.symbol"; 
    Token.Type[] expected = new Token.Type[]{Token.Type.SYMBOL, Token.Type.ACCESSOR, Token.Type.SYMBOL};
    
    GroupToken parsed = new GroupToken(input);
    Token.Type[] output = null;
    try{output = Token.arrayToTypeArray(parsed.getTokens()); }
    catch (TokenException e)
    {
      e.printStackTrace();
      fail();
    }
    
    assertArrayEquals(expected, output);
  }
  @Test
  public void symbol_token_2()
  {
    String input = "symbol.symbol.sym_bol"; 
    Token.Type[] expected = new Token.Type[]{Token.Type.SYMBOL, Token.Type.ACCESSOR, Token.Type.SYMBOL, Token.Type.ACCESSOR, Token.Type.SYMBOL};
    
    GroupToken parsed = new GroupToken(input);
    Token.Type[] output = null;
    try{output = Token.arrayToTypeArray(parsed.getTokens()); }
    catch (TokenException e)
    {
      e.printStackTrace();
      fail();
    }
    
    assertArrayEquals(expected, output);
  }
  
  @Test
  public void input_token_0()
  {
    String input = "()"; 
    Token.Type[] expected = new Token.Type[]{Token.Type.INPUT};
    
    GroupToken parsed = new GroupToken(input);
    Token.Type[] output = null;
    try{output = Token.arrayToTypeArray(parsed.getTokens()); }
    catch (TokenException e)
    {
      e.printStackTrace();
      fail();
    }
    
    assertArrayEquals(expected, output);
  }
  @Test
  public void input_token_1()
  {
    String input = "(hello_world)"; 
    Token.Type[] expected = new Token.Type[]{Token.Type.INPUT};
    
    GroupToken parsed = new GroupToken(input);
    Token.Type[] output = null;
    try{output = Token.arrayToTypeArray(parsed.getTokens()); }
    catch (TokenException e)
    {
      e.printStackTrace();
      fail();
    }
    
    assertArrayEquals(expected, output);
  }
  @Test
  public void input_token_2()
  {
    String input = "(actual,form)"; 
    Token.Type[] expected = new Token.Type[]{Token.Type.INPUT};
    
    GroupToken parsed = new GroupToken(input);
    Token.Type[] output = null;
    try{output = Token.arrayToTypeArray(parsed.getTokens()); }
    catch (TokenException e)
    {
      e.printStackTrace();
      fail();
    }
    
    assertArrayEquals(expected, output);
  }
  @Test
  public void input_token_3()
  {
    String input = "(foo(a),bar(b))"; 
    Token.Type[] expected = new Token.Type[]{Token.Type.INPUT};
    
    GroupToken parsed = new GroupToken(input);
    Token.Type[] output = null;
    try{output = Token.arrayToTypeArray(parsed.getTokens()); }
    catch (TokenException e)
    {
      e.printStackTrace();
      fail();
    }
    
    assertArrayEquals(expected, output);
  }
  @Test
  public void input_token_4()
  {
    String input = "real(foo(a),bar(b))"; 
    Token.Type[] expected = new Token.Type[]{Token.Type.SYMBOL, Token.Type.INPUT};
    
    GroupToken parsed = new GroupToken(input);
    Token.Type[] output = null;
    try{output = Token.arrayToTypeArray(parsed.getTokens()); }
    catch (TokenException e)
    {
      e.printStackTrace();
      fail();
    }
    
    assertArrayEquals(expected, output);
  }
  @Test
  public void input_token_5()
  {
    String input = "real(foo(a),bar(b)).then(this)"; 
    Token.Type[] expected = new Token.Type[]{Token.Type.SYMBOL, Token.Type.INPUT, Token.Type.ACCESSOR, Token.Type.SYMBOL, Token.Type.INPUT};
    
    GroupToken parsed = new GroupToken(input);
    Token.Type[] output = null;
    try{output = Token.arrayToTypeArray(parsed.getTokens()); }
    catch (TokenException e)
    {
      e.printStackTrace();
      fail();
    }
    
    assertArrayEquals(expected, output);
  }
  @Test
  public void input_token_6()
  {
    String input = "real({},{})"; 
    Token.Type[] expected = new Token.Type[]{Token.Type.SYMBOL, Token.Type.INPUT};
    
    GroupToken parsed = new GroupToken(input);
    Token.Type[] output = null;
    try{output = Token.arrayToTypeArray(parsed.getTokens()); }
    catch (TokenException e)
    {
      e.printStackTrace();
      fail();
    }
    
    assertArrayEquals(expected, output);
  }
  
  @Test
  public void frunction_token_0()
  {
    String input = "{}"; 
    Token.Type[] expected = new Token.Type[]{Token.Type.FRUNCTION};
    
    GroupToken parsed = new GroupToken(input);
    Token.Type[] output = null;
    try{output = Token.arrayToTypeArray(parsed.getTokens()); }
    catch (TokenException e)
    {
      e.printStackTrace();
      fail();
    }
    
    assertArrayEquals(expected, output);
  }
  @Test
  public void frunction_token_1()
  {
    String input = "{}.symb"; 
    Token.Type[] expected = new Token.Type[]{Token.Type.FRUNCTION, Token.Type.ACCESSOR, Token.Type.SYMBOL};
    
    GroupToken parsed = new GroupToken(input);
    Token.Type[] output = null;
    try{output = Token.arrayToTypeArray(parsed.getTokens()); }
    catch (TokenException e)
    {
      e.printStackTrace();
      fail();
    }
    
    assertArrayEquals(expected, output);
  }
  @Test
  public void frunction_token_2()
  {
    String input = "{a:hi;b:hello;}"; 
    Token.Type[] expected = new Token.Type[]{Token.Type.FRUNCTION};
    
    GroupToken parsed = new GroupToken(input);
    Token.Type[] output = null;
    try{output = Token.arrayToTypeArray(parsed.getTokens()); }
    catch (TokenException e)
    {
      e.printStackTrace();
      fail();
    }
    
    assertArrayEquals(expected, output);
  }
  @Test
  public void frunction_token_3()
  {
    String input = "{symb: 5.add(6)}.symb"; 
    Token.Type[] expected = new Token.Type[]{Token.Type.FRUNCTION, Token.Type.ACCESSOR, Token.Type.SYMBOL};
    
    GroupToken parsed = new GroupToken(input);
    Token.Type[] output = null;
    try{output = Token.arrayToTypeArray(parsed.getTokens()); }
    catch (TokenException e)
    {
      e.printStackTrace();
      fail();
    }
    
    assertArrayEquals(expected, output);
  }
  
  
  @Test
  public void symbol_token_source_0()
  {
    String input = "stuff";
    String expected = "stuff";
    
    GroupToken parsed = new GroupToken(input);
    String output = null;
    try
    {
      output = parsed.getTokens()[0].getSource();
    }
    catch (TokenException e)
    {
      e.printStackTrace();
      fail();
    }
    
    assertEquals(expected, output);
  }
  
  
  @Test
  public void accessor_token_source_0()
  {
    String input = ".";
    String expected = ".";
    
    GroupToken parsed = new GroupToken(input);
    String output = null;
    try
    {
      output = parsed.getTokens()[0].getSource();
    }
    catch (TokenException e)
    {
      e.printStackTrace();
      fail();
    }
    
    assertEquals(expected, output);
  }
  
  
  @Test
  public void group_token_source_0()
  {
    String input = "stuff";
    String expected = "stuff";
    
    GroupToken parsed = new GroupToken(input);
    String output = null;
    output = parsed.getSource();
    
    assertEquals(expected, output);
  }
  
  
  @Test
  public void input_token_source_0()
  {
    String input = "()";
    String expected = "";
    
    GroupToken parsed = new GroupToken(input);
    String output = null;
    try
    {
      output = parsed.getTokens()[0].getSource();
    }
    catch (TokenException e)
    {
      e.printStackTrace();
      fail();
    }
    
    assertEquals(expected, output);
  }
  @Test
  public void input_token_source_1()
  {
    String input = "(inner stuff)";
    String expected = "inner stuff";
    
    GroupToken parsed = new GroupToken(input);
    String output = null;
    try
    {
      output = parsed.getTokens()[0].getSource();
    }
    catch (TokenException e)
    {
      e.printStackTrace();
      fail();
    }
    
    assertEquals(expected, output);
  }
  
  
  @Test
  public void frunction_token_source_0()
  {
    String input = "{}";
    String expected = "";
    
    GroupToken parsed = new GroupToken(input);
    String output = null;
    try
    {
      output = parsed.getTokens()[0].getSource();
    }
    catch (TokenException e)
    {
      e.printStackTrace();
      fail();
    }
    
    assertEquals(expected, output);
  }
  @Test
  public void frunction_token_source_1()
  {
    String input = "{inner stuff}";
    String expected = "inner stuff";
    
    GroupToken parsed = new GroupToken(input);
    String output = null;
    try
    {
      output = parsed.getTokens()[0].getSource();
    }
    catch (TokenException e)
    {
      e.printStackTrace();
      fail();
    }
    
    assertEquals(expected, output);
  }
}
