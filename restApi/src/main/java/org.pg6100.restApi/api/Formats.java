package org.pg6100.restApi.api;

public interface Formats {
    String BASE_JSON = "application/json; charset=UTF-8";
    //note the "vnd." (which starts for "vendor") and the
    // "+json" (ie, treat it having json structure)
    String V2_JSON = "application/vnd.pg6100.quiz+json; charset=UTF-8; version=2";
}
