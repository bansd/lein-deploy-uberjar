(ns leiningen.deploy-uberjar
    (:require [leiningen.uberjar :as luber]
              [leiningen.pom :as lpom]
              [leiningen.deploy :as ldeploy]
              [leiningen.clean :as lclean]
              [leiningen.uberjar :as luberjar]
              [clojure.java.io :as io]))

(defn exit [message]
  (leiningen.core.main/info message)
  (System/exit 1))

(defn repo-target?
  "verify the given repository is defined in the project"
  [project repo]
  (some #( = repo %) (map #(first %) (:repositories project))))

(defn verify-repo [project repo]
  (when-not (repo-target? project repo)
    (exit (format "Could not find the target repository: %s." repo))))

(defn get-uberjar-name [project]
  (format "%s-%s-standalone.jar" (:name project) (:version project)))
    
(defn get-uberjar-path [project]
  (format (format "%s/%s" (:target-path project) (get-uberjar-name project))))

(defn get-group-name [project]
  (format "%s/%s" (:group project) (:name project)))

(defn uberjar? [uberjar]
  (.exists (io/as-file uberjar)))

(defn deploy-uberjar
  "lein deploy-uberjar releases
  
  Deploys release uberjar to :release repository defined in repositories"
  [project & args]
  (let [repo (first args)
        uberjar (get-uberjar-path project)]
    (verify-repo project repo)
  (lclean/clean project)
  (luberjar/uberjar project (:main project))
  (if (uberjar? (get-uberjar-path project))
    (do 
      (ldeploy/deploy project repo (get-group-name project) (:version project) uberjar (lpom/pom project)))
    (exit "error creating or locating uberjar"))))
  ; (leiningen.core.main/info "my first plugin!!")
  ; (leiningen.core.main/warn "warn")
  ; (leiningen.core.main/debug "debug")