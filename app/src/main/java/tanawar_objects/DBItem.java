package tanawar_objects;

public class DBItem {
    private String publishTime;
    private String title;
    private String description;
    private String fileURL;

    public DBItem(String publishTime, String title, String description, String fileURL) {
        this.publishTime = publishTime;
        this.title = title;
        this.description = description;
        this.fileURL = fileURL;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }
}
