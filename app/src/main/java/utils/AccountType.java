package utils;

/**
 *
 */
public enum AccountType {
    /**
     *
     */
    TEACHER("Teacher"),STUDENT("Student"),PARENT("Parent");
    /**
     *
     */
    private String value;
    /**
     *
     * @param val
     */
    AccountType(String val){
        this.value = val;
    }
    /**
     *
     * @return
     */
    public String getValue() {
        return value;
    }
    /**
     *
     * @param value
     * @return
     */
    public static AccountType getAccounType(String value){
        switch (value){
            case "Teacher":
                return TEACHER;
            case "Student":
                return STUDENT;
            case "Parent":
                return PARENT;
            default:
                break;
        }
        return STUDENT;
    }
}
