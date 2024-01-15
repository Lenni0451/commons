package net.lenni0451.commons.httpclient.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StatusCodes {

    /**
     * This interim response indicates that the client should continue the request or ignore the response if the request is already finished.
     */
    public static final int CONTINUE = 100;
    /**
     * This code is sent in response to an {@code Upgrade} request header from the client and indicates the protocol the server is switching to.
     */
    public static final int SWITCHING_PROTOCOLS = 101;
    /**
     * This code indicates that the server has received and is processing the request, but no response is available yet.
     */
    public static final int PROCESSING = 102;
    /**
     * This status code is primarily intended to be used with the {@code Link} header, letting the user agent start <a href="https://developer.mozilla.org/en-US/docs/Web/HTML/Attributes/rel/preload">preloading</a>
     * resources while the server prepares a response or <a href="https:/developer.mozilla.org//en-US/docs/Web/HTML/Attributes/rel/preconnect">preconnect</a> to an origin from which the page will need resources.
     */
    public static final int EARLY_HINTS = 103;
    /**
     * The request succeeded. The result meaning of "success" depends on the HTTP method:
     * <table>
     *     <tr>
     *         <th>Method</th>
     *         <th>Result</th>
     *     </tr>
     *     <tr>
     *         <td>GET</td>
     *         <td>The resource has been fetched and transmitted in the message body.</td>
     *     </tr>
     *     <tr>
     *         <td>HEAD</td>
     *         <td>The representation headers are included in the response without any message body.</td>
     *     </tr>
     *     <tr>
     *         <td>PUT/POST</td>
     *         <td>The resource describing the result of the action is transmitted in the message body.</td>
     *     </tr>
     *     <tr>
     *         <td>TRACE</td>
     *         <td>The message body contains the request message as received by the server.</td>
     *     </tr>
     * </table>
     */
    public static final int OK = 200;
    /**
     * The request succeeded, and a new resource was created as a result.
     * This is typically the response sent after {@code POST} requests, or some {@code PUT} requests.
     */
    public static final int CREATED = 201;
    /**
     * The request has been received but not yet acted upon.<br>
     * It is noncommittal, since there is no way in HTTP to later send an asynchronous response indicating the outcome of the request.<br>
     * It is intended for cases where another process or server handles the request, or for batch processing.
     */
    public static final int ACCEPTED = 202;
    /**
     * This response code means the returned metadata is not exactly the same as is available from the origin server, but is collected from a local or a third-party copy.<br>
     * This is mostly used for mirrors or backups of another resource.<br>
     * Except for that specific case, the {@code 200 OK} response is preferred to this status.
     */
    public static final int NON_AUTHORITATIVE_INFORMATION = 203;
    /**
     * There is no content to send for this request, but the headers may be useful.<br>
     * The user agent may update its cached headers for this resource with the new ones.
     */
    public static final int NO_CONTENT = 204;
    /**
     * Tells the user agent to reset the document which sent this request.
     */
    public static final int RESET_CONTENT = 205;
    /**
     * This response code is used when the {@code Range} header is sent from the client to request only part of a resource.
     */
    public static final int PARTIAL_CONTENT = 206;
    /**
     * Conveys information about multiple resources, for situations where multiple status codes might be appropriate.
     */
    public static final int MULTI_STATUS = 207;
    /**
     * Used inside a {@code WebDAV} response element to avoid repeatedly enumerating the internal members of multiple bindings to the same collection.
     */
    public static final int ALREADY_REPORTED = 208;
    /**
     * The server has fulfilled a {@code GET} request for the resource, and the response is a representation of the result of one or more instance-manipulations applied to the current instance.
     */
    public static final int IM_USED = 226;
    /**
     * The request has more than one possible response. The user agent or user should choose one of them. (There is no standardized way of choosing one of the responses, but HTML links to the possibilities are recommended so the user can pick.)
     */
    public static final int MULTIPLE_CHOICES = 300;
    /**
     * The URL of the requested resource has been changed permanently. The new URL is given in the response.
     */
    public static final int MOVED_PERMANENTLY = 301;
    /**
     * This response code means that the URI of requested resource has been changed <i>temporarily</i>.<br>
     * Further changes in the URI might be made in the future. Therefore, this same URI should be used by the client in future requests.
     */
    public static final int FOUND = 302;
    /**
     * The server sent this response to direct the client to get the requested resource at another URI with a GET request.
     */
    public static final int SEE_OTHER = 303;
    /**
     * This is used for caching purposes.<br>
     * It tells the client that the response has not been modified, so the client can continue to use the same cached version of the response.
     */
    public static final int NOT_MODIFIED = 304;
    /**
     * Defined in a previous version of the HTTP specification to indicate that a requested response must be accessed by a proxy.<br>
     * It has been deprecated due to security concerns regarding in-band configuration of a proxy.
     */
    public static final int USE_PROXY = 305;
    /**
     * This response code is no longer used; it is just reserved. It was used in a previous version of the HTTP/1.1 specification.
     */
    public static final int UNUSED = 306;
    /**
     * The server sends this response to direct the client to get the requested resource at another URI with the same method that was used in the prior request.<br>
     * This has the same semantics as the {@code 302 Found} HTTP response code, with the exception that the user agent <i>must not</i> change the HTTP method used:
     * if a {@code POST} was used in the first request, a {@code POST} must be used in the second request.
     */
    public static final int TEMPORARY_REDIRECT = 307;
    /**
     * This means that the resource is now permanently located at another URI, specified by the {@code Location:} HTTP Response header.<br>
     * This has the same semantics as the {@code 301 Moved Permanently} HTTP response code, with the exception that the user agent <i>must not</i> change the HTTP method used:
     * if a {@code POST} was used in the first request, a {@code POST} must be used in the second request.
     */
    public static final int PERMANENT_REDIRECT = 308;
    /**
     * The server cannot or will not process the request due to something that is perceived to be a client error (e.g., malformed request syntax, invalid request message framing, or deceptive request routing).
     */
    public static final int BAD_REQUEST = 400;
    /**
     * Although the HTTP standard specifies "unauthorized", semantically this response means "unauthenticated".<br>
     * That is, the client must authenticate itself to get the requested response.
     */
    public static final int UNAUTHORIZED = 401;
    /**
     * This response code is reserved for future use.<br>
     * The initial aim for creating this code was using it for digital payment systems, however this status code is used very rarely and no standard convention exists.
     */
    public static final int PAYMENT_REQUIRED = 402;
    /**
     * The client does not have access rights to the content; that is, it is unauthorized, so the server is refusing to give the requested resource.<br>
     * Unlike {@code 401 Unauthorized}, the client's identity is known to the server.
     */
    public static final int FORBIDDEN = 403;
    /**
     * The server cannot find the requested resource.<br>
     * In the browser, this means the URL is not recognized.<br>
     * In an API, this can also mean that the endpoint is valid but the resource itself does not exist.<br>
     * Servers may also send this response instead of {@code 403 Forbidden} to hide the existence of a resource from an unauthorized client.<br>
     * This response code is probably the most well known due to its frequent occurrence on the web.
     */
    public static final int NOT_FOUND = 404;
    /**
     * The request method is known by the server but is not supported by the target resource.<br>
     * For example, an API may not allow calling {@code DELETE} to remove a resource.
     */
    public static final int METHOD_NOT_ALLOWED = 405;
    /**
     * This response is sent when the web server, after performing <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Content_negotiation#server-driven_negotiation">server-driven content negotiation</a>,
     * doesn't find any content that conforms to the criteria given by the user agent.
     */
    public static final int NOT_ACCEPTABLE = 406;
    /**
     * This is similar to {@code 401 Unauthorized} but authentication is needed to be done by a proxy.
     */
    public static final int PROXY_AUTHENTICATION_REQUIRED = 407;
    /**
     * This response is sent on an idle connection by some servers, even without any previous request by the client.<br>
     * It means that the server would like to shut down this unused connection.<br>
     * This response is used much more since some browsers, like Chrome, Firefox 27+, or IE9, use HTTP pre-connection mechanisms to speed up surfing.<br>
     * Also note that some servers merely shut down the connection without sending this message.
     */
    public static final int REQUEST_TIMEOUT = 408;
    /**
     * This response is sent when a request conflicts with the current state of the server.
     */
    public static final int CONFLICT = 409;
    /**
     * This response is sent when the requested content has been permanently deleted from server, with no forwarding address.<br>
     * Clients are expected to remove their caches and links to the resource.<br>
     * The HTTP specification intends this status code to be used for "limited-time, promotional services".<br>
     * APIs should not feel compelled to indicate resources that have been deleted with this status code.
     */
    public static final int GONE = 410;
    /**
     * Server rejected the request because the {@code Content-Length} header field is not defined and the server requires it.
     */
    public static final int LENGTH_REQUIRED = 411;
    /**
     * The client has indicated preconditions in its headers which the server does not meet.
     */
    public static final int PRECONDITION_FAILED = 412;
    /**
     * Request entity is larger than limits defined by server.<br>
     * The server might close the connection or return an {@code Retry-After} header field.
     */
    public static final int PAYLOAD_TOO_LARGE = 413;
    /**
     * The URI requested by the client is longer than the server is willing to interpret.
     */
    public static final int URI_TOO_LONG = 414;
    /**
     * The media format of the requested data is not supported by the server, so the server is rejecting the request.
     */
    public static final int UNSUPPORTED_MEDIA_TYPE = 415;
    /**
     * The range specified by the {@code Range} header field in the request cannot be fulfilled.<br>
     * It's possible that the range is outside the size of the target URI's data.
     */
    public static final int RANGE_NOT_SATISFIABLE = 416;
    /**
     * This response code means the expectation indicated by the {@code Expect} request header field cannot be met by the server.
     */
    public static final int EXPECTATION_FAILED = 417;
    /**
     * The server refuses the attempt to brew coffee with a teapot.
     */
    public static final int IM_A_TEAPOT = 418;
    /**
     * The request was directed at a server that is not able to produce a response.<br>
     * This can be sent by a server that is not configured to produce responses for the combination of scheme and authority that are included in the request URI.
     */
    public static final int MISDIRECTED_REQUEST = 421;
    /**
     * The request was well-formed but was unable to be followed due to semantic errors.
     */
    public static final int UNPROCESSABLE_CONTENT = 422;
    /**
     * The resource that is being accessed is locked.
     */
    public static final int LOCKED = 423;
    /**
     * The request failed due to failure of a previous request.
     */
    public static final int FAILED_DEPENDENCY = 424;
    /**
     * Indicates that the server is unwilling to risk processing a request that might be replayed.
     */
    public static final int TOO_EARLY = 425;
    /**
     * The server refuses to perform the request using the current protocol but might be willing to do so after the client upgrades to a different protocol.<br>
     * The server sends an {@code Upgrade} header in a 426 response to indicate the required protocol(s).
     */
    public static final int UPGRADE_REQUIRED = 426;
    /**
     * The origin server requires the request to be conditional.<br>
     * This response is intended to prevent the {@code lost update} problem, where a client {@code GET}s a resource's state, modifies it and {@code PUT}s it back to the server, when meanwhile a third party has modified the state on the server, leading to a conflict.
     */
    public static final int PRECONDITION_REQUIRED = 428;
    /**
     * The user has sent too many requests in a given amount of time ("rate limiting").
     */
    public static final int TOO_MANY_REQUESTS = 429;
    /**
     * The server is unwilling to process the request because its header fields are too large.<br>
     * The request may be resubmitted after reducing the size of the request header fields.
     */
    public static final int REQUEST_HEADER_FIELDS_TOO_LARGE = 431;
    /**
     * The user agent requested a resource that cannot legally be provided, such as a web page censored by a government.
     */
    public static final int UNAVAILABLE_FOR_LEGAL_REASONS = 451;
    /**
     * The server has encountered a situation it does not know how to handle.
     */
    public static final int INTERNAL_SERVER_ERROR = 500;
    /**
     * The request method is not supported by the server and cannot be handled. The only methods that servers are required to support (and therefore that must not return this code) are {@code GET} and {@code HEAD}.
     */
    public static final int NOT_IMPLEMENTED = 501;
    /**
     * This error response means that the server, while working as a gateway to get a response needed to handle the request, got an invalid response.
     */
    public static final int BAD_GATEWAY = 502;
    /**
     * The server is not ready to handle the request.<br>
     * Common causes are a server that is down for maintenance or that is overloaded.<br>
     * Note that together with this response, a user-friendly page explaining the problem should be sent.<br>
     * This response should be used for temporary conditions and the {@code Retry-After} HTTP header should, if possible, contain the estimated time before the recovery of the service.<br>
     * The webmaster must also take care about the caching-related headers that are sent along with this response, as these temporary condition responses should usually not be cached.
     */
    public static final int SERVICE_UNAVAILABLE = 503;
    /**
     * This error response is given when the server is acting as a gateway and cannot get a response in time.
     */
    public static final int GATEWAY_TIMEOUT = 504;
    /**
     * The HTTP version used in the request is not supported by the server.
     */
    public static final int HTTP_VERSION_NOT_SUPPORTED = 505;
    /**
     * The server has an internal configuration error: the chosen variant resource is configured to engage in transparent content negotiation itself, and is therefore not a proper end point in the negotiation process.
     */
    public static final int VARIANT_ALSO_NEGOTIATES = 506;
    /**
     * The method could not be performed on the resource because the server is unable to store the representation needed to successfully complete the request.
     */
    public static final int INSUFFICIENT_STORAGE = 507;
    /**
     * The server detected an infinite loop while processing the request.
     */
    public static final int LOOP_DETECTED = 508;
    /**
     * Further extensions to the request are required for the server to fulfill it.
     */
    public static final int NOT_EXTENDED = 510;
    /**
     * Indicates that the client needs to authenticate to gain network access.
     */
    public static final int NETWORK_AUTHENTICATION_REQUIRED = 511;

    /**
     * A map of all status codes and their messages.
     */
    public static final Map<Integer, String> STATUS_CODES;

    static {
        Map<Integer, String> statusCodes = new HashMap<>();
        statusCodes.put(CONTINUE, "Continue");
        statusCodes.put(SWITCHING_PROTOCOLS, "Switching Protocols");
        statusCodes.put(PROCESSING, "Processing");
        statusCodes.put(EARLY_HINTS, "Early Hints");
        statusCodes.put(OK, "OK");
        statusCodes.put(CREATED, "Created");
        statusCodes.put(ACCEPTED, "Accepted");
        statusCodes.put(NON_AUTHORITATIVE_INFORMATION, "Non-Authoritative Information");
        statusCodes.put(NO_CONTENT, "No Content");
        statusCodes.put(RESET_CONTENT, "Reset Content");
        statusCodes.put(PARTIAL_CONTENT, "Partial Content");
        statusCodes.put(MULTI_STATUS, "Multi-Status");
        statusCodes.put(ALREADY_REPORTED, "Already Reported");
        statusCodes.put(IM_USED, "IM Used");
        statusCodes.put(MULTIPLE_CHOICES, "Multiple Choices");
        statusCodes.put(MOVED_PERMANENTLY, "Moved Permanently");
        statusCodes.put(FOUND, "Found");
        statusCodes.put(SEE_OTHER, "See Other");
        statusCodes.put(NOT_MODIFIED, "Not Modified");
        statusCodes.put(USE_PROXY, "Use Proxy");
        statusCodes.put(UNUSED, "Unused");
        statusCodes.put(TEMPORARY_REDIRECT, "Temporary Redirect");
        statusCodes.put(PERMANENT_REDIRECT, "Permanent Redirect");
        statusCodes.put(BAD_REQUEST, "Bad Request");
        statusCodes.put(UNAUTHORIZED, "Unauthorized");
        statusCodes.put(PAYMENT_REQUIRED, "Payment Required");
        statusCodes.put(FORBIDDEN, "Forbidden");
        statusCodes.put(NOT_FOUND, "Not Found");
        statusCodes.put(METHOD_NOT_ALLOWED, "Method Not Allowed");
        statusCodes.put(NOT_ACCEPTABLE, "Not Acceptable");
        statusCodes.put(PROXY_AUTHENTICATION_REQUIRED, "Proxy Authentication Required");
        statusCodes.put(REQUEST_TIMEOUT, "Request Timeout");
        statusCodes.put(CONFLICT, "Conflict");
        statusCodes.put(GONE, "Gone");
        statusCodes.put(LENGTH_REQUIRED, "Length Required");
        statusCodes.put(PRECONDITION_FAILED, "Precondition Failed");
        statusCodes.put(PAYLOAD_TOO_LARGE, "Payload Too Large");
        statusCodes.put(URI_TOO_LONG, "URI Too Long");
        statusCodes.put(UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type");
        statusCodes.put(RANGE_NOT_SATISFIABLE, "Range Not Satisfiable");
        statusCodes.put(EXPECTATION_FAILED, "Expectation Failed");
        statusCodes.put(IM_A_TEAPOT, "I'm a teapot");
        statusCodes.put(MISDIRECTED_REQUEST, "Misdirected Request");
        statusCodes.put(UNPROCESSABLE_CONTENT, "Unprocessable Content");
        statusCodes.put(LOCKED, "Locked");
        statusCodes.put(FAILED_DEPENDENCY, "Failed Dependency");
        statusCodes.put(TOO_EARLY, "Too Early");
        statusCodes.put(UPGRADE_REQUIRED, "Upgrade Required");
        statusCodes.put(PRECONDITION_REQUIRED, "Precondition Required");
        statusCodes.put(TOO_MANY_REQUESTS, "Too Many Requests");
        statusCodes.put(REQUEST_HEADER_FIELDS_TOO_LARGE, "Request Header Fields Too Large");
        statusCodes.put(UNAVAILABLE_FOR_LEGAL_REASONS, "Unavailable For Legal Reasons");
        statusCodes.put(INTERNAL_SERVER_ERROR, "Internal Server Error");
        statusCodes.put(NOT_IMPLEMENTED, "Not Implemented");
        statusCodes.put(BAD_GATEWAY, "Bad Gateway");
        statusCodes.put(SERVICE_UNAVAILABLE, "Service Unavailable");
        statusCodes.put(GATEWAY_TIMEOUT, "Gateway Timeout");
        statusCodes.put(HTTP_VERSION_NOT_SUPPORTED, "HTTP Version Not Supported");
        statusCodes.put(VARIANT_ALSO_NEGOTIATES, "Variant Also Negotiates");
        statusCodes.put(INSUFFICIENT_STORAGE, "Insufficient Storage");
        statusCodes.put(LOOP_DETECTED, "Loop Detected");
        statusCodes.put(NOT_EXTENDED, "Not Extended");
        statusCodes.put(NETWORK_AUTHENTICATION_REQUIRED, "Network Authentication Required");
        STATUS_CODES = Collections.unmodifiableMap(statusCodes);
    }

}
