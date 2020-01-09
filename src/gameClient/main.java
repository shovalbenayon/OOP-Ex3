package gameClient;

public class main {
    public static void main(String[] args) {

    }

    public static double valueFromString(String s){
        int val_index = s.indexOf("value");
        int tp_index = s.indexOf("type");
        String val = s.substring(val_index+6 , tp_index -2);
        System.out.println(val);
        return Double.parseDouble(val);
    }
}
