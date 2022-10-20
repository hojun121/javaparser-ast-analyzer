import java.sql.Timestamp;

public class SimpleProgram {

    public static void main(String[] args) {
        // HardCodedSecretCheck
        String password = "passwd1234";
        String tokentest = "php_12dc";
        // SimpleClassNameCheck
        java.util.List<String> mylist; // Noncompilant
        Timestamp tstamp; // Compilant
    }

    // IfElseIfStatementEndsWithElseCheck
    public static void foo_camel() {
        if (x == 16) {
        }else if (x == 17) {
        }
        if (x == 16) {
        }
        if (x == 16) {
        } else if (x == 17) {
        } else if (x == 18) {
        } else if (x == 19) {
        }  // Noncompliant
    }
}