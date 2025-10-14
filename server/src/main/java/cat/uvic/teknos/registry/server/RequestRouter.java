package cat.uvic.teknos.registry.server;

import cat.uvic.teknos.registry.app.DIManager;
import cat.uvic.teknos.registry.app.exceptions.DIException;
import cat.uvic.teknos.registry.server.controllers.EmployeeController;
import cat.uvic.teknos.registry.server.exceptions.BadRequestException;
import cat.uvic.teknos.registry.server.exceptions.ConflictException;
import cat.uvic.teknos.registry.server.exceptions.NotFoundException;


import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.RawHttpHeaders;
import rawhttp.core.body.StringBody;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Routes incoming requests to the appropriate controller.
 */

public class RequestRouter implements RawHttpService {

    private final EmployeeController employeeController;
    private final RawHttp http;

    public RequestRouter(DIManager diManager) throws DIException {
        this.employeeController = new EmployeeController(diManager);
        this.http = new RawHttp();
    }

    @Override // <-- CANVI: Anotació afegida
    public Optional<RawHttpResponse<?>> route(RawHttpRequest request) { // <-- CANVI: Nom del mètode canviat a "route"
        String path = request.getUri().getPath();

        try {
            // Lògica de ruteig
            // En un futur, aquí podries utilitzar un switch o un Map per a més rutes
            if (path.startsWith("/api/employees")) {
                // Deixem que el controlador gestioni la petició completa
                return employeeController.route(request); // Assumint que el controlador també implementa "route"
            }

            // Si cap ruta coincideix, el servidor retornarà un 404 per defecte
            return Optional.empty();

        } catch (NotFoundException e) {
            return Optional.of(errorResponse(404, "Not Found", e.getMessage()));
        } catch (ConflictException e) {
            return Optional.of(errorResponse(409, "Conflict", e.getMessage()));
        } catch (BadRequestException e) {
            return Optional.of(errorResponse(400, "Bad Request", e.getMessage()));
        } catch (Exception e) {
            // Gestionar qualsevol altre error no previst com un 500
            e.printStackTrace();
            return Optional.of(errorResponse(500, "Internal Server Error", "An unexpected error occurred."));
        }
    }

    /** Mètode auxiliar per generar les respostes d'error amb format JSON. */
    private RawHttpResponse<?> errorResponse(int status, String reason, String message) {
        // Estructura JSON d'error
        String jsonBody = String.format("{\"status\": %d, \"message\": \"%s\"}", status, message.replace("\"", "\\\""));

        // Creem la resposta utilitzant el patró correcte per a la v2.6.0
        return http.parseResponse("HTTP/1.1 " + status + " " + reason)
                .withBody(new StringBody(jsonBody, "application/json; charset=utf-8"));
    }
}