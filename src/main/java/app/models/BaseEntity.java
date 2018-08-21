package app.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue
    public long id;

    @Column(columnDefinition = "nvarchar(255)")
    public String name;

    @Column(columnDefinition = "nvarchar(255)")
    public String tag;

    public ZonedDateTime createDate;

    @PrePersist
    public void setUpCreateDate() {
        createDate = ZonedDateTime.now();
    }
}
