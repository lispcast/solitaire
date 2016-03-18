(ns solitaire.core
  (:require [solitaire.card :as card]))

;; card
;; (card/make :diamonds :2)

;; stack
;; {:down (list card) :up (list card)}

;; game
;; {:foundations [stack x4]
;;  :stock       [stack x1]
;;  :tableau     [stack x7]}

;; move
;; (fn [game] ... new-game)

(def empty-stack {:up () :down ()})

(def empty-game
  {:foundations (vec (repeat 4 empty-stack))
   :stock       (vec (repeat 1 empty-stack))
   :tableau     (vec (repeat 7 empty-stack))})

(defn split-deck [n deck]
  [(take n deck) (drop n deck)])

(defn deal-game []
  (let [deck (shuffle card/deck)
        game empty-game
        [game deck] (reduce (fn [[game deck] t]
                              (let [[up   deck] (split-deck 1 deck)
                                    [down deck] (split-deck t deck)]
                                [(-> game
                                   (assoc-in [:tableau t :up]   up)
                                   (assoc-in [:tableau t :down] down))
                                 deck]))
                      [game deck] (range 7))]
    (assoc-in game [:stock 0 :down] deck)))

(defn move [game count from to transform]
  (let [cards (take count (get-in game from))]
    (-> game
      (update-in from (partial drop count))
      (update-in to into (transform cards)))))

(defn transfer [game count from to]
  (move game
    count
    (conj (vec from) :up)
    (conj (vec to)   :up)
    reverse))

(defn flip [game count stack]
  (move game
    count
    (conj (vec stack) :down)
    (conj (vec stack)   :up)
    identity))

(defn apply-move [game [action & args]]
  (case action
    :move/transfer
    (apply transfer game args)

    :move/flip
    (apply flip game args)))

(defn to-foundation-one-card [game [type n _ [to-area _]]]
  (when (and
          (= :move/transfer type)
          (= :foundations to-area))
    (when (not= 1 n)
      (format "You can only transfer one card to the foundations at a time. You tried to transfer %s cards." n))))

(defn to-foundation-empty-ace [game [type n from [to-area to-idx :as to]]]
  (when (and
          (= :move/transfer type)
          (= :foundations to-area)
          (= 1 n)
          (empty? (:up (get-in game to))))
    (let [from-top (first (:up (get-in game from)))]
      (when (not= :A (card/value from-top))
        (format "You can only move an Ace to an empty foundation. You tried to move %s." from-top)))))

(defn to-foundation-not-empty+1 [game [type n from [to-area _ :as to]]]
  (when (and
          (= :move/transfer type)
          (= :foundations to-area)
          (= 1 n)
          (not (empty? (:up (get-in game to)))))
    (let [from-top (first (:up (get-in game from)))
          found-top (first (:up (get-in game to)))]
      (when (not= (card/numeric-value from-top)
              (+ 1 (card/numeric-value found-top)))
        (format "You can only stack cards up in sequential order in the foundations. You tried to put %s on top of %s."
          from-top found-top)))))

(defn to-foundation-not-empty-same-suit [game [type n from [to-area _ :as to]]]
  (when (and
          (= :move/transfer type)
          (= :foundations to-area)
          (= 1 n)
          (not (empty? (:up (get-in game to)))))
    (let [from-top (first (:up (get-in game from)))
          found-top (first (:up (get-in game to)))]
      (when (not= (card/suit from-top)
              (card/suit found-top))
        (format "You can only stack cards up of the same in the foundations. You tried to put %s on top of %s."
          from-top found-top)))))

(defn from-foundation-one-card [game [type n [from-area _] _]]
  (when (and
          (= :move/transfer type)
          (= :foundations from-area))
    (when (not= 1 n)
      (format "You can only transfer one card from the foundations at a time. You tried to transfer %s cards." n))))

(defn flip-foundation-illegal [game [type _ [area]]]
  (when (and
          (= :move/flip type)
          (= :foundations area))
    (format "You cannot flip cards in the foundations.")))

