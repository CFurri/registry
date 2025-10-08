package cat.uvic.teknos.registry.server;

import cat.uvic.teknos.registry.server.controllers.EmployeeController;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.RawHttp;

import java.io.IOException;

//Interpreta la petició (ex. "GET/employees") i crida el controlador adequat.
/**
 * Classe que analitza les peticions dels clients,
 * invoca el controlador apropiat i genera les respostes.
 */
public class RequestRouter {
    private final RawHttp http;
    private final EmployeeController employeeController;

    public RequestRouter(EmployeeController employeeController) {
        this.http = new RawHttp();
        this.employeeController = employeeController;
    }

    public RawHttpResponse<?> route(RawHttpRequest request) {
        try {
            String path = request.getUri().getPath();
            String method = request.getMethod();

            //Exemple: /employees o /employees/1
            if(path.startsWith("/employees")) {
                return switch (method) {
                    case "GET" -> employeeController.handleGet(request);
                    case "POST" -> employeeController.handPost(request);
                    case "PUT" -> employeeController.handlePut(request);
                    case "DELETE" -> employeeController.handleDelete(request);
                    default -> http.parseResponse("HTTP/1.1 405 Method Not Allowed\n\n");
                };
            }

            //Si no es cap ruta coneguda:
            return http.parseResponse("HTTP/1.1 404 Not Found\n\n");
        } catch (Exception e) {
            e.printStackTrace();
            return http.parseResponse("HTTP/1.1 500 Internal Server Error\n\n");
        }
    }
}

