package tm.salam.railway.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Year;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VanDTO {

    private int id;
    private String code;
    private Integer yearBuilding;
    private Double periodDuty;
    private Double endOfTheDuty;
    private Date dateRepear;
    private Date dateNextRepear;
    private Date dateAct;
    private String periodLease;
    private String comment;

    @Override
    public String toString() {
        return "VanDTO{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", yearBuilding=" + yearBuilding +
                ", periodDuty=" + periodDuty +
                ", endOfTheDuty=" + endOfTheDuty +
                ", dateRepear=" + dateRepear +
                ", dateNextRepear=" + dateNextRepear +
                ", dateAct=" + dateAct +
                ", periodLease=" + periodLease +
                ", comment='" + comment + '\'' +
                '}';
    }
}
