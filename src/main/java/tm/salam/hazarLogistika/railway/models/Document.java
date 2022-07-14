package tm.salam.hazarLogistika.railway.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "document")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    @NotEmpty(message = "document name is empty")
    private String name;
    @Column(name = "import")
    private String importt;
    @Column(name = "export")
    private String export;
    @Column(name = "instruction")
    private String instruction;
    @Column(name = "dispatch")
    private String dispatch;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "logist_name")
    private String logistName;
    @Column(name = "logist_surname")
    private String logistSurname;
    @Column(name = "status")
    private boolean status;
    @Column(name = "created")
    @CreationTimestamp
    private Date created;
    @Column(name = "updated")
    @UpdateTimestamp
    private Date updated;

}
