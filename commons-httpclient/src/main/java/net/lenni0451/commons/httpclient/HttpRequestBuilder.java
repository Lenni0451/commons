package net.lenni0451.commons.httpclient;

import net.lenni0451.commons.httpclient.requests.HttpContentRequest;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import net.lenni0451.commons.httpclient.requests.impl.*;

import java.net.MalformedURLException;
import java.net.URL;

public interface HttpRequestBuilder {

    default GetRequest get(final String url) throws MalformedURLException {
        return new GetRequest(url);
    }

    default GetRequest get(final URL url) {
        return new GetRequest(url);
    }

    default HeadRequest head(final String url) throws MalformedURLException {
        return new HeadRequest(url);
    }

    default HeadRequest head(final URL url) {
        return new HeadRequest(url);
    }

    default DeleteRequest delete(final String url) throws MalformedURLException {
        return new DeleteRequest(url);
    }

    default DeleteRequest delete(final URL url) {
        return new DeleteRequest(url);
    }

    default PostRequest post(final String url) throws MalformedURLException {
        return new PostRequest(url);
    }

    default PostRequest post(final URL url) {
        return new PostRequest(url);
    }

    default PutRequest put(final String url) throws MalformedURLException {
        return new PutRequest(url);
    }

    default PutRequest put(final URL url) {
        return new PutRequest(url);
    }

    default HttpRequest request(final String method, final String url) throws MalformedURLException {
        return new HttpRequest(method, url);
    }

    default HttpRequest request(final String method, final URL url) {
        return new HttpRequest(method, url);
    }

    default HttpContentRequest contentRequest(final String method, final String url) throws MalformedURLException {
        return new HttpContentRequest(method, url);
    }

    default HttpContentRequest contentRequest(final String method, final URL url) {
        return new HttpContentRequest(method, url);
    }

}
