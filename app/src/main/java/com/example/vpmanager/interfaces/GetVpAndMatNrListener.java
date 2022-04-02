package com.example.vpmanager.interfaces;

import java.util.List;
import java.util.Map;

public interface GetVpAndMatNrListener {
    void onAllDataReady(String vps, String matNr, List<Map<String, Object>> datesList, List<Map<String, Object>> studiesList);
}
