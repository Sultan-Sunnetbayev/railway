package tm.salam.railway.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StatusVanDTO {

    private int id;
    private String code;
    private String fullName;
    private String shortName;

    @Override
    public String toString() {
        return "StatusVanDTO{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", fullName='" + fullName + '\'' +
                ", shortName='" + shortName + '\'' +
                '}';
    }
}
