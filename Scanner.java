import java.io.*;
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
    currentChar = sourceFile.getSource();
    
}

private void take (char expectedChar){
    
    if(currentlyScanningToken)
    {
        if (currentChar == expectedChar)
        {
            currentSpelling.append(currentChar);
        }
    }
    currentChar = sourceFile.getSource();   
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
        while(isIntStarter(currentChar)) //('0'..'9')*
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
    else if(currentChar == '"')
        return scanNormalstring();
    else if(currentChar == '\'')
        return scanCharstring();
    else if((currentChar == ' ') || (currentChar == '\t') || (currentChar == '\u000C'))
        return scanWs();
    else if((currentChar == '\r') || (currentChar == '\n'))
        return scanNewline();
    switch(currentChar)
    {
        case '+':  
          takeIt();
          return Token.PLUS;
        case '-':
          takeIt();
              return Token.MINUS;
        case '*': 
            takeIt();
            return Token.TIMES;
        case '/':  
            takeIt();
            return Token.RBAR;
        case '%':  
           takeIt();
           return Token.PERCENT;
        case '^':
            takeIt();
            return Token.CHAPEU;
        case '#':
            takeIt();
            return Token.VELHA;
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
        case '<':  
            takeIt();
            if(currentChar == '=')
            {
                takeIt();
                return Token.LESSOREQUALTHAN;
             }   
             else
                return Token.LESSTHAN;
        case '>':
             takeIt();
             if(currentChar == '=')
             {
                 takeIt();
                 return Token.MOREOREQUALTHAN;
             }
             else
                return Token.MORETHAN;
        case '(':
                takeIt();
                return Token.LPAREN;
        case ')':
                takeIt();
                return Token.RPAREN;
        case '{':
                takeIt();
                return Token.LCURLY;
        case '}':
                takeIt();
                return Token.RCURLY;
        case '[':
                takeIt();
                if(currentChar == '=')
                    return scanLongstring();
                    else
                        return Token.LBRACKET;
        case ']':
            takeIt();
            return Token.RBRACKET;
        case ';':
            takeIt();
            return Token.SEMICOLON;
        case ':':
            takeIt();
            return Token.COLON;
        case ',':
            takeIt();
            return Token.COMMA;        
        case '.':
            takeIt();
            if(currentChar == '.')
            {
                takeIt();
                if(currentChar == '.')
                {
                    takeIt();
                    return Token.THREEDOTES;
                }
                else
                    return Token.TWODOTES;
            }
            return Token.DOT;
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
      int kind = -1;
      currentlyScanningToken = false;
      
      //isWsStarter(currentChar) || isNewlineStarter() || isCommentStarter() || isLinecommentStarter()
      while ((currentChar == ' ') || (currentChar == '\t') || (currentChar == '\u000C')||
         (currentChar == '\r') || (currentChar == '\n') || (currentChar == '-'))
     {
         
        if((currentChar == '\r') || (currentChar == '\n'))
            scanNewline();
        else if((currentChar == ' ') || (currentChar == '\t') || (currentChar == '\u000C'))
            scanWs();
        else if(currentChar == '-')
        {
            takeIt();
            if(currentChar == '-')
            {
                takeIt();
                if(currentChar == '[')
                {
                    scanComment();
                }else{
                    scanLinecomment();
                }
            }else{    
                kind = Token.MINUS;
                break;
            }
        }
    }
    currentlyScanningToken = true;
    currentSpelling = new StringBuffer("");
    pos = new SourcePosition();
    pos.start = sourceFile.getCurrentLine();
    if(kind != Token.MINUS)
        kind = scanToken();
    else
        currentSpelling.append('-');
    pos.finish = sourceFile.getCurrentLine();
    tok = new Token(kind, currentSpelling.toString(),pos);
        
    return tok;
}
private int scanEscapeSequence()
{ int kind;
    take('\\');
    if((currentChar=='b') || (currentChar=='t') || (currentChar=='n') //Escapesequence
    || (currentChar=='f') || (currentChar=='r') || (currentChar=='"')
    || (currentChar=='\'') || (currentChar=='\\'))
    {    takeIt(); return Token.OK;}
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
{int count = 0, countaux = 0;
    //Neste ponto ja verificou que esta usando colchete e ja aceitou para diferenciar em scanToken() o =
    while(currentChar == '=')
    {
        takeIt();
        count++;
    }
    take('[');
    while(currentChar != SourceFile.EOT) //A condicao de parada de encontrar colchete fechando de mesmo nivel e aceitar o resto esta dentro
    {
        if(currentChar == ']')
        {
            takeIt();
            countaux = count;
            while(countaux > 0)
            {
                if(currentChar == '=')
                {
                    takeIt();
                    countaux--;
                }
                else
                    break; //Nivel de comentario e menor que o necessario para fechar o bloco de comentario
            }
            if((countaux == 0) && (currentChar == ']')) //Se nao entrar nessa condicao o nivel de parenteses nao e o mesmo do de abertura
            {
                takeIt();
                
                
                return Token.LONGSTRING;
            } 
        }
        
        else if(currentChar=='\\')
        {
            if(scanEscapeSequence() == Token.ERROR)
                return Token.ERROR;
        } 
        else 
            takeIt(); //entao e uma letra qualquer que pertence a long string
    }
    return Token.ERROR; //Chegou ao fim do arquivo sem fechar string longa, retorne erro
}

private int scanComment()
{boolean foundDoubleRightBrackets = false;
    take('[');
    take('[');
    while(!((foundDoubleRightBrackets) || (currentChar == SourceFile.EOT))) //Existe uma condicao de parada do ']]' dentro tambem
    {
        System.out.print(currentChar);
        if(currentChar == ']')
        {
            takeIt();
            if(currentChar == ']')
            {
                takeIt();
                foundDoubleRightBrackets = true;
                return Token.OK;
            }
            else
                takeIt();
        }
        else
            takeIt();
    }
    if(currentChar == SourceFile.EOT)
        return Token.ERROR;
    return Token.OK; //Comentarios nao sao tokens
}
private int scanWs()
{
    if(currentChar == ' ')
    {
        takeIt();
        return Token.OK;
    }
    if(currentChar == '\t')
    {
        takeIt();
        return Token.OK;
    }
    if(currentChar == '\u000C')
    {
        takeIt();
        return Token.OK;
    }
    else
        return Token.ERROR; //Se esse metodo foi chamado entao alguns dos starters acima devem ter ocorrido, se entrou
        //neste metodo de outro local deve ser sinalizado um erro
}
private int scanNewline()
{
    if(currentChar == '\r')
        takeIt();
    take('\n');
    return Token.OK;
}
private int scanLinecomment()
{ //Neste ponto os -- ja foram lidos para diferenciar entre linecomment e comment
    while (!((currentChar == '\n') || (currentChar == '\r') || (currentChar == SourceFile.EOT)))
        takeIt();
    if(currentChar == '\r')
        takeIt();
    if(currentChar == '\n')
        takeIt();
    if(currentChar == SourceFile.EOT)
        return Token.EOT;
    else
        return Token.OK; //Comentario de linha nao e token
}
private int scanCharstring()
{
    take('\'');
    while(!((currentChar == '\'') || (currentChar == SourceFile.EOT )))
    {
        if(currentChar=='\\')
        {
            if(scanEscapeSequence() == Token.ERROR)
                return Token.ERROR;   
        }
        else
            takeIt(); //Qualquer outro simbolo que nao e ' e aceito para uma String
    }
    if(currentChar == '\'')
    {
        takeIt();
        return Token.CHARSTRING;
    }
    else
        return Token.ERROR; //Abriu string e chegou ao fim do arquivo sem fechar a string com aspas simples
}
private int scanNormalstring()
{
    take('"');
    //O or redundante em \\ e so para ficar legivel com a gramatica
    while(!((currentChar == '"') || (currentChar == SourceFile.EOT))) 
    {  
        if(currentChar=='\\')
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
        return Token.ERROR; //Abriu string e chegou ao fim do arquivo sem fechar a string com aspas simples
}
private boolean isNameStarter(char currentChar)
{
    if( (currentChar == 'a') || (currentChar == 'b') || (currentChar == 'c') || 
    (currentChar == 'd') || (currentChar == 'e') || (currentChar == 'f') ||
    (currentChar == 'g') || (currentChar == 'h') || (currentChar == 'i')
    || (currentChar == 'j') || (currentChar == 'k') || (currentChar == 'l') ||
    (currentChar == 'm') || (currentChar == 'n') || (currentChar == 'o')
    || (currentChar == 'p') || (currentChar == 'q') || (currentChar == 'r')
    || (currentChar == 's') || (currentChar == 't') || (currentChar == 'u')
    || (currentChar == 'v') || (currentChar == 'w') || (currentChar == 'x')
    || (currentChar == 'y') || (currentChar == 'y') || (currentChar == 'z')
    
    || (currentChar == 'A') || (currentChar == 'B') || (currentChar == 'C')
    || (currentChar == 'D') || (currentChar == 'E') || (currentChar == 'F')
    || (currentChar == 'G') || (currentChar == 'H') || (currentChar == 'I')
    || (currentChar == 'J') || (currentChar == 'K') || (currentChar == 'L')
    || (currentChar == 'M') || (currentChar == 'N') || (currentChar == 'O')
    || (currentChar == 'P') || (currentChar == 'Q') || (currentChar == 'R')
    || (currentChar == 'S') || (currentChar == 'T') || (currentChar == 'U')
    || (currentChar == 'V') || (currentChar == 'W') || (currentChar == 'X')
    || (currentChar == 'Y') || (currentChar == 'Z') || (currentChar == '_'))
    {return true;}
    else
        return false;
}
private boolean isIntStarter(char currentChar)
{
    if( (currentChar == '0') || (currentChar == '1') || (currentChar == '2') || 
    (currentChar == '3') || (currentChar == '4') || (currentChar == '5') ||
    (currentChar == '6') || (currentChar == '7') || (currentChar == '8')    
    || (currentChar == '9'))
    {return true;}
    else
        return false;
}
private int scanHex()
{
    //Ja teve que aceitar 0 para diferenciar numero de hex que e 0x
    take('x');
    if(isIntStarter(currentChar) || (currentChar == 'a') || (currentChar == 'b') || (currentChar == 'c') || 
    (currentChar == 'd') || (currentChar == 'e') || (currentChar == 'f'))
        takeIt();
    else
        return Token.ERROR;
    while(isIntStarter(currentChar) || (currentChar == 'a') || (currentChar == 'b') || (currentChar == 'c') || 
    (currentChar == 'd') || (currentChar == 'e') || (currentChar == 'f'))
        takeIt();
    return Token.HEX;
}

private int scanName(){ 
        if(isLetter(currentChar))
            takeIt();
        else
            return Token.ERROR;
    
        while (isLetter(currentChar) || isInt(currentChar))
            takeIt();
        return Token.NAME; 
}
private boolean isInt(char c) {
  return (c >= '0' && c <= '9');
}

private boolean isLetter(char c) {
  return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c=='_');
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
    return Token.INT;
}
public static void main(String args[])
{
    
    /*
    Token tok;
    SourceFile source = new SourceFile("entradas.txt");
    Scanner scanner = new Scanner(source);
    do{
        tok = scanner.scan();
        if(tok.kind == Token.ERROR)
        {
            System.out.println(tok);
            System.exit(0);
        }
        if(tok.kind != Token.EOT)
            System.out.println(tok);
    }while(tok.kind != Token.EOT);
    */
try{
        FileWriter fstream = new FileWriter("tokens.txt");
        BufferedWriter out = new BufferedWriter(fstream);
        Token tok;
        SourceFile source = new SourceFile("teste.txt");
        Scanner scanner = new Scanner(source);
        do{
            tok = scanner.scan();
            if(tok.kind == Token.ERROR)
            {
                out.write(tok.toString());
                System.exit(0);
            }
            if(tok.kind != Token.EOT)
                out.write(tok.toString());
        }while(tok.kind != Token.EOT);           
        out.close();
        
}catch (Exception e){//Catch exception if any
        System.err.println("Error: " + e.getMessage());
        } 

}
}