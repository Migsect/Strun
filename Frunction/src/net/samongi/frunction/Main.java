package net.samongi.frunction;

import java.io.File;

import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.error.runtime.SymbolNotFoundError;
import net.samongi.frunction.error.syntax.ExpressionError;
import net.samongi.frunction.error.syntax.SyntaxError;
import net.samongi.frunction.file.FileUtil;
import net.samongi.frunction.frunction.DynamicFrunction;
import net.samongi.frunction.parse.Commenting;
import net.samongi.frunction.parse.ParseUtil;

import net.samongi.frunction.error.FrunctionError;

public class Main
{

  public static void main(String[] args)
  {
    try {
      throw new FrunctionError(1234, 5);
    }
    catch (FrunctionError er) {
      System.out.println(er.getFormattedMessage());
    }
    
    
    if(args.length < 1) return;
    String file_loc = args[0];
    // System.out.println("Attempting to parse: " + file_loc);

    File file = new File(file_loc);
    String text_body = FileUtil.readFile(file);

    // System.out.println("___Raw Text:");
    // System.out.print(text_body);
    // System.out.println();
    // System.out.println();

    text_body = Commenting.removeComments(text_body);

    // System.out.println("___Removed Comments Text:");
    // System.out.print(text_body);
    // System.out.println();
    // System.out.println();

    // System.out.println("___Starting Parsing:");
    text_body = ParseUtil.removeNextLines(text_body);
    text_body = ParseUtil.squeeze(text_body);

    DynamicFrunction main_frunction = new DynamicFrunction(null, text_body);
    try
    {
      main_frunction.evaluate();
    }
    catch(SyntaxError | RunTimeError e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
      if(e instanceof ExpressionError)
      {
        System.out.println("Source : '" + ((ExpressionError) e).getSource() + "'");
      }
      if(e instanceof SymbolNotFoundError)
      {
        System.out.println("Symbol Not Found : '" + ((SymbolNotFoundError) e).getSymbol() + "'");
      }
    }

    // System.out.println("Displaying Mains Hierarchy:");
    // main_frunction.displayHierarchy(2);
  }

}
