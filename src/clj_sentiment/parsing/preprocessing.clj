(ns clj-sentiment.parsing.preprocessing
  (:require [clojure.contrib.str-utils2 :as str-utils]))

(def ^{:dynamic true} *default-stopwords* #{"therefore" "hasnt" "into" "yours" "again" "a" "de" "could" "were" "than" "all" "no" "sometime" "against" "each" "eleven" "during" "once" "but" "whereupon" "both" "herself" "eg" "down" "six" "km" "con" "whatever" "latter" "anything" "found" "done" "be" "cannot" "fill" "anyway" "or" "regarding" "these" "nobody" "another" "others" "most" "using" "becoming" "further" "there" "serious" "cant" "what" "anyhow" "either" "noone" "in" "may" "through" "keep" "indeed" "same" "with" "four" "although" "because" "themselves" "one" "often" "own" "nowhere" "bill" "hereby" "take" "back" "where" "third" "that" "become" "such" "couldnt" "ours" "sixty" "also" "between" "its" "without" "go" "have" "still" "they" "yet" "seeming" "why" "i" "for" "moreover" "must" "front" "least" "throughout" "therein" "was" "is" "hereupon" "cry" "interest" "from" "please" "within" "do" "it" "everyone" "various" "bottom" "had" "name" "describe" "really" "am" "been" "wherein" "co" "just" "my" "any" "two" "meanwhile" "never" "everywhere" "our" "him" "becomes" "only" "hereafter" "amoungst" "those" "much" "an" "empty" "around" "sincere" "about" "nothing" "via" "beforehand" "well" "forty" "however" "almost" "hundred" "we" "elsewhere" "out" "whom" "eight" "off" "always" "beyond" "system" "full" "put" "latterly" "how" "mostly" "she" "rather" "enough" "whose" "first" "other" "can" "here" "below" "detail" "the" "former" "twenty" "nor" "give" "while" "move" "thus" "as" "nine" "someone" "five" "sometimes" "his" "unless" "will" "at" "your" "mine" "see" "somewhere" "should" "re" "everything" "neither" "not" "next" "hence" "part" "over" "some" "too" "etc" "due" "are" "otherwise" "made" "herein" "doesn" "top" "whereafter" "would" "her" "more" "whether" "fify" "very" "fire" "last" "now" "get" "ourselves" "quite" "by" "might" "side" "whole" "of" "and" "itself" "toward" "thru" "already" "amount" "himself" "namely" "me" "thereafter" "under" "myself" "none" "whenever" "few" "seemed" "except" "seems" "else" "became" "less" "find" "whither" "un" "twelve" "thin" "perhaps" "whereas" "besides" "beside" "something" "onto" "anyone" "even" "behind" "their" "upon" "together" "seem" "has" "when" "since" "afterwards" "though" "thereby" "alone" "to" "anywhere" "up" "every" "computer" "mill" "ie" "whence" "kg" "wherever" "after" "whoever" "them" "so" "among" "somehow" "towards" "ltd" "several" "ten" "he" "if" "per" "fifteen" "which" "then" "amongst" "along" "show" "yourselves" "thick" "above" "used" "ever" "nevertheless" "three" "formerly" "thereupon" "call" "us" "this" "until" "whereby" "who" "inc" "hers" "you" "did" "across" "being" "on" "yourself" "many" "thence" "before"})

(defn to-lower-case [s]
  (.toLowerCase s))

(defn remove-default-stopwords [s]
  "Returns a lazy sequence for the items in coll that are not in the stoplist."
  (if ((complement *default-stopwords*) s) s nil))

(defn strip-non-word-characters [s]
  "Removes non-word characters (including punctuation) from s."
  (str-utils/replace s #"\W+" " "))

(defn remove-tags [s]
  "Removes (html) tags."
  (str-utils/replace s #"<([^>]+)>" ""))

(defn strip-short [n coll]
  "Strips words with less than n characters from coll"
  (filter #(> (count %) n) coll))

(defn merge-multiple-ws [s]
  "Replaces multiple consecutive occurences of ws character with a single \\space"
  (str-utils/replace s #"\s+" " "))


(defn add-prep [tokeniser prep]
  (fn [opts]
    (tokeniser (assoc opts :prep-fns (conj (:prep-fns opts) prep)))))
