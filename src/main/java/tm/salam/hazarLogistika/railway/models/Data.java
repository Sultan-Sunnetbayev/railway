package tm.salam.hazarLogistika.railway.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

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
    private String codeOfTheProperty;
    @Column(name = "last_station")
    private String lastStation;
    @Column(name = "current_station")
    private String currentStation;
    @Column(name = "status_van")
    private String statusVan;
    @Column(name = "year")
    private Double year;
    @Column(name = "date")
    private String date;
    @Column(name = "time")
    private String time;
    @Column(name = "year_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date yearDateTime;
    @Column(name = "type_van")
    private String typeVan;
    @Column(name = "set_station")
    private String setStation;
    @Column(name = "hour_for_passed_way")
    private Long hourForPassedWay;
    @Column(name = "day_for_repair")
    private Long dayForRepair;
    @Column(name = "index_train")
    private String indexTrain;
    @Column(name = "act")
    private Boolean act;
    @Column(name = "color")
    private String color;
    @Column(name = "type")
    private String type;
    @Column(name = "description")
    private String description;
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
