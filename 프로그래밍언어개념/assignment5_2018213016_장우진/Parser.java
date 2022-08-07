import java.io.IOException;
import java.util.*;

public class Parser {
    // Recursive descent parser that inputs a C++Lite program and 
    // generates its abstract syntax.  Each method corresponds to
    // a concrete syntax grammar rule, which appears as a comment
    // at the beginning of the method.
  
    Token token;          // current token from the input stream
    Lexer lexer;
  
    String returnplace;
    public Parser(Lexer ts) { // Open the C++Lite source program
        lexer = ts;                          // as a token stream, and
        token = lexer.next();            // retrieve its first Token
    }
  
    private String match (TokenType t) { //�ش� ��ū�� �´��� üũ�ϰ� ������ ���� ��ū���� �Ѿ �ƴϸ� ���� �߻�
        String value = token.value(); //String���� Ÿ���� value����
        if (token.type().equals(t)) 
            token = lexer.next(); //���� ��ū���� �Ѿ��
        else
            error(t);	//Ʋ���� error�Լ� ����
        return value;
    }
   
    
    private void error(TokenType tok) { //���� �޼��� �߰��ϴ� �Լ�
        System.err.println("Syntax error: expecting: " + tok 
                           + "; saw: " + token);
        System.exit(1);
    }
  
    private void error(String tok) { //���� �޼��� �߰��ϴ� �Լ�
        System.err.println("Syntax error: expecting: " + tok 
                           + "; saw: " + token);
        System.exit(1);
    }
    
    public Program program() {
    	// Program -> { Type Identifier FunctionOrGlobal } MainFuction
    	Functions fs = new Functions(); //FunctionOrGlobal���� �����ϴ� �迭 ��ü ����
    	Declarations g = new Declarations();
    	
    	while(!(token.type().equals(TokenType.Eof))) { //EoF�� ���ö����� �ݺ�
    		Type T = new Type(match(token.type()));
    		String id;
    		
    		if(token.type().equals(TokenType.Main)) { //mainfunction
    			id = match(TokenType.Main);
                fs.add(mainfunction(T, id));
    		}
    		else if(token.type().equals(TokenType.Identifier)){
    			id = match(TokenType.Identifier);
    			if (token.type().equals(TokenType.LeftParen)) { //function
    				fs.add(function(T, id)); 
    			}
    			else  { // Global
    				globals(g,T,id);
    			}
    		}
        	else {
        		error("FuctionOrGlobal error");
        	}
    	}
    	
    	
    	return new Program(g, fs);
    }
        
    
    public void globals(Declarations g, Type t, String id){
    	//Global -> {, Identifier }
        Variable V = new Variable(id);    
    	g.add(new Declaration(V,t)); //declartion Ŭ������ v,t�� ���� �־� ��ü�� ������ �װ� declations�� D�迭�� �߰�
    	
    	while(token.type().equals(TokenType.Comma)) { //���� ','�� �����ϸ� �˻� ���ϱ�
    		match(TokenType.Comma);
    		V = new Variable(match(TokenType.Identifier));
    		g.add(new Declaration(V,t));
    	}
    	
    	match(TokenType.Semicolon); //���� �������� ';'�˻�
    	
    }
    
    
    public Function function(Type t, String id){
    	returnplace = id;
    	match(TokenType.LeftParen);
        Declarations params = parameter();
        match(TokenType.RightParen);
        match(TokenType.LeftBrace);
        Declarations locals = declarations();
        Block body = statements();
        match(TokenType.RightBrace);
        return new Function(t,id,params,locals,body); 
    }
    
