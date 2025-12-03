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
import rawhttp.core.body.StringBody;

import java.util.Optional;

//P4
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import cat.uvic.teknos.registry.security.CryptoUtils;

/**
 * Routes incoming requests to the appropriate controller.
 */

public class RequestRouter implements RawHttpService {

    private final EmployeeController employeeController;
    private final RawHttp http;
    private final CryptoUtils cryptoUtils;

    public RequestRouter(DIManager diManager) throws DIException {
        this.employeeController = new EmployeeController(diManager);
        this.http = new RawHttp();
        this.cryptoUtils = new CryptoUtils();
    }

    @Override // <-- CANVI: Anotació afegida
    public Optional<RawHttpResponse<?>> route(RawHttpRequest request) { // <-- CANVI: Nom del mètode canviat a "route"
        String path = request.getUri().getPath();

        // 1. HANDSHAKE (Instrucció 8)
        if (path.startsWith("/keys/")) {
            return handleKeyExchange(path);
        }

        // 2. VALIDACIÓ DE HASH (Instrucció 6) - Per a tota la resta de rutes
        if (request.getBody().isPresent()) {
        }

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
            e.printStackTrace(); // <--- AFEGEIX AIXÒ
            return Optional.of(errorResponse(404, "Not Found", e.getMessage()));
        } catch (ConflictException e) {
            e.printStackTrace(); // <--- AFEGEIX AIXÒ
            return Optional.of(errorResponse(409, "Conflict", e.getMessage()));
        } catch (BadRequestException e) {
            e.printStackTrace(); // <--- AFEGEIX AIXÒ
            return Optional.of(errorResponse(400, "Bad Request", e.getMessage()));
        } catch (Exception e) {
            // Aquest ja ho tenia, perfecte
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


    private Optional<RawHttpResponse<?>> handleKeyExchange(String path) {
        try {
            // Extreure l'alias del client de la URL (/keys/client1 -> client1)
            String clientAlias = path.substring("/keys/".length());

            // 1. Generar Clau Simètrica Nova
            SecretKey sessionKey = cryptoUtils.generateSessionKey();

            // Convertir la clau a String per poder enviar-la
            String sessionKeyString = Base64.getEncoder().encodeToString(sessionKey.getEncoded());

            // 2. Xifrar la clau amb la PÚBLICA del client (que està al server.jks)
            // NOTA: 'server.jks' és on hem importat 'client1.cer'
            String encryptedSessionKey = cryptoUtils.asymmetricEncrypt(
                    sessionKeyString,
                    "/server.jks",
                    clientAlias
            );

            // 3. Guardar la clau de sessió al servidor per a futures comunicacions
            cryptoUtils.setSessionKey(sessionKey);
            // ALERTA: En un servidor real multithread, això NO es pot guardar al singleton CryptoUtils
            // perquè es barrejarien els clients. Per aquesta pràctica (si és un sol client alhora),
            // pot passar. Si no, necessitaries un Map<IP, SecretKey>.

            // 4. Enviar la clau xifrada
            return Optional.of(http.parseResponse("HTTP/1.1 200 OK")
                    .withBody(new StringBody(encryptedSessionKey)));

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.of(http.parseResponse("HTTP/1.1 500 Internal Server Error"));
        }
    }
}