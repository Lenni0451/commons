package net.lenni0451.commons.httpclient;

import net.lenni0451.commons.httpclient.requests.HttpContentRequest;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import net.lenni0451.commons.httpclient.requests.impl.*;

import java.net.MalformedURLException;
import java.net.URL;

public interface HttpRequestBuilder {

    /**
     * Create a new GET request with the given url.
     *
     * @param url The url to send the request to
     * @return The created request
     * @throws MalformedURLException If the url is invalid
     */
    default GetRequest get(final String url) throws MalformedURLException {
        return new GetRequest(url);
    }

    /**
     * Create a new GET request with the given url.
     *
     * @param url The url to send the request to
     * @return The created request
     */
    default GetRequest get(final URL url) {
        return new GetRequest(url);
    }

    /**
     * Create a new HEAD request with the given url.
     *
     * @param url The url to send the request to
     * @return The created request
     * @throws MalformedURLException If the url is invalid
     */
    default HeadRequest head(final String url) throws MalformedURLException {
        return new HeadRequest(url);
    }

    /**
     * Create a new HEAD request with the given url.
     *
     * @param url The url to send the request to
     * @return The created request
     */
    default HeadRequest head(final URL url) {
        return new HeadRequest(url);
    }

    /**
     * Create a new DELETE request with the given url.
     *
     * @param url The url to send the request to
     * @return The created request
     * @throws MalformedURLException If the url is invalid
     */
    default DeleteRequest delete(final String url) throws MalformedURLException {
        return new DeleteRequest(url);
    }

    /**
     * Create a new DELETE request with the given url.
     *
     * @param url The url to send the request to
     * @return The created request
     */
    default DeleteRequest delete(final URL url) {
        return new DeleteRequest(url);
    }

    /**
     * Create a new POST request with the given url.
     *
     * @param url The url to send the request to
     * @return The created request
     * @throws MalformedURLException If the url is invalid
     */
    default PostRequest post(final String url) throws MalformedURLException {
        return new PostRequest(url);
    }

    /**
     * Create a new POST request with the given url.
     *
     * @param url The url to send the request to
     * @return The created request
     */
    default PostRequest post(final URL url) {
        return new PostRequest(url);
    }

    /**
     * Create a new PUT request with the given url.
     *
     * @param url The url to send the request to
     * @return The created request
     * @throws MalformedURLException If the url is invalid
     */
    default PutRequest put(final String url) throws MalformedURLException {
        return new PutRequest(url);
    }

    /**
     * Create a new PUT request with the given url.
     *
     * @param url The url to send the request to
     * @return The created request
     */
    default PutRequest put(final URL url) {
        return new PutRequest(url);
    }

    /**
     * Create a new request with the given method and url.
     *
     * @param method The method to use
     * @param url    The url to send the request to
     * @return The created request
     * @throws MalformedURLException If the url is invalid
     */
    default HttpRequest request(final String method, final String url) throws MalformedURLException {
        return new HttpRequest(method, url);
    }

    /**
     * Create a new request with the given method and url.
     *
     * @param method The method to use
     * @param url    The url to send the request to
     * @return The created request
     */
    default HttpRequest request(final String method, final URL url) {
        return new HttpRequest(method, url);
    }

    /**
     * Create a new request with the given method and url.<br>
     * This request will send content to the server if specified.
     *
     * @param method The method to use
     * @param url    The url to send the request to
     * @return The created request
     * @throws MalformedURLException If the url is invalid
     */
    default HttpContentRequest contentRequest(final String method, final String url) throws MalformedURLException {
        return new HttpContentRequest(method, url);
    }

    /**
     * Create a new request with the given method and url.<br>
     * This request will send content to the server if specified.
     *
     * @param method The method to use
     * @param url    The url to send the request to
     * @return The created request
     */
    default HttpContentRequest contentRequest(final String method, final URL url) {
        return new HttpContentRequest(method, url);
    }

}
