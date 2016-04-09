package net.samongi.frunction.parse;

public class Commenting
{
  private static final String LINE_COMMENT_STR = "#";
  private static final String LINE_COMMENT_REGEX = LINE_COMMENT_STR + ".*" + "($|\\r\\n|\\r|\\n)";

  private static final String INLINE_COMMENT_STR_OPEN = "#<";
  private static final String INLINE_COMMENT_STR_CLOSE = ">#";
  private static final String INLINE_COMMENT_REGEX = INLINE_COMMENT_STR_OPEN + ".*" + INLINE_COMMENT_STR_CLOSE;

  public static String removeComments(String text)
  {
    return Commenting.removeLineComments(Commenting.removeInlineComments(text));
  }

  /** Replaces all Line based comments. Line based comments are defined as all text between a hash and a newline
   * character
   * 
   * @param text
   * @return */
  public static String removeLineComments(String text)
  {
    return text.replaceAll(LINE_COMMENT_REGEX, "");
  }

  public static String removeInlineComments(String text)
  {
    return text.replaceAll(INLINE_COMMENT_REGEX, "");
  }
}
