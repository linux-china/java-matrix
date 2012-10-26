package org.mvnsearch.matrix.regex;

import junit.framework.TestCase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * regex matrix test
 *
 * @author linux_china
 */
public class RegexMatrixTest extends TestCase {
    /**
     * email regex pattern
     */
    private String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?\\^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    /**
     * jdk regex pattern
     */
    private Pattern jdkRegexPattern = Pattern.compile(emailPattern);

    /**
     * spike test
     *
     * @throws Exception exception
     */
    public void testSpike() throws Exception {
        String email = "linux_china@hotmail.com";
        System.out.println(jdk(email));
    }

    /**
     * matrix test
     *
     * @throws Exception exception
     */
    public void testMatrix() throws Exception {
        int count = 100000;
        String email = "linux_china@hotmail.com";
        //hot swap
        for (int i = 0; i < 10000; i++) {
            jdk(email);
        }
        System.out.println(count + " loop:");
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            jdk(email);
        }
        long end1 = System.currentTimeMillis();
        System.out.println("jdk:" + (end1 - start));
    }

    /**
     * jdk regex pattern
     *
     * @param text text
     * @return result
     */
    private boolean jdk(String text) {
        Matcher matcher = jdkRegexPattern.matcher(text);
        return matcher.find();
    }
}
