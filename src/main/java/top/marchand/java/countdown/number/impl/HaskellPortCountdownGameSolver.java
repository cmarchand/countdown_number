package top.marchand.java.countdown.number.impl;

import top.marchand.java.countdown.number.CountdownGameSolver;
import top.marchand.java.countdown.number.Draw;

import java.util.*;
import java.util.stream.Stream;

public class HaskellPortCountdownGameSolver implements CountdownGameSolver {
  @Override
  public List<Solution> solve(Draw draw) {
    return solutions(
        Arrays.stream(draw.numbers()).boxed().toList(),
        draw.target()
    )
        .map(expr -> new Solution(expr.toString()))
        .toList();
  }
  static boolean isValid(Op op, int left, int right) {
    return switch (op) {
      case Add -> left <= right;
      case Sub -> left > right;
      case Mul -> left != 1 && right != 1 && left <= right;
      case Div -> right != 1 && left % right == 0;
    };
  }
  static int apply(Op op, int left, int right) {
    return switch (op) {
      case Add -> left + right;
      case Sub -> left - right;
      case Mul -> left * right;
      case Div -> left / right;
    };
  }
  OptionalInt eval(Expr expr) {
    return switch (expr) {
      case Val(var n) -> n > 0 ? OptionalInt.of(n) : OptionalInt.empty();
      case App(Op op, Expr left, Expr right) -> {
        OptionalInt l = eval(left);
        OptionalInt r = eval(right);
        yield (l.isPresent() && r.isPresent() && isValid(op, l.getAsInt(), r.getAsInt())) ?
            OptionalInt.of(apply(op, l.getAsInt(), r.getAsInt())) :
            OptionalInt.empty();

      }
    };
  }
  enum Op {
    Add, Sub, Mul, Div;
    @Override
    public String toString() {
      return switch (this) {
        case Add -> "+";
        case Div -> "/";
        case Mul -> "*";
        case Sub -> "-";
      };
    }
  }
  sealed interface Expr {
    static String brak(Expr expr) {
      return switch(expr) {
        case Val(var n) -> Integer.toString(n);
        default -> "(" + toStr(expr) + ")";
      };
    }
    static String toStr(Expr expr) {
      return switch (expr) {
        case Val(var n) -> Integer.toString(n);
        case App(var op, var left, var right) -> brak(left) + op + brak(right);
      };
    }
  }
  record Val(int n) implements Expr {
    @Override
    public String toString() {
      return Expr.toStr(this);
    }
  }
  record App(Op op, Expr left, Expr right) implements Expr {
    @Override
    public String toString() {
      return Expr.toStr(this);
    }
  }
  record Result(Expr expr, int value) {
    @Override
    public String toString() {
      return expr.toString() + " = " + value;
    }
  }
  static List<Result> combine(Result lx, Result ry) {
    // (l,x), (r,y) pattern
    var l = lx.expr();
    var x = lx.value();
    var r = ry.expr();
    var y = ry.value();

    // combine'' (l,x) (r,y) = [(App o l r, apply o x y) | o <- ops, valid' o x y]
    return Arrays.stream(Op.values()).
                 filter(op -> isValid(op, x, y)).
                 map(op -> new Result(new App(op, l, r), apply(op, x, y))).
                 toList();
  }
  static List<Result> results(List<Integer> ns) {
    if (ns.isEmpty()) {
      return List.of();
    }
    if (ns.size() == 1) {
      var n = head(ns);
      return n > 0 ? List.of(new Result(new Val(n), n)) : List.of();
    }
    var res = new ArrayList<Result>();
    for (int i = 1; i < ns.size(); i++) {
      var ls = ns.subList(0, i);
      var rs = ns.subList(i, ns.size());
      var lxs = results(ls);
      var rys = results(rs);
      for (Result lx : lxs) {
        for (Result ry : rys) {
          res.addAll(combine(lx, ry));
        }
      }
    }
    return res;
  }

  static <T> List<T> putAllInTheSameList(T head, List<T> tail) {
    final var tailLen = tail.size();
    return switch (tailLen) {
      case 0 -> List.of(head);
      case 1 -> List.of(head, tail.get(0));
      case 2 -> List.of(head, tail.get(0), tail.get(1));
      case 3 -> List.of(head, tail.get(0), tail.get(1), tail.get(2));
      default -> {
        var res = new ArrayList<T>(1 + tailLen);
        res.add(head);
        res.addAll(tail);
        yield res;
      }
    };
  }
  static <T> T head(List<T> list) {
    return list.getFirst();
  }
  static <T> List<T> tail(List<T> list) {
    final var len = list.size();
    return len == 1 ? List.of() : list.subList(1, len);
  }
  static List<List<Integer>> allSubsetsFrom(List<Integer> numbers) {
    if (numbers.isEmpty()) {
      return List.of(List.of());
    }
    var x = head(numbers);
    var xs = tail(numbers);
    var yss = allSubsetsFrom(xs);
    var res = new ArrayList<List<Integer>>(yss);
    yss.stream().
       map(l -> putAllInTheSameList(x, l)).
       forEach(res::add);
    return res;
  }
  static Stream<List<Integer>> interleave(int x, List<Integer> numbers) {
    if (numbers.isEmpty()) {
      return Stream.of(List.of(x));
    }
    var y = head(numbers);
    var ys = tail(numbers);
    return Stream.concat(
        Stream.of(putAllInTheSameList(x, numbers)),
        interleave(x, ys).map(l -> putAllInTheSameList(y, l))
    );
  }
  static Stream<List<Integer>> permutations(List<Integer> numbers) {
    if (numbers.isEmpty()) {
      return Stream.of(List.of());
    }
    var x = head(numbers);
    var xs = tail(numbers);
    return permutations(xs).flatMap(l -> interleave(x, l));
  }
  static Stream<List<Integer>> choices(List<Integer> numbers) {
    return allSubsetsFrom(numbers).stream().flatMap(HaskellPortCountdownGameSolver::permutations);
  }
  static Stream<Expr> solutions(List<Integer> numbers, int target) {
    return choices(numbers).
        flatMap(choice -> results(choice).stream()).
        filter(res -> res.value() == target).
        map(Result::expr);
  }
  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("usage: java CountDownProblem.java <comma-separated-values> <target>");
      System.exit(1);
    }

    int target = Integer.parseInt(args[1]);
    List<Integer> numbers = Stream.of(args[0].split(",")).map(Integer::parseInt).toList();
    // uniqueness check
    try {
      Set.of(numbers.toArray());
    } catch (IllegalArgumentException iae) {
      System.err.println(iae);
      System.exit(2);
    }
    var start = System.currentTimeMillis();
    solutions(numbers, target).forEach(System.out::println);
    System.out.println("Time taken (ms): " + (System.currentTimeMillis() - start));
  }
}
