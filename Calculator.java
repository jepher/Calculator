package Calculator;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Calculator {
	private Stack<String> operators = new Stack<String>();
	
	private enum Operator {
		ADD(0), SUBTRACT(0), 
		MULTIPLY(1), DIVIDE(1), 
		EXPONENT(2), SQRT(2),
		SIN(3), COS(3), TAN(3), CSC(3), SEC(3), COT(3), LN(3);  
		
		final int precedence;

		Operator(int p) {
			precedence = p;
		}
	}

	public static Map<String, Operator> OPERATIONS = new HashMap<String, Operator>() {
		{
			put("+", Operator.ADD);
			put("-", Operator.SUBTRACT);
			put("*", Operator.MULTIPLY);
			put("/", Operator.DIVIDE);
			put("^", Operator.EXPONENT);
			put("sin", Operator.SIN);
			put("cos", Operator.COS);
			put("tan", Operator.TAN);
			put("csc", Operator.CSC);
			put("sec", Operator.SEC);
			put("cot", Operator.COT);
			put("sqrt", Operator.SQRT);
			put("ln", Operator.LN);
		}
	};
	
	public static Set<String> FUNCTIONS = new HashSet<String>() {
		{
			add("sin");
			add("cos");
			add("tan");
			add("csc");
			add("sec");
			add("cot");
			add("sqrt");
			add("ln");
		}
	};

	// top of stack > current operator: return true
	private boolean isHigherPrecedence(String op1, String op2) {
		return OPERATIONS.containsKey(op2) // check if top of operator from stack is a parenthesis
				&& (OPERATIONS.get(op1).precedence <= OPERATIONS.get(op2).precedence);
	}

	public String postfix(String infix) {
		StringBuilder output = new StringBuilder();
		
		for (String token : infix.split(" ")) {
			// operators
			if (OPERATIONS.containsKey(token)) {
				// if operator has higher precedence than top of operator stack,
				// pop operators onto the string
				while (!operators.isEmpty() && isHigherPrecedence(token, operators.peek())) {
					output.append(operators.pop() + " ");
				}
				operators.push(token);
			}
			// left parenthesis
			else if (token.equals("(")) {
				operators.push(token);
			}
			// right parenthesis: pop operators off operator stack until left
			// parenthesis is reached, then remove left parenthesis
			else if (token.equals(")")) {
				while (!operators.peek().equals("("))
					output.append(operators.pop()).append(' ');
				operators.pop();
			}
			// digits
			else {
				output.append(token).append(' ');
			}
		}

		// add remaining operators
		while (!operators.isEmpty()) {
			output.append(operators.pop() + " ");
		}

		return output.toString();
	}

	public double evaluate(String expression){
		//System.out.println(expression);
		String postfix = postfix(expression);
		//System.out.println(postfix);
		Stack<Double> digits = new Stack<Double>();
		
		for(String token : postfix.split(" ")){
			// digits
			if(!OPERATIONS.containsKey(token)){
				digits.push(Double.parseDouble(token));
			}
			// operators
			else{
				if(FUNCTIONS.contains(token)) // functions
					digits.push(calculate(digits.pop(), token));
				else if(digits.size() == 1 && token.equals("-")) // negative number
					digits.push(-digits.pop());
				else
					digits.push(calculate(digits.pop(), digits.pop(), token));
			}
		}
		
		// no * multiplication (ex: 3 ( 3 + 2 ))
		while(digits.size() > 1) {
			digits.push(digits.pop() * digits.pop());
		}
		return digits.pop();
	}
	
	public double calculate(double n2, double n1, String operator) {
		switch (operator) {
		case ("+"):
			return n1 + n2;
		case ("-"):
			return n1 - n2;
		case ("*"):
			return n1 * n2;
		case ("/"):
			return n1 / n2;
		case ("^"):
			return Math.pow(n1, n2);
		default:
			return 0;
		}
	}
	
	// function calculations
	public double calculate(double n, String operator){
		switch (operator) {
		case ("sin"):
			return Math.sin(n);
		case ("cos"):
			return Math.cos(n);
		case ("tan"):
			return Math.tan(n);
		case ("csc"):
			return 1 / Math.sin(n);
		case ("sec"):
			return 1 / Math.cos(n);
		case ("cot"):
			return 1 / Math.tan(n);
		case ("sqrt"):
			return Math.sqrt(n);
		case ("ln"):
			return Math.log(n);
		default:
			return 0;
		}
	}

	public static void main(String[] args) {
		Calculator test = new Calculator();
		
//		System.out.println(test.evaluate("cos ( 2 + 4 )")); // 0.96
//		System.out.println(test.evaluate("3 * ( 2 + 3 * ( 3 - 5 ) )")); // -12
//		System.out.println(test.evaluate("3 ( 3 + 2 )")); // 15
//		System.out.println(test.evaluate("3 sin 3")); // .42
//		System.out.println(test.evaluate("- 2 + 2")); // 0
//		System.out.println(test.evaluate("( 3 + 3 * ( 3 - 1 ) / ( 2 + 4 ) ) - ( 1 + 2 ) * ( ( ( 3 + 1 ) / 2 ) + 3 )")); // -11
//		System.out.println(test.evaluate("3 * 4 ^ 2 + 5")); // 53
//		System.out.println(test.evaluate("sin ( 3 )")); // -19
//		System.out.println(test.evaluate("2 ^ 3 - 3 * 2 - 5 + 7")); // 4
//		System.out.println(test.evaluate("sqrt 4"));
	}
}
