package tm.salam.railway.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "data")
public class Data {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "number_van")
    @NotEmpty(message = "number van is empty")
    @NotBlank(message = "number van is mandatory")
    private String numberVan;
    @Column(name = "code_of_the_property")
//    @NotEmpty(message = "code of the property is empty")
//    @NotBlank(message = "code of the property is mandatory")
    private String codeOfTheProperty;
    @Column(name = "current_station")
//    @NotBlank(message = "current station is mandatory")
//    @NotEmpty(message = "current station is empty")
    private String currentStation;
    @Column(name = "status_van")
//    @NotEmpty(message = "status van is empty")
//    @NotBlank(message = "status van is mandatory")
    private String statusVan;
    @Column(name = "year")
//    @NotEmpty(message = "year is empty")
//    @NotBlank(message = "year is mandatory")
    private Integer year;
    @Column(name = "date")
//    @NotEmpty(message = "date is empty")
//    @NotBlank(message = "date is mandatory")
    private String date;
    @Column(name = "time")
//    @NotEmpty(message = "time is empty")
//    @NotBlank(message = "time is mandatory")
    private String time;
    @Column(name = "type_van")
//    @NotEmpty(message = "type van is empty")
//    @NotBlank(message = "type van is mandatory")
    private String typeVan;
    @Column(name = "set_station")
//    @NotBlank(message = "set station is empty")
//    @NotEmpty(message = "set station is mandatory")
    private String setStation;
    @Column(name = "index_train")
//    @NotEmpty(message = "index train is empty")
//    @NotBlank(message = "index train is mandatory")
    private String indexTrain;
    @Column(name = "created")
    @CreationTimestamp
    private Date created;
    @Column(name = "updated")
    @UpdateTimestamp
    private Date updated;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "excel_file_id")
    private ExcelFile excelFile;

}
