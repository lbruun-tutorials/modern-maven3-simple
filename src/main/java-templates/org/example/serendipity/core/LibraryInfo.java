package org.example.serendipity.core;

import java.time.Instant;
import java.util.Optional;
import java.util.regex.Pattern;

/** Static information about this library. */
public class LibraryInfo {

    // This class is meant to be used with the Template Maven Plugin
    // (https://www.mojohaus.org/templating-maven-plugin/)
    //
    // The plugin will replace the ${...} values at build time with values from the Maven session.

    private static final Pattern REPLACEMENT_VAR_PATTERN =
            Pattern.compile("\\$\\{[A-Za-z0-9.\\-_\\[\\]]+}");

    private LibraryInfo() {
    }

    /**
     * Version of library.
     *
     * <p>
     * Returns empty string if unknown, never {@code null}.
     */
    public static String version = fixUnresolved("${project.version}");

    /**
     * Time the library was build/compiled.
     *
     * <p>
     * Returns empty if no build time information is available.
     */
    // Note: This comes from 'maven.build.timestamp' but Maven does not make
    // this variable available for property substition directly. Instead, we
    // have to create a user property for it ('buildTime'). And then use that as an alias.
    // See the POM for more information.
    public static Optional<Instant> buildTime = getInstant("${buildTime}"); // must be in RFC-3339 format


    /**
     * Time of the commit to version control system (VCS) for the commit
     * from which this library was build.
     *
     * <p>
     * Returns empty if unknown/unavailable.
     */
    public static Optional<Instant> vcsCommitTime = getInstant("${git.commit.time}"); // must be in RFC-3339 format

    /**
     * Commit identifier in the version control system (VCS) for the commit
     * from which this library was. A commit identifier is typically an SHA-1 value.
     *
     * <p>Also sometimes referred to as a <i>commit reference</i>.
     *
     * <p>
     * Returns empty string if unknown/unavailable, never {@code null}.
     */
    public static String vcsCommitId = fixUnresolved("${git.commit.id}");

    /**
     * URL which allows to browse the exact version of the source code
     * that produced this library. This represents a view of the source code
     * at the time this library was build.
     *
     * <p>
     * For example: {@code 'https://github.com/foo/bar/tree/3.0.1'}
     * <p>
     * Returns empty string if unknown/unavailable, never {@code null}.
     */
    public static String vcsUrl = fixUnresolved("${scm.url}");

    /**
     * URL for the home page for the project which produced this library.
     * <p>
     * Returns empty string if unknown, never {@code null}.
     */
    public static String projectUrl = fixUnresolved("${project.url}");

    /**
     * Name of license which applies to this library.
     * For example: {@code 'The Apache License, Version 2.0'}.
     * <p>
     * Returns empty string if unknown or no license, never {@code null}.
     * @see #licenseUrl
     */
    public static String licenseName = fixUnresolved("${project.licenses[0].name}");

    /**
     * URL from where the full license text can be retrieved for the license which applies to this library.
     * For example: {@code 'https://www.apache.org/licenses/LICENSE-2.0.txt'}.
     * <p>
     * Returns empty string if unknown or no license, never {@code null}.
     * @see #licenseName
     */
    public static String licenseUrl = fixUnresolved("${project.licenses[0].url}");


    /**
     * Replaces unresolved placeholders with an empty string.
     *
     * Note that unresolved placeholders typically point to a flaw in your POM or similar.
     * Therefore, you may want to skip this logic and deal with it as a runtime failure
     * instead? Or simply not deal with it?
     */
    private static String fixUnresolved(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return REPLACEMENT_VAR_PATTERN
                .matcher(str).replaceAll("");
    }

    private static Optional<Instant> getInstant(String str) {
        String s = fixUnresolved(str);
        if (s == null || (s.isEmpty())) {
            return Optional.empty();
        }
        return Optional.of(Instant.parse(s));
    }
}
