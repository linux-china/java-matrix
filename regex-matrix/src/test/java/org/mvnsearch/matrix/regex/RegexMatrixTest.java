package org.mvnsearch.matrix.regex;

import junit.framework.TestCase;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

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
     * oro perl5 pattern
     */
    org.apache.oro.text.regex.Pattern oroPerl5Pattern = null;

    /**
     * init logic
     *
     * @throws Exception exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //oro
        Perl5Compiler perl5Compiler = new Perl5Compiler();
        oroPerl5Pattern = perl5Compiler.compile(emailPattern);
    }

    /**
     * spike test
     *
     * @throws Exception exception
     */
    public void testSpike() throws Exception {
        String email = "linux_china@hotmail.com";
        System.out.println(oro(email));
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
            oro(email);
        }
        System.out.println(count + " loop:");
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            jdk(email);
        }
        long end1 = System.currentTimeMillis();
        System.out.println("jdk:" + (end1 - start));
        for (int i = 0; i < count; i++) {
            oro(email);
        }
        long end2 = System.currentTimeMillis();
        System.out.println("oro:" + (end2 - end1));

    }

    /**
     * jdk regex
     *
     * @param text text
     * @return result
     */
    private boolean jdk(String text) {
        Matcher matcher = jdkRegexPattern.matcher(text);
        boolean result = matcher.find();
        matcher.group();
        return result;
    }

    /**
     * oro regex
     *
     * @param text text
     * @return result
     */
    private boolean oro(String text) {
        Perl5Matcher matcher = new Perl5Matcher();
        boolean result = matcher.matches(text, oroPerl5Pattern);
        matcher.getMatch().group(0);
        return result;
    }
}
