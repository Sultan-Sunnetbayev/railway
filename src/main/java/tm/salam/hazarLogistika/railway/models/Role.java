package tm.salam.hazarLogistika.railway.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    @NotEmpty(message = "name role's is empty")
    @NotBlank(message = "name role's is mandatory")
    private String name;
    @CreationTimestamp
    @Column(name = "created")
    private Date created;
    @UpdateTimestamp
    @Column(name = "updated")
    private Date updated;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<User>users;
}
