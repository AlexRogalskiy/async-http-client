package org.asynchttpclient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.util.Timer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

import javax.net.ssl.SSLContext;

import org.asynchttpclient.channel.SSLEngineFactory;
import org.asynchttpclient.channel.pool.KeepAliveStrategy;
import org.asynchttpclient.filter.IOExceptionFilter;
import org.asynchttpclient.filter.RequestFilter;
import org.asynchttpclient.filter.ResponseFilter;
import org.asynchttpclient.netty.EagerNettyResponseBodyPart;
import org.asynchttpclient.netty.LazyNettyResponseBodyPart;
import org.asynchttpclient.netty.NettyResponseBodyPart;
import org.asynchttpclient.netty.channel.pool.ChannelPool;
import org.asynchttpclient.proxy.ProxyServer;
import org.asynchttpclient.proxy.ProxyServerSelector;

public interface AsyncHttpClientConfig {

    /**
     * @return the version of AHC
     */
    String getAhcVersion();

    /**
     * Return the name of {@link AsyncHttpClient}, which is used for thread
     * naming and debugging.
     *
     * @return the name.
     */
    String getThreadPoolName();

    /**
     * Return the maximum number of connections an {@link AsyncHttpClient} can
     * handle.
     *
     * @return the maximum number of connections an {@link AsyncHttpClient} can
     *         handle.
     */
    int getMaxConnections();

    /**
     * Return the maximum number of connections per hosts an
     * {@link AsyncHttpClient} can handle.
     *
     * @return the maximum number of connections per host an
     *         {@link AsyncHttpClient} can handle.
     */
    int getMaxConnectionsPerHost();

    /**
     * Return the maximum time in millisecond an {@link AsyncHttpClient} can
     * wait when connecting to a remote host
     *
     * @return the maximum time in millisecond an {@link AsyncHttpClient} can
     *         wait when connecting to a remote host
     */
    int getConnectTimeout();

    /**
     * Return the maximum time, in milliseconds, a
     * {@link org.asynchttpclient.ws.WebSocket} may be idle before being timed
     * out.
     * 
     * @return the maximum time, in milliseconds, a
     *         {@link org.asynchttpclient.ws.WebSocket} may be idle before being
     *         timed out.
     */
    int getWebSocketTimeout();

    /**
     * Return the maximum time in millisecond an {@link AsyncHttpClient} can
     * stay idle.
     *
     * @return the maximum time in millisecond an {@link AsyncHttpClient} can
     *         stay idle.
     */
    int getReadTimeout();

    /**
     * Return the maximum time in millisecond an {@link AsyncHttpClient} will
     * keep connection in pool.
     *
     * @return the maximum time in millisecond an {@link AsyncHttpClient} will
     *         keep connection in pool.
     */
    int getPooledConnectionIdleTimeout();

    /**
     * Return the maximum time in millisecond an {@link AsyncHttpClient} waits
     * until the response is completed.
     *
     * @return the maximum time in millisecond an {@link AsyncHttpClient} waits
     *         until the response is completed.
     */
    int getRequestTimeout();

    /**
     * Is HTTP redirect enabled
     *
     * @return true if enabled.
     */
    boolean isFollowRedirect();

    /**
     * Get the maximum number of HTTP redirect
     *
     * @return the maximum number of HTTP redirect
     */
    int getMaxRedirects();

    /**
     * Is the {@link ChannelPool} support enabled.
     *
     * @return true if keep-alive is enabled
     */
    boolean isKeepAlive();

    /**
     * Return the USER_AGENT header value
     *
     * @return the USER_AGENT header value
     */
    String getUserAgent();

    /**
     * Is HTTP compression enforced.
     *
     * @return true if compression is enforced
     */
    boolean isCompressionEnforced();

    /**
     * Return the {@link java.util.concurrent.ThreadFactory} an
     * {@link AsyncHttpClient} use for handling asynchronous response.
     *
     * @return the {@link java.util.concurrent.ThreadFactory} an
     *         {@link AsyncHttpClient} use for handling asynchronous response.
     *         If no {@link ThreadFactory} has been explicitly provided, this
     *         method will return <code>null</code>
     */
    ThreadFactory getThreadFactory();

    /**
     * An instance of {@link ProxyServer} used by an {@link AsyncHttpClient}
     *
     * @return instance of {@link ProxyServer}
     */
    ProxyServerSelector getProxyServerSelector();

