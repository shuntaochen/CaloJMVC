package org.caloch.core;

public class JsonResult {
    public boolean success;
    public Object result;

    public static JsonResult ok() {
        JsonResult ret = new JsonResult();
        ret.success = true;
        ret.result = null;
        return ret;
    }

    public static JsonResult ok(Object src) {
        JsonResult ret = new JsonResult();
        ret.success = true;
        ret.result = src;
        return ret;
    }

    public static JsonResult fail() {
        JsonResult ret = new JsonResult();
        ret.success = false;
        ret.result = null;
        return ret;
    }

    public static JsonResult fail(Object src) {
        JsonResult ret = new JsonResult();
        ret.success = false;
        ret.result = src;
        return ret;
    }
}
