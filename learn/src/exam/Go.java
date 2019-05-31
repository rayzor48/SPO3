package exam;

import java.util.*;
import Collections.MyList;
import Collections.MySet;

public class Go {

    HashMap<String, Integer> tableOfVar = new HashMap<>();
    HashMap<String, MyList> tableOfList = new HashMap<>();
    HashMap<String, MySet> tableOfSet = new HashMap<>();
    List<Token> poliz;
    private Stack<String> buffer = new Stack<>();

    int a, b, c;
    boolean flag = false;

    public Go(List<Token> poliz){
        this.poliz = poliz;
    }

    public void interpretation(){
        Token token;
        for(int position = 0; position <= poliz.size() - 1; position ++ ){
            token = poliz.get(position);

            switch (token.getType()){
                case "INIT" :
                    initialization(poliz.get(position + 1).getValue());
                    break;
                case "LIST":
                    tableOfList.put(poliz.get(position + 1).getValue(), new MyList<String>());
                    break;
                case "SET":
                    tableOfSet.put(poliz.get(position + 1).getValue(), new MySet<String>());
                    break;
                case "VAR" :
                    if(checkInitialization(token.value)){
                        buffer.push(token.getValue());
                    }
                    break;
                case "NUMBER" :
                    buffer.push(token.getValue());
                    break;
                case "OP" :
                    operation(token.getValue());
                    break;
                case "LOG_OP" :
                    buffer.push(String.valueOf(logicOperation(token.getValue())));
                    break;
                case "COL_OP" :
                    collectionsOp(token.getValue());
                    break;
                case "ASSIGN_OP" :
                    assignOp();
                    break;
                case "NF" :
                    a = valueOrVariable(tableOfVar) -1;
                    flag = buffer.pop().equals("true");
                    position = flag ? position : a;
                    break;
                case "F" :
                    a = valueOrVariable(tableOfVar) - 1;
                    position  = a;
                    break;
                case "PRINT" :
                    System.out.println("\nVariables : " +  tableOfVar);
                    System.out.println("Lists: " + tableOfList);
                    System.out.println("Set: " + tableOfSet);
                    break;
            }
            //System.out.println(tableOfVar.toString());
        }
    }

    private void operation(String op) {

        b = valueOrVariable(tableOfVar);
        a = valueOrVariable(tableOfVar);

        switch (op) {
            case "+":
                c = a + b;
                break;
            case "-":
                c = a - b;
                break;
            case "/":
                c = a / b;
                break;
            case "*":
                c = a * b;
                break;
        }

        buffer.push(String.valueOf(c));
    }

    private boolean logicOperation(String logOp){
        boolean flag = false;

        b = valueOrVariable(tableOfVar);
        a = valueOrVariable(tableOfVar);

        switch (logOp) {
            case "<":
                flag = a < b;
                break;
            case ">":
                flag = a > b;
                break;
            case "==":
                flag = a == b;
                break;
            case "!=":
                flag = a != b;
                break;
            case "<=":
                flag = a <= b;
                break;
            case ">=":
                flag = a >= b;
                break;
        }

        return flag;
    }

    private void collectionsOp(String colOp){
        MyList<String> list = null;
        MySet<String> set = null;
        boolean flag = false;
        String s = "";

        if(!colOp.equals("size")){
            a = valueOrVariable(tableOfVar);
        }

        if(tableOfList.containsKey(buffer.peek())){
            list = tableOfList.get(buffer.pop());
        } else {
            flag = true;
            set = tableOfSet.get(buffer.pop());
        }

        switch (colOp) {
            case "add":
                if(!flag){
                    list.add(String.valueOf(a));
                } else {
                    set.add(String.valueOf(a));
                }
                break;
            case "get":
                if(!flag){
                    s = list.get(a);
                } else {
                    System.err.println();
                    System.exit(7);
                }
                break;
            case "remove":
                if(!flag){
                    s = list.remove(a);
                } else {
                    if(set.remove(String.valueOf(a))){
                        s = "1";
                    } else {
                        s = "0";
                    }
                }
                break;
            case "size":
                if(!flag){
                    s = String.valueOf(list.getSize());
                } else {
                    s = String.valueOf(set.getSize());
                }
                break;
        }

        if(!s.equals("")){
            buffer.push(String.valueOf(s));
        }

        set = null;
        list = null;
    }

    private void initialization(String nameOfVar){
        tableOfVar.put(nameOfVar, 0);
    }

    private boolean checkInitialization(String key){
        boolean checkInitialization = false;

        if(tableOfVar.containsKey(key) || tableOfList.containsKey(key) || tableOfSet.containsKey(key)){
            checkInitialization = true;
        } else{
            System.err.println("Error: Cannot resolve symbol \"" + key + "\"");
            System.exit(4);
        }

        return checkInitialization;
    }

    private void assignOp(){
        a = valueOrVariable(tableOfVar);
        tableOfVar.put(buffer.pop(), a);
    }

    private int valueOrVariable(Map<String, Integer> table) throws EmptyStackException {
        if(isDigit(buffer.peek())){
            return Integer.valueOf(buffer.pop());
        } else if(!isDigit(buffer.peek())){
            return table.get(buffer.pop());
        } else{
            System.err.println();
            System.exit(10);
        }

        return -1;
    }

    private static boolean isDigit(String s) throws NumberFormatException {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
