package ru.otus.servlet;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class TemplateProcessor {
    ClassLoader classLoad = Thread.currentThread().getContextClassLoader();
    private static final String HTML_DIR = "tml/";//"/tml/";
    private final Configuration configuration;

    public TemplateProcessor() throws IOException {
        configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setDirectoryForTemplateLoading(new File(HTML_DIR));
        configuration.setDefaultEncoding("UTF-8");
        //System.out.println(""+new File(HTML_DIR).getAbsolutePath());
    }

    String getPage(String filename, Map<String, Object> data) throws IOException {
        try (Writer stream = new StringWriter();) {
            Template template = configuration.getTemplate(filename);
            template.process(data, stream);
            return stream.toString();
        } catch (TemplateException e) {
            throw new IOException(e);
        }
    }
}
