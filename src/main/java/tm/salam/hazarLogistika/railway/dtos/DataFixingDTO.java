package tm.salam.hazarLogistika.railway.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataFixingDTO {

    private int id;
    private String name;

    @Override
    public String toString() {
        return "DataFixingDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