    public Function mainfunction(Type t, String id) {
    	// MainFuction --> int main ( ) '{' Declarations Statements '}'
    	
    	TokenType[ ] header = {TokenType.LeftParen, TokenType.RightParen};
        for (int i=0; i<header.length; i++)   // bypass "int main ( )"
            match(header[i]);
      
        // student exercise
        match(TokenType.LeftBrace); // '{'�� �ִ��� match�Լ��� ���� �˻�
        Declarations params = new Declarations();
        Declarations locals = declarations();
        Block body = statements();
        match(TokenType.RightBrace); // '}'�� �ִ��� match�Լ��� ���� �˻�
        return new Function(t,id,params,locals,body);  // student exercise
    }
  
    
    public Declarations parameter() {
    	Declarations P=new Declarations();
    	if(isType()) {
    		Type T = new Type(match(token.type())); //Type�� ������ T���� �־��ֱ�
    		Variable V = new Variable(match(TokenType.Identifier)); //V���� identifier�� �־��ֱ�
        	P.add(new Declaration(V,T)); //declartion Ŭ������ v,t�� ���� �־� ��ü�� ������ �װ� declations�� D�迭�� �߰�
        	
        	while(token.type().equals(TokenType.Comma)) { //���� ','�� �����ϸ� �˻� ���ϱ�
        		match(TokenType.Comma);
        		T = new Type(match(token.type()));
        		V = new Variable(match(TokenType.Identifier));
        		P.add(new Declaration(V,T));
        	}
    		
    		
    	}
    	else {	error("Parameters error");	} //�Ķ��Ÿ�� �������� �ʴ� ���
    	
    	return P;
    	
    }
    
    
    
    private Declarations declarations () {
        // Declarations --> { Declaration }
    	Declarations D = new Declarations();
    	while(isType()) { //type�� ���� ������ �ݺ�
    		declaration(D);
    	}
        return D;  // student exercise
    }
  
    private void declaration (Declarations ds) {
        // Declaration  --> Type Identifier { , Identifier } ;
        // student exercise
    	Type T = null;
    	if(isType()) {
    		T = new Type(match(token.type())); //Type�� ������ T���� �־��ֱ�
    	}
    	else {	error("declartion-type");	} //������ type�� ���� ������� ���
    	
    	Variable V = new Variable(match(TokenType.Identifier)); //V���� identifier�� �־��ֱ�
    	ds.add(new Declaration(V,T)); //declartion Ŭ������ v,t�� ���� �־� ��ü�� ������ �װ� declations�� D�迭�� �߰�
    	
    	while(token.type().equals(TokenType.Comma)) { //���� ','�� �����ϸ� �˻� ���ϱ�
    		match(TokenType.Comma);
    		V = new Variable(match(TokenType.Identifier));
    		ds.add(new Declaration(V,T));
    	}
    	
    	match(TokenType.Semicolon); //���� �������� ';'�˻�
    	
    }

  
    private Type type() {
        // Type  -->  int | bool | float | char 
        Type t = null;
        // student exercise
        t = new Type(match(token.type())); //type�˻縦 �ϰ� TypeŬ���� id�� �� ����
        return t;          
    }
  
    
    private Block statements () {
        // Block --> '{' Statements '}'
        Block b = new Block();
        // student exercise
       while(!token.type().equals(TokenType.RightBrace)) { //���� ���������� �ݺ��ϱ�
    	   b.members.add(statement()); //statement()�Լ��� �����ϰ� �� ��ȯ���� ArrayList<Statement> members�� �߰�
       }
        return b;
    }
    
    
    private Statement statement() {
        // Statement --> ; | Block | Assignment | IfStatement | WhileStatement  
    	
    	//| CallStatement | ReturnStatement

        Statement s = new Skip();
		
        // student exercise
        if (token.type().equals(TokenType.Semicolon)) { //';'�϶� �˻�
            match(TokenType.Semicolon);
        }
        else if (token.type().equals(TokenType.Return)) { 
            s = Returnstatement(); 
        }
        else if (token.type().equals(TokenType.LeftBrace)) { // ���� ��� '{'�� ������ �� �˻�
            match(TokenType.LeftBrace);
            s = statements(); //statements()�Լ��� �����ϰ� �װ���� s�� ����
            match(TokenType.RightBrace); // '}' �˻�
        }
        
        else if (token.type().equals(TokenType.Identifier)) { // ���� �����̸��� ������ �� �˻�
        	Variable V =new Variable(match(TokenType.Identifier)); //�Լ�ȣ������ �� �Ҵ����� �����ϱ� ���� assignment�� match(Identifier)�� ���⼭ ��)
        	
        	if(token.type().equals(TokenType.Assign)) {
        		s = assignment(V); //assignment()�Լ��� �����ϰ� �װ���� s�� ����
        	}
        	else if(token.type().equals(TokenType.LeftParen)) {
        		s = callstatement(V);
        	}
        }
        else if (token.type().equals(TokenType.If)) { // ���� 'if'�� ������ �� �˻�
            s = ifStatement(); //ifStatement()�Լ��� �����ϰ� �װ���� s�� ����
        }
        else if (token.type().equals(TokenType.While)) { // ���� 'while'�� ������ �� �˻�
            s = whileStatement(); //whileStatement()�Լ��� �����ϰ� �װ���� s�� ����
        }
        else {
            error("statement"); //���� �� �ƴϸ� statement������� ���
        }
        return s;
    }
  
