package Calculator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

//TO DO LIST
//functions: d to f
// inverse trig functions
// graphical formatting

public class GUI extends JFrame {
	private final int WINDOW_WIDTH = 500;
	private final int WINDOW_HEIGHT = 500;

	public static HashSet<String> functions = new HashSet<String>() {
		{
			add("sin");
			add("cos");
			add("tan");
			add("csc");
			add("sec");
			add("cot");
			add("ln");
			add("\u221A"); // sqrt
		}
	};
	public static HashSet<String> operators = new HashSet<String>() {
		{
			add("+");
			add("-");
			add("*");
			add("/");
			add("^");
		}
	};
	public static HashSet<String> constants = new HashSet<String>() {
		{
			add("\u03C0"); // pi
			add("e");
		}
	};
	public static HashSet<String> parentheses = new HashSet<String>() {
		{
			add("(");
			add(")");
		}
	};
	public static HashSet<String> noSpace = new HashSet<String>() {
		{
			add("^");
			add("(");
			add(")");
			add("sin");
			add("cos");
			add("tan");
			add("csc");
			add("sec");
			add("cot");
			add("ln");
		}
	};

	private JLabel emptyCell = new JLabel();

	private JButton num1;
	private JButton num2;
	private JButton num3;
	private JButton num4;
	private JButton num5;
	private JButton num6;
	private JButton num7;
	private JButton num8;
	private JButton num9;
	private JButton num0;

	private JButton add;
	private JButton subtract;
	private JButton multiply;
	private JButton divide;
	private JButton equals;
	private JButton delete;
	private JButton clear;
	private JButton decimal;
	private JButton leftParenthesis;
	private JButton rightParenthesis;
	private JButton exponent;
	private JButton decimalToFraction;
	private JButton sin;
	private JButton cos;
	private JButton tan;
	private JButton sqrt;
	private JButton ln;
	private JButton pi;
	private JButton e;

	private ArrayList<String> inputStack = new ArrayList<String>();
	private JTextArea result;

	private boolean calculationComplete = false; // check if equals was previously pressed
	private boolean startOfExpression = true; // determines if "-" is negative sign or subtraction operator, should it be pressed
	private Calculator calculator = new Calculator();

