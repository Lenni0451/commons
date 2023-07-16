package net.lenni0451.commons.network;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Start and manage a Tor proxy instance.<br>
 * The proxy has to be provided by the user.
 */
@SuppressWarnings("unused")
public class TorProxy {

    private static final Set<TorProxy> shutdownListener = Collections.newSetFromMap(new WeakHashMap<>());

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdownListener.stream().filter(TorProxy::isRunning).forEach(TorProxy::kill), "Tor Shutdown Hook"));
    }


    private final File torPath;
    private final int port;
    private final int controlPort;
    private Process torProcess;
    private int startProgress;

    /**
     * Create a new Tor proxy instance.
     *
     * @param torPath Path to the Tor executable
     */
    public TorProxy(final File torPath) {
        this(torPath, 0, 0);
    }

    /**
     * Create a new Tor proxy instance.
     *
     * @param torPath Path to the Tor executable
     * @param port    The port the proxy should listen on
     */
    public TorProxy(final File torPath, final int port) {
        this(torPath, port, 0);
    }

    /**
     * Create a new Tor proxy instance.
     *
     * @param torPath     Path to the Tor executable
     * @param port        The port the proxy should listen on
     * @param controlPort The port the control server should listen on
     */
    public TorProxy(final File torPath, final int port, final int controlPort) {
        this.torPath = torPath;
        this.port = this.getPort(port);
        this.controlPort = this.getPort(controlPort);

        shutdownListener.add(this);
    }

    /**
     * @return The Tor executable path
     */
    public File getTorPath() {
        return this.torPath;
    }

    /**
     * @return The port the proxy is listening on
     */
    public int getPort() {
        return this.port;
    }

    /**
     * @return The port the control server is listening on
     */
    public int getControlPort() {
        return this.controlPort;
    }

    /**
     * @return The current start progress of the Tor process
     */
    public int getStartProgress() {
        return this.startProgress;
    }

    /**
     * @return If the Tor process is running
     */
    public boolean isRunning() {
        return this.torProcess != null && this.torProcess.isAlive();
    }

    /**
     * @return If the Tor process is fully started
     */
    public boolean isStarted() {
        return this.isRunning() && this.startProgress == 100;
    }


    /**
     * Start the Tor process.<br>
     * A new thread will be started to listen for the start progress.
     *
     * @throws IllegalStateException If the Tor process is already running
     * @throws IOException           If the Tor process could not be started
     */
    public void start() throws IOException {
        if (this.isRunning()) throw new IllegalStateException("Tor process is already running");

        ProcessBuilder torProcessBuilder = new ProcessBuilder(this.torPath.getAbsolutePath(), "--SocksPort", String.valueOf(this.port), "--ControlPort", String.valueOf(this.controlPort));
        this.torProcess = torProcessBuilder.start();

        Thread startListener = new Thread(() -> {
            try {
                Pattern percentPattern = Pattern.compile("(?<=\\s)\\d{1,3}(?=%\\s)");
                BufferedReader br = new BufferedReader(new InputStreamReader(this.torProcess.getInputStream()));
                String line;
                while (this.startProgress < 100 && (line = br.readLine()) != null) {
                    Matcher matcher = percentPattern.matcher(line);
                    if (matcher.find()) this.startProgress = Integer.parseInt(matcher.group());
                }
                this.startProgress = 100;
            } catch (Throwable t) {
                t.printStackTrace();
                this.kill();
            }
        }, "Tor Start Listener #" + this.port + " | " + this.controlPort);
        startListener.setDaemon(true);
        startListener.start();
    }

    /**
     * Stop the Tor process.
     *
     * @throws IllegalStateException If the Tor process is not running
     */
    public void stop() {
        if (!this.isRunning()) throw new IllegalStateException("Tor process is not running");

        this.torProcess.destroy();
        this.torProcess = null;
        this.startProgress = 0;
    }

    /**
     * Kill the Tor process.<br>
     * This does nothing if the Tor process is not running.
     */
    public void kill() {
        if (this.torProcess != null && this.torProcess.isAlive()) {
            this.torProcess.destroyForcibly();
            this.torProcess = null;
            this.startProgress = 0;
        }
    }

    /**
     * Reconnect to the Tor network.<br>
     * This will change the external IP address of the proxy.
     *
     * @return If the reconnect was successful
     * @throws IOException If the control communication failed
     */
    public boolean reconnect() throws IOException {
        try (Socket controlSocket = new Socket()) {
            controlSocket.connect(new InetSocketAddress("127.0.0.1", this.controlPort));
            InputStream is = controlSocket.getInputStream();
            OutputStream os = controlSocket.getOutputStream();

            String authResponse = this.sendControlMessage(is, os, "AUTHENTICATE");
            if (authResponse != null && !authResponse.startsWith("250")) return false;

            String signalResponse = this.sendControlMessage(is, os, "SIGNAL NEWNYM");
            return signalResponse == null || signalResponse.startsWith("250");
        }
    }


    private int getPort(final int port) {
        if (port >= 1 && port <= 65535) return port;

        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress("127.0.0.1", 0));
            return serverSocket.getLocalPort();
        } catch (IOException e) {
            return ThreadLocalRandom.current().nextInt(65535) + 1;
        }
    }

    private String sendControlMessage(final InputStream is, final OutputStream os, final String message) throws IOException {
        os.write((message + "\r\n").getBytes());
        os.flush();

        byte[] response = new byte[1024];
        int read = is.read(response);
        if (read == -1) return null;
        return new String(response, 0, read).trim();
    }

}
