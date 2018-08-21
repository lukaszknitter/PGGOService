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
    protected long id;

    @Column(columnDefinition = "nvarchar(255)")
    protected String name;

    @Column(columnDefinition = "nvarchar(255)")
    protected String tag;

    protected ZonedDateTime createDate;

    @PrePersist
    protected void setUpCreateDate() {
        createDate = ZonedDateTime.now();
    }
}
