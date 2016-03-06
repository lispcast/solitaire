(ns solitaire.core-test
  (:require [clojure.test :refer :all]
            [solitaire.core :refer :all]
            [solitaire.card :as card]))

(deftest tableau0-tableau1
  (let [game {:tableau [{:up (list
                               (card/make :diamonds :3)
                               (card/make :clubs :4)
                               (card/make :hearts :5))}
                        {:up (list
                               (card/make :spades :6))}]}
        game2 {:tableau [{:up (list)}
                         {:up (list
                                (card/make :diamonds :3)
                                (card/make :clubs :4)
                                (card/make :hearts :5)
                                (card/make :spades :6))}]}
        move [:move/transfer 3 [:tableau 0] [:tableau 1]]]
    (is (= game2 (apply-move game move)))))

(deftest tableau0-foundation0
  (let [game {:foundations [{:up (list
                                   (card/make :diamonds :2)
                                   (card/make :diamonds :A))}]
              :tableau [{:up (list
                               (card/make :diamonds :3)
                               (card/make :clubs :4)
                               (card/make :hearts :5))}]}
        game2 {:foundations [{:up (list
                                    (card/make :diamonds :3)
                                    (card/make :diamonds :2)
                                    (card/make :diamonds :A))}]
               :tableau [{:up (list
                                (card/make :clubs :4)
                                (card/make :hearts :5))}]}
        move [:move/transfer 1 [:tableau 0] [:foundations 0]]]
    (is (= game2 (apply-move game move)))))

(deftest stock-down-stock-up
  (let [game {:stock [{:down (list
                               (card/make :spades :A)
                               (card/make :diamonds :K)
                               (card/make :diamonds :Q)
                               (card/make :clubs :6))
                       :up (list
                             (card/make :diamonds :2)
                             (card/make :diamonds :A))}]}
        game2 {:stock [{:down (list
                                (card/make :clubs :6))
                        :up (list
                              (card/make :diamonds :Q)
                              (card/make :diamonds :K)
                              (card/make :spades :A)
                              (card/make :diamonds :2)
                              (card/make :diamonds :A))}]}
        move [:move/flip 3 [:stock 0]]]
    (is (= game2 (apply-move game move)))))

(deftest tableau-down-tableau-up
  (let [game {:tableau [{:down (list
                                 (card/make :spades :A)
                                 (card/make :diamonds :K)
                                 (card/make :diamonds :Q)
                                 (card/make :clubs :6))
                         :up (list)}]}
        game2 {:tableau [{:down (list
                                  (card/make :diamonds :K)
                                  (card/make :diamonds :Q)
                                  (card/make :clubs :6))
                          :up (list
                                (card/make :spades :A))}]}
        move [:move/flip 1 [:tableau 0]]]
    (is (= game2 (apply-move game move)))))
