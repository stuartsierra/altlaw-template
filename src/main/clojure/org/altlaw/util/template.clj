(ns org.altlaw.util.template
  (:require [clojure.contrib.java-utils :as j])
  (:import (org.antlr.stringtemplate StringTemplate StringTemplateGroup)
           (org.antlr.stringtemplate.language DefaultTemplateLexer)
           (org.apache.commons.lang StringEscapeUtils)))

(def #^{:private true} *template-group*
     (delay (let [page-templates (StringTemplateGroup. "default")]
              (when (= "development" (System/getProperty "org.altlaw.env"))
               (.setRefreshInterval page-templates 0))
              page-templates)))

(defn- template-group [] (force *template-group*))

(defn- prep-xml
  "Prepare x to be an argument for StringTemplate.  Converts keywords
  to Strings.  XML-escapes Strings."
  [x]
  (cond (string? x) (StringEscapeUtils/escapeXml x)
        (map? x) (reduce (fn [m [k v]]
                           (assoc m (j/as-str k) (prep-xml v)))
                         {} x)
        (coll? x) (map prep-xml x)
        :else (prep-xml (j/as-str x))))

(defn- prep-text
  "Prepare x to be an argument for StringTemplate.  Converts map keys
  to Strings.  Does not do any escaping of values."
  [x]
  (cond (string? x) x
        (map? x) (reduce (fn [m [k v]]
                           (assoc m (j/as-str k) (prep-text v)))
                         {} x)
        (coll? x) (map prep-text x)
        :else (j/as-str x)))

(defn- render-with-escapes [escape-fn tmpl-name attr-map]
  (let [template (-> (template-group) (.getInstanceOf tmpl-name))]
    (.setAttributes template (escape-fn attr-map))
    (.toString template)))

(def #^{:doc "Renders template named t (a String) with attributes in
  m (a map).  XML-escapes values."
        :arglists '([t m])}
     render-xml (partial render-with-escapes prep-xml))

(def #^{:doc "Renders template named t (a String) with attributes in
  m (a map).  Does not do any escaping of values."
        :arglists '([t m])}
     render-text (partial render-with-escapes prep-text))

