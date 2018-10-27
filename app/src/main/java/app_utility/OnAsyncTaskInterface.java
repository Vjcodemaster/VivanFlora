package app_utility;

import java.util.LinkedHashMap;

public interface OnAsyncTaskInterface {
    void onAsyncTaskComplete(String sCase, int nFlag, LinkedHashMap<String, Integer> lhmData);
}
