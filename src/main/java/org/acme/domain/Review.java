package org.acme.domain;

import io.smallrye.common.constraint.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.*;

import java.util.Objects;

/**
 * A Review.
 */
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Getter
@Setter
@Table(name = "reviews")
public class Review extends AbstractEntity {

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "rating", nullable = false)
    private Long rating;

    public Review(@NotNull String title, @NotNull String description,
                  @NotNull Long rating) {
        this.title = title;
        this.description = description;
        this.rating = rating;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(title, review.title) &&
                Objects.equals(description, review.description) &&
                Objects.equals(rating, review.rating);
    }
    @Override
    public int hashCode() {
        return Objects.hash(title, description, rating);
    }
}
