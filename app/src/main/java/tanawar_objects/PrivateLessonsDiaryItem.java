package tanawar_objects;

import java.util.Calendar;
import java.util.Objects;

import utils.PrivateLessonType;

/**
 * a class to save the lesson data.
 */
public class PrivateLessonsDiaryItem {
    /**
     * the lesson number.
     */
    private String teacherEmail;
    /**
     * the lesson number.
     */
    private String teacherName;
    /**
     *
     */
    private String studentsEmail;
    /**
     *
     */
    private String studentsName;
    /**
     * the lesson type, online or face to face
     */
    private PrivateLessonType lessonType;
    /**
     * the lesson participants number
     */
    private int participantsNum;
    /**
     * the date and time of the lesson
     */
    private Calendar time;
    /**
     * the meeting url
     */
    private String meetingURL;
    /**
     * the lesson subject
     */
    private String subject;
    /**
     * the lesson's category
     */
    private String category;
    /**
     * the lesson's description
     */
    private String description;
    /**
     * a full constructor
     * @param teacherEmail
     * @param lessonType
     * @param time
     * @param meetingURL
     * @param subject
     */
    public PrivateLessonsDiaryItem(String teacherEmail,
                                   PrivateLessonType lessonType,
                                   Calendar time, String meetingURL, String subject,
                                   String studentsName,
                                   String studentsEmail) {
        this.teacherEmail = teacherEmail;
        this.lessonType = lessonType;
        this.time = time;
        this.meetingURL = meetingURL;
        this.subject = subject;
        this.studentsEmail = studentsEmail;
        this.studentsName = studentsName;
    }
    /**
     * a full constructor
     * @param teacherEmail
     * @param lessonType
     * @param time
     * @param meetingURL
     * @param subject
     */
    public PrivateLessonsDiaryItem(String teacherEmail, String teacherName,
                                   PrivateLessonType lessonType,
                                   Calendar time, String meetingURL, String subject,
                                   String studentsName,
                                   String studentsEmail) {
        this.teacherEmail = teacherEmail;
        this.teacherName = teacherName;
        this.lessonType = lessonType;
        this.time = time;
        this.meetingURL = meetingURL;
        this.subject = subject;
        this.studentsEmail = studentsEmail;
        this.studentsName = studentsName;
    }
    public String getStudentsEmail() {
        return studentsEmail;
    }

    public void setStudentsEmail(String studentsEmail) {
        this.studentsEmail = studentsEmail;
    }

    public String getStudentsName() {
        return studentsName;
    }

    public void setStudentsName(String studentsName) {
        this.studentsName = studentsName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    public PrivateLessonType getLessonType() {
        return lessonType;
    }

    public void setLessonType(PrivateLessonType lessonType) {
        this.lessonType = lessonType;
    }

    public int getParticipantsNum() {
        return participantsNum;
    }

    public void setParticipantsNum(int participantsNum) {
        this.participantsNum = participantsNum;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public String getMeetingURL() {
        return meetingURL;
    }

    public void setMeetingURL(String meetingURL) {
        this.meetingURL = meetingURL;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrivateLessonsDiaryItem that = (PrivateLessonsDiaryItem) o;
        return Objects.equals(teacherEmail, that.teacherEmail) &&
                Objects.equals(studentsEmail, that.studentsEmail) &&
                lessonType == that.lessonType &&
                Objects.equals(time, that.time) &&
                Objects.equals(subject, that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherEmail, studentsEmail, lessonType, time, subject);
    }
}
