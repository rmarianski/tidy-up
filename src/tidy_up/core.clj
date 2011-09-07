(ns tidy-up.core
  (:use [clojure.java.io :only (input-stream)])
  (:import java.io.ByteArrayOutputStream
           org.w3c.tidy.Tidy))

(defn make-tidy
  "generate an instance of a preconfigured tidy object"
  []
  (doto (Tidy.)
    (.setQuiet true)
    (.setShowWarnings false)
    (.setIndentContent true)))

(defn tidy-up
  "tidy up an xml string"
  [string-to-tidy]
  (if (string? string-to-tidy)
    (let [encoding "UTF-8"]
      (with-open [outputstream (ByteArrayOutputStream.)
                  inputstream  (input-stream
                                (.getBytes string-to-tidy encoding))]
        (.parse (make-tidy) inputstream outputstream)
        (.toString outputstream encoding)))
    string-to-tidy))

(defn wrap-tidy-up
  "create middleware that tidy's up string text/html responses"
  [app]
  (fn [request]
    (let [response (app request)
          content-type (get-in response [:headers "Content-Type"])]
      (if (and content-type
               (or (= "text/html" content-type)
                   (.startsWith content-type "text/html;")))
        (update-in response [:body] tidy-up)
        response))))
