package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_smtp;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Provider;

public final class WTUPCP_JSSEProvider extends Provider {
    public WTUPCP_JSSEProvider() {
        super("HarmonyJSSE", 1.0d, "Harmony JSSE Provider");
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                WTUPCP_JSSEProvider.this.put("SSLContext.TLS", "org.apache.harmony.xnet.provider.jsse.SSLContextImpl");
                WTUPCP_JSSEProvider.this.put("Alg.Alias.SSLContext.TLSv1", "TLS");
                WTUPCP_JSSEProvider.this.put("KeyManagerFactory.X509", "org.apache.harmony.xnet.provider.jsse.KeyManagerFactoryImpl");
                WTUPCP_JSSEProvider.this.put("TrustManagerFactory.X509", "org.apache.harmony.xnet.provider.jsse.TrustManagerFactoryImpl");
                return null;
            }
        });
    }
}
