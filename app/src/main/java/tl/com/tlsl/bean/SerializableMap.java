package tl.com.tlsl.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by admin on 2017/10/27.
 */

public class SerializableMap implements Serializable {
    private Map<String,Object> map;

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
