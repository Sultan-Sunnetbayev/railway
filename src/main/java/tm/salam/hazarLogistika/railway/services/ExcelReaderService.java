package tm.salam.hazarLogistika.railway.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public interface ExcelReaderService {

    List<TreeMap<Integer, List<Object>>> read(String filename) throws IOException;
}
