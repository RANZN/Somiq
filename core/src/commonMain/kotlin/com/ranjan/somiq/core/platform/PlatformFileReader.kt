package com.ranjan.somiq.core.platform

/**
 * Reads the content of a URI (e.g. content:// from gallery) into a ByteArray.
 * Returns null if the URI cannot be read.
 */
expect fun readUriToBytes(uri: String): ByteArray?
