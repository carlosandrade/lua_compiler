public class Scanner{
    private char currentChar;
    
    //Kind and spelling of the current token:
    
    private byte currentKind;
    private StringBuffer currentSpelling;
    
    //Etc
    
    private SourceFile sourceFile;
    private boolean currentlyScanningToken;

    // take verifies and appends the current character to the current token, 
    //and gets the next character from the source program.

private void take (char expectedChar){
    if (currentChar == expectedChar)
    {
        currentSpelling.append(currentChar);
        currentChar = sourceFile.getSource();
    }else
        //report syntax error
    }
}

// takeIt appends the current character to the current token, and gets
// the next character from the source program.

private void takeIt(){
    if(currentlyScanningToken)
        currentSpelling.append(currentChar);
    currentChar = sourceFile.getSource()
    
}

// scanSeparator skips a single separator.

private void scanSeparator() {
  switch (currentChar) {
  case '!':
    {
      takeIt();
      while ((currentChar != SourceFile.EOL) && (currentChar != SourceFile.EOT))
        takeIt();
      if (currentChar == SourceFile.EOL)
        takeIt();
    }
    break;

  case ' ': case '\n': case '\r': case '\t':
    takeIt();
    break;
  }
}

private boolean isDigit(char c) {
  return (c >= '0' && c <= '9');
}

private boolean isLetter(char c) {
  return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
}

// isBinop and isUnop returns true iff the given character is an operator character.

private boolean isBinop(char c) {
    return (c == '+' || c == '-' || c == '*' || c == '/' ||
            c == '^' || c == '%' || c == '..' || c == '<' ||
            c == '<=' || c == '>' || c == '>=' || c == '==' || 
	        c == '~=' || c == 'and' || c == 'or');
  }
  
private boolean isUnop(char c){
    return (c == '-' || c == 'not' || c == '#');
}

///////////////////////////////////////////////////////////////////////////////

public Scanner(SourceFile source) {
  sourceFile = source;
  currentChar = sourceFile.getSource();
}

private byte scanToken(){
      switch (currentChar) {

      case 'a':  case 'b':  case 'c':  case 'd':  case 'e':
      case 'f':  case 'g':  case 'h':  case 'i':  case 'j':
      case 'k':  case 'l':  case 'm':  case 'n':  case 'o':
      case 'p':  case 'q':  case 'r':  case 's':  case 't':
      case 'u':  case 'v':  case 'w':  case 'x':  case 'y':
      case 'z':
      case 'A':  case 'B':  case 'C':  case 'D':  case 'E':
      case 'F':  case 'G':  case 'H':  case 'I':  case 'J':
      case 'K':  case 'L':  case 'M':  case 'N':  case 'O':
      case 'P':  case 'Q':  case 'R':  case 'S':  case 'T':
      case 'U':  case 'V':  case 'W':  case 'X':  case 'Y':
      case 'Z':  case: '_':
        takeIt();
        while (isLetter(currentChar) || isDigit(currentChar))
          takeIt();
        return Token.NAME;

      case '0':  case '1':  case '2':  case '3':  case '4':
      case '5':  case '6':  case '7':  case '8':  case '9':
        takeIt();
        while (isDigit(currentChar))
          takeIt();
        return Token.INTLITERAL;

      case '+':  case '-':  case '*': case '/':  case '^':
      case '%':  case '..':  case '<':  case '<=':  case '>':
      case '>=':  case '==':  case '~=': case 'and' case 'or'
        takeIt();
        while (isBinop(currentChar))
          takeIt();
        return Token.BINOP;
        
        case '-':  case 'not':  case '#':
          takeIt();
          while (isBinop(currentChar))
            takeIt();
          return Token.UNOP;

      case '\'':
        takeIt();
        takeIt(); // the quoted character
        if (currentChar == '\'') {
        	takeIt();
          return Token.CHARLITERAL;
        } else
          return Token.ERROR;

      case '.':
        takeIt();
        return Token.DOT;

      case ':':
        takeIt();
        if (currentChar == '=') {
          takeIt();
          return Token.BECOMES;
        } else
          return Token.COLON;

      case ';':
        takeIt();
        return Token.SEMICOLON;

      case ',':
        takeIt();
        return Token.COMMA;

      case '~':
        takeIt();
        return Token.IS;

      case '(':
        takeIt();
        return Token.LPAREN;

      case ')':
        takeIt();
        return Token.RPAREN;

      case '[':
        takeIt();
        return Token.LBRACKET;

      case ']':
        takeIt();
        return Token.RBRACKET;

      case '{':
        takeIt();
        return Token.LCURLY;

      case '}':
        takeIt();
        return Token.RCURLY;

      case SourceFile.EOT:
        return Token.EOT;

      default:
        takeIt();
        return Token.ERROR;
      }
    }

}

public Token scan(){
     Token tok;
      SourcePosition pos;
      int kind;

      currentlyScanningToken = false;
      while (currentChar == '!'
             || currentChar == ' '
             || currentChar == '\n'
             || currentChar == '\r'
             || currentChar == '\t')
        scanSeparator();

      currentlyScanningToken = true;
      currentSpelling = new StringBuffer("");
      pos = new SourcePosition();
      pos.start = sourceFile.getCurrentLine();

      kind = scanToken();

      pos.finish = sourceFile.getCurrentLine();
      tok = new Token(kind, currentSpelling.toString(), pos);
      if (debug)
        System.out.println(tok);
      return tok;
    }

}
}