(defn to-stock-illegal [game [type _ [to-area]]]
  (when (and
          (= :move/transfer type)
          (= :stock to-area))
    (format "You cannot transfer cards to the stock.")))

(defn from-stock-one-card [game [type n [from-area] _]]
  (when (and
          (= :move/transfer type)
          (= :stock from-area))
    (when (not= 1 n)
      (format "You can only transfer one card from the stock at a time. You tried to transfer %s cards." n))))

(defn flip-stock-3 [game [type n [area]]]
  (when (and
          (= :move/flip type)
          (= :stock area))
    (when (not= 3 n)
      (format "You have to flip 3 cards in the stock at a time. You tried to flip %s." n))))

(defn to-tableau-empty [game [type n from [to-area _ :as to]]]
  (when (and
          (= :move/transfer type)
          (= :tableau to-area)
          (empty? (:up   (get-in game to)))
          (empty? (:down (get-in game to))))
    (let [bottom (last (take n (:up (get-in game from))))]
      (when (= :K (card/value bottom))
        (format "Only a King can go in an empty tableau stack. You tried to put %s." bottom)))))

(defn to-tableau-1 [game [type n from [to-area _ :as to]]]
  (when (and
          (= :move/transfer type)
          (= :tableau to-area)
          (not (empty? (:down (get-in game to)))))
    (let [bottom (last (take n (:up (get-in game from))))
          top (first (:up (get-in game to)))]
      (when (not= (card/numeric-value bottom)
              (- (card/numeric-value top) 1))
        (format "You have to stack cards in decreasing, sequential order on the tableau. You tried to stack %s on top of %s."
          bottom top)))))

(defn to-tableau-color [game [type n from [to-area _ :as to]]]
  (when (and
          (= :move/transfer type)
          (= :tableau to-area)
          (not (empty? (:down (get-in game to)))))
    (let [bottom (last (take n (:up (get-in game from))))
          top (first (:up (get-in game to)))]
      (when (= (card/color bottom) (card/color top))
        (format "You have to stack cards in alternating colors on the tableau. You tried to stack %s on top of %s."
          bottom top)))))

(defn flip-tableau-empty [game [type n [area :as stack]]]
  (when (and
          (= :move/flip type)
          (= :tableau area))
    (when (not (empty? (:up (get-in game stack))))
      (format "You can only flip cards in the tableau when the up stack is empty."))))

(defn flip-tableau-1 [game [type n [area :as stack]]]
  (when (and
          (= :move/flip type)
          (= :tableau area))
    (when (not= 1 n)
      (format "You can only flip one card at a time in the tableau."))))

(defn from-needs-cards [game [type n from _]]
  (when (= :move/transfer type)
    (let [up-count (count (:up (get-in game from)))]
      (when (< up-count n)
        (format "You tried to move %s cards from %s, but there are only %s cards."
          n from up-count)))))

(defn flip-needs-cards [game [type n area]]
  (when (= :move/flip type)
    (when (empty? (:down (get-in game area)))
      "You tried to flip cards that don't exist.")))

(def rules
  [;; transfer to foundation
   to-foundation-one-card
   to-foundation-empty-ace
   to-foundation-not-empty+1
   to-foundation-not-empty-same-suit
   ;; transfer from foundation
   from-foundation-one-card
   ;; flip foundation
   flip-foundation-illegal
   ;; transfer to stock
   to-stock-illegal
   ;; transfer from stock
   from-stock-one-card
   ;; flip stock
   flip-stock-3
   ;; transfer to tableau
   to-tableau-empty
   to-tableau-1
   to-tableau-color
   ;; transfer from tableau
   ;; flip tableau
   flip-tableau-empty
   flip-tableau-1
   ;; general
   from-needs-cards
   flip-needs-cards
   ])

(defn validate-move
  "Given a game, check if a move is valid against all rules. If it is
  valid, return nil. Otherwise, return a list of Strings describing
  the problems with the move."
  [game move]
  (->> rules
    (map #(% game move))
    (remove nil?)
    seq))
