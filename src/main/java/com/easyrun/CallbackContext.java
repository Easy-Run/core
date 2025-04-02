package com.easyrun;

import org.json.JSONObject;

public interface CallbackContext {
    void success(Object message);
    void error(Object message);
    void progress(JSONObject progress);
}