    private Callstatement callstatement(Variable V) {
    	
    	String N = V.toString();
    	match(TokenType.LeftParen);
    	Expressions A = new Expressions();
    	
    	
    	while(!(token.type().equals(TokenType.RightParen))){
            A.add(expression());
            if (!token.type().equals(TokenType.RightParen))
                match(TokenType.Comma);
        }
    	
    	
    	match(TokenType.RightParen);
    	match(TokenType.Semicolon);
    	return new Callstatement(N,A);
    }
    
    private Return Returnstatement() {
    	match(TokenType.Return);
    	Expression r= expression();
    	match(TokenType.Semicolon);
    	return new Return(new Variable(returnplace),r);
    }
    
    private Assignment assignment (Variable V) {
        // Assignment --> Identifier = Expression ;
		match(TokenType.Assign); //�״��� '=' �� �ִ��� �˻�
		Expression E = expression(); //�״��� expression() �Լ��� ����
		match(TokenType.Semicolon); //�������� ';'�˻�
		
		Assignment A = new Assignment(V,E); //�׸��� assignment Ŭ������ target,source�� �� ����
        return A;  // student exercise
    }
  
    private Conditional ifStatement () {
        // IfStatement --> if ( Expression ) Statement [ else Statement ]
    	match(TokenType.If); //���� 'if'�� �ִ��� �˻�
		match(TokenType.LeftParen); //�״��� '('�� �ִ��� �˻�
        Expression e = expression(); //�״��� expression�˻�
        match(TokenType.RightParen); // �� ���� ')'�˻�

		Statement S = statement();  //�� ���� statement�˻�
		
		Conditional C; //conditional Ŭ������ ���� ��ü�� ������ ���ǿ� ���� �����ε��� �̿��� ������ ȣ��
		if(token.type().equals(TokenType.Else)){ //else�� ������ ��
			match(TokenType.Else);
			
			Statement S2 = statement();  //else�� statement���� �˻���
			
			C = new Conditional(e,S,S2);	//conditionalŬ������ test,thenbranch,elsebranch�� �� ����
		}
		else { C = new Conditional(e,S); } //else�� ������ �׳� test,thenbranch���� �� ����

        return C;  // student exercise
    }
  
    private Loop whileStatement () {
        // WhileStatement --> while ( Expression ) Statement
    	match(TokenType.While); //���� while�� �ִ��� �˻�
		match(TokenType.LeftParen); //�״��� '('�� �ִ��� �˻�
        Expression e = expression(); //�״��� expression�˻�
        match(TokenType.RightParen); // �� ���� ')'�˻�

    	Statement S = statement();  //�� ���� statement�˻�
	    	
    	Loop L = new Loop(e,S); //loopŬ������ test�� body�� �� ����

        return L;  // student exercise
    }

    private Expression expression () {
        // Expression --> Conjunction { || Conjunction }
    	Expression e = conjunction(); //conjunction�Լ��� �������� ����
    	while(token.type().equals(TokenType.Or)) { //���� '||'�� ���� �� �˻�
    		 Operator op = new Operator(match(TokenType.Or)); //operatorŬ������ val�� �� ����
             Expression term2 = conjunction(); //�ι�° conjunction�Լ� ������ ����
             e = new Binary(op, e, term2); //BinaryŬ������ op,term1, term2�� �� ����
    	}
        return e;  // student exercise
 
    }
  
