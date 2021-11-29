package sk.qpp.poc.documents.commonparts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.rest.core.annotation.RestResource;
import sk.qpp.poc.user.repository.PocUser;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Common entity class with common fields for many of our entities. At least each entity which can be created/updated
 * by {@link PocUser} should extend this class to have transparently filled/updated common
 * fields defined here.
 * <p>
 * Class is highly inspired by tutorial available here:
 * https://www.callicoder.com/hibernate-spring-boot-jpa-one-to-many-mapping-example/
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdAt", "updatedAt"},
        allowGetters = true
)
@Getter
@SuperBuilder(toBuilder = true)
public abstract class AuditModel implements Serializable {
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    // hibernate way: @CreationTimestamp
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private Date updatedAt;

    /**
     * Only constructor. Fields {@link #updatedAt} and {@link #createdAt} are populated by spring magic.
     */
    public AuditModel() {
    }
}
