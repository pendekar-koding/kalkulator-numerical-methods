package parser;

import java.util.Map;

public class ExpressionParser {
    private final String input;
    private int pos;
    private int ch;

    public ExpressionParser(String input) {
        this.input = input.replaceAll("\\s+", "");
    }

    public double evaluate(Map<String, Double> variables) {
        pos = -1;
        nextChar();
        double x = parseExpression(variables);
        if (pos < input.length()) {
            throw new IllegalArgumentException("Karakter tidak dikenali: '" + (char) ch + "'");
        }
        return x;
    }

    private void nextChar() {
        ch = (++pos < input.length()) ? input.charAt(pos) : -1;
    }

    private boolean eat(int charToEat) {
        if (ch == charToEat) {
            nextChar();
            return true;
        }
        return false;
    }

    private double parseExpression(Map<String, Double> vars) {
        double x = parseTerm(vars);
        while (true) {
            if (eat('+')) x += parseTerm(vars);
            else if (eat('-')) x -= parseTerm(vars);
            else return x;
        }
    }

    private double parseTerm(Map<String, Double> vars) {
        double x = parsePower(vars);
        while (true) {
            if (eat('*')) x *= parsePower(vars);
            else if (eat('/')) x /= parsePower(vars);
            else return x;
        }
    }

    private double parsePower(Map<String, Double> vars) {
        double x = parseUnary(vars);
        if (eat('^')) {
            x = Math.pow(x, parsePower(vars));
        }
        return x;
    }

    private double parseUnary(Map<String, Double> vars) {
        if (eat('+')) return parseUnary(vars);
        if (eat('-')) return -parseUnary(vars);
        return parsePrimary(vars);
    }

    private double parsePrimary(Map<String, Double> vars) {
        if (eat('(')) {
            double x = parseExpression(vars);
            if (!eat(')')) throw new IllegalArgumentException("Kurung tutup tidak ditemukan.");
            return x;
        }

        if ((ch >= '0' && ch <= '9') || ch == '.') {
            int start = this.pos;
            while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
            return Double.parseDouble(input.substring(start, this.pos));
        }

        if (Character.isLetter(ch)) {
            int start = this.pos;
            while (Character.isLetterOrDigit(ch) || ch == '_') nextChar();
            String name = input.substring(start, this.pos).toLowerCase();

            if (eat('(')) {
                double arg = parseExpression(vars);
                if (!eat(')')) throw new IllegalArgumentException("Kurung tutup fungsi tidak ditemukan.");
                return applyFunction(name, arg);
            }

            if (name.equals("pi")) return Math.PI;
            if (name.equals("e")) return Math.E;
            if (vars.containsKey(name)) return vars.get(name);
            throw new IllegalArgumentException("Variabel atau fungsi tidak dikenal: " + name);
        }

        throw new IllegalArgumentException("Ekspresi tidak valid.");
    }

    private double applyFunction(String name, double arg) {
        return switch (name) {
            case "sin" -> Math.sin(arg);
            case "cos" -> Math.cos(arg);
            case "tan" -> Math.tan(arg);
            case "sqrt" -> Math.sqrt(arg);
            case "abs" -> Math.abs(arg);
            case "exp" -> Math.exp(arg);
            case "ln" -> Math.log(arg);
            case "log" -> Math.log10(arg);
            default -> throw new IllegalArgumentException("Fungsi tidak dikenal: " + name);
        };
    }
}
