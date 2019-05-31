package exam;

public class Token {
    public String type;
    public String value;

    public Token(String type, String value){
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setValue(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String toString()
    {
        return "[ Type := " + this.type + " Value := '" + this.value + "' ]";
    }
}
