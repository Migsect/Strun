package net.samongi.frunction;

import java.io.File;

import net.samongi.frunction.exceptions.parsing.ExpressionException;
import net.samongi.frunction.exceptions.parsing.ParsingException;
import net.samongi.frunction.exceptions.runtime.RunTimeException;
import net.samongi.frunction.exceptions.runtime.SymbolNotFoundException;
import net.samongi.frunction.file.FileUtil;
import net.samongi.frunction.frunction.DynamicFrunction;
import net.samongi.frunction.frunction.literal.BooleanFrunction;
import net.samongi.frunction.parse.Commenting;
import net.samongi.frunction.parse.ParseUtil;

public class Main
{

  public static void main(String[] args)
  {
    if(args.length < 1) return;
    String file_loc = args[0];
    // System.out.println("Attempting to parse: " + file_loc);

    File file = new File(file_loc);
    String text_body = FileUtil.readFile(file);

    text_body = Commenting.removeComments(text_body);

    text_body = ParseUtil.removeNextLines(text_body);
    text_body = ParseUtil.squeeze(text_body);

    try
    {
      BooleanFrunction.getTypeDefiner().define();
    }
    catch(ParsingException | RunTimeException e)
    {
      e.printStackTrace();
    }
    
    DynamicFrunction main_frunction = new DynamicFrunction(null, text_body);
    try
    {
      main_frunction.evaluate();
    }
    catch(ParsingException | RunTimeException e)
    {
      e.printStackTrace();
      if(e instanceof ExpressionException)
      {
        System.out.println("Source : '" + ((ExpressionException) e).getSource() + "'");
      }
      if(e instanceof SymbolNotFoundException)
      {
        System.out.println("Symbol Not Found : '" + ((SymbolNotFoundException) e).getSymbol() + "'");
      }
    }

    // System.out.println("Displaying Mains Hierarchy:");
    // main_frunction.displayHierarchy(2);
  }

}
