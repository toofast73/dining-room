package ru.live.toofast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;

@SpringBootApplication
@EnableRetry
public class DiningHallSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiningHallSimulatorApplication.class, args);
    }

    @Bean
    public MyWebAppInitializer myWebAppInitializer() {


        return new MyWebAppInitializer();
    }


    private class MyWebAppInitializer implements WebApplicationInitializer {

        @Override
        public void onStartup(ServletContext container) {
            // Create the 'root' Spring application context
            AnnotationConfigWebApplicationContext rootContext =
                    new AnnotationConfigWebApplicationContext();
            rootContext.register(DiningHallSimulatorApplication.class);

            // Manage the lifecycle of the root application context
            container.addListener(new ContextLoaderListener(rootContext));

            // Create the dispatcher servlet's Spring application context
            AnnotationConfigWebApplicationContext dispatcherContext =
                    new AnnotationConfigWebApplicationContext();

////			dispatcherContext.register(DispatcherConfig.class);
//
//			// Register and map the dispatcher servlet
//			ServletRegistration.Dynamic dispatcher =
//					container.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
//			dispatcher.setLoadOnStartup(1);
//			dispatcher.addMapping("/");
        }

    }
}
