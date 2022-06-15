package tm.salam.hazarLogistika.railway.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDTO {

    private int id;
    private String name;
    private String importt;
    private String export;
    private String instruction;
    private String dispatch;
    private Date created;

    @Override
    public String toString() {
        return "DocumentDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", importt='" + importt + '\'' +
                ", export='" + export + '\'' +
                ", instruction='" + instruction + '\'' +
                ", dispatch='" + dispatch + '\'' +
                ", created=" + created +
                '}';
    }
}
