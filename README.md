# deploy-uberjar

Leiningen plugin to deploy uberjars to defined repository in project.

[![Clojars Project](https://clojars.org/bansd/deploy-uberjar/latest-version.svg)](https://clojars.org/bansd/deploy-uberjar)

# Example:

Add this to your plugins sections in project.clj

```clojure
[bansd/deploy-uberjar "0.1.2"]
```

This will deploy the uberjar to the release repository
```
lein deploy-uberjar <name of repository>
```
Or you can also specify 

```clojure
:repositories 
    [["clojar-releases" {:url "https://clojars.org/repo"}]
      ["snapshots" {:url "https://internalrepo.com/repo
                    :sign-releases false}]]
```
And below will deploy to repository defined by `clojar-releases`
```
lein deploy-uberjar clojar-releases
```

You can even have `:release-tasks` defined

```clojure
:release-tasks [["change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["deploy-uberjar" "releases"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]
                  ["vcs" "push"]]
```

And `lein release` will perform above release tasks, bumps up version, creates uberjar and commits the changed files to the repository.
