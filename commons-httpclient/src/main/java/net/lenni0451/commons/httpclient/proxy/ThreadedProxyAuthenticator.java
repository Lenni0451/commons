package net.lenni0451.commons.httpclient.proxy;

import lombok.SneakyThrows;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.PasswordAuthentication;
import java.net.URL;

public class ThreadedProxyAuthenticator extends Authenticator {

    private static final MethodHandle GET_AUTHENTICATOR;
    private static final MethodHandle REQUEST_PASSWORD_AUTHENTICATION_INSTANCE;
    private static final ThreadLocal<PasswordAuthentication> AUTHENTICATION = new ThreadLocal<>();

    static {
        MethodHandle getAuthenticator;
        try {
            getAuthenticator = MethodHandles.lookup().findStatic(Authenticator.class, "getDefault", MethodType.methodType(Authenticator.class));
        } catch (Throwable t) {
            try {
                Field f = Authenticator.class.getDeclaredField("theAuthenticator");
                f.setAccessible(true);
                getAuthenticator = MethodHandles.lookup().unreflectGetter(f);
            } catch (Throwable t2) {
                getAuthenticator = MethodHandles.constant(Authenticator.class, null);
            }
        }
        GET_AUTHENTICATOR = getAuthenticator;

        MethodHandle authenticate = null;
        try {
            authenticate = MethodHandles.lookup().findVirtual(Authenticator.class, "requestPasswordAuthenticationInstance", MethodType.methodType(PasswordAuthentication.class, String.class, InetAddress.class, int.class, String.class, String.class, String.class, URL.class, RequestorType.class));
        } catch (Throwable ignored) {
        }
        REQUEST_PASSWORD_AUTHENTICATION_INSTANCE = authenticate;
    }

    @SneakyThrows
    public static void setAuthentication(final String username, final String password) {
        Authenticator currentAuthenticator = (Authenticator) GET_AUTHENTICATOR.invokeExact();
        if (!(currentAuthenticator instanceof ThreadedProxyAuthenticator)) {
            Authenticator.setDefault(new ThreadedProxyAuthenticator(currentAuthenticator));
        }
        AUTHENTICATION.set(new PasswordAuthentication(username, password.toCharArray()));
    }

    public static void clearAuthentication() {
        AUTHENTICATION.remove();
    }


    @Nullable
    private final Authenticator parent;

    public ThreadedProxyAuthenticator(@Nullable Authenticator parent) {
        this.parent = parent;
    }

    @Override
    @SneakyThrows
    protected PasswordAuthentication getPasswordAuthentication() {
        PasswordAuthentication authentication = AUTHENTICATION.get();
        if (authentication != null) {
            return authentication;
        }
        if (this.parent != null) {
            if (REQUEST_PASSWORD_AUTHENTICATION_INSTANCE == null) {
                return this.invokeAuthenticator(this.parent);
            } else {
                return (PasswordAuthentication) REQUEST_PASSWORD_AUTHENTICATION_INSTANCE.invokeExact(
                        this.parent,
                        this.getRequestingHost(),
                        this.getRequestingSite(),
                        this.getRequestingPort(),
                        this.getRequestingProtocol(),
                        this.getRequestingPrompt(),
                        this.getRequestingScheme(),
                        this.getRequestingURL(),
                        this.getRequestorType()
                );
            }
        }
        return super.getPasswordAuthentication();
    }

    private PasswordAuthentication invokeAuthenticator(final Authenticator authenticator) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        {
            Field field = Authenticator.class.getDeclaredField("requestingHost");
            field.setAccessible(true);
            field.set(authenticator, this.getRequestingHost());
        }
        {
            Field field = Authenticator.class.getDeclaredField("requestingSite");
            field.setAccessible(true);
            field.set(authenticator, this.getRequestingSite());
        }
        {
            Field field = Authenticator.class.getDeclaredField("requestingPort");
            field.setAccessible(true);
            field.setInt(authenticator, this.getRequestingPort());
        }
        {
            Field field = Authenticator.class.getDeclaredField("requestingProtocol");
            field.setAccessible(true);
            field.set(authenticator, this.getRequestingProtocol());
        }
        {
            Field field = Authenticator.class.getDeclaredField("requestingPrompt");
            field.setAccessible(true);
            field.set(authenticator, this.getRequestingPrompt());
        }
        {
            Field field = Authenticator.class.getDeclaredField("requestingScheme");
            field.setAccessible(true);
            field.set(authenticator, this.getRequestingScheme());
        }
        {
            Field field = Authenticator.class.getDeclaredField("requestingURL");
            field.setAccessible(true);
            field.set(authenticator, this.getRequestingURL());
        }
        {
            Field field = Authenticator.class.getDeclaredField("requestingAuthType");
            field.setAccessible(true);
            field.set(authenticator, this.getRequestorType());
        }
        Method method = Authenticator.class.getDeclaredMethod("getPasswordAuthentication");
        method.setAccessible(true);
        return (PasswordAuthentication) method.invoke(authenticator);
    }

}
