package util;

import org.junit.Test;
import org.slf4j.Logger;

import static org.junit.Assert.*;

public class LogUtilsTest {

    @Test
    public void getLoggerTest() {
        Logger logger = LogUtils.getInstance();
        assertNotNull(logger);
    }

    @Test
    public void getCallerClassNameTest() {
        String name = LogUtils.getCallerClassName();
        assertEquals(this.getClass().getName(), name);
    }
}