package tm.salam.hazarLogistika.railway.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StationDTO {

    private int id;
    private String fullName;
    private String shortName;
    private String status;

    @Override
    public String toString() {
        return "StationDTO{" +
                "fullName='" + fullName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