	public GUI() {
		setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		JPanel middle = new JPanel();
		middle.setLayout(new GridLayout(5, 3));
		middle.add(leftParenthesis = new JButton("("));
		middle.add(rightParenthesis = new JButton(")"));
		middle.add(clear = new JButton("C"));
		middle.add(num1 = new JButton("1"));
		middle.add(num2 = new JButton("2"));
		middle.add(num3 = new JButton("3"));
		middle.add(num4 = new JButton("4"));
		middle.add(num5 = new JButton("5"));
		middle.add(num6 = new JButton("6"));
		middle.add(num7 = new JButton("7"));
		middle.add(num8 = new JButton("8"));
		middle.add(num9 = new JButton("9"));
		middle.add(decimal = new JButton("."));
		middle.add(num0 = new JButton("0"));
		middle.add(equals = new JButton("="));

		JPanel right = new JPanel();
		right.setLayout(new GridLayout(5, 1));
		right.add(delete = new JButton("DEL"));
		right.add(add = new JButton("+"));
		right.add(subtract = new JButton("-"));
		right.add(multiply = new JButton("x"));
		right.add(divide = new JButton("÷"));

		JPanel left = new JPanel();
		left.setLayout(new GridLayout(5, 2));
		left.add(emptyCell);
		left.add(decimalToFraction = new JButton("D \u2192 F"));
		left.add(sin = new JButton("sin"));
		left.add(exponent = new JButton("^"));
		left.add(cos = new JButton("cos"));
		left.add(sqrt = new JButton("\u221A"));
		left.add(tan = new JButton("tan"));
		left.add(ln = new JButton("ln"));
		left.add(pi = new JButton("\u03C0"));
		left.add(e = new JButton("e"));

		JPanel display = new JPanel();
		display.add(result = new JTextArea());
		result.setVisible(true);
		result.setFont(new Font("Dialog", Font.PLAIN, 25));
		result.requestFocusInWindow();
		result.setEditable(false);
		result.setFocusable(true);
		result.setText("");

		result.addKeyListener(new KeyListener() {
			boolean shiftPressed = false; // keep track of shift key

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SHIFT)
					shiftPressed = true;
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
					int last = inputStack.size() - 1;
					if (inputStack.isEmpty())
						return;
					else if (functions.contains(inputStack.get(last)) || operators.contains(inputStack.get(last))) { // operation
						inputStack.remove(last);
					} else { // delete last digit
						inputStack.set(last, inputStack.get(last).substring(0, inputStack.get(last).length() - 1));
					}

					if (inputStack.get(last).isEmpty())
						inputStack.remove(last);
					updateScreen();
				}
				if (e.getKeyCode() == KeyEvent.VK_MINUS) {
					if (startOfExpression) {
						addDigit("-");
						startOfExpression = false;
					} else
						addOperator("-");
				}
				if (e.getKeyCode() == KeyEvent.VK_SLASH) {
					addOperator("/");
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_EQUALS) {
					if (shiftPressed && e.getKeyCode() == KeyEvent.VK_EQUALS)
						addOperator("+");
					else {
						calculationComplete = true;
						try {
							String infixStr = "";
							for (String token : inputStack) {
								infixStr += token + " ";
							}
							inputStack.clear();
							infixStr = infixStr.trim();
							inputStack.add(Double.toString(calculator.evaluate(infixStr)));
							updateScreen();
						} catch (Exception x) {
							inputStack.clear();
							result.setText("ERROR");
						}
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_PERIOD) {
					addDigit(".");
				}
				if (e.getKeyCode() == KeyEvent.VK_1) {
					addDigit("1");
				}
				if (e.getKeyCode() == KeyEvent.VK_2) {
					addDigit("2");
				}
				if (e.getKeyCode() == KeyEvent.VK_3) {
					addDigit("3");
				}
				if (e.getKeyCode() == KeyEvent.VK_4) {
					addDigit("4");
				}
				if (e.getKeyCode() == KeyEvent.VK_5) {
					addDigit("5");
				}
				if (e.getKeyCode() == KeyEvent.VK_6) {
					addDigit("6");
				}
				if (e.getKeyCode() == KeyEvent.VK_7) {
					addDigit("7");
				}
				if (e.getKeyCode() == KeyEvent.VK_8) {
					if (shiftPressed) // multiply
						addOperator("*");
					else // 8
						addDigit("8");
				}
				if (e.getKeyCode() == KeyEvent.VK_9) {
					if(shiftPressed) // left parenthesis
						addParenthesis("(");
					else // 9
						addDigit("9");
				}
				if (e.getKeyCode() == KeyEvent.VK_0) {
					if(shiftPressed) // right parenthesis
						addParenthesis(")");
					else // 0
						addDigit("0");
				}
				if (e.getKeyCode() == KeyEvent.VK_S) {
					addFunction("sin");
				}
				if (e.getKeyCode() == KeyEvent.VK_C) {
					addFunction("cos");
				}
				if (e.getKeyCode() == KeyEvent.VK_T) {
					addFunction("tan");
				}
				if (e.getKeyCode() == KeyEvent.VK_P) {
					addConstant("\u03C0");
				}
				if (e.getKeyCode() == KeyEvent.VK_E) {
					addConstant("e");
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SHIFT)
					shiftPressed = false;
			}

			@Override
			public void keyTyped(KeyEvent e) {

			}

		});

		JPanel GUI = new JPanel();
		GUI.setLayout(new BorderLayout());
		GUI.add(display, BorderLayout.NORTH);
		GUI.add(middle, BorderLayout.CENTER);
		GUI.add(right, BorderLayout.EAST);
		GUI.add(left, BorderLayout.WEST);
		add(GUI);

		delete.addActionListener(new ListenToDelete());
		clear.addActionListener(new ListenToClear());
		equals.addActionListener(new ListenToEquals());

		decimal.addActionListener(new ListenToDecimal());
		num1.addActionListener(new ListenToOne());
		num2.addActionListener(new ListenToTwo());
		num3.addActionListener(new ListenToThree());
		num4.addActionListener(new ListenToFour());
		num5.addActionListener(new ListenToFive());
		num6.addActionListener(new ListenToSix());
		num7.addActionListener(new ListenToSeven());
		num8.addActionListener(new ListenToEight());
		num9.addActionListener(new ListenToNine());
		num0.addActionListener(new ListenToZero());

		add.addActionListener(new ListenToAdd());
		subtract.addActionListener(new ListenToSubtract());
		multiply.addActionListener(new ListenToMultiply());
		divide.addActionListener(new ListenToDivide());
		exponent.addActionListener(new ListenToExponent());

		leftParenthesis.addActionListener(new ListenToLeftParenthesis());
		rightParenthesis.addActionListener(new ListenToRightParenthesis());

		sin.addActionListener(new ListenToSin());
		cos.addActionListener(new ListenToCos());
		tan.addActionListener(new ListenToTan());
		sqrt.addActionListener(new ListenToSqrt());
		ln.addActionListener(new ListenToLn());
		pi.addActionListener(new ListenToPi());
		e.addActionListener(new ListenToE());
	}

	class ListenToDelete implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int last = inputStack.size() - 1;
			if (inputStack.isEmpty())
				return;
			else if (functions.contains(inputStack.get(last)) || operators.contains(inputStack.get(last))) { // operation
				inputStack.remove(last);
			} else { // delete last digit
				inputStack.set(last, inputStack.get(last).substring(0, inputStack.get(last).length() - 1));
			}

			if (inputStack.get(last).isEmpty())
				inputStack.remove(last);
			updateScreen();
		}
	}

	class ListenToClear implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			inputStack.clear();
			updateScreen();
			calculationComplete = false;
			startOfExpression = true;
		}
	}

	class ListenToDecimal implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addDigit(".");
		}
	}

	class ListenToOne implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addDigit("1");
		}
	}

	class ListenToTwo implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addDigit("2");
		}
	}

	class ListenToThree implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addDigit("3");
		}
	}

	class ListenToFour implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addDigit("4");
		}
	}

	class ListenToFive implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addDigit("5");
		}
	}

	class ListenToSix implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addDigit("6");
		}
	}

	class ListenToSeven implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addDigit("7");
		}
	}

	class ListenToEight implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addDigit("8");
		}
	}

	class ListenToNine implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addDigit("9");
		}
	}

	class ListenToZero implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addDigit("0");
		}
	}

	class ListenToAdd implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addOperator("+");
		}
	}

	class ListenToSubtract implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addOperator("-");
		}
	}

	class ListenToMultiply implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addOperator("*");
		}
	}

	class ListenToDivide implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addOperator("/");
		}
	}

	class ListenToExponent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addOperator("^");
		}
	}

	class ListenToLeftParenthesis implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addParenthesis("(");
		}
	}

	class ListenToRightParenthesis implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addParenthesis(")");
		}
	}

	class ListenToSin implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addFunction("sin");
		}
	}

	class ListenToCos implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addFunction("cos");
		}
	}

	class ListenToTan implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addFunction("tan");
		}
	}

	class ListenToSqrt implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addFunction("\u221A");
		}
	}

	class ListenToLn implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addFunction("ln");
		}
	}
	
	class ListenToPi implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addConstant("\u03C0");
		}
	}
	
	class ListenToE implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addConstant("e");
		}
	}

	class ListenToDecimalFractionConverter implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				evaluate();
				String decimal = inputStack.remove(0);
				
				//count decimal places
				int decimalCount = 0;
				for(int start = decimal.indexOf(".") + 1; start < decimal.length(); start++)
					decimalCount++;
				
			} catch (Exception x) {
				inputStack.clear();
				result.setText("ERROR");
			}
		}
	}
	
	class ListenToEquals implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			calculationComplete = true;
			try {
				evaluate();
				updateScreen();
			} catch (Exception x) {
				inputStack.clear();
				result.setText("ERROR");
			}
		}
	}

	public void evaluate() {
		String infixStr = "";
		for (String token : inputStack) {
			if (token.equals("\u221A")) // change sqrt symbol
				token = "sqrt";
			else if (token.equals("\u03C0")) // change pi
				token = Double.toString(Math.PI);
			else if (token.equals("e")) // change e
				token = Double.toString(Math.E);
			infixStr += token + " ";
		}
		inputStack.clear();
		infixStr = infixStr.trim();
		inputStack.add(Double.toString(calculator.evaluate(infixStr)));
	}
	
	public void addDigit(String digit) {
		int last = inputStack.size() - 1;
		if (calculationComplete) {
			reset();
		}
		if (inputStack.isEmpty() || // empty stack
				operators.contains(inputStack.get(last)) || 
				functions.contains(inputStack.get(last)) || 
				parentheses.contains(inputStack.get(last)) ||
				constants.contains(inputStack.get(last))) 
		{ 
			inputStack.add(digit);
		} else {
			inputStack.set(last, inputStack.get(last) + digit);
		}
		
		calculationComplete = false;
		startOfExpression = false;
		updateScreen();
	}

	public void addOperator(String operator) {
		int last = inputStack.size() - 1;
		if (startOfExpression && operator.equals("-")) // negative number
			inputStack.add(operator);
		else if (inputStack.isEmpty() || functions.contains(inputStack.get(last)) || inputStack.get(last).equals("(")) { // invalid input
			return;
		} else if (!operators.contains(inputStack.get(last)) && !functions.contains(inputStack.get(last))) { // last input was a digit
			inputStack.add(operator);
		} else { // change last operator to current operator
			inputStack.set(last, operator);
		}
		
		if (calculationComplete) // continue calculations after pressing equals
			calculationComplete = false;

		updateScreen();
	}

	public void addFunction(String function) {
		int last = inputStack.size() - 1;
		if (calculationComplete) { // new calculation
			inputStack.clear();
			calculationComplete = false;
		} 
		
		inputStack.add(function);
		inputStack.add("(");
		startOfExpression = true;

		updateScreen();
	}
	
	public void addConstant(String constant) {
		if(calculationComplete) {
			reset();
		}
		inputStack.add(constant);
		
		updateScreen();
	}
	
	public void addParenthesis(String parenthesis) {
		if(parenthesis.equals("("))
			startOfExpression = true;
		
		inputStack.add(parenthesis);
		
		updateScreen();
	}

	public void reset() {
		inputStack.clear();
		startOfExpression = true;
		calculationComplete = false;
	}

	public void updateScreen() {
		String displayStr = "";
		
		for(int i = 0; i < inputStack.size(); i++) {
			String token = inputStack.get(i);
			
			if (noSpace.contains(token)) {
				if (!token.equals("("))
					displayStr = displayStr.trim();
				displayStr += token;
			} else if(constants.contains(token)) {
				if(i > 0 && !operators.contains(inputStack.get(i - 1))) // last token was a number or functions
					displayStr = displayStr.trim();
				if(i < inputStack.size() - 1 && !operators.contains(inputStack.get(i + 1))) // next token is a number or function
					displayStr += token;
				else
					displayStr += token;
			} else
				displayStr += token + " ";
		}
		
		result.setText(displayStr);
	}

	public static void main(String[] args) {
		GUI calc = new GUI();
		// calc.pack();
		calc.setLocationRelativeTo(null);
		calc.setVisible(true);
		calc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