    /**
     * Return an instance of {@link SSLContext} used for SSL connection.
     *
     * @return an instance of {@link SSLContext} used for SSL connection.
     */
    SSLContext getSslContext();

    /**
     * Return the current {@link Realm}
     *
     * @return the current {@link Realm}
     */
    Realm getRealm();

    /**
     * Return the list of {@link RequestFilter}
     *
     * @return Unmodifiable list of {@link ResponseFilter}
     */
    List<RequestFilter> getRequestFilters();

    /**
     * Return the list of {@link ResponseFilter}
     *
     * @return Unmodifiable list of {@link ResponseFilter}
     */
    List<ResponseFilter> getResponseFilters();

    /**
     * Return the list of {@link java.io.IOException}
     *
     * @return Unmodifiable list of {@link java.io.IOException}
     */
    List<IOExceptionFilter> getIoExceptionFilters();

    /**
     * Return the number of time the library will retry when an
     * {@link java.io.IOException} is throw by the remote server
     *
     * @return the number of time the library will retry when an
     *         {@link java.io.IOException} is throw by the remote server
     */
    int getMaxRequestRetry();

    /**
     * @return the disableUrlEncodingForBoundRequests
     */
    boolean isDisableUrlEncodingForBoundRequests();

    /**
     * In the case of a POST/Redirect/Get scenario where the server uses a 302
     * for the redirect, should AHC respond to the redirect with a GET or
     * whatever the original method was. Unless configured otherwise, for a 302,
     * AHC, will use a GET for this case.
     *
     * @return <code>true</code> if string 302 handling is to be used, otherwise
     *         <code>false</code>.
     */
    boolean isStrict302Handling();

    /**
     * Return the maximum time in millisecond an {@link AsyncHttpClient} will
     * keep connection in the pool, or -1 to keep connection while possible.
     *
     * @return the maximum time in millisecond an {@link AsyncHttpClient} will
     *         keep connection in the pool, or -1 to keep connection while
     *         possible.
     */
    int getConnectionTtl();

    boolean isAcceptAnyCertificate();

    /**
     * @return the array of enabled protocols
     */
    String[] getEnabledProtocols();

    /**
     * @return the array of enabled cipher suites
     */
    String[] getEnabledCipherSuites();

    /**
     * @return the size of the SSL session cache
     */
    Integer getSslSessionCacheSize();

    /**
     * @return the SSL session timeout in seconds (optional)
     */
    Integer getSslSessionTimeout();

    int getHttpClientCodecMaxInitialLineLength();

    int getHttpClientCodecMaxHeaderSize();

    int getHttpClientCodecMaxChunkSize();

    boolean isDisableZeroCopy();

    int getHandshakeTimeout();

    SSLEngineFactory getSslEngineFactory();

    int getChunkedFileChunkSize();

    int getWebSocketMaxBufferSize();

    int getWebSocketMaxFrameSize();

    boolean isKeepEncodingHeader();

    int getShutdownQuietPeriod();

    int getShutdownTimeout();

    Map<ChannelOption<Object>, Object> getChannelOptions();

    EventLoopGroup getEventLoopGroup();

    boolean isPreferNative();

    AdditionalPipelineInitializer getHttpAdditionalPipelineInitializer();

    AdditionalPipelineInitializer getWsAdditionalPipelineInitializer();

    ResponseBodyPartFactory getResponseBodyPartFactory();

    ChannelPool getChannelPool();

    Timer getNettyTimer();

    KeepAliveStrategy getKeepAliveStrategy();

    interface AdditionalPipelineInitializer {

        void initPipeline(ChannelPipeline pipeline) throws Exception;
    }

    enum ResponseBodyPartFactory {

        EAGER {
            @Override
            public NettyResponseBodyPart newResponseBodyPart(ByteBuf buf, boolean last) {
                return new EagerNettyResponseBodyPart(buf, last);
            }
        },

        LAZY {

            @Override
            public NettyResponseBodyPart newResponseBodyPart(ByteBuf buf, boolean last) {
                return new LazyNettyResponseBodyPart(buf, last);
            }
        };

        public abstract NettyResponseBodyPart newResponseBodyPart(ByteBuf buf, boolean last);
    }
}