package tm.salam.hazarLogistika.railway.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutputDataDTO {

    private int id;
    private String numberVan;
    private String lastStation;
    private String currentStation;
    private String statusVan;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    private String typeVan;
    private String setStation;
    private Boolean act;
    private Long hourForPassedWay;
    private Long dayForRepair;
    private String indexVan;
    private String type;
    private String color;
    private String description;


    @Override
    public String toString() {
        return "OutputDataDTO{" +
                "numberVan='" + numberVan + '\'' +
                ", lastStation='" + lastStation + '\'' +
                ", currentStation='" + currentStation + '\'' +
                ", statusVan='" + statusVan + '\'' +
                ", date=" + date +
                ", typeVan='" + typeVan + '\'' +
                ", setStation='" + setStation + '\'' +
                ", hourForPassedWay=" + hourForPassedWay +
                ", dayForRepair=" + dayForRepair +
                '}';
    }
}
