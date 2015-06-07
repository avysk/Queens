package nl.ocaml;

import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Queens {

    private final static int SIZE = 8;

    private static final class Position extends ArrayList<Integer> {
        Position prepend(Integer element) {
            Position newPosition = (Position) this.clone();
            newPosition.add(0, element);
            return newPosition;
        }
    }

    private static boolean newIsOk(Position position) {
        Integer newQueen = position.get(0);
        return IntStream.range(1, position.size())
                .noneMatch(i -> {
                    Integer oldQueen = position.get(i);
                    int shift = Math.abs(newQueen - oldQueen);
                    return shift == 0 || shift == i;
                });
    }

    private static Stream<Position> addNewQueen(Position position) {
        return IntStream.rangeClosed(1, SIZE).mapToObj(position::prepend);
    }

    private static Stream<Position> queens(int col) {
        if (col == 0) {
            return Stream.of(new Position());
        }
        return queens(col - 1)
                .flatMap(Queens::addNewQueen)
                .filter(Queens::newIsOk);
    }

    public static void main(String[] args) {
        queens(SIZE).forEach(System.out::println);
    }
}
