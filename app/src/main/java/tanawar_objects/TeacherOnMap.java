package tanawar_objects;

import utils.AccountType;

public class TeacherOnMap extends UserOnMap{
//    private String first_name;
//    private String last_name;
//    private String email;
//    private String phone;
//    private long latitude;
//    private long longitude;
//    private String profileImg;
    private boolean gender;

    public TeacherOnMap(AccountType accountType, String address, double latitude,
                        double longitude, String displayName, String email, String phone,
                        String description, String date, String time,boolean gender) {
        super(accountType, address, latitude, longitude, displayName, email,
                phone, description, date, time);
        this.gender = gender;
    }

    public boolean isMale() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }
    //    public TeacherOnMap(String first_name, String last_name, String email, long latitude,
//                        long longitude, String profileImg, String phone, String gender) {
//        this.first_name = first_name;
//        this.last_name = last_name;
//        this.email = email;
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.profileImg = profileImg;
//        this.phone = phone;
//        this.gender = gender;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getFirst_name() {
//        return first_name;
//    }
//
//    public void setFirst_name(String first_name) {
//        this.first_name = first_name;
//    }
//
//    public String getLast_name() {
//        return last_name;
//    }
//
//    public void setLast_name(String last_name) {
//        this.last_name = last_name;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public long getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(long latitude) {
//        this.latitude = latitude;
//    }
//
//    public long getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(long longitude) {
//        this.longitude = longitude;
//    }
//
//    public String getProfileImg() {
//        return profileImg;
//    }
//
//    public void setProfileImg(String profileImg) {
//        this.profileImg = profileImg;
//    }
//
//    @Override
//    public String toString() {
//        return "TeacherOnMap{" +
//                "first_name='" + first_name + '\'' +
//                ", last_name='" + last_name + '\'' +
//                ", email='" + email + '\'' +
//                '}';
//    }
}
