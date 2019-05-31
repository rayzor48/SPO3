package exam;

import java.util.regex.Pattern;

public class Lexem {
    Pattern p;
    String name;
    Lexem(Pattern p, String name){
        this.p = p;
        this.name = name;
    }

    public Pattern getPattern()
    {
        return this.p;
    }
}
