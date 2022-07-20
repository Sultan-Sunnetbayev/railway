package tm.salam.hazarLogistika.railway.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "vans")
public class Van {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "code")
    @NotBlank(message = "code van's is mandatory")
    @NotEmpty(message = "code van's is empty")
    private String code;
    @Column(name = "year_building")
    private Double yearBuilding;
    @Column(name = "period_duty")
    private Double periodDuty;
    @Column(name = "end_of_the_duty")
    private Double endOfTheDuty;
    @Column(name = "date_repear")
    private Date dateRepear;
    @Column(name = "date_next_repear")
    private Date dateNextRepear;
    @Column(name = "date_act")
    private Date dateAct;
    @Column(name = "period_lease")
    private String periodLease;
    @Column(name = "comment")
    private String comment;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "excel_file_id")
    private ExcelFile excelFile;
    @Override
    public String toString() {
        return "Van{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", yearBuilding=" + yearBuilding +
                ", periodDuty=" + periodDuty +
                ", endOfTheDuty=" + endOfTheDuty +
                ", dateRepear=" + dateRepear +
                ", dateNextRepear=" + dateNextRepear +
                ", dateAct=" + dateAct +
                ", periodLease='" + periodLease + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
