package nl.ocaml;

import java.util.LinkedList;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Queens {

    private final static int SIZE = 8;

    /**
     * Representation of the position of few queens on the board as implementation of List.
     *
     * Contains the positions of queens in the few rightmost columns of the board (from left to right).
     *
     * For example, to describe the rightmost three columns of the following Position:
     *
     *  . . . . . . . x
     *  . x . . . . . .
     *  . . . x . . . .
     *  x . . . . . . .
     *  . . . . . . x .
     *  . . . . x . . .
     *  . . x . . . . .
     *  . . . . . x . .
     *
     *  the Position will contain [1, 4, 8].
     */
    private static final class Position extends LinkedList<Integer> {
        /**
         * "Functional" insert of a new column into existing Position.
         * @param element position of the queen in a column to add
         * @return New Position object, containing one more column on the left
         */
        Position prepend(Integer element) {
            // we do shallow clone, so there's not so much overhead in creating a new object
            Position newPosition = (Position) this.clone();
            newPosition.add(0, element);
            return newPosition;
        }
    }

    /**
     * Check that the queen in the leftmost column of the given position is safe with respect to queens in other
     * columns.
     * @param position Position to check
     * @return true if the leftmost queen is safe, false otherwise
     */
    private static boolean lastAddedQueenIsSafe(Position position) {
        Integer newQueen = position.get(0);
        // for all the columns to the right of the leftmost one
        return IntStream.range(1, position.size())
                // check that there are non-safe queens
                .noneMatch(i -> {
                    Integer oldQueen = position.get(i);
                    int shift = Math.abs(newQueen - oldQueen);
                    // new queen is not safe with respect to the queen which is 'i' columns away if it is either on the
                    // same row or on the same diagonal, i.e. 'i' rows away
                    return shift == 0 || shift == i;
                });
    }

    /**
     * From the given position create a stream of positions which can be obtained by adding one column from the left.
     * @param position original Position
     * @return Stream of all Positions, derived from the given Position
     */
    private static Stream<Position> addNewQueen(Position position) {
        // New queen can be in any row from 1 to SIZE
        return IntStream.rangeClosed(1, SIZE).mapToObj(position::prepend);
    }

    /**
     * Stream of all valid (=safe) positions of queens in a few rightmost columns.
     * @param columns Number of columns to calculate positions for
     * @return Stream of all valid Positions
     */
    private static Stream<Position> queens(int columns) {
        // If there are no columns, there is only one possible positions -- empty
        if (columns == 0) {
            return Stream.of(new Position());
        }
        // Otherwise, take all safe positions for the all columns except the leftmost one
        return queens(columns - 1)
                // generate all possible positions from them
                // since addNewQueen generates Stream on its own, use flatMap to glue all the streams together
                .flatMap(Queens::addNewQueen)
                // and keep only safe positions
                .filter(Queens::lastAddedQueenIsSafe);
    }

    public static void main(String[] args) {
        queens(SIZE).forEach(System.out::println);
    }
}
