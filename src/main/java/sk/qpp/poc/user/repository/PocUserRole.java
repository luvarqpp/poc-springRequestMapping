package sk.qpp.poc.user.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;

import javax.persistence.*;

// TODO use postgresql enum type? https://vladmihalcea.com/the-best-way-to-map-an-enum-type-with-jpa-and-hibernate/
@Entity
@Table(name = "poc_user_roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PocUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "SMALLINT")
    @Type(type = "org.hibernate.type.ShortType")
    private Short id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private RoleName name;

    public PocUserRole(RoleName roleName) {
        this.name = roleName;
    }
}
