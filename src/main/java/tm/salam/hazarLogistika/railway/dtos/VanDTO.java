package tm.salam.hazarLogistika.railway.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
    private Double yearBuilding;
    private Double periodDuty;
    private Double endOfTheDuty;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateRepear;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateNextRepear;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
