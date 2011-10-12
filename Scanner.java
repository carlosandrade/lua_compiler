/*

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

private boolean isInt(char c) {
  return (c >= '0' && c <= '9');
}

private boolean isLetter(char c) {
  return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c=='_');
}
*/

///////////////////////////////////////////////////////////////////////////////

public class Scanner{
    private char currentChar;
    
    //Kind and spelling of the current token:
    
    //private byte currentKind;
    private StringBuffer currentSpelling;
    
    //Etc
    
    private SourceFile sourceFile;
    private boolean currentlyScanningToken;

    // take verifies and appends the current character to the current token, 
    //and gets the next character from the source program.



public Scanner(SourceFile source) {
  sourceFile = source;
  currentChar = sourceFile.getSource();
}

// takeIt appends the current character to the current token, and gets
// the next character from the source program.

private void takeIt(){
    if(currentlyScanningToken)
        currentSpelling.append(currentChar);
    currentChar = sourceFile.getSource()
    
}

private void take (char expectedChar){
    if (currentChar == expectedChar)
    {
        currentSpelling.append(currentChar);
        currentChar = sourceFile.getSource();
    }else
        //report syntax error
    }
}

//erros sao definidos aqui em producoes que nao foram modularizadas e nos metodos que foram construidos
private int scanToken()
{int kind;
    
    if(isNameStarter(currentChar)) //NAME::= ('a'..'z'|'A'..'Z'|'_')('a'..'z'|'A'..'Z'|'_'|'0'..'9')*
        return scanName();
    else if(isIntStarter(currentChar)) //Comeca a verificacao de ser INT, FLOAT ou EXP
    {
        takeIt(); //('0'..'9')
        if(currentChar == 'x')
            return scanHex(); //Starter de Hex '0x'
        while(isIntStarter()) //('0'..'9')*
            takeIt();
        kind = Token.INT;
        if(currentChar == '.') //FLOAT::= INT '.' INT
        {
            takeIt();
            if(isIntStarter(currentChar))
                scanInt();
            else
                return Token.ERROR;
            kind = Token.FLOAT;
        }
        switch(currentChar) //Verifica se EXP::= (INT| FLOAT) ('E'|'e') (('-')|&) INT;
        {
            case 'E':
            case 'e':
                takeIt();
                if(currentChar == '-') //Se possui '-' opcional
                    takeIt();
                if(isIntStarter(currentChar)) //Producao deve Concanetar com INT para formar EXP
                {
                    scanInt(); 
                    return Token.EXP; 
                }
                else
                    return Token.ERROR; //Producao nao concatenou com INT
            default:
                    return kind; //Token = Int, ou Token = Float
        }
    }
    else if(currentChar == '\\')
        return scanNormalString();
    else if(currentChar == '\'')
        return scanCharString();
    else if(isWsStarter(currentChar))
        return scanWs();
    else if(isNewlineStarter(currentChar))
        return scanNewline();
    switch(currentChar)
    {
        case '+':  
          takeIt();
          return Token.PLUS;
        case '-':
          takeIt();
          if(currentChar == '-')
          {
              takeIt();
              if(currentChar == '[')
                  return scanComment();
              else
                  return scanLinecomment();
          }
          else
              return Token.MINUS;
        case '*': 
          takeIt();
          return Token.TIMES;
        case '/':  
          takeIt();
          return Token.RBAR;
        case '^':
          takeIt();
          return Token.CHAPEU;
        case '%':  
          takeIt();
          return Token.PERCENT;
        case '..': 
          takeIt(); 
          return Token.TWODOTES;
        case '<':  
          takeIt();
          return Token.LESSTHAN;
        case '<=':  
          takeIt();
          return Token.LESSOREQUALTHAN;
        case '>':
          takeIt();
          return Token.MORETHAN;
        case '>=':  
          takeIt();
          return Token.MOREOREQUALTHAN;
          /*como e feito um becomes da triangle, `:=` vamos precisar disso para as outras partes
                  case ':':
                    takeIt();
                    if (currentChar == '=') {
                      takeIt();
                      return Token.BECOMES;
                    } else
                      return Token.COLON;
          */
        case '=':
          takeIt();
          if(currentChar == '=')
          {
              takeIt();
              return Token.EQUALS;
          }
          else
              return Token.BECOMES;
        case '~':
          takeIt();
          if(currentChar == '=')
          {
              takeIt();
              return Token.NOTEQUALS;
          }
          else
            return Token.ERROR;
        case 'and': 
          takeIt();
          return Token.AND;
        case 'or':
          takeIt();
          return Token.OR;
        case 'not':
          takeit();
          return Token.NOT;
        case '#':
          takeIt();
          return Token.VELHA;
        case '.':
          takeIt();
          return Token.DOT;
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
          if(currentChar == '=')
              return scanLongstring();
          else
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
public Token scan(){
     Token tok;
      SourcePosition pos;
      int kind;

      currentlyScanningToken = false;
      while (isWsStarter(currentChar) || isNewlineStarter() || isCommentStarter() || isLinecommentStarter())
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

private int scanEscapeSequence()
{ int kind;
    take('\\');
    if((currentChar=='b') || (currentChar=='t') || (currentChar=='n') //Escapesequence
    || (currentChar=='f') || (currentChar=='r') || (currentChar=='"')
    || (currentChar=='\'') || (currentChar=='\\'))
    {    takeIt(); return Token.OK;
    else if((currentChar=='u')) //UnicodeEscape
    {
        takeIt();
        if((currentChar=='0') || (currentChar=='1') || (currentChar=='2') //HexDigit
        || (currentChar=='3') || (currentChar=='4') || (currentChar=='5') 
        || (currentChar=='6') || (currentChar=='7') || (currentChar=='8')
        || (currentChar=='9') || (currentChar=='a') || (currentChar=='b')
        || (currentChar=='c') || (currentChar=='d') || (currentChar=='e')
        || (currentChar=='f') || (currentChar=='A') || (currentChar=='B')
        || (currentChar=='C') || (currentChar=='D') || (currentChar=='E')
        || (currentChar=='F'))
        {
            takeIt();
            if((currentChar=='0') || (currentChar=='1') || (currentChar=='2') 
            || (currentChar=='3') || (currentChar=='4') || (currentChar=='5') 
            || (currentChar=='6') || (currentChar=='7') || (currentChar=='8')
            || (currentChar=='9') || (currentChar=='a') || (currentChar=='b')
            || (currentChar=='c') || (currentChar=='d') || (currentChar=='e')
            || (currentChar=='f') || (currentChar=='A') || (currentChar=='B')
            || (currentChar=='C') || (currentChar=='D') || (currentChar=='E')
            || (currentChar=='F'))
            {    
                takeIt();
                if((currentChar=='0') || (currentChar=='1') || (currentChar=='2')
                || (currentChar=='3') || (currentChar=='4') || (currentChar=='5') 
                || (currentChar=='6') || (currentChar=='7') || (currentChar=='8')
                || (currentChar=='9') || (currentChar=='a') || (currentChar=='b')
                || (currentChar=='c') || (currentChar=='d') || (currentChar=='e')
                || (currentChar=='f') || (currentChar=='A') || (currentChar=='B')
                || (currentChar=='C') || (currentChar=='D') || (currentChar=='E')
                || (currentChar=='F'))
                {
                    takeIt();
                    if((currentChar=='0') || (currentChar=='1') || (currentChar=='2')
                    || (currentChar=='3') || (currentChar=='4') || (currentChar=='5') 
                    || (currentChar=='6') || (currentChar=='7') || (currentChar=='8')
                    || (currentChar=='9') || (currentChar=='a') || (currentChar=='b')
                    || (currentChar=='c') || (currentChar=='d') || (currentChar=='e')
                    || (currentChar=='f') || (currentChar=='A') || (currentChar=='B')
                    || (currentChar=='C') || (currentChar=='D') || (currentChar=='E')
                    || (currentChar=='F'))
                    {
                        takeIt();
                        return Token.OK;
                    }else
                        return Token.ERROR;
                }else
                    return Token.ERROR;
            }else
            return Token.ERROR;
        }else
            return Token.ERROR;   
    }//Octalescape, as 3 alternancias tem esse intervalo em comum
    else if((currentChar=='0') || (currentChar=='1') || (currentChar=='2')
    || (currentChar=='3') || (currentChar=='4') || (currentChar=='5') 
    || (currentChar=='6') || (currentChar=='7')) 
    {
        kind = Token.ERROR;
        if((currentChar=='0') || (currentChar=='1') || (currentChar=='2')
        || (currentChar=='3'))
        {    takeIt(); kind = Token.OK; }
        if((currentChar=='0') || (currentChar=='1') || (currentChar=='2')
        || (currentChar=='3') ||(currentChar=='4') || (currentChar=='5') || (currentChar=='6')
        || (currentChar=='7')) // ('0'..'7') || ('0'..'7') ('0'..'7')
        {    takeIt(); kind = Token.OK; }
        if((currentChar=='0') || (currentChar=='1') || (currentChar=='2')
        || (currentChar=='3') ||(currentChar=='4') || (currentChar=='5') || (currentChar=='6')
        || (currentChar=='7')) // ('0'..'7') || ('0'..'7') ('0'..'7')
        {    takeIt(); kind = Token.OK; }
        return kind;
    }else
        return Token.ERROR;    
}
private int scanLongstring()
{int contigual = 0;
    //Neste ponto ja verificou que esta usando colchete e ja aceitou para diferenciar em scanToken() o =
    while(currentChar == '=')
        takeIt();
        contigual++;
    take('[');
    while(currentChar == '\\' || (!(currentChar == '\\')) || (!(currentChar == ']')
    || !(currentChar == Token.EOT))
    {
        scanEscapesequence();
    }
    if(currentChar == ']')
        takeIt();
    else if(currentchar == Token.EOT)
        return Token.ERROR;
    while((currentChar == '=') && (contigual > 0))
    {
        takeIt();
        count--;
    }
    if(count!=0)
        return Token.ERROR;
    take(']');
    return Token.LONGSTRING;
}

private int scanComment()
{
    take('[');
    take('[');
    while(!(currentChar == Token.EOT)) //Existe uma condicao de parada do ']]' dentro tambem
    {
        if(currentChar == ']')
        {
            takeIt();
            if(currentChar == ']')
            {
                takeIt();
                return Token.COMMENT;
            }
            else
                takeIt();
        }
        else
            takeIt();
    }
    if(currentChar == Token.EOT)
        return Token.ERROR;
}
private int scanWs()
{
    if(currentChar == ' ')
        takeIt();
    if(currentChar == '\t')
        takeIt();
    if(currentCHar == '\u000C')
        takeIt();
}
private int scanNewline()
{
    if(currentChar == '\r')
        takeIt();
    take('\n');
}
private int scanLinecomment()
{ //Neste ponto os -- ja foram lidos para diferenciar entre linecomment e comment
    while((!(currentChar == Token.EOT)) || (!(currentChar == '\n')) || 
    (!(currentChar == '\r'))) 
        takeIt();
    if(currentChar == '\r')
        takeIt();
    if(currentChar == '\n')
        takeIt();
    if(currentChar == Token.EOT)
        return Token.ERROR;
}
private int scanCharstring()
{
    //O or redundante em \\ e so para ficar legivel com a gramatica
    while(currentChar == '\\' || (!(currentChar == '\\')) || (!(currentChar == '\'')) ||
    (!(currentChar == Token.EOT)))
    {
        if(scanEscapeSequence() == Token.ERROR)
            return Token.ERROR;        
    }
    if(currentChar == '\'')
    {
        takeIt();
        return Token.CHARSTRING;
    }
    else
        return Token.EOT;
    }
    
}
private int scanNormalstring()
{
    take('"');
    //O or redundante em \\ e so para ficar legivel com a gramatica
    while(currentChar == '\\' || (!(currentChar == '\\')) || (!(currentChar == '"')) ||
    (!(currentChar == Token.EOT))) 
    {
        if((currentChar=='\\'))
        {
            if(scanEscapeSequence() == Token.ERROR)
                return Token.ERROR;
        }
        else
            takeIt(); //Qualquer outro simbolo que nao " e aceito para uma String
    }
    if(currentChar == '"')
    {
        takeIt();
        return Token.NORMALSTRING;
    }
    else
        return Token.EOT;
}
private boolean isNameStarter(char currentChar)
{
    if( (currentChar == 'a') || (currentChar == 'b') || (currentChar == 'c') || 
    (currentChar == 'd') || (currentChar == 'e') || (currentChar == 'f') ||
    (currentChar == 'g') || (currentChar == 'h') || (currentChar == 'i')
    || (currentChar == 'j') || (currentChar == 'k') || (currentChar == 'l')
    (currentChar == 'm') || (currentChar == 'n') || (currentChar == 'o')
    || (currentChar == 'p') || (currentChar == 'q') || (currentChar == 'r')
    || (currentChar == 's') || (currentChar == 't') || (currentChar == 'u')
    || (currentChar == 'v') || (currentChar == 'w') || (currentChar == 'x')
    || (currentChar == 'y') || (currentChar == 'y') || (currentChar == 'z')
    
    || (currentChar == 'A') || (currentChar == 'B') || (currentChar == 'C')
    || (currentChar == 'D') || (currentChar == 'E') || (currentChar == 'F')
    || (currentChar == 'G') || (currentChar == 'H') || (currentChar == 'I')
    || (currentChar == 'J') || (currentChar == 'K') (currentChar == 'L')
    || (currentChar == 'M') || (currentChar == 'N') || (currentChar == 'O')
    || (currentChar == 'P') || (currentChar == 'Q') || (currentChar == 'R')
    || (currentChar == 'S') || (currentChar == 'T') || (currentChar == 'U')
    || (currentChar == 'V') || (currentChar == 'W') || (currentChar == 'X')
    || (currentChar == 'Y') || (currentChar == 'Z') || (currentChar == '_'))
    {return true}
    else
        return false
}
private boolean isIntStarter(char currentChar)
{
    if( (currentChar == '0') || (currentChar == '1') || (currentChar == '2') || 
    (currentChar == '3') || (currentChar == '4') || (currentChar == '5') ||
    (currentChar == '6') || (currentChar == '7') || (currentChar == '8')    
    || (currentChar == '9'))
    {return true}
    else
        return false
}
private int scanHex()
{
    //Ja teve que aceitar 0 para diferenciar numero de hex que e 0x
    take('x');
    if(isIntStarter() || (currentChar == 'a') || (currentChar == 'b') || (currentChar == 'c') || 
    (currentChar == 'd') || (currentChar == 'e') || (currentChar == 'f'))
        takeIt();
    else
        return Token.ERROR;
    while(isIntStarter() || (currentChar == 'a') || (currentChar == 'b') || (currentChar == 'c') || 
    (currentChar == 'd') || (currentChar == 'e') || (currentChar == 'f'))
        takeIt();
    return Token.HEX;
}

/*

private int scanName(){ 
    switch (currentChar) 
    {
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
    
        while (isLetter(currentChar) || isNumber(currentChar))
            takeIt();
        return Token.NAME; 
        default:
            takeIt();
            return Token.ERROR;
    }
}
private int scanInt(){ 
    switch(currentChar)
    {
        case '0':  case '1':  case '2':  case '3':  case '4':
        case '5':  case '6':  case '7':  case '8':  case '9':
            takeIt();
    }
    while (isInt(currentChar))
        takeIt();
     
        if(currentChar != '.')
            return Token.INT;
        else
        {
            case '0':  case '1':  case '2':  case '3':  case '4':
            case '5':  case '6':  case '7':  case '8':  case '9':
                takeIt();
                while (isDigit(currentChar))
                  takeIt();
                return Token.FLOAT;
        }
            
        
        


      }
    }

}


}
*/