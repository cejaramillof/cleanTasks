package app.demo.web.task;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

//Necesario para ejecutar los test de los RestControllers
@SpringBootApplication
class TestApp {
    public static void main(String[] args) {
        run(TestApp.class, args);
    }
}
