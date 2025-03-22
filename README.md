# Modern Maven: library which gets published to Maven Central

Example of a library suite.

It serves as an opinionated best practice for how to structure such a use-case, 
in particular with focus on Maven.

The example presented here is a pretend library named _serendipity_.
You can use it as a template for your own library suite.

- Uses Maven [CI Friendly versioning](https://maven.apache.org/guides/mini/guide-maven-ci-friendly.html) 
in order to make the release process as simple and transparent as possible.
- Uses Maven Wrapper
- Uses BouncyCastle for signing as opposed to GnuPG. Much simpler.
- All URLs related to the project's "home" are variables and are supplied by the CI platform.
- Javadocs are build for every execution in CI system as opposed to only when in "release" event.
This is because we want to know early if there is a Javadoc error.
- There is a single `mvn` invocation.

## Development model

This project uses trunk-based development. This means there is only single long-lived branch in git, `main`, 
and all releases are done from that branch.


## Releasing

When the project is ready to have a new release published:

1. Make sure the `main` branch builds and tests without errors. Look in [Actions](/../../actions) for any failed recent executions.
(in the ideal world this requirement is true; the project's `main` branch should always be kept in a 'releasable' state)
2. Go to the GitHub UI and press "Releases". Choose a tag which complies with [SemVer](https://semver.org/)
and press "Publish release". That is all!

You can optionally put a `v` in front of your tags as in `v1.4.8`. It won't become part of the Maven version string. 
Whether you use the prefix `v` or not is up to you. Just be consistent.

Note: Be careful with choosing a tag. Once something is published to Maven Central is can never be retracted. 

### What if the release execution fails?

It depends on the cause:

If it is something not related to the committed code, for example transient networking issue or a missing or incorrect GitHub Secret:

1. Correct the problem (unless it is transient).
2. Re-run the job: find the failed workflow execution in the GitHub UI and press the "Re-run all jobs" button.

If it is something related to the commited code then it is likely that you did _not_ release from a state
where the `main` branch was passing the pipeline without failure. However, if it really happens then: 

1. Delete the failed release from [Releases](/../../releases)  (this will not delete the git tag).
2. Delete the tag from [Tags](/../../tags).
3. Correct the problem with a new commit and push (but this time you _await_ the CI pipeline for that push and check
if it passes!).
4. Create a new release with the same tag as before.


## How do I know if my flow works? (without publishing)

Simple: Just create a snapshot release, meaning create a release from the GitHub UI with a prerelease suffix,
for example `3.9.0-RC1`. This will test that the signing works and that your credentials for Maven Central works. 
Without creating a true release.
