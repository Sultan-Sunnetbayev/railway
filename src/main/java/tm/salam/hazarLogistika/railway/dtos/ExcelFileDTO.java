package tm.salam.hazarLogistika.railway.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcelFileDTO {

    private int id;
    private String name;
    private String path;
}
