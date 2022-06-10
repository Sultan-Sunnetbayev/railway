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
@Table(name = "stations")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "full_name")
    @NotBlank(message = "full name station's is mandatory")
    @NotEmpty(message = "full name station's is empty")
    private String fullName;
    @Column(name = "short_name")
    @NotBlank(message = "short name station's is mandatory")
    @NotEmpty(message = "shor name station's is empty")
    private String shortName;
    @Column(name = "status")
    @NotEmpty(message = "status station's is empty")
    @NotBlank(message = "status station's is mandatory")
    private String status;
    @Column(name = "address")
    private String address;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "sequence")
    private Integer sequence;
    @Column(name = "created")
    @CreationTimestamp
    private Date created;
    @Column(name = "updated")
    @UpdateTimestamp
    private Date updated;

}