package tm.salam.hazarLogistika.railway.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTransfer {

    private String message;
    private Boolean status;

    @Override
    public String toString() {
        return "ResponseTransfer{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
