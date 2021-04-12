package utils;

public enum PrivateLessonType {
    ONLINE_MEETING("https://firebasestorage.googleapis.com/v0/b/tanawar-mr.appspot.com/o/Images%2Fabstract%20images%2Fonlineic.png?alt=media&token=639879d3-8541-442b-bead-00646ff7374b"),
    FACE_TO_FACE("https://firebasestorage.googleapis.com/v0/b/tanawar-mr.appspot.com/o/Images%2Fabstract%20images%2Ffacetoface.png?alt=media&token=36adf40c-1366-4654-8b4b-56baa84e1a15");
    private String imgPath;
    PrivateLessonType(String imgPath){
        this.imgPath = imgPath;
    }

    public static PrivateLessonType getByValue(String s) {
        if("ONLINE_MEETING".equals(s)){
            return  ONLINE_MEETING;
        }else if("FACE_TO_FACE".equals(s)){
            return FACE_TO_FACE;
        }else{
            return null;
        }
    }

    public String getImgPath() {
        return imgPath;
    }
}
