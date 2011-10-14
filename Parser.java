public class Parser {

private int flag;
private Token currentToken;
private Scanner scanner; 
private SourcePosition previousTokenPosition = new SourcePosition();

//m√©todos starters

private boolean statStarters()
{
    if((currentToken.kind == Token.NAME) || (currentToken.kind == Token.LPAREN) || (currentToken.kind == Token.DO) || (currentToken.kind  == Token.WHILE) || (currentToken.kind == Token.REPEAT) || (currentToken.kind == Token.IF) || (currentToken.kind == Token.FOR) || (currentToken.kind == Token.FUNCTION) || (currentToken.kind == Token.LOCAL))
        return true;
    else
        return false;
}

private boolean prefixexp1Starters()
{
    if((currentToken.kind == Token.COLON) || (currentToken.kind == Token.COMMA) || (currentToken.kind == Token.LPAREN) || (currentToken.kind  == Token.LCURLY) || (currentToken.kind == Token.NORMALSTRING) || (currentToken.kind == Token.CHARSTRING) || (currentToken.kind == Token.LONGSTRING) || (currentToken.kind == Token.LBRACKET) || (currentToken.kind == Token.DOT))
        return true;
    else
        return false;
}

private boolean explistStarters()
{
    if((currentToken.kind == Token.TRUE) ||(currentToken.kind == Token.FALSE) ||(currentToken.kind == Token.NIL) ||(currentToken.kind == Token.HEX) ||(currentToken.kind == Token.FLOAT) ||(currentToken.kind == Token.INT) ||(currentToken.kind == Token.EXP) || (currentToken.kind == Token.NORMALSTRING) || (currentToken.kind == Token.CHARSTRING) || (currentToken.kind == Token.LONGSTRING) || (currentToken.kind == Token.THREEDOTES) || (currentToken.kind == Token.FUNCTION) || (currentToken.kind == Token.NAME) || (currentToken.kind == Token.LPAREN) || (currentToken.kind == Token.LCURLY) || (currentToken.kind == Token.MINUS) || (currentToken.kind == Token.NOT) || (currentToken.kind == Token.VELHA))
        return true;
    else
        return false;
}

private boolean binopStarters()
{
    if((currentToken.kind == Token.MINUS) || (currentToken.kind == Token.PLUS) || (currentToken.kind == Token.TIMES) || (currentToken.kind == Token.RBAR) || (currentToken.kind == Token.CHAPEU) || (currentToken.kind == Token.PERCENT) || (currentToken.kind == Token.TWODOTES) || (currentToken.kind == Token.LESSTHAN) || (currentToken.kind == Token.LESSOREQUALTHAN) || (currentToken.kind == Token.MOREOREQUALTHAN) || (currentToken.kind == Token.MORETHAN) || (currentToken.kind == Token.EQUALS) || (currentToken.kind == Token.NOTEQUALS) || (currentToken.kind == Token.AND) || (currentToken.kind == Token.OR))
        return true;
    else
        return false;
}

private boolean fieldlistStarters()
{
    if ((currentToken.kind == Token.NAME) || (currentToken.kind == Token.LBRACKET) || (currentToken.kind == Token.TRUE) ||(currentToken.kind == Token.FALSE) ||(currentToken.kind == Token.NIL) ||(currentToken.kind == Token.HEX) ||(currentToken.kind == Token.FLOAT) ||(currentToken.kind == Token.INT) ||(currentToken.kind == Token.EXP) || (currentToken.kind == Token.NORMALSTRING) || (currentToken.kind == Token.CHARSTRING) || (currentToken.kind == Token.LONGSTRING) || (currentToken.kind == Token.THREEDOTES) || (currentToken.kind == Token.FUNCTION) || (currentToken.kind == Token.NAME) || (currentToken.kind == Token.LPAREN) || (currentToken.kind == Token.LCURLY) || (currentToken.kind == Token.MINUS) || (currentToken.kind == Token.NOT) || (currentToken.kind == Token.VELHA))
        return true;
    else
        return false;
}

//usado no parseField
private boolean auxiliarStarters()
{
    if ((currentToken.kind == Token.MINUS) || (currentToken.kind == Token.PLUS) || (currentToken.kind == Token.TIMES) || (currentToken.kind == Token.RBAR) || (currentToken.kind == Token.CHAPEU) || (currentToken.kind == Token.PERCENT) || (currentToken.kind == Token.TWODOTES) || (currentToken.kind == Token.LESSTHAN) || (currentToken.kind == Token.LESSOREQUALTHAN) || (currentToken.kind == Token.MOREOREQUALTHAN) || (currentToken.kind == Token.MORETHAN) || (currentToken.kind == Token.BECOMES) || (currentToken.kind == Token.NOTEQUALS) || (currentToken.kind == Token.AND) || (currentToken.kind == Token.OR) || (currentToken.kind == Token.COLON) || (currentToken.kind == Token.COMMA) || (currentToken.kind == Token.LPAREN) || (currentToken.kind  == Token.LCURLY) || (currentToken.kind == Token.NORMALSTRING) || (currentToken.kind == Token.CHARSTRING) || (currentToken.kind == Token.LONGSTRING) || (currentToken.kind == Token.LBRACKET) || (currentToken.kind == Token.DOT))
        return true;
    else
        return false;
}


//m√©todos accept e acceptIt

void accept (int tokenExpected)
{
    if(currentToken.kind == tokenExpected)
    {
        previousTokenPosition = currentToken.position;
        currentToken = scanner.scan();
    }
    else
        System.out.println("PARSER REJECT");
}

void acceptIt()
{
    System.out.println(currentToken);
    currentToken = scanner.scan();  
}

//In√≠cio dos m√©todos parsing

//chunk::= (stat (';'|&) )* (laststat (';'|&)|&)

private void parseChunk()
{
    while(statStarters())
    {
        parseStat();
        if(currentToken.kind == Token.SEMICOLON)
            acceptIt();
        else
            dummy();
    }
    if((currentToken.kind == Token.RETURN) || (currentToken.kind == Token.BREAK))
    {
        parseLaststat();
        if(currentToken.kind == Token.SEMICOLON)
        {
           acceptIt();
        }
        else 
            dummy();
    }
    else
        dummy();
}

//block::= chunk

private void parseBlock()
{
    parseChunk();
}


//stat 

private void parseStat()
{
        
    switch(currentToken.kind)
    {
        case Token.NAME: case Token.LPAREN:
        {
            if(currentToken.kind == Token.LPAREN)
            {
                acceptIt();
                parseExp();
                accept(Token.RPAREN);
            }
            else
                parseName();
                if (prefixexp1Starters())
                {
                    parsePrefixexp1();
                    parsePrefixexp2();
                }
             if ((currentToken.kind == Token.COMMA) || (currentToken.kind == Token.BECOMES))
             {                 
                while(currentToken.kind == Token.COMMA)
                {
                    acceptIt();
                    parseVar();
                }
                accept(Token.BECOMES);
                System.out.println(currentToken);
                parseExplist();
             }
             else if (flag == 0)
                System.out.println("erro");
             break;
        }
            
        
        

//do block end
        case Token.DO:
        {
            acceptIt();
            parseBlock();
            accept(Token.END);
            break;
        }
//while exp do block end
        case Token.WHILE:
        {
            acceptIt();
            parseExp();
            accept(Token.DO);
            parseBlock();
            accept(Token.END);
            break;
        }

//repeat block until exp 
        case Token.REPEAT:
        {
            acceptIt();
            parseBlock();
            accept(Token.UNTIL);
            parseExp();
            break;
        }

//if exp then block (elseif exp then block)* ((else block) | & ) end
        case Token.IF:
        {
            acceptIt();
            parseExp();
            accept(Token.THEN);
            parseBlock();
            while(currentToken.kind == Token.ELSEIF)
            {
                acceptIt();
                parseExp();
                accept(Token.THEN);
                parseBlock();
            }
            if(currentToken.kind == Token.ELSE)
            {
                acceptIt();
                parseBlock();
            }
            else
                dummy();
            accept(Token.END);
            break;
        }


// for Name (`=´ exp `,´ exp (`,´ exp|&) do block end |(`,` Name)* in explist do block end)
        case Token.FOR:
        {
            acceptIt();
            parseName();
            if(currentToken.kind == Token.BECOMES)
            {    
                acceptIt();
                parseExp();
                accept(Token.COMMA);
                parseExp();
                if(currentToken.kind == Token.COMMA)
                {
                    acceptIt();
                    parseExp();
                }
                else
                    dummy();
                accept(Token.DO);
                System.out.println(currentToken);
                parseBlock();
                System.out.println(currentToken);
                accept(Token.END);
            }
            else
            {
                while(currentToken.kind == Token.COMMA)
                {
                    acceptIt();
                    parseName();
                }
                accept(Token.IN);
                parseExplist();
                accept(Token.DO);
                parseBlock();
                System.out.println(currentToken);
                accept(Token.END);
            }
            break;
            
        }

//function funcname funcbody 
        case Token.FUNCTION:
        {
            acceptIt();            
            parseFuncname();
            parseFuncbody();
            break;
        }

//local (function Name funcbody | namelist ((`=` explist) | & ))
        case Token.LOCAL:
        {
            acceptIt();
            switch(currentToken.kind)
            {
                case Token.FUNCTION:
                {
                    acceptIt();
                    parseName();
                    parseFuncbody();
                    break;
                }
                case Token.NAME:
                {                  
                    parseNamelist();
                    if(currentToken.kind == Token.BECOMES)
                    {
                        acceptIt();
                        parseExplist();
                    }
                    else
                        dummy();
                    break;
                }
                default:
                    dummy();
            }
            break;
        }
        default:
            System.out.println("parse reject stat");
    }
}

//laststat::= return (explist|&) | break

private void parseLaststat()
{
    switch(currentToken.kind)
    {
        case Token.RETURN:
        {
            acceptIt();
            if(explistStarters())
            {
                parseExplist();
            }
            else
                dummy();
            break;
        }
        case Token.BREAK:
        {
            acceptIt();
            break;
        }
        default:
            System.out.println("parse reject laststat");
    }
}

//funcname ::= Name (`.` Name)* ((`:` Name)|&)

private void parseFuncname()
{
    parseName();
    while(currentToken.kind == Token.DOT)
    {
        acceptIt();
        parseName();
    }
    if(currentToken.kind == Token.COLON)
    {
        acceptIt();
        parseName();
    }
    else
        dummy();
}

//var::= Name (prefixexp1 prefixexp2(`[` exp `]` |  `.` Name)| &) | '(' exp ')'  prefixexp1 prefixexp2 (`[` exp `]` |  `.` Name)
private void parseVar()
{
    switch(currentToken.kind)
    {
        case Token.NAME:
        {
            parseName();
            if (prefixexp1Starters())
            {                
                parsePrefixexp1();
                parsePrefixexp2();
                if (flag == 1)
                    System.out.println("erro\n");
                if (currentToken.kind == Token.LBRACKET)
                {
                    acceptIt();
                    parseExp();
                    accept(Token.RBRACKET);
                }
                else if (currentToken.kind == Token.DOT)
                {
                    acceptIt();
                    parseName();
                }
            }
            else
                dummy();
            break;
            
        }
        case Token.LPAREN:
        {
            acceptIt();
            parseExp();
            accept(Token.RPAREN);
            parsePrefixexp1();
            parsePrefixexp2();
            if (flag == 1)
                System.out.println("erro\n");
            if(currentToken.kind == Token.LBRACKET)
            {
                acceptIt();
                parseExp();
                accept(Token.RBRACKET);
            }
            else if (currentToken.kind == Token.DOT)
            {
                acceptIt();
                parseName();
            }
            break;
        }
        default:
            System.out.println("Parser reject parseVar\n");
    } 
}

//namelist::= Name (`,` Name)*

private void parseNamelist()
{
    if (currentToken.kind == Token.NAME)
    {
        parseName();
        while(currentToken.kind == Token.COMMA)
        {
            acceptIt();
            if (currentToken.kind == Token.NAME)
            {
                parseName();
            }
        }
    }
    else
        System.out.println("parse reject parseNamelist\n");
}

//explist::= exp (`,` exp)*

private void parseExplist()
{
    switch(currentToken.kind)
    {    
        case Token.TRUE:
            parseExp();    
            while(currentToken.kind == Token.COMMA)
            {
                acceptIt();
                if(explistStarters())
                    parseExp();
            }
            break;  
        case Token.FALSE:
            parseExp();    
            while(currentToken.kind == Token.COMMA)
            {
                acceptIt();
                if(explistStarters())
                    parseExp();
            }
            break;  
        case Token.NIL:
            parseExp();    
            while(currentToken.kind == Token.COMMA)
            {
                acceptIt();
                if(explistStarters())
                    parseExp();
            }
            break;  
        case Token.HEX:
            parseExp();    
            while(currentToken.kind == Token.COMMA)
            {
                acceptIt();
                if(explistStarters())
                    parseExp();
            }
            break;  
        case Token.FLOAT:
            parseExp();    
            while(currentToken.kind == Token.COMMA)
            {
                acceptIt();
                if(explistStarters())
                    parseExp();
            }
            break;  
        case Token.INT:
            parseExp();    
            while(currentToken.kind == Token.COMMA)
            {
                acceptIt();
                if(explistStarters())
                    parseExp();
            }
            break;  
        case Token.EXP:
            parseExp();    
            while(currentToken.kind == Token.COMMA)
            {
                acceptIt();
                if(explistStarters())
                    parseExp();
            }
            break;  
        case Token.NORMALSTRING:
            parseExp();    
            while(currentToken.kind == Token.COMMA)
            {
                acceptIt();
                if(explistStarters())
                    parseExp();
            }
            break;  
        case Token.CHARSTRING:
            parseExp();    
            while(currentToken.kind == Token.COMMA)
            {
                acceptIt();
                if(explistStarters())
                    parseExp();
            }
            break;  
        case Token.LONGSTRING:
            parseExp();    
            while(currentToken.kind == Token.COMMA)
            {
                acceptIt();
                if(explistStarters())
                    parseExp();
            }
            break;  
        case Token.THREEDOTES:
            parseExp();    
            while(currentToken.kind == Token.COMMA)
            {
                acceptIt();
                if(explistStarters())
                    parseExp();
            }
            break;  
        case Token.FUNCTION:
            parseExp();    
            while(currentToken.kind == Token.COMMA)
            {
                acceptIt();
                if(explistStarters())
                    parseExp();
            }
            break;  
        case Token.NAME:
            parseExp();    
            while(currentToken.kind == Token.COMMA)
            {
                acceptIt();
                if(explistStarters())
                    parseExp();
            }
            break;  
        case Token.LPAREN:
            parseExp();    
            while(currentToken.kind == Token.COMMA)
            {
                acceptIt();
                if(explistStarters())
                    parseExp();
            }
            break;  
        case Token.LCURLY:
            parseExp();    
            while(currentToken.kind == Token.COMMA)
            {
                acceptIt();
                if(explistStarters())
                    parseExp();
            }
            break;  
        case Token.MINUS:
            parseExp();    
            while(currentToken.kind == Token.COMMA)
            {
                acceptIt();
                if(explistStarters())
                    parseExp();
            }
            break;
        case Token.NOT:
            parseExp();    
            while(currentToken.kind == Token.COMMA)
            {
                acceptIt();
                if(explistStarters())
                    parseExp();
            }
            break;
        case Token.VELHA:
            parseExp();    
            while(currentToken.kind == Token.COMMA)
            {
                acceptIt();
                if(explistStarters())
                    parseExp();
            }
            break;
        default:
            System.out.println("erro explist\n");
    }
}



/* exp::= (nil | false | true | Number | String | `‚Ä¶` | function | prefixexp | tableconstructor | unop exp) exp1 */

private void parseExp()
{
    switch(currentToken.kind)
    {
        case Token.NIL:
        {
            acceptIt();
            parseExp1();
            break;
        }
        case Token.FALSE:
        {
            acceptIt();
            parseExp1();
            break;
        }
        case Token.TRUE:
        {
            acceptIt();
            parseExp1();
            break;
        }
        case Token.INT:
        {
            parseNumber();
            parseExp1();
            break;
        }
        case Token.FLOAT:
        {
            parseNumber();
            parseExp1();
            break;
        }
        case Token.HEX:
        {
            parseNumber();
            parseExp1();
            break;
        }
        case Token.EXP:
        {
            parseNumber();
            parseExp1();
            break;
        }
        case Token.NORMALSTRING:
        {
            parseString();
            System.out.println("mimi\n"+currentToken);
            parseExp1();
            break;
        }
        case Token.CHARSTRING:
        {
            parseString();
            parseExp1();
            break;
        }
        case Token.LONGSTRING:
        {
            parseString();
            parseExp1();
            break;
        }
        case Token.THREEDOTES:
        {
            acceptIt();
            parseExp1();
            break;
        }
        case Token.FUNCTION:
        {
            parseFunction();
            parseExp1();
            break;
        }
        case Token.NAME:
        {           
            parsePrefixExp();
            parseExp1();
            break;
        }
        case Token.LPAREN:
        {
            parsePrefixExp();
            parseExp1();
            break;
        }
        case Token.LCURLY:
        {
            parseTableConstructor();
            parseExp1();
            break;
        }
        case Token.MINUS:
        {
            parseUnop();
            parseExp();
            parseExp1();
            break;
        }
        case Token.NOT:
        {
            parseUnop();
            parseExp();
            parseExp1();
            break;
        }
        case Token.VELHA:
        {
            parseUnop();
            parseExp();
            parseExp1();
            break;
        }
        default:
        {
            System.out.println("parse exp reject");
        }
    }
    parseExp1();

}

//exp1::= binop exp exp1|&

private void parseExp1()
{
    if (binopStarters())
    {
        parseBinop();
        System.out.println(currentToken);
        parseExp();
        parseExp1();
    }
    else
        dummy();
    
}

//prefixexp::=  (Name| '(' exp ')' ) prefixexp1 prefixexp2

private void parsePrefixExp()
{
    switch(currentToken.kind)
    {
        case Token.NAME:
        {
            parseName();
            parsePrefixexp1();
            parsePrefixexp2();
            break;
        }
        case Token.LPAREN:
        {
            acceptIt();
            parseExp();
            accept(Token.RPAREN);
            parsePrefixexp1();
            parsePrefixexp2();
            break;
        }
        default:
            System.out.println("parse reject prefixexp");            
    }
}

//prefixexp1::=  ( (`:` Name|&) args)  prefixexp1| &	

private void parsePrefixexp1()
{
    switch(currentToken.kind)
    {    
        case Token.COLON:
        {
            acceptIt();
            parseName();
            parseArgs();
            flag = 1;
            parsePrefixexp1();
            break;
        }
        case Token.LPAREN:
        {   
            parseArgs();
            flag = 1;
            parsePrefixexp1();
            break;
        }
        case Token.LCURLY:
        {    
            parseArgs();
            flag = 1;
            parsePrefixexp1();
            break;
        }
        case Token.NORMALSTRING:
        {    
            parseArgs();
            flag = 1;
            parsePrefixexp1();
            break;
        }
        case Token.CHARSTRING:
        {    
            parseArgs();
            flag = 1;
            parsePrefixexp1();
            break;
        }
        case Token.LONGSTRING:
        {    
            parseArgs();
            flag = 1;
            parsePrefixexp1();
            break;
        }
        default:
            dummy();
    }
}

//prefixexp2::= ((`[` exp `]` |  `.` Name )  prefixexp1) prefixexp2| &

private void parsePrefixexp2()
{
    switch (currentToken.kind)
    {
        case Token.LBRACKET:
        {
            flag = 0;            
            acceptIt();
            parseExp();
            accept(Token.RBRACKET);
            parsePrefixexp1();
            parsePrefixexp2();
            break;
        }
        case Token.DOT:
            flag = 0;            
            acceptIt();
            parseName();
            parsePrefixexp1();
            parsePrefixexp2();
            break;
        default:
            dummy();
    }
}

//args::= `(` (explist | &) `)` | tableconstructor | String

private void parseArgs()
{
    switch(currentToken.kind)
    {
        case Token.LPAREN:
        {
            acceptIt();
            if(explistStarters())
                parseExplist();
            accept(Token.RPAREN);
            break;
        }
        case Token.LCURLY:
        {
            parseTableConstructor();
            break;
        }
        case Token.NORMALSTRING:
        {
            parseString();
            break;
        }
        case Token.CHARSTRING:
        {
            parseString();
            break;
        }
        case Token.LONGSTRING:
        {
            parseString();
            break;
        }
        default:
            dummy();
    }
}

//function::= function funcbody

private void parseFunction()
{
    if(currentToken.kind == Token.FUNCTION)
    {
        acceptIt();
        parseFuncbody();
    }
}

//funcbody ::= `(` (parlist | & ) `)` block end

private void parseFuncbody()
{
    if(currentToken.kind == Token.LPAREN)
    {
        acceptIt();
        if((currentToken.kind == Token.NAME) || (currentToken.kind == Token.THREEDOTES))
        {
            parseParList();
        }
        else
        {
            dummy();
        }
        accept(Token.RPAREN);
        parseBlock();
        accept(Token.END);
    }
}

//parlist ::= Name parlist2  | `...´

private void parseParList()
{
    switch(currentToken.kind)
    {
        case Token.NAME:
        {
            parseName();
            System.out.println(currentToken);
            parseParlist2();
            break;
        }
        case Token.THREEDOTES:
        {
            acceptIt();
            break;
        }
        default:
            System.out.println("parse parlist reject");
             
    }
}

//parlist2 ::= ‘,’ ( (Name parlist2) | ‘...’) | &

private void parseParlist2()
{
    switch(currentToken.kind)
    {
    case Token.COMMA:
        acceptIt();
        if(currentToken.kind == Token.NAME)
        {
            parseName();
            parseParlist2();
        }
        else if(currentToken.kind == Token.THREEDOTES)
            acceptIt();
        break;
    default:
        dummy();   
    } 
}


//tableconstructor::= `{` (fieldlist|&) `}`

private void parseTableConstructor()
{
    if(currentToken.kind == Token.LCURLY)
    {
        acceptIt();
        if(fieldlistStarters())
        {
            parseFieldList();
        }
        else
        {
            dummy();
        }
        accept(Token.RCURLY);
    }
}

//fieldlist:= field (fieldsep (fieldlist | &) | &)

private void parseFieldList()
{
    if(fieldlistStarters())
    {
        parseField();
        if((currentToken.kind == Token.COMMA) || (currentToken.kind == Token.SEMICOLON))
        {
            parseFieldSep();
            if ((currentToken.kind == Token.COMMA) || (currentToken.kind == Token.SEMICOLON))
                parseFieldSep();
            else
                dummy();
        }
        else
            dummy();
    }
    else
        System.out.println("parse reject fieldlist");
}


//field::= Name (`=` exp | prefixexp1 prefixexp2 exp1) | `[` exp `]` `=` exp | nil exp1| false exp1| true exp1| Number exp1| String exp1| `‚Ä¶` exp1| function exp1| '(' exp ')' prefixexp1 prefixexp2 exp1| tableconstructor exp1|  unop exp exp1 

private void parseField()
{
    switch (currentToken.kind)
    {
        case Token.NAME:
        {
            parseName();
            if(currentToken.kind == Token.BECOMES)
            {
                acceptIt();
                parseExp();
            }
            else if (auxiliarStarters())
            {
                parsePrefixexp1();
                parsePrefixexp2();
                parseExp1();                
            }       
            break;                     
        }
        case Token.LBRACKET:
        {
            acceptIt();
            parseExp();
            accept(Token.RBRACKET);
            accept(Token.BECOMES);
            parseExp();
            break;
        }
        case Token.NIL:
        {
            acceptIt();
            parseExp1();
            break;
        }
        case Token.FALSE:
        {
            acceptIt();
            parseExp1();
            break;
        } 
        case Token.TRUE:
        {   
            acceptIt();
            parseExp1();
            break;
        }
        case Token.INT:
        {
            parseNumber();
            break;
        }
        case Token.FLOAT:
        {
            parseNumber();
            break;
        }
        case Token.HEX:
        {
            parseNumber();
            break;
        }
        case Token.EXP:
        {
            parseNumber();
            break;
        }
        case Token.NORMALSTRING:
        {
            parseString();
            break;
        }
        case Token.CHARSTRING:
        {
            parseString();
            break;
        }
        case Token.LONGSTRING:
        {
            parseString();
            break;
        }
        case Token.THREEDOTES:
        {
            acceptIt();
            parseExp1();
            break;
        }
        case Token.FUNCTION:
        {
            parseFunction();
            parseExp1();
            break;
        }
        case Token.LPAREN:
        {
            acceptIt();
            parseExp();
            accept(Token.RPAREN);
            parsePrefixexp1();
            parsePrefixexp2();
            parseExp1();
            break;
        }
        case Token.LCURLY:
        {
            parseTableConstructor();
            parseExp1();
            break;
        }
        case Token.MINUS:
        {
            parseUnop();
            parseExp();
            break;
        }
        case Token.NOT:
        {
            parseUnop();
            parseExp();
            break;
        }
        case Token.VELHA:
        {
            parseUnop();
            parseExp();
            break;
        }
        default:
            System.out.println("parse reject field");
    }
}

//fieldsep::= `,` | `;`

private void parseFieldSep()
{
    switch(currentToken.kind)
    {
        case Token.COMMA:
        {
            acceptIt();
            break;
        }
        case Token.SEMICOLON:
        {
            acceptIt();
            break;
        }
        default:
            System.out.println("parse reject fieldsep");
    }            
}

/*binop::= `+¬¥ | `-¬¥ | `*¬¥ | `/¬¥ | `^¬¥ | `%¬¥ | `..¬¥ |

                   `<¬¥ | `<=¬¥ | `>¬¥ | `>=¬¥ | `==¬¥ | `~=¬¥ |

                   and | or*/

private void parseBinop()
{
    switch(currentToken.kind)
    {
        case Token.PLUS:
        {
            acceptIt();
            break;
        }
        case Token.MINUS:
        {
            acceptIt();
            break;
        }
        case Token.TIMES:
        {
            acceptIt();
            break;
        }
        case Token.RBAR:
        {
            acceptIt();
            break;
        }
        case Token.CHAPEU:
        {
            acceptIt();
            break;
        }
        case Token.PERCENT:
        {
            acceptIt();
            break;
        }
        case Token.TWODOTES:
        {
            acceptIt();
            break;
        }
        case Token.LESSTHAN:
        {
            acceptIt();
            break;
        }
        case Token.LESSOREQUALTHAN:
        {
            acceptIt();  
            break;          
        }
        case Token.MOREOREQUALTHAN:
        {
            acceptIt();
            break;
        }
        case Token.MORETHAN:
        {
            acceptIt();
            break;
        }
        case Token.EQUALS:
        {
            acceptIt();
            break;
        }
        case Token.NOTEQUALS:
        {
            acceptIt();
            break;
        }
        case Token.AND:
        {
            acceptIt();
            break;
        }
        case Token.OR:
        {
            acceptIt();
            break;
        }
        default:
        {
            System.out.println("parse reject binop");
        } 
    }
}

//unop::= `-` | not | `#`

private void parseUnop()
{
    switch(currentToken.kind)
    {
        case Token.MINUS:
        {
            acceptIt();
            break;
        }
        case Token.NOT:
        {
            acceptIt();
            break;
        }
        case Token.VELHA:
        {
            acceptIt();
            break;
        }
        default:
            System.out.println("parse reject unop"); 
    }
}

// demais parsing segundo o livro p√°gina 99

private void parseName()
{
    if(currentToken.kind == Token.NAME)
    {
        currentToken = scanner.scan();
    }
    else
        System.out.println("parse reject Name");
}

private void parseNumber()
{
    if((currentToken.kind == Token.INT) || (currentToken.kind == Token.FLOAT) || (currentToken.kind == Token.EXP) || (currentToken.kind == Token.HEX))
    {
        currentToken = scanner.scan();            
    }
    else
        System.out.println("parse reject number");
}

private void parseString()
{   
    if((currentToken.kind == Token.NORMALSTRING) || (currentToken.kind == Token.CHARSTRING) || (currentToken.kind == Token.LONGSTRING))
    {
        currentToken = scanner.scan();
    }
    else  
        System.out.println("parse reject string");
}

// dummy
private void dummy() {}

public static void main (String args[])
{
//    Token token;    
    Parser parser = new Parser();
    SourceFile source = new SourceFile("entradas.txt");
    parser.scanner = new Scanner(source);

        parser.currentToken = parser.scanner.scan();
        if(parser.currentToken == null)
            System.out.println("vazio");
        System.out.println(parser.currentToken);
        parser.parseChunk();


    if(parser.currentToken.kind != Token.EOT) 
        System.out.println("erroxxx");

}

}
