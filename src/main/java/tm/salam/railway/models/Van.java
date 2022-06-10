package tm.salam.railway.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
import java.time.Year;
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
//    @NotEmpty(message = "year building is empty")
//    @NotBlank(message = "year building is mandatory")
    private Integer yearBuilding;
    @Column(name = "period_duty")
//    @NotEmpty(message = "period duty is empty")
//    @NotBlank(message = "period duty is mandatory")
    private Double periodDuty;
    @Column(name = "end_of_the_duty")
//    @NotBlank(message = "end of the duty van's is mandatory")
//    @NotEmpty(message = "end of the duty van's is empty")
    private Double endOfTheDuty;
    @Column(name = "date_repear")
//    @NotBlank(message = "date repear van's is mandatory")
//    @NotEmpty(message = "date repear van's is empty")
    private Date dateRepear;
    @Column(name = "date_next_repear")
//    @NotBlank(message = "date next repear van's is mandatory")
//    @NotEmpty(message = "date next repear van's is empty")
    private Date dateNextRepear;
    @Column(name = "date_act")
//    @NotBlank(message = "date act van's is mandatory")
//    @NotEmpty(message = "date act van's is empty")
    private Date dateAct;
    @Column(name = "period_lease")
//    @NotBlank(message = "period lease van's is mandatory")
//    @NotEmpty(message = "period lease van's is empty")
    private String periodLease;
    @Column(name = "comment")
    private String comment;

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