    private Expression conjunction () {
        // Conjunction --> Equality { && Equality }
    	Expression e = equality(); //equality�Լ��� �������� ����
    	while(token.type().equals(TokenType.And)) { //���� '&&'�� ���� �� �˻�
    		 Operator op = new Operator(match(TokenType.And)); //operatorŬ������ val�� �� ����
             Expression term2 = equality(); //�ι�° equality�Լ��� �������� ����
             e = new Binary(op, e, term2); //BinaryŬ������ op,term1, term2�� �� ����
    	}
        return e;  // student exercise
    }
  
    private Expression equality () {
        // Equality --> Relation [ EquOp Relation ]
    	Expression e = relation(); //relation�Լ��� �������� ����
    	while(isEqualityOp()) { //���� ����񱳿�����(==)�� ���� �� �˻�
    		 Operator op = new Operator(match(TokenType.Equals)); //operatorŬ������ val�� �� ����
             Expression term2 = relation(); //�ι�° relation�Լ��� �������� ����
             e = new Binary(op, e, term2); //BinaryŬ������ op,term1, term2�� �� ����
    	}
        return e;  // student exercise
    }

    private Expression relation (){
        // Relation --> Addition [RelOp Addition] 
    	Expression e = addition(); //addition�Լ��� �������� ����
    	while(isRelationalOp()) { //���� �񱳿�����(>)�� ���� �� �˻�
    		Operator op = new Operator(match(token.type())); //operatorŬ������ val�� �� ����
    		Expression term2 = addition(); //�ι�° addition�Լ��� �������� ����
    		e = new Binary(op,e,term2); //BinaryŬ������ op,term1, term2�� �� ����
    	} 
        return e;  // student exercise
    }
  
    private Expression addition () {
        // Addition --> Term { AddOp Term }
        Expression e = term(); //term�Լ��� �������� ����
        while (isAddOp()) { //���� ����������(+)�� ���� �� �˻�
            Operator op = new Operator(match(token.type())); //operatorŬ������ val�� �� ����
            Expression term2 = term(); //�ι�° term�Լ��� �������� ����
            e = new Binary(op, e, term2); //BinaryŬ������ op,term1, term2�� �� ����
        }
        return e;
    }
  
    private Expression term () {
        // Term --> Factor { MultiplyOp Factor }
        Expression e = factor(); //factor�Լ��� �������� ����
        while (isMultiplyOp()) { //���� ����,������ �����ڰ� ���� �� �˻�
            Operator op = new Operator(match(token.type())); //operatorŬ������ val�� �� ����
            Expression term2 = factor(); //�ι�° factor�Լ��� �������� ����
            e = new Binary(op, e, term2); //BinaryŬ������ op,term1, term2�� �� ����
        }
        return e;
    }
  
    private Expression factor() {
        // Factor --> [ UnaryOp ] Primary 
        if (isUnaryOp()) { //���� '!'�� '-'�� ������
            Operator op = new Operator(match(token.type())); //operatorŬ������ val�� �� ����
            
            Expression term = primary(); //primary�Լ� ������ ����
            return new Unary(op, term); //UnaryŬ������ op,term�� �� ����
        }
        else return primary(); //���ٸ� �׳� primary�Լ� �������� ����
    }
  
