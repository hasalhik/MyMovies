package com.example.mymovies.data;

public class Trailer {
    private String linkToVideo;
    private String nameVideo;
    private String keyOfTrailer;

    public Trailer(String linkToVideo, String nameVideo, String keyOfTrailer) {
        this.linkToVideo = linkToVideo;
        this.nameVideo = nameVideo;
        this.keyOfTrailer = keyOfTrailer;
    }

    public String getKeyOfTrailer() {
        return keyOfTrailer;
    }

    public void setKeyOfTrailer(String keyOfTrailer) {
        this.keyOfTrailer = keyOfTrailer;
    }

    public String getLinkToVideo() {
        return linkToVideo;
    }

    public void setLinkToVideo(String linkToVideo) {
        this.linkToVideo = linkToVideo;
    }

    public String getNameVideo() {
        return nameVideo;
    }

    public void setNameVideo(String nameVideo) {
        this.nameVideo = nameVideo;
    }
}
