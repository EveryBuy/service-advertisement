package ua.everybuy.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Setter
@Getter
@Embeddable
public class AdvertisementStatistics {
    @Column(name = "views", nullable = false)
    private Integer views;

    @Column(name = "favourite_count", nullable = false)
    private Integer favouriteCount;

    public AdvertisementStatistics() {
        this.views = 0;
        this.favouriteCount = 0;
    }

    public void incrementViews() {
        views++;
    }

    public void incrementFavouriteCount() {
        favouriteCount++;
    }
}
