package net.lenni0451.commons.httpclient.constants;

import net.lenni0451.commons.httpclient.model.ContentType;

import java.nio.charset.StandardCharsets;

public class ContentTypes {

    public static final ContentType WILDCARD = new ContentType("*/*");
    public static final ContentType APPLICATION_ATOM_XML = new ContentType("application/atom+xml", StandardCharsets.ISO_8859_1);
    public static final ContentType APPLICATION_FORM_URLENCODED = new ContentType("application/x-www-form-urlencoded", StandardCharsets.ISO_8859_1);
    public static final ContentType APPLICATION_JSON = new ContentType("application/json", StandardCharsets.UTF_8);
    public static final ContentType APPLICATION_OCTET_STREAM = new ContentType("application/octet-stream");
    public static final ContentType APPLICATION_SOAP_XML = new ContentType("application/soap+xml", StandardCharsets.UTF_8);
    public static final ContentType APPLICATION_SVG_XML = new ContentType("application/svg+xml", StandardCharsets.ISO_8859_1);
    public static final ContentType APPLICATION_XHTML_XML = new ContentType("application/xhtml+xml", StandardCharsets.ISO_8859_1);
    public static final ContentType APPLICATION_XML = new ContentType("application/xml", StandardCharsets.ISO_8859_1);
    public static final ContentType IMAGE_BMP = new ContentType("image/bmp");
    public static final ContentType IMAGE_GIF = new ContentType("image/gif");
    public static final ContentType IMAGE_JPEG = new ContentType("image/jpeg");
    public static final ContentType IMAGE_PNG = new ContentType("image/png");
    public static final ContentType IMAGE_SVG = new ContentType("image/svg+xml");
    public static final ContentType IMAGE_TIFF = new ContentType("image/tiff");
    public static final ContentType IMAGE_WEBP = new ContentType("image/webp");
    public static final ContentType MULTIPART_FORM_DATA = new ContentType("multipart/form-data", StandardCharsets.ISO_8859_1);
    public static final ContentType TEXT_HTML = new ContentType("text/html", StandardCharsets.ISO_8859_1);
    public static final ContentType TEXT_PLAIN = new ContentType("text/plain", StandardCharsets.ISO_8859_1);
    public static final ContentType TEXT_XML = new ContentType("text/xml", StandardCharsets.ISO_8859_1);

}
