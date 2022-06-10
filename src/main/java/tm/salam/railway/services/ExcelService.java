package tm.salam.railway.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface ExcelService {

    List<HashMap<Integer, List<Object>>> read(String filename) throws IOException;
}
