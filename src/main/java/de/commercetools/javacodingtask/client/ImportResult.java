package de.commercetools.javacodingtask.client;

import io.sphere.sdk.models.Base;

/**
 * The result of the import for a single entity.
 */
public class ImportResult extends Base {
    private String id;
    private String errorCode;
    private boolean success;

    public ImportResult() {
    }

    /**
     *
     * @param id the id of the imported object
     * @param success true if import is done correctly
     * @param errorCode error code, nullable
     */
    public ImportResult(final String id, final boolean success, final String errorCode) {
        this.errorCode = errorCode;
        this.id = id;
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }
}
