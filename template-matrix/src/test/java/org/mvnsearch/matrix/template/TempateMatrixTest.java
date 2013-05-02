package org.mvnsearch.matrix.template;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import de.neuland.jade4j.Jade4J;
import de.neuland.jade4j.template.JadeTemplate;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.perf4j.StopWatch;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * template matrix test
 *
 * @author linux_china
 */
public class TempateMatrixTest extends TestCase {
    /**
     * handlebars template
     */
    private Template handlebarsTemplate = null;
    JadeTemplate jadeTemplate;
    /**
     * velocity template
     */
    private org.apache.velocity.Template velocityTemplate = null;
    /**
     * freemarker template
     */
    private freemarker.template.Template freemarkerTemplate = null;

    /**
     * init logic
     *
     * @throws Exception exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //init handlebars
        String templateContent = IOUtils.toString(this.getClass().getResourceAsStream("/template/demo.hbs"));
        Handlebars handlebars = new Handlebars();
        this.handlebarsTemplate = handlebars.compileInline(templateContent);
        //jade4j
        this.jadeTemplate = Jade4J.getTemplate("/Users/linux_china/github/java-matrix/template-matrix/src/test/resources/template/demo.jade");
        //velocity
        Velocity.init();
        Properties p = new Properties();
        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        p.setProperty("resource.loader", "class");
        p.setProperty("input.encoding", "UTF-8");
        p.setProperty("output.encoding", "UTF-8");
        VelocityEngine engine = new VelocityEngine(p);
        this.velocityTemplate = engine.getTemplate("/template/demo.vm");
        //freemarker
        Configuration cfg = new Configuration();
        cfg.setClassForTemplateLoading(this.getClass(), "/");
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        this.freemarkerTemplate = cfg.getTemplate("template/demo.ftl");
    }

    /**
     * spike test
     *
     * @throws Exception exception
     */
    public void testSpike() throws Exception {
        Map<String, Object> context = constructContext();
        VelocityContext context2 = new VelocityContext(context);
        System.out.println(freemarker(context));
    }

    /**
     * matrix test
     *
     * @throws Exception exception
     */
    public void testMatrix() throws Exception {
        int count = 100000;
        Map<String, Object> context = constructContext();
        VelocityContext context2 = new VelocityContext(context);
        //hot swap
        for (int i = 0; i < 10000; i++) {
            handlebars(context);
            velocity(context2);
            freemarker(context);
            jad4j(context);
        }
        System.out.println(count + " loop");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("handlebars");
        for (int i = 0; i < count; i++) {
            handlebars(context);
        }
        stopWatch.stop("handlebars");
        System.out.println(stopWatch.getTag() + ":" + stopWatch.getElapsedTime());
        stopWatch.start("velocity");
        for (int i = 0; i < count; i++) {
            velocity(context2);
        }
        stopWatch.stop("velocity");
        System.out.println(stopWatch.getTag() + ":" + stopWatch.getElapsedTime());
        stopWatch.start("freemarker");
        for (int i = 0; i < count; i++) {
            freemarker(context);
        }
        stopWatch.stop("freemarker");
        System.out.println(stopWatch.getTag() + ":" + stopWatch.getElapsedTime());
        stopWatch.start("jade");
        for (int i = 0; i < count; i++) {
            jad4j(context);
        }
        stopWatch.stop("jade");
        System.out.println(stopWatch.getTag() + ":" + stopWatch.getElapsedTime());
    }


    /**
     * handlebars render
     *
     * @param context context
     * @return output
     * @throws Exception exception
     */
    private String handlebars(Map<String, Object> context) throws Exception {
        return handlebarsTemplate.apply(context);
    }

    /**
     * velocity render
     *
     * @param context context
     * @return output
     * @throws Exception exception
     */
    private String velocity(VelocityContext context) throws Exception {
        StringWriter writer = new StringWriter();
        velocityTemplate.merge(context, writer);
        return writer.toString();
    }

    /**
     * freemarker render
     *
     * @param context context
     * @return output
     * @throws Exception exception
     */
    private String freemarker(Map<String, Object> context) throws Exception {
        StringWriter writer = new StringWriter();
        freemarkerTemplate.process(context, writer);
        return writer.toString();
    }

    /**
     * jad4j render
     *
     * @param context context
     * @return html code
     * @throws Exception exception
     */
    private String jad4j(Map<String, Object> context) throws Exception {
        return Jade4J.render(jadeTemplate, context);
    }

    /**
     * construct context
     *
     * @return context
     */
    private Map<String, Object> constructContext() {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("nick", "陈立兵");
        context.put("description", "Please give me a good description");
        User user = new User();
        user.setId(1);
        user.setNick("linux_china");
        context.put("user", user);
        return context;
    }
}
