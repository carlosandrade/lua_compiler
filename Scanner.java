public class Scanner{
    private char currentChar;
    
    //Kind and spelling of the current token:
    
    private byte currentKind;
    private StringBuffer currentSpelling;

private void take (char expectedChar){
    if (currentChar == expectedChar)
    {
        currentSpelling.append(currentChar);
        currentChar = //next source charactert;
    }else
        //report syntax error
    }
}

private void takeIt(){
    currentSpelling.append(currentChar);
    currentChar = //next source character;
}

private boolean isDigit(char c){
    //returns true iff the character c is a digit
}

private byte scanToken(){
    //As above
}

public Token scan(){
    while
}
}