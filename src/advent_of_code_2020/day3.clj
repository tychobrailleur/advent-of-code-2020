(ns advent-of-code-2020.day3)

(defn load-landscape [file]
  (with-open [rdr (clojure.java.io/reader file)]
    (doall (line-seq rdr))))

(defn block-at [land i j]
  (let [width (count (first land))]
    (-> land
        (nth j)
        (nth (mod i width)))))

(defn count-tree [block]
  (if (= \# block) 1 0))


;; I originally thought that we were counting the trees
;; at every step of the slide, but it is not the case,
;; so these slide right / slide down functions are pretty
;; redundant but keeping them for now.

(defn slide-right [land step state]
  (let [[x y] (state :pos)
        c (state :count)]
    (if (= 0 step) state
        (let [block (block-at land (inc x) y)]
          (slide-right land (dec step) {:pos (list (inc x) y)
                                        :count c
                                        :step (state :step)})))))

(defn slide-down [land step state]
  (let [[x y] (state :pos)
        c (state :count)]
    (if (or (= y (dec (count land))) (= 0 step)) state
        (let [block (block-at land x (inc y))]
          (slide-down land (dec step) {:pos (list x (inc y))
                                       :count c
                                       :step (state :step)})))))

(defn check-tree [land state]
  (let [[x y] (state :pos)
        c (state :count)
        block (block-at land x y)]
    {:pos (list x y)
     :count (+ c (count-tree block))
     :step (state :step)}))


(defn slide
  [state land]
  (let [right (first (state :step))
        down (second (state :step))
        [x y] (state :pos)]
    (if (= y (dec (count land)))
      state
      (slide (->> state
                  (slide-right land right)
                  (slide-down land down)
                  (check-tree land)) land))))

(defn slide-step [step file]
  (slide {:pos '(0 0) :count 0 :step step}
         (load-landscape file)))

(defn -main [& args]
  (println ((slide-step '(3 1) "resources/input3.txt") :count))
  (println (reduce * (map #((slide-step % "resources/input3.txt") :count) (list '(1 1)
                                                                                '(3 1)
                                                                                '(5 1)
                                                                                '(7 1)
                                                                                '(1 2))))))
