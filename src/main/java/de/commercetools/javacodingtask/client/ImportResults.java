package de.commercetools.javacodingtask.client;

import io.sphere.sdk.models.Base;

import java.util.List;

/**
 * The result of an import for multiple entities.
 */
public final class ImportResults extends Base {
    private List<ImportResult> results;

    public ImportResults() {
    }

    public ImportResults(final List<ImportResult> results) {
        this.results = results;
    }

    public List<ImportResult> getResults() {
        return results;
    }

    public void setResults(final List<ImportResult> results) {
        this.results = results;
    }
}
