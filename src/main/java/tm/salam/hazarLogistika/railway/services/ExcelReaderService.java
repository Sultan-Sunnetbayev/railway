package tm.salam.hazarLogistika.railway.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface ExcelReaderService {

    List<HashMap<Integer, List<Object>>> read(String filename) throws IOException;
}
