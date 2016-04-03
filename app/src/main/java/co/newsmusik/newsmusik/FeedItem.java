package co.newsmusik.newsmusik;

import java.io.Serializable;

/**
 * Created by deki kurnia on 23/03/16.
 */
public class FeedItem implements Serializable {
    private static final long serialVersionUID = 0L;
    private String title;
    private String thumbnail;
    private String category;
    private String date;
    private String contentDetail;
    private String imageCredit;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContentDetail() {
        return contentDetail;
    }

    public void setContentDetail(String contentDetail) {
        this.contentDetail = contentDetail;
    }

    public String getImageCredit() {
        return imageCredit;
    }

    public void setImageCredit(String imageCredit) {
        this.imageCredit = imageCredit;
    }
}
