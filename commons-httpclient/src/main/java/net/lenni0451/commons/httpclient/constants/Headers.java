package net.lenni0451.commons.httpclient.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Headers {

    /**
     * Defines the authentication method that should be used to access a resource.
     */
    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
    /**
     * Contains the credentials to authenticate a user-agent with a server.
     */
    public static final String AUTHORIZATION = "Authorization";
    /**
     * Defines the authentication method that should be used to access a resource behind a proxy server.
     */
    public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
    /**
     * Contains the credentials to authenticate a user agent with a proxy server.
     */
    public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
    /**
     * The time, in seconds, that the object has been in a proxy cache.
     */
    public static final String AGE = "Age";
    /**
     * Directives for caching mechanisms in both requests and responses.
     */
    public static final String CACHE_CONTROL = "Cache-Control";
    /**
     * Clears browsing data (e.g. cookies, storage, cache) associated with the requesting website.
     */
    public static final String CLEAR_SITE_DATA = "Clear-Site-Data";
    /**
     * The date/time after which the response is considered stale.
     */
    public static final String EXPIRES = "Expires";
    /**
     * The last modification date of the resource, used to compare several versions of the same resource. It is less accurate than {@link #ETAG}, but easier to calculate in some environments.<br>
     * Conditional requests using {@link #IF_MODIFIED_SINCE} and {@link #IF_UNMODIFIED_SINCE} use this value to change the behavior of the request.
     */
    public static final String LAST_MODIFIED = "Last-Modified";
    /**
     * A unique string identifying the version of the resource. Conditional requests using {@link #IF_MATCH} and {@link #IF_NONE_MATCH} use this value to change the behavior of the request.
     */
    public static final String ETAG = "ETag";
    /**
     * Makes the request conditional, and applies the method only if the stored resource matches one of the given ETags.
     */
    public static final String IF_MATCH = "If-Match";
    /**
     * Makes the request conditional, and applies the method only if the stored resource <i>doesn't</i> match any of the given ETags.<br>
     * This is used to update caches (for safe requests), or to prevent uploading a new resource when one already exists.
     */
    public static final String IF_NONE_MATCH = "If-None-Match";
    /**
     * Makes the request conditional, and expects the resource to be transmitted only if it has been modified after the given date.<br>
     * This is used to transmit data only when the cache is out of date.
     */
    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    /**
     * Makes the request conditional, and expects the resource to be transmitted only if it has not been modified after the given date.<br>
     * This ensures the coherence of a new fragment of a specific range with previous ones, or to implement an optimistic concurrency control system when modifying existing documents.
     */
    public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    /**
     * Determines how to match request headers to decide whether a cached response can be used rather than requesting a fresh one from the origin server.
     */
    public static final String VARY = "Vary";
    /**
     * Controls whether the network connection stays open after the current transaction finishes.
     */
    public static final String CONNECTION = "Connection";
    /**
     * Controls how long a persistent connection should stay open.
     */
    public static final String KEEP_ALIVE = "Keep-Alive";
    /**
     * Informs the server about the mime types of data that can be sent back.
     */
    public static final String ACCEPT = "Accept";
    /**
     * The encoding algorithm, usually a <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Compression">compression algorithm</a>, that can be used on the resource sent back.
     */
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    /**
     * Informs the server about the human language the server is expected to send back. This is a hint and is not necessarily under the full control of the user: the server should always pay attention not to override an explicit user choice (like selecting a language from a dropdown).
     */
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    /**
     * Indicates expectations that need to be fulfilled by the server to properly handle the request.
     */
    public static final String EXPECT = "Expect";
    /**
     * When using <a href="TRACE">https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/TRACE</a>, indicates the maximum number of hops the request can do before being reflected to the sender.
     */
    public static final String MAX_FORWARDS = "Max-Forwards";
    /**
     * Contains stored <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Cookies">HTTP cookies</a> previously sent by the server with the {@link #SET_COOKIE} header.
     */
    public static final String COOKIE = "Cookie";
    /**
     * Send cookies from the server to the user-agent.
     */
    public static final String SET_COOKIE = "Set-Cookie";
    /**
     * Indicates whether the response to the request can be exposed when the credentials flag is true.
     */
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    /**
     * Used in response to a <a href="https://developer.mozilla.org/en-US/docs/Glossary/Preflight_request">preflight request</a> to indicate which HTTP headers can be used when making the actual request.
     */
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    /**
     * Specifies the methods allowed when accessing the resource in response to a preflight request.
     */
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    /**
     * Indicates whether the response can be shared.
     */
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    /**
     * Indicates which headers can be exposed as part of the response by listing their names.
     */
    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    /**
     * Indicates how long the results of a preflight request can be cached.
     */
    public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
    /**
     * Used when issuing a preflight request to let the server know which HTTP headers will be used when the actual request is made.
     */
    public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
    /**
     * Used when issuing a preflight request to let the server know which <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods">HTTP method</a> will be used when the actual request is made.
     */
    public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
    /**
     * Indicates where a fetch originates from.
     */
    public static final String ORIGIN = "Origin";
    /**
     * Specifies origins that are allowed to see values of attributes retrieved via features of the <a href="https://developer.mozilla.org/en-US/docs/Web/API/Performance_API/Resource_timing">Resource Timing API</a>, which would otherwise be reported as zero due to cross-origin restrictions.
     */
    public static final String TIMING_ALLOW_ORIGIN = "Timing-Allow-Origin";
    /**
     * Indicates if the resource transmitted should be displayed inline (default behavior without the header), or if it should be handled like a download and the browser should present a "Save As" dialog.
     */
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    /**
     * The size of the resource, in decimal number of bytes.
     */
    public static final String CONTENT_LENGTH = "Content-Length";
    /**
     * Indicates the media type of the resource.
     */
    public static final String CONTENT_TYPE = "Content-Type";
    /**
     * Used to specify the compression algorithm.
     */
    public static final String CONTENT_ENCODING = "Content-Encoding";
    /**
     * Describes the human language(s) intended for the audience, so that it allows a user to differentiate according to the users' own preferred language.
     */
    public static final String CONTENT_LANGUAGE = "Content-Language";
    /**
     * Indicates an alternate location for the returned data.
     */
    public static final String CONTENT_LOCATION = "Content-Location";
    /**
     * Contains information from the client-facing side of proxy servers that is altered or lost when a proxy is involved in the path of the request.
     */
    public static final String FORWARDED = "Forwarded";
    /**
     * Added by proxies, both forward and reverse proxies, and can appear in the request headers and the response headers.
     */
    public static final String VIA = "Via";
    /**
     * Indicates the URL to redirect a page to.
     */
    public static final String LOCATION = "Location";
    /**
     * Directs the browser to reload the page or redirect to another. Takes the same value as the `meta` element with <a href="https://developer.mozilla.org/en-US/docs/Web/HTML/Element/meta#http-equiv">refresh</a>.
     */
    public static final String REFRESH = "Refresh";
    /**
     * Contains an Internet email address for a human user who controls the requesting user agent.
     */
    public static final String FROM = "From";
    /**
     * Specifies the domain name of the server (for virtual hosting), and (optionally) the TCP port number on which the server is listening.
     */
    public static final String HOST = "Host";
    /**
     * The address of the previous web page from which a link to the currently requested page was followed.
     */
    public static final String REFERER = "Referer";
    /**
     * Governs which referrer information sent in the {@link #REFERER} header should be included with requests made.
     */
    public static final String REFERRER_POLICY = "Referrer-Policy";
    /**
     * Contains a characteristic string that allows the network protocol peers to identify the application type, operating system, software vendor or software version of the requesting software user agent.
     */
    public static final String USER_AGENT = "User-Agent";
    /**
     * Lists the set of HTTP request methods supported by a resource.
     */
    public static final String ALLOW = "Allow";
    /**
     * Contains information about the software used by the origin server to handle the request.
     */
    public static final String SERVER = "Server";
    /**
     * Indicates if the server supports range requests, and if so in which unit the range can be expressed.
     */
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    /**
     * Indicates the part of a document that the server should return.
     */
    public static final String RANGE = "Range";
    /**
     * Creates a conditional range request that is only fulfilled if the given etag or date matches the remote resource. Used to prevent downloading two ranges from incompatible version of the resource.
     */
    public static final String IF_RANGE = "If-Range";
    /**
     * Indicates where in a full body message a partial message belongs.
     */
    public static final String CONTENT_RANGE = "Content-Range";
    /**
     * Allows a server to declare an embedder policy for a given document.
     */
    public static final String CROSS_ORIGIN_EMBEDDER_POLICY = "Cross-Origin-Embedder-Policy";
    /**
     * Prevents other domains from opening/controlling a window.
     */
    public static final String CROSS_ORIGIN_OPENER_POLICY = "Cross-Origin-Opener-Policy";
    /**
     * Prevents other domains from reading the response of the resources to which this header is applied.
     *
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Cross-Origin_Resource_Policy">CORP explainer article</a>
     */
    public static final String CROSS_ORIGIN_RESOURCE_POLICY = "Cross-Origin-Resource-Policy";
    /**
     * Controls resources the user agent is allowed to load for a given page.
     */
    public static final String CONTENT_SECURITY_POLICY = "Content-Security-Policy";
    /**
     * Allows web developers to experiment with policies by monitoring, but not enforcing, their effects. These violation reports consist of JSON documents sent via an HTTP {@code POST} request to the specified URI.
     */
    public static final String CONTENT_SECURITY_POLICY_REPORT_ONLY = "Content-Security-Policy-Report-Only";
    /**
     * Provides a mechanism to allow and deny the use of browser features in a website's own frame, and in {@code iframe}s that it embeds.
     */
    public static final String PERMISSIONS_POLICY = "Permissions-Policy";
    /**
     * Force communication using HTTPS instead of HTTP.
     */
    public static final String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";
    /**
     * Sends a signal to the server expressing the client's preference for an encrypted and authenticated response, and that it can successfully handle the {@code upgrade-insecure-requests} directive.
     */
    public static final String UPGRADE_INSECURE_REQUESTS = "Upgrade-Insecure-Requests";
    /**
     * Disables MIME sniffing and forces browser to use the type given in {@link #CONTENT_TYPE}.
     */
    public static final String X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";
    /**
     * Indicates whether a browser should be allowed to render a page in a {@code frame}, {@code iframe}, {@code embed} or {@code object}.
     */
    public static final String X_FRAME_OPTIONS = "X-Frame-Options";
    /**
     * Specifies if a cross-domain policy file ({@code crossdomain.xml}) is allowed.<br>
     * The file may define a policy to grant clients, such as Adobe's Flash Player (now obsolete), Adobe Acrobat, Microsoft Silverlight (now obsolete), or Apache Flex,
     * permission to handle data across domains that would otherwise be restricted due to the <a href="https://developer.mozilla.org/en-US/docs/Web/Security/Same-origin_policy">Same-Origin Policy</a>.
     *
     * @see <a href="https://www.adobe.com/devnet-docs/acrobatetk/tools/AppSec/CrossDomain_PolicyFile_Specification.pdf>Cross-domain Policy File Specification</a>
     */
    public static final String X_PERMITTED_CROSS_DOMAIN_POLICIES = "X-Permitted-Cross-Domain-Policies";
    /**
     * May be set by hosting environments or other frameworks and contains information about them while not providing any usefulness to the application or its visitors.<br>
     * Unset this header to avoid exposing potential vulnerabilities.
     */
    public static final String X_POWERED_BY = "X-Powered-By";
    /**
     * Enables cross-site scripting filtering.
     */
    public static final String X_XSS_PROTECTION = "X-XSS-Protection";
    /**
     * Indicates the relationship between a request initiator's origin and its target's origin. It is a Structured Header whose value is a token with possible values {@code cross-site}, {@code same-origin}, {@code same-site}, and {@code none}.
     */
    public static final String SEC_FETCH_SITE = "Sec-Fetch-Site";
    /**
     * Indicates the request's mode to a server. It is a Structured Header whose value is a token with possible values {@code cors}, {@code navigate}, {@code no-cors}, {@code same-origin}, and {@code websocket}.
     */
    public static final String SEC_FETCH_MODE = "Sec-Fetch-Mode";
    /**
     * Indicates whether or not a navigation request was triggered by user activation. It is a Structured Header whose value is a boolean so possible values are {@code ?0} for false and {@code ?1} for true.
     */
    public static final String SEC_FETCH_USER = "Sec-Fetch-User";
    /**
     * Indicates the request's destination. It is a Structured Header whose value is a token with possible values {@code audio}, {@code audioworklet}, {@code document}, {@code embed}, {@code empty}, {@code font}, {@code image}, {@code manifest}, {@code object}, {@code paintworklet}, {@code report}, {@code script}, {@code serviceworker}, {@code sharedworker}, {@code style}, {@code track}, {@code video}, {@code worker}, and {@code xslt}.
     */
    public static final String SEC_FETCH_DEST = "Sec-Fetch-Dest";
    /**
     * Indicates the purpose of the request, when the purpose is something other than immediate use by the user-agent.<br>
     * The header currently has one possible value, {@code prefetch}, which indicates that the resource is being fetched preemptively for a possible future navigation.
     */
    public static final String SEC_PURPOSE = "Sec-Purpose";
    /**
     * A request header sent in preemptive request to {@code fetch()} a resource during service worker boot.<br>
     * The value, which is set with {@code NavigationPreloadManager.setHeaderValue()"}, can be used to inform a server that a different resource should be returned than in a normal {@code fetch()} operation.
     */
    public static final String SERVICE_WORKER_NAVIGATION_PRELOAD = "Service-Worker-Navigation-Preload";
    /**
     * Used to specify a server endpoint for the browser to send warning and error reports to.
     */
    public static final String REPORT_TO = "Report-To";
    /**
     * Specifies the form of encoding used to safely transfer the resource to the user.
     */
    public static final String TRANSFER_ENCODING = "Transfer-Encoding";
    /**
     * Specifies the transfer encodings the user agent is willing to accept.
     */
    public static final String TE = "TE";
    /**
     * Allows the sender to include additional fields at the end of chunked message.
     */
    public static final String TRAILER = "Trailer";
    /**
     * Used to list alternate ways to reach this service.
     */
    public static final String ALT_SVC = "Alt-Svc";
    /**
     * Used to identify the alternative service in use.
     */
    public static final String ALT_USED = "Alt-Used";
    /**
     * Contains the date and time at which the message was originated.
     */
    public static final String DATE = "Date";
    /**
     * This entity-header field provides a means for serializing one or more links in HTTP headers. It is semantically equivalent to the HTML {@code link} element.
     */
    public static final String LINK = "Link";
    /**
     * Indicates how long the user agent should wait before making a follow-up request.
     */
    public static final String RETRY_AFTER = "Retry-After";
    /**
     * Communicates one or more metrics and descriptions for the given request-response cycle.
     */
    public static final String SERVER_TIMING = "Server-Timing";
    /**
     * Used to remove the <a href="https://developer.mozilla.org/en-US/docs/Web/API/Service_Worker_API/Using_Service_Workers#why_is_my_service_worker_failing_to_register">path restriction</a> by including this header <a href="https://w3c.github.io/ServiceWorker/#service-worker-script-response">in the response of the Service Worker script</a>.
     */
    public static final String SERVICE_WORKER_ALLOWED = "Service-Worker-Allowed";
    /**
     * Links generated code to a <a href="https://firefox-source-docs.mozilla.org/devtools-user/debugger/how_to/use_a_source_map/index.html">source map</a>.
     */
    public static final String SOURCEMAP = "SourceMap";
    /**
     * This HTTP/1.1 (only) header can be used to upgrade an already established client/server connection to a different protocol (over the same transport protocol).<br>
     * For example, it can be used by a client to upgrade a connection from HTTP 1.1 to HTTP 2.0, or an HTTP or HTTPS connection into a WebSocket.
     */
    public static final String UPGRADE = "Upgrade";
    /**
     * Servers can advertise support for Client Hints using the {@code Accept-CH} header field or an equivalent HTML {@code <meta>} element with <a href="https://developer.mozilla.org/en-US/docs/Web/HTML/Element/meta#http-equiv">http-equiv</a> attribute.
     */
    public static final String ACCEPT_CH = "Accept-CH";
    /**
     * Servers use {@code Critical-CH} along with {@link #ACCEPT_CH} to specify that accepted client hints are also <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Client_hints#critical_client_hints">critical client hints</a>.
     */
    public static final String CRITICAL_CH = "Critical-CH";
    /**
     * User agent's branding and version.
     */
    public static final String SEC_CH_UA = "Sec-CH-UA";
    /**
     * User agent's underlying platform architecture.
     */
    public static final String SEC_CH_UA_ARCH = "Sec-CH-UA-Arch";
    /**
     * User agent's underlying CPU architecture bitness (for example "64" bit).
     */
    public static final String SEC_CH_UA_BITNESS = "Sec-CH-UA-Bitness";
    /**
     * Full version for each brand in the user agent's brand list.
     */
    public static final String SEC_CH_UA_FULL_VERSION_LIST = "Sec-CH-UA-Full-Version-List";
    /**
     * User agent is running on a mobile device or, more generally, prefers a "mobile" user experience.
     */
    public static final String SEC_CH_UA_MOBILE = "Sec-CH-UA-Mobile";
    /**
     * User agent's device model.
     */
    public static final String SEC_CH_UA_MODEL = "Sec-CH-UA-Model";
    /**
     * User agent's underlying operation system/platform.
     */
    public static final String SEC_CH_UA_PLATFORM = "Sec-CH-UA-Platform";
    /**
     * User agent's underlying operation system version.
     */
    public static final String SEC_CH_UA_PLATFORM_VERSION = "Sec-CH-UA-Platform-Version";
    /**
     * User's preference of dark or light color scheme.
     */
    public static final String SEC_CH_UA_PREFERS_COLOR_SCHEME = "Sec-CH-UA-Prefers-Color-Scheme";
    /**
     * User's preference to see fewer animations and content layout shifts.
     */
    public static final String SEC_CH_UA_PREFERS_REDUCED_MOTION = "Sec-CH-UA-Prefers-Reduced-Motion";
    /**
     * Approximate amount of available client RAM memory.<br>
     * This is part of the <a href="https://developer.mozilla.org/en-US/docs/Web/API/Device_Memory_API">Device Memory API</a>.
     */
    public static final String DEVICE_MEMORY = "Device-Memory";
    /**
     * Approximate bandwidth of the client's connection to the server, in Mbps.<br>
     * This is part of the <a href="https://developer.mozilla.org/en-US/docs/Web/API/Network_Information_API">Network Information API</a>.
     */
    public static final String DOWNLINK = "Downlink";
    /**
     * The {@code effective connection type} ("network profile") that best matches the connection's latency and bandwidth.<br>
     * This is part of the <a href="https://developer.mozilla.org/en-US/docs/Web/API/Network_Information_API">Network Information API</a>.
     */
    public static final String ECT = "ECT";
    /**
     * Application layer round trip time (RTT) in milliseconds, which includes the server processing time.<br>
     * This is part of the <a href="https://developer.mozilla.org/en-US/docs/Web/API/Network_Information_API">Network Information API</a>.
     */
    public static final String RTT = "RTT";
    /**
     * A string {@code on} that indicates the user agent's preference for reduced data usage.
     */
    public static final String SAVE_DATA = "Save-Data";
    /**
     * Indicates whether the user consents to a website or service selling or sharing their personal information with third parties.
     */
    public static final String SEC_GPC = "Sec-GPC";
    /**
     * Provides a mechanism to allow web applications to isolate their origins.
     */
    public static final String ORIGIN_ISOLATION = "Origin-Isolation";
    /**
     * Defines a mechanism that enables developers to declare a network error reporting policy.
     */
    public static final String NEL = "NEL";
    /**
     * A client can express the desired push policy for a request by sending an <a href="https://datatracker.ietf.org/doc/html/draft-ruellan-http-accept-push-policy-00#section-3.1">Accept-Push-Policy</a> header field in the request.
     */
    public static final String ACCEPT_PUSH_POLICY = "Accept-Push-Policy";
    /**
     * A client can send the <a href="https://wicg.github.io/webpackage/draft-yasskin-http-origin-signed-responses.html#rfc.section.3.7">Accept-Signature</a> header field to indicate intention to take advantage of any available signatures and to indicate what kinds of signatures it supports.
     */
    public static final String ACCEPT_SIGNATURE = "Accept-Signature";
    /**
     * Indicates that the request has been conveyed in TLS early data.
     */
    public static final String EARLY_DATA = "Early-Data";
    /**
     * A <a href="https://datatracker.ietf.org/doc/html/draft-ruellan-http-accept-push-policy-00#section-3.2">Push-Policy</a> defines the server behavior regarding push when processing a request.
     */
    public static final String PUSH_POLICY = "Push-Policy";
    /**
     * The <a href="https://wicg.github.io/webpackage/draft-yasskin-http-origin-signed-responses.html#rfc.section.3.1">Signature</a> header field conveys a list of signatures for an exchange, each one accompanied by information about how to determine the authority of and refresh that signature.
     */
    public static final String SIGNATURE = "Signature";
    /**
     * The <a href="https://wicg.github.io/webpackage/draft-yasskin-http-origin-signed-responses.html#rfc.section.5.1.2">Signed-Headers</a> header field identifies an ordered list of response header fields to include in a signature.
     */
    public static final String SIGNED_HEADERS = "Signed-Headers";
    /**
     * Set by a navigation target to opt-in to using various higher-risk loading modes.<br>
     * For example, cross-origin, same-site <a href="https://developer.mozilla.org/en-US/docs/Web/API/Speculation_Rules_API#using_prerendering">prerendering</a> requires a {@code Supports-Loading-Mode} value of {@code credentialed-prerender}.
     */
    public static final String SUPPORTS_LOADING_MODE = "Supports-Loading-Mode";
    /**
     * Identifies the originating IP addresses of a client connecting to a web server through an HTTP proxy or a load balancer.
     */
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    /**
     * Identifies the original host requested that a client used to connect to your proxy or load balancer.
     */
    public static final String X_FORWARDED_HOST = "X-Forwarded-Host";
    /**
     * Identifies the protocol (HTTP or HTTPS) that a client used to connect to your proxy or load balancer.
     */
    public static final String X_FORWARDED_PROTO = "X-Forwarded-Proto";
    /**
     * Controls DNS prefetching, a feature by which browsers proactively perform domain name resolution on both links that the user may choose to follow as well as URLs for items referenced by the document, including images, CSS, JavaScript, and so forth.
     */
    public static final String X_DNS_PREFETCH_CONTROL = "X-DNS-Prefetch-Control";
    /**
     * The <a href="https://developers.google.com/search/docs/advanced/robots/robots_meta_tag">X-Robots-Tag</a> HTTP header is used to indicate how a web page is to be indexed within public search engine results.<br>
     * The header is effectively equivalent to {@code <meta name="robots" content="…">}.
     */
    public static final String X_ROBOTS_TAG = "X-Robots-Tag";
    /**
     * Implementation-specific header that may have various effects anywhere along the request-response chain.<br>
     * Used for backwards compatibility with HTTP/1.0 caches where the {@code Cache-Control} header is not yet present.
     */
    public static final String PRAGMA = "Pragma";
    /**
     * General warning information about possible problems.
     */
    public static final String WARNING = "Warning";

}
