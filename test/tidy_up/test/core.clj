(ns tidy-up.test.core
  (:use tidy-up.core
        clojure.test))

(deftest empty-works
  (is (= "\n" (tidy-up ""))))

(let [dirty  "<html><body><h1>Hi world</h1></body></html>"
      tidyed "<!DOCTYPE html PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n<html>\n  <head>\n    <meta name=\"generator\" content=\"HTML Tidy, see www.w3.org\">\n    <title>\n    </title>\n  </head>\n  <body>\n    <h1>\n      Hi world\n    </h1>\n  </body>\n</html>\n\n"
      dummy-request {}]
  (deftest simple-page
    (is (= tidyed
           (tidy-up dirty))))
  (deftest middleware-triggered
    (let [dummy-response {:body dirty :headers {"Content-Type" "text/html"}}
          dummy-app (fn [request] dummy-response)
          tidy-app (wrap-tidy-up dummy-app)]
      (is (= tidyed
             (:body (tidy-app dummy-request))))))
  (deftest middleware-not-text-html
    (let [dummy-response {:body dirty :headers {"Content-Type" "text/plain"}}
          dummy-app (fn [request] dummy-response)
          tidy-app (wrap-tidy-up dummy-app)]
      (is (= dirty
             (:body (tidy-app dummy-request))))))
  (deftest middleware-not-string
    (let [dummy-response {:body 42 :headers {"Content-Type" "text/html"}}
          dummy-app (fn [request] dummy-response)
          tidy-app (wrap-tidy-up dummy-app)]
      (is (= 42
             (:body (tidy-app dummy-request))))))
  (deftest middleware-content-type-has-charset
    (let [dummy-response {:body dirty :headers {"Content-Type" "text/html; charset=utf-8"}}
          dummy-app (fn [request] dummy-response)
          tidy-app (wrap-tidy-up dummy-app)]
      (is (= tidyed
             (:body (tidy-app dummy-request)))))))
