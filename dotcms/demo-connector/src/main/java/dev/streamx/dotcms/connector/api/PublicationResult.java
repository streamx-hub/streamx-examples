package dev.streamx.dotcms.connector.api;

/**
 * Model to describe the possible outcomes of publications.
 */
enum PublicationResult {
    //dotCMS content was successfully converted and pushed to StreamX
    SUCCESS,
    //some error happened during converting dotCMS content
    PUBLICATION_ERROR,
    //dotCMS content was successfully converted but push to StreamX failed for some reason
    STREAMX_ERROR,
}
