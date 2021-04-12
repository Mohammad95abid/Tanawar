package tanawar_objects;

public class Post {
    String title;
    String description;
    String category;
    String imgURL;
    String data_link;
    String postOwnerName;
    String post_publish_date;
    String post_publish_time;
    String postOwnerEmail;

    public Post(String title, String description, String img, String data_link,
                String postOwner,String post_publish_date,String post_publish_time,
                String postOwnerEmail, String category) {
        this.title = title;
        this.description = description;
        this.imgURL = img;
        this.data_link = data_link;
        this.postOwnerName = postOwner;
        this.post_publish_date = post_publish_date;
        this.post_publish_time = post_publish_time;
        this.postOwnerEmail = postOwnerEmail;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPost_publish_time() {
        return post_publish_time;
    }

    public void setPost_publish_time(String post_publish_time) {
        this.post_publish_time = post_publish_time;
    }

    public String getPostOwnerEmail() {
        return postOwnerEmail;
    }

    public void setPostOwnerEmail(String postOwnerEmail) {
        this.postOwnerEmail = postOwnerEmail;
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

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getData_link() {
        return data_link;
    }

    public void setData_link(String data_link) {
        this.data_link = data_link;
    }

    public String getPostOwnerName() {
        return postOwnerName;
    }

    public void setPostOwnerName(String postOwnerName) {
        this.postOwnerName = postOwnerName;
    }

    public String getPost_publish_date() {
        return post_publish_date;
    }

    public void setPost_publish_date(String post_publish_date) {
        this.post_publish_date = post_publish_date;
    }
}
