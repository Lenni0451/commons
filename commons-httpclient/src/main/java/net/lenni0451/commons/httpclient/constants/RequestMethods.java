package net.lenni0451.commons.httpclient.constants;

public class RequestMethods {

    /**
     * The {@code GET} method requests a representation of the specified resource. Requests using {@code GET} should only retrieve data.
     */
    public static final String GET = "GET";
    /**
     * The {@code HEAD} method asks for a response identical to a {@link #GET} request, but without the response body.
     */
    public static final String HEAD = "HEAD";
    /**
     * The {@code POST} method submits an entity to the specified resource, often causing a change in state or side effects on the server.
     */
    public static final String POST = "POST";
    /**
     * The {@code PUT} method replaces all current representations of the target resource with the request payload.
     */
    public static final String PUT = "PUT";
    /**
     * The {@code DELETE} method deletes the specified resource.
     */
    public static final String DELETE = "DELETE";
    /**
     * The {@code CONNECT} method establishes a tunnel to the server identified by the target resource.
     */
    public static final String CONNECT = "CONNECT";
    /**
     * The {@code OPTIONS} method describes the communication options for the target resource.
     */
    public static final String OPTIONS = "OPTIONS";
    /**
     * The {@code TRACE} method performs a message loop-back test along the path to the target resource.
     */
    public static final String TRACE = "TRACE";
    /**
     * The {@code PATCH} method applies partial modifications to a resource.
     */
    public static final String PATCH = "PATCH";

}