    private Expression primary () {
        // Primary --> Identifier | Literal | ( Expression )
        //             | Type ( Expression )
        Expression e = null;
        if (token.type().equals(TokenType.Identifier)) { //���� ���� �̸��� ���� ��
        	String n = match(TokenType.Identifier); //�����̸� ����
        
        	if(token.type().equals(TokenType.LeftParen)){
                match(TokenType.LeftParen);
                Expressions args = new Expressions();
                while(!(token.type().equals(TokenType.RightParen))){
                    args.add(expression());
                    if (!token.type().equals(TokenType.RightParen))
                        match(TokenType.Comma);
                }
                match(TokenType.RightParen);
                e = new Call(n,args);
        	}
        	else {
                e = new Variable(n);
            }
        
        
        } else if (isLiteral()) {
            e = literal();
        } else if (token.type().equals(TokenType.LeftParen)) { //���� '('�� ���� ��
            token = lexer.next(); //match�� ���� ������ next()�� �̿��� token�� �����ɷ� �ѱ��
            e = expression();      //expression�Լ� ��� ���� 
            match(TokenType.RightParen); // ')'�˻�
        } else if (isType( )) { //���� type�� ���� �� �˻�(����ȯ)
            Operator op = new Operator(match(token.type()));
            match(TokenType.LeftParen); //'(' �˻�
            Expression term = expression(); //expression�Լ� ��� ����
            match(TokenType.RightParen); //')' �˻�
            e = new Unary(op, term); //UnaryŬ������ op,term�� �� ����
        } else error("Identifier | Literal | ( | Type"); //�ƹ��͵� �ƴ� �� ���� �޼��� ���
        return e;
    }

    private Value literal( ) {
    	Value V = null;
    	if(token.type().equals(TokenType.IntLiteral)) { //int�� ���� ��
    		V = new IntValue(Integer.parseInt(match(TokenType.IntLiteral)));
    	}
    	else if(token.type().equals(TokenType.FloatLiteral)) { //Float�� ���� ��
    		V = new FloatValue(Float.valueOf(match(TokenType.FloatLiteral)));
    	}
    	else if(token.type().equals(TokenType.CharLiteral)) { //Char�� ���� ��
    		V = new CharValue(match(TokenType.CharLiteral).charAt(0));
    	}
    	else if(token.type().equals(TokenType.Bool)) { //bool������ ���� ��
    		V = new BoolValue(Boolean.parseBoolean(match(TokenType.IntLiteral)));
    	}
    	else if(token.type().equals(TokenType.True)) { //true���� ���� ��
    		V = new BoolValue(Boolean.parseBoolean(match(TokenType.True)));
    	}
    	else if(token.type().equals(TokenType.False)) { //false���� ���� ��
    		V = new BoolValue(Boolean.parseBoolean(match(TokenType.False)));
    	}
        return V;  // student exercise
    }
  

    private boolean isAddOp( ) { // +,-�����ڸ� ����
        return token.type().equals(TokenType.Plus) ||
               token.type().equals(TokenType.Minus);
    }
    
    private boolean isMultiplyOp( ) { // *,/ �����ڸ� ����
        return token.type().equals(TokenType.Multiply) ||
               token.type().equals(TokenType.Divide);
    }
    
    private boolean isUnaryOp( ) { // !, -(��ȣ) �����ڸ� ����
        return token.type().equals(TokenType.Not) ||
               token.type().equals(TokenType.Minus);
    }
    
    private boolean isEqualityOp( ) { // ==, != �����ڸ� ����
        return token.type().equals(TokenType.Equals) ||
            token.type().equals(TokenType.NotEqual);
    }
    
    private boolean isRelationalOp( ) { // <, <=, >, >= �����ڸ� ����
        return token.type().equals(TokenType.Less) ||
               token.type().equals(TokenType.LessEqual) || 
               token.type().equals(TokenType.Greater) ||
               token.type().equals(TokenType.GreaterEqual);
    }
    
    private boolean isType( ) { // int, float, char, bool Ÿ���� ����
        return token.type().equals(TokenType.Int)
            || token.type().equals(TokenType.Bool) 
            || token.type().equals(TokenType.Float)
            || token.type().equals(TokenType.Char);
    }
    
    private boolean isLiteral( ) {  //int,float,char�� ���� isBooleanLiteral() �� ����
        return token.type().equals(TokenType.IntLiteral) ||
            isBooleanLiteral() ||
            token.type().equals(TokenType.FloatLiteral) ||
            token.type().equals(TokenType.CharLiteral);
    }
    
    private boolean isBooleanLiteral( ) { //true�� false�� ����
        return token.type().equals(TokenType.True) ||
            token.type().equals(TokenType.False);
    }
    
    public static void main(String args[]) throws IOException {
        Parser parser  = new Parser(new Lexer(args[0]));
        Program prog = parser.program();
		prog.display(args[0]);
		
    } //main

} // Parser
