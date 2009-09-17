(ns org.altlaw.util.test-template
  (:use org.altlaw.util.template
        clojure.test))

(deftest t-test1
  (is (= "<p>Hello, Foo &amp; Bar</p>"
         (render-xml "test1" {:name "Foo & Bar"}))))

(deftest t-test1-text
  (is (= "<p>Hello, <i>Foo</i></p>"
         (render-text "test1" {:name "<i>Foo</i>"}))))

(deftest t-subdir
  (is (= "<div><p>Hello, Bob</p></div>"
         (render-xml "subdir/subtest" {:name "Bob"}))))