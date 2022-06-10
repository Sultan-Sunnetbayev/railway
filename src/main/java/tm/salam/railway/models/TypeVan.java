package tm.salam.railway.models;

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
@Table(name = "van_types")
public class TypeVan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "full_name")
    @NotBlank(message = "full name type wagon's is mandatory")
    @NotEmpty(message = "full name type wagon's is empty")
    private String fullName;
    @Column(name = "short_name")
    @NotBlank(message = "short name type wagon's is mandatory")
    @NotEmpty(message = "shor name type wagon's is empty")
    private String shortName;
    @Column(name = "created")
    @CreationTimestamp
    private Date created;
    @Column(name = "updated")
    @UpdateTimestamp
    private Date updated;

}