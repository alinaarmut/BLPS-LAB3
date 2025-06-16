package org.example.jca;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.*;
import lombok.RequiredArgsConstructor;
import me.dynomake.yookassa.Yookassa;

import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class YookassaManagedConnection implements ManagedConnection {

    private final int shopId;
    private final String shopKey;

    @Override
    public Object getConnection(Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException {
        return new YookassaConnection(shopId, shopKey);
    }

    @Override
    public void destroy() throws ResourceException {
    }

    @Override
    public void cleanup() throws ResourceException {
    }

    @Override
    public void associateConnection(Object o) throws ResourceException {
    }

    @Override
    public void addConnectionEventListener(ConnectionEventListener connectionEventListener) {
    }

    @Override
    public void removeConnectionEventListener(ConnectionEventListener connectionEventListener) {
    }

    @Override
    public XAResource getXAResource() throws ResourceException {
        return null;
    }

    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {
        return null;
    }

    @Override
    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter printWriter) throws ResourceException {

    }

    @Override
    public PrintWriter getLogWriter() throws ResourceException {
        return null;
    }

}
