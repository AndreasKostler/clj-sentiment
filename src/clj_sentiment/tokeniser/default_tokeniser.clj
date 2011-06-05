(ns clj-sentiment.tokeniser.default-tokeniser)

(defn default-tokeniser [{:keys [prep-fns reader path]}]
  "Reads file(s) and splits the content based on whitespace characters."
  (if reader
    (let [files (reader path :prep-fns prep-fns)]
      (map (fn [{:keys [id text] :as file}]
             (assoc file :text (seq (.split text "\\s+")))) files))
    nil))

