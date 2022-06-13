package tm.salam.hazarLogistika.railway.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataDTO {

    private int id;
    private String numberVan;
    private String codeOfTheProperty;
    private String lastStation;
    private String currentStation;
    private String statusVan;
    private Double year;
    private String date;
    private String time;
    @Temporal(TemporalType.TIMESTAMP)
    private Date yearDateTime;
    private String typeVan;
    private String setStation;
    private Long hourForPassedWay;
    private Long dayForRepair;
    private String indexTrain;

    @Override
    public String toString() {
        return "DataDTO{" +
                "id=" + id +
                ", numberVan=" + numberVan +
                ", codeOfTheProperty='" + codeOfTheProperty + '\'' +
                ", currentStation='" + currentStation + '\'' +
                ", statusVan='" + statusVan + '\'' +
                ", date=" + date +
                ", typeVan='" + typeVan + '\'' +
                ", setStation='" + setStation + '\'' +
                ", indexTrain='" + indexTrain + '\'' +
                '}';
    }
}
