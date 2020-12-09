(ns advent-of-code-2020.day8)

(def flip-codes {"jmp" "nop"
                 "nop" "jmp"
                 "acc" "acc"})

(defn signum [ch]
  (if (= ch "-") -1 1))

(defn load-code [file]
  (with-open [rdr (clojure.java.io/reader file)]
    (doall (line-seq rdr))))

(defn flip-code [opcode flip index p]
  (if (and flip (= index p))
    (get flip-codes opcode)
    opcode))

(defn execute-instr [state code]
  (if (>= (state :p) (count code))
    (assoc state :done true)
    (let [p (state :p)
          cur (nth code p)
          flip (state :flip)
          flip-index (state :flip-index)
          [_ opcode sign arg] (re-matches #"([a-z]{3}) (\+|\-)([0-9]+)" cur)]
      (cond
        (>= p (count code)) (assoc state :done true)
        (.contains (state :seen) p) state
        (= "nop" (flip-code opcode flip flip-index p))
        (execute-instr {:p (inc p)
                        :acc (state :acc)
                        :flip (state :flip)
                        :flip-index (state :flip-index)
                        :seen (conj (state :seen) p)} code)
        (= "acc" (flip-code opcode flip flip-index p))
        (execute-instr {:p (inc p)
                        :acc (+ (* (signum sign) (Integer/parseInt arg)) (state :acc))
                        :flip (state :flip)
                        :flip-index (state :flip-index)
                        :seen (conj (state :seen) p)} code)
        (= "jmp" (flip-code opcode flip flip-index p))
        (execute-instr {:p (+ (* (signum sign) (Integer/parseInt arg)) p)
                        :acc (state :acc)
                        :flip (state :flip)
                        :flip-index (state :flip-index)
                        :seen (conj (state :seen) p)} code)))))

(defn execute-code [file acc flip flip-index]
  (->> file
       load-code
       (execute-instr {:p 0 :acc acc :seen [] :flip flip :flip-index flip-index})))

(defn execute-code-show-acc [file acc]
  ((execute-code file acc false 0) :acc))

(defn execute-code-with-flip [file acc]
  (loop [current-index 0]
    (let [state (execute-code file acc true current-index)]
      (if (state :done)
        (state :acc)
        (recur (inc current-index))))))

(defn -main [& args]
  (println (execute-code-show-acc "resources/input8.txt" 0))
  (println (execute-code-with-flip "resources/input8.txt" 0)))
