## tidy-up

Hiccup is fantastic, but generates markup that is hard to read when viewing the source to debug. Tidy-up contains middleware to format that html to make it easier to read.

## Usage

;; middleware to tidy up text/html output
(def app (wrap-tidy-up main-routes))

;; function to tidy up a string
(tidy-up "<html><body><h1>Hi World</h1></body></html>")

## License

Copyright (C) 2011 Robert Marianski

Distributed under the Eclipse Public License, the same as Clojure.
