package kz.izzi.tree.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import kz.izzi.tree.domain.enumeration.TreeType;

import kz.izzi.tree.domain.enumeration.EventType;

/**
 * A Application.
 */
@Entity
@Table(name = "application")
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private TreeType type;

    @NotNull
    @Column(name = "place_latitude", nullable = false)
    private Double placeLatitude;

    @NotNull
    @Column(name = "place_longitude", nullable = false)
    private Double placeLongitude;

    @Column(name = "plate_text")
    private String plateText;

    @Column(name = "seeding_date")
    private LocalDate seedingDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "event")
    private EventType event;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TreeType getType() {
        return type;
    }

    public Application type(TreeType type) {
        this.type = type;
        return this;
    }

    public void setType(TreeType type) {
        this.type = type;
    }

    public Double getPlaceLatitude() {
        return placeLatitude;
    }

    public Application placeLatitude(Double placeLatitude) {
        this.placeLatitude = placeLatitude;
        return this;
    }

    public void setPlaceLatitude(Double placeLatitude) {
        this.placeLatitude = placeLatitude;
    }

    public Double getPlaceLongitude() {
        return placeLongitude;
    }

    public Application placeLongitude(Double placeLongitude) {
        this.placeLongitude = placeLongitude;
        return this;
    }

    public void setPlaceLongitude(Double placeLongitude) {
        this.placeLongitude = placeLongitude;
    }

    public String getPlateText() {
        return plateText;
    }

    public void setPlateText(String plateText) {
        this.plateText = plateText;
    }

    public LocalDate getSeedingDate() {
        return seedingDate;
    }

    public Application seedingDate(LocalDate seedingDate) {
        this.seedingDate = seedingDate;
        return this;
    }

    public void setSeedingDate(LocalDate seedingDate) {
        this.seedingDate = seedingDate;
    }

    public EventType getEvent() {
        return event;
    }

    public Application event(EventType event) {
        this.event = event;
        return this;
    }

    public void setEvent(EventType event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public Application user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Application application = (Application) o;
        if (application.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), application.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Application{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", placeLatitude='" + getPlaceLatitude() + "'" +
            ", placeLongitude='" + getPlaceLongitude() + "'" +
            ", seedingDate='" + getSeedingDate() + "'" +
            ", event='" + getEvent() + "'" +
            "}";
    }
}
