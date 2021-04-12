package tanawar_objects;
/**
 *
 */

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import apis.FirebaseConstants.Collections.Users;
import utils.AccountType;
import utils.TanawarCalendar;

/**
 *
 */
public class User {
    private String fname;
    private String lname;
    private String email;
    private String password;
    private Calendar birthday;
    private AccountType accountType;
    private String phone;
    private Calendar registeredDate;
    // address data
    private String address;
    private double latitude;
    private double longitude;
    private String profileImGURL;
    /**
     *
     * @param fname
     * @param lname
     * @param email
     * @param password
     * @param birthday
     * @param accountType
     * @param phone
     * @param registeredDate
     */
    public User(String fname, String lname, String email, String password,
                Calendar birthday, AccountType accountType, String phone, Calendar registeredDate,
                String address, double latitude, double longitude, String profileImGURL
    ) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.accountType = accountType;
        this.phone = phone;
        this.registeredDate = registeredDate;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.profileImGURL = profileImGURL;
    }


    public String getProfileImGURL() {
        return profileImGURL;
    }

    public void setProfileImGURL(String profileImGURL) {
        this.profileImGURL = profileImGURL;
    }

    /**
     *
     * @param email
     * @param password
     */
    public User(String email, String phone , String password) {
        this.email = email;
        this.phone = phone ;
        this.password = password;
    }
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Calendar getBirthday() {
        return birthday;
    }

    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Calendar getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Calendar registeredDate) {
        this.registeredDate = registeredDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "User{" +
                "fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birthday=" + birthday +
                ", accountType=" + accountType +
                ", phone='" + phone + '\'' +
                ", registeredDate=" + registeredDate +
                '}';
    }
    /**
     *
     * @return
     */
    public Map<String,String> getMapCollection(){
        Map<String,String> col = new HashMap<>();
        col.put(Users.FIRST_NAME,this.fname);
        col.put(Users.LAST_NAME,this.lname);
        col.put(Users.EMAIL,this.email);
        col.put(Users.PASSWORD,this.password);
        String birthday = this.birthday.get(Calendar.DAY_OF_MONTH)+"/"+
                this.birthday.get(Calendar.MONTH)+"/"+
                this.birthday.get(Calendar.YEAR);
        col.put(Users.BIRTHDAY,birthday);
        col.put(Users.ACCOUNT_TYPE,this.accountType.getValue());
        col.put(Users.PHONE,this.phone);
        col.put(Users.REGISTER_DATE, TanawarCalendar.getDateTimeAsString(this.registeredDate));
        // add address data
        col.put(Users.ADDRESS,this.address);
        col.put(Users.LATITUDE,this.latitude+"");
        col.put(Users.LONGITUDE,longitude+"");
        return col;
    }

    public String getDisplayName() {
        return fname+" "+lname;
    }
}
