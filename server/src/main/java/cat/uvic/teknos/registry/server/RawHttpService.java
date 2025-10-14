package cat.uvic.teknos.registry.server;

import rawhttp.core.server.Router;

/**
 * Defineix el contracte per a qualsevol servei que pugui gestionar peticions HTTP.
 * Aquesta interfície estén rawhttp.core.server.Router per ser compatible
 * directament amb TcpRawHttpServer.
 */
public interface RawHttpService extends Router {
    // Aquesta interfície hereta automàticament el mètode que necessitem:
    // Optional<RawHttpResponse<?>> route(RawHttpRequest request);
    // Per tant, no cal afegir res més aquí dins.
}
