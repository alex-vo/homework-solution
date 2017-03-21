package config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Created by alex on 17.18.3.
 */
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{Config.class};
    }


    protected Class<?>[] getServletConfigClasses() {
        return null;
    }


    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}
