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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "status_vans")
public class StatusVan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "code")
    @NotEmpty(message = "cod status van's is empty")
    @NotBlank(message = "cod status van's is mandatory")
    private String code;
    @Column(name = "full_name")
    @NotBlank(message = "full name status van's is mandatory")
    @NotEmpty(message = "full name status van's is empty")
    private String fullName;
    @Column(name = "short_name")
    @NotBlank(message = "short name status van's is mandatory")
    @NotEmpty(message = "shor name status van's is empty")
    private String shortName;
    @Column(name = "created")
    @CreationTimestamp
    private Date created;
    @Column(name = "updated")
    @UpdateTimestamp
    private Date updated;

}
