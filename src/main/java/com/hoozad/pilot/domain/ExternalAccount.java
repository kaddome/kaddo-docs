package com.hoozad.pilot.domain;

import java.io.Serializable;

/**
 * An externally managed account that is associated with an internal user.  For example, an
 * account with Google or Facebook.
 */
public class ExternalAccount implements Serializable {


    private ExternalAccountProvider externalProvider;


    private String externalId;


    public ExternalAccount() {
    }

    public ExternalAccount(ExternalAccountProvider externalProvider, String externalId) {
        this.externalProvider = externalProvider;
        this.externalId = externalId;
    }

    public ExternalAccountProvider getExternalProvider() {
        return externalProvider;
    }

    public void setExternalProvider(ExternalAccountProvider externalProvider) {
        this.externalProvider = externalProvider;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
            + ((externalId == null) ? 0 : externalId.hashCode());
        result = prime
            * result
            + ((externalProvider == null) ? 0 : externalProvider.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!getClass().isAssignableFrom(obj.getClass()))
            return false;
        ExternalAccount other = (ExternalAccount) obj;
        if (externalId == null) {
            if (other.externalId != null)
                return false;
        } else if (!externalId.equals(other.externalId))
            return false;
        if (externalProvider != other.externalProvider)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ExternalAccount [externalProvider=" + externalProvider
            + ", externalId=" + externalId + "]";
    }
}
